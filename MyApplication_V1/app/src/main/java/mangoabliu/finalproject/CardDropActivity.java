package mangoabliu.finalproject;

import android.content.Context;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nineoldandroids.view.ViewHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.Random;

import mangoabliu.finalproject.Animation.OnSwipeTouchListener;
import mangoabliu.finalproject.Animation.RotateObject;
import mangoabliu.finalproject.Animation.ZoomObject;
import mangoabliu.finalproject.Model.GameModel;
import mangoabliu.finalproject.Model.UserAccount;

import static mangoabliu.finalproject.Animation.DisplayImageOptionsUtil.getDisplayImageOptions;


/**
 * Created by Byanka on 26/11/2016.
 */

public class CardDropActivity extends AppCompatActivity  {

    GameModel gameModel;
    UserAccount myUser;
    private RelativeLayout CardRootM;
    private ImageView CardBackM;
    private ImageView CardFrontM;
    private RelativeLayout CardRootR;
    private ImageView CardBackR;
    private ImageView CardFrontR;
    private RelativeLayout CardRootL;
    private ImageView CardBackL;
    private ImageView CardFrontL;
    private Button ReturnMain;
    private String errMessage;
    private ImageView expandedCard;
    private static MediaPlayer bgm;
    private SoundPool soundPool;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameModel=GameModel.getInstance();
        gameModel.addActivity(this);
        gameModel.setCardDropActivity(this);
        // FULLSCREEN  /LYRIS 11.27
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_carddrop);
        initView();
        int DropCardId = GeneDropCardID();
        initData(DropCardId);

        BGMInit();


    }

    private void initView() {

        CardRootM = (RelativeLayout) findViewById(R.id.CardRootM);
        CardBackM = (ImageView) findViewById(R.id.CardBackM);
        CardFrontM = (ImageView) findViewById(R.id.CardFrontM);
        CardRootR = (RelativeLayout) findViewById(R.id.CardRootR);
        CardBackR = (ImageView) findViewById(R.id.CardBackR);
        CardFrontR = (ImageView) findViewById(R.id.CardFrontR);
        CardRootL = (RelativeLayout) findViewById(R.id.CardRootL);
        CardBackL = (ImageView) findViewById(R.id.CardBackL);
        CardFrontL = (ImageView) findViewById(R.id.CardFrontL);
        ReturnMain = (Button) findViewById(R.id.btn_returnMain);

        expandedCard = (ImageView) findViewById(R.id.expanded_image);

        CardBackM.setOnClickListener(new CardBackM_Listener());
        CardBackR.setOnClickListener(new CardBackR_Listener());
        CardBackL.setOnClickListener(new CardBackL_Listener());
        ReturnMain.setOnClickListener(new ReturnMain_Listener());

        CardFrontL.setOnClickListener(new CardFrontL_Listener());
        CardFrontM.setOnClickListener(new CardFrontM_Listener());
        CardFrontR.setOnClickListener(new CardFrontR_Listener());


        expandedCard.setOnTouchListener(new OnSwipeTouchListener(CardDropActivity.this) {

            public void onSwipeRight(float dis, float velocity) {
                //Toast.makeText(getContext(), "right", Toast.LENGTH_SHORT).show();
                rotateCard(expandedCard,"right",dis,velocity,3000);

            }
            public void onSwipeLeft(float dis, float velocity) {
                //Toast.makeText(getContext(), "left", Toast.LENGTH_SHORT).show();
                rotateCard(expandedCard,"left",dis,velocity,3000);
            }
        });


        CardFrontL.setOnTouchListener(new OnSwipeTouchListener(CardDropActivity.this) {

            public void onSwipeRight(float dis, float velocity) {
                //Toast.makeText(getContext(), "right", Toast.LENGTH_SHORT).show();
                rotateCard(CardFrontL,"right",dis,velocity,2000);

            }
            public void onSwipeLeft(float dis, float velocity) {
                //Toast.makeText(getContext(), "left", Toast.LENGTH_SHORT).show();
                rotateCard(CardFrontL,"left",dis,velocity,2000);
            }
        });

        CardFrontM.setOnTouchListener(new OnSwipeTouchListener(CardDropActivity.this) {

            public void onSwipeRight(float dis, float velocity) {
                //Toast.makeText(getContext(), "right", Toast.LENGTH_SHORT).show();
                rotateCard(CardFrontM,"right",dis,velocity,2000);

            }
            public void onSwipeLeft(float dis, float velocity) {
                //Toast.makeText(getContext(), "left", Toast.LENGTH_SHORT).show();
                rotateCard(CardFrontM,"left",dis,velocity,2000);
            }
        });

        CardFrontR.setOnTouchListener(new OnSwipeTouchListener(CardDropActivity.this) {

            public void onSwipeRight(float dis, float velocity) {
                //Toast.makeText(getContext(), "right", Toast.LENGTH_SHORT).show();
                rotateCard(CardFrontR,"right",dis,velocity,2000);

            }
            public void onSwipeLeft(float dis, float velocity) {
                //Toast.makeText(getContext(), "left", Toast.LENGTH_SHORT).show();
                rotateCard(CardFrontR,"left",dis,velocity,2000);
            }
        });

    }

    private void rotateCard(ImageView view, String leftOrRight, float dis, float velocity, int duration){
        int percentage = 0;
        if (leftOrRight.equals("left"))
            dis = Math.abs(dis);

        if (dis != 0)
            percentage = Math.round(view.getWidth()/dis);
        if (percentage >= 7)
            percentage = 2;
        else if (percentage >= 5 && percentage < 7)
            percentage = 3;
        else
            percentage = 4;

        view.animate().setDuration(duration);
        if (leftOrRight.equals("left"))
            view.animate().rotationYBy(360*-percentage);
        else
            view.animate().rotationYBy(360*percentage);
    }

    private class ReturnMain_Listener implements View.OnClickListener {

        public void onClick(View v) {
            if(gameModel.isSoundOn()==1) soundPool.play(1,1,1,0,0,1);
            finish();
        }
    }


    private class CardBackL_Listener implements View.OnClickListener {

        public void onClick(View v) {
            if(gameModel.isSoundOn()==1) soundPool.play(2,1,1,0,0,0.5f);

            ViewHelper.setRotationY(CardFrontL, 180f);//先翻转180，转回来时就不是反转的了
            RotateObject rotatable = new RotateObject.Builder(CardRootL)
                    .sides(R.id.CardBackL, R.id.CardFrontL)
                    .direction(RotateObject.ROTATE_Y)
                    .rotationCount(1)
                    .build();
            rotatable.setTouchEnable(false);
            rotatable.rotate(RotateObject.ROTATE_Y, -180, 1500);
            CardBackM.setEnabled(false);
            CardBackR.setEnabled(false);
            if(errMessage != null) {
                Toast toast = Toast.makeText(CardDropActivity.this,errMessage, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
            else if(gameModel.isSoundOn()==1) soundPool.play(3,1,1,0,0,1);


        }
    }


    private class CardBackR_Listener implements View.OnClickListener {

        public void onClick(View v) {
            if(gameModel.isSoundOn()==1) soundPool.play(2,1,1,0,0,0.5f);

            ViewHelper.setRotationY(CardFrontR, 180f);//先翻转180，转回来时就不是反转的了
            RotateObject rotatable = new RotateObject.Builder(CardRootR)
                    .sides(R.id.CardBackR, R.id.CardFrontR)
                    .direction(RotateObject.ROTATE_Y)
                    .rotationCount(1)
                    .build();
            rotatable.setTouchEnable(false);
            rotatable.rotate(RotateObject.ROTATE_Y, -180, 1500);
            CardBackM.setEnabled(false);
            CardBackL.setEnabled(false);
            if(errMessage != null) {
                Toast toast = Toast.makeText(CardDropActivity.this,errMessage, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
            else if(gameModel.isSoundOn()==1) soundPool.play(3,1,1,0,0,1);


        }
    }

    private class CardBackM_Listener implements View.OnClickListener {

        public void onClick(View v) {
            if(gameModel.isSoundOn()==1) soundPool.play(2,1,1,0,0,0.5f);

            ViewHelper.setRotationY(CardFrontM, 180f);//先翻转180，转回来时就不是反转的了
            RotateObject rotatable = new RotateObject.Builder(CardRootM)
                    .sides(R.id.CardBackM, R.id.CardFrontM)
                    .direction(RotateObject.ROTATE_Y)
                    .rotationCount(1)
                    .build();
            rotatable.setTouchEnable(false);
            rotatable.rotate(RotateObject.ROTATE_Y, -180, 1500);
            CardBackL.setEnabled(false);
            CardBackR.setEnabled(false);
            if(errMessage != null) {
                Toast toast = Toast.makeText(CardDropActivity.this,errMessage, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
            else if(gameModel.isSoundOn()==1) soundPool.play(3,1,1,0,0,1);

        }
    }


    private int GeneDropCardID(){
        Random random=new Random();
        int DropOneCard =random.nextInt(18);
        int CardID = DropOneCard+1;
        gameModel.updateUserCardRelation(gameModel.getUserAccount().getUserId(),CardID);
        gameModel.getUserCard(gameModel.getUserAccount().getUserId());
        Resources res = getResources();
        String[] CardsName = res.getStringArray(R.array.cards_name);
        Context context = CardBackM.getContext();
        int DropCardId = context.getResources().getIdentifier(CardsName[DropOneCard], "drawable", context.getPackageName());
        return DropCardId;

    }

    public void showMessage(){
        errMessage = "YOU ALREADY HAVE THIS CARD";

    }

    /**
     * 设置数据
     */
    public void initData(int DropCardId) {

        ImageLoader imageLoader;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        String imageUri = "drawable://" + R.drawable.card_back;
        ImageLoader.getInstance().displayImage(imageUri, CardBackM, getDisplayImageOptions());
        ImageLoader.getInstance().displayImage(imageUri, CardBackR, getDisplayImageOptions());
        ImageLoader.getInstance().displayImage(imageUri, CardBackL, getDisplayImageOptions());


        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        String imageUriFront = "drawable://" + DropCardId ;
        ImageLoader.getInstance().displayImage(imageUriFront, CardFrontL, getDisplayImageOptions());
        ImageLoader.getInstance().displayImage(imageUriFront, CardFrontM, getDisplayImageOptions());
        ImageLoader.getInstance().displayImage(imageUriFront, CardFrontR, getDisplayImageOptions());

        ImageLoader.getInstance().displayImage(imageUriFront, expandedCard, getDisplayImageOptions());

        CardFrontL.setVisibility(View.INVISIBLE);
        CardFrontR.setVisibility(View.INVISIBLE);
        CardFrontM.setVisibility(View.INVISIBLE);
        CardBackM.setVisibility(View.VISIBLE);
        CardBackR.setVisibility(View.VISIBLE);
        CardBackL.setVisibility(View.VISIBLE);

        setCameraDistance(CardRootM);
        setCameraDistance(CardRootL);
        setCameraDistance(CardRootR);


    }


    /**
     * 改变视角距离, 贴近屏幕
     */
    private void setCameraDistance(RelativeLayout CardTurnRoot) {
        int distance = 10000;
        float scale = getResources().getDisplayMetrics().density * distance;
        CardTurnRoot.setCameraDistance(scale);
    }



    private class CardFrontL_Listener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(gameModel.isSoundOn()==1) soundPool.play(2,1,1,0,0,0.5f);
            ZoomObject zoomHelper = new ZoomObject();
            zoomHelper.zoomImageFromThumb(CardFrontL,expandedCard,findViewById(R.id.dropCardLayout));
        }

    }

    private class CardFrontM_Listener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(gameModel.isSoundOn()==1) soundPool.play(2,1,1,0,0,0.5f);
            ZoomObject zoomHelper = new ZoomObject();
            zoomHelper.zoomImageFromThumb(CardFrontM,expandedCard,findViewById(R.id.dropCardLayout));
        }

    }

    private class CardFrontR_Listener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(gameModel.isSoundOn()==1) soundPool.play(2,1,1,0,0,0.5f);
            ZoomObject zoomHelper = new ZoomObject();
            zoomHelper.zoomImageFromThumb(CardFrontR,expandedCard,findViewById(R.id.dropCardLayout));
        }

    }


    //Bgm
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
            // soundpool
            //play(id, 1, 1, 0, 0, 1) =(id, left, right, priority, loop, rate )
            soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC,0);
            soundPool.load(this, R.raw.button, 1);  //按键
            soundPool.load(this, R.raw.map_cardrotate, 2); //转卡
            soundPool.load(this, R.raw.card_getnew,3);  //getnew
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
