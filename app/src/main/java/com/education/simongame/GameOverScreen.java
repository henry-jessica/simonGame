package com.education.simongame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GameOverScreen extends AppCompatActivity {

    //VARIABLES
    EditText etName;
    HiScore score;
    TextView tvGameOver,tvLevel, tvScore;


    DatabaseHandler db = new DatabaseHandler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over_screen);

        tvScore = findViewById(R.id.TV_ScoreItem);
        tvLevel = findViewById(R.id.tv_Level);
        tvGameOver = findViewById(R.id.TV_GameOver);
        etName = findViewById(R.id.ET_Name);

        // create score object from the current score and level
        score = new HiScore("Jeh",(MainActivity.CurrentScore)+"",""+(MainActivity.CurrentLevel-1));
        // show score on screen
        tvScore.setText("Your Score is: "+score.getScore());
        tvLevel.setText("Your got to level: "+score.getLevel());


        // check if the current score is a new high score
        ArrayList<HiScore> scores = db.GetTopFive();
        for(HiScore highScore : scores){
            if(score.getScoreInt() > highScore.getScoreInt()){
                tvGameOver.setText("Highest score, insert name");
                ((Button)findViewById(R.id.btn_SaveScore)).setEnabled(true);
                etName.setEnabled(true);
                break;
            }
        }
    }

    public void StartNewGame(View view){
        StartActivityFrom(MainActivity.GameState.Restart);
    }
    public void ShowScoreBoard(View view){
        StartActivityFrom(MainActivity.GameState.End);
    }
    private void StartActivityFrom(MainActivity.GameState gameState) {
        MainActivity.CurrentScore = 0;
        MainActivity.CurrentLevel = 0;
        MainActivity.gameState = gameState;
        this.finish();
    }

    public void SaveHighScore(View view) {
        score.setName(etName.getText().toString());
        db.addHiScore(score);
        Toast.makeText(this,"Saved",Toast.LENGTH_LONG).show();
        ((Button) view).setEnabled(false);
        etName.setEnabled(false);
        etName.setBackgroundColor(Color.GRAY);
    }
}