package mangoabliu.finalproject;

import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import mangoabliu.finalproject.Layout.FontTextView;
import mangoabliu.finalproject.Model.GameModel;

/**
 * Created by herenjie on 2016/11/13.
 */

public class RegistrationActivity extends AppCompatActivity {
    GameModel gameModel;
    Button btn_cancel,btn_register;
    EditText et_UserName,et_Password,et_Password_repeat;

    //REVISED BY LYRIS   11/26+++
    FontTextView tv_UserName,tv_Password,tv_Password_repeat;
    CheckBox cb_ShowPW;

    private static MediaPlayer bgm;
    private SoundPool soundPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //register to Model
        gameModel= GameModel.getInstance();
        gameModel.addActivity(this);
        gameModel.setRegistrationActivity(this);

        // FULLSCREEN  /LYRIS 11.26
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_registration);

        btn_cancel = (Button)findViewById(R.id.cancelButton);
        btn_register = (Button) findViewById(R.id.registerButton);

        btn_cancel.setOnClickListener(new bt_CancelListener());
        btn_register.setOnClickListener(new bt_RegisterListener());

        et_UserName = (EditText) findViewById(R.id.usernameRegisterText);
        et_Password = (EditText) findViewById(R.id.passwordRegisterText);
        et_Password_repeat = (EditText) findViewById(R.id.passwordConfirmText);

        //ADD TV CB IME BY LYRIS   11/26+++
        tv_UserName =(FontTextView) findViewById(R.id.usernameRegister);
        tv_Password =(FontTextView)findViewById(R.id.passwordRegister);
        tv_Password_repeat =(FontTextView) findViewById(R.id.passwordRegisterConfirm);
        Typeface typeFace = Typeface.createFromAsset(getAssets(),"fonts/Marvel-Bold.ttf");
        tv_UserName.setTypeface(typeFace);
        tv_Password.setTypeface(typeFace);
        tv_Password_repeat.setTypeface(typeFace);
        //IME
        et_UserName.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        et_Password.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PASSWORD);
        et_Password_repeat.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PASSWORD);

        cb_ShowPW=(CheckBox) findViewById(R.id.cb_showconfirmpassword);
        cb_ShowPW.setOnClickListener(new cb_OnclickListener());
        cb_ShowPW.setTypeface(typeFace);

        BGMInit();

    }

    public void successful(String msg){
        gameModel.showToast(RegistrationActivity.this, msg);
        super.onBackPressed();
    }

    public void errorMessage(String err){
        gameModel.showToast(RegistrationActivity.this, err);
    }

    private class bt_RegisterListener implements View.OnClickListener {

        public void onClick(View v) {
            if(gameModel.isSoundOn()==1){
                soundPool.play(1,1,1,1,0,3);
            }
            String str_UserName=et_UserName.getText().toString();
            String str_Password=et_Password.getText().toString();
            String str_Pwd_confirm = et_Password_repeat.getText().toString();
            Log.i("RegistrationActivity","bt_RegisterListener onClick!");
            if(str_UserName.equals(""))
                gameModel.showToast(RegistrationActivity.this, "Please Input the Username");
            else if(str_Password.equals(""))
                gameModel.showToast(RegistrationActivity.this, "Please Input the Password");
            else if(!str_Pwd_confirm.equals(str_Password)){
                gameModel.showToast(RegistrationActivity.this, "Password is not same");

            }else{
                gameModel.registration(str_UserName, str_Password);
            }

        }
    }

    private class bt_CancelListener implements View.OnClickListener {
        public void onClick(View view) {
            if(gameModel.isSoundOn()==1){
                soundPool.play(1,1,1,1,0,3);
            }
            RegistrationActivity.super.onBackPressed();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    //show password /Lyris 11-26
    private class cb_OnclickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(gameModel.isSoundOn()==1){
                soundPool.play(1,1,1,1,0,3);
            }
            if(RegistrationActivity.this.cb_ShowPW.isChecked()){

                RegistrationActivity.this.et_Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                RegistrationActivity.this.et_Password_repeat.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                RegistrationActivity.this.et_Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                RegistrationActivity.this.et_Password_repeat.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
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

    //跳转、中断暂停播放，回activity继续播放
    @Override
    protected void onStart() {
        super.onStart();
        if(gameModel.isMusicOn()==1)  bgm.start();
    }


    @Override
    protected void onStop() {
        super.onStop();
        if(bgm!= null)
            bgm.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bgm!= null)
            bgm.release();
    }

}
