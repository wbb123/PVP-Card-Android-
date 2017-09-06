package mangoabliu.finalproject.Model;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import mangoabliu.finalproject.MainGameActivity;

/**
 * Created by herenjie on 2016/11/22.
 */

public class StepService extends Service {
    String TAG = "StepService";
    GameModel gameModel;
    private  boolean isRunning;
    private int stepCount;
    private int sendStepCounter;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        gameModel=GameModel.getInstance();
        int retVal = super.onStartCommand(intent,flags,startId);
        stepCount = 0;
        sendStepCounter = 0;
        isRunning = true;
        Log.d(TAG,String.valueOf(stepCount));
        Log.d(TAG, "onStartCommand" + intent);
        registerForSensorEvents();
        return retVal;

    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        this.stopService(new Intent(this,StepService.class));
        isRunning = false;
        Log.d(TAG,"onDestroy");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void registerForSensorEvents() {
        SensorManager sManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //step counter
        Sensor countSensor = sManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null){
            sManager.registerListener(new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    if (isRunning){

                        stepCount++;
                        sendStepCounter++;
                        Log.i(TAG,""+stepCount);
                        if (gameModel.getUserAccount().getWalkDistance() + stepCount >= gameModel.getTotalSteps()){


                            gameModel.getUserAccount().setCurrentLocId(gameModel.getUserAccount().getTargetLocId());

                            gameModel.getUserAccount().setTargetLocId(0);

                            gameModel.getUserAccount().setCurrentLocCoordinate(new double[]{
                                    gameModel.getPlanets().get(gameModel.getUserAccount().getCurrentLocId()-1).getPlanetX(),
                                    gameModel.getPlanets().get(gameModel.getUserAccount().getCurrentLocId()-1).getPlanetY()
                            });

                            gameModel.getUserAccount().setWalkDistance(0);

                            gameModel.updateUserStep(gameModel.getUserAccount().getUserId(),0);

                            gameModel.updateTargetLocation(gameModel.getUserAccount().getUserId(),0);
                            gameModel.updateCurrentLocation(gameModel.getUserAccount().getUserId(),
                                    gameModel.getUserAccount().getCurrentLocId());
                            gameModel.updateCurrentPosition(gameModel.getUserAccount().getUserId(),
                                    new double[]{gameModel.getUserAccount().getCurrentLocCoordinate()[0],
                                            gameModel.getUserAccount().getCurrentLocCoordinate()[1]});

                            gameModel.mainDisplayDistance();

                            gameModel.mainUpdateUFO((float)gameModel.getUserAccount().getCurrentLocCoordinate()[0],
                                    (float) gameModel.getUserAccount().getCurrentLocCoordinate()[1]);
                            stepCount = 0;
                            sendStepCounter = 0;
                            gameModel.getMainGameActivity().stopService(new Intent(gameModel.getMainGameActivity(),StepService.class));
                            isRunning = false;
                        }

                        if (stepCount == 1) {

                            gameModel.updateMainGameStep(stepCount);
                            stepCount = 0;

                        }
                        if (sendStepCounter == 10){
                            gameModel.updateUserStep(gameModel.getUserAccount().getUserId(),
                                    gameModel.getUserAccount().getWalkDistance());
                            sendStepCounter = 0;
                        }
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {

                }
            }, countSensor, SensorManager.SENSOR_DELAY_UI);
        }
        else{
            Log.d(TAG,"Count sensor not available!");
        }
    }

}
