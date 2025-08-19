package ai;

import java.io.IOException;
import com.google.gson.*;
import org.json.JSONObject;

import okhttp3.*;

public class GenAIClient {

	private static final String API_KEY = "AIzaSyBA4WuFZYrQcSQLJUSxCIMcM2I5kqgWGdQ"; // Apna Gemini API Key yahan lagao
	private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";

	private static final Gson gson = new Gson();
	private static final OkHttpClient httpClient = new OkHttpClient();

	public static void main(String[] args) {
		try {
			GenAIClient genAI = new GenAIClient();


			String keyword = genAI.generateProductSearchKeyword();
			String customAddress = genAI.generateAddress();
			

			// Remove ```json and ``` from start/end
			String cleanedJson = returnText(customAddress).replaceAll("(?s)```json", "")  // remove opening ```json
			                            .replaceAll("```", "")           // remove closing ```
			                            .trim(); 
			System.out.println("After cleaned Json " +cleanedJson);
			extractAddress(cleanedJson);

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void extractAddress(String jsonData) {
		JSONObject jsonObject = new JSONObject(jsonData);
		System.out.println("Yha kya arhha h "+jsonObject);
		String street = jsonObject.getString("street");
		String city = jsonObject.getString("city");
		String state = jsonObject.getString("state");
		String zip = jsonObject.getString("zip");

		System.out.println("Street: " + street);
		System.out.println("City: " + city);
		System.out.println("State: " + state);
		System.out.println("ZIP: " + zip);
	}

	public static String returnText(String jsonString) {
		String jsonResponse = jsonString;
		String text = null;
		JsonObject root = JsonParser.parseString(jsonResponse).getAsJsonObject();
		JsonArray candidates = root.getAsJsonArray("candidates");

		if (candidates != null && candidates.size() > 0) {
			JsonObject firstCandidate = candidates.get(0).getAsJsonObject();
			JsonObject content = firstCandidate.getAsJsonObject("content");
			JsonArray parts = content.getAsJsonArray("parts");

			if (parts != null && parts.size() > 0) {
				text = parts.get(0).getAsJsonObject().get("text").getAsString();
				System.out.println("Extracted Text: " + text);

			}
		}
		return text;

	}

	// Core method to send prompt
	private String sendPrompt(String prompt) throws IOException, InterruptedException {
		JsonObject part = new JsonObject();
		part.addProperty("text", prompt);

		JsonArray parts = new JsonArray();
		parts.add(part);

		JsonObject content = new JsonObject();
		content.add("parts", parts);

		JsonArray contents = new JsonArray();
		contents.add(content);

		JsonObject jsonBody = new JsonObject();
		jsonBody.add("contents", contents);

		RequestBody body = RequestBody.create(gson.toJson(jsonBody), MediaType.parse("application/json"));

		Request request = new Request.Builder().url(API_URL + "?key=" + API_KEY)
				.addHeader("Content-Type", "application/json").post(body).build();

		int retries = 0;
		int maxRetries = 5;
		long waitTime = 1500; // 2 sec

		while (true) {
			try (Response response = httpClient.newCall(request).execute()) {

				if (response.isSuccessful()) {
					if (response.body() != null) {
						return response.body().string();
					} else {
						throw new IOException("Empty response body");
					}
				}

				// Handle Rate Limit
				else if (response.code() == 429) {
					retries++;
					if (retries > maxRetries) {
						throw new IOException("Max retries exceeded due to rate limit");
					}

					// Check Retry-After header if available
					String retryAfter = response.header("Retry-After");
					if (retryAfter != null) {
						try {
							waitTime = Long.parseLong(retryAfter) * 1000; // Convert to ms
						} catch (NumberFormatException ignored) {
						}
					}

					System.out.println("Rate limit hit. Retrying in " + (waitTime / 1000) + "s...");
					Thread.sleep(waitTime);
					waitTime *= 2; // exponential backoff
				}

				// Handle other errors
				else {
					throw new IOException("HTTP error code: " + response.code() + " - " + response.message());
				}

			} catch (IOException e) {
				throw e;
			}
		}
	}

	// Helper methods for different test data
	public String generateCredentials() throws IOException, InterruptedException {
		return sendPrompt(
				"Generate a random test user with a realistic full name, email, and password in JSON format.");
	}

	public String generateAddress() throws IOException, InterruptedException {
		return sendPrompt("Generate a fake shipping address in JSON format with street, city, state, and zip.");
	}

	public String generateProductSearchKeyword() throws IOException, InterruptedException {
		return sendPrompt("Suggest a random e-commerce product category for testing searches, only one phrase.");
	}

	public String validateProductRelevance(String keyword, String[] productTitles)
			throws IOException, InterruptedException {
		StringBuilder list = new StringBuilder();
		for (String title : productTitles) {
			list.append("- ").append(title).append("\n");
		}
		return sendPrompt("Check if these products are relevant to '" + keyword + "'. Products:\n" + list
				+ "\nAnswer only Yes or No.");
	}
}
