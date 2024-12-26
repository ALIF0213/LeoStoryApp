package com.example.leostoryapp.ui.maps

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.leostoryapp.R
import com.example.leostoryapp.data.pref.UserPreference
import com.example.leostoryapp.data.remote.response.ListStoryItem
import com.example.leostoryapp.data.remote.retrofit.ApiConfig
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var progressBar: ProgressBar

    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        progressBar = findViewById(R.id.progressBar)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        val uiSettings = googleMap.uiSettings
        uiSettings.isZoomControlsEnabled = true
        uiSettings.isCompassEnabled = true
        uiSettings.isMyLocationButtonEnabled = true
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val userLocation = LatLng(location.latitude, location.longitude)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
            } else {
                val defaultLocation = LatLng(-2.548926, 118.014863)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 5f))
            }
        }

        val userPreference = UserPreference(this)
        val token = userPreference.getToken()

        if (!token.isNullOrEmpty()) {
            fetchStoriesWithLocation(token)
        } else {
            Toast.makeText(this, "Token not found, please login again", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchStoriesWithLocation(token: String) {
        val apiService = ApiConfig.instance
        progressBar.visibility = ProgressBar.VISIBLE

        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.getStories("Bearer $token", 1,20, 1)
                }

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        // Ensure the list is non-null before passing it
                        val stories = response.body()?.listStory ?: emptyList()
                        addMarkersToMap(stories)
                    } else {
                        Toast.makeText(this@MapsActivity, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MapsActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } finally {
                progressBar.visibility = ProgressBar.GONE
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate menu dari file XML
        menuInflater.inflate(R.menu.menu_map, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.normal_type -> {
                googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }
            R.id.satellite_type -> {
                googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }
            R.id.terrain_type -> {
                googleMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            }
            R.id.hybrid_type -> {
                googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun addMarkersToMap(stories: List<ListStoryItem>) {
        for (story in stories) {
            if (story.lat != null && story.lon != null) {
                val location = LatLng(story.lat, story.lon)
                googleMap.addMarker(
                    MarkerOptions()
                        .position(location)
                        .title(story.name)
                        .snippet(story.description)
                )
            }
        }
    }
}

