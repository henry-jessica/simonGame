package com.education.simongame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class ScoreBoard extends AppCompatActivity {

    ListView lv_scores;
    DatabaseHandler db = new DatabaseHandler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);

        lv_scores = findViewById(R.id.LV_TopScores);
        FillTopFiveScores();
    }

    public void StartNewGame(View view){
        MainActivity.CurrentScore = 0;
        MainActivity.CurrentLevel = 0;
        startActivity(new Intent(this,MainActivity.class));
        this.finish();
    }

    private void FillTopFiveScores(){
        lv_scores.setAdapter(new ScoreListViewAdapter(this,db.GetTopFive()));
    }

    public void ClearScores(View view) {
        db.emptyHiScores();
        lv_scores.setAdapter(new ScoreListViewAdapter(this,db.GetTopFive()));
    }
}