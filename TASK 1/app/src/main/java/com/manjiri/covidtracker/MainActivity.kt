package com.manjiri.covidtracker

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

class MainActivity : AppCompatActivity() {
    lateinit var txtWorldTotalCasesNo :TextView
    lateinit var txtWorldRecoveredCasesNo :TextView
    lateinit var txtWorldDeathCasesNo :TextView
    lateinit var txtIndiaTotalCasesNo :TextView
    lateinit var txtIndiaRecoveredCasesNo :TextView
    lateinit var txtIndiaDeathCasesNo :TextView
    lateinit var StateName:TextView
    lateinit var txtTotalCasesNo :TextView
    lateinit var txtRecoveredCasesNo :TextView
    lateinit var txtDeathCasesNo :TextView
    lateinit var recyclerList:RecyclerView
    lateinit var layoutManager:RecyclerView.LayoutManager
    lateinit var recyclerAdapter:RecyclerAdapter
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    val model = arrayListOf<StateCases>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        txtWorldTotalCasesNo=findViewById(R.id.txtWorldTotalCasesNo)
        txtWorldRecoveredCasesNo=findViewById(R.id.txtWorldRecoveredCasesNo)
        txtWorldDeathCasesNo=findViewById(R.id.txtWorldDeathCasesNo)
        txtIndiaTotalCasesNo=findViewById(R.id.txtIndiaTotalCasesNo)
        txtIndiaRecoveredCasesNo=findViewById(R.id.txtIndiaRecoveredCasesNo)
        txtIndiaDeathCasesNo=findViewById(R.id.txtIndiaDeathCasesNo)

        recyclerList=findViewById(R.id.recyclerList)

        progressLayout=findViewById(R.id.progressLayout)

        progressBar=findViewById(R.id.progressBar)

        progressLayout.visibility=View.VISIBLE
        layoutManager = LinearLayoutManager(this)
        getStateInfo()
        getWorldInfo()

    }

    private fun getStateInfo(){
        val queue= Volley.newRequestQueue(this@MainActivity)
        val url="https://api.rootnet.in/covid19-in/stats/latest"
        if(ConnectionManager().checkConnectivity(this)){
            val jsonObjectRequest=object:JsonObjectRequest(Request.Method.GET,url,null,Response.Listener {

                try {
                    progressLayout.visibility = View.GONE
                    val data = it.getJSONObject("data")
                    val dataObject = data.getJSONObject("summary")
                    val cases: Int = dataObject.getInt("total")
                    val recovered: Int = dataObject.getInt("discharged")
                    val deaths: Int = dataObject.getInt("deaths")
                    txtIndiaTotalCasesNo.text=cases.toString()
                    txtIndiaRecoveredCasesNo.text=recovered.toString()
                    txtIndiaDeathCasesNo.text=deaths.toString()
                    val regionalArray = data.getJSONArray("regional")
                    for (i in 0 until regionalArray.length()) {
                        val regionalObj = regionalArray.getJSONObject(i)
                        val stateName: String = regionalObj.getString("loc")
                        val cases: Int = regionalObj.getInt("totalConfirmed")
                        val deaths: Int = regionalObj.getInt("deaths")
                        val recovered: Int = regionalObj.getInt("discharged")
                        val objectModel = StateCases(stateName,recovered,deaths,cases)
                        model.add(objectModel)
                        recyclerAdapter = RecyclerAdapter(this, model)
                        recyclerList.layoutManager=LinearLayoutManager(this)
                        recyclerList.adapter = recyclerAdapter


                    }


                } catch (e: JSONException) {
                    Toast.makeText(this, "Some unexpected error occurred!!", Toast.LENGTH_SHORT)
                        .show()
                }


            }, Response.ErrorListener {
                if (this!=null){
                    Toast.makeText(this,"Volley Error Occurred!!",Toast.LENGTH_SHORT).show()}
            }){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers=HashMap<String, String>()
                    headers["Content-type"]="application/json"

                    return headers
                }

            }

            queue.add(jsonObjectRequest)



        }else{
            val dialog= AlertDialog.Builder(this)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not Found")
            dialog.setPositiveButton("Open Settings"){ text,listener->
                val settingsIntent= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                this?.finish()

            }

            dialog.setNegativeButton("Exit"){ text,listener->
                ActivityCompat.finishAffinity(this)
            }
            dialog.create()
            dialog.show()
        }
    }
    private fun getWorldInfo(){
        val url = "https://corona.lmao.ninja/v3/covid-19/all"
        val queue = Volley.newRequestQueue(this)
        val request =
            JsonObjectRequest(Request.Method.GET, url, null, { response ->
                try {
                    val totalCasesWorld: Int = response.getInt("cases")
                    val recoveredWorld: Int = response.getInt("recovered")
                    val deathWorld: Int = response.getInt("deaths")

                    txtWorldTotalCasesNo.text = totalCasesWorld.toString()
                    txtWorldRecoveredCasesNo.text = recoveredWorld.toString()
                    txtWorldDeathCasesNo.text = deathWorld.toString()

                } catch (e: JSONException) {
                    Toast.makeText(this, "Some unexpected error occurred!!", Toast.LENGTH_SHORT)
                        .show()
                }
            }, { error ->
                Toast.makeText(
                    this,
                    "Volley error occurred!!",
                    Toast.LENGTH_SHORT
                ).show()

            })
        queue.add(request)

    }

    }
