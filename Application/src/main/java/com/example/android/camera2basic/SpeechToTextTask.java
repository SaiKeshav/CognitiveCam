package com.example.android.camera2basic;

import android.os.AsyncTask;
import android.os.Environment;

import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.http.ServiceCall;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;

import java.io.File;

/**
 * Created by Keshav on 9/1/2016.
 */
public class SpeechToTextTask extends AsyncTask<String,Void,String> {

    @Override
    protected String doInBackground(String... paths) {
        System.out.println("In speech to text");
        SpeechToText service = new SpeechToText();
        service.setUsernameAndPassword("1739623b-bdf8-444d-a1ff-45577fdaf99a", "ixSozkf7TwTF");

        String fileName = Environment.getExternalStorageDirectory().getAbsolutePath()+"/question.wav";
        System.out.println("File name : "+fileName);
        SpeechResults results = service.recognize(new File(fileName)).execute();
        System.out.println(results);
        return "NothingToBeDone";
    }
}
