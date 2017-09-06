package mangoabliu.finalproject.Model;


import android.os.Handler;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import mangoabliu.finalproject.BattleActivity;

/**
 * Created by SHI Zhongqi on 2016-11-29.
 */

public class BattleModel {
    private static BattleModel instance;
    private final String String_base_url="http://i.cs.hku.hk/~zqshi/ci/index.php/";
    private BattleActivity battleActivity;
    private ArrayList<Card> UserCards;

    private HashMap<Card,Integer> mapIsUserCardPicked =new HashMap<>();
    private HashMap<Integer,Card> mapCardIDtoCard =new HashMap<>();
    private HashMap<Integer,Card> mapBtnNumberChooseToCard = new HashMap<>();
    private HashMap<Integer,Integer> myCardHP = new HashMap<>();

    private UserAccount myUser;

    private HashMap<Integer,Card> mapOtherCardIDtoCard =new HashMap<>();
    private HashMap<Integer,Card> mapOtherBtnNumberChooseToCard = new HashMap<>();
    private HashMap<Integer,Integer> otherCardHP = new HashMap<>();

    private Handler handler = new Handler( );
    private Runnable runnable;
    private Runnable timeRunnable;
    private int int_stateCase =0;
    private int int_myplayerID =0;
    private int int_roomId;
    private int int_myPlayCardIndex =-1;
    private int int_otherPlayCardIndex =-1;
    private int int_otherUserID =0;
    private String String_otherUserName;
    private boolean bool_winGame=false;
    /**
     *  0  just start
     *  1  循环str_isRoomReadyM_function
     *  2  该你出牌
     *  3  循环，str_isFightReadyM_function
     *  4  循环，str_myTurnM_function
     *  5  循环，str_getTime_function ->单独拿出来写
     *
     */
    protected final static String str_getTime_function="getTime";
    protected final static String str_myTurnM_function = "myTurnM";
    protected final static String str_playCardM_function = "playCardM";
    protected final static String str_isFightReadyM_function = "isFightReadyM";
    protected final static String str_setCardsM_function = "setCardsM";
    protected final static String str_isRoomReadyM_function = "isRoomReadyM";
    protected final static String str_applyForFightM_function = "applyForFightM";


    private BattleModel() {
        // public Card(int _CardID,String _CardName,int _CardHP,int _CardAttack,int _CardArmor,int _CardRarity);
        runnable = new Runnable( ) {
            public void run ( ) {
                switch(int_stateCase){
                    case 1:
                        getIsRoomReady();
                        break;
                    case 3:
                        getIsFightReady();
                        break;
                    case 4:
                        getIsMyTurn();
                        break;
                }
                //postDelayed(this,2000)方法安排一个Runnable对象到主线程队列中
            }
        };


        timeRunnable = new Runnable( ) {
            public void run ( ) {
                serverPHPPostConnection(getTimeUrl(),"",str_getTime_function);
                handler.postDelayed(this,3000);
                //postDelayed(this,2000)方法安排一个Runnable对象到主线程队列中
            }
        };
    }

    public static BattleModel getInstance(){
        if(null == instance){
            instance = new BattleModel();
        }

        return instance;
    }

    public void initialBattle(){
        int_roomId =0;
        int_myplayerID =0;
        int_stateCase =0;
        int_myPlayCardIndex =0;
        int_otherPlayCardIndex =0;
        int_otherUserID =0;
        String_otherUserName ="";
        bool_winGame=false;
        mapIsUserCardPicked.clear();
        mapCardIDtoCard.clear();
        mapBtnNumberChooseToCard.clear();
        myCardHP.clear();
        mapOtherCardIDtoCard.clear();
        mapOtherBtnNumberChooseToCard.clear();
        otherCardHP.clear();
    }


    public void setBattleActivity(BattleActivity battleActivity){
        this.battleActivity=battleActivity;
    }

    public void setUserCards(ArrayList<Card> userCards){
        this.UserCards=userCards;
        for(int i=0;i<UserCards.size();i++){
            mapIsUserCardPicked.put(UserCards.get(i),1);//1 就是可以选的，0是不可以选的
            mapCardIDtoCard.put(UserCards.get(i).getCardID(),UserCards.get(i));
        }
    }

    public ArrayList<Card> getUserCards(){
        return this.UserCards;
    }
    //1 就是可以选的，0是不可以选的
    public int checkCardPick(int CardID){
        return mapIsUserCardPicked.get(mapCardIDtoCard.get(CardID));
    }

    public Card getCard(int CardID){
        return mapCardIDtoCard.get(CardID);
    }

    public Card getOtherCard(int CardID){
        return mapOtherCardIDtoCard.get(CardID);
    }

    public void attackOtherTarget(int otherSideIndex){
        int_otherPlayCardIndex =otherSideIndex;
        if(int_myPlayCardIndex>0)
            playACard();
    }

    public void chooseMyCard(int myIndex){
        int_myPlayCardIndex =myIndex;
        if(myCardHP.get(int_myPlayCardIndex)<=0)
            int_myPlayCardIndex=-1;
    }

    public void pickCard(int CardID,int index){
        mapIsUserCardPicked.put(mapCardIDtoCard.get(CardID),0);
        if(mapBtnNumberChooseToCard.containsKey(index))
            mapIsUserCardPicked.put(mapBtnNumberChooseToCard.get(index),1);
        mapBtnNumberChooseToCard.put(index, mapCardIDtoCard.get(CardID));
        battleActivity.updateMyCard(CardID,index);
    }

    public int chosedCardNo(){
        return mapBtnNumberChooseToCard.size();
    }

    public void setUserAccount(UserAccount user){
        myUser = user;
    }

    public UserAccount getUserAccount(){
        return myUser;
    }

    public int getMyCardHP(int index){
        return myCardHP.get(index);
    }

    public int getOtherCardHP(int index){
        return otherCardHP.get(index);
    }
    //Server Communication from here

    //applyForFightM
    public void applyForFight(){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("UserID",myUser.getUserId());
            int_stateCase =1;
            serverPHPPostConnection(getApplyForFightUrl(),jsonObject.toString(),str_applyForFightM_function);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void applyForFightSuccessful(String result){
        try {
            JSONObject jsonObj = new JSONObject(result);
            if((Integer)jsonObj.get("code")==0) {
                this.int_roomId =Integer.parseInt(jsonObj.getString("RoomID"));
                int_stateCase=1;
                handler.postDelayed(runnable,500);
            }
            else
                battleActivity.displayMessage((String)jsonObj.get("message"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //isRoomReadyM
    public void getIsRoomReady(){
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("RoomID", int_roomId);

            serverPHPPostConnection(getIsRoomReadyUrl(),jsonObject.toString(),str_isRoomReadyM_function);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //getRoomSuccessful
    public void getIsRoomReadySuccessful(String result){
        try {
            JSONObject jsonObj = new JSONObject(result);

            if((Integer)jsonObj.get("code")==0) {
                handler.removeCallbacks(runnable);
                JSONArray jsonArr=(JSONArray)jsonObj.get("UserName");
                JSONObject jsonUserObject1=jsonArr.getJSONObject(0);
                JSONObject jsonUserObject2=jsonArr.getJSONObject(1);
                int Player1ID = Integer.parseInt(jsonUserObject1.getString("UserID"));
                if(Player1ID==myUser.getUserId())
                    int_myplayerID =1;
                else
                    int_myplayerID =2;

                switch(int_myplayerID){
                    case 1:
                        int_otherUserID =Integer.parseInt(jsonUserObject2.getString("UserID"));
                        String_otherUserName =(String) jsonUserObject2.get("UserName");
                        break;
                    case 2:
                        int_otherUserID =Integer.parseInt(jsonUserObject1.getString("UserID"));
                        String_otherUserName =(String) jsonUserObject1.get("UserName");
                        break;
                }
                battleActivity.setOtherSideUserName(String_otherUserName);
                battleActivity.waitingRoomFinished();
            }
            else {
                battleActivity.displayMessage((String) jsonObj.get("message"));
                handler.postDelayed(runnable,3000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //setCardsM
    public void playerCardPickConfirm(){
        /*
        $paramemter['RoomID'] = $json['RoomID'];
        $paramemter['UserID'] = $json['UserID'];
        $paramemter['CardID1'] = $json['CardID1'];
        $paramemter['CardID2'] = $json['CardID2'];
        $paramemter['CardID3'] = $json['CardID3'];
         */
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("RoomID", int_roomId);
            jsonObject.put("UserID",myUser.getUserId());
            jsonObject.put("CardID1", mapBtnNumberChooseToCard.get(1).getCardID());
            jsonObject.put("CardID2", mapBtnNumberChooseToCard.get(2).getCardID());
            jsonObject.put("CardID3", mapBtnNumberChooseToCard.get(3).getCardID());
            for(int i=1;i<4;i++)
                myCardHP.put(i,mapBtnNumberChooseToCard.get(i).getCardHP());

            serverPHPPostConnection(getSetCardsUrl(),jsonObject.toString(),str_setCardsM_function);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setCardsSuccessful(String result){
        try {
            JSONObject jsonObj = new JSONObject(result);
            if((Integer)jsonObj.get("code")==0) {
                this.int_roomId =Integer.parseInt(jsonObj.getString("RoomID"));
                int_stateCase =3;
                /**
                 *  0  just start
                 *  1  循环str_isRoomReadyM_function
                 *  3  循环，str_isFightReadyM_function
                 *  4  循环，str_myTurnM_function
                 *  5  循环，str_getTime_function ->单独拿出来写
                 */
                handler.postDelayed(runnable,500);
            }
            else {
                battleActivity.displayMessage((String) jsonObj.get("message"));
                battleActivity.restartConfirm();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //isFightReadyM
    public void getIsFightReady(){
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("RoomID", int_roomId);

            serverPHPPostConnection(getIsFightReadyUrl(),jsonObject.toString(),str_isFightReadyM_function);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void isFightReadySuccessful(String result){
        try {
            JSONObject jsonObj = new JSONObject(result);
            if((Integer)jsonObj.get("code")==0) {
                JSONArray jsonArr=(JSONArray)jsonObj.get("CardInfo");
                int temp_id=0;
                int_stateCase =2;
                if(int_myplayerID ==1){
                    temp_id+=3;
                    battleActivity.setTurn(myUser.getUserName());
                }
                else {
                    int_stateCase = 4;
                    battleActivity.setTurn(String_otherUserName);
                }
                JSONObject jsonUserObject1=jsonArr.getJSONObject(0+temp_id);
                JSONObject jsonUserObject2=jsonArr.getJSONObject(1+temp_id);
                JSONObject jsonUserObject3=jsonArr.getJSONObject(2+temp_id);

                int CardID = Integer.parseInt(jsonUserObject1.getString("CardID"));
                String CardName = jsonUserObject1.getString("CardName");
                int CardHP = Integer.parseInt(jsonUserObject1.getString("CardHP"));
                int CardAttack = Integer.parseInt(jsonUserObject1.getString("CardAttack"));
                int CardArmor = Integer.parseInt(jsonUserObject1.getString("CardArmor"));
                int CardRarity = Integer.parseInt(jsonUserObject1.getString("CardRarity"));
                mapOtherBtnNumberChooseToCard.put(1,new Card(CardID,CardName,CardHP,CardAttack,CardArmor,CardRarity));
                CardID = Integer.parseInt(jsonUserObject2.getString("CardID"));
                CardName = jsonUserObject2.getString("CardName");
                CardHP = Integer.parseInt(jsonUserObject2.getString("CardHP"));
                CardAttack = Integer.parseInt(jsonUserObject2.getString("CardAttack"));
                CardArmor = Integer.parseInt(jsonUserObject2.getString("CardArmor"));
                CardRarity = Integer.parseInt(jsonUserObject2.getString("CardRarity"));
                mapOtherBtnNumberChooseToCard.put(2,new Card(CardID,CardName,CardHP,CardAttack,CardArmor,CardRarity));
                CardID = Integer.parseInt(jsonUserObject3.getString("CardID"));
                CardName = jsonUserObject3.getString("CardName");
                CardHP = Integer.parseInt(jsonUserObject3.getString("CardHP"));
                CardAttack = Integer.parseInt(jsonUserObject3.getString("CardAttack"));
                CardArmor =Integer.parseInt(jsonUserObject3.getString("CardArmor"));
                CardRarity = Integer.parseInt(jsonUserObject3.getString("CardRarity"));
                mapOtherBtnNumberChooseToCard.put(3,new Card(CardID,CardName,CardHP,CardAttack,CardArmor,CardRarity));
                mapOtherCardIDtoCard.put(mapOtherBtnNumberChooseToCard.get(1).getCardID(),mapOtherBtnNumberChooseToCard.get(1));
                mapOtherCardIDtoCard.put(mapOtherBtnNumberChooseToCard.get(2).getCardID(),mapOtherBtnNumberChooseToCard.get(2));
                mapOtherCardIDtoCard.put(mapOtherBtnNumberChooseToCard.get(3).getCardID(),mapOtherBtnNumberChooseToCard.get(3));
                battleActivity.updateOtherSideCard(mapOtherBtnNumberChooseToCard.get(1).getCardID(),1);
                battleActivity.updateOtherSideCard(mapOtherBtnNumberChooseToCard.get(2).getCardID(),2);
                battleActivity.updateOtherSideCard(mapOtherBtnNumberChooseToCard.get(3).getCardID(),3);
                for(int i=1;i<4;i++) {
                    otherCardHP.put(i, mapOtherBtnNumberChooseToCard.get(i).getCardHP());
                    Log.i("BattleModel","OthercardHP:"+otherCardHP.get(i));
                }
                /**
                 *  0  just start
                 *  1  循环str_isRoomReadyM_function
                 *  3  循环，str_isFightReadyM_function
                 *  4  循环，str_myTurnM_function
                 *  5  循环，str_getTime_function ->单独拿出来写
                 *
                 */
                if(int_stateCase==4){
                    handler.postDelayed(runnable,1000);
                }
            }
            else {
                handler.postDelayed(runnable,2000);
                battleActivity.displayMessage((String) jsonObj.get("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //playCardM
    public void playACard() {
        try {
            JSONObject jsonObject = new JSONObject();
            //	{"RoomID":"19","UserID":"12","Player1CardID":"1","Player2CardID":"3","Player1CardNum":"1","Player2CardNum":"2","Player":"1"}
            //  {"RoomID":19,"UserID":12,"Player1CardID":1,"Player12CardID":1,"Player1CardNum":1,"Player12CardNum":1,"Player":1}
            jsonObject.put("RoomID", int_roomId);
            jsonObject.put("UserID",myUser.getUserId());
            int int_otherPlayerID=2;
            if(int_myplayerID==2)
                int_otherPlayerID=1;

            jsonObject.put("Player"+ int_myplayerID +"CardID", mapBtnNumberChooseToCard.get(int_myPlayCardIndex).getCardID());
            jsonObject.put("Player"+ int_otherPlayerID +"CardID", mapOtherBtnNumberChooseToCard.get(int_otherPlayCardIndex).getCardID());
            jsonObject.put("Player"+ int_myplayerID +"CardNum", int_myPlayCardIndex);;
            jsonObject.put("Player"+ int_otherPlayerID +"CardNum", int_otherPlayCardIndex);
            jsonObject.put("Player", int_myplayerID);


            serverPHPPostConnection(getPlayCardUrl(),jsonObject.toString(),str_playCardM_function);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void playACardSuccessful(String result){
        try {
            JSONObject jsonObj = new JSONObject(result);
            if((Integer)jsonObj.get("code")==0) {
                int hurt = (int)jsonObj.getDouble("Hurt");
                otherCardHP.put(int_otherPlayCardIndex,otherCardHP.get(int_otherPlayCardIndex)-hurt);
                battleActivity.playerAttackOther(hurt,int_myPlayCardIndex,int_otherPlayCardIndex);


                if(Integer.parseInt(jsonObj.getString("Win"))!=0)
                    battleActivity.playerWin();
                else{
                    int_stateCase = 4;
                    handler.postDelayed(runnable,1000);
                    battleActivity.setTurn(String_otherUserName);
                    int_myPlayCardIndex=0;
                    int_otherPlayCardIndex=0;
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //isMyTurn
    public void getIsMyTurn(){
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("RoomID", int_roomId);
            jsonObject.put("UserID",myUser.getUserId());

            serverPHPPostConnection(getMyTurnUrl(),jsonObject.toString(),str_myTurnM_function);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void isMyTurnSuccessful(String result){
        try {
            JSONObject jsonObj = new JSONObject(result);
            if((Integer)jsonObj.get("code")==0) {
                handler.removeCallbacks(runnable);
                battleActivity.setTurn(myUser.getUserName());
                JSONObject lastPlay = (JSONObject) jsonObj.get("LastPlay");
                int_stateCase = 2;
                //{"code":0,"LastPlay":{"PlayID":"3","Player1ID":"12","Player2ID":"18","UserID":"18","Player1Card1ID":"1","Player1Card1HP":"267","Player1Card2ID":"3","Player1Card2HP":"300","Player1Card3ID":"2","Player1Card3HP":"300","Player2Card1ID":"1","Player2Card1HP":"300","Player2Card2ID":"2","Player2Card2HP":"267","Player2Card3ID":"3","Player2Card3HP":"300","FromNum":"2","ToNum":"1"}}
                int fromNo = lastPlay.getInt("FromNum");
                int toNo = lastPlay.getInt("ToNum");
                int newHP= lastPlay.getInt("Player"+ int_myplayerID +"Card"+toNo+"HP");
                int oldHP = myCardHP.get(toNo);
                Log.i("BattleModel","newHP:"+newHP+",oldHP"+oldHP);
                myCardHP.put(toNo,newHP);
                battleActivity.otherSideAttackPlayer(oldHP-newHP,toNo,fromNo);
                bool_winGame=true;
                for(int i=1;i<4;i++){
                    if(myCardHP.get(i)>0) {
                        bool_winGame = false;
                        break;
                    }
                }
                if(bool_winGame){
                    battleActivity.OtherWin();
                }

            }
            else{
                handler.postDelayed(runnable,3000);
                battleActivity.setTurn(String_otherUserName);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    //HTTP Request Related Info
    private void serverPHPPostConnection(String str_URL,String str_JSON,String str_Function){
        try{
            GameHTTPRequest mTask = new GameHTTPRequest();
            mTask.execute(str_URL,str_JSON,str_Function);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getTimeUrl(){
        return String_base_url+"/Game/getTime";
    }

    private String getMyTurnUrl(){
        return String_base_url+"/Game/myTurnM";
    }

    private String getPlayCardUrl(){
        return String_base_url+"/Game/playCardM";
    }

    private String getIsFightReadyUrl(){
        return String_base_url+"/Game/isFightReadyM";
    }

    private String getSetCardsUrl(){
        return String_base_url+"/Game/setCardsM";
    }

    private String getIsRoomReadyUrl(){
        return String_base_url+"/Game/isRoomReadyM";
    }

    private String getApplyForFightUrl(){
        return String_base_url+"/Game/applyForFightM";
    }
}
