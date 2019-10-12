package com.freedom_man.soqlexplorer

import android.app.Application
import com.salesforce.androidsdk.app.SalesforceSDKManager

class SOQLApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SalesforceSDKManager.initNative(applicationContext, MainActivity::class.java)
    }
}