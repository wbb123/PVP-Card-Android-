package mangoabliu.finalproject;

import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.SeekBar;

import mangoabliu.finalproject.Model.GameModel;

/**
 * Created by Lyris on 30/11/16.
 */

public class SettingDialog extends Dialog {
    GameModel gameModel;

    // setting
    private ImageButton btnMusicCtrl = null;
    private SeekBar seekbar;
    private CheckBox cbMusic,cbSound;
    private int maxVolume;
    private int currentVolume;
    private int muteFlag,cv;
    private AudioManager audioManager;
    private Context con;
    private MediaPlayer mpBgm;


    protected SettingDialog (Context context, int style, MediaPlayer bgm) {
        super(context,style);
        con = context;
        mpBgm = bgm;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameModel= GameModel.getInstance();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.dialog_setting);

        SettingInit();

    }


    // SETTING INIT
    public void SettingInit(){

        btnMusicCtrl = (ImageButton) findViewById(R.id.btnMute);

        cbMusic =(CheckBox) findViewById(R.id.cb_musicswitch);
        cbSound =(CheckBox) findViewById(R.id.cb_soundswitch);

        audioManager = (AudioManager) con.getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        cv=currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        seekbar = (SeekBar) findViewById(R.id.seekBar);
        seekbar.setMax(maxVolume);
        seekbar.setProgress(currentVolume);

        //unmute、mute切换
        if(cv!=0) {btnMusicCtrl.setBackgroundResource(R.drawable.unmute);}
        else {btnMusicCtrl.setBackgroundResource(R.drawable.mute);}

        btnMusicCtrl.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                isMute();
            }
        });
        cbMusic.setOnClickListener(new cbMusicOnclickListener());
        cbSound.setOnClickListener(new cbSoundOnclickListener());

        //获取Music、Sound的初始状态
        if(gameModel.isMusicOn()==1) cbMusic.setChecked(true);
        else cbMusic.setChecked(false);

        if(gameModel.isSoundOn()==1) cbSound.setChecked(true);
        else cbSound.setChecked(false);


        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,i,0);
                currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                if(i>0) {
                    btnMusicCtrl.setBackgroundResource(R.drawable.unmute);
                    seekbar.setProgress(i);}
                else  btnMusicCtrl.setBackgroundResource(R.drawable.mute);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }


    public void isMute(){
        //按静音键,muteFlag=1说明静音状态
        if (currentVolume!=0 || muteFlag==0){
            muteFlag=1;
            audioManager = (AudioManager) con.getSystemService(Context.AUDIO_SERVICE);
            cv = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_MUTE,0);
            btnMusicCtrl.setBackgroundResource(R.drawable.mute);
            seekbar.setProgress(0);
        }else//按开启键
        {
            muteFlag=0;
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_UNMUTE,0);
            btnMusicCtrl.setBackgroundResource(R.drawable.unmute);
            seekbar.setProgress(cv);
        }
    }

    private class cbMusicOnclickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(cbMusic.isChecked()){
                gameModel.setMusicOn(1);
                mpBgm.start();
            }
            else{
                gameModel.setMusicOn(0);
                mpBgm.pause();
                }
        }
    }

    private class cbSoundOnclickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(cbSound.isChecked()){
                gameModel.setSoundOn(1);
            }
            else{
                gameModel.setSoundOn(0);
            }
        }
    }

}
