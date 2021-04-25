package com.example.tictactoe_f1

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat


class MainActivity : AppCompatActivity() {

    /*
    Due to the inability to enter initial values during the constructor execution,
    each of these were preceded by the keyword lateinit.
     */

   lateinit var imageButtons: Array<Array<ImageButton>>
   lateinit var newGameButton: Button
   lateinit var textViewD: TextView
   lateinit var textViewK: TextView

    private var danielTurn: Boolean = true
    private var roundCount: Int = 0
    private var danielPoints: Int = 0
    private var kimiPoints: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    /*
    In the code below is referenced to the value initialized from lateinit above,
    each of these values is assigned an ID contained on activity_main.xml.
    */

        textViewD = findViewById(R.id.danielTextView)
        textViewK = findViewById(R.id.kimiTextView)

    // Below is created an board which include three rows and columns
        imageButtons = Array(3){row->
            Array(3){column->
                Buttons(row,column)
            }
        }

    // By clicking newGameButton the game board and points are reset
        newGameButton = findViewById(R.id.btnNewGame)
        newGameButton.setOnClickListener {
            //Points are reset to zero
            danielPoints = 0
            kimiPoints = 0
            addScore()
            clearBoard()
        }
    }



    // In this game every box is an image button
    private fun Buttons(row: Int, column: Int): ImageButton {
        val btn: ImageButton =
                findViewById(resources.getIdentifier("btn$row$column", "id", packageName))
        btn.setOnClickListener {
            onClickButton(btn)
        }
        return btn
    }



    //GAME LOGIC

    /*
    In function gameLogic it is a box value which is an array and
    contains row size three and column size three by using lambda
     */


    private fun gameLogic(): Boolean {
         val box = Array(3){row->
             Array(3){column->
                 getBox(imageButtons[row][column])
             }
         }

        /*
        For loop ranges i from 0 to 2, because to win in this case you will need three boxes next to each other(horizontal).
        If there is a true, the player scores a point.
         */
        for(i in 0..2){
            if(
                    (box[i][0] == box[i][1])&&
                    (box[i][0] == box[i][2])&&
                    (box[i][0] != null)
            ) return true
        }

        /*
        For loop ranges i from 0 to 2, because to win in this case you will need three boxes vertically.
        If there is a true, the player scores a point.
         */
        for(i in 0..2) {
            if (
                    (box[0][i] == box[1][i])&&
                    (box[0][i] == box[2][i])&&
                    (box[0][i] != null)
            ) return true
        }

        /*
        In this case to win you will need three boxes starting from upper left corner,box in the middle to lower right.
        If there is a true, the player scores a point.
         */
        if (
                (box[0][0] == box[1][1])&&
                (box[0][0] == box[2][2])&&
                (box[0][0] != null)
        ) return true

        /*
        In this case to win you will need three boxes starting from upper right corner,box in the middle to lower left.
        If there is a true, the player scores a point.
         */
        if (
                (box[0][2] == box[1][1])&&
                (box[0][2] == box[2][0])&&
                (box[0][2] != null)
        ) return true

        //if there is a false nothing happend

        return false
    }

    //In this function PNG files are uploaded into this game
    private fun getBox(btn: ImageButton): Char? {
        val drw: Drawable? = btn.drawable
        val drwDan: Drawable? = ResourcesCompat.getDrawable(resources, R.drawable.dann, null)
        val drwKimi: Drawable? = ResourcesCompat.getDrawable(resources, R.drawable.kimi2, null)

        return  when(drw?.constantState){
            drwDan?.constantState -> 'X'
            drwKimi?.constantState -> 'Y'
            else -> null
        }
    }

    //This function presents what's happen if we click separate box
    private fun onClickButton(btn: ImageButton) {
        if(btn.drawable != null) return
        if(danielTurn){
            btn.setImageResource(R.drawable.dann)
        }else{
            btn.setImageResource(R.drawable.kimi2)
        }
        roundCount++ //++ add one to scores

        if (gameLogic()){
            if(danielTurn) win(1) else win(2)
        }else if(roundCount == 9){
            draw()
        }else{
            danielTurn = !danielTurn
        }
    }

    //Function which clears all boxes and scores
    private fun clearBoard() {
        for (i in 0..2){
            for (j in 0..2){
                imageButtons[i][j].setImageResource(0)
            }
        }
        roundCount = 0
        danielTurn = true
    }



    //This function is adding point to winner player
    private fun addScore() {
        textViewD.text = "Daniel: $danielPoints"
        textViewK.text = "Kimi: $kimiPoints"

    }

    //In this function we know who win
    private fun win(driver: Int){
        if (driver == 1) danielPoints++ else kimiPoints++
        addScore()
        clearBoard()

    }

    //when it is a draw this function will work
    private fun draw(){
        Toast.makeText(applicationContext, "Draw!", Toast.LENGTH_SHORT).show()
        clearBoard()
    }

}