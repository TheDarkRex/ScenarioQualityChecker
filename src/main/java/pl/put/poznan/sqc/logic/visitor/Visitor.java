package pl.put.poznan.sqc.logic.visitor;
import pl.put.poznan.sqc.model.Scenario;
import pl.put.poznan.sqc.model.Step;

/**
 * Interfejs definiujący wzorzec projektowy Wizytator.
 */

public interface Visitor {
    /**
     * Metoda odwiedzająca scenariusz.
     *
     * @param scenario scenariusz do odwiedzenia
     */
    void visit(Scenario scenario);

    /**
     * Metoda odwiedzająca krok scenariusza.
     *
     * @param step krok scenariusza do odwiedzenia
     */
    void visit(Step step);
}