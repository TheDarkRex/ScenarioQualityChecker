package pl.put.poznan.sqc.model;
import pl.put.poznan.sqc.logic.vistor.Visitor;

public interface Visitable {
    void accept(Visitor visitor);
}