package com.fekea.besttripapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import com.fekea.besttripapp.activities.HistoryActivity
import com.fekea.besttripapp.activities.SearchRouteActivity
import com.fekea.besttripapp.activities.VehiclesActivity

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "com.fekea.besttripapp.MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val searchButton: Button = findViewById(R.id.home_button_search)
        searchButton.setOnClickListener {
            val myIntent = Intent(this, SearchRouteActivity::class.java)
            startActivity(myIntent)
        }

        val homeButton: Button = findViewById(R.id.home_button_history)
        homeButton.setOnClickListener {
            val myIntent = Intent(this, HistoryActivity::class.java)
            startActivity(myIntent)
        }

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
            val myIntent = Intent(this, VehiclesActivity::class.java)
            startActivity(myIntent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}