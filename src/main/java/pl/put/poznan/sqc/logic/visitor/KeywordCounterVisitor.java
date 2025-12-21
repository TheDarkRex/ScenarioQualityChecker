package pl.put.poznan.sqc.logic.visitor;

import pl.put.poznan.sqc.model.Scenario;
import pl.put.poznan.sqc.model.Step;

public class KeywordCounterVisitor implements Visitor {
    private int keywordCount = 0;

    @Override
    public void visit(Scenario scenario) {

    }

    @Override
    public void visit(Step step) {
        String content = step.getContent();

        if (content != null) {
            if (content.startsWith("IF") || content.startsWith("ELSE") || content.startsWith("FOR EACH")) {
                keywordCount++;
            }
        }
    }

    public int getKeywordCount() {
        return keywordCount;
    }
}