package com.clone.tetris;

// 31/08/2017 v0.4
//Put in ghost Shapes. Done
//Figure out why rows disappear oddly. Maybe Done
//Fix clipping issue when moving shape down forcefully. Done
//Fix ghost detection.
//Add in other windows and score system.

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class GameView extends SurfaceView implements SurfaceHolder.Callback{

    private Context myContext;
    private Bitmap background;
    private Bitmap canvasCopy;
    private SurfaceHolder mySurfaceHolder;
    private GameThread thread;
    private boolean running = false;
    private int screenW = 1;
    private int screenH = 1;
    private float scaleW;
    private float scaleH;
    private int backgroundOrigW;
    private int backgroundOrigH;
    private Bitmap shapeBit;
    private Shape shape;
    private Block block;
    private Ghost ghost;
    private Bitmap ghostBit;
    private int shapeChoice;
    private int rotation = 0;
    private ArrayList<Shape> oldShapes = new ArrayList<Shape>();
    private ArrayList<Bitmap> oldBitmaps = new ArrayList<Bitmap>();
    private Bitmap rows[] = new Bitmap[20];
    private Bitmap[] tempArray = new Bitmap[20];
    private int overWrite = -1;
    private int noOfOldShapes = 0;
    private boolean enterHorizontal = true;
    private int fingerDownXLeft = 0;
    private int fingerDownXRight = 0;
    private int fingerDownY = 0;
    private boolean positionXChanged = false;
    private boolean positionYChanged = false;
    private boolean shape7RotationRight = false;
    private boolean shape7RotationLeft = false;
    private boolean rotate = true;
    private int array[] = new int[]{1, 2, 3, 4, 5, 6, 7};
    private boolean finishedClearing = true;
    private static int shapingMovingDownTimer = 0;

    public GameView(Context context, AttributeSet attrs){
        super(context, attrs);

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        thread = new GameThread(holder, context, new Handler(){
            @Override
            public void handleMessage(Message m){

            }
        });

        setFocusable(true);
    }

    class GameThread extends Thread{

        public GameThread(SurfaceHolder surfaceHolder, Context context, Handler handler){
            mySurfaceHolder = surfaceHolder;
            myContext = context;

            background = BitmapFactory.decodeResource(myContext.getResources(),
                    R.drawable.background);
            backgroundOrigW = background.getWidth();
            backgroundOrigH = background.getHeight();
        }

        @Override
        public void run(){
            while(running){
                Canvas canvas = null;
                try{
                    canvas = mySurfaceHolder.lockCanvas();
                    synchronized (mySurfaceHolder){

                        if(canvasCopy != null) {
                            try {
                                shape.canJoinLeft(shape, canvasCopy);
                                shape.canJoinRight(shape, shapeBit, canvasCopy);

                                if (shape.getShapeMovingDown()) {
                                    shape.setCanJoinLeft(false);
                                    shape.setCanJoinRight(false);
                                }
                            } catch(Exception e){
                                e.printStackTrace();
                            }
                        }

                        draw(canvas);

                        if(shapeBit != null && finishedClearing){
                            //Create a new shape and stores old shape data
                            if(shape.moveY(screenH, shapeBit.getHeight(), thread)){
                                newShape();
                            }
                            if(shape.getPosY() + shapeBit.getHeight() != background.getHeight() &&
                                    oldShapes.size() != 0 && shape.getPosY() == ghost.getPosY()) {
                                for (int i = 0; i < oldShapes.size(); i++) {
                                    if (shape.collision(shape, shapeBit, oldShapes.get(i),
                                            oldBitmaps.get(i), canvasCopy)) {
                                        newShape();
                                    }
                                }
                            }
                        }
                    }
                }
                finally {
                    if(canvas != null){
                        mySurfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }

        private void draw(Canvas canvas){
            try {
                canvas.drawBitmap(background, 0, 0, null);

                //Keeps old shapes on screen
                if(oldShapes.size() != 0){
                    for(int i = 0; i < oldShapes.size(); i++){
                        if(oldShapes.get(i).getPosY() < background.getHeight()) {
                            canvas.drawBitmap(oldBitmaps.get(i), oldShapes.get(i).getPosX(),
                                    oldShapes.get(i).getPosY(), null);
                        }

                    }
                }

               canvasCopy = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(),
                        Bitmap.Config.RGB_565);


                Canvas replacement = new Canvas(canvasCopy);

                replacement.drawBitmap(background, 0, 0, null);

                if(oldShapes.size() != 0){
                    for(int i = 0; i < oldShapes.size(); i++){
                        if(oldShapes.get(i).getPosY() < canvasCopy.getHeight()) {
                            replacement.drawBitmap(oldBitmaps.get(i), oldShapes.get(i).getPosX(),
                                    oldShapes.get(i).getPosY(), null);
                        }

                    }
                }

                int num = checkIfRowFull();

                for (int i = 0; i < rows.length - 1; i++) {
                    canvas.drawBitmap(rows[i], 0, canvasCopy.getHeight() -
                            (block.getBlockHeight() * (i + 1)), null);

                    replacement.drawBitmap(rows[i], 0, canvasCopy.getHeight() -
                            (block.getBlockHeight() * (i + 1)), null);
                }

                if(num >= 0){

                    finishedClearing = false;

                    //System.out.println("Num is: " + num);

                    for (int j = 0; j < rows.length; j++) {
                        tempArray[j] = rows[j];
                    }

                    for(int i = 0; i < oldShapes.size(); i++){

                        //overwrite cannot be equal to num or else a bug occurs where all rows under
                        //the row that equals num do not get removed when removing the row that
                        //equals num and any other rows at the same time.
                        if(num > 0 && overWrite != num){
                            noOfOldShapes = oldShapes.size();
                            oldShapes.get(i).setPosY(oldShapes.get(i).getPosY() + block.getBlockHeight());

                            if(oldShapes.get(i).getPosY() < canvasCopy.getHeight()) {
                                canvas.drawBitmap(oldBitmaps.get(i), oldShapes.get(i).getPosX(),
                                        oldShapes.get(i).getPosY(), null);

                                replacement.drawBitmap(oldBitmaps.get(i), oldShapes.get(i).getPosX(),
                                        oldShapes.get(i).getPosY(), null);
                            }

                        } else{
                            oldShapes.get(i).setPosY(oldShapes.get(i).getPosY() + block.getBlockHeight());

                            if(oldShapes.get(i).getPosY() < canvasCopy.getHeight()) {
                                canvas.drawBitmap(oldBitmaps.get(i), oldShapes.get(i).getPosX(),
                                        oldShapes.get(i).getPosY(), null);

                                replacement.drawBitmap(oldBitmaps.get(i), oldShapes.get(i).getPosX(),
                                        oldShapes.get(i).getPosY(), null);
                            }
                        }
                    }

                    overWrite = num;
                } else{
                    finishedClearing = true;
                }

                if(overWrite >= 1) {

                    //System.out.println("Overwrite is: " + overWrite);

                    for(int i = 0; i < overWrite; i++){
                        canvas.drawBitmap(tempArray[i], 0, canvasCopy.getHeight() -
                                (block.getBlockHeight() * (i + 1)), null);

                        replacement.drawBitmap(tempArray[i], 0, canvasCopy.getHeight() -
                                (block.getBlockHeight() * (i + 1)), null);
                    }

                    for(int j = noOfOldShapes; j < oldShapes.size(); j++) {
                        //This part does work because it lets old cleared rows be filled but
                        //it also causes the bug where old shapes will keep moving down one
                        //row whenever a row is cleared.
                        /*System.out.println("Shape type: " + oldShapes.get(j).getShapeType());
                        System.out.println("Is Placed: " + oldShapes.get(j).getPlaced());*/
                            if(!shape.getPlaced()) {
                                canvas.drawBitmap(oldBitmaps.get(j), oldShapes.get(j).getPosX(),
                                        oldShapes.get(j).getPosY(), null);

                                replacement.drawBitmap(oldBitmaps.get(j), oldShapes.get(j).getPosX(),
                                        oldShapes.get(j).getPosY(), null);

                                noOfOldShapes = oldShapes.size();
                                shape.setPlaced(true);
                            }
                    }

                    for (int i = 0; i < overWrite; i++) {
                        tempArray[i] = Bitmap.createBitmap(canvasCopy, 0,
                                canvasCopy.getHeight() - (block.getBlockHeight() * (i + 1)),
                                canvasCopy.getWidth(), block.getBlockHeight());
                    }
                }

                if(shape.getPosY() != ghost.getPosY()) {
                    if (ghost.moveUp(shape, shapeBit, canvasCopy, ghostBit, screenH)) {
                        //Put ghost up

                    } else if (ghost.getPosY() != screenH - ghostBit.getHeight() &&
                            ghost.getPosY() != 0 && ghost.getPosY() >= shape.getPosY()) {
                        //Keep ghost up
                    } else {
                        //keep drawing ghost on ground
                        ghost.setPosY(screenH - ghostBit.getHeight());
                    }
                }

                if (ghost.getPosY() + ghostBit.getHeight() < screenH) {
                    /*System.out.println("screenH: " + screenH);
                    System.out.println("Ghost Y Bottom: " + (ghost.getPosY()));*/
                        while(!(ghost.moveDown(ghost, ghostBit, canvasCopy))) {
                            //System.out.println("DOWN");
                            ghost.setPosY(ghost.getPosY() + block.getBlockHeight());
                        }
                }

                canvas.drawBitmap(ghostBit, ghost.getPosX(), ghost.getPosY(), null);

                replacement.drawBitmap(ghostBit, ghost.getPosX(), ghost.getPosY(), null);

                canvas.drawBitmap(shapeBit, shape.getPosX(), shape.getPosY(), null);

                replacement.drawBitmap(shapeBit, shape.getPosX(), shape.getPosY(), null);



            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

        boolean doTouchEvent(MotionEvent event){
            int eventAction = event.getAction();
            int X = (int) event.getX();
            int Y = (int) event.getY();
            int offset = 0;
            long time = event.getEventTime() - event.getDownTime();
            boolean shapeMovingDown = false;

            if(shape.getShapeType() == 7){
                if(shape.getRotation() == 1 || shape.getRotation() == 3){
                    offset = 4;
                } else if(shape.getRotation() == 0 || shape.getRotation() == 2){
                    offset = 2;
                }
            }else if(shape.getRotation() == 1 || shape.getRotation() == 3 || shape.getShapeType() == 6){
                offset = 2;
            } else if(shape.getRotation() == 0 || shape.getRotation() == 2){
                offset = 3;
            }

            switch (eventAction) {
                case MotionEvent.ACTION_DOWN:
                    fingerDownXLeft = X - block.getBlockWidth()/2;
                    fingerDownXRight = X + block.getBlockWidth()/2;
                    fingerDownY = Y + block.getBlockHeight();
                    break;
                case MotionEvent.ACTION_MOVE:
                    //if finger is down for over 100 milliseconds move
                    if(time > 100) {
                            if (X < screenW - (shapeBit.getWidth() / offset) &&
                                    X > 0) {
                                if (Y > fingerDownY && (shape.getPosY() + block.getBlockHeight()) < ghost.getPosY()) {
                                    shapeMovingDown = true;
                                    shape.setShapeMovingDown(shapeMovingDown);
                                    shape.setCanJoinLeft(false);
                                    shape.setCanJoinRight(false);
                                    if(shapingMovingDownTimer == 10) {
                                        shape.setPosY(shape.getPosY() + block.getBlockHeight());
                                        shapingMovingDownTimer = 0;
                                    } else{
                                        shapingMovingDownTimer++;
                                    }
                                } else if ((!shapeMovingDown)) {
                                    //System.out.println("shapeCanJoinLeft: " + shape1.getCanJoinLeft());
                                    //System.out.println("shapeCanJoinRight: " + shape1.getCanJoinRight());
                                    if (shape.getPosX() != 0 && X < fingerDownXLeft &&
                                            shape.getCanJoinLeft() && enterHorizontal) {
                                        //System.out.println("Left");
                                        shape.setPosX(shape.getPosX() - block.getBlockWidth());
                                        enterHorizontal = false;
                                    } else if (shape.getPosX() + shapeBit.getWidth() != screenW &&
                                            X > fingerDownXRight && shape.getCanJoinRight() && enterHorizontal) {
                                        //System.out.println("Right");
                                        shape.setPosX(shape.getPosX() +
                                                block.getBlockWidth());
                                        enterHorizontal = false;
                                    }
                                    else if(!enterHorizontal){
                                        enterHorizontal = true;
                                    }
                                    ghost.setPosX(shape.getPosX());
                                    try {
                                        thread.sleep(200);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    //if finger is down for less than 100 milliseconds rotate
                    if(time < 100) {

                        if(shape.getShapeType() == 7){
                            for(int i = 0; i < oldShapes.size(); i++){
                                if(shape.getPosY() + shapeBit.getHeight() >= oldShapes.get(i).getPosY() &&
                                   (shape.getPosX() <= oldShapes.get(i).getPosX() + (block.getBlockWidth() * 4) ||
                                    shape.getPosX() >= oldShapes.get(i).getPosX() - (block.getBlockWidth() * 4)) &&
                                   (shape.getRotation() == 1 || shape.getRotation() == 3)){
                                    rotate = false;
                                } else{
                                    shape.setCanJoinLeft(true);
                                    shape.setCanJoinRight(true);
                                }
                            }
                        } else if(shape.getShapeType() != 6 &&
                                (shape.getRotation() == 1 || shape.getRotation() == 3) &&
                                (shape.getCanJoinRight() || shape.getCanJoinLeft())){
                            if(shape.getRotation() == 3 && !shape.getCanJoinRight()){
                                shape.setPosX(shape.getPosX() - block.getBlockWidth());
                                shape.setCanJoinRight(true);
                            } else if(shape.getRotation() == 1 && !shape.getCanJoinLeft()){
                                shape.setPosX(shape.getPosX() + block.getBlockWidth());
                                shape.setCanJoinLeft(true);
                            } else if(shape.getRotation() == 3 && !shape.getCanJoinLeft()){
                                shape.setCanJoinLeft(true);
                                shape.setCanJoinRight(true);
                            } else if(shape.getRotation() == 1 && !shape.getCanJoinRight()){
                                shape.setCanJoinLeft(true);
                                shape.setCanJoinRight(true);
                            }
                        } else if(shape.getShapeType() != 6 &&
                                (shape.getRotation() == 0 || shape.getRotation() == 2)){
                            shape.setCanJoinLeft(true);
                            shape.setCanJoinRight(true);
                        }

                        //Mskes sure shape does not clip through lower shapes when rotating
                        if(shape.getShapeType() == 7){
                            if(shape.getRotation() == 0 || shape.getRotation() == 2) {
                                for (int i = 0; i < oldShapes.size(); i++) {
                                    if (shape.getPosY() + shapeBit.getHeight() == oldShapes.get(i).getPosY() &&
                                            shape.getPosX() + shapeBit.getWidth() >= oldShapes.get(i).getPosX() &&
                                            shape.getPosX() <= oldShapes.get(i).getPosX() + oldBitmaps.get(i).getWidth()) {
                                        shape.setPosY((shape.getPosY() - shapeBit.getHeight() - block.getBlockHeight()));
                                    }
                                }
                            }
                        } else if(shape.getRotation() == 0 || shape.getRotation() == 2) {
                            for (int i = 0; i < oldShapes.size(); i++) {
                                if (shape.getPosY() + shapeBit.getHeight() == oldShapes.get(i).getPosY() &&
                                        shape.getPosX() + shapeBit.getWidth() >= oldShapes.get(i).getPosX() &&
                                        shape.getPosX() <= oldShapes.get(i).getPosX() + oldBitmaps.get(i).getWidth()) {
                                    shape.setPosY((shape.getPosY() - shapeBit.getHeight()) + block.getBlockHeight());
                                }
                            }
                        }

                        //Makes sure shape does not clip through the edge of the screen when rotating
                        if(!rotate){

                        } else if (shape.getCanJoinRight() && shape.getCanJoinLeft()) {
                            if (X >= shape.getPosX()) {
                                rotateRight();
                            } else if (X < shape.getPosX()) {
                                rotateLeft();
                            }
                        } else if (shape.getPosX() + shapeBit.getWidth() == screenW &&
                                (shape.getCanJoinRight() || shape.getCanJoinLeft())) {
                            if (shape.getRotation() == 1 || shape.getRotation() == 3) {
                                shape.setPosX(shape.getPosX() - block.getBlockWidth());
                            }
                            if (X >= shape.getPosX()) {
                                rotateRight();
                            } else if (X < shape.getPosX()) {
                                rotateLeft();
                            }
                        } else if (shape.getPosX() == 0 &&
                                (shape.getCanJoinRight() || shape.getCanJoinLeft())) {
                            if (shape.getRotation() == 1 || shape.getRotation() == 3) {
                                shape.setPosX(shape.getPosX() + block.getBlockWidth());
                            }
                            if (X >= shape.getPosX()) {
                                rotateRight();
                            } else if (X < shape.getPosX()) {
                                rotateLeft();
                            }
                        }

                        //Keeps shapes in correct positions while rotating
                        if(shape.getShapeType() == 7 && rotate){
                            if(shape7RotationRight) {
                                if (shape.getRotation() == 0) {
                                    shape.setPosX(shape.getPosX() - block.getBlockWidth());
                                    shape.setPosY(shape.getPosY() + (block.getBlockHeight()));
                                } else if (shape.getRotation() == 1) {
                                    shape.setPosX(shape.getPosX() + (block.getBlockWidth() * 2));
                                    shape.setPosY(shape.getPosY() - block.getBlockHeight());
                                } else if (shape.getRotation() == 2) {
                                    shape.setPosX(shape.getPosX() - (block.getBlockWidth() * 2));
                                    shape.setPosY(shape.getPosY() + (block.getBlockHeight() * 2));
                                } else if (shape.getRotation() == 3) {
                                    shape.setPosX(shape.getPosX() + (block.getBlockWidth()));
                                    shape.setPosY(shape.getPosY() - (block.getBlockHeight() * 2));
                                }
                                ghost.setPosX(shape.getPosX());
                            } else if(shape7RotationLeft){
                                if (shape.getRotation() == 0) {
                                    shape.setPosX(shape.getPosX() - (block.getBlockWidth() * 2));
                                    shape.setPosY(shape.getPosY() + (block.getBlockHeight()));
                                } else if (shape.getRotation() == 1) {
                                    shape.setPosX(shape.getPosX() + (block.getBlockWidth() * 2));
                                    shape.setPosY(shape.getPosY() - (block.getBlockHeight() * 2));
                                } else if (shape.getRotation() == 2) {
                                    shape.setPosX(shape.getPosX() - block.getBlockWidth());
                                    shape.setPosY(shape.getPosY() + (block.getBlockHeight() * 2));
                                } else if (shape.getRotation() == 3) {
                                    shape.setPosX(shape.getPosX() + (block.getBlockWidth()));
                                    shape.setPosY(shape.getPosY() - block.getBlockHeight());
                                }
                            }

                            if(ghost.getRotation() == 0 || ghost.getRotation() == 2){
                                ghost.setPosY(ghost.getPosY() + (block.getBlockHeight() * 3));
                            }
                        } else if(shape.getShapeType() != 6 && rotate) {
                            if (shape.getRotation() == 1 && !positionXChanged) {
                                if (positionYChanged) {
                                    shape.setPosY(shape.getPosY() - block.getBlockHeight());
                                    positionYChanged = false;
                                }
                                shape.setPosX(shape.getPosX() + block.getBlockWidth());
                                positionXChanged = true;
                            } else if (shape.getRotation() == 2 && !positionYChanged) {
                                if (positionXChanged) {
                                    shape.setPosX(shape.getPosX() - block.getBlockWidth());
                                    positionXChanged = false;
                                }
                                shape.setPosY(shape.getPosY() + block.getBlockHeight());
                                positionYChanged = true;
                            } else {
                                if (positionXChanged) {
                                    shape.setPosX(shape.getPosX() - block.getBlockWidth());
                                    positionXChanged = false;
                                }
                                if (positionYChanged) {
                                    shape.setPosY(shape.getPosY() - block.getBlockHeight());
                                    positionYChanged = false;
                                }
                            }

                            if(ghost.getRotation() == 0 || ghost.getRotation() == 2){
                                ghost.setPosY(ghost.getPosY() + block.getBlockHeight());
                            }
                        }

                        if (shape.getPosX() + shapeBit.getWidth() > screenW) {
                            shape.setPosX(screenW - shapeBit.getWidth());
                            ghost.setPosX(screenW = ghostBit.getWidth());
                        } else if (shape.getPosX() < 0) {
                            shape.setPosX(0);
                        }

                        if (shape.getPosY() < 0) {
                            shape.setPosY(0);
                        }

                        ghost.setPosX(shape.getPosX());
                        rotate = true;
                    }
                    break;

            }
            shape.setShapeMovingDown(shapeMovingDown);
            invalidate();
            return true;
        }

        public void setSurfaceSize(int width, int height){
            synchronized (mySurfaceHolder){
                screenW = width;
                screenH = height;
                scale();
            }
        }

        public void setRunning(boolean b){
            running = b;
        }

        private void rotateRight(){
            shape7RotationLeft = false;
            shape7RotationRight = true;
            rotation++;
            if (rotation > 3) {
                rotation = 0;
            }
            //System.out.println("Rotation: " + rotation);
            shape.setRotation(rotation);
            //System.out.println("Shape Rotation: " + shape.getRotation());
            shapeBit = shape.getPosition(rotation);
            shapeBit = Bitmap.createScaledBitmap(shapeBit,
                    (int)(shapeBit.getWidth() * scaleW),
                    (int)(shapeBit.getHeight() * scaleH), true);

            ghost.setRotation(rotation);
            ghostBit = ghost.getShapeType(rotation);
            ghostBit = Bitmap.createScaledBitmap(ghostBit,
                    (int)(ghostBit.getWidth() * scaleW),
                    (int)(ghostBit.getHeight() * scaleH), true);
        }

        private void rotateLeft(){
            shape7RotationLeft = true;
            shape7RotationRight = false;
            rotation--;
            if (rotation < 0) {
                rotation = 3;
            }
            //System.out.println("Rotation: " + rotation);
            shape.setRotation(rotation);
            //System.out.println("Shape Rotation: " + shape.getRotation());
            shapeBit = shape.getPosition(rotation);
            shapeBit = Bitmap.createScaledBitmap(shapeBit,
                    (int)(shapeBit.getWidth() * scaleW),
                    (int)(shapeBit.getHeight() * scaleH), true);

            ghost.setRotation(rotation);
            ghostBit = ghost.getShapeType(rotation);
            ghostBit = Bitmap.createScaledBitmap(ghostBit,
                    (int)(ghostBit.getWidth() * scaleW),
                    (int)(ghostBit.getHeight() * scaleH), true);
        }

        //Scales shapes and background to fit screen
        private void scale(){
            scaleW = (float)screenW/(float)backgroundOrigW;
            scaleH = (float)screenH/(float)backgroundOrigH;

            initShapes();

            background = Bitmap.createScaledBitmap(background, screenW, screenH, true);

            block = new Block(myContext, scaleW, scaleH);

            position();
        }

        private void newShape(){
            oldShapes.add(shape);
            oldBitmaps.add(shapeBit);

            initShapes();

            rotation = 0;

            position();
        }

        //Keeps shape in center
        private void position(){

            shapeBit = shape.getPosition(shape.getRotation());
            shapeBit = Bitmap.createScaledBitmap(shapeBit,
                    (int)(shapeBit.getWidth() * scaleW),
                    (int)(shapeBit.getHeight() * scaleH), true);

            ghostBit = ghost.getShapeType(ghost.getRotation());
            ghostBit = Bitmap.createScaledBitmap(ghostBit,
                    (int)(ghostBit.getWidth() * scaleW),
                    (int)(ghostBit.getHeight() * scaleH), true);

            if(shape.getShapeType() == 7){
                shape.setPosX((int)(screenW - (shapeBit.getWidth() * 1.75)));
                ghost.setPosX((int)(screenW - (ghostBit.getWidth() * 1.75)));
            }else if(shape.getShapeType() == 6){
                shape.setPosX((screenW - (shapeBit.getWidth() * 3)));
                ghost.setPosX((screenW - (ghostBit.getWidth() * 3)));
            } else {
                shape.setPosX((screenW - (shapeBit.getWidth() * 2)));
                ghost.setPosX((screenW - (ghostBit.getWidth() * 2)));
            }
        }

        private void initShapes(){
            boolean chosen = false;

            //System.out.println("NEW SHAPE");

            while(chosen == false) {
                if (array.length != 0) {

                    shapeChoice = randomNum(array.length, 1);

                    for (int i = 0; i < array.length; i++) {
                        if(array[i] == shapeChoice) {
                            array[i] = 0;
                        }
                    }

                    if(shape != null) {
                        shape.setPlaced(true);
                    }

                    shape = new Shape(myContext, shapeChoice, scaleW, scaleH);

                    ghost = new Ghost(myContext, shapeChoice, scaleW, scaleH);

                    int tempArray[] = new int[array.length - 1];
                    boolean past = false;

                    for (int i = 0; i < array.length - 1; i++) {
                        if (array[i] != 0 && past == false) {
                            tempArray[i] = array[i];
                        } else {
                            tempArray[i] = array[i + 1];
                            past = true;
                        }
                    }

                    array = tempArray;

                    chosen = true;
                    positionXChanged = false;
                    positionYChanged = false;

                } else {
                    array = new int[]{1, 2, 3, 4, 5, 6, 7};
                }
            }
        }

        private int randomNum(int max, int min){

            int range = (max - min) + 1;
            return array[(int)(Math.random() * range)];
        }

        private int checkIfRowFull(){

            int x = 0;
            int y = canvasCopy.getHeight() - block.getBlockHeight();
            int backgroundColor = canvasCopy.getPixel((block.getBlockWidth()/2),
                    (block.getBlockHeight()/2));
            boolean allSquares;

            for(int i = 0; i < rows.length; i++){
                Bitmap tempBitmap = Bitmap.createBitmap(canvasCopy, x, y,
                        canvasCopy.getWidth(), (canvasCopy.getHeight()/20));
                rows[i] = tempBitmap;
                y -= block.getBlockHeight();
            }


            //Stops all rows under the full row disappearing
            if(overWrite >= 1) {
                for (int j = 0; j < overWrite; j++) {
                    rows[j] = tempArray[j];
                }
            }

            try {
                for (int i = 0; i < rows.length; i++) {
                    allSquares = true;
                    for (int j = 1; j < 20; j+=2) {

                        int checkColor = rows[i].getPixel(((block.getBlockWidth()/2) * j),
                                (block.getBlockHeight()/2));

                        int R = Color.red(checkColor);
                        int G = Color.green(checkColor);
                        int B = Color.blue(checkColor);

                        if (R != Color.red(backgroundColor) &&
                                G != Color.green(backgroundColor) &&
                                B != Color.blue(backgroundColor)) {
                            if (j == 19 && allSquares) {
                                restructureBoard(i);
                                return i;
                            }
                        }
                        else{
                            allSquares = false;
                        }
                    }
                }
            }
            catch(Exception ex){
                ex.printStackTrace();
            }

            return -1;

        }

        private void restructureBoard(int startingRow){
            for(int i = startingRow; i < rows.length; i++){
                if(i != rows.length - 1) {
                    rows[i] = rows[i + 1];
                } else{
                    rows[i] = BitmapFactory.decodeResource(myContext.getResources(),
                            R.drawable.row);

                    rows[i] = Bitmap.createScaledBitmap(rows[i], screenW, screenH, true);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        return thread.doTouchEvent(event);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
        thread.setSurfaceSize(width, height);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        thread.setRunning(true);
        if (thread.getState() == Thread.State.NEW) {
            thread.start();
        }
    }

    public void pause(){
        thread.setRunning(false);
        while(true){
            try{
                thread.join();
            }catch(InterruptedException e){

            }
            break;
        }
        thread = null;
    }

    public void resume(){
        thread = new GameThread(mySurfaceHolder, myContext, new Handler());
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
    }

}
