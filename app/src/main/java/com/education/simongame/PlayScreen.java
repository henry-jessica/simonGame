package com.education.simongame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;


public class PlayScreen extends AppCompatActivity implements SensorEventListener {

    //Create a enum to hold buttons
    enum UserInput{
        Book(0),
        WitchHate(1),
        Poison(2),
        Candle(3),
        Nothing(4),
        ;

        private  int Value;
        UserInput(int value){
            Value = value;
        }

        public int getValue() {
            return Value;
        }
    }
    UserInput userInput;
    SensorManager sensorManager;
    Sensor Accelerometer;
    Button btnCandle,btnPoison,btnBook,btnWitchHat;
    boolean readUserInput;
    CountUpTimer GameLoop;
    int[] correctAnswer;
    int checkIndex = 0;
    boolean gameOver = false;
    TextView tv_UserDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_screen);

        btnBook = findViewById(R.id.btn_book);
        btnWitchHat = findViewById(R.id.btn_witch);
        btnCandle = findViewById(R.id.btn_candle);
        btnPoison = findViewById(R.id.btn_poison);

        tv_UserDisplay = findViewById(R.id.TV_UserDisplay);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        userInput = UserInput.Nothing;
        readUserInput = true;
        correctAnswer = getIntent().getIntArrayExtra("sequence");
        sensorManager.registerListener(this,Accelerometer,SensorManager.SENSOR_DELAY_GAME);


        // used as a loop to animate button when the used tilt the phone
        GameLoop = new CountUpTimer(10000000) {
            public void onTick(double second) {
                // set all buttons to disabled
                btnBook.setEnabled(false);
                btnWitchHat.setEnabled(false);
                btnCandle.setEnabled(false);
                btnPoison.setEnabled(false);
                // only enable the button matching user input
                switch (userInput){
                    case Book:
                        btnBook.setEnabled(true);
                        break;
                    case WitchHate:
                        btnWitchHat.setEnabled(true);
                        break;
                    case Poison:
                        btnPoison.setEnabled(true);
                        break;
                    case Candle:
                        btnCandle.setEnabled(true);
                        break;
                }

                if(gameOver){
                    tv_UserDisplay.setText("Game Over");
                    StartGameOverScreen();
                }
                if(checkIndex >= correctAnswer.length){
                    MainActivity.CurrentScore +=4;
                    tv_UserDisplay.setText("You win");
                    MainActivity.gameState = MainActivity.GameState.Setup;
                    CleanActivity();
                }
            }
        };
        GameLoop.start();
    }

    void StartGameOverScreen(){
        CleanActivity();
        startActivity(new Intent(this,GameOverScreen.class));
    }
    void CleanActivity(){
        sensorManager.unregisterListener(this,Accelerometer);
        GameLoop.cancel();
        finish();
    }



    final int lOW_LIMIT = 2;
    final double UP_LIMIT = 3.5;

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];

        if(readUserInput) {
            // get user input
            if (x < -UP_LIMIT && (y > -lOW_LIMIT && y < lOW_LIMIT)) {
                userInput = UserInput.Book;
                readUserInput = false;
            }else if(x > UP_LIMIT && (y > -lOW_LIMIT && y < lOW_LIMIT)){
                userInput = UserInput.WitchHate;
                readUserInput = false;
            }else if (y < -UP_LIMIT && (x > -lOW_LIMIT && x < lOW_LIMIT)) {
                userInput = UserInput.Poison;
                readUserInput = false;
            }else if(y > UP_LIMIT && (x > -lOW_LIMIT && x < lOW_LIMIT)){
                userInput = UserInput.Candle;
                readUserInput = false;
            }
        }
        else if( (x > -lOW_LIMIT && x < lOW_LIMIT) && (y > -lOW_LIMIT && y < lOW_LIMIT) )
        {
            if(userInput.getValue() != correctAnswer[checkIndex] && !gameOver) {
                gameOver = true;
            }
            else if(userInput.getValue() == correctAnswer[checkIndex]){
                checkIndex++;
                userInput = UserInput.Nothing;
                readUserInput = true;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}