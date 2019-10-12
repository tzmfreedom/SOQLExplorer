package com.freedom_man.soqlexplorer

import android.os.Bundle
import android.widget.TableLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.salesforce.androidsdk.rest.RestClient
import com.salesforce.androidsdk.ui.SalesforceActivity

class RecordDetailActivity : SalesforceActivity() {
    private lateinit var client: RestClient

    override fun onResume(client: RestClient?) {
        if (client != null) {
            this.client = client
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val record = intent.getSerializableExtra("record") as HashMap<String, String>
        setContentView(R.layout.record_detail)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = intent.getStringExtra("name")
        val view = findViewById<TableLayout>(R.id.record_detail_table)
        var i = 0
        if (record.containsKey("Id")) {
            layoutInflater.inflate(R.layout.record_field, view)
            val row = view.getChildAt(i)
            row.findViewById<TextView>(R.id.field_label).text = "Id"
            row.findViewById<TextView>(R.id.field_value).text = record["Id"]
            i++
        }
        record.forEach { t: String, u: String ->
            if (t == "Id") return@forEach
            layoutInflater.inflate(R.layout.record_field, view)
            val row = view.getChildAt(i)
            row.findViewById<TextView>(R.id.field_label).text = t
            row.findViewById<TextView>(R.id.field_value).text = u
            i++
        }
    }
}
