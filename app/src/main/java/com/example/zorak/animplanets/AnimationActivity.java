package com.example.zorak.animplanets;


import android.animation.ValueAnimator;
import android.app.Activity;

import android.content.pm.PackageManager;
import android.hardware.Camera;

import android.hardware.Camera.Parameters;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;

import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;


/**
 * Created by Zorak on 9/1/2018.
 */

public class AnimationActivity extends Activity {

private Camera camera;
private Parameters parameters;
private boolean hasFalsh = false;
private boolean isOn = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);


        ImageView earthImage =  (ImageView) findViewById(R.id.earth_image);
        ImageView marsImage =   (ImageView) findViewById(R.id.mars_image);
        ImageView saturnImage = (ImageView) findViewById(R.id.saturn_image);
        ImageView sunImage  =  (ImageView) findViewById(R.id.sun_image);


        ValueAnimator earthAnimator = animatePlanet(earthImage, TimeUnit.SECONDS.toMillis(2));
        ValueAnimator marsAnimator = animatePlanet(marsImage, TimeUnit.SECONDS.toMillis(6));
        ValueAnimator saturnAnimator = animatePlanet(saturnImage, TimeUnit.SECONDS.toMillis(12));


        earthAnimator.start();
        marsAnimator.start();
        saturnAnimator.start();

        hasFalsh = getApplication().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if(!hasFalsh){
            Toast.makeText(AnimationActivity.this, "Sorry, you device does not have any camera", Toast.LENGTH_LONG).show();
            return;
        }
        else{
            this.camera = Camera.open(0);
            parameters = this.camera.getParameters();
        }
        View.OnClickListener sunListener = new View.OnClickListener() {



            @Override
            public void onClick(View v) {
                if(!isOn) {

                        Toast.makeText(AnimationActivity.this, "Sun On!", Toast.LENGTH_SHORT).show();
                    turnOnTheFlash();
                }else {


                        Toast.makeText(AnimationActivity.this, "Sun Off!", Toast.LENGTH_SHORT).show();
                    turnOffTheFlash();
               }
                }
        };

        View.OnClickListener earthListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AnimationActivity.this,"Earth!",Toast.LENGTH_SHORT).show();
            }
        };

        View.OnClickListener marsListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AnimationActivity.this,"Mars!",Toast.LENGTH_SHORT).show();
           ;
            }
        };
        View.OnClickListener saturnListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AnimationActivity.this,"Saturn!",Toast.LENGTH_SHORT).show();
            }
        };
        sunImage.setOnClickListener(sunListener);
        earthImage.setOnClickListener(earthListener);
        marsImage.setOnClickListener(marsListener);
        saturnImage.setOnClickListener(saturnListener);
    }

    private ValueAnimator animatePlanet(final ImageView planet, long orbitDuration) {
        ValueAnimator anim = ValueAnimator.ofInt(0, 359);
        anim.addUpdateListener(
                new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        int val = (Integer) valueAnimator.getAnimatedValue();
                        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) planet.getLayoutParams();
                        layoutParams.circleAngle = val;
                        planet.setLayoutParams(layoutParams);
                    }
                });
        anim.setDuration(orbitDuration);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatMode(ValueAnimator.RESTART);
        anim.setRepeatCount(ValueAnimator.INFINITE);

        return anim;
    }

    private void turnOffTheFlash() {
        parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
        this.camera.setParameters(parameters);
        this.camera.stopPreview();
        isOn = false;

    }

    private void turnOnTheFlash() {
        if(this.camera != null){
            parameters = this.camera.getParameters();
            parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
            this.camera.setParameters(parameters);
            this.camera.startPreview();
            isOn = true;

        }
    }

    private void getCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                parameters = camera.getParameters();
            } catch (RuntimeException e) {
                System.out.println("Error: Failed to Open: " + e.getMessage());
            }
        }
    }


}
