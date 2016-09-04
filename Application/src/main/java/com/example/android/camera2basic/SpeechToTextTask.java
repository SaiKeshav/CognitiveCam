package com.example.android.camera2basic;

import android.os.AsyncTask;
import android.os.Environment;

import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by Keshav on 9/1/2016.
 */
public class SpeechToTextTask extends AsyncTask<Object,Void,String> {

    public String DirectoryPath = null;

    @Override
    protected String doInBackground(Object... opaths) {
        String path = (String)opaths[0];
        System.out.println("Performing Speech-to-Text...");
        SpeechToText service = new SpeechToText();
        service.setUsernameAndPassword("1739623b-bdf8-444d-a1ff-45577fdaf99a", "ixSozkf7TwTF");

        DirectoryPath = path;
        String fileName = path+"/question.wav";
        SpeechResults results = service.recognize(new File(fileName)).execute();
        return results.toString();
    }

    protected void onPostExecute(String result) {
        try {
            JSONObject obj = new JSONObject(result);
            JSONObject resultarray1= obj.getJSONArray("results").getJSONObject(0);
            String question=resultarray1.getJSONArray("alternatives").getJSONObject(0).getString("transcript");
            // TODO question can be null !
            new QuestionClassifier().execute(question,DirectoryPath);
            System.out.println("Question : "+question);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
