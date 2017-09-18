package com.clone.tetris;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

public class Ghost{

    private Bitmap positions[] = new Bitmap[4];
    private Block block;
    private int rotation = 0;
    private int posX = 0;
    private int posY = 0;
    private static int balance = 0;
    private int myShape;
    private boolean canDrawGhost = false;
    private int highestColorBlock = 0;

    public Ghost(Context context, int shape, float scaleW, float scaleH){

        block = new Block(context, scaleW, scaleH);
        myShape = shape;

        if(shape == 1) {
            positions = new Bitmap[]{BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ghostshape1_rn), BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ghostshape1_re), BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ghostshape1_rs), BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ghostshape1_rw)};
        }
        else if(shape == 2){
            positions = new Bitmap[]{BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ghostshape2_rn), BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ghostshape2_re), BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ghostshape2_rs), BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ghostshape2_rw)};
        }
        else if(shape == 3){
            positions = new Bitmap[]{BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ghostshape3_rn), BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ghostshape3_re), BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ghostshape3_rs), BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ghostshape3_rw)};
        }
        else if(shape == 4){
            positions = new Bitmap[]{BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ghostshape4_rn), BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ghostshape4_re), BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ghostshape4_rs), BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ghostshape4_rw)};
        }
        else if(shape == 5){
            positions = new Bitmap[]{BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ghostshape5_rn), BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ghostshape5_re), BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ghostshape5_rs), BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ghostshape5_rw)};
        }
        else if(shape == 6){
            positions = new Bitmap[]{BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ghostshape6_rn), BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ghostshape6_re), BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ghostshape6_rs), BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ghostshape6_rw)};
        }
        else if(shape == 7){
            positions = new Bitmap[]{BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ghostshape7_re), BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ghostshape7_rn), BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ghostshape7_rw), BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ghostshape7_rs)};
        }
    }

    //Moves the ghost shape up
    public boolean moveUp(Shape shape, Bitmap shapeBit, Bitmap background,
                          Bitmap ghostBit, int screenH){
        int oppositeColorLeft;
        int oppositeColorCenter;
        int oppositeColorRight;
        int ghostColorRight;
        int ghostColorLeft;
        int backgroundColorLeft;
        int backgroundColorRight;
        int detectedColorBlock = 1;
        int ghostBase = 1;
        boolean transparencyRL;
        boolean transparencyGL;
        boolean transparencyBL;
        boolean transparencyRR;
        boolean transparencyGR;
        boolean transparencyBR;
        int backgroundR;
        int backgroundG;
        int backgroundB;

        if(shape.getPosY() + shapeBit.getHeight() < getPosY() && getPosY() != 0) {
            for (int i = 1; i <= 39; i += 2) {

                //Makes sure each block is checked for transparency when ghost is on the bottom
                // row.
                if(getPosY() + ghostBit.getHeight() == background.getHeight()) {
                    if (getRotation() == 0 || getRotation() == 2) {
                        if (balance < 2) {
                            ghostBase += balance;
                        }
                    } else if (getRotation() == 1 || getRotation() == 3) {
                        if (balance < 3) {
                            ghostBase += balance;
                        }
                    }
                }

                if (getPosY() + ghostBit.getHeight() > screenH) {
                    setPosY(screenH - ghostBit.getHeight());
                }

                if (getRotation() == 0 || getRotation() == 2) {
                    oppositeColorCenter = background.getPixel(getPosX() + ((block.getBlockWidth() / 2) * 3),
                            background.getHeight() - ((block.getBlockHeight() / 2) * i));
                } else {
                    oppositeColorCenter = 0;
                }

                oppositeColorLeft = background.getPixel(getPosX() + (block.getBlockWidth() / 2),
                        background.getHeight() - ((block.getBlockHeight() / 2) * i));

                oppositeColorRight = background.getPixel(
                        getPosX() + ghostBit.getWidth() - (block.getBlockWidth() / 2),
                        background.getHeight() - ((block.getBlockHeight() / 2) * i));

                //Stops ghost flashing when transparent block is found on the bottom row that the
                // ghost is on.
                // <= because the bottom row must always be checked and ghostBase is at least 1
                // each time
                if ((i - balance) <= ghostBase) {
                    //System.out.println(ghostBase);
                    ghostColorLeft = ghostBit.getPixel((block.getBlockWidth() / 2),
                            ghostBit.getHeight() - ((block.getBlockHeight() / 2) * ghostBase));

                    ghostColorRight = ghostBit.getPixel(ghostBit.getWidth() - (block.getBlockWidth() / 2),
                            ghostBit.getHeight() - ((block.getBlockHeight() / 2) * ghostBase));

                    backgroundColorLeft = background.getPixel(getPosX() + (block.getBlockWidth() / 2),
                            (getPosY() + ghostBit.getHeight()) - ((block.getBlockHeight() / 2) * ghostBase));

                    backgroundColorRight = background.getPixel(
                            getPosX() + ghostBit.getWidth() - (block.getBlockWidth() / 2),
                            (getPosY() + ghostBit.getHeight()) - ((block.getBlockHeight() / 2) * ghostBase));

                    transparencyRL = Color.red(ghostColorLeft) != Color.TRANSPARENT;
                    transparencyGL = Color.green(ghostColorLeft) != Color.TRANSPARENT;
                    transparencyBL = Color.blue(ghostColorLeft) != Color.TRANSPARENT;
                    transparencyRR = Color.red(ghostColorRight) != Color.TRANSPARENT;
                    transparencyGR = Color.green(ghostColorRight) != Color.TRANSPARENT;
                    transparencyBR = Color.blue(ghostColorRight) != Color.TRANSPARENT;

                    if(!transparencyRL && !transparencyGL && !transparencyBL){
                        backgroundR = Color.red(backgroundColorLeft);
                        backgroundG = Color.green(backgroundColorLeft);
                        backgroundB = Color.blue(backgroundColorLeft);

                        if(backgroundR != 0 &&
                                backgroundG != 0 &&
                                backgroundB != 0){
                            transparencyRL = true;
                            transparencyGL = true;
                            transparencyBL = true;
                        }

                    } else if(!transparencyRR && !transparencyGR && !transparencyBR){
                        backgroundR = Color.red(backgroundColorRight);
                        backgroundG = Color.green(backgroundColorRight);
                        backgroundB = Color.blue(backgroundColorRight);

                        if(backgroundR != 0 &&
                                backgroundG != 0 &&
                                backgroundB != 0){
                            transparencyRR = true;
                            transparencyGR = true;
                            transparencyBR = true;
                        }
                    }
                } else {
                    transparencyRL = true;
                    transparencyGL = true;
                    transparencyBL = true;
                    transparencyRR = true;
                    transparencyGR = true;
                    transparencyBR = true;
                }

                if ((getPosY() + ghostBit.getHeight() > background.getHeight() -
                        (block.getBlockHeight() * (i - balance)))) {

                    if ((Color.red(oppositeColorLeft) != 0 &&
                        Color.green(oppositeColorLeft) != 0 &&
                        Color.blue(oppositeColorLeft) != 0 &&
                        transparencyRL && transparencyGL && transparencyBL) ||

                        (Color.red(oppositeColorRight) != 0 &&
                        Color.green(oppositeColorRight) != 0 &&
                        Color.blue(oppositeColorRight) != 0 &&
                        transparencyRR && transparencyGR && transparencyBR) ||

                        (Color.red(oppositeColorCenter) != 0 &&
                                Color.green(oppositeColorCenter) != 0 &&
                                Color.blue(oppositeColorCenter) != 0 &&
                                (getRotation() == 0 || getRotation() == 2))) {
                        setPosY(getPosY() - (block.getBlockHeight() * detectedColorBlock));
                        //System.out.println("UP");
                        highestColorBlock = i;
                        detectedColorBlock = 1;
                    } else {
                        detectedColorBlock++;
                    }
                } else if (i == 39) {
                    balance = 0;
                    if (highestColorBlock != 0) {
                        return true;
                    } else {
                        return false;
                    }
                }

                balance++;
            }
        }

        return false;
    }

    private boolean colorBottomLeftChecker(Ghost ghost, Bitmap bitmap, Bitmap background){

        int pixelLeft = background.getPixel((ghost.getPosX() + (block.getBlockWidth()/2)),
                ghost.getPosY() + bitmap.getHeight() + (block.getBlockHeight()/2));

        if(Color.red(pixelLeft) != 0 &&
                Color.green(pixelLeft) != 0 &&
                Color.blue(pixelLeft) != 0){
            return true;
        } else{
            return false;
        }
    }

    private boolean colorMidLeftChecker(Ghost ghost, Bitmap bitmap, Bitmap background){

        int pixelLeft = background.getPixel((ghost.getPosX() + (block.getBlockWidth()/2)),
                ghost.getPosY() + bitmap.getHeight() - (block.getBlockHeight()/2));

        if(Color.red(pixelLeft) != 0 &&
                Color.green(pixelLeft) != 0 &&
                Color.blue(pixelLeft) != 0){
            return true;
        } else{
            return false;
        }
    }

    private boolean colorTopLeftChecker(Ghost ghost, Bitmap bitmap, Bitmap background){

        int pixelLeft = background.getPixel((ghost.getPosX() + (block.getBlockWidth()/2)),
                ghost.getPosY() + bitmap.getHeight() - ((block.getBlockHeight()/2)*3));

        if(Color.red(pixelLeft) != 0 &&
                Color.green(pixelLeft) != 0 &&
                Color.blue(pixelLeft) != 0){
            return true;
        } else{
            return false;
        }

    }

    private boolean colorBottomCentreChecker(Ghost ghost, Bitmap bitmap, Bitmap background){

        int pixelMid = background.getPixel((ghost.getPosX() + (bitmap.getWidth()/2)),
                ghost.getPosY() + bitmap.getHeight() + (block.getBlockHeight()/2));

        if(Color.red(pixelMid) != 0 &&
                Color.green(pixelMid) != 0 &&
                Color.blue(pixelMid) != 0){
            return true;
        } else{
            return false;
        }
    }

    private boolean colorMidCentreChecker(Ghost ghost, Bitmap bitmap, Bitmap background){

        int pixelMid = background.getPixel((ghost.getPosX() + (bitmap.getWidth()/2)),
                ghost.getPosY() + bitmap.getHeight() - (block.getBlockHeight()/2));

        if(Color.red(pixelMid) != 0 &&
                Color.green(pixelMid) != 0 &&
                Color.blue(pixelMid) != 0){
            return true;
        } else{
            return false;
        }
    }

    private boolean colorBottomRightChecker(Ghost ghost, Bitmap bitmap, Bitmap background){

        int pixelRight = background.getPixel((ghost.getPosX() + bitmap.getWidth() -
                (block.getBlockWidth()/2)), ghost.getPosY() + bitmap.getHeight() + (block.getBlockHeight()/2));

        if(Color.red(pixelRight) != 0 &&
                Color.green(pixelRight) != 0 &&
                Color.blue(pixelRight) != 0){
            return true;
        } else{
            return false;
        }
    }

    private boolean colorMidRightChecker(Ghost ghost, Bitmap bitmap, Bitmap background){

        int pixelRight = background.getPixel((ghost.getPosX() + bitmap.getWidth() -
                (block.getBlockWidth()/2)), ghost.getPosY() + bitmap.getHeight() - (block.getBlockHeight()/2));

        if(Color.red(pixelRight) != 0 &&
                Color.green(pixelRight) != 0 &&
                Color.blue(pixelRight) != 0){
            return true;
        } else{
            return false;
        }
    }

    private boolean colorTopRightChecker(Ghost ghost, Bitmap bitmap, Bitmap background){

        int pixelLeft = background.getPixel((ghost.getPosX() + bitmap.getWidth() -
                (block.getBlockWidth()/2)), ghost.getPosY() + bitmap.getHeight() -
                ((block.getBlockHeight()/2)*3));

        if(Color.red(pixelLeft) != 0 &&
                Color.green(pixelLeft) != 0 &&
                Color.blue(pixelLeft) != 0){
            return true;
        } else{
            return false;
        }

    }

    public boolean moveDown(Ghost ghost, Bitmap bitmap, Bitmap background){

        if(ghost.getShapeType() == 1){
            if (ghost.getRotation() == 0) {
                if (colorBottomLeftChecker(ghost, bitmap, background)) {
                    return true;
                } else if (colorBottomCentreChecker(ghost, bitmap, background)) {
                    return true;
                } else if (colorBottomRightChecker(ghost, bitmap, background)) {
                    return true;
                } else {
                    return false;
                }
            } else if (ghost.getRotation() == 1) {
                if (colorBottomLeftChecker(ghost, bitmap, background)) {
                    return true;
                } else if (colorMidRightChecker(ghost, bitmap, background)) {
                    return true;
                } else {
                    return false;
                }
            } else if (ghost.getRotation() == 2) {
                if (colorMidLeftChecker(ghost, bitmap, background)) {
                    return true;
                } else if (colorBottomCentreChecker(ghost, bitmap, background)) {
                    return true;
                } else if (colorMidRightChecker(ghost, bitmap, background)) {
                    return true;
                } else {
                    return false;
                }
            } else if (ghost.getRotation() == 3) {
                if (colorMidLeftChecker(ghost, bitmap, background)) {
                    return true;
                } else if (colorBottomRightChecker(ghost, bitmap, background)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else if (ghost.getShapeType() == 2) {
            if (ghost.getRotation() == 0 || ghost.getRotation() == 2) {
                if (colorBottomLeftChecker(ghost, bitmap, background)) {
                    return true;
                } else if (colorBottomCentreChecker(ghost, bitmap, background)) {
                    return true;
                } else if (colorMidRightChecker(ghost, bitmap, background)) {
                    return true;
                } else {
                    return false;
                }
            } else if (ghost.getRotation() == 1 || ghost.getRotation() == 3) {
                if (colorMidLeftChecker(ghost, bitmap, background)) {
                    return true;
                } else if (colorBottomRightChecker(ghost, bitmap, background)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        // XXXXXX
        //    XXXXXX
        else if (ghost.getShapeType() == 3) {
            if (ghost.getRotation() == 0 || ghost.getRotation() == 2) {
                if (colorMidLeftChecker(ghost, bitmap, background)) {
                    return true;
                } else if (colorBottomCentreChecker(ghost, bitmap, background)) {
                    return true;
                } else if (colorBottomRightChecker(ghost, bitmap, background)) {
                    return true;
                } else {
                    return false;
                }
            } else if (ghost.getRotation() == 1 || ghost.getRotation() == 3) {
                if (colorBottomLeftChecker(ghost, bitmap, background)) {
                    return true;
                } else if (colorMidRightChecker(ghost, bitmap, background)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }

        }
        // XXX
        // XXXXXXXXX
        else if (ghost.getShapeType() == 4) {
            if (ghost.getRotation() == 0) {
                if (colorBottomLeftChecker(ghost, bitmap, background)) {
                    return true;
                } else if (colorBottomCentreChecker(ghost, bitmap, background)) {
                    return true;
                } else if (colorBottomRightChecker(ghost, bitmap, background)) {
                    return true;
                } else {
                    return false;
                }
            } else if (ghost.getRotation() == 1) {
                if (colorBottomLeftChecker(ghost, bitmap, background)) {
                    return true;
                } else if (colorTopRightChecker(ghost, bitmap, background)) {
                    return true;
                } else {
                    return false;
                }
            } else if (ghost.getRotation() == 2) {
                if (colorMidLeftChecker(ghost, bitmap, background)) {
                    return true;
                } else if (colorMidCentreChecker(ghost, bitmap, background)) {
                    return true;
                } else if (colorBottomRightChecker(ghost, bitmap, background)) {
                    return true;
                } else {
                    return false;
                }

            } else if (ghost.getRotation() == 3) {
                if (colorBottomLeftChecker(ghost, bitmap, background)) {
                    return true;
                } else if (colorBottomRightChecker(ghost, bitmap, background)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        //       XXX
        // XXXXXXXXX
        else if (ghost.getShapeType() == 5) {
            if (ghost.getRotation() == 0) {
                if (colorBottomLeftChecker(ghost, bitmap, background)) {
                    return true;
                } else if (colorBottomCentreChecker(ghost, bitmap, background)) {
                    return true;
                } else if (colorBottomRightChecker(ghost, bitmap, background)) {
                    return true;
                } else {
                    return false;
                }
            } else if (ghost.getRotation() == 1) {
                if (colorBottomLeftChecker(ghost, bitmap, background)) {
                    return true;
                } else if (colorBottomRightChecker(ghost, bitmap, background)) {
                    return true;
                } else {
                    return false;
                }
            } else if (ghost.getRotation() == 2) {
                if (colorBottomLeftChecker(ghost, bitmap, background)) {
                    return true;
                } else if (colorMidCentreChecker(ghost, bitmap, background)) {
                    return true;
                } else if (colorMidRightChecker(ghost, bitmap, background)) {
                    return true;
                } else {
                    return false;
                }

            } else if (ghost.getRotation() == 3) {
                if (colorTopLeftChecker(ghost, bitmap, background)) {
                    return true;
                } else if (colorBottomRightChecker(ghost, bitmap, background)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        // XXXXXX
        // XXXXXX
        else if (ghost.getShapeType() == 6) {
            if (colorBottomLeftChecker(ghost, bitmap, background)) {
                return true;
            } else if (colorBottomRightChecker(ghost, bitmap, background)) {
                return true;
            } else {
                return false;
            }
        }
        // XXXXXXXXXXXX
        else if (ghost.getShapeType() == 7) {
            if (ghost.getRotation() == 1 || ghost.getRotation() == 3) {
                if (colorBottomCentreChecker(ghost, bitmap, background)) {
                    return true;
                } else {
                    return false;
                }
            } else if (ghost.getRotation() == 0 || ghost.getRotation() == 2) {
                int leftBlockPixel = background.getPixel((ghost.getPosX() + ((bitmap.getWidth() / 8) * 3)),
                        ghost.getPosY() + bitmap.getHeight() + (block.getBlockHeight() / 2));

                int rightBlockPixel = background.getPixel((ghost.getPosX() + ((bitmap.getWidth() / 8) * 5)),
                        ghost.getPosY() + bitmap.getHeight() + (block.getBlockHeight() / 2));

                if (colorBottomLeftChecker(ghost, bitmap, background)) {
                    return true;
                } else if (Color.red(leftBlockPixel) != 0 &&
                        Color.green(leftBlockPixel) != 0 &&
                        Color.blue(leftBlockPixel) != 0) {
                    return true;
                } else if (Color.red(rightBlockPixel) != 0 &&
                        Color.green(rightBlockPixel) != 0 &&
                        Color.blue(rightBlockPixel) != 0) {
                    return true;
                } else if (colorBottomRightChecker(ghost, bitmap, background)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }else{
            return false;
        }
    }

    public Bitmap getShapeType(int shapeType){
        return positions[shapeType];
    }

    public void setRotation(int currentRotation){
        rotation = currentRotation;
    }

    public int getRotation(){
        return rotation;
    }

    public int getPosX(){
        return posX;
    }

    public void setPosX(int posX){
        this.posX = posX;
    }

    public void setPosY(int posY){
        this.posY = posY;
    }

    public int getPosY(){
        return posY;
    }

    public int getShapeType(){
        return myShape;
    }

    public void setCanDrawGhost(boolean setDrawValue){
        canDrawGhost = setDrawValue;
    }

    public boolean getCanDrawGhost(){
        return canDrawGhost;
    }

}
