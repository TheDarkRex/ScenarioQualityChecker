package pl.put.poznan.sqc.logic.visitor;
import pl.put.poznan.sqc.model.Scenario;
import pl.put.poznan.sqc.model.Step;

import java.util.ArrayList;
import java.util.List;

public class ActorCheckVisitor implements Visitor{
    private List<String> errors = new ArrayList<>();
    private List<String> validActors;

    public List<String> getErrors() {
        return errors;
    }


    @Override
    public void visit(Scenario scenario) {
        this.validActors = new ArrayList<>(scenario.getActors());
        this.validActors.add(scenario.getSystemActor());
    }

    @Override
    public void visit(Step step) {
        String text = step.getContent();
        String[] keywords = {"IF", "ELSE", "FOR EACH"};

        for (String keyword : keywords) {
            if (text.startsWith(keyword)) {
                text = text.substring(keyword.length());
                break;
            }
        }

        text = text.trim();
        if (text.startsWith(":")) {
            text = text.substring(1).trim();
        }

        if (text.isEmpty()) {
            errors.add(step.getContent());
            return;
        }

        boolean startsWithActor = false;
        for (String actor : validActors) {
            if (text.startsWith(actor)) {
                startsWithActor = true;
                break;
            }
        }

        if (!startsWithActor) {
            errors.add(step.getContent());
        }

        if (step.getSubSteps() != null && !step.getSubSteps().isEmpty()) {
            for (Step subStep : step.getSubSteps()) {
                visit(subStep);
            }
        }
    }
}
