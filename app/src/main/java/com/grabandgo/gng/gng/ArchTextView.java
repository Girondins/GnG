package com.grabandgo.gng.gng;

/**
 * Created by karlcasserfelt on 16-05-12.
 */

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.View;

public class ArchTextView extends View {
    private static final String MY_TEXT = "Möllans Falafel";
    private Path mArc;

    private Paint mPaintText;

    public ArchTextView(Context context) {
        super(context);

        mArc = new Path();
        RectF oval = new RectF(150, 130, 535, 500);

        mArc.addArc(oval, -175, 260);
        mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintText.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaintText.setColor(Color.BLACK);
        mPaintText.setTextSize(75);

        Typeface plain = Typeface.createFromAsset(getContext().getAssets(), "fonts/splash-font.ttf");
        Typeface bold = Typeface.create(plain, Typeface.NORMAL);
        mPaintText.setTypeface(bold);


        this.setBackgroundColor(Color.BLUE);
        this.setAlpha(0.4f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawTextOnPath(MY_TEXT, mArc, 0, 20, mPaintText);
        invalidate();
    }
}