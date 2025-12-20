package pl.put.poznan.sqc.logic.vistor;
import pl.put.poznan.sqc.model.Scenario;
import pl.put.poznan.sqc.model.Step;

public class StepCounterVisitor implements Visitor {
    private int stepCount = 0;

    @Override
    public void visit(Scenario scenario) {

    }

    @Override
    public void visit(Step step) {
        stepCount++;
    }

    public int getStepCount() {
        return stepCount;
    }
}