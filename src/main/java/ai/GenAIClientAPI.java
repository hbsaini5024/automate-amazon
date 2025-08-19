package ai;



import java.io.IOException;
import java.util.*;
import okhttp3.*;
import com.google.gson.*;

public class GenAIClientAPI {
	
	private static final String API_KEY = "AIzaSyBA4WuFZYrQcSQLJUSxCIMcM2I5kqgWGdQ"; // replace with your key
    private static final String API_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";
    private static final Gson gson = new Gson();

    public static void main(String[] args) throws IOException {
    	Map<String, Object> userData = GenAIClientAPI.generateCartData();
        System.out.println("Generated User: " + userData);
	}

    public static Map<String, String> generateUserData() throws IOException {
        OkHttpClient client = new OkHttpClient();

        // Prompt for AI
        String prompt = "Generate a fake ecommerce user with fields: name, email, password (JSON format).";

        // JSON request
        String requestBody = "{\n" +
                "  \"contents\": [{\"parts\":[{\"text\":\"" + prompt + "\"}]}]\n" +
                "}";

        Request request = new Request.Builder()
                .url(API_URL + "?key=" + API_KEY)
                .post(RequestBody.create(requestBody, MediaType.parse("application/json")))
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        // Parse JSON
        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
        String text = jsonObject.getAsJsonArray("candidates")
                .get(0).getAsJsonObject()
                .getAsJsonObject("content")
                .getAsJsonArray("parts")
                .get(0).getAsJsonObject()
                .get("text").getAsString();

        // Clean AI response
        text = text.replaceAll("```json", "").replaceAll("```", "").trim();

        Gson gson = new Gson();
        return gson.fromJson(text, Map.class);
    }

    public static String generateProductName() throws IOException {
        OkHttpClient client = new OkHttpClient();
        String prompt = "Generate one random product name for ecommerce site like Amazon.";

        String requestBody = "{\n" +
                "  \"contents\": [{\"parts\":[{\"text\":\"" + prompt + "\"}]}]\n" +
                "}";

        Request request = new Request.Builder()
                .url(API_URL + "?key=" + API_KEY)
                .post(RequestBody.create(requestBody, MediaType.parse("application/json")))
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
        return jsonObject.getAsJsonArray("candidates")
                .get(0).getAsJsonObject()
                .getAsJsonObject("content")
                .getAsJsonArray("parts")
                .get(0).getAsJsonObject()
                .get("text").getAsString().trim();
    }
    
    public static Map<String, Object> generateCartData() throws IOException {
        String prompt = "Generate a random shopping cart JSON with 2 products, each having productId (1-20) and quantity (1-5).";
        String prompt1 = "Generate a valid FakeStoreAPI cart JSON with fields: userId (1-10), date (YYYY-MM-DD), and products (array of productId 1-20 and quantity 1-5). Do not add extra text, only return raw JSON.";

        String requestBody = gson.toJson(Map.of(
                "contents", new Object[]{Map.of("parts", new Object[]{Map.of("text", prompt1)})}
        ));
        
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(API_URL + "?key=" + API_KEY)
                .post(RequestBody.create(requestBody, MediaType.parse("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            String responseBody = response.body().string();
            JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
            String text = jsonResponse
                    .getAsJsonArray("candidates")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("content")
                    .getAsJsonArray("parts")
                    .get(0).getAsJsonObject()
                    .get("text").getAsString()
                    .trim();

            // If AI wrapped JSON in code fences or quotes, clean it
            if (text.startsWith("```")) {
                text = text.replaceAll("```(json)?", "").trim();
            }
            if (text.startsWith("\"") && text.endsWith("\"")) {
                text = text.substring(1, text.length() - 1);
            }

            // Now parse
            return gson.fromJson(text, Map.class);
        }
    }
}
