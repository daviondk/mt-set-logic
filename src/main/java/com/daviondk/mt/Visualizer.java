package com.daviondk.mt;

import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static guru.nidi.graphviz.attribute.Rank.RankDir.TOP_TO_BOTTOM;
import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;

public class Visualizer {

    public Visualizer() {
    }

    public void visualize(Tree tree, String path, int imageHeight) {
        AtomicLong nodeId = new AtomicLong(0);
        Graph g = graph("example1").directed()
                .graphAttr().with(Rank.dir(TOP_TO_BOTTOM))
                .with(traverse(tree, nodeId));

        try {
            Graphviz.fromGraph(g).height(imageHeight).render(Format.PNG).toFile(new File(path));
        } catch (IOException e) {
            System.err.println("Error while generating image for graph: " + e.getMessage());
        }
    }

    private Node traverse(Tree tree, AtomicLong nodeId) {
        Node root = node(String.format("%s_%d", tree.getName(), nodeId.get()));

        List<Node> childrenNodes = new ArrayList<>();
        for (Tree child : tree.getChildren()) {
            nodeId.incrementAndGet();
            childrenNodes.add(traverse(child, nodeId));
        }
        root = root.link(childrenNodes);

        return root;
    }
}
