package inlamning2;

import java.util.ArrayList;

/**
 * Created by johan on 2017-04-11.
 */
public class Question {
    public ArrayList<String> question = new ArrayList<>();
    public ArrayList<String> answer = new ArrayList<>();
    public volatile static int counter = 0;

    //Method for incrementing question counter
    public void plusCount(){
        if(counter==question.size()-1){
            counter=0;
        } else {
            counter++;
        }
    }

    //Constructor saves a couple of questions and answers
    public Question(){
        question.add("Världens största djur?");
        question.add("Vad heter apelsin på engelska?");
        question.add("Vilket är världens snabbaste djur?");
        question.add("Cucumber på svenska?");
        question.add("Vad heter filmen om dinosaurier producerades 1993? ");
        answer.add("blåval");
        answer.add("orange");
        answer.add("pilgrimsfalken");
        answer.add("gurka");
        answer.add("jurassic park");
    }

}
