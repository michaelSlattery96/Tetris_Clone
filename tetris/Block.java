package com.clone.tetris;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Michael on 9/1/2017.
 */

public class Block {

    private Bitmap block;
    private int blockWidth;
    private int blockHeight;

    public Block(Context context, float scaleW, float scaleH){

        block = BitmapFactory.decodeResource(context.getResources(), R.drawable.blue_block);

        block = Bitmap.createScaledBitmap(block, (int)(block.getWidth() * scaleW),
                (int)(block.getHeight() * scaleH), true);

        blockWidth = block.getWidth();
        blockHeight= block.getHeight();

    }

    public void setBlockWidth(int setWidth){
        blockWidth = setWidth;
    }

    public int getBlockWidth(){
        return blockWidth;
    }

    public void setBlockHeight(int setHeight){
    blockHeight = setHeight;
    }

    public int getBlockHeight(){
        return blockHeight;
    }
}
