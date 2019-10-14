package com.freedom_man.soqlexplorer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.salesforce.androidsdk.rest.RestClient
import com.salesforce.androidsdk.rest.RestRequest
import com.salesforce.androidsdk.rest.RestResponse
import org.json.JSONArray
import java.lang.Exception

class SoqlSearch(private var client: RestClient) : Fragment() {

    companion object {
        const val DEFAULT_SOQL = "SELECT Id, Name FROM Contact"
        const val DEFAULT_API_VERSION = "v44.0"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.soql_search, container, false)
        val sqlText = view.findViewById<EditText>(R.id.sql)
        sqlText.setText(DEFAULT_SOQL, TextView.BufferType.NORMAL)
        val sqlButton = view.findViewById<Button>(R.id.button)
        sqlButton.setOnClickListener{
            this.onClickQueryButton(view)
        }
        return view
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        println(view!!.findViewById<EditText>(R.id.sql).text.toString())
//        outState.putString("soql", view!!.findViewById<EditText>(R.id.sql).text.toString())
//    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val soql = savedInstanceState?.getString("soql")
        if (soql != null) {
            val sqlText = view?.findViewById<EditText>(R.id.sql)
            sqlText?.setText(soql, TextView.BufferType.NORMAL)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun onClickQueryButton(view: View) {
        val regexp = Regex("""FROM\s+([a-zA-Z1-9_]+)""")
        val sqlText = view.findViewById<EditText>(R.id.sql)
        val sobject = regexp.find(sqlText.text.toString())!!.groupValues[1]
        this.client.sendAsync(RestRequest.getRequestForQuery(DEFAULT_API_VERSION, sqlText.text.toString()), object : RestClient.AsyncRequestCallback {
            override fun onError(exception: Exception?) {
                println(exception)
            }

            override fun onSuccess(request: RestRequest?, response: RestResponse?) {
                val ret = response!!.asJSONObject()
                val records = convertSerializable(ret.getJSONArray("records"))
                val intent = Intent(context, RecordListActivity::class.java)
                intent.putExtra("records", records)
                intent.putExtra("sobject", sobject)
                startActivity(intent)
            }
        })
    }

    private fun convertSerializable(records: JSONArray): ArrayList<HashMap<String, String>> {
        val serializableRecords = arrayListOf<HashMap<String, String>>()
        for (i in 0 until records.length()) {
            val record = records.getJSONObject(i)
            val serializableRecord = HashMap<String, String>()
            record.keys().asSequence().filter { it != "attributes" }.forEach {
                serializableRecord[it] = record.getString(it)
            }
            serializableRecords.add(serializableRecord)
        }
        return serializableRecords
    }
}
