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
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author johan
 */
public class QuizServer {
    //Static arraylist of that will contain all clients writers making it
    //possible to broadcast messages.
    public static  ArrayList<PrintStream> writers = new ArrayList<PrintStream>();

    
    
    public static void main(String[] args) throws IOException{
        
        int portNumber = 8080;

        //Declare objects
        QuizServerThread serverThread;
        Thread t1;
        Question question = new Question();
        
        //Open port for server
        ServerSocket serverSocket = new ServerSocket(portNumber);

        //Thread that prints questions
        Runnable questionThread = () -> {
            while (true) {
                question.plusCount();
                for (PrintStream writer : writers) {
                    writer.println("fråga: " + question.question.get(question.counter));


                }
                try {
                    Thread.sleep(10000);
                }catch(InterruptedException ex){
                    System.out.println("ex = " + ex);
                }


            }

        };
        new Thread(questionThread).start();

        
        //Waits for clients to connect and then create new thread.
        while(true){
            Socket connection = serverSocket.accept();
            serverThread = new QuizServerThread(connection);
            t1 = new Thread(serverThread);
            t1.start();
        }




           
    }
        
 }

