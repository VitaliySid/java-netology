package org.example.nasa;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.example.nasa.models.NasaAnswer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class NasaClient {
    private static final Logger logger = LoggerFactory.getLogger(NasaClient.class);
    final String PICTURE_DAY_URL = "https://api.nasa.gov/planetary/apod?api_key=%s";
    private final String apiKey;

    public NasaClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public NasaAnswer getPictureDay(String strDate) {

        CloseableHttpResponse response;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            HttpGet httpGet = new HttpGet(buildUrl(strDate));

            response = httpClient.execute(httpGet);

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.getEntity().getContent(), NasaAnswer.class);

        } catch (IOException e) {
            logger.error("Произошла ошибка доступа к серверу NASA");
            return null;
        }
    }

    private String buildUrl(String strDate) {
        var url = String.format(PICTURE_DAY_URL, apiKey);

        if (strDate != null && !strDate.isEmpty()) {
            SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
            try {
                url += "&date=" + formater.format(formater.parse(strDate));
            } catch (ParseException e) {
                logger.error("Ошибка формата переданной даты");
            }
        }
        logger.info(url);

        return url;
    }

}
