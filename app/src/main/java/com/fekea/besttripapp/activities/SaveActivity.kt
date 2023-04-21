package com.fekea.besttripapp.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.fekea.besttripapp.MainActivity
import com.fekea.besttripapp.R
import com.fekea.besttripapp.dataModel.TravelLocation
import com.fekea.besttripapp.dataModel.TravelRoute
import com.fekea.besttripapp.databinding.ActivityRouteBinding
import com.fekea.besttripapp.databinding.ActivitySaveBinding
import com.fekea.besttripapp.utils.FuelConsumption
import com.fekea.besttripapp.viewModel.RouteViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SaveActivity: AppCompatActivity(), AdapterView.OnItemSelectedListener {

    companion object {
        const val TAG = "besttripapp.SaveActivity"
        const val MAPS_API_KEY = "AIzaSyCOK0WAAutVBjR1gDyrkjJwgGUrRvZL_Jk"
    }

    private lateinit var binding: ActivitySaveBinding
    private lateinit var route: TravelRoute
    private lateinit var routeModel: RouteViewModel
    private lateinit var category: String

    private lateinit var fuelConsumption: FuelConsumption
    private lateinit var dialog: Dialog
    private lateinit var vehiclesList: ArrayList<String>
    private lateinit var vehicleSearch: TextView
    private lateinit var vehicleSelected: String
    private lateinit var liters: String
    private lateinit var dollars: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        route = intent.extras?.getSerializable("search_route") as TravelRoute
        Log.e(TAG, "Route from ${route.startPoint.name} to ${route.endPoint.name}")
        Log.e(TAG, "Route: ${route.route}")

        routeModel = RouteViewModel(this)

        val routeName = findViewById<TextView>(R.id.route_title)
        routeName.text = "Travel to ${route.name}"

        val routeDistance = findViewById<TextView>(R.id.route_distance)
        routeDistance.text = "Distance: ${route.distanceString}"

        val routeDuration = findViewById<TextView>(R.id.route_duration)
        routeDuration.text = "Duration: ${route.durationString}"

        val categoriesSpinner = findViewById<Spinner>(R.id.route_category)
        ArrayAdapter.createFromResource(
            this,
            R.array.route_types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            categoriesSpinner.adapter = adapter
        }
        categoriesSpinner.onItemSelectedListener = this

        val saveButton = findViewById<Button>(R.id.route_button_save)
        saveButton.setOnClickListener {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy")
            route.category = this.category
            route.date = current.format(formatter)
            routeModel.insertRouteToDB(route)
            val myIntent = Intent(this, HistoryActivity::class.java)
            startActivity(myIntent)
        }

        fuelConsumption = FuelConsumption(context1 = this, id1 = R.raw.fuelconsumptionratings)
        vehiclesList = fuelConsumption.getVehicles()

        vehicleSearch = findViewById<TextView>(R.id.vehicle_search)
        vehicleSearch.setOnClickListener {
            dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_vehicle_search)
            dialog.getWindow()?.setLayout(750, 800)
            dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()

            // Initialize and assign variable
            val editText: EditText = dialog.findViewById(R.id.edit_text)
            val listView: ListView = dialog.findViewById(R.id.list_view)
            val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                vehiclesList
            )

            listView.setAdapter(adapter)
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    adapter.filter.filter(s)
                }

                override fun afterTextChanged(s: Editable) {}
            })

            listView.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id -> // when item selected from list
                // set selected item on textView
                vehicleSearch.text = adapter.getItem(position)
                vehicleSelected = adapter.getItem(position).toString()
                calculateGasConsumption()
                // Dismiss dialog
                dialog.dismiss()
            })
        }
    }

    private fun calculateGasConsumption() {
        val reader = FuelConsumption(context1 = this, id1 = R.raw.fuelconsumptionratings)
        val gasConsumption = reader.getConsumptionModelByString(vehicleSelected)

        route.gasCost = gasConsumption.calculateCostConsumption(route.distance/1000)
        route.liters = gasConsumption.calculateLiterConsumption(route.distance/1000)
        val fuelConsumptionText = findViewById<TextView>(R.id.route_gas_consumption)
        val liters = gasConsumption.calculateLiterConsumptionString((route.distance/1000))
        val dollars = String.format("%.2f", gasConsumption.calculateCostConsumption((route.distance/1000)))

        fuelConsumptionText.text = "$liters / $ $dollars"

        Log.e(TAG, "GAS : ${gasConsumption.toString()}")
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        Log.e(TAG, "selected: ${parent.getItemAtPosition(pos)}")
        category = parent.getItemAtPosition(pos).toString()
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        Log.e(TAG, "Nothing selected")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.item_home) {
            val myIntent = Intent(this, MainActivity::class.java)
            startActivity(myIntent)
            return true
        }
        if (id == R.id.item_user) {
            Toast.makeText(this, "Item user Clicked", Toast.LENGTH_LONG).show()
            return true
        }
        if (id == R.id.item_history) {
            val myIntent = Intent(this, HistoryActivity::class.java)
            startActivity(myIntent)
            return true
        }
        if (id == R.id.item_vehicles) {
            val myIntent = Intent(this, SearchRouteActivity::class.java)
            startActivity(myIntent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}