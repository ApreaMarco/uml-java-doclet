package it.edu.marconi.umljavadoclet.model;

import java.util.ArrayList;
import java.util.List;

public class RelFilter {
    public RelFilter() {
        _rels = new ArrayList<>();
    }

    public RelFilter(List<ModelRel> rels) {
        _rels = rels;
    }

    public ModelRel first() {
        return _rels.size() > 0 ? _rels.get(0) : null;
    }

    public void add(ModelRel rel) {
        _rels.add(rel);
    }

    public RelFilter source(ModelClass source) {
        RelFilter filter = new RelFilter();
        for (ModelRel rel : _rels) {
            if (rel.source() == source) {
                filter.add(rel);
            }
        }
        return filter;
    }

    public RelFilter destination(ModelClass dest) {
        RelFilter filter = new RelFilter();
        for (ModelRel rel : _rels) {
            if (rel.destination() == dest) {
                filter.add(rel);
            }
        }
        return filter;
    }

    public RelFilter kind(ModelRel.Kind kind) {
        RelFilter filter = new RelFilter();
        for (ModelRel rel : _rels) {
            if (rel.kind() == kind) {
                filter.add(rel);
            }
        }
        return filter;
    }

    private final List<ModelRel> _rels;
}