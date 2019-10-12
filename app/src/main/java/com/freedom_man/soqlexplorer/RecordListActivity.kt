package com.freedom_man.soqlexplorer

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.salesforce.androidsdk.rest.RestClient
import com.salesforce.androidsdk.ui.SalesforceActivity

class RecordListActivity : SalesforceActivity() {
    private lateinit var client: RestClient

    override fun onResume(client: RestClient?) {
        if (client != null) {
            this.client = client
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.record_list)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = intent.getStringExtra("sobject") + " List"
        val records = intent.getSerializableExtra("records") as ArrayList<HashMap<String, String>>
        val viewRecords = ArrayList<String>()
        records.forEach {
            viewRecords.add(getTitle(it))
        }
        val adapter = ArrayAdapter<String>(application, android.R.layout.simple_list_item_1, viewRecords)
        val listView = findViewById<ListView>(R.id.records)
        listView.adapter = adapter
        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val record = records[position]
            val intent = Intent(application, RecordDetailActivity::class.java)
            intent.putExtra("record", record)
            intent.putExtra("name", viewRecords[position])
            startActivity(intent)
        }
    }

    fun getTitle(record: HashMap<String, String>): String {
        return if (record.containsKey("Name")) {
            record["Name"]!!
        } else if (record.containsKey("Id")){
            record["Id"]!!
        } else {
            val key = record.keys.filter { it != "attributes" }.first()
            record[key].toString()
        }
    }
}
