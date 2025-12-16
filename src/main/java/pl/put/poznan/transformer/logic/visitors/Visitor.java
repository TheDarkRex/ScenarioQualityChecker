package pl.put.poznan.transformer.logic.visitors;

import pl.put.poznan.transformer.logic.elements.Scenario;
import pl.put.poznan.transformer.logic.elements.Step;

public interface Visitor {
    void visit(Scenario scenario);
    void visit(Step step);
}
