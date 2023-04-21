package com.fekea.besttripapp.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.fekea.besttripapp.R
import com.fekea.besttripapp.dataModel.TravelLocation
import com.fekea.besttripapp.dataModel.TravelRoute
import com.fekea.besttripapp.databinding.ActivityShowRouteBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil
import org.json.JSONArray
import org.json.JSONObject
import kotlin.collections.ArrayList

class ShowRouteActivity: AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener,
    GoogleMap.OnPolylineClickListener {

    companion object {
        const val TAG = "besttripapp.ShowRouteActivity"
        const val MAPS_API_KEY = "AIzaSyCOK0WAAutVBjR1gDyrkjJwgGUrRvZL_Jk"
    }

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityShowRouteBinding
    private lateinit var layersButton: ImageButton
    private lateinit var infoButton: ImageButton
    private lateinit var detailText: TextView
    private lateinit var closeText: TextView
    private lateinit var chooseText: TextView
    private lateinit var route: TravelRoute
    private lateinit var polyline: Polyline
    private lateinit var routes: JSONArray
    
    private var instructions: ArrayList<String> = ArrayList()
    private var distances: ArrayList<String> = ArrayList()
    private var waypointsLatLng: ArrayList<LatLng> = ArrayList()

    private var mapTypeSelected: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShowRouteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        route = intent.extras?.getSerializable("show_route") as TravelRoute
        waypointsLatLng = intent.extras?.getSerializable("waypoints") as ArrayList<LatLng>

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

        infoButton = findViewById(R.id.route_info_button)
        infoButton.setOnClickListener(){
            //Not implemented yet
        }

        // Choose detail text
        chooseText = findViewById(R.id.choose_text)
        chooseText.setOnClickListener {

            val newRoute = TravelRoute()
            val legs = routes.getJSONObject(0).getJSONArray("legs")
            newRoute.startPoint = TravelLocation(name= route.startPoint.name, latitude = route.startPoint.latitude, longitude = route.startPoint.longitude)
            newRoute.endPoint = TravelLocation(name = route.endPoint.name ,latitude = route.endPoint.latitude, longitude = route.endPoint.longitude)
            newRoute.name = route.endPoint.name
            newRoute.route = routes[0].toString()
            newRoute.distance = legs.getJSONObject(0).getJSONObject("distance").getDouble("value")
            newRoute.duration = legs.getJSONObject(0).getJSONObject("duration").getDouble("value")
            newRoute.distanceString = legs.getJSONObject(0).getJSONObject("distance").getString("text")
            newRoute.durationString = legs.getJSONObject(0).getJSONObject("duration").getString("text")

            val myIntent = Intent(this, SaveActivity::class.java)
            myIntent.putExtra("search_route", newRoute)
            startActivity(myIntent)

            /*val myIntent = Intent(this, SaveActivity::class.java)
            val newRoute = TravelRoute()
            newRoute.startPoint = TravelLocation(name= route.startPoint.name, latitude = route.startPoint.latitude, longitude = route.startPoint.longitude)
            newRoute.endPoint = TravelLocation(name = route.endPoint.name ,latitude = route.endPoint.latitude, longitude = route.endPoint.longitude)
            newRoute.name = route.endPoint.name.toString()
            newRoute.route = routes[0].toString()
            myIntent.putExtra("search_route", newRoute)
            startActivity(myIntent)*/
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
    }

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

        //Starting Point
        route.startPoint.latitude?.let { route.startPoint.longitude?.let { it1 -> LatLng(it, it1) } }
            ?.let { CameraUpdateFactory.newLatLngZoom(it, 7.0f) }?.let { mMap.moveCamera(it) }

       //Add markers
        route.startPoint.latitude?.let { route.startPoint.longitude?.let { it1 -> LatLng(it, it1) } }
            ?.let { MarkerOptions().position(it).title(route.startPoint.name) }?.let { mMap.addMarker(it) }

        route.endPoint.latitude?.let { route.endPoint.longitude?.let { it1 -> LatLng(it, it1) } }
            ?.let { MarkerOptions().position(it).title(route.endPoint.name) }?.let { mMap.addMarker(it) }

        for (i in waypointsLatLng.indices) {
            waypointsLatLng[i].latitude?.let { waypointsLatLng[i].longitude?.let { it1 -> LatLng(it, it1) } }
                ?.let { MarkerOptions().position(it).title(route.waypoints[i]) }?.let { mMap.addMarker(it) }
        }
        searchRoutes()
    }

    /**
     * Search routes between two points
     */
    fun searchRoutes() {
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f))
        
        var wayPoints: String = ""
            
        if (route.waypoints.size > 0) {
            wayPoints = "&waypoints=optimize:true"

            for (i in route.waypoints.indices) {
                wayPoints += "|" + route.waypoints[i]
            }
        }

        instructions.clear()
        distances.clear()

        var urlDirections: String

        if (wayPoints == "") {
            urlDirections =
                "https://maps.googleapis.com/maps/api/directions/json?origin=" + route.startPoint.latitude.toString() + "," +
                        route.startPoint.longitude.toString() + "&destination=" + route.endPoint.latitude.toString() + "," +
                        route.endPoint.longitude.toString() + "&sensor=false&avoidTolls=false&alternatives=true&units=metric&mode=driving" +
                        "&key=${SearchRouteActivity.MAPS_API_KEY}"
        } else {
            urlDirections =
                "https://maps.googleapis.com/maps/api/directions/json?origin=" + route.startPoint.latitude.toString() + "," +
                        route.startPoint.longitude.toString() + "&destination=" + route.endPoint.latitude.toString() + "," +
                        route.endPoint.longitude.toString() + wayPoints + "&sensor=false&avoidTolls=false&alternatives=true&units=metric&mode=driving" +
                        "&key=${SearchRouteActivity.MAPS_API_KEY}"
        }

        val directionsRequest = object : StringRequest(Method.GET, urlDirections, Response.Listener<String> {
                response ->
            val jsonResponse = JSONObject(response)
            // Get routes
            routes = jsonResponse.getJSONArray("routes")

            //Store instructions and distances
            instructions.add("\nROUTE INFORMATION")
            distances.add("")

            //Paint routes and store instructions and distances for each step
            val legs = routes.getJSONObject(0).getJSONArray("legs")
            val overlayPolyline = routes.getJSONObject(0).getJSONObject("overview_polyline").getString("points")

            for (j in 0 until legs.length()) {
                val steps = legs.getJSONObject(j).getJSONArray("steps")

                instructions.add("\nDistance - ")
                distances.add(legs.getJSONObject(j).getJSONObject("distance").getString("text"))
                instructions.add("Duration - ")
                distances.add(legs.getJSONObject(j).getJSONObject("duration").getString("text"))
                instructions.add("\nROUTE INSTRUCTIONS\n")
                distances.add("")

                for (i in 0 until steps.length()) {
                    //Unused
                    val points =
                        steps.getJSONObject(i).getJSONObject("polyline").getString("points")

                    var instruction = steps.getJSONObject(i).getString("html_instructions")

                    instruction = Html.fromHtml(instruction, Html.FROM_HTML_MODE_COMPACT).toString()

                    instructions.add(instruction + " - ")
                    distances.add(steps.getJSONObject(i).getJSONObject("distance").getString("text"))
                    polyline = mMap.addPolyline(
                        PolylineOptions().addAll(PolyUtil.decode(overlayPolyline)).color(
                            Color.BLUE).clickable(true).width(20f))
                    polyline.tag = "A"
                }
            }
        }, Response.ErrorListener {
                _ ->
        }){}

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(directionsRequest)

        route.startPoint.latitude?.let { route.startPoint.longitude?.let { it1 -> LatLng(it, it1) } }
            ?.let { CameraUpdateFactory.newLatLngZoom(it,15.0f) }?.let { mMap.moveCamera(it) }
    }
    
    override fun onInfoWindowClick(p0: Marker) {
        Log.e(SearchRouteActivity.TAG, "onInfoWindowClick")
    }

    override fun onPolylineClick(p0: Polyline) {
        var text: String = ""

        Toast.makeText(
            applicationContext,
            p0.tag.toString(),
            Toast.LENGTH_LONG
        ).show()

        for (i in 0 until instructions.size) {
            text += instructions[i] + distances[i] + "\n"
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