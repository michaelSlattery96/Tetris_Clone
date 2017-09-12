package com.clone.tetris;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

public class GameActivity extends AppCompatActivity{

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.game_layout);
        gameView = (GameView)findViewById(R.id.tetris);
        gameView.setKeepScreenOn(true);
    }

    @Override
    public void onPause(){
        super.onPause();

        gameView.pause();
    }

    @Override
    public void onResume(){
        super.onResume();

        gameView.resume();
    }
}
