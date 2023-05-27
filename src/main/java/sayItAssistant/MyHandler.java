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
                this.handlePost(httpExchange);
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
        // InputStream inStream = httpExchange.getRequestBody();
        // Scanner scanner = new Scanner(inStream);
        // String putData = scanner.nextLine();
        // String email = putData.substring(
        //     0,
        //     putData.indexOf("~")
        // ), password = putData.substring(putData.indexOf("~") + 1);

        // scanner.close();

        // return email;
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
            response = commandHandler.getAccount(email, emailAndPassword);
        }
        System.out.println(response);
        return response;
        // return response
    }

    private void handlePost(HttpExchange httpExchange) throws IOException {
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String postData = scanner.nextLine();
        String temp = "";
        while (scanner.hasNextLine()){
            temp = scanner.nextLine();
            postData = postData + temp;
        }
        System.out.println("Why am i in post?");
        System.out.println(postData);

        // Store data in the Arraylist
        data.add(postData);
     
        scanner.close();
    }

    private String handlePut(HttpExchange httpExchange) throws IOException {
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String putData = scanner.nextLine();
        String email = putData.substring(
            0,
            putData.indexOf("~")
        ), password = putData.substring(putData.indexOf("~") + 1);

        commandHandler.createAccount(email, password);
        String response = email + " " + password;
        data.add(response);
        scanner.close();
        return response;
    }
}
