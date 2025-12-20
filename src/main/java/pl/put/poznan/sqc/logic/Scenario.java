package pl.put.poznan.sqc.logic;

import java.util.List;

public class Scenario implements ScenarioElement {
    public String title;
    public List<String> actors;
    public List<String> systemActors;
    public List<Step> steps;

    @Override
    public void accept(ScenarioVisitor visitor) {
        if (steps != null) {
            for (Step step : steps) {
                step.accept(visitor);
            }
        }
    }
}
