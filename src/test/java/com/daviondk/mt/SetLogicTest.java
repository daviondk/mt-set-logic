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
}
