package com.example.retrofit_yt

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.converter.gson.GsonConverterFactory // Add this import statement
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class MainActivity : AppCompatActivity() {
    lateinit var rvMain: RecyclerView
    lateinit var myAdapter: MyAdaptor

    private val BASE_URL = "https://api.github.com"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        rvMain = findViewById(R.id.recycler_view)
        rvMain.layoutManager = LinearLayoutManager(this)

        getAllData()
    }

    private fun getAllData() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)

        val retroData = retrofit.getData()

        retroData.enqueue(object : Callback<List<UsersItem>> {
            override fun onResponse(
                call: Call<List<UsersItem>>,
                response: Response<List<UsersItem>>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.let {
                        myAdapter = MyAdaptor(this@MainActivity, it)
                        rvMain.adapter = myAdapter
                    }
                } else {
                    Log.e("getAllData", "Failed to get data: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<UsersItem>>, t: Throwable) {
                Log.e("getAllData", "Error fetching data", t)
            }
        })
    }
}
