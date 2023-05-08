package src.main.java.sayItAssistant;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public class API {

    /*
     * Transcribes the new question with Whisper
     */
    String transcribe() throws IOException, InterruptedException {
        return Whisper.getQuestion();
    }

    /*
     * Answers the new question with ChatGPT
     */
    String ask(String questionTranscription) throws IOException, InterruptedException {
        return ChatGPT.getAnswer(transcribe());
    }
}