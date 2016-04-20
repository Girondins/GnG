package com.grabandgo.gng.gng;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.jorgecastillo.FillableLoader;
import com.github.jorgecastillo.FillableLoaderBuilder;
import com.github.jorgecastillo.State;
import com.github.jorgecastillo.clippingtransforms.PlainClippingTransform;
import com.github.jorgecastillo.listener.OnStateChangeListener;
import com.nineoldandroids.animation.Animator;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;
import com.viksaa.sssplash.lib.utils.UIUtil;
import com.viksaa.sssplash.lib.utils.ValidationUtil;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

public class StartUpActivity extends AppCompatActivity {

    private RelativeLayout mRlReveal;
    private ImageView mImgLogo;
    private AppCompatTextView mTxtTitle;
    private FillableLoader mPathLogo;
    private FrameLayout mFl;
    private ConfigSplash mConfigSplash;
    private boolean hasAnimationStarted = false;
    private int pathOrLogo = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mConfigSplash = new ConfigSplash();
        initSplash(mConfigSplash);

        pathOrLogo = ValidationUtil.hasPath(mConfigSplash);
        initUI(pathOrLogo);

    }

    public void initUI(int flag) {
        setContentView(com.viksaa.sssplash.lib.R.layout.activity_main_lib);

        mRlReveal = (RelativeLayout) findViewById(com.viksaa.sssplash.lib.R.id.rlColor);
        mTxtTitle = (AppCompatTextView) findViewById(com.viksaa.sssplash.lib.R.id.txtTitle);

        switch (flag) {
            case Flags.WITH_PATH:
                mFl = (FrameLayout) findViewById(com.viksaa.sssplash.lib.R.id.flCentral);
                initPathAnimation();
                break;
            case Flags.WITH_LOGO:
                mImgLogo = (ImageView) findViewById(com.viksaa.sssplash.lib.R.id.imgLogo);
                mImgLogo.setImageResource(mConfigSplash.getLogoSplash());
                break;
            default:
                break;
        }
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !hasAnimationStarted) {
            startCircularReveal();
        }
    }

    public void initPathAnimation() {
        int viewSize = getResources().getDimensionPixelSize(com.viksaa.sssplash.lib.R.dimen.fourthSampleViewSize);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(viewSize, viewSize);

        params.setMargins(0, 0, 0, 50);
        FillableLoaderBuilder loaderBuilder = new FillableLoaderBuilder();
        mPathLogo = loaderBuilder
                .parentView(mFl)
                .layoutParams(params)
                .svgPath(mConfigSplash.getPathSplash())
                .originalDimensions(mConfigSplash.getOriginalWidth(), mConfigSplash.getOriginalHeight())
                .strokeWidth(mConfigSplash.getPathSplashStrokeSize())
                .strokeColor(Color.parseColor(String.format("#%06X", (0xFFFFFF & getResources().getColor(mConfigSplash.getPathSplashStrokeColor())))))
                .fillColor(Color.parseColor(String.format("#%06X", (0xFFFFFF & getResources().getColor(mConfigSplash.getPathSplashFillColor())))))
                .strokeDrawingDuration(mConfigSplash.getAnimPathStrokeDrawingDuration())
                .fillDuration(mConfigSplash.getAnimPathFillingDuration())
                .clippingTransform(new PlainClippingTransform())
                .build();
        mPathLogo.setOnStateChangeListener(new OnStateChangeListener() {
            public void onStateChange(int i) {
                if (i == State.FINISHED) {
                    startTextAnimation();
                }
            }
        });
    }

    public void startCircularReveal() {
        // get the final radius for the clipping circle
        int finalRadius = Math.max(mRlReveal.getWidth(), mRlReveal.getHeight()) + mRlReveal.getHeight() / 2;
        //bottom or top
        int y = UIUtil.getRevealDirection(mRlReveal, mConfigSplash.getRevealFlagY());
        //left or right
        int x = UIUtil.getRevealDirection(mRlReveal, mConfigSplash.getRevealFlagX());

        mRlReveal.setBackgroundColor(getResources().getColor(mConfigSplash.getBackgroundColor()));
        SupportAnimator animator = ViewAnimationUtils.createCircularReveal(mRlReveal, x, y, 0, finalRadius);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(mConfigSplash.getAnimCircularRevealDuration());
        animator.addListener(new SupportAnimator.AnimatorListener() {

            public void onAnimationStart() {
            }

            public void onAnimationCancel() {
            }

            public void onAnimationRepeat() {
            }

            public void onAnimationEnd() {

                if (pathOrLogo == Flags.WITH_PATH) {
                    mPathLogo.start();
                } else {
                    startLogoAnimation();
                }
            }
        });
        animator.start();
        hasAnimationStarted = true;
    }

    public void startLogoAnimation() {
        mImgLogo.setVisibility(View.VISIBLE);
        mImgLogo.setImageResource(mConfigSplash.getLogoSplash());

        YoYo.with(mConfigSplash.getAnimLogoSplashTechnique()).withListener(new Animator.AnimatorListener() {

            public void onAnimationStart(Animator animation) {}

            public void onAnimationEnd(Animator animation) {
                startTextAnimation();
            }

            public void onAnimationCancel(Animator animation) {}

            public void onAnimationRepeat(Animator animation) {}

        }).duration(mConfigSplash.getAnimLogoSplashDuration()).playOn(mImgLogo);
    }

    public void startTextAnimation() {

        mTxtTitle.setText(mConfigSplash.getTitleSplash());
        mTxtTitle.setTextSize(mConfigSplash.getTitleTextSize());
        mTxtTitle.setTextColor(getResources().getColor(mConfigSplash.getTitleTextColor()));
        if (!mConfigSplash.getTitleFont().isEmpty())
            setFont(mConfigSplash.getTitleFont());

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, com.viksaa.sssplash.lib.R.id.flCentral);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mTxtTitle.setLayoutParams(params);
        mTxtTitle.setVisibility(View.VISIBLE);

        YoYo.with(mConfigSplash.getAnimTitleTechnique()).withListener(new Animator.AnimatorListener() {

            public void onAnimationStart(Animator animation) {}

            public void onAnimationEnd(Animator animation) {
                animationsFinished();
            }

            public void onAnimationCancel(Animator animation) {}

            public void onAnimationRepeat(Animator animation) {}

        }).duration(mConfigSplash.getAnimTitleDuration()).playOn(mTxtTitle);
    }

    public void setFont(String font) {
        Typeface type = Typeface.createFromAsset(getAssets(), font);
        mTxtTitle.setTypeface(type);
    }

    public void initSplash(ConfigSplash configSplash){
        /* you don't have to override every property */

        //Customize Circular Reveal
        configSplash.setBackgroundColor(R.color.grabNgo); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(2000); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

        //Customize Logo
        configSplash.setLogoSplash(R.drawable.start_icon_temp); //or any other drawable
        configSplash.setAnimLogoSplashDuration(2000); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.Bounce); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)

        //Customize Path
        //configSplash.setPathSplash(); //set path String
        configSplash.setOriginalHeight(400); //in relation to your svg (path) resource
        configSplash.setOriginalWidth(400); //in relation to your svg (path) resource
        configSplash.setAnimPathStrokeDrawingDuration(3000);
        configSplash.setPathSplashStrokeSize(3); //I advise value be <5
        configSplash.setPathSplashStrokeColor(R.color.white); //any color you want form colors.xml
        configSplash.setAnimPathFillingDuration(3000);
        configSplash.setPathSplashFillColor(R.color.white); //path object filling color

        //Customize Title
        configSplash.setTitleSplash("Money, bitches!");
        configSplash.setTitleTextColor(R.color.white);
        configSplash.setTitleTextSize(70f); //float value
        configSplash.setAnimTitleDuration(3000);
        configSplash.setAnimTitleTechnique(Techniques.FlipInX);
        configSplash.setTitleFont("fonts/splash-font.ttf"); //provide string to your font located in assets/fonts/
    }

    public void animationsFinished(){
        MainActivity mainActivity = new MainActivity();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}