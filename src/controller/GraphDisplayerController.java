package controller;

import exception.PosetException;
import graphs.Edge;
import graphs.GraphVisualizer;
import graphs.IntervalFactory;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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


    Set<Vertex> complement;

    Map<Edge, Edge> edgeMap = new HashMap<>();

    @FXML
    public void initialize() {
        /*Integer[] arr = new Integer[] {3, 5, 1, 7, 3, 10, 5, 7, 2, 4, 4, 9, 8, 1, 9, 6, 10, 2, 6, 8};

        List<Integer> list = Arrays.asList(arr);*/

        Set<Vertex> graph = IntervalFactory.createGraph(100);
        complement = IntervalFactory.createComplement(graph);
        createUndirectedGraph(graphDisplay, graph);
        createUndirectedGraph(complementDisplay, complement);
        createDirectedGraph(directedDisplay, complement);
        createPosetGraph(posetDisplay, complement);
        Set<Edge> edgeSet = IntervalFactory.createOrientation(complement);
        for (Edge e : edgeSet) {
            edgeMap.put(e, e);
        }

        if (complement == null || complement.isEmpty()) {
            return;
        }





    }

    @FXML
    public void find22() {

        /*Set<Vertex> complement = new HashSet<>();
        Vertex v1 = new Vertex(1);
        v1.getNeighbors().add(new Vertex(2));
        //v1.getNeighbors().add(new Vertex(5));

        Vertex v2 = new Vertex(2);
        v2.getNeighbors().add(new Vertex(1));

        Vertex v3 = new Vertex(3);
        v3.getNeighbors().add(new Vertex(4));
        //v3.getNeighbors().add(new Vertex(5));

        Vertex v4 = new Vertex(4);
        v4.getNeighbors().add(new Vertex(3));

        *//*Vertex v5 = new Vertex(5);
        v5.getNeighbors().add(new Vertex(1));
        v5.getNeighbors().add(new Vertex(3));*//*

        complement.add(v1);
        complement.add(v2);
        complement.add(v3);
        complement.add(v4);
        //complement.add(v5);

        Edge e1 = new Edge(v1, v2);
        e1.orient(v1);

        Edge e2 = new Edge(v3, v4);
        e2.orient(v3);

        *//*Edge e3 = new Edge(v1, v5);
        e3.orient(v5);

        Edge e4 = new Edge(v3, v5);
        e4.orient(v5);*//*

        Map<Edge, Edge> edgeMap = new HashMap<>();

        edgeMap.put(e1, e1);
        edgeMap.put(e2, e2);
        *//*edgeMap.put(e3,e3);
        edgeMap.put(e4,e4);*/

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Find a 2+2");
        alert.setHeaderText("This is a non-polynomial algorithm");
        alert.setContentText("I can't guarantee that this will halt in due time.\nI don't recommend this for |V| > 75. Are you ok \nwith proceeding?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != ButtonType.OK){
            return;
        }

        Alert resultMsg = new Alert(Alert.AlertType.INFORMATION);
        resultMsg.setTitle("Find a 2+2");
        try {
            IntervalFactory.findTwoTwo(complement, edgeMap);
        } catch (PosetException e) {
            resultMsg.setHeaderText("Failure: " + e.getMessage());
            resultMsg.showAndWait();
            return;
        }

        resultMsg.setHeaderText("SUCCESS: No 2+2 found.");
        resultMsg.showAndWait();

        System.out.println("SUCCESS");


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
