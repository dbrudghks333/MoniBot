package com.hello.slackApp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class PrometheusService {

    private String prometheusUrl = "http://localhost:9090";

    public String processQuery(String query) throws UnsupportedEncodingException {

        // Define Prometheus HTTP API request
        String promApiCall = "api/v1/query?query=";


        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
        String requestUrl = prometheusUrl + "/" + promApiCall + encodedQuery;

        System.out.println("Prometheus REST API request: " + requestUrl);

        // 1. Create HTTP request
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(requestUrl));

        // 2. Create RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // 3. Invoke HTTP request
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

        // 4. Provide response information
        System.out.println("resp.Header: " + responseEntity.getHeaders());
        System.out.println("resp.StatusCode: " + responseEntity.getStatusCode());

        // 5. Verify the request status
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            String responseBody = responseEntity.getBody();
            System.out.println("-> Response bodyString: " + responseBody);
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(responseBody);

                JsonNode valueNode = jsonNode
                        .path("data")
                        .path("result")
                        .get(0)
                        .path("value");

                // Accessing the second element in the "value" array
                String value = valueNode.get(1).asText();
                System.out.println("Parsed value: " + value);

                return value;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return "Error";
    }
}
