package test.example.com.demo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var diceImage : ImageView
    lateinit var test : Button
    lateinit var next : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        diceImage = findViewById(R.id.ivDice)
        test = findViewById(R.id.btnTest)
        next = findViewById(R.id.tvNext)

        next.setOnClickListener {
            val intent = Intent(this,OneActivity::class.java)
            startActivity(intent)
        }

        test.setOnClickListener{

            val randomInt = Random().nextInt(6)+1
            val drawableResource = when(randomInt){
                1 -> R.drawable.dice_1
                2 -> R.drawable.dice_2
                3 -> R.drawable.dice_3
                4 -> R.drawable.dice_4
                5 -> R.drawable.dice_5
                else -> R.drawable.dice_6
            }
            diceImage.setImageResource(drawableResource)
        }
    }
}
