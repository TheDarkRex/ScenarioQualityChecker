package pl.put.poznan.sqc.model;

import pl.put.poznan.sqc.logic.visitor.Visitor;
import java.util.List;

/**
 * Klasa reprezentująca scenariusz, implementuje interfejs {@link Visitable}.
 * Opisuje interakcję między aktorami i systemem w postaci listy kroków {@link Step}.
 */
public class Scenario implements Visitable {
    /**
     * Tytuł scenariusza.
     */
    private String title;
    /**
     * Lista aktorów scenariusza.
     */
    private List<String> actors;
    /**
     * Nazwa aktora systemowego scenariusza.
     */
    private String systemActor;
    /**
     * Lista kroków scenariusza.
     */
    private List<Step> steps;

    /**
     * Metoda akceptująca wizytatora.
     *
     * @param visitor wizytator wykonujący działanie na scenariuszu
     */
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
        if (steps != null) {
            for (Step step : steps) {
                step.accept(visitor);
            }
        }
    }


    /**
     * Metoda zwracająca tytuł scenariusza.
     *
     * @return tytuł scenariusza
     */
    public String getTitle() { return title; }

    /**
     * Metoda ustawiająca tytuł scenariusza.
     *
     * @param title tytuł scenariusza
     */
    public void setTitle(String title) { this.title = title; }

    /**
     * Metoda zwracająca listę kroków scenariusza.
     *
     * @return lista kroków scenariusza
     */
    public List<Step> getSteps() { return steps; }

    /**
     * Metoda ustawiająca listę kroków scenariusza.
     *
     * @param steps lista kroków scenariusza
     */
    public void setSteps(List<Step> steps) { this.steps = steps; }

    /**
     * Metoda zwracająca listę aktorów scenariusza.
     *
     * @return lista aktorów scenariusza
     */
    public List<String> getActors() { return actors; }

    /**
     * Metoda ustawiająca listę aktorów scenariusza.
     *
     * @param actors lista aktorów scenariusza
     */
    public void setActors(List<String> actors) { this.actors = actors; }

    /**
     * Metoda zwracająca aktora systemowego scenariusza.
     *
     * @return aktor systemowy scenariusza
     */
    public String getSystemActor() { return systemActor; }

    /**
     * Metoda ustawiająca aktora systemowego scenariusza.
     *
     * @param systemActor aktor systemowy scenariusza
     */
    public void setSystemActor(String systemActor) { this.systemActor = systemActor; }
}