package pl.put.poznan.sqc.logic.visitor;
import pl.put.poznan.sqc.model.Scenario;
import pl.put.poznan.sqc.model.Step;

/**
 * Klasa implementująca interfejs {@link Visitor}, która zlicza
 * liczbę kroków {@link Step} w scenariuszu {@link Scenario}.
 */


public class StepCounterVisitor implements Visitor {
    /** Liczba kroków zliczonych podczas wizyty w scenariuszu. */
    private int stepCount = 0;

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
     * Inkrementuje licznik kroków.
     *
     * @param step krok scenariusza
     */
    @Override
    public void visit(Step step) {
        stepCount++;
    }

    /**
     * Zwraca liczbę kroków, które zostały zliczone podczas wizyty
     * w scenariuszu.
     *
     * @return liczba kroków
     */
    public int getStepCount() {
        return stepCount;
    }
}