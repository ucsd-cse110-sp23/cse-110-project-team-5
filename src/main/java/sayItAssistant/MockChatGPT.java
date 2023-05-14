
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Mock class to get static anwers for testing
 */
public class MockChatGPT {
    
    private static final String API_ENDPOINT = "https://api.openai.com/v1/completions";
    private static final String API_KEY = "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";
    private static final String MODEL = "text-davinci-003";

    public static String getAnswer(String questionTranscription) throws IOException, InterruptedException {
        //String questionTranscription is the prompt
        String prompt = questionTranscription;
        int maxTokens = 100;

        // Create a request body which you will pass into request object
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", MODEL);
        requestBody.put("prompt", prompt);
        requestBody.put("max_tokens", maxTokens);
        requestBody.put("temperature", 1.0);

        // Create the HTTP client
        HttpClient client = HttpClient.newHttpClient();

        // Create the request object
        HttpRequest request = HttpRequest
        .newBuilder()
        .uri(URI.create(API_ENDPOINT))
        .header("Content-Type", "application/json")
        .header("Authorization", String.format("Bearer %s", "mock_api_key")) //mock request key
        // .header("Authorization", String.format("Bearer %s", API_KEY))
        .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
        .build();

        // Send the request and receive the response
        HttpResponse<String> response = client.send(
            request, 
            HttpResponse.BodyHandlers.ofString()
        );

        // // Process the response
        // String responseBody = response.body();

        // Process the mock response
        String responseBody = "{\"id\":\"cmpl-7E47oEiuLZNgAj5ICQs988ZWVubz8\",\"object\":\"text_completion\",\"created\":1683586804,\"model\":\"text-davinci-003\",\"choices\":[{\"text\":\"Sorry, I don't know the answer to that question.\",\"index\":0,\"logprobs\":null,\"finish_reason\":\"stop\"}],\"usage\":{\"prompt_tokens\":35,\"completion_tokens\":15,\"total_tokens\":50}}";

        JSONObject responseJson = new JSONObject(responseBody);

        JSONArray choices = responseJson.getJSONArray("choices");
        String answerText = choices.getJSONObject(0).getString("text");
        
        return answerText;
    }
}