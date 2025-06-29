package com.example.recipelist.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BroadcastReceiver(val onSomething:() -> Unit): BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        onSomething()
    }
}