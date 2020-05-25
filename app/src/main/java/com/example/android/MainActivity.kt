package com.example.android

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.android.recycler_cities.CityAdapter
import com.example.android.response.WeatherResponse
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope(),
    SearchView.OnQueryTextListener {

    companion object {

        private var PERMISSION_REQUEST_CODE = 1
        private var wayLatitude: Double = 0.0
        private var wayLongitude: Double = 0.0
    }

    @Inject
    @Suppress("LateinitUsage")
    lateinit var service: WeatherService

    @Inject
    @Suppress("LateinitUsage")
    lateinit var mFusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        MyApp().plusFscComponent(this).inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (toolbar as Toolbar).also {
            setSupportActionBar(it)
        }
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
            1
        )
    }


    private fun setupRecyclerWithCities() {
        launch {
            delay(OtherConstants.delay)
            val response = withContext(Dispatchers.IO) {
                service.weatherInNearbyCities(
                    wayLongitude,
                    wayLatitude,
                    OtherConstants.numberOfCities
                )
            }
            if (response.body()?.list?.size != 1) {
                rv_cities.adapter =
                    CityAdapter(response.body()?.list ?: LinkedList<WeatherResponse>())
                    { weatherResponse ->
                        startActivity(
                            CityActivity.createIntent(
                                this@MainActivity,
                                weatherResponse.id
                            )
                        )
                    }
            }
        }
    }

    private fun setLocation() {
        mFusedLocationClient.lastLocation.addOnSuccessListener(this) {
            it?.let {
                wayLatitude = it.latitude
                wayLongitude = it.longitude
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    this.onResume()
                } else this.onStop()
            }
        }
    }

        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.menu_main, menu)
            val searchItem: MenuItem? = menu?.findItem(R.id.action_search)
            val searchView = searchItem?.actionView as SearchView
            searchView.setOnQueryTextListener(this)
            searchView.queryHint = "Enter the name of the city"
            return true
        }

        override fun onDestroy() {
            super.onDestroy()
            coroutineContext.cancelChildren()
            MyApp().clearFSCComponent()
        }

        override fun onQueryTextSubmit(query: String?): Boolean {
            if (query != null) {
                launch {
                    try {
                        val response = withContext(Dispatchers.IO) {
                            service.weatherByName(query)
                        }
                        val intent = Intent(this@MainActivity, CityActivity::class.java)
                        intent.putExtra(CityActivity.CITY_ID, response.id)
                        startActivity(intent)
                    } catch (e: retrofit2.HttpException) {
                        Snackbar.make(
                            findViewById(android.R.id.content),
                            "No such city",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean = true
    }

