package com.labmacc.tttoberver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

val FREE = 0 //Position free
val USER = 1 //User ID
val AGENT = 2 //Agent ID
val FAIR = 3


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ChessView(this))
    }
}