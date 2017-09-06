package mangoabliu.finalproject;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import mangoabliu.finalproject.Layout.FontTextView;
import mangoabliu.finalproject.Model.GameModel;

/**
 * Created by LyrisXu on 2016-11-26.
 */
/**
 * Add SoundEffect & Media by LyrisXu on 2016-12-01.
 */

public class MenuActivity extends AppCompatActivity implements AnimationListener {


    GameModel gameModel;

    FontTextView tv_Menu;
    ImageButton btn_Setting;
    RelativeLayout rl_menu;

    Animation anim = new AlphaAnimation(0.0f, 1.0f);
//    Point size;
//    Animation animationFadeIn, animationFadeOut;

    private static MediaPlayer bgm;
    private SoundPool soundPool;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameModel= GameModel.getInstance();
        gameModel.addActivity(this);
        gameModel.setMenuActivity(this);

        UIInit();
        AnimInit();
        BGMInit();

    }


    //跳转、中断暂停播放，回activity继续播放
    @Override
    protected void onStart() {
        super.onStart();
        if(gameModel.isMusicOn()==1)  bgm.start();
    }


    @Override
    protected void onStop() {
        super.onStop();
        if(bgm!= null){
            bgm.pause();
            bgm.seekTo(0);}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bgm!= null)
            bgm.release();
    }

    public void UIInit(){

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_menu);
        rl_menu =(RelativeLayout)findViewById(R.id.activity_menu);
        rl_menu.setOnClickListener(new rl_MenuListener());
        btn_Setting = (ImageButton) findViewById(R.id.btn_menu_setting);
        btn_Setting.setOnClickListener(new bt_SettingListener());
        tv_Menu = (FontTextView) findViewById(R.id.tv_menu);
        Typeface typeFace = Typeface.createFromAsset(getAssets(),"fonts/Marvel-Bold.ttf");
        tv_Menu.setTypeface(typeFace);


    }

    public void AnimInit(){
        // translation animate -deleted
////        tv_Menu.setOnClickListener(new tv_MenuListener());
//        //get screen size
//        size=new Point();
//        Display display=getWindowManager().getDefaultDisplay();
//        display.getSize(size);
//        tv_Menu.animate().translationX(size.y/2).withStartAction(new Runnable(){
//            @Override
//            public void run() {
//                tv_Menu.animate().translationY(size.y/4).alpha(0.5f);
//            }
//        });

        //slide animate -deleted
//        tv_Menu.startAnimation(AnimationUtils.loadAnimation(MenuActivity.this,android.R.anim.slide_in_left));

        //fade_In
//        animationFadeIn= AnimationUtils.loadAnimation(MenuActivity.this,R.anim.fade_in);
//        animationFadeIn.setRepeatCount(10);
//        animationFadeOut=AnimationUtils.loadAnimation(MenuActivity.this,R.anim.fade_out);
//        tv_Menu.setVisibility(View.VISIBLE);
//        tv_Menu.setAnimation(animationFadeIn);

        //blink
        anim.setDuration(1500); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        tv_Menu.startAnimation(anim);

    }

    public void BGMInit(){
        //循环播放
        if(gameModel.isMusicOn()==1){
            bgm = MediaPlayer.create(this,R.raw.index_bg);
            bgm.start();
            bgm.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
                @Override
                public void onCompletion(MediaPlayer mp) {
                    bgm.start();
                }
            });
        }
        if(gameModel.isSoundOn()==1){
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC,0);
            soundPool.load(this, R.raw.button, 1);  //button
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }


    private class rl_MenuListener implements View.OnClickListener {

        public void onClick(View view) {

            Intent myIntent = new Intent(MenuActivity.this, LoginActivity.class);
            startActivityForResult(myIntent, 0);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    private class bt_SettingListener implements View.OnClickListener {

        public void onClick(View view) {
            if(gameModel.isSoundOn()==1){
                soundPool.play(1,1,1,0,1,3);
            }

            SettingDialog set = new SettingDialog(MenuActivity.this,R.style.DialogTranslucent,bgm);
            set.show();
        }
    }

}
