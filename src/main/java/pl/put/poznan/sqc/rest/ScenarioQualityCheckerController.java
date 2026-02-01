package pl.put.poznan.sqc.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.put.poznan.sqc.logic.RemoteXmlService;
import pl.put.poznan.sqc.logic.visitor.ActorCheckVisitor;
import pl.put.poznan.sqc.logic.visitor.KeywordCounterVisitor;
import pl.put.poznan.sqc.logic.visitor.LevelFilterVisitor;
import pl.put.poznan.sqc.logic.visitor.StepCounterVisitor;
import pl.put.poznan.sqc.logic.visitor.StepNumberingVisitor;
import pl.put.poznan.sqc.model.Scenario;

/**
 * Kontroler REST odpowiedzialny za analizę jakości scenariuszy przypadków użycia.
 * <p>
 * Udostępnia endpointy umożliwiające obliczanie metryk jakościowych oraz
 * weryfikację poprawności scenariusza. Analiza realizowana jest
 * z wykorzystaniem wzorca projektowego Visitor.
 * </p>
 */
@RestController
@RequestMapping("/api")
public class ScenarioQualityCheckerController {

    /** Logger wykorzystywany do celów diagnostycznych. */
    private static final Logger logger = LoggerFactory.getLogger(ScenarioQualityCheckerController.class);

    private final RemoteXmlService remoteXmlService;

    public ScenarioQualityCheckerController(RemoteXmlService remoteXmlService) {
        this.remoteXmlService = remoteXmlService;
    }

    /**
     * Oblicza całkowitą liczbę kroków w scenariuszu.
     * <p>
     * Metoda wykorzystuje klasę {@link StepCounterVisitor}, która przechodzi
     * przez całą strukturę scenariusza i zlicza wszystkie kroki,
     * w tym również kroki zagnieżdżone.
     * </p>
     *
     * @param scenario scenariusz przesłany w treści żądania
     * @return całkowita liczba kroków w scenariuszu
     */
    @PostMapping("/calculate/steps")
    public String calculateSteps(@RequestBody Scenario scenario) {
        logger.info("Received request to count steps for scenario: {}", scenario.getTitle());

        StepCounterVisitor visitor = new StepCounterVisitor();
        scenario.accept(visitor);

        logger.info("Counting steps complete for scenario: {}. Count is {}.",scenario.getTitle() , visitor.getStepCount());
        return "{\n\t\"count\": "+visitor.getStepCount()+"\n}";
    }

    /**
     * Zlicza liczbę słów kluczowych występujących w scenariuszu.
     * <p>
     * Słowa kluczowe mogą oznaczać m.in. kroki warunkowe lub alternatywne.
     * Logika zliczania została zaimplementowana w klasie
     * {@link KeywordCounterVisitor}.
     * </p>
     *
     * @param scenario scenariusz przesłany w treści żądania
     * @return liczba wykrytych słów kluczowych
     */
    @PostMapping("/calculate/keywords")
    public String calculateKeywords(@RequestBody Scenario scenario) {
        logger.info("Received request to count keywords for scenario: {}", scenario.getTitle());

        KeywordCounterVisitor visitor = new KeywordCounterVisitor();
        scenario.accept(visitor);

        logger.info("Counting keywords complete for scenario: {}. Count is {}.",scenario.getTitle() , visitor.getKeywordCount());
        return "{\n\t\"count\": "+visitor.getKeywordCount()+"\n}";
    }

    /**
     * Weryfikuje poprawność przypisania aktorów do kroków scenariusza.
     * <p>
     * Sprawdzenie polega na zweryfikowaniu, czy każdy krok scenariusza
     * jest wykonywany przez aktora zdefiniowanego w scenariuszu.
     * Wszelkie naruszenia są zwracane w postaci komunikatów tekstowych.
     * </p>
     *
     * @param scenario scenariusz przesłany w treści żądania
     * @return lista błędów; lista pusta oznacza poprawny scenariusz
     */
    @PostMapping("/analyze/scenario")
    public String verifyScenario(@RequestBody Scenario scenario) {
        logger.info("Received request to verify correct actor assignment for scenario: {}", scenario.getTitle());

        ActorCheckVisitor visitor = new ActorCheckVisitor();
        scenario.accept(visitor);

        logger.info("Analyzing steps complete for scenario: {}. Found {} errors.",scenario.getTitle(), visitor.getErrors().size());
        if (visitor.getErrors().size() == 0) {
            return "{\n\t\"errors\":\n\t[]\n}";
        } else {
            return "{\n\t\"errors\":\n\t[\n\t\t\"step\": \""+String.join("\",\n\t\t\"step\": \"", visitor.getErrors())+"\"\n\t]\n}";
        }
    }

    @PostMapping(value = "/get-xml", produces = MediaType.APPLICATION_XML_VALUE)
    public String getScenarioAsXml(@RequestBody Scenario scenario) {
        logger.info("Received request to convert scenario to XML: {}", scenario.getTitle());
        return remoteXmlService.convertScenarioToXml(scenario);
    }

    @PostMapping(value = "/get-numbered-scenario")
    public String getStepNumberingVisitor(@RequestBody Scenario scenario) {
        logger.info("Received request to list all steps from scenario: {}", scenario.getTitle());
        StepNumberingVisitor visitor = new StepNumberingVisitor();
        scenario.accept(visitor);
        return visitor.getScenarioText();
    }

    public static class ScenarioFilterRequest {
        private Scenario scenario;
        private int maxLevel;

        public Scenario getScenario() { return scenario; }
        public void setScenario(Scenario scenario) { this.scenario = scenario; }
        public int getMaxLevel() { return maxLevel; }
        public void setMaxLevel(int maxLevel) { this.maxLevel = maxLevel; }
    }

    @PostMapping(value = "/get-scenario-to-level")
    public Scenario getStepNumberingVisitor(@RequestBody ScenarioFilterRequest request) {
        Scenario scenario = request.getScenario();
        int maxLevel = request.getMaxLevel();

        logger.info("Received request to get limited scenario: {}, to a level of: {}", scenario.getTitle(), maxLevel);
        LevelFilterVisitor visitor = new LevelFilterVisitor();
        scenario.accept(visitor);
        return visitor.getFilteredScenario();
    }
}
