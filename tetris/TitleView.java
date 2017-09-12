package com.clone.tetris;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

public class TitleView extends View{

    private  Context myContext;
    private Bitmap titleImage;
    private Bitmap playButton;
    private int screenW;
    private int screenH;
    private boolean playButtonPressed;
    private float scale;

    public TitleView(Context context){
        super(context);
        myContext = context;
        scale = myContext.getResources().getDisplayMetrics().density;
        playButton = BitmapFactory.decodeResource(myContext.getResources(),
                R.drawable.play_button);
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH){
        super.onSizeChanged(w, h, oldW, oldH);
        screenH = h;
        screenW = w;
        Bitmap tempBitmap = BitmapFactory.decodeResource(myContext.getResources(),
                R.drawable.title_menu);
        titleImage = Bitmap.createScaledBitmap(tempBitmap, screenW, screenH, false);
    }

    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawBitmap(titleImage, 0, 0, null);
        canvas.drawBitmap(playButton, (screenW - playButton.getWidth())/2,
                (screenH)/4, null);
    }

    public boolean onTouchEvent(MotionEvent event){
        int eventAction = event.getAction();
        int X = (int)event.getX();
        int Y = (int)event.getY();

        switch(eventAction){
            case MotionEvent.ACTION_DOWN:
                if(X > (screenW - playButton.getWidth())/2 &&
                        X < ((screenW - playButton.getWidth()/2) + playButton.getWidth()) &&
                        Y > screenH/4 && Y < ((screenH/4) + playButton.getHeight())){
                    playButtonPressed = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if(playButtonPressed){
                    Intent gameIntent = new Intent(myContext, GameActivity.class);
                    myContext.startActivity(gameIntent);
                }
                playButtonPressed = false;
                break;

        }
        invalidate();
        return true;
    }

}
