
import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class MyHandler implements HttpHandler{

    static ArrayList<String> data;
    public CommandHandler commandHandler = new CommandHandler();

    public MyHandler(ArrayList<String> data){
        this.data = data;
    }

    public static String getDataLength() {
        return Integer.toString(data.size());
    }

    public static void clearData() {
        data.clear();
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();
        try{
            if (method.equals("GET")){
                response = this.handleGet(httpExchange);
            } else if (method.equals("POST")) {
                response = this.handlePost(httpExchange);
            } else if (method.equals("PUT")){
                response = this.handlePut(httpExchange);
            } else if (method.equals("DELETE")){
                clearData();
            } else {
                throw new Exception ("Not Valid Request Method");
            }
        } catch (Exception e) {
            System.out.println("An erroneous request");
            response = e.toString();
            e.printStackTrace();
        }

        //sending back response to the client
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.close();
    }

    public String handleGet(HttpExchange httpExchange) throws IOException {
        System.out.println("I am in GET");
        String response = "Invalid GET Request";
        String email = "";
        String password = "";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        String emailAndPassword = "";
        if (query != null){
            // System.out.println("Query is not null!");
            emailAndPassword = query.substring(query.indexOf("=")+1);
            email = emailAndPassword.substring(0, emailAndPassword.indexOf("~"));
            password = emailAndPassword.substring(emailAndPassword.indexOf("~") + 1);

            // Set the commandHandlers email
            commandHandler.setEmail(email);
            response = commandHandler.getAccount(email, emailAndPassword);
        }
        System.out.println(response);
        return response;
        // return response
    }

    private String handlePost(HttpExchange httpExchange) throws IOException {
        String response = "";
        System.out.println("I am in POST");
        InputStream inStream = httpExchange.getRequestBody();

        File audioFile = new File("audioFile.wav");
        try {
            byte[] buffer = new byte[2 * 1024];
            int readBytes;
            FileOutputStream outputStream = new FileOutputStream(audioFile);
            try (InputStream in = inStream){
                    while ((readBytes = in.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, readBytes);
                   }
               }
            outputStream.close();
            
            String transcription = Whisper.getQuestion();
            System.out.println(transcription);
            response = commandHandler.HandlePrompt(transcription);
        }catch(Exception exception)
        {
            System.out.println("Couldn't process the audio");
        }
        System.out.println("Success: " + response);      

        return response;
        /* 
        Scanner scanner = new Scanner(inStream);

        // EVENTUALLY: PARSE THE AUDIO FILE EMAIL IS ALREADY SET IN COMMAND HANDLER
        // NO NEED TO SEND EMAIL TO COMMANDHANDLER
        String postData = scanner.nextLine();
        String email = postData.substring(
            0,
            postData.indexOf("~")
        ), transcriptionFromWhisper = postData.substring(postData.indexOf("~") + 1);
        

        response = commandHandler.HandlePrompt(transcriptionFromWhisper);
     
        scanner.close();
        return response;
        */
    }

    private String handlePut(HttpExchange httpExchange) throws IOException {
        System.out.println("I am in PUT");
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String putData = scanner.nextLine();
        String email = putData.substring(
            0,
            putData.indexOf("~")
        ), password = putData.substring(putData.indexOf("~") + 1);

        commandHandler.setEmail(email);
        commandHandler.createAccount(email, password);
        String response = email + " " + password;
        data.add(response);
        scanner.close();
        return response;
    }
}
