package com.example.thirteenstones.activities.activities;

import android.content.Intent;
import android.os.Bundle;

import com.example.thirteenstones.activities.models.ThirteenStones;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.widget.TextView;

import com.example.thirteenstones.R;

import java.util.Locale;

public class StatisticsActivity extends AppCompatActivity {

    private TextView tvDataGamesPlayed,
            tvDataPlayer1Wins, tvDataPlayer1WinsPercent,
            tvDataPlayer2Wins, tvDataPlayer2WinsPercent;

    private ThirteenStones mCurrentGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        setupToolbar();
        setupFAB();
        setupViews();
        getIncomingData();
        processAndOutputIncomingData();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() !=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupFAB() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> onBackPressed());
    }

    private void setupViews() {
        tvDataGamesPlayed = findViewById(R.id.tv_data_games_played);
        tvDataPlayer1Wins = findViewById(R.id.tv_data_player1_wins);
        tvDataPlayer1WinsPercent = findViewById(R.id.tv_data_player1_win_percent);
        tvDataPlayer2Wins = findViewById(R.id.tv_data_player2_wins);
        tvDataPlayer2WinsPercent = findViewById(R.id.tv_data_player2_win_percent);
    }

    private void getIncomingData() {
        Intent intent = getIntent();
        String gameJSON = intent.getStringExtra("GAME");
        mCurrentGame = ThirteenStones.getGameFromJSON(gameJSON);
    }

    private void processAndOutputIncomingData() {
        final String FORMAT_STRING = "%2.1f%%", N_A = "N/A";
        int numberOfGamesPlayed = mCurrentGame.getNumberOfGamesPlayed();
        if (!mCurrentGame.isGameOver() && numberOfGamesPlayed > 0)
            numberOfGamesPlayed--;
        int p1Wins = mCurrentGame.getNumberOfWinsForPlayer(1);
        int p2Wins = mCurrentGame.getNumberOfWinsForPlayer(2);
        String p1WinPct = numberOfGamesPlayed  == 0 ? N_A :
                String.format(Locale.US, FORMAT_STRING, (p1Wins/(double)numberOfGamesPlayed)*100);
        String p2WinPct = numberOfGamesPlayed == 0 ? N_A :
                String.format(Locale.US, FORMAT_STRING, (p2Wins/(double)numberOfGamesPlayed)*100);
        tvDataGamesPlayed.setText(String.valueOf(numberOfGamesPlayed));     // don't forget String.valueOf()
        tvDataPlayer1Wins.setText(String.valueOf(p1Wins));
        tvDataPlayer2Wins.setText(String.valueOf(p2Wins));
        tvDataPlayer1WinsPercent.setText(p1WinPct);
        tvDataPlayer2WinsPercent.setText(p2WinPct);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }
}