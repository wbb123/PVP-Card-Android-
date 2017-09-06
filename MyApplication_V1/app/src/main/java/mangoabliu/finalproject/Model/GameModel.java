package mangoabliu.finalproject.Model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;

import mangoabliu.finalproject.CardDropActivity;
import mangoabliu.finalproject.LoginActivity;
import mangoabliu.finalproject.MainGameActivity;
import mangoabliu.finalproject.MenuActivity;
import mangoabliu.finalproject.RegistrationActivity;
import mangoabliu.finalproject.UserProfileAlertView;

/**
 * Created by SHI Zhongqi on 2016-11-16.
 */

public class GameModel {
    private LinkedList<AppCompatActivity> activityLinkedList = new LinkedList<>();
    private static GameModel instance;
    private final String String_base_url="http://i.cs.hku.hk/~zqshi/ci/index.php/";
    private MenuActivity menuActivity;
    private LoginActivity loginActivity;
    private RegistrationActivity registrationActivity;
    private MainGameActivity mainGameActivity;
    private CardDropActivity cardDropActivity;
    private UserProfileAlertView userProfileAlertView;

    protected final static String str_login_function="login";
    protected final static String str_registration_function="registration";
    protected final static String str_updateUserStep_function = "updateUserStep";
    protected final static String str_updateTargetLocation_function = "updateTargetLocation";
    protected final static String str_updateUserCardRelation_function = "updateUserCardRelation";
    protected final static String str_updateCurrentLocation_function = "updateCurrentLocation";
    protected final static String str_updateCurrentPosition_function = "updateCurrentPosition";
    protected final static String str_getUserCard_function = "GetUserCards";

    ArrayList<Planet> planets = new ArrayList<Planet>();
    ArrayList<Card> UserCards= new ArrayList<Card>();
    ArrayList<Integer> myImageIds = new ArrayList<Integer>();

    int[] distances = new int[]{20,32,20,40,20,32};

    private UserAccount myUser;

    private int totalSteps = 0;

    private int musicSwitch=1;
    private int soundSwitch=1;

    private GameModel() {
    }

    public int getTotalSteps(){
        return totalSteps;
    }

    public void setTotalSteps(int steps){
        totalSteps = steps;
    }

    public MainGameActivity getMainGameActivity(){
        return mainGameActivity;
    }

    public int[] getDistances(){
        return distances;
    }

    public void setUserAccount(UserAccount user){
        myUser = user;
    }

    public UserAccount getUserAccount(){
        return myUser;
    }

    public static GameModel getInstance(){
        if(null == instance){
            instance = new GameModel();
        }
        return instance;
    }

    public ArrayList<Planet> getPlanets(){
        return planets;
    }

    public ArrayList<Card> getUserCards(){
        return UserCards;
    }

    public ArrayList<Integer> getMyImageIds(){
        return myImageIds;
    }


    //Registration Related
    public void setRegistrationActivity(RegistrationActivity registrationActivity){
        this.registrationActivity=registrationActivity;
    }

    public void registration(String str_Username,String str_Password){
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("UserName",str_Username);
            jsonObject.put("Password",str_Password);
            jsonObject.put("Nickname","shabi");
            jsonObject.put("Alliance","adf");

            serverPHPPostConnection(getRegistrationURL(),jsonObject.toString(),str_registration_function);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void registration_finished(String str_result){
        try {
            JSONObject jsonObj = new JSONObject(str_result);
            if((Integer)jsonObj.get("code")==0) {
                registrationActivity.successful((String)jsonObj.get("message"));
            }
            else
                registrationActivity.errorMessage((String)jsonObj.get("message"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void mainDisplayDistance(){
        mainGameActivity.displayDistance();
    }

    public void mainUpdateUFO(float x,float y){
        mainGameActivity.updateUFO(x,y);
    }

    public void updateMainGameStep(int step){

        double startLocX = planets.get(myUser.getCurrentLocId()-1).getPlanetX();
        double startLocY = planets.get(myUser.getCurrentLocId()-1).getPlanetY();


        double destLocX = planets.get(myUser.getTargetLocId()-1).getPlanetX();
        double destLocY = planets.get(myUser.getTargetLocId()-1).getPlanetY();

        double currentLocX = myUser.getCurrentLocCoordinate()[0];
        double currentLocY = myUser.getCurrentLocCoordinate()[1];
        double nextLocX = 0;
        double nextLocY = 0;


        int walkedDis = myUser.getWalkDistance();

        myUser.setWalkDistance(step + walkedDis);


        nextLocX = currentLocX + (destLocX-startLocX)/totalSteps;
        nextLocY = currentLocY + (destLocY-startLocY)/totalSteps;
        myUser.setCurrentLocCoordinate(new double[]{ nextLocX, nextLocY});

        float animEndX = (float) nextLocX;
        float animEndY = (float) nextLocY;

        this.mainGameActivity.updateDistance(animEndX,animEndY);

    }

    public void setMenuActivity(MenuActivity menuActivity){
        this.menuActivity=menuActivity;
    }

    public MenuActivity getMenuActivity(){
        return this.menuActivity;
    }

    public void setCardDropActivity(CardDropActivity cardDropActivity){
        this.cardDropActivity=cardDropActivity;
    }

    public CardDropActivity getCardDropActivity(){
        return this.cardDropActivity;
    }

    //Main Game Related
    public void setMainGameActivity(MainGameActivity mainGameActivity){
        this.mainGameActivity=mainGameActivity;
    }

    public void setUserProfileAlertView(UserProfileAlertView userProfileAlertView){
        this.userProfileAlertView = userProfileAlertView;
    }

    //login related Activity
    public void setLoginActivity(LoginActivity loginActivity){
        this.loginActivity=loginActivity;
    }

    public void login(String str_UserName, String str_Password) {
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("UserName", str_UserName);
            jsonObject.put("Password", str_Password);

            serverPHPPostConnection(getLoginURL(),jsonObject.toString(),str_login_function);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void login_finished(String str_result){
        try {
            JSONObject jsonObj = new JSONObject(str_result);
            if((Integer)jsonObj.get("code")==0) {
                loginActivity.successful(jsonObj.toString());
            }
            else
                loginActivity.errorMessage((String)jsonObj.get("message"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void getUserCard(int str_UserId){
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("UserID",str_UserId);

            serverPHPPostConnection(getUserCardURL(),jsonObject.toString(),
                    str_getUserCard_function);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getUserCardFinished(String str_result){
        try {
            JSONObject jsonObj = new JSONObject(str_result);
            if((Integer)jsonObj.get("code")==0) {
                mainGameActivity.getUserCards_successful(jsonObj.toString());
            }
            else
                mainGameActivity.errorMessage((String)jsonObj.get("message"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updateUserCardRelation(int str_UserId, int str_CardID){
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("UserID",str_UserId);
            jsonObject.put("CardID",str_CardID);

            serverPHPPostConnection(getUpdateUserCardRelationURL(),jsonObject.toString(),
                    str_updateUserCardRelation_function);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateUserCardRelationFinished(String str_result){
        try {
            JSONObject jsonObj = new JSONObject(str_result);
            if ((Integer) jsonObj.get("code") == 0) {
                mainGameActivity.updateUserCardRelationSuccessful(jsonObj.toString());
            } else if((Integer) jsonObj.get("code") == 1) {
                cardDropActivity.showMessage();
            }else{
                mainGameActivity.errorMessage((String)jsonObj.get("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateUserStep(int str_UserId,int walkDistance){
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("UserID",str_UserId);
            jsonObject.put("WalkDistance",walkDistance);

            serverPHPPostConnection(getUpdateUserStepURL(),jsonObject.toString(),str_updateUserStep_function);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateUserStepFinished(String str_result){
        try {
            JSONObject jsonObj = new JSONObject(str_result);
            if((Integer)jsonObj.get("code")==0) {
                mainGameActivity.sendStepSuccessful(jsonObj.toString());
            }
            else
                mainGameActivity.errorMessage((String)jsonObj.get("message"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void updateTargetLocation(int str_UserId, int targetLocation){
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("UserID",str_UserId);
            jsonObject.put("TargetLocationID",targetLocation);

            serverPHPPostConnection(getUpdateTargetLocationURL(),jsonObject.toString(),
                    str_updateTargetLocation_function);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateTargetLocationFinished(String str_result){
        try {
            JSONObject jsonObj = new JSONObject(str_result);
            if((Integer)jsonObj.get("code")==0) {
                mainGameActivity.updateTargetLocationSuccessful(jsonObj.toString());
            }
            else
                mainGameActivity.errorMessage((String)jsonObj.get("message"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateCurrentLocation(int str_UserId, int currentLocation){
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("UserID",str_UserId);
            jsonObject.put("CurrentLocationID",currentLocation);

            serverPHPPostConnection(getUpdateCurrentLocationURL(),jsonObject.toString(),
                    str_updateCurrentLocation_function);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateCurrentLocationFinished(String str_result){
        try {
            JSONObject jsonObj = new JSONObject(str_result);
            if((Integer)jsonObj.get("code")==0) {
                mainGameActivity.updateCurrentLocationSuccessful(jsonObj.toString());
            }
            else
                mainGameActivity.errorMessage((String)jsonObj.get("message"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateCurrentPosition(int str_UserId, double[] currentLocation){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("UserID",str_UserId);
            jsonObject.put("CurrentPositionX",currentLocation[0]);
            jsonObject.put("CurrentPositionY",currentLocation[1]);
            serverPHPPostConnection(getUpdateCurrentPositionURL(),jsonObject.toString(),
                    str_updateCurrentPosition_function);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateCurrentPositionFinished(String str_result){
        try {
            JSONObject jsonObj = new JSONObject(str_result);
            if((Integer)jsonObj.get("code")==0) {
                mainGameActivity.updateCurrentPositionSuccessful(jsonObj.toString());
            }
            else
                mainGameActivity.errorMessage((String)jsonObj.get("message"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //HTTP Request Related Info
    private void serverPHPPostConnection(String str_URL,String str_JSON,String str_Function){
        try{
            HTTPRequest mTask = new HTTPRequest();
            mTask.execute(str_URL,str_JSON,str_Function);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getLoginURL(){
        return String_base_url+"Server/loginM";
    }

    public String getRegistrationURL(){
        return String_base_url+"Server/registerM";
    }

    public String getUpdateUserStepURL(){
        return String_base_url+"Server/updateUserStepM";
    }

    public String getUpdateTargetLocationURL(){
        return String_base_url + "Server/updateTargetLocationIDM";
    }
    public String getUpdateUserCardRelationURL(){
        return String_base_url + "Server/updateUserCardRelationM";
    }

    public String getUpdateCurrentLocationURL(){
        return String_base_url + "Server/updateCurrentLocationIDM";
    }

    public String getUpdateCurrentPositionURL(){
        return String_base_url + "Server/updateCurrentPositionM";
    }

    public String getUserCardURL(){
        return String_base_url + "Server/getUserCardsM";
    }
    //HTTP Request Related End

    public void addActivity(AppCompatActivity activity){
        activityLinkedList.add(activity);

    }


    public void finshAllActivities() {
        updateUserStep(myUser.getUserId(),myUser.getWalkDistance());
        updateCurrentPosition(myUser.getUserId(), myUser.getCurrentLocCoordinate());
        for (AppCompatActivity activity : activityLinkedList) {
            activity.finish();
        }
    }


    public void showToast(Context context, String string){
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

    public boolean isConnectingToInternet(Context _context){
        //Toast.makeText(Home.this, "10%", Toast.LENGTH_SHORT).show();
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //Toast.makeText(Home.this, "20%", Toast.LENGTH_SHORT).show();
        if (connectivity != null)
        {
            //Toast.makeText(Home.this, "30%", Toast.LENGTH_SHORT).show();
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null){
                //Toast.makeText(Home.this, "70%", Toast.LENGTH_SHORT).show();
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
            }
        }
        return false;
    }

    public int getWalkingLine(){
        if ((myUser.getCurrentLocId() == 1 && myUser.getTargetLocId() == 2)
                || (myUser.getCurrentLocId() == 2 && myUser.getTargetLocId() == 1))
            return 1;
        else if ((myUser.getCurrentLocId() == 2 && myUser.getTargetLocId() == 3)
                || (myUser.getCurrentLocId() == 3 && myUser.getTargetLocId() == 2))
            return 2;
        else if ((myUser.getCurrentLocId() == 2 && myUser.getTargetLocId() == 4)
                || (myUser.getCurrentLocId() == 4 && myUser.getTargetLocId() == 2))
            return 3;
        else if ((myUser.getCurrentLocId() == 3 && myUser.getTargetLocId() == 5)
                || (myUser.getCurrentLocId() == 5 && myUser.getTargetLocId() == 3))
            return 4;
        else if ((myUser.getCurrentLocId() == 4 && myUser.getTargetLocId() == 5)
                || (myUser.getCurrentLocId() == 5 && myUser.getTargetLocId() == 4))
            return 5;
        else if ((myUser.getCurrentLocId() == 5 && myUser.getTargetLocId() == 6)
                || (myUser.getCurrentLocId() == 6 && myUser.getTargetLocId() == 5))
            return 6;
        else
            return 0;
    }

    public int isMusicOn(){
        return musicSwitch;
    }

    public void setMusicOn(int music){
        musicSwitch=music;
    }

    public int isSoundOn(){
        return soundSwitch;
    }

    public void setSoundOn(int sound){
        soundSwitch=sound;
    }
}
