package controller;

import graphs.Edge;
import graphs.GraphVisualizer;
import graphs.IntervalFactory;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import vertex.Vertex;

import javax.swing.*;
import java.util.List;
import java.util.Set;

/**
 * Created by jeremy on 11/15/16.
 */
public class GraphDisplayerController {
    @FXML
    SwingNode graphDisplay;

    @FXML
    SwingNode complementDisplay;

    @FXML
    SwingNode directedDisplay;

    @FXML
    SwingNode posetDisplay;

    @FXML
    public void initialize() {
        Set<Vertex> graph = IntervalFactory.createGraph(8);
        Set<Vertex> complement = IntervalFactory.createComplement(graph);
        createUndirectedGraph(graphDisplay, graph);
        createUndirectedGraph(complementDisplay, complement);
        createDirectedGraph(directedDisplay, complement);
        createPosetGraph(posetDisplay, complement);
    }

    private void createPosetGraph(final SwingNode swingNode, Set<Vertex> vertices) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                swingNode.setContent(GraphVisualizer.createPosetRepresentation(vertices));
            }
        });
    }
    private void createUndirectedGraph(final SwingNode swingNode, Set<Vertex> vertices) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                swingNode.setContent(GraphVisualizer.createGraph(vertices));
            }
        });
    }
    private void createDirectedGraph(final SwingNode swingNode, Set<Vertex> vertices) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                swingNode.setContent(GraphVisualizer.createDirectedGraph(vertices));
            }
        });

    }
}
