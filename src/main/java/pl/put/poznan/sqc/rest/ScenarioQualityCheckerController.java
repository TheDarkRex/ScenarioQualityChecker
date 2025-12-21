package pl.put.poznan.sqc.rest;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.put.poznan.sqc.logic.visitor.ActorCheckVisitor;
import pl.put.poznan.sqc.logic.visitor.KeywordCounterVisitor;
import pl.put.poznan.sqc.logic.visitor.StepCounterVisitor;
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
    public int calculateSteps(@RequestBody Scenario scenario) {
        logger.debug("Received request to count steps for scenario: {}", scenario.getTitle());

        StepCounterVisitor visitor = new StepCounterVisitor();
        scenario.accept(visitor);

        return visitor.getStepCount();
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
    public int calcuateKeywords(@RequestBody Scenario scenario) {
        logger.debug("Received request to count keywords for scenario: {}", scenario.getTitle());

        KeywordCounterVisitor visitor = new KeywordCounterVisitor();
        scenario.accept(visitor);

        return visitor.getKeywordCount();
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
    public List<String> verifyScenario(@RequestBody Scenario scenario) {
        logger.debug("Received request to verify correct actor assignment for scenario: {}", scenario.getTitle());

        ActorCheckVisitor visitor = new ActorCheckVisitor();
        scenario.accept(visitor);

        return visitor.getErrors();
    }
}