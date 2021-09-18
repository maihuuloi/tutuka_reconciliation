package com.tutuka.txmanagement.bdd;

import com.tutuka.txmanagement.TutukaReconciliationApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@CucumberContextConfiguration
@SpringBootTest(classes = TutukaReconciliationApplication.class
, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SpringIntegrationTest {
    protected ResponseEntity responseEntity = null;

    @Autowired
    protected RestTemplate restTemplate;

    private String contextPath = "http://localhost:8080/";


    protected <T> void executeMultipartFilePost(String path, MultiValueMap<String, Object> body, Class<T> clazz) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);


        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        String serverUrl = contextPath + path;
        responseEntity = restTemplate.postForEntity(serverUrl, requestEntity, clazz);

    }

}
