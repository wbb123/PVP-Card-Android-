package mangoabliu.finalproject.Model;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static android.content.ContentValues.TAG;

/**
 * Created by SHI ZHONGQI on 2016/11/16.
 */

//Reference http://blog.csdn.net/liuhe688/article/details/6532519
public class GameHTTPRequest extends AsyncTask<String, Integer, String> {
    BattleModel battleModel=BattleModel.getInstance();
    String str_function="";

    //onPreExecute方法用于在执行后台任务前做一些UI操作
    @Override
    protected void onPreExecute() {
        Log.i(TAG, "onPreExecute() called");

    }
    //doInBackground方法内部执行后台任务,不可在此方法内修改UI
    //INPUT from Model will be : URL, PARAMEMTER, FUNCTION
    @Override
    protected String doInBackground(String... params) {
        Log.i(TAG, "doInBackground(Params... params) called");
        try {
            System.out.println("Sending URL:"+params[0]);
            str_function=params[2];
            URL url = new URL(params[0]);
            String response="";
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            Log.i(TAG, params[1]);
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(params[1]);

            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";
            }
            System.out.println(response);
            return response;

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    //onProgressUpdate方法用于更新进度信息
    @Override
    protected void onProgressUpdate(Integer... progresses) {
        Log.i(TAG, "onProgressUpdate(Progress... progresses) called");

    }

    //onPostExecute方法用于在执行完后台任务后更新UI,显示结果
    @Override
    protected void onPostExecute(String result) {
        Log.i(TAG, "onPostExecute(Result result) called");
        Log.i(TAG,"Result:"+result);
        if(str_function.equals(BattleModel.str_applyForFightM_function))
            battleModel.applyForFightSuccessful(result);
        if(str_function.equals(BattleModel.str_isRoomReadyM_function))
            battleModel.getIsRoomReadySuccessful(result);
        if(str_function.equals(BattleModel.str_setCardsM_function))
            battleModel.setCardsSuccessful(result);
        if(str_function.equals(BattleModel.str_isFightReadyM_function))
            battleModel.isFightReadySuccessful(result);
        if(str_function.equals(BattleModel.str_myTurnM_function))
            battleModel.isMyTurnSuccessful(result);
        if(str_function.equals(BattleModel.str_playCardM_function))
            battleModel.playACardSuccessful(result);

    }

    //onCancelled方法用于在取消执行中的任务时更改UI
    @Override
    protected void onCancelled() {
        Log.i(TAG, "onCancelled() called");

    }
}

