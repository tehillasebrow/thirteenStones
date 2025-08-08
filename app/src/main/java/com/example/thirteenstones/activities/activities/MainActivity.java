package com.example.thirteenstones.activities.activities;

import static androidx.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.example.thirteenstones.activities.lib.Utils.showInfoDialog;
import static com.example.thirteenstones.activities.models.ThirteenStones.getGameFromJSON;
import static com.example.thirteenstones.activities.models.ThirteenStones.getJSONFromGame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.thirteenstones.R;
import com.example.thirteenstones.activities.models.ThirteenStones;
import com.example.thirteenstones.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ThirteenStones mGame;
    private TextView mTvStatusBarCurrentPlayer, mTvStatusBarStonesRemaining;
    private ImageView mImageViewStones;
    private Snackbar mSnackBar;

    private int[] mImages;

    private boolean mUseAutoSave;   // win on last pick is set in the model

    private final String mKEY_GAME = "GAME";
    private String mKEY_AUTO_SAVE;
    private String mKEY_WIN_ON_LAST_PICK;

    private ActivityMainBinding binding;


    @Override
    protected void onStop() {
        super.onStop();
        saveOrDeleteGameInSharedPrefs();
    }

    private void saveOrDeleteGameInSharedPrefs() {
        SharedPreferences defaultSharedPreferences = getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = defaultSharedPreferences.edit();

        // Save current game or remove any prior game to/from default shared preferences
        if (mUseAutoSave) {
            editor.putString(mKEY_GAME, mGame.getJSONFromCurrentGame());
        } else {
            editor.remove(mKEY_GAME);
        }

        editor.apply();
    }

    @Override
    protected void onStart() {
        super.onStart();

        restoreFromPreferences_SavedGameIfAutoSaveWasSetOn();
        restoreOrSetFromPreferences_AllAppAndGameSettings();
    }

    private void restoreFromPreferences_SavedGameIfAutoSaveWasSetOn() {
        SharedPreferences defaultSharedPreferences = getDefaultSharedPreferences(this);
        if (defaultSharedPreferences.getBoolean(mKEY_AUTO_SAVE, true)) {
            String gameString = defaultSharedPreferences.getString(mKEY_GAME, null);
            if (gameString != null) {
                mGame = ThirteenStones.getGameFromJSON(gameString);
                updateUI();
            }
        }
    }

    private void restoreOrSetFromPreferences_AllAppAndGameSettings() {
        SharedPreferences sp = getDefaultSharedPreferences(this);
        mUseAutoSave = sp.getBoolean(mKEY_AUTO_SAVE, true);
        if (mGame != null) {
            mGame.setWinnerIsLastPlayerToPick(sp.getBoolean(mKEY_WIN_ON_LAST_PICK, false));
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(mKEY_GAME, getJSONFromGame(mGame));
        outState.putBoolean(mKEY_AUTO_SAVE, mUseAutoSave);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mGame = getGameFromJSON(savedInstanceState.getString(mKEY_GAME));
        mUseAutoSave = savedInstanceState.getBoolean(mKEY_AUTO_SAVE, true);
        updateUI();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.includeToolbar.toolbar);
        setupViews();
        setupFAB();
        setupImagesIntArray();
        setupFields();
        startFirstGame();
    }

    private void setupViews() {
        mTvStatusBarCurrentPlayer = binding.tvStatusCurrentPlayer;
        mTvStatusBarStonesRemaining = binding.tvStatusStonesRemaining;
        mSnackBar =
                Snackbar.make(findViewById(android.R.id.content), getString(R.string.welcome),
                        Snackbar.LENGTH_LONG);
        mImageViewStones = binding.mainContent.imageViewStones;
    }

    private void setupFAB() {
        String title = getString(R.string.info_title);

        binding.fab.setOnClickListener
                (view -> showInfoDialog(this, title, mGame.getRules()));
    }

    private void setupImagesIntArray() {
        mImages = new int[]{R.drawable.stones_00, R.drawable.stones_01, R.drawable.stones_02,
                R.drawable.stones_03, R.drawable.stones_04, R.drawable.stones_05,
                R.drawable.stones_06, R.drawable.stones_07, R.drawable.stones_08,
                R.drawable.stones_09, R.drawable.stones_10, R.drawable.stones_11,
                R.drawable.stones_12, R.drawable.stones_13};
    }

    private void setupFields() {
        mKEY_AUTO_SAVE = getString(R.string.auto_save_key);
        mKEY_WIN_ON_LAST_PICK = getString(R.string.win_on_last_pick_key);
    }

    private void startFirstGame() {
        mGame = new ThirteenStones();
        updateUI();
    }

    private void startNextNewGame() {
        mGame.startGame();
        updateUI();
    }

    public void pick123(View view) {
        try {
            mGame.takeTurn(Integer.parseInt(((Button) view).getText().toString()));
            updateUI();
            showGameOverMessageIfGameNowOver();
        } catch (IllegalStateException | IllegalArgumentException e) {
            if (e.getMessage() != null) {
                mSnackBar.setText(e.getMessage());
                mSnackBar.show();
            }
        }
    }

    private void dismissSnackBarIfShown() {
        if (mSnackBar.isShown()) {
            mSnackBar.dismiss();
        }
    }

    private void showGameOverMessageIfGameNowOver() {
        if (mGame.isGameOver()) {
            dismissSnackBarIfShown();
            showInfoDialog(this, getString(R.string.game_over),
                    getString(R.string.winner_is_player_number) +
                            mGame.getWinningPlayerNumberIfGameOver() +
                            ". " + getString(
                            R.string.games_played) + mGame.getNumberOfGamesPlayed());
        }
    }

    private void updateUI() {
        dismissSnackBarIfShown();
        mTvStatusBarCurrentPlayer.setText(
                String.format(Locale.getDefault(), "%s: %d",
                        getString(R.string.current_player),
                        mGame.getCurrentPlayerNumber()));
        mTvStatusBarStonesRemaining.setText
                (String.format(Locale.getDefault(), "%s: %d",
                        getString(R.string.stones_remaining_in_pile),
                        mGame.getStonesRemaining()));
        try {
            mImageViewStones.setImageDrawable(
                    ContextCompat.getDrawable(this, mImages[mGame.getStonesRemaining()]));
        } catch (ArrayIndexOutOfBoundsException e) {
            mSnackBar.setText(R.string.error_msg_could_not_update_image);
            mSnackBar.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_new_game) {
            startNextNewGame();
            return true;
        } else if (itemId == R.id.action_statistics) {
            showStatistics();
            return true;
        } else if (itemId == R.id.action_reset_stats) {
            mGame.resetStatistics();
            return true;
        } else if (itemId == R.id.action_settings) {
            showSettings();
            return true;
        } else if (itemId == R.id.action_about) {
            showAbout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showStatistics() {
        dismissSnackBarIfShown();
        Intent intent = new Intent(getApplicationContext(), StatisticsActivity.class);
        intent.putExtra(mKEY_GAME, mGame.getJSONFromCurrentGame());
        startActivity(intent);
    }

    private void showAbout() {
        dismissSnackBarIfShown();
        showInfoDialog(MainActivity.this, "About 13 Stones",
                "A quick two-player game; have fun!\n" +
                        "\nAndroid game by SA.\nmintedtech@gmail.com");
    }

    private void showSettings() {
        dismissSnackBarIfShown();
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        settingsLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> settingsLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> restoreOrSetFromPreferences_AllAppAndGameSettings());
}