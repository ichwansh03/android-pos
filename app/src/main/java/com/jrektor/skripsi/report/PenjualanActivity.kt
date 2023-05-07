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
import org.json.JSONException

class PenjualanActivity : AppCompatActivity() {

    private val urldaily = GlobalData.BASE_URL+"order/quantitiesdaily.php"
    private val urlweekly = GlobalData.BASE_URL+"order/quantitiesweekly.php"
    private val urlmonthly = GlobalData.BASE_URL+"order/quantitiesmonthly.php"

    lateinit var linechart: LineChart
    private val entries = mutableListOf<Entry>()
    private val labels = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_penjualan)

        linechart = findViewById(R.id.chart_penjualan)

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            getChartDaily()
            pb_penjualan.visibility = View.GONE
        }, 5000)

    }

    private fun getChartDaily() {
        val request = JsonArrayRequest(Request.Method.GET, urldaily, null,
            { response ->
                for (i in 0 until response.length()) {
                    try {
                        val obj = response.getJSONObject(i)
                        val day = obj.getString("hari")
                        val quantity = obj.getInt("quantity")
                        entries.add(Entry(i.toFloat(), quantity.toFloat()))
                        labels.addAll(listOf(day))
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

                val dataSet = LineDataSet(entries, "Daily Sales")
                val lineData = LineData(dataSet)

                val xAxis = linechart.xAxis
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.valueFormatter = IndexAxisValueFormatter(labels)
                linechart.data = lineData
                linechart.invalidate()
            },
            { error ->
                Toast.makeText(this, "Error: " + error.message, Toast.LENGTH_SHORT).show()
            })

        Volley.newRequestQueue(this).add(request)
    }

}
