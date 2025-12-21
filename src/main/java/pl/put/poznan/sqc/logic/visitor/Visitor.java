package pl.put.poznan.sqc.logic.visitor;
import pl.put.poznan.sqc.model.Scenario;
import pl.put.poznan.sqc.model.Step;

public interface Visitor {
    void visit(Scenario scenario);
    void visit(Step step);
}