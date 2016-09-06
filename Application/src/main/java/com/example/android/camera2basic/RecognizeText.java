package com.example.android.camera2basic;

/**
 * Created by Shreyans Shrimal on 9/4/2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.ibm.watson.developer_cloud.visual_recognition.v3.model.RecognizedText;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualRecognitionOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import id.zelory.compressor.Compressor;

/**
 * Copyright 2015 IBM Corp. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

public class RecognizeText extends AsyncTask<Object, Void, String> {

    private Context mContext;

    public RecognizeText(Context c) {
        mContext = c;
    }

    public String DirectoryPath = null;
    public static String text;
    public static CountDownLatch countDownLatch;
    @Override
    protected String doInBackground(Object... paths) {
        System.out.println("Performing Visual Recognition...");

        // params comes from the execute() call: params[0] is the url.
        com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition service = new com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition(com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition.VERSION_DATE_2016_05_20);
        service.setApiKey("26a259b7f5dc0f5c8c1cc933d8722b0e66aed5df");

        File actualImageFile = new File((String)paths[0]);
        // Library link : https://github.com/zetbaitsu/Compressor
        Bitmap compressedBitmap = Compressor.getDefault(mContext).compressToBitmap(actualImageFile);
        File compressedImage = bitmapToFile(compressedBitmap);
        // TODO Image size may be still greater than 1 MB !
        VisualRecognitionOptions options= new VisualRecognitionOptions.Builder().images(compressedImage).build();

        RecognizedText result = service.recognizeText(options).execute();

        DirectoryPath = (String)paths[1];

        System.out.println("OnPostExecute...");
        System.out.println(result);
        try {

            JSONObject obj = new JSONObject(result.toString());
            JSONObject resultarray1= obj.getJSONArray("images").getJSONObject(0);
            text=resultarray1.getString("text");
            System.out.println("Recognize Text : "+text);
            //new TextToSpeechTask().execute(classes,DirectoryPath);
        } catch (JSONException e) {
            System.out.println("Nothing Detected In Text ");
        }

        countDownLatch.countDown();
        System.out.println("Latch counted down in Recongize Text");

        return result.toString();
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {

// textView.setText(result);
    }

    public File bitmapToFile(Bitmap bmp) {

        //create a file to write bitmap data
        File f = new File(mContext.getCacheDir(), "CompressedFile.jpeg");
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Convert bitmap to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

        //write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    }
//    public static void main(String[] args) {
//        VisualRecognition service = new VisualRecognition(VisualRecognition.VERSION_DATE_2016_05_20);
//        service.setApiKey("26a259b7f5dc0f5c8c1cc933d8722b0e66aed5df");
//
//        System.out.println("Classify an image");
//        ClassifyImagesOptions options = new ClassifyImagesOptions.Builder()
//                .images(new File("res/drawable/car.png"))
//                .build();
//        VisualClassification result = service.classify(options).execute();
//        System.out.println("Result is : "+result);
//    }

}
