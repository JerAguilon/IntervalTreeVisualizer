package controller;

import com.sun.corba.se.impl.orbutil.graph.Graph;
import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;
import exception.PosetException;
import graphs.*;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    TextField elementEntry;

    @FXML
    RadioButton interval;

    @FXML
    RadioButton random;

    @FXML
    RadioButton fiftyFifty;

    Set<Vertex> complement;

    Map<Edge, Edge> edgeMap = new HashMap<>();

    ToggleGroup group = new ToggleGroup();

    @FXML
    public void initialize() {

        //group = new ToggleGroup();

        interval.setToggleGroup(group);
        random.setToggleGroup(group);
        fiftyFifty.setToggleGroup(group);
        group.selectToggle(interval);
        createGraph();
    }

    @FXML
    public void createGraph()  {


        int value;
        try {
            value = Integer.parseInt(elementEntry.getText());
        } catch (NumberFormatException e) {
            Alert empty = new Alert(Alert.AlertType.ERROR);
            empty.setTitle("Graph creation");
            empty.setHeaderText("Please enter a valid number in the textfield");
            return;
        }

        if (value > 100) {
            Alert toolarge = new Alert(Alert.AlertType.ERROR);
            toolarge.setTitle("Graph creation");
            toolarge.setHeaderText("Graphs of over 100 vertices are not supported");
            toolarge.show();
            return;
        }

        String choice = ((RadioButton) group.getSelectedToggle()).getText();
        GraphOptions option;

        if (choice.equals("Interval only")) option = GraphOptions.INTERVAL;
        else if (choice.equals("Random graph")) option = GraphOptions.RANDOM;
        else option = GraphOptions.FIFTYFIFTY;

        Set<Vertex> graph = IntervalFactory.createGraph(Integer.parseInt(elementEntry.getText()), option);
        complement = IntervalFactory.createComplement(graph);
        createUndirectedGraph(graphDisplay, graph);
        createUndirectedGraph(complementDisplay, complement);

        createDirectedGraph(directedDisplay, complement);


        createPosetGraph(posetDisplay, complement);
        Set<Edge> edgeSet = IntervalFactory.createOrientation(complement);
        for (Edge e : edgeSet) {
            edgeMap.put(e, e);
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

        if (complement == null || complement.isEmpty()) {
            Alert empty = new Alert(Alert.AlertType.ERROR);
            empty.setTitle("Find a 2+2");
            empty.setHeaderText("Create a graph first.");
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
    private void createDirectedGraph(final SwingNode swingNode, Set<Vertex> vertices)  {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                swingNode.setContent(GraphVisualizer.createDirectedGraph(vertices));

            }
        });



    }
}
