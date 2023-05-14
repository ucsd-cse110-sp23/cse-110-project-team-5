
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import org.json.JSONException;

/**
 * Mock class to get static results for testing
 */
public class MockWhisper {
    private static final String API_ENDPOINT = "https://api.openai.com/v1/audio/transcriptions";
    private static final String TOKEN = "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";
    private static final String MODEL = "whisper-1";


    // Helper method to write a parameter to the output stream in multipart form data format
    private static void writeParameterToOutputStream(
    OutputStream outputStream,
    String parameterName,
    String parameterValue,
    String boundary
    ) throws IOException {
        outputStream.write(("--" + boundary + "\r\n").getBytes());
        outputStream.write(
            (
            "Content-Disposition: form-data; name=\"" + parameterName + "\"\r\n\r\n"
            ).getBytes()
        );
        outputStream.write((parameterValue + "\r\n").getBytes());
    }

    // Helper method to write a file to the output stream in multipart form data format 
    private static void writeFileToOutputStream(
    OutputStream outputStream,
    File file,
    String boundary
    ) throws IOException {
        outputStream.write(("--" + boundary + "\r\n"). getBytes()); 
        outputStream.write(
            (
                "Content-Disposition: form-data; name=\"file\"; filename=\"" +
                file.getName() + 
                "\"\r\n"
            ).getBytes()
        );
        outputStream.write(("Content-Type: audio/mpeg\r\n\r\n").getBytes());

        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buffer = new byte [1024];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        fileInputStream.close();
    }

    private static String handleSuccessResponse(HttpURLConnection connection)
        throws IOException, JSONException {
        BufferedReader in = new BufferedReader(
        new InputStreamReader(connection.getInputStream())
        );
        String inputLine;
        StringBuilder response = new StringBuilder();
        while((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONObject responseJson = new JSONObject(response.toString());

        String generatedText = responseJson.getString("text");

        return generatedText;
    }

    private static void handleErrorResponse(HttpURLConnection connection) 
    throws IOException, JSONException {
        BufferedReader errorReader = new BufferedReader(
            new InputStreamReader(connection.getErrorStream())
        );
        String errorLine;
        StringBuilder errorResponse = new StringBuilder();
        while ((errorLine = errorReader.readLine()) != null) {
            errorResponse.append(errorLine);
        }

        errorReader.close();
        String errorResult = errorResponse.toString();
        System.out.println("Error Result: " + errorResult);
    }

    public static String getQuestion() throws IOException {
        String fileName = Record.getRecordingFileName();
        File file = new File(fileName);
        URL url = new URL(API_ENDPOINT);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        String boundary = "Boundary-" + System.currentTimeMillis();
        connection.setRequestProperty(
        "Content-Type",
        "multipart/form-data; boundary=" + boundary
        );
        //connection.setRequestProperty("Authorization", "Bearer " + TOKEN);
        connection.setRequestProperty("Authorization", "Bearer " + "mock_api_key"); //mock api key
        
        OutputStream outputStream = connection.getOutputStream();
        
        writeParameterToOutputStream(outputStream, "model", MODEL, boundary);
        
        writeFileToOutputStream(outputStream, file, boundary);

        outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());

        outputStream.flush();
        outputStream.close();

        int responseCode = connection.getResponseCode();

        String questionTranscription = "Is this a mock question?";

        if (responseCode ==  HttpURLConnection.HTTP_OK) {
            questionTranscription = handleSuccessResponse(connection);
        } else {
            handleErrorResponse(connection);
        }

        connection.disconnect();

        return questionTranscription;
    }
}