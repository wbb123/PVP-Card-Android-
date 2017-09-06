package mangoabliu.finalproject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.Random;

import mangoabliu.finalproject.Animation.OnSwipeTouchListener;
import mangoabliu.finalproject.Animation.ZoomObject;
import mangoabliu.finalproject.Layout.FontTextView;
import mangoabliu.finalproject.Model.GameModel;
import mangoabliu.finalproject.Model.Planet;
import mangoabliu.finalproject.Model.StepService;

import static android.content.ContentValues.TAG;
import static mangoabliu.finalproject.Animation.DisplayImageOptionsUtil.getDisplayImageOptions;

/**
 * Created by 10836 on 2016-11-16.
 */

// Add PossibleCard, LocName by Lyrisxu  11-28

public class LocationDialogView extends Dialog {

    GameModel gameModel;
    int clickedLoc, btnJudge=0;
    ImageView image_pCard;
    ImageView expandedImageView;

    //change into imageview
    ImageView action;

    FontTextView tv_locName;
    String clickedLocName;

    ZoomObject zoomHelper;

    boolean actionVisible;
    private SoundPool soundPool =null;
    private Context con;

    //Add Style /Lyris 11-26
    protected LocationDialogView(Context context, int loc, int style ) {
        super(context, style);
        clickedLoc = loc;
        con=context;
        gameModel= GameModel.getInstance();
    }


    public ImageView getAction(){
        return action;
    }

    public boolean getActionVisible(){
        return actionVisible;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置不显示对话框标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置对话框显示哪个布局文件

        //ADD FULLSCREEN
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.dialog_location);
        //getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //对话框也可以通过资源id找到布局文件中的组件，从而设置点击侦听
//        Button cancel = (Button) findViewById(R.id.locationDialogCancel);

        BGMInit();

        action = (ImageView) findViewById(R.id.locationAction);
        action.setEnabled(false);

        if (gameModel.getUserAccount().getCurrentLocId() == clickedLoc) {
            action.setBackgroundResource(R.drawable.btn_drop);
            action.setEnabled(true);
            actionVisible = true;
            btnJudge=1;   //判断是drop按钮
        }

        //PossibleCardDisplay  /Lyris
        image_pCard =(ImageButton) findViewById(R.id.locationImage);
        final int pCardID = possibleCardGenerator();
        expandedImageView =  (ImageView) findViewById(
                R.id.expanded_image);
        initCardData(pCardID);
        zoomHelper = new ZoomObject(this);


        image_pCard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(gameModel.isSoundOn()==1) soundPool.play(4,1,1,0,0,1);
                zoomHelper.zoomImageFromThumb(image_pCard,expandedImageView,findViewById(R.id.expanded_image));
            }
        });

        image_pCard.setOnTouchListener(new OnSwipeTouchListener(getContext()) {

            public void onSwipeRight(float dis, float velocity) {
                //Toast.makeText(getContext(), "right", Toast.LENGTH_SHORT).show();
                rotateCard(image_pCard,"left",dis,velocity);
            }
            public void onSwipeLeft(float dis, float velocity) {
                //Toast.makeText(getContext(), "left", Toast.LENGTH_SHORT).show();
                rotateCard(image_pCard,"left",dis,velocity);
            }
        });


        expandedImageView.setOnTouchListener(new OnSwipeTouchListener(getContext()) {

            public void onSwipeRight(float dis, float velocity) {
                //Toast.makeText(getContext(), "right", Toast.LENGTH_SHORT).show();
                rotateCard(expandedImageView,"right",dis,velocity);

            }
            public void onSwipeLeft(float dis, float velocity) {
                //Toast.makeText(getContext(), "left", Toast.LENGTH_SHORT).show();
                rotateCard(expandedImageView,"left",dis,velocity);
            }
        });

        //Display LocationName  /Lyris
        tv_locName = (FontTextView) findViewById(R.id.locationDialogTitle);

        gameModel = GameModel.getInstance();

//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });


        gameModel = GameModel.getInstance();
        int currentLocID = gameModel.getUserAccount().getCurrentLocId();
        Log.i(TAG, currentLocID+","+clickedLoc);
        if ((currentLocID == 1 && clickedLoc == 2) ||
                (currentLocID == 2 && (clickedLoc == 1 || clickedLoc == 3 || clickedLoc ==4)) ||
                (currentLocID == 3 && (clickedLoc == 2 || clickedLoc == 5))||
                (currentLocID == 4 && (clickedLoc == 2 || clickedLoc == 5))||
                (currentLocID == 5 && (clickedLoc == 3 || clickedLoc == 4 || clickedLoc == 6))||
                (currentLocID == 6 && (clickedLoc == 5))) {

            action.setBackgroundResource(R.drawable.btn_go);
            action.setEnabled(true);
            actionVisible = true;
            btnJudge =2; //判断是go按钮
        }

        if (btnJudge==2)  //go
            action.setOnClickListener(new startTripListener());
        else if (btnJudge==1) //drop
            action.setOnClickListener(new drop_DropCardListener());

        initLocName();

    }


    private void rotateCard(ImageView view, String leftOrRight, float dis, float velocity){
        if(gameModel.isSoundOn()==1) soundPool.play(1,1,1,0,0,1);
        int percentage = 0;
        if (leftOrRight.equals("left"))
            dis = Math.abs(dis);

        if (dis != 0)
            percentage = Math.round(view.getWidth()/dis);
        if (percentage > 5)
            percentage = 2;
        else
            percentage = 4;

        view.animate().setDuration(2000);
        if (leftOrRight.equals("left"))
            view.animate().rotationYBy(360*-percentage);
        else
            view.animate().rotationYBy(360*percentage);
    }


    //PossibleCardGenerator - Random
    private int possibleCardGenerator(){
        Random rand = new Random();
        int pOneCard = rand.nextInt(18);
        Resources res = this.getContext().getResources();
        String[] CardsName = res.getStringArray(R.array.cards_name);
        Context context = image_pCard.getContext();
        int pCardID = context.getResources().getIdentifier(CardsName[pOneCard], "drawable", context.getPackageName());
        return pCardID;
    }

    //Init Card
    private void initCardData(int pCardID){

        ImageLoader imageLoader;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this.getContext()));
        String imageUriFront = "drawable://" + pCardID;
        ImageLoader.getInstance().displayImage(imageUriFront, image_pCard, getDisplayImageOptions());
        ImageLoader.getInstance().displayImage(imageUriFront, expandedImageView, getDisplayImageOptions());
    }

    //Init Location Name
    private  void initLocName(){
        ArrayList<Planet> planets = gameModel.getPlanets();
        Planet clickedPlanet = planets.get(clickedLoc-1);
        clickedLocName = clickedPlanet.getPlanetName();
        tv_locName.setText(clickedLocName);
//        tv_locName.setTypeface(typeFace);
    }



    private class drop_DropCardListener implements View.OnClickListener {

        public void onClick(View v) {

            if(gameModel.isSoundOn()==1) soundPool.play(4,1,1,0,0,1);

            Intent intent = new Intent();
            intent.setClass(v.getContext(), CardDropActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            v.getContext().startActivity(intent);



        }
    }
    private class startTripListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            if(gameModel.isSoundOn()==1) soundPool.play(4,1,1,0,0,1);

            gameModel.getUserAccount().setTargetLocId(clickedLoc);
            gameModel.updateTargetLocation(gameModel.getUserAccount().getUserId(),
                    clickedLoc);
            gameModel.setTotalSteps(gameModel.getDistances()[gameModel.getWalkingLine()-1]);
            gameModel.getMainGameActivity().startService(new Intent(gameModel.getMainGameActivity(),StepService.class));
            dismiss();
        }

    }


    public void BGMInit(){

        if(gameModel.isSoundOn()==1){
            // soundpool
            //play(id, 1, 1, 0, 0, 1) =(id, left, right, priority, loop, rate )
            soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC,0);
            soundPool.load(con, R.raw.map_cardrotate, 1);  //按fight键
            soundPool.load(con, R.raw.map_clickplanet, 2); //点击星球
            soundPool.load(con, R.raw.map_info,3);  //点击个人信息键
            soundPool.load(con, R.raw.button,4);
        }
    }


}

