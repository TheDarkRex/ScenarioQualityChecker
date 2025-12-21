package pl.put.poznan.sqc.logic.visitor;
import pl.put.poznan.sqc.model.Scenario;
import pl.put.poznan.sqc.model.Step;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa implementująca interfejs {@link Visitor}, która sprawdza
 * czy kroki {@link Step} w scenariuszu {@link Scenario} rozpoczynają
 * się od poprawnych aktorów.
 */

public class ActorCheckVisitor implements Visitor{
    /** Lista błędów wykrytych podczas weryfikacji kroków. */
    private List<String> errors = new ArrayList<>();

    /** Lista poprawnych aktorów dla danego scenariusza. */
    private List<String> validActors;

    /**
     * Zwraca listę kroków, które nie zaczynają się od poprawnego aktora.
     *
     * @return lista błędów
     */
    public List<String> getErrors() {
        return errors;
    }

    /**
     * Metoda odwiedzająca scenariusz.
     * Tworzy listę poprawnych aktorów na podstawie aktorów scenariusza
     * oraz aktora systemowego.
     *
     * @param scenario scenariusz do odwiedzenia
     */
    @Override
    public void visit(Scenario scenario) {
        this.validActors = new ArrayList<>(scenario.getActors());
        this.validActors.add(scenario.getSystemActor());
    }

    /**
     * Metoda odwiedzająca krok scenariusza.
     * Sprawdza, czy krok rozpoczyna się od poprawnego aktora.
     * Jeśli nie, dodaje jego treść do listy błędów.
     *
     * @param step krok scenariusza
     */
    @Override
    public void visit(Step step) {
        String text = step.getContent();
        String[] keywords = {"IF", "ELSE", "FOR EACH"};

        for (String keyword : keywords) {
            if (text.startsWith(keyword)) {
                text = text.substring(keyword.length());
                break;
            }
        }

        text = text.trim();
        if (text.startsWith(":")) {
            text = text.substring(1).trim();
        }

        if (text.isEmpty()) {
            errors.add(step.getContent());
            return;
        }

        boolean startsWithActor = false;
        for (String actor : validActors) {
            if (text.startsWith(actor)) {
                startsWithActor = true;
                break;
            }
        }

        if (!startsWithActor) {
            errors.add(step.getContent());
        }
    }
}
