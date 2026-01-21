package pl.put.poznan.sqc.rest.external;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.put.poznan.sqc.model.Scenario;

// kontroler symulujący zewnętrzną aplikację

@RestController
@RequestMapping("/external/converter")
public class ExternalXmlConverterController {

    // endpoint przyjmuje JSON i wymusza zwrot XML
    @PostMapping(produces = MediaType.APPLICATION_XML_VALUE)
    public Scenario convertToXml(@RequestBody Scenario scenario) {
        // Jackson XML sam weźmie obiekt i zamieni go na XML w odpowiedzi
        return scenario;
    }
}