package org.springblade.modules.sp.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springblade.modules.resource.MultipartInputStreamFileResource;
import org.springframework.http.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Service
@DS("postgresql")
@Slf4j
public class HttpService {



    public String getProgress(String url) throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                org.apache.http.HttpEntity entity =  response.getEntity();
                return entity != null ? EntityUtils.toString(entity, StandardCharsets.UTF_8) : null;
            }
        }catch (Exception e){
            e.getMessage();
            return null;
        }
    }

    public Map<String,String> isProgressComplete(String url) throws Exception {
        Map<String,String> res = new HashMap<>();
        String response = getProgress(url);

        if (response == null){
            res.put("status","500");
            res.put("message","请求超时");
            return res;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response);

        String status = jsonNode.get("status").asText();
        String uploadState = jsonNode.get("data").get("uploadState").asText();
        String message = jsonNode.get("message").asText();
        res.put("status",status);
        res.put("uploadState",uploadState);
        res.put("message",message);
        return res ;
    }

    public Map<String,String> uploadFile(MultipartFile file, String filename, String userId, String streamId, String branchId, String branchName,String url) throws IOException {
        Map<String,String> res = new HashMap<>();
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        // Create HttpHeaders
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("filename", filename);
        headers.set("userId", userId);
        headers.set("streamId", streamId);
        headers.set("branchId", branchId);
        headers.set("branchName", branchName);

        // Create MultiValueMap to hold the file and other form data
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));

        // Create HttpEntity
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Send the request
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        // 提取 status
        String statusKey = "\"status\":\"";
        String status = extractValue(response.getBody(), statusKey);

        // 提取 message
        String messageKey = "\"message\":\"";
        String message = extractValue(response.getBody(), messageKey);

        res.put("status",status);
        res.put("message",message);
        res.put("ip",extractUrl(url));
        return res;
    }

    private static String extractValue(String jsonString, String key) {
        int startIndex = jsonString.indexOf(key) + key.length();
        int endIndex = jsonString.indexOf("\"", startIndex);
        return jsonString.substring(startIndex, endIndex);
    }

    private static String extractUrl(String jsonString) {
        int startIndex = jsonString.indexOf("http://") + "http://".length();
        int endIndex = jsonString.indexOf("/uploadFile", startIndex);
        return jsonString.substring(startIndex, endIndex);
    }
}
