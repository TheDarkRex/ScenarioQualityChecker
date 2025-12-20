package pl.put.poznan.sqc.logic;

public class StepCounterVisitor implements ScenarioVisitor {
    private int count = 0;

    @Override
    public void visit(Scenario scenario){

    }

    @Override
    public void visit(Step step) {
        count++;
    }

    public int getCount() {
        return count;
    }
}
