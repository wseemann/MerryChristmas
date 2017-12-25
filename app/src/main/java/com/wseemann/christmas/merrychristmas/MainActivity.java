package com.wseemann.christmas.merrychristmas;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
 import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String NAME = "";
    private static final String PHONE_NUMBER = "";

    private MediaPlayer mMediaPlayer;
    private FrameLayout mFrameLayout;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mFrameLayout.removeAllViews();

            View view = setUpSecondView();

            mFrameLayout.addView(view);

            Animation animation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_in);
            animation.reset();
            animation.setDuration(6000);
            view.clearAnimation();
            view.startAnimation(animation);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFrameLayout = findViewById(R.id.my_content);

        mMediaPlayer = MediaPlayer.create(this, R.raw.music);
        mMediaPlayer.start();

        View view = setUpFirstView();
        mFrameLayout.addView(view);
        runFadeInAnimation(view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
    }

    private String getMessage() {
        String model = Build.MODEL;
        String manufacturer = Build.MANUFACTURER;
        String version = Build.VERSION.RELEASE;

        StringBuffer buffer = new StringBuffer();
        buffer.append(NAME);
        buffer.append(", enjoy your new ");
        buffer.append(manufacturer);
        buffer.append(" ");
        buffer.append(model);
        buffer.append(" running Android ");
        buffer.append(version);
        buffer.append("!\n");
        buffer.append("Merry Christmas!!");

        return buffer.toString();
    }

    private void runFadeInAnimation(final View view) {
        Animation animation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        animation.reset();
        animation.setDuration(6000);
        view.clearAnimation();

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                runFadeOutAnimation(view);
            }
        });

        view.startAnimation(animation);
    }

    private void runFadeOutAnimation(View view) {
        Animation animation = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        animation.reset();
        animation.setDuration(2000);
        view.clearAnimation();

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mHandler.sendEmptyMessage(0);
            }
        });

        view.startAnimation(animation);
    }

    private View setUpFirstView() {
        View view = getLayoutInflater().inflate(R.layout.initial_layout, null);

        TextView messageTextView = view.findViewById(R.id.text);
        messageTextView.setText(getMessage());

        return view;
    }

    private View setUpSecondView() {
        View view = getLayoutInflater().inflate(R.layout.second_layout, null);

        Button thanksButton = view.findViewById(R.id.thanks_button);
        thanksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSms();
            }
        });

        return view;
    }

    private void sendSms() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + PHONE_NUMBER));
        intent.putExtra("sms_body", getString(R.string.thank_you_message));
        startActivity(intent);
    }
}
