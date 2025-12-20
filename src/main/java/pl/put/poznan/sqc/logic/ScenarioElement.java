package pl.put.poznan.sqc.logic;

public interface ScenarioElement {
    void accept(ScenarioVisitor visitor);
}
