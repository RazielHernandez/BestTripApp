package com.fekea.besttripapp.activities

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fekea.besttripapp.R
import com.fekea.besttripapp.adapters.VehicleAdapter
import com.fekea.besttripapp.dataModel.TravelerVehicle
import com.fekea.besttripapp.databinding.ActivityVehiclesBinding
import com.fekea.besttripapp.interfaces.VehicleInterface
import com.fekea.besttripapp.utils.FuelConsumption


class VehiclesActivity: AppCompatActivity(), VehicleInterface {

    companion object {
        const val TAG = "besttripapp.VehiclesActivity"
    }

    private lateinit var binding: ActivityVehiclesBinding
    private lateinit var vehicleAdapter: VehicleAdapter
    private lateinit var fuelConsumption: FuelConsumption

    private lateinit var dialog: Dialog
    private lateinit var vehiclesList: ArrayList<String>
    private lateinit var vehicleSearch: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVehiclesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recycleView = findViewById<RecyclerView>(R.id.vehicle_recycle_view)
        recycleView.layoutManager = LinearLayoutManager(this)
        vehicleAdapter = VehicleAdapter(this, this)
        recycleView.adapter = vehicleAdapter

        fuelConsumption = FuelConsumption(context1 = this, id1 = R.raw.fuelconsumptionratings)
        vehiclesList = fuelConsumption.getVehicles()

        vehicleSearch = findViewById<TextView>(R.id.vehicle_search)
        vehicleSearch.setOnClickListener {
            // Initialize dialog
            // Initialize dialog
            dialog = Dialog(this)

            // set custom dialog

            // set custom dialog
            dialog.setContentView(R.layout.dialog_vehicle_search)

            // set custom height and width

            // set custom height and width
            dialog.getWindow()?.setLayout(750, 800)

            // set transparent background

            // set transparent background
            dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            // show dialog

            // show dialog
            dialog.show()

            // Initialize and assign variable

            // Initialize and assign variable
            val editText: EditText = dialog.findViewById(R.id.edit_text)
            val listView: ListView = dialog.findViewById(R.id.list_view)

            // Initialize array adapter

            // Initialize array adapter
            val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                vehiclesList
            )

            // set adapter

            // set adapter
            listView.setAdapter(adapter)
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    adapter.filter.filter(s)
                }

                override fun afterTextChanged(s: Editable) {}
            })

            listView.setOnItemClickListener(OnItemClickListener { parent, view, position, id -> // when item selected from list
                // set selected item on textView
                vehicleSearch.text = adapter.getItem(position)

                // Dismiss dialog
                dialog.dismiss()
            })
        }

        /*val spinnerMakerSelectedListener: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener() {
            override fun onItemSelected(
                arg0: AdapterView<*>?, arg1: View?, arg2: Int,
                arg3: Long
            ) {

            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {
                // TODO Auto-generated method stub
            }
        }

        val spinnerYearSelectedListener: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener() {
            override fun onItemSelected(
                arg0: AdapterView<*>?, arg1: View?, arg2: Int,
                arg3: Long
            ) {

            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {
                // TODO Auto-generated method stub
            }
        }

        val spinnerModelSelectedListener: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener() {
            override fun onItemSelected(
                arg0: AdapterView<*>?, arg1: View?, arg2: Int,
                arg3: Long
            ) {

            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {
                //
            }
        }

        val spinnerMaker = findViewById<Spinner>(R.id.vehicle_maker)
        spinnerMaker.onItemSelectedListener(spinnerMakerSelectedListener)

        val spinnerYear = findViewById<Spinner>(R.id.vehicle_year)
        spinnerYear.setOnItemClickListener(object : AdapterView.OnItemSelectedListener() {
            override fun onItemSelected(
                arg0: AdapterView<*>?, arg1: View?, arg2: Int,
                arg3: Long
            ) {

            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {
                //
            }
        })

        val spinnerModel = findViewById<Spinner>(R.id.vehicle_model)
        */


    }

    override fun onVehicleDelete(vehicle: TravelerVehicle) {
        Log.e(TAG, "Delete vehicle $vehicle")
    }

}