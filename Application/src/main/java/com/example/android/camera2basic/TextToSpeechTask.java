package com.example.android.camera2basic;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.AudioFormat;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;
import com.ibm.watson.developer_cloud.text_to_speech.v1.util.WaveUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Keshav on 9/2/2016.
 */
public class TextToSpeechTask extends AsyncTask<String,Void,String> {
    @Override
    protected String doInBackground(String... strings) {
        TextToSpeech service = new TextToSpeech();
        service.setUsernameAndPassword("31eecc79-e78f-467d-9aa0-0ece59b780b4", "e1dS1NnhaFAl");

        try {
            String text = "Hello world.";
            InputStream in = service.synthesize (text, Voice.EN_ALLISON, AudioFormat.FLAC).execute();

            OutputStream out = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath()+"/answer.flac");
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            out.close();
            in.close();
            System.out.println("Text-to-Speech done");
        } catch (Exception e) {
            e.printStackTrace();
        }
        startPlaying();
        return "NothingToBeReturned";
    }

    public void startPlaying() {
        String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath()+"/answer.flac";
        System.out.println("Playing from file : "+mFileName);
        MediaPlayer mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e("TexToSpeechTask", "prepare() failed");
        }
    }
}

