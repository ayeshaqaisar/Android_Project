package com.example.dicee;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button rollbutton;
        rollbutton = findViewById(R.id.rollbutton);

        final ImageView leftDice =findViewById(R.id.image_LeftDice);
        final ImageView rightDice = findViewById(R.id.image_RightDice);

        final int[] diceArray = {
            R.drawable.dice1,R.drawable.dice2, R.drawable.dice3, R.drawable.dice4, R.drawable.dice5, R.drawable.dice6,

        };

        rollbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Dicee" , "The button has been pressed!");

                Random randomNumberGenerator = new Random();
                int number = randomNumberGenerator.nextInt(6);
                int number2 = randomNumberGenerator.nextInt(6);


                leftDice.setImageResource( diceArray[number]);
                rightDice.setImageResource( diceArray[number2]);





            }
        });

    }
}