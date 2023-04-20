package com.jrektor.skripsi.report

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R

class PenjualanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_penjualan)

        val lineChart = findViewById<LineChart>(R.id.line_chart)
        val url = GlobalData.BASE_URL+"order/quantitiesweekly.php"
        val requestQueue = Volley.newRequestQueue(this)

        val entries = ArrayList<Entry>()
        val labels = ArrayList<String>()

        val request = JsonArrayRequest(Request.Method.GET, url, null,
            { response ->
                for (i in 0 until response.length()) {
                    val jsonObject = response.getJSONObject(i)
                    val yValue = jsonObject.getInt("quantity")
                    val xValue = jsonObject.getString("hari")
                    entries.add(Entry(i.toFloat(), yValue.toFloat()))
                    labels.addAll(listOf(xValue))
                }

                // Mengatur data dan konfigurasi chart
                val dataSet = LineDataSet(entries, "Hari")
                val data = LineData(dataSet)

                val xAxis = lineChart.xAxis
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.valueFormatter = IndexAxisValueFormatter(labels)

                lineChart.data = data
                lineChart.description.isEnabled = false
                lineChart.legend.isEnabled = false
                lineChart.setPinchZoom(false)
                lineChart.setDrawGridBackground(false)
                lineChart.invalidate()
            },
            { error ->
                Toast.makeText(this, "Error: " + error.message, Toast.LENGTH_SHORT).show()
            })
        requestQueue.add(request)
    }
}