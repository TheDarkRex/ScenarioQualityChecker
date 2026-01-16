package pl.put.poznan.sqc.logic.visitor;

import pl.put.poznan.sqc.model.Scenario;
import pl.put.poznan.sqc.model.Step;

import java.util.ArrayList;
import java.util.List;

public class LevelFilterVisitor implements Visitor {
    private int maxLevel;
    private Scenario filteredScenario;

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public Scenario getFilteredScenario() {
        return filteredScenario;
    }

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

    @Override
    public void visit(Step step) {

    }
}
