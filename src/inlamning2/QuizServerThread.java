/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inlamning2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author johan
 */
public class QuizServerThread implements Runnable{
    private PrintStream out;
    private BufferedReader in;
    
    private String name;
    private  int points;
    private Question question;
    
    //Constructor takes socket connection as argument 
    public QuizServerThread(Socket connection) throws IOException {
	
        
        //Stream for sending to client
        out = new PrintStream(connection.getOutputStream());
        
        //Stream for reading from client
        in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        question = new Question();

        QuizServer.writers.add(out);
    }

    @Override
    public void run() {

        String inputLine;
        
        try{

            //First line being read from popup in java fx gets stored in name variable for each client
            name = in.readLine();

            while ((inputLine = in.readLine()) != null) {
                System.out.println("inputLine: " + inputLine);
                System.out.println("answer: " + question.answer.get(Question.counter));
                System.out.println(question.answer.get(Question.counter).equals(inputLine));

                for (PrintStream writer : QuizServer.writers) {
                    writer.println(name + ": " + inputLine );
                    writer.flush();
                     }


                if(question.answer.get(Question.counter).equals(inputLine.toLowerCase())) {
                    points++;
                    System.out.println("Loop up");
                    for (PrintStream writer : QuizServer.writers) {
                        writer.println("Rätt! " +  name +" har " + points + " poäng.");
                        System.out.println(inputLine);
                    }
                } else{
                    for (PrintStream writer : QuizServer.writers) {
                        writer.println("Fel svar "+name+", försök igen!");
                    }
                }

            }



        } catch (IOException e){
            System.out.println("e = " + e);
        }
    } 
}