package com.labmacc.tttoberver

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson

var winner = FREE
var chess = IntArray(9) { FREE }

class ChessView(context: Context?) :  View(context) {


    var dx = 0f
    var dy = 0f

    val linepaint = Paint().apply {
        color = Color.WHITE
        strokeWidth = 10f
        style = Paint.Style.STROKE
        textSize=25f
    }
    val textpaint = Paint().apply {
        color = Color.CYAN
        strokeWidth = 1f
        style = Paint.Style.FILL
        textSize=100f
    }

    val yourTurnMessage="Observing Playing"
    var message = yourTurnMessage

    val myDatabase = Firebase
        .database("https://tictactoev2-334d2-default-rtdb.europe-west1.firebasedatabase.app/")

    var remoteWinner  = myDatabase.getReference("winner")
    var remoteChessboard = myDatabase.getReference("chessboard")



    val chessBoardListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // Get Post object and use the values to update the UI
            val value = dataSnapshot.getValue<String>()
            chess =Gson().fromJson(value, chess.javaClass)
            invalidate()

        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Post failed, log a message
            Log.w("CHESS", "loadPost:onCancelled", databaseError.toException())
        }
    }

    val winnerListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // Get Post object and use the values to update the UI
            winner = dataSnapshot.getValue<String>()!!.toInt()
            invalidate()
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Post failed, log a message
            Log.w("CHESS", "loadPost:onCancelled", databaseError.toException())
        }
    }


    init {
        myDatabase.reference.child("chessboard").get().addOnSuccessListener {
            Log.i("CHESS",""+it.key+" "+it.value)
        }
        remoteChessboard.addValueEventListener(chessBoardListener)
        remoteWinner.addValueEventListener(winnerListener)

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        dx = width / 3f
        dy = height / 3f

        canvas?.drawRGB(255, 4, 244)
        canvas?.drawText(message, 10f,100f,textpaint)


        if (winner == USER) canvas?.drawRGB(0, 255, 0)
        if (winner == AGENT) canvas?.drawRGB(255, 0, 0)
        if (winner == FAIR) canvas?.drawRGB(255, 255, 0)

        canvas?.drawLine(dx, 0f, dx, height.toFloat(), linepaint)
        canvas?.drawLine(2 * dx, 0f, 2 * dx, height.toFloat(), linepaint)

        canvas?.drawLine(0f, dy, width.toFloat(), dy, linepaint)
        canvas?.drawLine(0f, 2 * dy, width.toFloat(), 2 * dy, linepaint)

        for (k in 0..8) {
            if (chess[k] == USER) //draw a Circle
            {
                val i = k % 3
                val j = k / 3
                // Toast.makeText(context, ""+i+" "+j, Toast.LENGTH_SHORT).show()
                canvas?.drawCircle(
                    dx * i.toFloat() + dx / 2,
                    dy * j.toFloat() + dy / 2,
                    dx / 2f,
                    linepaint
                )
            }

            if (chess[k] == AGENT) { //draw a cross
                val i = k % 3
                val j = k / 3

                canvas?.drawLine(i * dx, j * dy, (i + 1) * dx, (j + 1) * dy, linepaint)
                canvas?.drawLine((i + 1) * dx, j * dy, i * dx, (j + 1) * dy, linepaint)
                //Toast.makeText(context, ""+i+" "+j, Toast.LENGTH_SHORT).show()

            }
        }

    }

}