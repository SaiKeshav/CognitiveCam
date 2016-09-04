package com.example.android.camera2basic;

import android.os.AsyncTask;

import com.ibm.watson.developer_cloud.http.ServiceCall;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.NaturalLanguageClassifier;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classification;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Keshav on 8/30/2016.
 * Classifier training done using curl, mentioned in TrainClassifierPoint.txt
 * Only using classifier is done over here (TODO implement training also)
 * Website: http://www.ibm.com/watson/developercloud/natural-language-classifier/api/v1/?java
 */
public class QuestionClassifier extends AsyncTask<String,Void,String> {

    public String DirectoryPath = null;

    @Override
    protected String doInBackground(String... paths) {
        System.out.println("Classifying question...");
        String question = paths[0];
        NaturalLanguageClassifier service = new NaturalLanguageClassifier();
        service.setUsernameAndPassword("50484991-1d28-49a0-aaf6-0caae44b8608", "7luqfgcIasoz");

        ServiceCall<Classification> classificationServiceCall = service.classify("33fffex86-nlc-3331", question);
        Classification classification = classificationServiceCall.execute();

        DirectoryPath = paths[1];

        return classification.toString();
    }

    protected void onPostExecute(String result) {
        try {
            JSONObject obj = new JSONObject(result);
            String qclass= obj.getString("top_class");
            System.out.println("Question Class: "+qclass);
           // new VisualRecognition(Camera2BasicFragment.currContext).execute(Camera2BasicFragment.mFile.getPath(),DirectoryPath);
            switch (qclass){
                case "1":{
                    System.out.println("It classified as Identification");
                    new TextToSpeechTask().execute(VisualRecognition.classes,DirectoryPath);
                }
                break;
                case "2":{
                    System.out.println("It classified as Description");
                    String des="Minimum Age is " + AgeGender.minAge + " and gender is " +AgeGender.gender;
                    new TextToSpeechTask().execute(des,DirectoryPath);
                }
                break;
                case "3":{
                    System.out.println("It classified as Text Recognition");
                    new TextToSpeechTask().execute(RecognizeText.text,DirectoryPath);
                }
                break;
                case "4":{
                    System.out.println("Sorry Couldn't classify");
                    new TextToSpeechTask().execute("Sorry ! we couldn't figure out what it is.",DirectoryPath);
                }

            }


            } catch (JSONException e) {
            e.printStackTrace();
        }

    }
//    public static void main(String args[]) {
//        System.out.println("!! In my main !!");
//
//        String question = "What is this ?";
//        NaturalLanguageClassifier service = new NaturalLanguageClassifier();
//        service.setUsernameAndPassword("50484991-1d28-49a0-aaf6-0caae44b8608", "7luqfgcIasoz");
//
//        Classification classification = (Classification) service.classify("33fffex86-nlc-3331", question);
//        System.out.println(classification);
//
//    }
}