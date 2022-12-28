package it.edu.marconi.umljavadoclet.model;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Type;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Model {
    private final RootDoc _rootDoc;
    private final Map<String, ModelClass> _classes = new LinkedHashMap<>();
    private final Map<String, ModelPackage> _packages = new LinkedHashMap<>();

    public Model(RootDoc rootDoc) {
        _rootDoc = rootDoc;
    }

    public void map() {
        mapClasses();
        mapRelationships();
        createPackages();
        mapPackages();
    }

    public List<ModelClass> classes() {
        return new ArrayList<>(_classes.values());
    }

    public List<ModelPackage> packages() {
        return new ArrayList<>(_packages.values());
    }

    public ModelPackage modelPackage(String fullName) {
        return _packages.get(fullName);
    }

    public List<ModelPackage> childPackages(ModelPackage parentPackage) {
        List<ModelPackage> childPackages = new ArrayList<>();
        for (ModelPackage modelPackage : packages()) {
            if (modelPackage.isChildPackage(parentPackage)) {
                childPackages.add(modelPackage);
            }
        }
        return childPackages;
    }

    public List<ModelPackage> rootPackages() {
        List<ModelPackage> rootPackages = new ArrayList<>();
        for (ModelPackage modelPackage : packages()) {
            if (isRootPackage(modelPackage)) {
                rootPackages.add(modelPackage);
            }
        }
        return rootPackages;
    }

    public boolean isRootPackage(ModelPackage modelPackage) {
        String parentName = modelPackage.parentPackageFullName();
        while (parentName != null) {
            if (modelPackage(parentName) != null) {
                return false;
            }
            if (parentName.lastIndexOf(".") == -1) {
                return true;
            }
            parentName = parentName.substring(0, parentName.lastIndexOf("."));
        }
        return false;
    }

    public ModelClass createClassIfNotExists(Type classType) {
        String fullName = ModelClass.fullName(classType);
        ModelClass modelClass = _classes.get(fullName);
        if (modelClass == null) {
            modelClass = new ModelClass(this, classType, false);
            modelClass.map();
            _classes.put(fullName, modelClass);
        }
        return modelClass;
    }

    private void mapClasses() {
        for (ClassDoc classDoc : _rootDoc.classes()) {
            ModelClass modelClass = new ModelClass(this, classDoc, true);
            String fullName = ModelClass.fullName(classDoc);
            _classes.put(fullName, modelClass);
        }
    }

    private void mapRelationships() {
        for (ClassDoc classDoc : _rootDoc.classes()) {
            String fullName = ModelClass.fullName(classDoc);
            ModelClass modelClass = _classes.get(fullName);
            modelClass.map();
        }
    }

    private void createPackages() {
        for (ClassDoc classDoc : _rootDoc.classes()) {
            PackageDoc packageDoc = classDoc.containingPackage();
            String fullName = ModelPackage.fullName(packageDoc);
            ModelPackage modelPackage = _packages.get(fullName);
            if (modelPackage == null) {
                modelPackage = new ModelPackage(this, packageDoc);
                _packages.put(fullName, modelPackage);
            }
            String classFullName = ModelClass.fullName(classDoc);
            ModelClass modelClass = _classes.get(classFullName);
            modelPackage.addClass(modelClass);
        }
    }

    public void mapPackages() {
        for (ModelPackage modelPackage : _packages.values()) {
            modelPackage.map();
        }
    }
}