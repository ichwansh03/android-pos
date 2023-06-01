package com.jrektor.skripsi.outlet.employee

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import com.jrektor.skripsi.verification.LoginActivity
import kotlinx.android.synthetic.main.fragment_list_employee.*

class ListEmployeeFragment : Fragment() {

    private val list = ArrayList<ItemPegawai>()
    private val empList: MutableLiveData<ArrayList<ItemPegawai>> = MutableLiveData()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_list_employee, container, false)

        val addEmployee = view.findViewById<FloatingActionButton>(R.id.btn_add_employee)

        if (LoginActivity.OutletData.pekerjaan == "Karyawan") {
            addEmployee.visibility = View.INVISIBLE
        } else {
            addEmployee.setOnClickListener { startActivity(Intent(activity, AddPegawaiActivity::class.java)) }
        }

        val adapterPegawai = AdapterPegawai(requireContext(), list)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_employee)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapterPegawai

        empList.observe(viewLifecycleOwner) { employees ->
            adapterPegawai.getData(employees)
        }

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            handler.post {
                getEmployee(LoginActivity.OutletData.namaOutlet)
                pb_employee.visibility = View.GONE
            }
        }, 3000)
        return view
    }

    private fun getEmployee(outlet: String) {
        val activity = activity

        if (activity != null) {
            val queue = Volley.newRequestQueue(activity)
            val request = JsonArrayRequest(Request.Method.GET, GlobalData.BASE_URL+ "employee/apiemployeebyoutlet.php?in_outlet=$outlet", null,
                { response ->
                    list.clear()
                    for (i in 0 until response.length()){
                        val jsonObject = response.getJSONObject(i)
                        val id = jsonObject.getInt("id")
                        val name = jsonObject.getString("name")
                        val job = jsonObject.getString("job")
                        val phone = jsonObject.getString("phone")
                        val email = jsonObject.getString("email")
                        val no_pin = jsonObject.getInt("no_pin")
                        val image = jsonObject.getString("image").replace("http://localhost/pos/",GlobalData.BASE_URL)
                        val in_outlet = jsonObject.getString("in_outlet")
                        val branch = jsonObject.getString("branch")
                        GlobalData.branchPegawai = branch

                        list.add(ItemPegawai(id, name, job, phone, email, no_pin, image, in_outlet, branch))
                    }

                    empList.value = list

                }, {error ->
                    Log.d("error ", error.toString())

                })
            queue.add(request)
        }
    }
}