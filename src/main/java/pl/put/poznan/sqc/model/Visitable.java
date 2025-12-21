package pl.put.poznan.sqc.model;
import pl.put.poznan.sqc.logic.visitor.Visitor;

public interface Visitable {
    void accept(Visitor visitor);
}