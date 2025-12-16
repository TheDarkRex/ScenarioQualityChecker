package pl.put.poznan.transformer.logic.elements;
import pl.put.poznan.transformer.logic.visitors.Visitor;

public interface Element {
    void accept(Visitor visitor);
}
