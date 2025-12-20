package pl.put.poznan.sqc.logic;

public interface ScenarioVisitor {
    void visit(Step step);
    void visit(Scenario scenario);
}
