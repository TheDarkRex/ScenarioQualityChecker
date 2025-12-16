package pl.put.poznan.transformer.logic.elements;

import pl.put.poznan.transformer.logic.visitors.Visitor;

import java.util.ArrayList;
import java.util.List;

public class Step implements Element {
    private String text;
    private List<Step> subSteps;

    public Step(String text, List<Step> subSteps) {
        this.text = text;
        this.subSteps = (subSteps != null) ? subSteps : new ArrayList<>();
    }

    public String getText() {
        return text;
    }

    public List<Step> getSubSteps() {
        return subSteps;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);

        if(subSteps != null && !subSteps.isEmpty()) {
            for (Step subStep : subSteps) {
                subStep.accept(visitor);
            }
        }
    }
}
