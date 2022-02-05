package com.example.selectingcar

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.databinding.DataBindingUtil
import com.example.selectingcar.databinding.ActivityMainBinding

const val KEY_AMOUNT_PRODUCTS = "AMOUNT_PRODUCTS"
const val KEY_TOTAL_PRICE = "TOTAL PRICE"
class MainActivity : AppCompatActivity() {


    lateinit var binding:ActivityMainBinding

    var amountProducts = 1
    var totalPrice = 0

    var currentCar = DataSource.listCars[0]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        if(savedInstanceState != null){
            amountProducts = savedInstanceState.getInt(KEY_AMOUNT_PRODUCTS,0)
            totalPrice = savedInstanceState.getInt(KEY_TOTAL_PRICE,0)
        }
        setImageCarAndText()
        binding.imgCar.setOnClickListener {
            buyCar()
        }
    }

    private fun setImageCarAndText() {
        var newCar = currentCar
        for(car in DataSource.listCars){
            if(amountProducts >= car.startProduction){
                newCar = car
            }else break
        }
        if(newCar != currentCar){
            currentCar = newCar
        }
        binding.imgCar.setImageResource(currentCar.imageRescource)
        binding.carPrice = totalPrice
        binding.carProduct = amountProducts
    }

    private fun buyCar() {
        amountProducts += 1
        totalPrice += currentCar.price

        setImageCarAndText()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_share,menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(KEY_AMOUNT_PRODUCTS,amountProducts)
        outState.putInt(KEY_TOTAL_PRICE,totalPrice)
        super.onSaveInstanceState(outState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_share -> onShare()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onShare() {
        val share = ShareCompat.IntentBuilder(this)
            .setText("Selecting Car")
            .setType("text/plain")
            .intent
        try {
            startActivity(share)
        }catch (ex: ActivityNotFoundException) {
            Toast.makeText(this,"sharing is not available",
                Toast.LENGTH_LONG).show()
        }

    }
}