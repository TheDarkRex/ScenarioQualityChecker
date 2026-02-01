package pl.put.poznan.sqc.logic.visitor;

import pl.put.poznan.sqc.model.Scenario;
import pl.put.poznan.sqc.model.Step;

import java.util.List;

public class StepNumberingVisitor implements Visitor {

    private StringBuilder sb;

    public StepNumberingVisitor() {
        sb = new StringBuilder();
    }

    @Override
    public void visit(Scenario scenario) {
        sb.append("Tytuł: ").append(scenario.getTitle()).append("\n");
        sb.append("Aktorzy: ").append(String.join(", ", scenario.getActors())).append("\n");
        sb.append("Aktor systemowy: ").append(scenario.getSystemActor()).append("\n\n");

        if (scenario.getSteps() != null) {
            visitSteps(scenario.getSteps(), "");
        }
    }

    private void visitSteps(List<Step> steps, String prefix) {
        int counter = 1;
        for (Step step : steps) {
            String number = prefix.isEmpty() ? counter + "." : prefix + counter + ".";
            sb.append(number).append(" ").append(step.getContent()).append("\n");

            if (step.getSubSteps() != null) {
                visitSteps(step.getSubSteps(), number);
            }
            counter++;
        }
    }

    @Override
    public void visit(Step step) {

    }

    public String getScenarioText() {
        return sb.toString();
    }
}
