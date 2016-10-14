package com.example.jasonfarkas.scarnesdice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;
import android.os.Handler;


public class MainActivity extends AppCompatActivity {

    public int user_score_total = 0;
    public int user_score_current = 0;
    public int computer_score_total = 0;
    public int computer_score_current = 0;

    TextView score_view;
    ImageView dice_view;
    Button holdButton;
    Button resetButton;
    Button rollButton;

    long startTime = 0;
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            computerTurnHelper();
//            timerTextView.setText(String.format("%d:%02d", minutes, seconds));
//            timerHandler.postDelayed(this, 500);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        score_view = (TextView)findViewById(R.id.status_score);
        dice_view = (ImageView)findViewById(R.id.dice_image);
        holdButton = (Button)findViewById(R.id.holdDice);
        resetButton = (Button)findViewById(R.id.resetDice);
        rollButton = (Button)findViewById(R.id.rollDice);
    }

    public void rollDice(View v){
        Random rand = new Random();
        int random = rand.nextInt(6) + 1;
        setDice(random);
        String score_text = "";
        if (random != 1){
            user_score_current += random;
            score_text = "You rolled a "+ random+"\nYour score: "+ user_score_total+ " computer score: "+computer_score_total +" your turn score:" + user_score_current;
            score_view.setText(score_text);
        }
        else{
            user_score_current = 0;
            score_text = "You rolled a "+ random+" \n Your score: "+ user_score_total+ " computer score: "+computer_score_total +" your turn score:" + user_score_current;
            score_view.setText(score_text);
            computerTurn();
        }
    }

    private int diceRollHelper(){
        // Returns -1 if a 1 is rolled
        Random rand = new Random();
        int random = rand.nextInt(6) + 1;
        setDice(random);
        String score_text = "";
        if (random != 1){
            computer_score_current += random;
            score_text = "Computer rolled a "+rand+ "\n Your score: "+ user_score_total+ " computer score: "+computer_score_total +" Computer turn score:" + user_score_current;
            score_view.setText(score_text);
            return 0;
        }
        else{
            computer_score_current = 0;
            score_text = "Computer rolled a "+rand+ "\n Your score: "+ user_score_total+ " computer score: "+computer_score_total +" Computer turn score:" + user_score_current;
            score_view.setText(score_text);
            return -1;
        }

    }

    private void holdHelper(){
        computer_score_total += computer_score_current;
        computer_score_current= 0;
        String score_text = "Computer Holds \n Your score: "+ user_score_total+ " computer score: "+computer_score_total;
        score_view.setText(score_text);
        holdButton.setEnabled(true);
        rollButton.setEnabled(true);
    }

    public void resetDice(View v){
        timerHandler.removeCallbacks(timerRunnable);
        user_score_total = 0;
        user_score_current = 0;
        computer_score_total = 0;
        computer_score_current = 0;
        holdButton.setEnabled(true);
        rollButton.setEnabled(true);
    }

    public void holdDice(View v){
        user_score_total = user_score_total + user_score_current;
        user_score_current = 0;
        String score_text = "You hold \n Your score: "+ user_score_total+ " computer score: "+computer_score_total;
        score_view.setText(score_text);
        computerTurn();
    }

    private void computerTurnHelper(){
        timerHandler.removeCallbacks(timerRunnable);
        if (diceRollHelper() == -1 || computer_score_current >= 20){
            holdHelper();
        }
        else {
            timerHandler.postDelayed(timerRunnable, 3000);
        }
    }
    public void computerTurn(){
        holdButton.setEnabled(false);
        rollButton.setEnabled(false);
        timerHandler.postDelayed(timerRunnable, 100);
    }


    private void setDice(int n){
        if (n==1){
            dice_view.getResources().
            getResources().getDrawable((R.drawable.dice1);
        }
        else if(n==2){
            getResources().getDrawable(R.drawable.dice2);
        }
        else if(n==3){
            getResources().getDrawable(R.drawable.dice3);
        }
        else if(n==4){
            getResources().getDrawable(R.drawable.dice4);
        }
        else if(n==5){
            getResources().getDrawable(R.drawable.dice5);
        }
        else if(n==6){
            getResources().getDrawable(R.drawable.dice6);
        }
    }
}
