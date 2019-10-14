package com.freedom_man.soqlexplorer

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.salesforce.androidsdk.rest.RestClient

class MainActivity : SalesforceFragmentActivity() {
    private lateinit var client: RestClient
    private lateinit var soqlSearch: SoqlSearch
    private lateinit var simpleSearch: SimpleSearch

    override fun onResume(client: RestClient?) {
        if (client != null) {
            this.client = client
            this.soqlSearch = SoqlSearch(this.client)
            this.simpleSearch = SimpleSearch(this.client)

            supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment, SoqlSearch(this.client))
                .commit()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navigation = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.soql_search_item -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, this.soqlSearch)
                        .commit()
                    true
                }
                R.id.simple_search_item -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, this.simpleSearch)
                        .commit()
                    true
                }
                else -> {
                    false
                }
            }
        }
    }
}
