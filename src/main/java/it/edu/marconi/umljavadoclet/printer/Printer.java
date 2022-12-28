package it.edu.marconi.umljavadoclet.printer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public abstract class Printer {
    public boolean toFile(File file) {
        return dumpToFile(file, _sb.toString());
    }

    @Override
    public String toString() {
        return _sb.toString();
    }

    public StringBuilder stringBuilder() {
        return _sb;
    }

    public void print(String str) {
        _sb.append(str);
    }

    public void println(String str) {
        _sb.append(str);
        _sb.append("\n");
    }

    public void println(int level, String str) {
        indent(level);
        _sb.append(str);
        _sb.append("\n");
    }

    public void indent(int level) {
        for (int i = 0; i < level; i++) {
            print("  ");
        }
    }

    public void newline() {
        _sb.append("\n");
    }

    private boolean dumpToFile(File file, String str) {
        try {
            FileOutputStream os = new FileOutputStream(file);
            os.write(str.getBytes());
            os.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private final StringBuilder _sb = new StringBuilder();
}