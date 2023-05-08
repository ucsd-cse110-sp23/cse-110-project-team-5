import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import javax.sound.sampled.*;

import org.json.JSONArray;
import org.json.JSONObject;

public class recordAPI {

    private String recordingFileName = "new_question.wav";

    /* the number of samples of audio per second.
     * 44100 represents the typical sample rate for CD-quality audio.
     * the number of bits in each sample of a sound that has been digitized.
     * 2 audio channels for stereo.
     * the data is signed.
     * audio data is stored in big endian order. */
    float sampleRate = 44100;
    int sampleSizeInBits = 16;
    int channels = 2;
    boolean signed = true;
    boolean bigEndian = false;
    private AudioFormat defaultAudioFormat = new AudioFormat(
        sampleRate,
        sampleSizeInBits,
        channels,
        signed,
        bigEndian
    );

    /*
     * Starts recording the new question
     */
    void startRecording() {
        Thread t = new Thread(
            () -> {
            try {
                // the format of the TargetDataLine
                DataLine.Info dataLineInfo = new DataLine.Info(
                TargetDataLine.class,
                audioFormat
                );
                // the TargetDataLine used to capture audio data from the microphone
                targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
                targetDataLine.open(audioFormat);
                targetDataLine.start();
                recordingLabel.setVisible(true);
    
                // the AudioInputStream that will be used to write the audio data to a file
                AudioInputStream audioInputStream = new AudioInputStream(targetDataLine);
    
                // the file that will contain the audio data
                File audioFile = new File("recording.wav");
                AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, audioFile);
                recordingLabel.setVisible(false);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            }
        );
        t.start();
    }

    /*
     * Stops the recording
     * Saves to a file named the recordingFileName
     */
    void stopRecording() {
        targetDataLine.stop();
        targetDataLine.close();
    }

    /*
     * Transcribes the new question with Whisper
     */
    String transcribe() throws IOException, InterruptedException {

        String API_ENDPOINT = "https://api.openai.com/v1/completions";
        String API_KEY = "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";
        String MODEL = "text-davinci-003";

        // Create a request body which you will pass into request object
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", MODEL);
        requestBody.put("prompt", prompt);
        //requestBody.put("max_tokens", maxTokens);
        requestBody.put("temperature", 1.0);

        // Create the HTTP client
        HttpClient client = HttpClient.newHttpClient();

        // Create the request object
        HttpRequest request = HttpRequest
        .newBuilder()
        .uri(URI.create(API_ENDPOINT))
        .header("Content-Type", "application/json")
        .header("Authorization", String.format("Bearer %s", API_KEY))
        .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
        .build();

        // Send the request and receive the response
        HttpResponse<String> response = client.send(
            request, 
            HttpResponse.BodyHandlers.ofString()
        );

        // Process the response
        String responseBody = response.body();

        JSONObject responseJson = new JSONObject(responseBody);

        JSONArray choices = responseJson.getJSONArray("choices");
        String questionTranscription = choices.getJSONObject(0).getString("text");

        return questionTranscription;
    }

    /*
     * Answers the new question with ChatGPT
     */
    String ask(String questionTranscription) throws IOException, InterruptedException {

        String API_ENDPOINT = "https://api.openai.com/v1/completions";
        String API_KEY = "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";
        String MODEL = "text-davinci-003";

        //String questionTranscription is the prompt
        String prompt = questionTranscription;
        //int maxTokens = 100; No max tokens specified thus far

        // Create a request body which you will pass into request object
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", MODEL);
        requestBody.put("prompt", prompt);
        //requestBody.put("max_tokens", maxTokens);
        requestBody.put("temperature", 1.0);

        // Create the HTTP client
        HttpClient client = HttpClient.newHttpClient();

        // Create the request object
        HttpRequest request = HttpRequest
        .newBuilder()
        .uri(URI.create(API_ENDPOINT))
        .header("Content-Type", "application/json")
        .header("Authorization", String.format("Bearer %s", API_KEY))
        .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
        .build();

        // Send the request and receive the response
        HttpResponse<String> response = client.send(
            request, 
            HttpResponse.BodyHandlers.ofString()
        );

        // Process the response
        String responseBody = response.body();

        JSONObject responseJson = new JSONObject(responseBody);

        JSONArray choices = responseJson.getJSONArray("choices");
        String answerText = choices.getJSONObject(0).getString("text");

        return answerText;
    }
}
