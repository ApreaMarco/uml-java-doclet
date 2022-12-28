package it.edu.marconi.umljavadoclet.printer;

import it.edu.marconi.umljavadoclet.model.Model;
import it.edu.marconi.umljavadoclet.model.ModelClass;
import it.edu.marconi.umljavadoclet.model.ModelPackage;

public class OverviewDiagramPrinter extends PumlDiagramPrinter {
    public OverviewDiagramPrinter(Model model, DiagramOptions options) {
        super(model, options);
    }

    public void generate() {
        start();
        for (ModelPackage modelPackage : getModel().rootPackages()) {
            packageDefinition(modelPackage, packageFilepath(modelPackage), null);
            for (ModelClass modelClass : modelPackage.classes()) {
                String filepath = null;
                if (modelClass.modelPackage() != null) {
                    filepath = classFilepath(modelClass);
                }
                classDefinitionNoDetail(modelClass, false, filepath, null);
            }
        }
        end();
    }
}