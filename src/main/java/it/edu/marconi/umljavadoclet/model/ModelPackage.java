package it.edu.marconi.umljavadoclet.model;

import com.sun.javadoc.PackageDoc;

import java.util.ArrayList;
import java.util.List;

public class ModelPackage {
    public ModelPackage(Model model, PackageDoc packageDoc) {
        _model = model;
        _packageDoc = packageDoc;
    }

    public void map() {
        mapRelationships();
    }

    public String fullName() {
        return fullName(_packageDoc);
    }

    public String qualifiedName() {
        return fullName(_packageDoc);
    }

    public List<ModelClass> classes() {
        return _classes;
    }

    public List<ModelPackage> dependencies() {
        return _dependencyPackages;
    }

    public List<ModelPackage> dependents() {
        return _dependentPackages;
    }

    public static String fullName(PackageDoc packageDoc) {
        return packageDoc.name();
    }

    public boolean isChildPackage(ModelPackage parentPackage) {
        if (parentPackage != this) {
            if (qualifiedName().startsWith(parentPackage.qualifiedName())) {
                String thisPath = qualifiedName().substring(parentPackage.qualifiedName().length() + 1);
                // If the remaining part of the package name does not contain a period, it is an immediate child.
                return (!thisPath.contains("."));
            }
            return false;
        }
        return false;
    }

    public String parentPackageFullName() {
        if (qualifiedName().contains(".")) {
            return qualifiedName().substring(0, qualifiedName().lastIndexOf("."));
        } else {
            return qualifiedName();
        }
    }

    public void addClass(ModelClass modelClass) {
        if (!_classes.contains(modelClass)) {
            _classes.add(modelClass);
        }
    }

    private void mapRelationships() {
        for (ModelClass modelClass : _classes) {
            for (ModelRel rel : modelClass.relationships()) {
                if (rel.source() == modelClass) {
                    ModelClass dest = rel.destination();
                    ModelPackage destPackage = dest.modelPackage();
                    if (destPackage != null) {
                        if (destPackage != this && !_dependencyPackages.contains(destPackage)) {
                            _dependencyPackages.add(destPackage);
                        }
                    }
                } else {
                    ModelClass src = rel.source();
                    ModelPackage srcPackage = src.modelPackage();
                    if (srcPackage != null) {
                        if (srcPackage != this && !_dependentPackages.contains(srcPackage)) {
                            _dependentPackages.add(srcPackage);
                        }
                    }
                }
            }
        }
    }

    private final Model _model;
    private final PackageDoc _packageDoc;
    private final List<ModelClass> _classes = new ArrayList<>();
    private final List<ModelPackage> _dependentPackages = new ArrayList<>();
    private final List<ModelPackage> _dependencyPackages = new ArrayList<>();
}