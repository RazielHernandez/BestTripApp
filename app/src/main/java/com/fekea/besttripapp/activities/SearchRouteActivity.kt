package com.fekea.besttripapp.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.fekea.besttripapp.R
import com.fekea.besttripapp.dataModel.TravelLocation
import com.fekea.besttripapp.dataModel.TravelRoute
import com.fekea.besttripapp.databinding.ActivitySearchBinding
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.maps.android.PolyUtil
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class SearchRouteActivity: AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener,
    GoogleMap.OnPolylineClickListener {

    companion object {
        const val TAG = "besttripapp.SearchRouteActivity"
        const val MAPS_API_KEY = "AIzaSyCOK0WAAutVBjR1gDyrkjJwgGUrRvZL_Jk"
    }

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivitySearchBinding
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var origin: LatLng
    private lateinit var destination: LatLng
    private lateinit var destinationName: String
    private lateinit var layersButton: ImageButton
    private lateinit var infoButton: ImageButton
    private lateinit var detailText: TextView
    private lateinit var closeText: TextView
    private lateinit var chooseText: TextView
    private lateinit var polyline1: Polyline
    private lateinit var polyline2: Polyline
    private lateinit var polyline3: Polyline
    private var instructions1: ArrayList<String> = ArrayList()
    private var instructions2: ArrayList<String> = ArrayList()
    private var instructions3: ArrayList<String> = ArrayList()
    private var distances1: ArrayList<String> = ArrayList()
    private var distances2: ArrayList<String> = ArrayList()
    private var distances3: ArrayList<String> = ArrayList()
    lateinit var placesClient: PlacesClient
    private var mapTypeSelected: Int = 1

    private lateinit var routes: JSONArray
    private var routeSelected: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        Places.initialize(applicationContext, MAPS_API_KEY)
        placesClient = Places.createClient(applicationContext)

        //Autocomplete fragment to search destination
        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment

        autocompleteFragment.view?.setBackgroundColor(Color.WHITE)
        autocompleteFragment.setHint("Where are you traveling?")

        autocompleteFragment.setPlaceFields(listOf(Place.Field.NAME, Place.Field.ID, Place.Field.LAT_LNG, Place.Field.ADDRESS))

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onError(p0: Status) {
                Log.i(TAG, p0.toString())
            }

            override fun onPlaceSelected(p0: Place) {
                mMap.clear()

                getLocation()
                mMap.addMarker(MarkerOptions().position(origin).title("Toronto"))

                destination= p0.latLng
                destinationName = p0.name
                mMap.addMarker(MarkerOptions().position(destination).title(destinationName))

                searchRoutes(origin,destination)
            }
        })

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //Actions on click Layer button
        layersButton = findViewById(R.id.layers_button)
        layersButton.setOnClickListener(){
            mapTypeSelected += 1
            mMap.mapType = mapTypeSelected

            if(mapTypeSelected == 4) {
                mapTypeSelected = 0
            }
        }

        // Choose detail text
        chooseText = findViewById(R.id.choose_text)
        chooseText.setOnClickListener {
            if (routeSelected >= 0 && routes.length() > routeSelected) {
                val myIntent = Intent(this, RouteActivity::class.java)
                val newRoute = TravelRoute()
                newRoute.startPoint = TravelLocation(name= "Toronto", latitude = origin.latitude, longitude = origin.longitude)
                newRoute.endPoint = TravelLocation(name = destinationName ,latitude = destination.latitude, longitude = destination.longitude)
                newRoute.name = destinationName
                newRoute.route = routes[routeSelected].toString()
                myIntent.putExtra("search_route", newRoute)
                startActivity(myIntent)
            }else {
                Log.e(TAG, "No data selected $routeSelected / ${routes.length()}")
            }

        }

        //Route detail TextView Scroll
        detailText = findViewById(R.id.detail_text)
        detailText.movementMethod = ScrollingMovementMethod()

        //Close Route detail TextView
        closeText = findViewById(R.id.close_text)
        closeText.setOnClickListener() {
            detailText.visibility = View.GONE
            closeText.visibility = View.GONE
            chooseText.visibility = View.GONE
        }

        infoButton = findViewById(R.id.route_info_button)
        infoButton.setOnClickListener(){
            //Not implemented yet
        }
    }

    /**
     * Settings and initial position for map
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //Map Settings
        mMap.uiSettings.isCompassEnabled = true
        mMap.mapType = mapTypeSelected
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isZoomGesturesEnabled = true
        mMap.uiSettings.isRotateGesturesEnabled = true
        mMap.uiSettings.isTiltGesturesEnabled = true
        mMap.uiSettings.isScrollGesturesEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true

        //listeners
        mMap.setOnInfoWindowClickListener(this)
        mMap.setOnPolylineClickListener(this)

        //val initOrigin = LatLng(43.684345, -79.431292) //Toronto
        getLocation()
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origin, 7.0f))
    }

    /**
     * Search routes between two points
     */
    fun searchRoutes(origin: LatLng, destination: LatLng) {
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f))

        instructions1.clear()
        distances1.clear()
        instructions2.clear()
        distances2.clear()
        instructions3.clear()
        distances3.clear()

        val urlDirections = "https://maps.googleapis.com/maps/api/directions/json?origin="+ origin.latitude.toString() + "," + origin.longitude.toString() +
                "&destination=" + destination.latitude.toString() + "," + destination.longitude.toString() +
                "&sensor=false&avoidTolls=true&alternatives=true&units=metric&mode=driving" + "&key=${MAPS_API_KEY}"

        //Test
        //val urlDirections = "https://maps.googleapis.com/maps/api/directions/json?origin=Toronto, ON" +
        //        "&destination=Mississauga, ON" +
        //        "&sensor=false&alternatives=true&units=metric&mode=driving" + "&key=${MAPS_API_KEY}"

        val directionsRequest = object : StringRequest(Method.GET, urlDirections, Response.Listener<String> {
                response ->
            val jsonResponse = JSONObject(response)
            // Get routes
            routes = jsonResponse.getJSONArray("routes")

            //Store instructions and distances
            instructions1.add("ROUTE A INFORMATION\n")
            distances1.add("")
            instructions2.add("ROUTE B INFORMATION\n")
            distances2.add("")
            instructions3.add("ROUTE C INFORMATION\n")
            distances3.add("")

            //Paint routes and store instructions and distances for each step
            for (j in 0 until routes.length()) {

                val legs = routes.getJSONObject(j).getJSONArray("legs")
                val overlayPolyline = routes.getJSONObject(j).getJSONObject("overview_polyline").getString("points")
                val steps = legs.getJSONObject(0).getJSONArray("steps")

                when (j) {
                    0 -> {
                        instructions1.add("Distance - ")
                        distances1.add(legs.getJSONObject(0).getJSONObject("distance").getString("text"))
                        instructions1.add("Duration - ")
                        distances1.add(legs.getJSONObject(0).getJSONObject("duration").getString("text"))
                        instructions1.add("\nROUTE INSTRUCTIONS\n")
                        distances1.add("")
                    }
                    1 -> {
                        instructions2.add("Distance - ")
                        distances2.add(legs.getJSONObject(0).getJSONObject("distance").getString("text"))
                        instructions2.add("Duration - ")
                        distances2.add(legs.getJSONObject(0).getJSONObject("duration").getString("text"))
                        instructions2.add("\nROUTE INSTRUCTIONS\n")
                        distances2.add("")
                    }
                    else -> {
                        instructions3.add("Distance - ")
                        distances3.add(legs.getJSONObject(0).getJSONObject("distance").getString("text"))
                        instructions3.add("Duration - ")
                        distances3.add(legs.getJSONObject(0).getJSONObject("duration").getString("text"))
                        instructions3.add("\nROUTE INSTRUCTIONS\n")
                        distances3.add("")
                    }
                }

                for (i in 0 until steps.length()) {
                    //Unused
                    val points =
                        steps.getJSONObject(i).getJSONObject("polyline").getString("points")

                    var instruction = steps.getJSONObject(i).getString("html_instructions")

                    instruction = Html.fromHtml(instruction, Html.FROM_HTML_MODE_COMPACT).toString()

                    when (j) {
                        0 -> {
                            instructions1.add(instruction + " - ")
                            distances1.add(steps.getJSONObject(i).getJSONObject("distance").getString("text"))
                            polyline1 = mMap.addPolyline(
                                PolylineOptions().addAll(PolyUtil.decode(overlayPolyline)).color(
                                    Color.RED).clickable(true).width(20f))
                            polyline1.tag = "A"
                        }
                        1 -> {
                            instructions2.add(instruction + " - ")
                            distances2.add(steps.getJSONObject(i).getJSONObject("distance").getString("text"))
                            polyline2 = mMap.addPolyline(
                                PolylineOptions().addAll(PolyUtil.decode(overlayPolyline)).color(
                                    Color.BLUE).clickable(true).width(20f).zIndex(10.0f))
                            polyline2.tag = "B"
                        }
                        else -> {
                            instructions3.add(instruction + " - ")
                            distances3.add(steps.getJSONObject(i).getJSONObject("distance").getString("text"))
                            polyline3 = mMap.addPolyline(
                                PolylineOptions().addAll(PolyUtil.decode(overlayPolyline)).color(
                                    Color.GREEN).clickable(true).width(20f))
                            polyline3.tag = "C"
                        }
                    }
                }
            }
        }, Response.ErrorListener {
                _ ->
        }){}

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(directionsRequest)

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origin, 15.0f))
    }

    override fun onInfoWindowClick(p0: Marker) {
        Log.e(TAG, "onInfoWindowClick")
    }

    /**
     * Check location is enabled
     */
    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    /**
     * Check location permissions
     */
    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    /**
     * Get device actual location if is available
     */
    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        val geocoder = Geocoder(this, Locale.getDefault())
                        val list: List<Address> =
                            geocoder.getFromLocation(location.latitude, location.longitude, 1) as List<Address>

                        origin = LatLng(list[0].latitude,list[0].longitude)
                    }
                }
            } else {
                Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            origin = LatLng(43.684345, -79.431292) //Toronto
        }
    }

    /**
     * Actions on click route line
     */
    override fun onPolylineClick(p0: Polyline) {
        var text: String = ""

        Toast.makeText(
            applicationContext,
            p0.tag.toString(),
            Toast.LENGTH_LONG
        ).show()

        when (p0.tag) {
            "A" -> {
                routeSelected = 0
                for (i in 0 until instructions1.size) {
                    text += instructions1[i] + distances1[i] + "\n"
                }
            }
            "B" -> {
                routeSelected = 1
                for (i in 0 until instructions2.size) {
                    text += instructions2[i] + distances2[i] + "\n"
                }
            }
            else -> {
                routeSelected = 2
                for (i in 0 until instructions3.size) {
                    text += instructions3[i] + distances3[i] + "\n"
                }
            }
        }

        if(!detailText.isVisible) {
            detailText.text = text
            detailText.visibility = View.VISIBLE
            closeText.visibility = View.VISIBLE
            chooseText.visibility = View.VISIBLE
        }else {
            detailText.visibility = View.GONE
            closeText.visibility = View.GONE
            chooseText.visibility = View.GONE
        }
    }
}