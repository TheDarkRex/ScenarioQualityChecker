package pl.put.poznan.sqc.logic;

import java.util.List;

public class Step implements ScenarioElement {
    public String text;
    public List<Step> subSteps; // kroki dla podscenariuszy

    @Override
    public void accept(ScenarioVisitor visitor) {
        visitor.visit(this);
        if (subSteps != null) {
            for (Step subStep : subSteps) {
                subStep.accept(visitor);
            }
        }
    }
}
