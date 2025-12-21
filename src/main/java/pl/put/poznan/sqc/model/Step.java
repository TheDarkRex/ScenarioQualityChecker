package pl.put.poznan.sqc.model;

import pl.put.poznan.sqc.logic.visitor.Visitor;
import java.util.List;


public class Step implements Visitable {
    private String content;
    private List<Step> subSteps;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
        if (subSteps != null) {
            for (Step step : subSteps) {
                step.accept(visitor);
            }
        }
    }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public List<Step> getSubSteps() { return subSteps; }
    public void setSubSteps(List<Step> subSteps) { this.subSteps = subSteps; }
}