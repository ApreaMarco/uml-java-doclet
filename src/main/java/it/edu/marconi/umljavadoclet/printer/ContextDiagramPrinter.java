package it.edu.marconi.umljavadoclet.printer;

import it.edu.marconi.umljavadoclet.model.Model;
import it.edu.marconi.umljavadoclet.model.ModelClass;
import it.edu.marconi.umljavadoclet.model.ModelRel;

import java.util.ArrayList;
import java.util.List;

public class ContextDiagramPrinter extends PumlDiagramPrinter {
    public ContextDiagramPrinter(Model model, ModelClass contextClass, DiagramOptions options) {
        super(model, options);
        _contextClass = contextClass;
    }

    public void generate() {
        start();
        noPackagesOption();
        addContextClass(_contextClass);
        for (ModelRel rel : _contextClass.relationships()) {
            addRelationship(rel);
            newline();
        }
        end();
    }

    private void addContextClass(ModelClass modelClass) {
        String filepath = classFilepath(modelClass, modelClass);
        classDefinition(modelClass, true, filepath, null, true, true, true, false, true);
        _classes.add(modelClass);
    }

    private void addRelationship(ModelRel rel) {
        ModelClass otherClass = (rel.source() != _contextClass ? rel.source() : rel.destination());
        if (!otherClass.fullName().startsWith("java.util.")) {
            if (getOptions().isExcludedPackage(otherClass) || getOptions().isExcludedClass(otherClass)) {
                return;
            }
            if (isRelationshipVisible(rel)) {
                if (!_classes.contains(otherClass)) {
                    String filepath = null;
                    if (otherClass.modelPackage() != null) {
                        filepath = classFilepath(_contextClass, otherClass);
                    }
                    if (otherClass.modelPackage() == _contextClass.modelPackage()) {
                        classDefinitionNoDetail(otherClass, true, filepath, null);
                    } else if (otherClass.isInternal()) {
                        classDefinitionNoDetail(otherClass, true, filepath, "white");
                    } else {
                        classDefinitionNoDetail(otherClass, true, filepath, "lightgrey");
                    }
                    _classes.add(otherClass);
                }
                relationship(rel);
            }
        }
    }

    private final ModelClass _contextClass;
    private final List<ModelClass> _classes = new ArrayList<>();
}