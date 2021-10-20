package com.example.simplegetrequest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import kotlinx.coroutines.*
import org.json.JSONArray
import java.lang.Exception
import java.net.URL

class MainActivity : AppCompatActivity() {

    lateinit var tv: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv = findViewById(R.id.tv)
        requestApi()
    }

    private fun requestApi(){
        var data = ""
        CoroutineScope(Dispatchers.IO).launch{
            data = async {
                fetchData()
            }.await()
            if(data.isNotEmpty()){
                val array = JSONArray(data)
                val len = array.length()
                withContext(Dispatchers.Main){
                    for (i in 0 until len-1){
                        tv.text = "${tv.text}\n${array.getJSONObject(i).getString("name")}"
                    }
                }
            }
        }
    }

    private fun fetchData(): String {
        var response = ""
        try {
            response = URL("https://dojo-recipes.herokuapp.com/people/").readText(Charsets.UTF_8)
        }catch (e: Exception){
            Log.e("TAG", "$e")
        }
        return response
    }
}