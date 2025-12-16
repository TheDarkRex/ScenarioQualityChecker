package pl.put.poznan.transformer.logic.elements;

import pl.put.poznan.transformer.logic.visitors.Visitor;
import java.util.List;

public class Scenario implements Element {
    private String title;
    private List<String> actors;
    private String systemActor;
    private List<Step> steps;

    public Scenario(String title, List<String> actors, String systemActor, List<Step> steps) {
        this.title = title;
        this.actors = actors;
        this.systemActor = systemActor;
        this.steps = steps;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getActors() {
        return actors;
    }

    public String getSystemActor() {
        return systemActor;
    }

    public List<Step> getSteps() {
        return steps;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
        for (Step step : steps) {
            step.accept(visitor);
        }
    }

}
