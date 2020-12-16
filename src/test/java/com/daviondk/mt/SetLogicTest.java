package com.daviondk.mt;

import org.junit.Test;

public class SetLogicTest {

    private final Parser parser = new Parser();
    private final Visualizer visualizer = new Visualizer();

    @Test
    public void test() {
        Tree tree = parser.parse("a and (b or c)");
        visualizer.visualize(tree, "test.png", 1000);
    }

    @Test
    public void and() {
        Tree tree = parser.parse("a and b");
        visualizer.visualize(tree, "and.png", 1000);
    }

    @Test
    public void or() {
        Tree tree = parser.parse("a or b");
        visualizer.visualize(tree, "or.png", 1000);
    }

    @Test
    public void in() {
        Tree tree = parser.parse("a in b");
        visualizer.visualize(tree, "in.png", 1000);
    }

    @Test
    public void notIn() {
        Tree tree = parser.parse("a not in b");
        visualizer.visualize(tree, "notIn.png", 1000);
    }

    @Test
    public void notInNot() {
        Tree tree = parser.parse("a not in not b");
        visualizer.visualize(tree, "notInNot.png", 1000);
    }

    @Test
    public void priority() {
        Tree tree = parser.parse("a xor b and c in not (d or e)");
        visualizer.visualize(tree, "priority.png", 1000);
    }
}
