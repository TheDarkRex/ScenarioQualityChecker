package pl.put.poznan.sqc.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import pl.put.poznan.sqc.model.Scenario;
import pl.put.poznan.sqc.logic.vistor.StepCounterVisitor;

@RestController
@RequestMapping("/api") // Zmieniamy ścieżkę na bardziej standardową
public class ScenarioQualityCheckerController {

    private static final Logger logger = LoggerFactory.getLogger(ScenarioQualityCheckerController.class);

    // Przykład endpointu do liczenia kroków
    // Oczekuje JSONa ze scenariuszem w ciele żądania (Body)
    @PostMapping("/calculate/steps")
    public int calculateSteps(@RequestBody Scenario scenario) {
        logger.debug("Received request to count steps for scenario: {}", scenario.getTitle());

        // 1. Tworzymy wizytatora
        StepCounterVisitor visitor = new StepCounterVisitor();

        // 2. Uruchamiamy wizytatora na scenariuszu
        scenario.accept(visitor);

        // 3. Zwracamy wynik
        return visitor.getStepCount();
    }
}