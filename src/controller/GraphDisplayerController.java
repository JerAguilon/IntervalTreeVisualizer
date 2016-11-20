package controller;

import graphs.Edge;
import graphs.GraphVisualizer;
import graphs.IntervalFactory;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import vertex.Vertex;

import javax.swing.*;
import java.util.*;

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
        /*Integer[] arr = new Integer[] {8, 7, 8, 2, 1, 6, 5, 9, 2, 3, 1, 10, 7, 4, 4, 5, 10, 9, 3, 6};

        List<Integer> list = Arrays.asList(arr);*/

        Set<Vertex> graph = IntervalFactory.createGraph(25);
        Set<Vertex> complement = IntervalFactory.createComplement(graph);
        createUndirectedGraph(graphDisplay, graph);
        createUndirectedGraph(complementDisplay, complement);
        createDirectedGraph(directedDisplay, complement);
        createPosetGraph(posetDisplay, complement);
        Set<Edge> edgeSet = IntervalFactory.createOrientation(complement);
        Map<Edge, Edge> edgeMap = new HashMap<>();
        for (Edge e : edgeSet) {
            edgeMap.put(e, e);
        }

        /*try {
            IntervalFactory.findTwoTwo(complement,edgeMap);
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }*/
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
