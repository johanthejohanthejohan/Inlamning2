/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inlamning2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Optional;
import java.util.Scanner;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 *
 * @author johan
 */
public class QuizClient extends Application{
    
    //Layout base
    private int windowHeight = 300;
    private int windowWidth = 500;
    private BorderPane root = new BorderPane();
    private Scene scene = new Scene(root, windowWidth, windowHeight);
    private HBox hBox = new HBox(5);
    
    private TextField input;
    private Button send;
    private TextArea output;

    //Volatile so both threads gets the same value straight from memory instead
    //of cached values.
    private volatile String inputString = "";
    private volatile boolean run = true;

    
    
    //Network variables initialization
    private String adress = "localhost";
    private int portNumber = 8080;
    private BufferedReader reader;
    private PrintWriter out;
    private String nameString;
    private Socket socket;
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        launch(args);
    }
    
    @Override
    public void init(){
        send = new Button();
        send.setText("skicka svar");
        input = new TextField();
        output = new TextArea();
        hBox.getChildren().addAll(input, send);
        HBox.setMargin(input, new Insets(5,0,0,0));
        HBox.setMargin(send, new Insets(5,0,0,0));
        root.setPadding(new Insets(5,5,5,5));
        root.setCenter(output);
        root.setBottom(hBox);
    }

    //Method for sending answer to server
    public void sendAnswer(){
        inputString = input.getText();
        out.println(inputString);
        input.setText("");
    }
    
    @Override
    public void start(Stage primaryStage){

        //Input dialog for entering client name
        TextInputDialog name = new TextInputDialog();
        name.setContentText("Skriv in namn: ");
        name.setHeaderText("Namn");
        name.showAndWait();
        nameString = name.getResult();
        System.out.println(nameString);

        //Java fx
        primaryStage.setTitle("Inlamning 2");
        primaryStage.setScene(scene);
        primaryStage.show();

        //Listener for send  button
        send.setOnAction(e->{
            sendAnswer();
        });

        //Listener for enter button
        scene.setOnKeyPressed(event -> {
            if(event.getCode()==KeyCode.ENTER){
                sendAnswer();
            }
        });


        //Stops network thread when fx window is closed.
        primaryStage.setOnCloseRequest(e->{
            
           run = false;

           //Creates exception when closing the client but couldn't figure out how to close the reader properly.
            try{
                socket.close();
            }catch(IOException ex){
                System.out.println("error = " + ex);
            }
                
        });
        
     
        Runnable network = new Network();
        Thread netWorkThread = new Thread(network);
        netWorkThread.start();
        
    }
    
    //Thread for listening on socket
    public class Network implements Runnable  {
        
        @Override
        public void run(){

            try {
                //Create socket that connects to server
                socket = new Socket(adress, portNumber);
                //Open input stream on socket
                InputStream stream  = socket.getInputStream();
                //Open reader on input stream
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                //Open writer on outputstream
                out = new PrintWriter(socket.getOutputStream(), true);

                out.println(nameString);

                String message;
                while(run){
                    message = reader.readLine();
                    output.appendText(message+"\n");
                }

            }catch (IOException e) { 

            }
        }
    }   
}
