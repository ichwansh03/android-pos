package com.jrektor.skripsi.report

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
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
import kotlinx.android.synthetic.main.activity_penjualan.*

class PenjualanActivity : AppCompatActivity() {

    lateinit var lineChart: LineChart

    private val entries = ArrayList<Entry>()
    private val labels = ArrayList<String>()
    private val urldaily = GlobalData.BASE_URL+"order/quantitiesdaily.php"
    private val urlweekly = GlobalData.BASE_URL+"order/quantitiesweekly.php"
    private val urlmonthly = GlobalData.BASE_URL+"order/quantitiesmonthly.php"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_penjualan)

        lineChart = findViewById(R.id.line_chart)

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            getChartDaily()
            pb_penjualan.visibility = View.GONE
//            if (rbharian.isChecked) {
//                getChartDaily()
//            } else if (rbpekanan.isChecked) {
//                getChartWeekly()
//            } else if (rbbulanan.isChecked) {
//                getChartMonthly()
//            }
        }, 5000)

    }

    private fun getChartMonthly() {
        val request = JsonArrayRequest(Request.Method.GET, urlmonthly, null,
            { response ->
                for (i in 0 until response.length()) {
                    val jsonObject = response.getJSONObject(i)
                    val yValue = jsonObject.getInt("quantity")
                    val xValue = jsonObject.getString("nomor_bulan")
                    entries.add(Entry(i.toFloat(), yValue.toFloat()))
                    labels.addAll(listOf(xValue))
                }

                // Mengatur data dan konfigurasi chart
                val dataSet = LineDataSet(entries, "Bulan ke")
                val data = LineData(dataSet)
                lineChart.data = data

            },
            { error ->
                Toast.makeText(this, "Error: " + error.message, Toast.LENGTH_SHORT).show()
            })
        Volley.newRequestQueue(this).add(request)
    }

    private fun getChartWeekly() {
        val request = JsonArrayRequest(Request.Method.GET, urlweekly, null,
            { response ->
                for (i in 0 until response.length()) {
                    val jsonObject = response.getJSONObject(i)
                    val yValue = jsonObject.getInt("quantity")
                    val xValue = jsonObject.getString("nomor_pekan")
                    entries.add(Entry(i.toFloat(), yValue.toFloat()))
                    labels.addAll(listOf(xValue))
                }

                // Mengatur data dan konfigurasi chart
                val dataSet = LineDataSet(entries, "Pekan ke")
                val data = LineData(dataSet)
                lineChart.data = data

            },
            { error ->
                Toast.makeText(this, "Error: " + error.message, Toast.LENGTH_SHORT).show()
            })
        Volley.newRequestQueue(this).add(request)
    }

    private fun getChartDaily() {
        val request = JsonArrayRequest(Request.Method.GET, urldaily, null,
            { response ->
                for (i in 0 until response.length()) {
                    val jsonObject = response.getJSONObject(i)
                    val yValue = jsonObject.getInt("quantity")
                    val xValue = jsonObject.getString("hari")
                    entries.add(Entry(i.toFloat(), yValue.toFloat()))
                    labels.addAll(listOf(xValue))
                }
                val dataSet = LineDataSet(entries, "Hari")
                val data = LineData(dataSet)
                lineChart.data = data

                val xAxis = lineChart.xAxis
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.valueFormatter = IndexAxisValueFormatter(labels)

                lineChart.description.isEnabled = false
                lineChart.legend.isEnabled = false
                lineChart.setPinchZoom(false)
                lineChart.setDrawGridBackground(false)
                lineChart.invalidate()
            },
            { error ->
                Toast.makeText(this, "Error: " + error.message, Toast.LENGTH_SHORT).show()
            })
        Volley.newRequestQueue(this).add(request)
    }

    private fun showChart(label: String, entries: ArrayList<Entry>, labels: ArrayList<String>){
        // Mengatur data dan konfigurasi chart
        val dataSet = LineDataSet(entries, label)
        val data = LineData(dataSet)
        lineChart.data = data

        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)

        lineChart.description.isEnabled = false
        lineChart.legend.isEnabled = false
        lineChart.setPinchZoom(false)
        lineChart.setDrawGridBackground(false)
        lineChart.invalidate()
    }
}