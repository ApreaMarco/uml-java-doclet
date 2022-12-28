package it.edu.marconi.umljavadoclet.printer;

import it.edu.marconi.umljavadoclet.model.Model;
import it.edu.marconi.umljavadoclet.model.ModelClass;
import it.edu.marconi.umljavadoclet.model.ModelPackage;
import it.edu.marconi.umljavadoclet.model.ModelRel;

import java.util.List;

public abstract class PumlDiagramPrinter extends Printer {
    public PumlDiagramPrinter(Model model, DiagramOptions options) {
        _model = model;
        _options = options;
    }

    public Model getModel() {
        return _model;
    }

    public DiagramOptions getOptions() {
        return _options;
    }

    public void start() {
        println("@startuml");
        if (_options.hasPumlIncludeFile()) {
            println("!include " + _options.getPumlIncludeFile());
        }
        newline();
        svglinktargetOption("_parent");
        printLineTypeOption();
    }

    public void noPackagesOption() {
        println("set namespaceSeparator none");
    }

    public void svglinktargetOption(String target) {
        println("skinparam svgLinkTarget " + target);
    }

    public void end() {
        newline();
        println("@enduml");
    }

    public void packageDefinition(ModelPackage modelPackage, String filepath, String color) {
        print("package ");
        print(modelPackage.fullName());
        if (filepath != null && filepath.length() > 0) {
            print(" [[");
            print(filepath);
            print("{" + modelPackage.fullName() + "}");
            print("]]");
        }
        if (color != null && color.length() > 0) {
            print(" #" + color);
        }
        println(" {");
        println("}");
        newline();
    }

    public void classDefinition(ModelClass modelClass,
                                boolean displayPackageName,
                                String filepath,
                                String color,
                                boolean showFields,
                                boolean showConstructors,
                                boolean showMethods,
                                boolean publicMethodsOnly,
                                boolean includeTypeInfo) {
        classDeclaration(modelClass, displayPackageName);
        if (filepath != null && filepath.length() > 0) {
            print(" [[");
            print(filepath);
            print("{" + modelClass.fullNameWithoutParameters() + "}");
            print("]]");
        }
        if (color != null && color.length() > 0) {
            print(" #" + color);
        }
        println(" {");
        if (showFields) {
            for (ModelClass.Field field : modelClass.fields()) {
                field(field, includeTypeInfo);
            }
        }
        if (showConstructors) {
            for (ModelClass.Constructor cons : modelClass.constructors()) {
                if (!publicMethodsOnly || cons.visibility == ModelClass.Visibility.PUBLIC) {
                    constructor(cons, includeTypeInfo);
                }
            }
        }
        if (showMethods) {
            for (ModelClass.Method method : modelClass.methods()) {
                if (!publicMethodsOnly || method.visibility == ModelClass.Visibility.PUBLIC) {
                    method(method, includeTypeInfo);
                }
            }
        }
        println("}");
        newline();
        if (!showFields) {
            hideFields(modelClass);
        }
        if (!showConstructors && !showMethods) {
            hideMethods(modelClass);
        }
        newline();
    }

    public void classDefinitionNoDetail(ModelClass modelClass, boolean displayPackageName, String filepath, String color) {
        classDefinition(modelClass, displayPackageName, filepath, color, false, false, false, false, false);
    }

    public void classDeclaration(ModelClass modelClass, boolean displayPackageName) {
        classType(modelClass);
        print(" \"");
        print("<b><size:14>");
        print(modelClass.shortName());
        print("</b>");
        if (displayPackageName) {
            print("\\n<size:10>");
            print(modelClass.packageName());
        }
        print("\" as ");
        className(modelClass);
        print(" ");
        annotations(modelClass);
    }

    public void annotations(ModelClass modelClass) {
        List<String> annotations = modelClass.annotations();
        if (annotations.size() > 0) {
            for (String annotation : modelClass.annotations()) {
                print("<<");
                print(annotation);
                print(">>");
            }
        }
    }

    public void className(ModelClass modelClass) {
        print(modelClass.fullNameWithoutParameters());
        for (String param : modelClass.parameters()) {
            param = param.replace("<", "").replace(">", "");
            print(param);
        }
    }

    public void classType(ModelClass modelClass) {
        switch (modelClass.type()) {
            case INTERFACE:
                print("interface");
                break;
            case ENUM:
                print("enum");
                break;
            default:
                print("class");
        }
    }

    public void field(ModelClass.Field field, boolean includeTypeInfo) {
        if (field.isStatic) {
            printStatic();
        }
        visibility(field.visibility);
        if (includeTypeInfo) {
            print(field.type + " ");
        }
        print(field.name);
        newline();
    }

    public void visibility(ModelClass.Visibility visibility) {
        switch (visibility) {
            case PUBLIC:
                print("+");
                break;
            case PROTECTED:
                print("#");
                break;
            case PACKAGE_PRIVATE:
                print("~");
                break;
            default:
                print("-");
        }
    }

    public void constructor(ModelClass.Constructor constructor, boolean includeTypeInfo) {
        visibility(constructor.visibility);
        print(constructor.name);
        print("(");
        if (includeTypeInfo) {
            String sep = "";
            for (ModelClass.MethodParameter param : constructor.parameters) {
                print(sep);
                print(param.type);
                print(" ");
                print(param.name);
                sep = ",";
            }
        }
        print(")");
        newline();
    }

    public void method(ModelClass.Method method, boolean includeTypeInfo) {
        if (method.isStatic) {
            printStatic();
        }
        if (method.isAbstract) {
            printAbstract();
        }
        visibility(method.visibility);
        if (includeTypeInfo) {
            print(method.returnType);
            print(" ");
        }
        print(method.name);
        print("(");
        if (includeTypeInfo) {
            String sep = "";
            for (ModelClass.MethodParameter param : method.parameters) {
                print(sep);
                print(param.type);
                print(" ");
                print(param.name);
                sep = ",";
            }
        }
        print(")");
        newline();
    }

    public void hideFields(ModelClass modelClass) {
        print("hide ");
        className(modelClass);
        print(" fields");
        newline();
    }

    public void hideMethods(ModelClass modelClass) {
        print("hide ");
        className(modelClass);
        print(" methods");
        newline();
    }

    public void relationship(ModelRel rel) {
        if (isRelationshipVisible(rel)) {
            switch (rel.kind()) {
                case GENERALIZATION:
                    generalization(rel.source(), rel.destination());
                    break;
                case DEPENDENCY:
                    dependency(rel.source(), rel.destination());
                    break;
                case REALIZATION:
                    realization(rel.source(), rel.destination());
                    break;
                case DIRECTED_ASSOCIATION:
                    aggregation(rel.source(), rel.destination(), rel.destinationRole(), multiplicityLabel(rel.destinationCardinality()), multiplicityLabel(rel.sourceCardinality()));
                    break;
            }
        }
    }

    public boolean isRelationshipVisible(ModelRel rel) {
        if (_options.getDependenciesVisibility() == DiagramOptions.Visibility.PUBLIC) {
            return rel.isVisible(ModelRel.Visibility.PUBLIC);
        } else if (_options.getDependenciesVisibility() == DiagramOptions.Visibility.PROTECTED) {
            return rel.isVisible(ModelRel.Visibility.PROTECTED);
        } else if (_options.getDependenciesVisibility() == DiagramOptions.Visibility.PACKAGE) {
            return rel.isVisible(ModelRel.Visibility.PACKAGE);
        } else if (_options.getDependenciesVisibility() == DiagramOptions.Visibility.PRIVATE) {
            return rel.isVisible(ModelRel.Visibility.PRIVATE);
        }
        return false;
    }

    public void generalization(ModelClass src, ModelClass dest) {
        String relText = "-u-|>";
        printRel(src, relText, dest);
    }

    public void realization(ModelClass src, ModelClass dest) {
        String relText = ".u.|>";
        printRel(src, relText, dest);
    }

    public void dependency(ModelClass src, ModelClass dest) {
        String relText = ".u.>";
        printRel(src, relText, dest);
    }

    public void aggregation(ModelClass src, ModelClass dest, String destRole, String destCardinality, String srcCardinality) {
        String relText = "o-d-";
        printRel(src, relText, dest, destRole, destCardinality, srcCardinality);
    }

    public String multiplicityLabel(ModelRel.Multiplicity mult) {
        String retValue = null;
        if (mult != null) {
            switch (mult) {
                case ONE:
                    retValue = "1";
                    break;
                case ZERO_OR_ONE:
                    retValue = "0..1";
                    break;
                case MANY:
                    retValue = "N";
                    break;
            }
        }
        return retValue;
    }

    private void printAbstract() {
        print("{abstract} ");
    }

    private void printStatic() {
        print("{static} ");
    }

    private void printRel(ModelClass src, String relText, ModelClass dest) {
        className(src);
        print(" " + relText + " ");
        className(dest);
        newline();
    }

    private void printRel(ModelClass src, String relText, ModelClass dest, String destRole, String destCardinality, String srcCardinality) {
        className(src);
        print(" \"" + srcCardinality + "\" " + relText + " \"" + destCardinality + "\" ");
        className(dest);
        print(": \"" + destRole + "\"\n");
    }

    private void printLineTypeOption() {
        switch (_options.getLineType()) {
            case ORTHO:
                println("skinparam linetype ortho");
                break;
            case POLYLINE:
                println("skinparam linetype polyline");
                break;
            case SPLINE:
                println("skinparam linetype spline");
                break;
        }
    }

    public String classFilepath(ModelClass modelClass, ModelClass classFile) {
        return relativePathToRoot(modelClass.packageName()) +
                pathToPackage(classFile.packageName()) +
                classFile.shortNameWithoutParameters() +
                ".html";
    }

    public String classFilepath(ModelPackage modelPackage, ModelClass classFile) {
        return relativePathToRoot(modelPackage.fullName()) +
                pathToPackage(classFile.packageName()) +
                classFile.shortNameWithoutParameters() +
                ".html";
    }

    public String packageFilepath(ModelPackage modelPackage, ModelPackage packageFile) {
        return relativePathToRoot(modelPackage.fullName()) +
                pathToPackage(packageFile.fullName()) +
                "package-summary.html";
    }

    public String classFilepath(ModelClass classFile) {
        return pathToPackage(classFile.packageName()) +
                classFile.shortNameWithoutParameters() +
                ".html";
    }

    public String packageFilepath(ModelPackage packageFile) {
        return pathToPackage(packageFile.fullName()) +
                "package-summary.html";
    }

    public String relativePathToRoot(String packageName) {
        StringBuilder sb = new StringBuilder();
        String[] parts = packageName.split("\\.");
        if (parts.length > 0) {
            for (int i = 0; i < parts.length; i++) {
                sb.append("../");
            }
        }
        return sb.toString();
    }

    public String pathToPackage(String packageName) {
        StringBuilder sb = new StringBuilder();
        String[] parts = packageName.split("\\.");
        for (String part : parts) {
            sb.append(part);
            sb.append("/");
        }
        return sb.toString();
    }

    private final Model _model;
    private final DiagramOptions _options;
}