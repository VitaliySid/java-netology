package org.example.nasa;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.example.nasa.models.NasaAnswer;

import java.io.IOException;

public class NasaClient {

    final String PICTURE_DAY_URL = "https://api.nasa.gov/planetary/apod?api_key=%s";
    private final String apiKey;

    public NasaClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public NasaAnswer getPictureDay() throws IOException {

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(String.format(PICTURE_DAY_URL, apiKey));

        CloseableHttpResponse response = httpClient.execute(httpGet);

        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(response.getEntity().getContent(), NasaAnswer.class);
    }

    public CloseableHttpResponse getImageData(NasaAnswer nasaAnswer) throws IOException {

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet imageRequest = new HttpGet(nasaAnswer.getUrl());

        return httpClient.execute(imageRequest);
    }

}
