package dogapi;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     *
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    // List<String>
    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {

        Request request = new Request.Builder()
                .url("https://dog.ceo/api/breeds/list/all").build();
        Call call = client.newCall(request);

        List<String> subBreeds = new ArrayList<>();
         try (Response response = call.execute()){
            if (response.code() != 200) {
                throw new RuntimeException("Unexpected HTTP response: " + response.code());
            }

            String jsonString = response.body().string();
            // System.out.println(responseBody);

            JSONObject root = new JSONObject(jsonString);
            JSONObject message = root.getJSONObject("message");


                JSONArray subArray = message.getJSONArray(breed.toLowerCase().strip());

                    for (int i = 0; i < subArray.length(); i++) {
                        subBreeds.add(subArray.getString(i));
                    }
                    if (subArray == null) {
                        return new ArrayList<>();
                    }

        } catch (IOException | JSONException e) {
             throw new BreedNotFoundException(breed);
        }

        return subBreeds;
    }
}

