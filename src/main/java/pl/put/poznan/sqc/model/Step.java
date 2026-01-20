package pl.put.poznan.sqc.model;

import pl.put.poznan.sqc.logic.visitor.Visitor;
import java.util.List;

/**
 * Klasa reprezentująca pojedynczy krok scenariusza {@link Scenario}, implementuje
 * interfejs {@link Visitable}. Opisuje pojedynczą interakcję między aktorami a systemem.
 */
public class Step implements Visitable {
    /**
     * Treść kroku scenariusza.
     */
    private String content;

    /**
     * Lista pod-kroków kroku scenariusza.
     */
    private List<Step> subSteps;

    /**
     * Metoda akceptująca wizytatora.
     *
     * @param visitor wizytator wykonujący działanie na scenariuszu
     */
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
        if (subSteps != null) {
            for (Step step : subSteps) {
                step.accept(visitor);
            }
        }
    }

    /**
     * Metoda zwracająca treść kroku scenariusza.
     *
     * @return treść kroku scenariusza
     */
    public String getContent() { return content; }

    /**
     * Metoda ustawiająca treść kroku scenariusza.
     *
     * @param content treść kroku scenariusza
     */
    public void setContent(String content) { this.content = content; }

    /**
     * Metoda zwracająca listę pod-kroków kroku scenariusza.
     *
     * @return lista pod-kroków kroku scenariusza
     */
    public List<Step> getSubSteps() { return subSteps; }

    /**
     * Metoda ustawiająca listę pod-kroków kroku scenariusza.
     *
     * @param subSteps lista pod-kroków kroku scenariusza
     */
    public void setSubSteps(List<Step> subSteps) { this.subSteps = subSteps; }
}