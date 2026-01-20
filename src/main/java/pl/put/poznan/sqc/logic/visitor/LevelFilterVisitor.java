package pl.put.poznan.sqc.logic.visitor;

import pl.put.poznan.sqc.model.Scenario;
import pl.put.poznan.sqc.model.Step;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa implementująca interfejs {@link Visitor}, która uzyskuje
 * scenariusz {@link Scenario} zawierający pod-scenariusze do określonego
 * poziomu zagnieżdżenia kroków.
 */

public class LevelFilterVisitor implements Visitor {
    /** Maksymalny poziom zagnieżdżenia kroków, który ma zostać uwzględniony w scenariuszu wynikowym.*/
    private int maxLevel;

    /** Otrzymany w wyniku filtrowania scenariusz. */
    private Scenario filteredScenario;

    /**
     * Metoda ustawia maksymalny poziom zagnieżdżenia kroków.
     *
     * @param maxLevel maksymalny poziom zagnieżdżenia kroków
     */
    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    /**
     * Metoda zwraca wynikowy scenariusz.
     *
     * @return scenariusz zawierający pod-scenariusze wyłącznie do określonego poziomu
     */
    public Scenario getFilteredScenario() {
        return filteredScenario;
    }

    /**
     * Metoda rekurencyjnie kopiuje listę kroków scenariusza ograniczając głębokość zagnieżdżenia kroków do
     * {@link #maxLevel}.
     *
     * @param originalSteps lista oryginalnych kroków scenariusza
     * @param currentLevel aktualny poziom zagnieżdżenia kroków
     *
     * @return nowa lista kroków
     */
    private List<Step> copySteps(List<Step> originalSteps, int currentLevel) {
        if (originalSteps == null || currentLevel > maxLevel) {
            return null;
        }

        List<Step> newSteps = new ArrayList<>();
        for (Step oldStep : originalSteps) {
            Step newStep = new Step();
            newStep.setContent(oldStep.getContent());

            if (currentLevel < maxLevel && oldStep.getSubSteps() != null) {
                newStep.setSubSteps(copySteps(oldStep.getSubSteps(), currentLevel + 1));
            } else {
                newStep.setSubSteps(new ArrayList<>());
            }

            newSteps.add(newStep);
        }
        return newSteps;
    }

    /**
     * Metoda odwiedzająca scenariusz.
     * Tworzy przefiltrowaną kopię scenariusza z głębokością kroków
     * do {@link #maxLevel}.
     *
     * @param scenario scenariusz do przefiltrowania
     */
    @Override
    public void visit(Scenario scenario) {
        this.filteredScenario = new Scenario();
        this.filteredScenario.setTitle(scenario.getTitle());
        this.filteredScenario.setActors(new ArrayList<>(scenario.getActors()));
        this.filteredScenario.setSystemActor(scenario.getSystemActor());

        if (maxLevel >= 1 && scenario.getSteps() != null) {
            this.filteredScenario.setSteps(copySteps(scenario.getSteps(), 1));
        }
    }

    /**
     * Metoda odwiedzająca krok scenariusza, wymagana przez interfejs {@link Visitor}.
     *
     * @param step krok scenariusza do odwiedzenia
     */
    @Override
    public void visit(Step step) {

    }
}
