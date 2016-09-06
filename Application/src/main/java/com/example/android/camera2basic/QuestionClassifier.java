package com.example.android.camera2basic;

import android.os.AsyncTask;
import android.os.SystemClock;

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
    public boolean noQuestion = false;

    @Override
    protected String doInBackground(String... paths) {
        System.out.println("Classifying question...");
        String question = paths[0];
        if(question == "noquestion"){
            noQuestion = true;
        }
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
            VisualRecognition.countDownLatch.await();
            AgeGender.countDownLatch.await();
            RecognizeText.countDownLatch.await();
            System.out.println("Finished waiting");
            if(noQuestion==true){
                qclass="default";
            }
            switch (qclass){
                case "1":{
                    System.out.println("It classified as Identification");
                    String answer;
                    if(VisualRecognition.classes!=null && !VisualRecognition.classes.isEmpty()){
                        answer = "Found object "+VisualRecognition.classes;
                    }else{
                        answer = "Sorry, could not recognize any object";
                    }
                    new TextToSpeechTask().execute(answer, DirectoryPath);
                }
                break;
                case "2":{
                    String answer;
                    if(AgeGender.minAge==0 && AgeGender.maxAge==0 && (AgeGender.gender=="" || AgeGender.gender==null)){
                        answer="Sorry, could not recognize anyone";
                    }else{
                        answer = "The person is a "+AgeGender.gender+" with age between "+AgeGender.minAge+" and "+AgeGender.maxAge;
                    }
                    System.out.println("It classified as Description");
                    new TextToSpeechTask().execute(answer,DirectoryPath);
                }
                break;
                case "3":{
                    String answer;
                    if(RecognizeText.text!=null && !RecognizeText.text.isEmpty()){
                        answer = "Found text "+RecognizeText.text;
                    }else{
                        answer = "Sorry, could not recognize any text";
                    }

                    System.out.println("It classified as Text Recognition");
                    new TextToSpeechTask().execute(answer,DirectoryPath);
                }
                break;
                case "4":{
                    System.out.println("Sorry Couldn't classify");
                    new TextToSpeechTask().execute("Sorry ! we couldn't figure out what it is.",DirectoryPath);
                }
                break;
                default:{
                    new TextToSpeechTask().execute("Sorry ! Couldn't get the question.",DirectoryPath);
                }
            }
            } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}