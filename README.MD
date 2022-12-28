## Overview ##

Add UML diagrams (using PlantUML) to Javadocs. Extends the standard Java doclet.

### JavaDoc from Doclet (using this tool) Examples ###

You can find some generated JavaDoc, using this tool (UML-Java-Doclet) from simple java sourcecode
examples, [here](https://github.com/ApreaMarco/examples_uml-java-doclet).

1. Inheritances, Aggregation, Relationships and Cardinality

### Generating Updated Javadocs ###

To generate UML diagrams for your own project, add the following to your POM:

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    </properties>
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-javadoc-plugin</artifactId>
                <version>3.4.1</version>
                <configuration>
                    <doclet>it.edu.marconi.umljavadoclet.UmlJavaDoclet</doclet>
                    <docletArtifact>
                        <groupId>com.github.ApreaMarco</groupId>
                        <artifactId>uml-java-doclet</artifactId>
                        <version>master-SNAPSHOT</version>
                    </docletArtifact>
                    <useStandardDocletOptions>true</useStandardDocletOptions>
                    <additionalOptions>
                        <additionalOption>-output-model true</additionalOption>
                        <!-- Specify each diagram option here as an additionOption tag. -->
                    </additionalOptions>
                </configuration>
            </plugin>
        </plugins>
    </build>

### Diagram Options ###

Options for the diagrams are specified as `additionalOption` tags in the POM.

| Option             | Valid Values                     | Default | Description                                                       |
|--------------------|----------------------------------|---------|-------------------------------------------------------------------|
| -linetype          | polyline,spline,ortho            | ortho   | Types of lines to display on diagrams                             |
| -dependencies      | public,protected,package,private | public  | What dependencies to explicitly show on the diagram               |
| -output-model      | true,false                       | false   | Whether to output the details of the model (useful for debugging) |
| -puml-include-file | free-form                        | none    | Name of PUML file to include in every diagram PUML                |
| -exclude-classes   | comma-separated                  | none    | List of qualified class names to exclude from context diagrams    |
| -exclude-packages  | comma-separated                  | none    | List of qualified package names to exclude from context diagrams  |
