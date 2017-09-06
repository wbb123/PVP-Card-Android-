package mangoabliu.finalproject;

import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.Image;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

import mangoabliu.finalproject.Animation.ZoomObject;
import mangoabliu.finalproject.Layout.FontTextView;
import mangoabliu.finalproject.Model.GameModel;

/**
 * Created by herenjie on 2016/11/16.
 */

public class UserProfileAlertView extends Dialog {

    //Add Style_transparent
    String name;
    GameModel gameModel;
    private Context con;
    private SoundPool soundPool;

    protected UserProfileAlertView(Context context, int style, String username) {
        super(context, style);
        name = username;
        con=context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置不显示对话框标题栏
        gameModel = GameModel.getInstance();
        gameModel.setUserProfileAlertView(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //ADD FULLSCREEN
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        //设置对话框显示哪个布局文件
        setContentView(R.layout.dialog_userprofile);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        BGMInit();

        final GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(getContext()));


        //对话框也可以通过资源id找到布局文件中的组件，从而设置点击侦听
        Button bt = (Button) findViewById(R.id.userProfileDialogCancel);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gameModel.isSoundOn()==1) soundPool.play(4,1,1,0,0,3);
                dismiss();
            }
        });


        FontTextView tv_userName = (FontTextView) findViewById(R.id.usernameProfile);
        tv_userName.setText(name);
/*
        Log.i("ddd",""+gridImages.size());
        for (int i = 0; i < gridImages.size(); i++) {
            clickedCard = gridImages.get(i);
            clickedExpand = expandImages.get(i);
            gridImages.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gameModel.showToast(v.getContext(), "傻逼");
                    ZoomObject zoomHelper = new ZoomObject();
                    zoomHelper.zoomImageFromThumb(clickedCard, clickedExpand, findViewById(R.id.expandedCard));
                }
            });
        }*/
        ImageView userIcon = (ImageView) findViewById(R.id.userIcon);
        userIcon.setVisibility(View.INVISIBLE);

    }

    public class ImageAdapter extends BaseAdapter {

        private Context mcontext;

        ArrayList<Integer> myImageIds = gameModel.getMyImageIds();

        public ImageAdapter(Context c) {
            mcontext = c;
        }

        @Override
        public int getCount() {
            return myImageIds.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ImageView iv;

            iv = new ImageView(mcontext);
            iv.setImageResource(myImageIds.get(position));
            Log.i("UserProfileView","position:"+position+","+myImageIds.get(position).toString());
            Log.i("UserProfileView","myImageIdsSize:"+myImageIds.size());
            //原值- Lyris修改
//                iv.setScaleType(ImageView.ScaleType.FIT_XY);
//                iv.setLayoutParams(new GridView.LayoutParams(400,500));
            iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            iv.setLayoutParams(new GridView.LayoutParams(350, 450));

            return iv;
        }
    }

    public void BGMInit(){

        if(gameModel.isSoundOn()==1){
            // soundpool
            //play(id, 1, 1, 0, 0, 1) =(id, left, right, priority, loop, rate )
            soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC,0);
            soundPool.load(con, R.raw.map_btnfight, 1);  //按fight键
            soundPool.load(con, R.raw.map_clickplanet, 2); //点击星球
            soundPool.load(con, R.raw.map_info,3);  //点击个人信息键
            soundPool.load(con, R.raw.button,4);
        }
    }


}
