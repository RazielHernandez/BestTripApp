package com.fekea.besttripapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.fekea.besttripapp.activities.HistoryActivity
import com.fekea.besttripapp.activities.SaveActivity
import com.fekea.besttripapp.activities.SearchRouteActivity

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
}