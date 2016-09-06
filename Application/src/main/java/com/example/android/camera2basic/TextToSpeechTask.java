package com.example.android.camera2basic;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;

import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.AudioFormat;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static android.os.SystemClock.sleep;

/**
 * Created by Keshav on 9/2/2016.
 */
public class TextToSpeechTask extends AsyncTask<String,Void,String> {
    public  String DirectoryPath = null;

    @Override
    protected String doInBackground(String... strings) {
        DirectoryPath = strings[1];
        System.out.println("Performing Text-to-Speech...");

        TextToSpeech service = new TextToSpeech();
        service.setUsernameAndPassword("31eecc79-e78f-467d-9aa0-0ece59b780b4", "e1dS1NnhaFAl");

        try {
            String text = strings[0];
            InputStream in = service.synthesize (text, Voice.EN_ALLISON, AudioFormat.FLAC).execute();

            OutputStream out = new FileOutputStream(DirectoryPath+"/answer.flac");
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        startPlaying();
        return "NothingToBeReturned";
    }

    public void startPlaying() {
        System.out.println("Playing the answer...");
        String mFileName = DirectoryPath+"/answer.flac";
        MediaPlayer mPlayer = CameraActivity.mPlayer;
        if(mPlayer != null){
            mPlayer.release();
            mPlayer = new MediaPlayer();
        }
        try {
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    //Release the player on completion
                    mp.reset();
                    mp.release();
                    mp = null;
                    sleep(1000);
                    CameraActivity.startPlaying("afteranswer");
                }
            });
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e("TexToSpeechTask", "prepare() failed");
        }
    }

    protected void onPostExecute(String result){
        Camera2BasicFragment.answerCompleted = true;
        long timeElapsed = System.currentTimeMillis() - Camera2BasicFragment.startTime;
        System.out.println("Time elapsed (in sec) : "+(timeElapsed/1000));
    }
}

