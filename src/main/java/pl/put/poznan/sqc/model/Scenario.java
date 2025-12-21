package pl.put.poznan.sqc.model;

import pl.put.poznan.sqc.logic.vistor.Visitor;
import java.util.List;

public class Scenario implements Visitable {
    private String title;
    private List<String> actors;
    private String systemActor;
    private List<Step> steps;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
        if (steps != null) {
            for (Step step : steps) {
                step.accept(visitor);
            }
        }
    }


    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public List<Step> getSteps() { return steps; }
    public void setSteps(List<Step> steps) { this.steps = steps; }

    public List<String> getActors() { return actors; }
    public void setActors(List<String> actors) { this.actors = actors; }

    public String getSystemActor() { return systemActor; }
    public void setSystemActor(String systemActor) { this.systemActor = systemActor; }
}