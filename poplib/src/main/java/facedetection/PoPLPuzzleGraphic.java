/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package facedetection;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.util.DisplayMetrics;

import java.util.ArrayList;

/** Graphic instance for rendering inference info (latency, FPS, resolution) in an overlay view. */
public class PoPLPuzzleGraphic extends GraphicOverlay.Graphic {

  private static final int TEXT_COLOR = Color.WHITE;
  private static final float TEXT_SIZE = 60.0f;

  ArrayList<ShapeDrawable> sectionList ;
  ArrayList<ShapeDrawable> rectList;
  Path fullPath ;
  ShapeDrawable mBall ;
  ShapeDrawable mTargetGreen ;
  ShapeDrawable mTargetUp ;
  ArrayList<ShapeDrawable> mWrongTargets ;
  ArrayList<ShapeDrawable> nodeList ;
  Paint mPaintPath ;
  private  DisplayMetrics displayMetrics;
  private String currtext1;
  private String currtext2;
  private String popText;

  public PoPLPuzzleGraphic(FaceDetectorProcessor _processor,
      GraphicOverlay overlay,ArrayList<ShapeDrawable> _rectList , ArrayList<ShapeDrawable> _sectionList,ShapeDrawable _mBall,ShapeDrawable _mTargetGreen ,
                           ShapeDrawable _mTargetUp ,ArrayList<ShapeDrawable> _mWrongTargets,ArrayList<ShapeDrawable> _nodeList ,
                           Paint _mPaintPath ,String _currtext1 ,String _currtext2 ,Path _fullPath , String _popText) {
    super(overlay);
    this.fullPath = _fullPath;
    this.popText=_popText;
    this.displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
    this.currtext1 = _currtext1;
    this.currtext2 = _currtext2;
    this.rectList = _rectList;
    this.sectionList = _sectionList;
    this.mBall = _mBall;
    this.mTargetGreen = _mTargetGreen;
    this.mTargetUp = _mTargetUp;
    this.mWrongTargets = _mWrongTargets;
    this.nodeList = _nodeList;
    this.mPaintPath = _mPaintPath;
    postInvalidate();
  }


  @Override
  public synchronized void draw(Canvas canvas) {

    for (ShapeDrawable rc:rectList
    ) {
      rc.draw(canvas);
    }
    for (ShapeDrawable sd:sectionList
    ) {
      sd.draw(canvas);
    }
    for (ShapeDrawable nd:nodeList
    ) {
      nd.draw(canvas);
    }
    mTargetUp.setAlpha(80);
    mBall.draw(canvas);
    mTargetUp.draw(canvas);

    canvas.drawPath(fullPath, mPaintPath);

    Paint paint2=new Paint();
    paint2.setTextSize(60);
    Paint paint3=new Paint();
    paint3.setTextSize(50);
    paint3.setColor(Color.WHITE);

    int screenWidth = displayMetrics.widthPixels;
    int screenHeight = displayMetrics.heightPixels;

    canvas.drawText(currtext1, 140- paint2.getTextSize(), 100, paint2);
    canvas.drawText(currtext2, (screenWidth/2) + 140- paint2.getTextSize(), 100, paint2);
    canvas.drawText(popText, (screenWidth/4), screenHeight/8 +10, paint3);

  }
}
