package com.example.android.camera2basic;

import android.os.AsyncTask;

import com.ibm.watson.developer_cloud.http.ServiceCall;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.NaturalLanguageClassifier;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classification;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classifier;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classifiers;

/**
 * Created by Keshav on 8/30/2016.
 * Classifier training done using curl, mentioned in TrainClassifierPoint.txt
 * Only using classifier is done over here (TODO implement training also)
 * Website: http://www.ibm.com/watson/developercloud/natural-language-classifier/api/v1/?java
 */
public class QuestionClassifier extends AsyncTask<String,Void,String> {

    @Override
    protected String doInBackground(String... paths) {
        System.out.println("!!!!!!!!!!!!!!!!! Doing classification !!!!!!!!!!!");
        String question = paths[0];
        NaturalLanguageClassifier service = new NaturalLanguageClassifier();
        service.setUsernameAndPassword("50484991-1d28-49a0-aaf6-0caae44b8608", "7luqfgcIasoz");

//        ServiceCall<Classifiers> classifiers = service.getClassifiers();
        ServiceCall<Classification> classificationServiceCall = service.classify("33fffex86-nlc-3331", question);
        Classification classification = classificationServiceCall.execute();
        System.out.println(classification);
        return "NothingToReturn";
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