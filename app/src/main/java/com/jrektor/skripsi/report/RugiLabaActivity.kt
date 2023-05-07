package com.jrektor.skripsi.report

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R

class RugiLabaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rugi_laba)

        val line = findViewById<LineChart>(R.id.lc_rugilaba)
        val url = GlobalData.BASE_URL+"order/totalorderweekly.php"
        val entries = ArrayList<Entry>()
        val labels = ArrayList<String>()

        val queue = Volley.newRequestQueue(this)
        val request = JsonArrayRequest(Request.Method.GET, url, null,
            { response ->
                for (i in 0 until response.length()) {
                    val objects = response.getJSONObject(i)
                    //no value for hari
                    val xval = objects.getString("hari")
                    val yval = objects.getInt("total")
                    entries.add(Entry(i.toFloat(), yval.toFloat()))
                    labels.addAll(listOf(xval))
                }

                val dataSet = LineDataSet(entries, "Hari")
                val data = LineData(dataSet)

                val xAxis = line.xAxis
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.valueFormatter = IndexAxisValueFormatter(labels)

                line.data = data
                line.description.isEnabled = false
                line.legend.isEnabled = false
                line.setPinchZoom(false)
                line.setDrawGridBackground(false)
                line.invalidate()
            },
            { error ->
                Toast.makeText(this, "Error: " + error.message, Toast.LENGTH_SHORT).show()
            })
        queue.add(request)
    }
}