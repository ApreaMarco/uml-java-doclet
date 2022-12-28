package it.edu.marconi.umljavadoclet.printer;

import it.edu.marconi.umljavadoclet.model.Model;
import it.edu.marconi.umljavadoclet.model.ModelClass;
import it.edu.marconi.umljavadoclet.model.ModelPackage;
import it.edu.marconi.umljavadoclet.model.ModelRel;

public class PackageDiagramPrinter extends PumlDiagramPrinter {
    public PackageDiagramPrinter(Model model, ModelPackage modelPackage, DiagramOptions options) {
        super(model, options);
        _modelPackage = modelPackage;
    }

    public void generate() {
        start();
        addPackage(_modelPackage, null);
        addRelationships(_modelPackage);
        for (ModelPackage subPackage : getModel().childPackages(_modelPackage)) {
            addPackage(subPackage, null);
        }
        end();
    }

    public void addPackage(ModelPackage modelPackage, String color) {
        String filepath = packageFilepath(_modelPackage, modelPackage);
        packageDefinition(modelPackage, filepath, color);
        for (ModelClass modelClass : modelPackage.classes()) {
            filepath = classFilepath(_modelPackage, modelClass);
            classDefinitionNoDetail(modelClass, false, filepath, null);
        }
    }

    public void addRelationships(ModelPackage modelPackage) {
        for (ModelClass modelClass : modelPackage.classes()) {
            for (ModelRel rel : modelClass.relationships()) {
                if (rel.source() == modelClass && rel.destination().modelPackage() == modelPackage) {
                    relationship(rel);
                }
            }
        }
    }

    private final ModelPackage _modelPackage;
}