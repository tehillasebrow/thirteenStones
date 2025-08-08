package com.example.thirteenstones;

import android.content.Intent;
import android.os.Bundle;

import com.example.thirteenstones.activities.StatisticsActivity;
import com.example.thirteenstones.activities.lib.DialogUtils;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;



import com.example.thirteenstones.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.includeToolbar.toolbar);



        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new_game) {
            startNextNewGame();
            return true;
        } else if (id == R.id.action_statistics) {
            showStatistics();
            return true;
        } else if (id == R.id.action_reset_stats) {
            // mGame.resetStatistics();
            return true;
        } else if (id == R.id.action_settings) {
            showSettings();
            return true;
        } else if (id == R.id.action_about) {
            showAbout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

        private void startNextNewGame() {
    }

    private void showStatistics() { Intent intent = new Intent(getApplicationContext(), StatisticsActivity.class);
        startActivity(intent);
    }

    private void showSettings() {
    }

    private void showAbout() {
        DialogUtils.showInfoDialog(this, "About 13 Stones", "This is our second in-class app of the semester!");
    }


}