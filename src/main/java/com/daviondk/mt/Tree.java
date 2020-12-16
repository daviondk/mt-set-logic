package com.daviondk.mt;

import java.util.Collections;
import java.util.List;

public class Tree {

    private final String name;
    private final List<Tree> children;

    public Tree(String name, List<Tree> children) {
        this.name = name;
        this.children = children;
    }

    public Tree(String name) {
        this.name = name;
        children = Collections.emptyList();
    }

    public String getName() {
        return name;
    }

    public List<Tree> getChildren() {
        return children;
    }

}
