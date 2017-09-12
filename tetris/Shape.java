package com.clone.tetris;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

public class Shape{

    private Bitmap positions[] = new Bitmap[4];
    private Block block;
    private int posX = 0;
    private int posY = 0;
    private static int count = 0;
    private int shapeType;
    private int rotation;
    private int ticks = 20;
    private boolean canJoinLeft = true;
    private boolean canJoinRight = true;
    private boolean shapeMovingDown = false;
    private boolean placed = false;

    public Shape(Context context, int shape, float scaleW, float scaleH){
        shapeType = shape;

        block = new Block(context, scaleW, scaleH);

        if(shape == 1) {
            positions = new Bitmap[]{BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.shape1_rn), BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.shape1_re), BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.shape1_rs), BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.shape1_rw)};
        }
        else if(shape == 2){
            positions = new Bitmap[]{BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.shape2_rn), BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.shape2_re), BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.shape2_rs), BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.shape2_rw)};
        }
        else if(shape == 3){
            positions = new Bitmap[]{BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.shape3_rn), BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.shape3_re), BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.shape3_rs), BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.shape3_rw)};
        }
        else if(shape == 4){
            positions = new Bitmap[]{BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.shape4_rn), BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.shape4_re), BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.shape4_rs), BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.shape4_rw)};
        }
        else if(shape == 5){
            positions = new Bitmap[]{BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.shape5_rn), BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.shape5_re), BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.shape5_rs), BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.shape5_rw)};
        }
        else if(shape == 6){
            positions = new Bitmap[]{BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.shape6_rn), BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.shape6_re), BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.shape6_rs), BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.shape6_rw)};
        }
        else if(shape == 7){
            positions = new Bitmap[]{BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.shape7_re), BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.shape7_rn), BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.shape7_rw), BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.shape7_rs)};
        }
    }

    public boolean moveY(int screenH, int scaledBitmapHeight, Thread thread){

        if (posY <= (screenH - scaledBitmapHeight)) {
            if(count < ticks){
                try {
                    thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count++;
            }
            else {
                posY += block.getBlockHeight();
                //System.out.println("Moved");
                count = 0;
            }
        }
        if (posY > (screenH - scaledBitmapHeight)) {
            posY = screenH - scaledBitmapHeight;
        }

        if(posY == (screenH - scaledBitmapHeight) && count == ticks-1){
            return true;
        }
        else{
            return false;
        }
    }

    private boolean colorBottomLeftChecker(Shape shape, Bitmap bitmap, Shape oldShape,
                                           Bitmap oldBitmap, Bitmap background){

        int pixelLeft = background.getPixel((shape.getPosX() + (block.getBlockWidth()/2)),
                shape.getPosY() + bitmap.getHeight() + (block.getBlockHeight()/2));

        if(shape.getPosY() + bitmap.getHeight() >= oldShape.getPosY() &&
                shape.getPosX() + bitmap.getWidth() > oldShape.getPosX() &&
                shape.getPosX() < oldShape.getPosX() + oldBitmap.getWidth() &&
                Color.red(pixelLeft) != 0 &&
                Color.green(pixelLeft) != 0 &&
                Color.blue(pixelLeft) != 0){
            return true;
        } else{
            return false;
        }
    }

    private boolean colorMidLeftChecker(Shape shape, Bitmap bitmap, Shape oldShape,
                                        Bitmap oldBitmap, Bitmap background){

        int pixelLeft = background.getPixel((shape.getPosX() + (block.getBlockWidth()/2)),
                shape.getPosY() + bitmap.getHeight() - (block.getBlockHeight()/2));

        if(shape.getPosY() + bitmap.getHeight() > oldShape.getPosY() &&
                shape.getPosX() + bitmap.getWidth() > oldShape.getPosX() &&
                shape.getPosX() < oldShape.getPosX() + oldBitmap.getWidth() &&
                Color.red(pixelLeft) != 0 &&
                Color.green(pixelLeft) != 0 &&
                Color.blue(pixelLeft) != 0){
            return true;
        } else{
            return false;
        }
    }

    private boolean colorTopLeftChecker(Shape shape, Bitmap bitmap, Shape oldShape,
                                        Bitmap oldBitmap, Bitmap background){

        int pixelLeft = background.getPixel((shape.getPosX() + (block.getBlockWidth()/2)),
                shape.getPosY() + bitmap.getHeight() - ((block.getBlockHeight()/2)*3));

        if(shape.getPosY() + bitmap.getHeight() > oldShape.getPosY() &&
                shape.getPosX() + bitmap.getWidth() > oldShape.getPosX() &&
                shape.getPosX() < oldShape.getPosX() + oldBitmap.getWidth() &&
                Color.red(pixelLeft) != 0 &&
                Color.green(pixelLeft) != 0 &&
                Color.blue(pixelLeft) != 0){
            return true;
        } else{
            return false;
        }

    }

    private boolean colorBottomCentreChecker(Shape shape, Bitmap bitmap, Shape oldShape,
                                             Bitmap oldBitmap, Bitmap background){

        int pixelMid = background.getPixel((shape.getPosX() + (bitmap.getWidth()/2)),
                shape.getPosY() + bitmap.getHeight() + (block.getBlockHeight()/2));

        if(shape.getPosY() + bitmap.getHeight() >= oldShape.getPosY() &&
                shape.getPosX() + bitmap.getWidth() > oldShape.getPosX() &&
                shape.getPosX() < oldShape.getPosX() + oldBitmap.getWidth() &&
                Color.red(pixelMid) != 0 &&
                Color.green(pixelMid) != 0 &&
                Color.blue(pixelMid) != 0){
            return true;
        } else{
            return false;
        }
    }

    private boolean colorMidCentreChecker(Shape shape, Bitmap bitmap, Shape oldShape,
                                          Bitmap oldBitmap, Bitmap background){

        int pixelMid = background.getPixel((shape.getPosX() + (bitmap.getWidth()/2)),
                shape.getPosY() + bitmap.getHeight() - (block.getBlockHeight()/2));

        if(shape.getPosY() + bitmap.getHeight() > oldShape.getPosY() &&
                shape.getPosX() + bitmap.getWidth() > oldShape.getPosX() &&
                shape.getPosX() < oldShape.getPosX() + oldBitmap.getWidth() &&
                Color.red(pixelMid) != 0 &&
                Color.green(pixelMid) != 0 &&
                Color.blue(pixelMid) != 0){
            return true;
        } else{
            return false;
        }
    }

    private boolean colorBottomRightChecker(Shape shape, Bitmap bitmap, Shape oldShape,
                                            Bitmap oldBitmap, Bitmap background){

        int pixelRight = background.getPixel((shape.getPosX() + bitmap.getWidth() -
                (block.getBlockWidth()/2)), shape.getPosY() + bitmap.getHeight() + (block.getBlockHeight()/2));

        if(shape.getPosY() + bitmap.getHeight() >= oldShape.getPosY() &&
                shape.getPosX() + bitmap.getWidth() > oldShape.getPosX() &&
                shape.getPosX() < oldShape.getPosX() + oldBitmap.getWidth() &&
                Color.red(pixelRight) != 0 &&
                Color.green(pixelRight) != 0 &&
                Color.blue(pixelRight) != 0){
            return true;
        } else{
            return false;
        }
    }

    private boolean colorMidRightChecker(Shape shape, Bitmap bitmap, Shape oldShape,
                                         Bitmap oldBitmap, Bitmap background){

        int pixelRight = background.getPixel((shape.getPosX() + bitmap.getWidth() -
                (block.getBlockWidth()/2)), shape.getPosY() + bitmap.getHeight() - (block.getBlockHeight()/2));

        if(shape.getPosY() + bitmap.getHeight() > oldShape.getPosY() &&
                shape.getPosX() + bitmap.getWidth() > oldShape.getPosX() &&
                shape.getPosX() < oldShape.getPosX() + oldBitmap.getWidth() &&
                Color.red(pixelRight) != 0 &&
                Color.green(pixelRight) != 0 &&
                Color.blue(pixelRight) != 0){
            return true;
        } else{
            return false;
        }
    }

    private boolean colorTopRightChecker(Shape shape, Bitmap bitmap, Shape oldShape,
                                        Bitmap oldBitmap, Bitmap background){

        int pixelLeft = background.getPixel((shape.getPosX() + bitmap.getWidth() -
                (block.getBlockWidth()/2)), shape.getPosY() + bitmap.getHeight() -
                ((block.getBlockHeight()/2)*3));

        if(shape.getPosY() + bitmap.getHeight() > oldShape.getPosY() &&
                shape.getPosX() + bitmap.getWidth() > oldShape.getPosX() &&
                shape.getPosX() < oldShape.getPosX() + oldBitmap.getWidth() &&
                Color.red(pixelLeft) != 0 &&
                Color.green(pixelLeft) != 0 &&
                Color.blue(pixelLeft) != 0){
            return true;
        } else{
            return false;
        }

    }

    public void canJoinLeft(Shape shape, Bitmap background){

        if(count != 0 && count != 19) {
            int oppositeColor;

            if (shape.getShapeType() == 7) {
                if (shape.getRotation() == 1 || shape.getRotation() == 3) {
                    for (int level = 1; level <= 7; level += 2) {
                        int shapeColor = background.getPixel(shape.getPosX() + (block.getBlockWidth() / 2),
                                shape.getPosY() + ((block.getBlockHeight() / 2) * level));

                        if (shape.getPosX() != 0) {
                            oppositeColor = background.getPixel(shape.getPosX() - (block.getBlockWidth() / 2),
                                    shape.getPosY() + ((block.getBlockHeight() / 2) * level));
                        } else {
                            oppositeColor = background.getPixel(shape.getPosX(),
                                    shape.getPosY() + ((block.getBlockHeight() / 2) * level));
                        }

                        if (Color.red(shapeColor) != 0 &&
                                Color.green(shapeColor) != 0 &&
                                Color.blue(shapeColor) != 0 &&
                                Color.red(oppositeColor) != 0 &&
                                Color.red(oppositeColor) != 0 &&
                                Color.red(oppositeColor) != 0) {
                            setCanJoinLeft(false);
                            break;
                        } else if (level == 7) {
                            setCanJoinLeft(true);
                        }
                    }
                } else if (shape.getRotation() == 0 || shape.getRotation() == 2) {
                    for (int level = 1; level < 2; level++) {
                        int shapeColor = background.getPixel(shape.getPosX() + (block.getBlockWidth() / 2),
                                shape.getPosY() + ((block.getBlockHeight() / 2) * level));

                        if (shape.getPosX() != 0) {
                            oppositeColor = background.getPixel(shape.getPosX() - (block.getBlockWidth() / 2),
                                    shape.getPosY() + ((block.getBlockHeight() / 2) * level));
                        } else {
                            oppositeColor = background.getPixel(shape.getPosX(),
                                    shape.getPosY() + ((block.getBlockHeight() / 2) * level));
                        }

                        if (Color.red(shapeColor) != 0 &&
                                Color.green(shapeColor) != 0 &&
                                Color.blue(shapeColor) != 0 &&
                                Color.red(oppositeColor) != 0 &&
                                Color.red(oppositeColor) != 0 &&
                                Color.red(oppositeColor) != 0) {
                            setCanJoinLeft(false);
                            break;
                        } else {
                            setCanJoinLeft(true);
                        }
                    }
                }

            } else if (shape.getRotation() == 0 || shape.getRotation() == 2 || shape.getShapeType() == 6) {

                for (int level = 1; level <= 3; level += 2) {
                    int shapeColor = background.getPixel((shape.getPosX() + (block.getBlockWidth() / 2)),
                            shape.getPosY() + ((block.getBlockHeight() / 2) * level));

                    if (shape.getPosX() != 0) {
                        oppositeColor = background.getPixel(shape.getPosX() - (block.getBlockWidth() / 2),
                                shape.getPosY() + ((block.getBlockHeight() / 2) * level));
                    } else {
                        oppositeColor = background.getPixel(shape.getPosX(),
                                shape.getPosY() + ((block.getBlockHeight() / 2) * level));
                    }

            /*System.out.println("INT VALUE: " + shapeColor);
            System.out.println("-----------------------------------------");
            System.out.println("Shape PosX: " + (shape.getPosX() + (blockWidth / 2)));
            System.out.println("Shape PosY: " + (shape.getPosY() + ((blockHeight / 2) * level)));
            System.out.println("Shape R: " + Color.red(shapeColor) + " Background R: " + Color.red(backgroundColor));
            System.out.println("Shape G: " + Color.green(shapeColor) + " Background G: " + Color.green(backgroundColor));
            System.out.println("Shape B: " + Color.blue(shapeColor) + " Background B: " + Color.blue(backgroundColor));
            System.out.println("Opposite R: " + Color.red(oppositeColor) + " Background R: " + Color.red(backgroundColor));
            System.out.println("Opposite G: " + Color.green(oppositeColor) + " Background G: " + Color.green(backgroundColor));
            System.out.println("Opposite B: " + Color.blue(oppositeColor) + " Background B: " + Color.blue(backgroundColor));
            System.out.println("-----------------------------------------");*/

                    if (Color.red(shapeColor) != 0 &&
                            Color.green(shapeColor) != 0 &&
                            Color.blue(shapeColor) != 0 &&
                            Color.red(oppositeColor) != 0 &&
                            Color.red(oppositeColor) != 0 &&
                            Color.red(oppositeColor) != 0) {
                        setCanJoinLeft(false);
                        break;
                    } else if (level == 3) {
                        setCanJoinLeft(true);
                    }
                }

            } else if (shape.getRotation() == 1 || shape.getRotation() == 3) {

                for (int level = 1; level <= 5; level += 2) {
                    int shapeColor = background.getPixel(shape.getPosX() + (block.getBlockWidth() / 2),
                            shape.getPosY() + ((block.getBlockHeight() / 2) * level));

                    if (shape.getPosX() != 0) {
                        oppositeColor = background.getPixel(shape.getPosX() - (block.getBlockWidth() / 2),
                                shape.getPosY() + ((block.getBlockHeight() / 2) * level));
                    } else {
                        oppositeColor = background.getPixel(shape.getPosX(),
                                shape.getPosY() + ((block.getBlockHeight() / 2) * level));
                    }

                    if (Color.red(shapeColor) != 0 &&
                            Color.green(shapeColor) != 0 &&
                            Color.blue(shapeColor) != 0 &&
                            Color.red(oppositeColor) != 0 &&
                            Color.red(oppositeColor) != 0 &&
                            Color.red(oppositeColor) != 0) {
                        setCanJoinLeft(false);
                        break;
                    } else if (level == 5) {
                        setCanJoinLeft(true);
                    }
                }
            }
        }
    }

    public void canJoinRight(Shape shape, Bitmap bitmap, Bitmap background){

        if(count != 0 && count != 19) {
            int oppositeColor;

            if (shape.getShapeType() == 7) {
                if (shape.getRotation() == 1 || shape.getRotation() == 3) {
                    for (int level = 1; level <= 7; level += 2) {
                        int shapeColor = background.getPixel(shape.getPosX() + bitmap.getWidth() -
                                        (block.getBlockWidth() / 2),
                                shape.getPosY() + ((block.getBlockHeight() / 2) * level));

                        if (shape.getPosX() + bitmap.getWidth() != background.getWidth()) {
                            oppositeColor = background.getPixel(shape.getPosX() + bitmap.getWidth() +
                                            (block.getBlockWidth() / 2),
                                    shape.getPosY() + ((block.getBlockHeight() / 2) * level));
                        } else {
                            oppositeColor = background.getPixel(background.getWidth() - 1,
                                    shape.getPosY() + ((block.getBlockHeight() / 2) * level));
                        }

                        if (Color.red(shapeColor) != 0 &&
                                Color.green(shapeColor) != 0 &&
                                Color.blue(shapeColor) != 0 &&
                                Color.red(oppositeColor) != 0 &&
                                Color.red(oppositeColor) != 0 &&
                                Color.red(oppositeColor) != 0) {
                            setCanJoinRight(false);
                            break;
                        } else if (level == 7) {
                            setCanJoinRight(true);
                        }
                    }
                } else if (shape.getRotation() == 0 || shape.getRotation() == 2) {
                    for (int level = 1; level < 2; level++) {
                        int shapeColor = background.getPixel(shape.getPosX() + bitmap.getWidth() -
                                        (block.getBlockWidth() / 2),
                                shape.getPosY() + ((block.getBlockHeight() / 2) * level));

                        if (shape.getPosX() + bitmap.getWidth() != background.getWidth()) {
                            oppositeColor = background.getPixel(shape.getPosX() + bitmap.getWidth() +
                                            (block.getBlockWidth() / 2),
                                    shape.getPosY() + ((block.getBlockHeight() / 2) * level));
                        } else {
                            oppositeColor = background.getPixel(background.getWidth() - 1,
                                    shape.getPosY() + ((block.getBlockHeight() / 2) * level));
                        }

                        if (Color.red(shapeColor) != 0 &&
                                Color.green(shapeColor) != 0 &&
                                Color.blue(shapeColor) != 0 &&
                                Color.red(oppositeColor) != 0 &&
                                Color.red(oppositeColor) != 0 &&
                                Color.red(oppositeColor) != 0) {
                            setCanJoinRight(false);
                            break;
                        } else {
                            setCanJoinRight(true);
                        }
                    }
                }

            } else if (shape.getRotation() == 0 || shape.getRotation() == 2 || shape.getShapeType() == 6) {

                for (int level = 1; level <= 3; level += 2) {
                    int shapeColor = background.getPixel(shape.getPosX() + bitmap.getWidth()
                                    - (block.getBlockWidth() / 2),
                            shape.getPosY() + ((block.getBlockHeight() / 2) * level));

                    if (shape.getPosX() + bitmap.getWidth() != background.getWidth()) {
                        oppositeColor = background.getPixel(shape.getPosX() + bitmap.getWidth() +
                                        (block.getBlockWidth() / 2),
                                shape.getPosY() + ((block.getBlockHeight() / 2) * level));
                    } else {
                        oppositeColor = background.getPixel(background.getWidth() - 1,
                                shape.getPosY() + ((block.getBlockHeight() / 2) * level));
                    }

                    if (Color.red(shapeColor) != 0 &&
                            Color.green(shapeColor) != 0 &&
                            Color.blue(shapeColor) != 0 &&
                            Color.red(oppositeColor) != 0 &&
                            Color.red(oppositeColor) != 0 &&
                            Color.red(oppositeColor) != 0) {
                        setCanJoinRight(false);
                        break;
                    } else if (level == 3) {
                        setCanJoinRight(true);
                    }
                }
            } else if (shape.getRotation() == 1 || shape.getRotation() == 3) {

                for (int level = 1; level <= 5; level += 2) {
                    int shapeColor = background.getPixel(shape.getPosX() + bitmap.getWidth() -
                                    (block.getBlockWidth() / 2),
                            shape.getPosY() + ((block.getBlockHeight() / 2) * level));

                    if (shape.getPosX() + bitmap.getWidth() != background.getWidth()) {
                        oppositeColor = background.getPixel(shape.getPosX() + bitmap.getWidth() +
                                        (block.getBlockWidth() / 2),
                                shape.getPosY() + ((block.getBlockHeight() / 2) * level));
                    } else {
                        oppositeColor = background.getPixel(background.getWidth() - 1,
                                shape.getPosY() + ((block.getBlockHeight() / 2) * level));
                    }

                    if (Color.red(shapeColor) != 0 &&
                            Color.green(shapeColor) != 0 &&
                            Color.blue(shapeColor) != 0 &&
                            Color.red(oppositeColor) != 0 &&
                            Color.red(oppositeColor) != 0 &&
                            Color.red(oppositeColor) != 0) {
                        setCanJoinRight(false);
                        break;
                    } else if (level == 5) {
                        setCanJoinRight(true);
                    }
                }
            }
        }
    }

    public boolean collision(Shape shape, Bitmap bitmap, Shape oldShape, Bitmap oldBitmap,
                             Bitmap background) {

        if(count == ticks-1 || shapeMovingDown) {
            //    XXX
            // XXXXXXXXX
            if (shape.getShapeType() == 1) {
                if (shape.getRotation() == 0) {
                    if (colorBottomLeftChecker(shape, bitmap, oldShape, oldBitmap, background)) {
                        return true;
                    } else if (colorBottomCentreChecker(shape, bitmap, oldShape, oldBitmap, background)) {
                        return true;
                    } else if (colorBottomRightChecker(shape, bitmap, oldShape, oldBitmap, background)) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (shape.getRotation() == 1) {
                    if (colorBottomLeftChecker(shape, bitmap, oldShape, oldBitmap, background)) {
                        return true;
                    } else if (colorMidRightChecker(shape, bitmap, oldShape, oldBitmap, background)) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (shape.getRotation() == 2) {
                    if (colorMidLeftChecker(shape, bitmap, oldShape, oldBitmap, background)) {
                        return true;
                    } else if (colorBottomCentreChecker(shape, bitmap, oldShape, oldBitmap, background)) {
                        return true;
                    } else if (colorMidRightChecker(shape, bitmap, oldShape, oldBitmap, background)) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (shape.getRotation() == 3) {
                    if (colorMidLeftChecker(shape, bitmap, oldShape, oldBitmap, background)) {
                        return true;
                    } else if (colorBottomRightChecker(shape, bitmap, oldShape, oldBitmap, background)) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            //    XXXXXX
            // XXXXXX
            else if (shape.getShapeType() == 2) {
                if (shape.getRotation() == 0 || shape.getRotation() == 2) {
                    if (colorBottomLeftChecker(shape, bitmap, oldShape, oldBitmap, background)) {
                        return true;
                    } else if (colorBottomCentreChecker(shape, bitmap, oldShape, oldBitmap, background)) {
                        return true;
                    } else if (colorMidRightChecker(shape, bitmap, oldShape, oldBitmap, background)) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (shape.getRotation() == 1 || shape.getRotation() == 3) {
                    if (colorMidLeftChecker(shape, bitmap, oldShape, oldBitmap, background)) {
                        return true;
                    } else if (colorBottomRightChecker(shape, bitmap, oldShape, oldBitmap, background)) {
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
            else if (shape.getShapeType() == 3) {
                if (shape.getRotation() == 0 || shape.getRotation() == 2) {
                    if (colorMidLeftChecker(shape, bitmap, oldShape, oldBitmap, background)) {
                        return true;
                    } else if (colorBottomCentreChecker(shape, bitmap, oldShape, oldBitmap, background)) {
                        return true;
                    } else if (colorBottomRightChecker(shape, bitmap, oldShape, oldBitmap, background)) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (shape.getRotation() == 1 || shape.getRotation() == 3) {
                    if (colorBottomLeftChecker(shape, bitmap, oldShape, oldBitmap, background)) {
                        return true;
                    } else if (colorMidRightChecker(shape, bitmap, oldShape, oldBitmap, background)) {
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
            else if (shape.getShapeType() == 4) {
                if (shape.getRotation() == 0) {
                    if (colorBottomLeftChecker(shape, bitmap, oldShape, oldBitmap, background)) {
                        return true;
                    } else if (colorBottomCentreChecker(shape, bitmap, oldShape, oldBitmap, background)) {
                        return true;
                    } else if (colorBottomRightChecker(shape, bitmap, oldShape, oldBitmap, background)) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (shape.getRotation() == 1) {
                    if (colorBottomLeftChecker(shape, bitmap, oldShape, oldBitmap, background)) {
                        return true;
                    } else if (colorTopRightChecker(shape, bitmap, oldShape, oldBitmap, background)) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (shape.getRotation() == 2) {
                    if (colorMidLeftChecker(shape, bitmap, oldShape, oldBitmap, background)) {
                        return true;
                    } else if (colorMidCentreChecker(shape, bitmap, oldShape, oldBitmap, background)) {
                        return true;
                    } else if (colorBottomRightChecker(shape, bitmap, oldShape, oldBitmap, background)) {
                        return true;
                    } else {
                        return false;
                    }

                } else if (shape.getRotation() == 3) {
                    if (colorBottomLeftChecker(shape, bitmap, oldShape, oldBitmap, background)) {
                        return true;
                    } else if (colorBottomRightChecker(shape, bitmap, oldShape, oldBitmap, background)) {
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
            else if (shape.getShapeType() == 5) {
                if (shape.getRotation() == 0) {
                    if (colorBottomLeftChecker(shape, bitmap, oldShape, oldBitmap, background)) {
                        return true;
                    } else if (colorBottomCentreChecker(shape, bitmap, oldShape, oldBitmap, background)) {
                        return true;
                    } else if (colorBottomRightChecker(shape, bitmap, oldShape, oldBitmap, background)) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (shape.getRotation() == 1) {
                    if (colorBottomLeftChecker(shape, bitmap, oldShape, oldBitmap, background)) {
                        return true;
                    } else if (colorBottomRightChecker(shape, bitmap, oldShape, oldBitmap, background)) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (shape.getRotation() == 2) {
                    if (colorBottomLeftChecker(shape, bitmap, oldShape, oldBitmap, background)) {
                        return true;
                    } else if (colorMidCentreChecker(shape, bitmap, oldShape, oldBitmap, background)) {
                        return true;
                    } else if (colorMidRightChecker(shape, bitmap, oldShape, oldBitmap, background)) {
                        return true;
                    } else {
                        return false;
                    }

                } else if (shape.getRotation() == 3) {
                    if (colorTopLeftChecker(shape, bitmap, oldShape, oldBitmap, background)) {
                        return true;
                    } else if (colorBottomRightChecker(shape, bitmap, oldShape, oldBitmap, background)) {
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
            else if (shape.getShapeType() == 6) {
                if (colorBottomLeftChecker(shape, bitmap, oldShape, oldBitmap, background)) {
                    return true;
                } else if (colorBottomRightChecker(shape, bitmap, oldShape, oldBitmap, background)) {
                    return true;
                } else {
                    return false;
                }
            }
            // XXXXXXXXXXXX
            else if (shape.getShapeType() == 7) {
                if (shape.getRotation() == 1 || shape.getRotation() == 3) {
                    if (colorBottomCentreChecker(shape, bitmap, oldShape, oldBitmap, background)) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (shape.getRotation() == 0 || shape.getRotation() == 2) {
                    int leftBlockPixel = background.getPixel((shape.getPosX() + ((bitmap.getWidth() / 8) * 3)),
                            shape.getPosY() + bitmap.getHeight() + (block.getBlockHeight() / 2));

                    int rightBlockPixel = background.getPixel((shape.getPosX() + ((bitmap.getWidth() / 8) * 5)),
                            shape.getPosY() + bitmap.getHeight() + (block.getBlockHeight() / 2));

                    if (colorBottomLeftChecker(shape, bitmap, oldShape, oldBitmap, background)) {
                        return true;
                    } else if (shape.getPosY() + bitmap.getHeight() >= oldShape.getPosY() &&
                            shape.getPosX() + bitmap.getWidth() > oldShape.getPosX() &&
                            shape.getPosX() < oldShape.getPosX() + oldBitmap.getWidth() &&
                            Color.red(leftBlockPixel) != 0 &&
                            Color.green(leftBlockPixel) != 0 &&
                            Color.blue(leftBlockPixel) != 0) {
                        return true;
                    } else if (shape.getPosY() + bitmap.getHeight() >= oldShape.getPosY() &&
                            shape.getPosX() + bitmap.getWidth() > oldShape.getPosX() &&
                            shape.getPosX() < oldShape.getPosX() + oldBitmap.getWidth() &&
                            Color.red(rightBlockPixel) != 0 &&
                            Color.green(rightBlockPixel) != 0 &&
                            Color.blue(rightBlockPixel) != 0) {
                        return true;
                    } else if (colorBottomRightChecker(shape, bitmap, oldShape, oldBitmap, background)) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else{
            return false;
        }
    }

    public Bitmap getPosition(int position){
        return positions[position];
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
        return shapeType;
    }

    public int getRotation(){
        return rotation;
    }

    public void setRotation(int rotation){
        this.rotation = rotation;
    }

    public void setCanJoinRight(boolean reset){
        canJoinRight = reset;
    }

    public boolean getCanJoinRight(){
        return canJoinRight;
    }

    public void setCanJoinLeft(boolean reset){
        canJoinLeft = reset;
    }

    public boolean getCanJoinLeft(){
        return canJoinLeft;
    }

    public void setShapeMovingDown(boolean isWaiting){
        shapeMovingDown = isWaiting;
    }

    public boolean getShapeMovingDown(){
        return shapeMovingDown;
    }

    public void setPlaced(boolean isPlaced){
        placed = isPlaced;
    }

    public boolean getPlaced(){
        return placed;
    }

    public int getCount(){
        return count;
    }
}
