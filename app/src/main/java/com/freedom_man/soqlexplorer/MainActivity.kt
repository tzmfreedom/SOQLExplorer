package com.freedom_man.soqlexplorer

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import com.salesforce.androidsdk.rest.RestClient
import com.salesforce.androidsdk.rest.RestRequest
import com.salesforce.androidsdk.rest.RestResponse
import com.salesforce.androidsdk.ui.SalesforceActivity
import org.json.JSONArray
import java.lang.Exception

class MainActivity : SalesforceActivity() {
    private lateinit var client: RestClient

    companion object {
        const val DEFAULT_SOQL = "SELECT Id, Name FROM Contact"
        const val DEFAULT_API_VERSION = "v44.0"
    }

    override fun onResume(client: RestClient?) {
        if (client != null) {
            this.client = client
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sqlText = findViewById<EditText>(R.id.sql)
        sqlText.setText(DEFAULT_SOQL, TextView.BufferType.NORMAL)
    }

    fun onClickQueryButton(view: View) {
        val sqlText = findViewById<EditText>(R.id.sql)
        val regexp = Regex("""FROM\s+([a-zA-Z1-9_]+)""")
        val sobject = regexp.find(sqlText.text.toString())!!.groupValues[1]
        this.client.sendAsync(RestRequest.getRequestForQuery(DEFAULT_API_VERSION, sqlText.text.toString()), object : RestClient.AsyncRequestCallback {
            override fun onError(exception: Exception?) {
                println(exception)
            }

            override fun onSuccess(request: RestRequest?, response: RestResponse?) {
                val ret = response!!.asJSONObject()
                val records = convertSerializable(ret.getJSONArray("records"))
                val intent = Intent(application, RecordListActivity::class.java)
                intent.putExtra("records", records)
                intent.putExtra("sobject", sobject)
                startActivity(intent)
            }
        })
    }

    fun convertSerializable(records: JSONArray): ArrayList<HashMap<String, String>> {
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
