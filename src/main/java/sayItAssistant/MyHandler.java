import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;

// class Pair {
//     String first;
//     String second;

//     public Pair(String first, String second) {
//         this.first = first;
//         this.second = second;
//     }
//     public String getFirst() {
//         return first;
//     }
//     public void setFirst(String first) {
//         this.first = first;
//     }
//     public String getSecond() {
//         return second;
//     }
//     public void setSecond(String second) {
//         this.second = second;
//     }
// }

public class MyHandler implements HttpHandler{

    static ArrayList<String> data;

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
                response = this.getDataLength();
            } else if (method.equals("DELETE")){
                this.clearData();
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
        String response = "Invalid GET Request";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        if (query != null){
            String message = query.substring(query.indexOf("=")+1);
            int index = Integer.parseInt(message);
            try{
                String content = data.get(index);
                response = content;
                System.out.println(content);
            } catch (Exception e){
                response = "Index out of bound";
            }
        }
        return response;
    }

    private void handlePost(HttpExchange httpExchange) throws IOException {
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String postData = scanner.nextLine();
        System.out.println(postData);

        // Store data in the Arraylist
        data.add(postData);
     
        scanner.close();
    }
}
