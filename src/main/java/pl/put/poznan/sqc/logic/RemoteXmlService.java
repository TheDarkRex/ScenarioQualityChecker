package pl.put.poznan.sqc.logic;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import pl.put.poznan.sqc.model.Scenario;

@Service
public class RemoteXmlService {

    // adres "zewnętrznego" narzędzia
    private static final String EXTERNAL_SERVICE_URL = "http://localhost:8080/external/converter";

    public String convertScenarioToXml(Scenario scenario) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Accept", "application/xml");

            HttpEntity<Scenario> request = new HttpEntity<>(scenario, headers);
            String xmlResponse = restTemplate.postForObject(EXTERNAL_SERVICE_URL, request, String.class);

            return xmlResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return "<Error>Connection to external service failed</Error>";
        }
    }
}