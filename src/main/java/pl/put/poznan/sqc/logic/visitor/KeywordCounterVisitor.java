package pl.put.poznan.sqc.logic.visitor;

import pl.put.poznan.sqc.model.Scenario;
import pl.put.poznan.sqc.model.Step;

/**
 * Klasa implementująca interfejs {@link Visitor}, która
 * zllicza kroki ({@link Step}) scenariusza {@link Scenario}
 * rozpoczynające się od słów kluczowych.
 */

public class KeywordCounterVisitor implements Visitor {
    /** Liczba kroków rozpoczynających się od słów kluczowych. */
    private int keywordCount = 0;

    /**
     * Metoda odwiedzająca scenariusz.
     *
     * @param scenario scenariusz do odwiedzenia
     */
    @Override
    public void visit(Scenario scenario) {

    }

    /**
     * Metoda odwiedzająca krok scenariusza.
     * Inkrementuje licznik, jeśli krok zaczyna się od słowa kluczowego.
     *
     * @param step krok do odwiedzenia
     */
    @Override
    public void visit(Step step) {
        String content = step.getContent();

        if (content != null) {
            if (content.startsWith("IF") || content.startsWith("ELSE") || content.startsWith("FOR EACH")) {
                keywordCount++;
            }
        }
    }

    /**
     * Zwraca liczbę kroków rozpoczynających się od słów kluczowych.
     *
     * @return liczba kroków zaczynających się od słów kluczowych
     */
    public int getKeywordCount() {
        return keywordCount;
    }
}