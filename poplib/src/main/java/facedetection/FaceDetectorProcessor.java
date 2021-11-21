package facedetection;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/** Face Detector Demo. */
public class FaceDetectorProcessor extends VisionProcessorBase<List<Face>> {

    private static final String TAG = "FaceDetectorProcessor";
    private  Context ctx;
    private final FaceDetector detector;

    private long starttime;
    public static int x;
    public static int y;
    private int[] correctpath;
    private int[] correctfullpath;
    private int[] wrongpath;
    int ballRadius = 15;
    private int depth;
    private int currLevel;
    private String currtext1;
    private String currtext2;
    private int currPassedLevel;
    public int startx;
    public int starty;
    private int mindepth=10;
    private String popText = "";
    private  DisplayMetrics displayMetrics;

    ArrayList<ShapeDrawable> sectionList = new ArrayList<ShapeDrawable>();
    ArrayList<ShapeDrawable> rectList = new ArrayList<ShapeDrawable>();
    ArrayList<int[]> nodePosList = new ArrayList<int[]>();
    Path fullPath = new Path();
    ShapeDrawable mBall = new ShapeDrawable(new OvalShape());
    ShapeDrawable mTargetGreen = new ShapeDrawable(new RectShape());
    ShapeDrawable mTargetUp = new ShapeDrawable(new RectShape());
    ArrayList<ShapeDrawable> mWrongTargets = new ArrayList<ShapeDrawable>();
    ArrayList<ShapeDrawable> nodeList = new ArrayList<ShapeDrawable>();
    Paint mPaintPath = new Paint();


    public FaceDetectorProcessor(Context context, FaceDetectorOptions options, int depth, String pt) {
        super(context);
        popText = pt;
        ctx = context;
        detector = FaceDetection.getClient(options);
        setupPuzzle(depth);
        drawSections(correctpath[currLevel]);
        drawPaths();
    }

    @Override
    public void stop() {
        super.stop();
        detector.close();
    }

    @Override
    protected Task<List<Face>> detectInImage(InputImage image) {
        return detector.process(image);
    }

    @Override
    protected void onSuccess(@NonNull List<Face> faces, @NonNull GraphicOverlay graphicOverlay) {


        if(faces.size()<1){
            graphicOverlay.add(
                    new PoPLPuzzleGraphic(this,graphicOverlay,rectList,sectionList,mBall,mTargetGreen,mTargetUp,mWrongTargets,nodeList,mPaintPath,currtext1,currtext2,fullPath,popText));

        }else{
            Face face = faces.get(0);
                y-=(int)(face.getHeadEulerAngleX()-6)*1.5;
                x-=(int)face.getHeadEulerAngleY()*1.5;
                updateMovement();
                graphicOverlay.add(
                        new PoPLPuzzleGraphic(this,graphicOverlay,rectList,sectionList,mBall,mTargetGreen,mTargetUp,mWrongTargets,nodeList,mPaintPath,currtext1,currtext2,fullPath,popText));

        }
    }

    private void setupPuzzle(int _depth){
        displayMetrics = ctx.getResources().getDisplayMetrics();
        depth = _depth;
        starttime = System.currentTimeMillis();
        correctfullpath = new int[depth*7];
        correctpath = new int[depth];
        wrongpath = new int[depth];
        currLevel = currPassedLevel = 0;
        SecureRandom random = new SecureRandom();
        for(int i=0;i<depth;i++){
            correctpath[i] = random.nextInt(8) ;
        }for(int i=0;i<depth;i++){
            int[] levelpath = getSequenceForLevel(correctpath[i]);
            for(int j=0;j<7;j++){
                correctfullpath[(j)+(i*7)]=levelpath[j];
            }
        }

        int screenWidth = displayMetrics.widthPixels;
        startx=x=screenWidth/2;
        starty=y=11 + (2*ballRadius);
        currtext1 = "Place ball in ";
        currtext2 = "green zone";

    }

    private int[] getSequenceForLevel(int levelSolution){
        int[] out = {3,0,3,0,3,0,3};
        switch(levelSolution) {
            case 1:
                out[5]=1;
                break;
            case 2:
                out[5]=1;
                break;
            case 3:
                out[3]=1;
                break;
            case 4:
                out[3]=1;
                out[5]=1;
                break;
            case 5:
                out[1]=1;
                break;
            case 6:
                out[1]=1;
                out[5]=1;
                break;
            case 7:
                out[1]=1;
                out[3]=1;
                out[5]=1;
                break;
            default:
                break;
        }
        return  out;
    }



    private boolean contains(ShapeDrawable path, ShapeDrawable ball){
        if(path.getBounds().contains(ball.getBounds().left+1,ball.getBounds().top+1, ball.getBounds().right+1,ball.getBounds().bottom+1)){
            return true;
        }else return false;
    }

    private boolean isHorizontal(ShapeDrawable path){
        if(path.getBounds().right-path.getBounds().left > path.getBounds().bottom-path.getBounds().top){
            return true;
        }else return false;
    }


    private boolean intersects(ShapeDrawable path, ShapeDrawable ball){
        if(path.getBounds().intersects(ball.getBounds().left,ball.getBounds().top, ball.getBounds().right,ball.getBounds().bottom)){
            return true;
        }else return false;
    }
    private void drawSplitInRect(int left, int top, int right, int bottom){
        int width = 20;

        ShapeDrawable mDrawableRect = new ShapeDrawable(new RectShape());
        mDrawableRect.getPaint().setColor(Color.BLACK);
        mDrawableRect.setBounds((left + ((right-left)/4))-width, top-width, (left + ((right-left)/4))+width, bottom+width);
        rectList.add(mDrawableRect);

        ShapeDrawable mDrawableRect2 = new ShapeDrawable(new RectShape());
        mDrawableRect2.getPaint().setColor(Color.BLACK);
        mDrawableRect2.setBounds((left + ((3*(right-left))/4))-width , top-width, width + left + ((3*(right-left))/4), bottom + width);
        rectList.add(mDrawableRect2);

        ShapeDrawable mDrawableRect3 = new ShapeDrawable(new RectShape());
        mDrawableRect3.getPaint().setColor(Color.BLACK);
        mDrawableRect3.setBounds((left + ((right-left)/4))-width , top-width, width + left + ((3*(right-left))/4), top + width);
        rectList.add(mDrawableRect3);

    }
    //path for ball to move in
    private void drawPaths() {
        int screenHeight = displayMetrics.heightPixels;
        int screenWidth = displayMetrics.widthPixels;
        int width = 20;
        int shift = screenHeight/8;
        mBall.getPaint().setColor(Color.GREEN);
        mBall.setBounds((screenWidth/2)-ballRadius,0+shift-ballRadius,(screenWidth/2)+ballRadius,0+shift+ballRadius );
        ShapeDrawable mEntryRect = new ShapeDrawable(new RectShape());
        mEntryRect.getPaint().setColor(Color.BLACK);
        mEntryRect.setBounds((screenWidth/2)-width,0,(screenWidth/2)+width,shift+width) ;
        rectList.add(mEntryRect);
        drawSplitInRect(0,0+shift, screenWidth, screenHeight/4+shift);
        drawSplitInRect(0,screenHeight/4+shift, screenWidth/2, screenHeight/2+shift);
        drawSplitInRect(screenWidth/2,screenHeight/4+shift, screenWidth, screenHeight/2+shift);
        drawSplitInRect(0,screenHeight/2+shift, screenWidth/4, (screenHeight)/2+2*shift);
        drawSplitInRect(screenWidth/4,screenHeight/2+shift, screenWidth/2, (screenHeight)/2+2*shift);
        drawSplitInRect(screenWidth/2,screenHeight/2+shift, (3*screenWidth/4), (screenHeight)/2+2*shift);
        drawSplitInRect((3*screenWidth/4),screenHeight/2+shift, screenWidth, (screenHeight)/2+2*shift);
        //drawBottomNodes();
        drawGuide();
    }


    private void drawGuide() {
        fullPath.reset();
        int screenHeight = displayMetrics.heightPixels;
        int screenWidth = displayMetrics.widthPixels;
        int shift= screenHeight/8;
        if(currPassedLevel>= currLevel && currLevel<correctpath.length){
            int greenpos = correctpath[currLevel];

            fullPath.moveTo(screenWidth/2 , 0);
            fullPath.lineTo(screenWidth/2 , shift);

            if(greenpos==0){
                fullPath.lineTo(screenWidth/4 , shift);
                fullPath.lineTo(screenWidth/4 , screenHeight/4+shift);
                fullPath.lineTo(screenWidth/8 , screenHeight/4+shift);
                fullPath.lineTo(screenWidth/8 , screenHeight/2+shift);
                fullPath.lineTo(screenWidth/8 - screenWidth/16 , screenHeight/2+shift);
                fullPath.lineTo(screenWidth/8 - screenWidth/16 , screenHeight/2+2*shift);

            }else if(greenpos==1){
                fullPath.lineTo(screenWidth/4 , shift);
                fullPath.lineTo(screenWidth/4 , screenHeight/4+shift);
                fullPath.lineTo(screenWidth/8 , screenHeight/4+shift);
                fullPath.lineTo(screenWidth/8 , screenHeight/2+shift);
                fullPath.lineTo(screenWidth/8  + screenWidth/16, screenHeight/2+shift);
                fullPath.lineTo(screenWidth/8  + screenWidth/16, screenHeight/2+2*shift);

            }else if(greenpos==2){
                fullPath.lineTo(screenWidth/4 , shift);
                fullPath.lineTo(screenWidth/4 , screenHeight/4+shift);
                fullPath.lineTo(screenWidth/4 + screenWidth/8 , screenHeight/4+shift);
                fullPath.lineTo(screenWidth/4 + screenWidth/8 , screenHeight/2+shift);
                fullPath.lineTo(screenWidth/4 + screenWidth/8 - screenWidth/16 , screenHeight/2+shift);
                fullPath.lineTo(screenWidth/4 + screenWidth/8 - screenWidth/16 , screenHeight/2+2*shift);

            }else if(greenpos==3){
                fullPath.lineTo(screenWidth/4 , shift);
                fullPath.lineTo(screenWidth/4 , screenHeight/4+shift);
                fullPath.lineTo(screenWidth/4 + screenWidth/8 , screenHeight/4+shift);
                fullPath.lineTo(screenWidth/4 + screenWidth/8 , screenHeight/2+shift);
                fullPath.lineTo(screenWidth/4 + screenWidth/8 + screenWidth/16, screenHeight/2+shift);
                fullPath.lineTo(screenWidth/4 + screenWidth/8 + screenWidth/16, screenHeight/2+2*shift);

            }else if(greenpos==4){
                fullPath.lineTo((3*screenWidth)/4 , shift);
                fullPath.lineTo((3*screenWidth)/4 , screenHeight/4+shift);
                fullPath.lineTo((3*screenWidth)/4 - screenWidth/8 , screenHeight/4+shift);
                fullPath.lineTo((3*screenWidth)/4 - screenWidth/8 , screenHeight/2+shift);
                fullPath.lineTo((3*screenWidth)/4 - screenWidth/8 - screenWidth/16 , screenHeight/2+shift);
                fullPath.lineTo((3*screenWidth)/4 - screenWidth/8 - screenWidth/16 , screenHeight/2+2*shift);

            }else if(greenpos==5){
                fullPath.lineTo((3*screenWidth)/4 , shift);
                fullPath.lineTo((3*screenWidth)/4 , screenHeight/4+shift);
                fullPath.lineTo((3*screenWidth)/4 - screenWidth/8 , screenHeight/4+shift);
                fullPath.lineTo((3*screenWidth)/4 - screenWidth/8 , screenHeight/2+shift);
                fullPath.lineTo((3*screenWidth)/4 - screenWidth/8 + screenWidth/16 , screenHeight/2+shift);
                fullPath.lineTo((3*screenWidth)/4 - screenWidth/8 + screenWidth/16 , screenHeight/2+2*shift);

            }else if(greenpos==6){
                fullPath.lineTo((3*screenWidth)/4 , shift);
                fullPath.lineTo((3*screenWidth)/4 , screenHeight/4+shift);
                fullPath.lineTo((3*screenWidth)/4 +screenWidth/8, screenHeight/4+shift);
                fullPath.lineTo((3*screenWidth)/4 +screenWidth/8, screenHeight/2+shift);
                fullPath.lineTo((3*screenWidth)/4 +screenWidth/8 - screenWidth/16, screenHeight/2+shift);
                fullPath.lineTo((3*screenWidth)/4 +screenWidth/8 - screenWidth/16, screenHeight/2+2*shift);

            }else if(greenpos==7){
                fullPath.lineTo((3*screenWidth)/4 , shift);
                fullPath.lineTo((3*screenWidth)/4 , screenHeight/4+shift);
                fullPath.lineTo((3*screenWidth)/4 +screenWidth/8 , screenHeight/4+shift);
                fullPath.lineTo((3*screenWidth)/4 +screenWidth/8 ,screenHeight/2+shift);
                fullPath.lineTo((3*screenWidth)/4 +screenWidth/8 + screenWidth/16,screenHeight/2+shift);
                fullPath.lineTo((3*screenWidth)/4 +screenWidth/8 + screenWidth/16,screenHeight/2+2*shift);

            }
            //fullPath.close();
        }
        int max = (depth*2)-(currLevel*2);
        mPaintPath.setStyle(Paint.Style.STROKE);
        mPaintPath.setColor(Color.GREEN);
        mPaintPath.setStrokeWidth(8f);
    }

    //draw Color sections with green at position greenpos
    private void drawSections(int greenpos){
        sectionList.clear();
        int screenHeight = displayMetrics.heightPixels;
        int screenWidth = displayMetrics.widthPixels;
        int shift = screenHeight/8;
        int rectWidth=screenWidth/8;
        int rectHeight=screenHeight/2 - 3*(shift/2);
        int[] colors = {Color.RED, Color.BLUE, Color.YELLOW, Color.MAGENTA, Color.GRAY, Color.CYAN, Color.BLACK, Color.LTGRAY};
        mTargetUp= new ShapeDrawable(new RectShape());
        mTargetUp.setBounds(0, 0, screenWidth , 10 );
        mTargetUp.getPaint().setColor(Color.GREEN);
        mTargetUp.setAlpha(60);
        int colcount = 0;
        mWrongTargets.clear();
        for(int i = 0; i<8; i++){
            ShapeDrawable mDrawableSection = new ShapeDrawable(new RectShape());
            mDrawableSection.setBounds(rectWidth*i, screenHeight - rectHeight, rectWidth*(i+1) , screenHeight );
            if(i==greenpos){
                mDrawableSection.getPaint().setColor(Color.GREEN);
                ShapeDrawable placeholder = new ShapeDrawable(new RectShape());
                mWrongTargets.add(placeholder);
                mTargetGreen = mDrawableSection;
            }else{
                mDrawableSection.getPaint().setColor(colors[colcount]);
                mWrongTargets.add(mDrawableSection);
                colcount++;
            }
            mDrawableSection.getPaint().setAlpha(130);
            sectionList.add(mDrawableSection);
        }
    }

    private void updateMovement(){

        ArrayList<ShapeDrawable> containedRects = new ArrayList<>();
        for (ShapeDrawable rc:rectList
        ) {
            if (contains(rc,mBall)){
                containedRects.add(rc);
            }
        }

        if(containedRects.size()==1){
            ShapeDrawable rect = containedRects.get(0);
            y = Math.max(y,rect.getBounds().top + ballRadius+1);
            y = Math.min(y,rect.getBounds().bottom -ballRadius-1);
            x = Math.max(x,rect.getBounds().left +ballRadius+1);
            x = Math.min(x,rect.getBounds().right - ballRadius-1);

        }else if(containedRects.size()==2){
            ShapeDrawable horrect ;
            ShapeDrawable verrect ;
            ShapeDrawable rect1 = containedRects.get(0);
            ShapeDrawable rect2 = containedRects.get(1);
            if(isHorizontal(rect1)){
                horrect = rect1;
                verrect = rect2;
            }else{
                horrect = rect2;
                verrect = rect1;
            }

            int hortop = horrect.getBounds().top + ballRadius+1;
            int horbottom = horrect.getBounds().bottom -ballRadius-1;
            int horleft = horrect.getBounds().left +ballRadius+1;
            int horright =horrect.getBounds().right - ballRadius-1;
            int vertop = verrect.getBounds().top + ballRadius+1;
            int verbottom = verrect.getBounds().bottom -ballRadius-1;
            int verleft = verrect.getBounds().left +ballRadius+1;
            int verright =verrect.getBounds().right - ballRadius-1;

            y = Math.max(y,vertop);
            y = Math.min(y,verbottom);
            if(y<hortop){
                x = Math.max(x,verleft);
                x = Math.min(x, verright);
            }else if(y>horbottom){
                x = Math.max(x,verleft);
                x = Math.min(x, verright);
            }else {
                x = Math.max(x,horleft);
                x = Math.min(x, horright);
            }

        }

        //make sure the ball remains inside the paths
        ShapeDrawable mTempBall = new ShapeDrawable(new OvalShape());
        mTempBall.setBounds(x-ballRadius, y-ballRadius, x+ballRadius , y+ballRadius);
        boolean isStillInPath = false;
        for (ShapeDrawable rc:rectList
        ) {
            if (contains(rc,mTempBall)){
                isStillInPath = true;
            }
        }
        if(isStillInPath){
            mBall.setBounds(mTempBall.getBounds());

        }

        boolean hitWrongTarget = false;
        int wrongTargetId = -1;
        for (int i=0; i<mWrongTargets.size(); i++) {
            if(intersects(mWrongTargets.get(i),mBall)){
                hitWrongTarget = true;
                wrongTargetId = i;
            }
        }

        if(intersects(mTargetGreen, mBall)){
            x = startx;
            y=starty;
            mBall.setBounds(startx-ballRadius, starty, startx, starty+(2*ballRadius));
            currLevel+=1;
            currPassedLevel+=1;
            if(currLevel<depth){
                drawSections(correctpath[currLevel]);
                currtext1 = "Current level: " ;
                currtext2 = (currPassedLevel+1) + "/"+ depth;
            }else{
                currtext1 = "You win!";
                currtext2 = (currPassedLevel+1) + "/"+ depth;
                long duration = System.currentTimeMillis()-  starttime ;
                Intent returnIntent = new Intent();
                returnIntent.putExtra("PoPPuzzle",true);
                returnIntent.putExtra("PoPPuzzleTime",duration);
                Activity myact = (Activity) ctx;
                myact.setResult(Activity.RESULT_OK,returnIntent);
                myact.finish();
            }
            drawGuide();
            //   drawBottomNodes();
        }else if (hitWrongTarget){
            mTargetGreen =  new ShapeDrawable(new RectShape());
            x=startx;
            y=starty;
            mBall.setBounds(startx-ballRadius, starty, startx, starty+(2*ballRadius));
            wrongpath[currLevel] = wrongTargetId;
            currLevel+=1;
            if(currLevel<depth){
                drawSections(10);
                currtext1 = "Wrong path" ;
                currtext2 = (currLevel+1) + "/"+ depth ;
            }else{
                currtext1 = "You Failed!" ;
                currtext2 = "You Failed!";
                //currtext2 = (currLevel+1) + "/"+ depth + "(" + currPassedLevel+")";

                long duration = System.currentTimeMillis()-  starttime ;
                Intent returnIntent = new Intent();
                returnIntent.putExtra("PoPPuzzle",true);
                returnIntent.putExtra("PoPPuzzleTime",duration);

                Activity myact = (Activity) ctx;
                myact.setResult(Activity.RESULT_CANCELED,returnIntent);
                myact.finish();
            }
            drawGuide();
            //  drawBottomNodes();
        }else if (intersects(mTargetUp, mBall)&& currLevel>currPassedLevel){

            mTargetGreen =  new ShapeDrawable(new RectShape());
            currLevel=currLevel-1;
            ShapeDrawable oldTarget = mWrongTargets.get(wrongpath[currLevel]);
            x=oldTarget.getBounds().left + ((oldTarget.getBounds().right - oldTarget.getBounds().left)/2);
            y=oldTarget.getBounds().top-100;
            mBall.setBounds(x-ballRadius, y, x, y+(2*ballRadius));
            if(currPassedLevel<currLevel){
                drawSections(10);
                currtext1 = "Wrong path" ;
                currtext2 = "Return UP!";
                currtext2 = (currLevel+1) + "/"+ depth + "(" + currPassedLevel+")";
            }else{
                drawSections(correctpath[currLevel]);
                currtext1 = "Current level: " ;
                currtext2 = (currPassedLevel+1) + "/"+ depth;
                //currtext2 = (currLevel+1) + "/"+ depth + "(" + currPassedLevel+")";
            }
            drawGuide();
            // drawBottomNodes();
        }
    }

    @Override
    protected void onFailure(@NonNull Exception e) {
        Log.e(TAG, "Face detection failed " + e);
    }
}