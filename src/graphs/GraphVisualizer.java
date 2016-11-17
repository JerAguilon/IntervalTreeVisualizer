package graphs;

import javax.swing.*;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;
import vertex.Vertex;

import java.util.*;

public class GraphVisualizer
{

    /**
     *
     */
    private static final long serialVersionUID = -2707712944901661771L;

    /*public GraphVisualizer(Set<Vertex> graph)
    {
        super("Hello, World!");

        getContentPane().add(createDirectedGraph(graph));


    }*/

    public static mxGraphComponent createPosetRepresentation(Set<Vertex> intervalGraph) {


        Set<Edge> orientation = IntervalFactory.createOrientation(intervalGraph);

        Map<Integer, Set<Integer>> levels = IntervalFactory.createLevelList(intervalGraph, orientation);
        System.out.println("Levels: "  + levels);
        mxGraph graph = new mxGraph();

        graph.setAllowDanglingEdges(false);
        Object parent = graph.getDefaultParent();

        int maxLevel = 0;
        for (int i : levels.keySet()) {
            if (i > maxLevel) maxLevel = i;
        }
        int sideLength = 20;
        int x = 20;
        int bottomLevel = 50 + 50 * maxLevel;
        graph.getModel().beginUpdate();
        try
        {
            Set<Object> vertices = new HashSet<>();
            for (int thisLevel : levels.keySet()) {
                Set<Integer> verticesOnLevel = levels.get(thisLevel);
                for (int newVertexLabel : verticesOnLevel) {
                    Object newVertexObject = graph.insertVertex(parent, Integer.toString(newVertexLabel), newVertexLabel, x, bottomLevel - 50 * thisLevel, sideLength, sideLength);
                    vertices.add(newVertexObject);
                    x += 100;
                }
                x = 20 + 50 * (thisLevel + 1);
            }

            for (Edge edge : orientation) {
                Vertex u = edge.u;
                Vertex v = edge.v;
                Vertex orientedVertex = edge.getOrient();
                Object uCell = null;
                Object vCell = null;
                for (Object vertex : vertices) {
                    mxCell castedVertex = (mxCell) vertex;

                    if (castedVertex.getValue().equals(u.getLabel())) {
                        uCell = vertex;
                    } else if (castedVertex.getValue().equals(v.getLabel())) {
                        vCell = vertex;
                    }
                }

                if (u.equals(orientedVertex)) {
                    graph.insertEdge(parent, null, "" , vCell, uCell);
                } else if (v.equals(orientedVertex)) {
                    graph.insertEdge(parent, null, "", uCell, vCell);

                }


            }

        }
        finally
        {
            graph.getModel().endUpdate();
        }

        for (Object cell : graph.getSelectionCells()) {
            mxCell castedCell = (mxCell) cell;
            System.out.println(castedCell);
            System.out.println("bar");
            if (castedCell.isVertex()) {
                System.out.println("foo");
                System.out.println(castedCell.getValue());
            }
        }

        mxGraphComponent graphComponent = new mxGraphComponent(graph);

        graphComponent.setConnectable(false);

        return graphComponent;


    }


    public static mxGraphComponent createDirectedGraph(Set<Vertex> intervalGraph) {
        Collection<Edge> orientation = IntervalFactory.createOrientation(intervalGraph);


        mxGraph graph = new mxGraph();

        graph.setAllowDanglingEdges(false);
        Object parent = graph.getDefaultParent();

        int centerXY = 165;
        int width = 150;

        int numberOfVertices = intervalGraph.size();

        double deltaDegree = 360/numberOfVertices;


        int sideLength = 20;
        /*int x = 20;
        int y = 20;*/
        graph.getModel().beginUpdate();
        try
        {
            Set<Object> vertices = new HashSet<>();
            int tracker = 0;
            for (Vertex v : intervalGraph) {
                int x =  centerXY + (int) (width * Math.cos(Math.toRadians(deltaDegree * tracker)));
                int y =  centerXY + (int) (width * Math.sin(Math.toRadians(deltaDegree * tracker)));

                System.out.println(String.format("(%d, %d)", x, y));
                Object newVertex = graph.insertVertex(parent, Integer.toString(v.getLabel()), v.getLabel(), x, y, sideLength, sideLength);
                vertices.add(newVertex);

                tracker++;
            }
            for (Edge edge : orientation) {
                Vertex u = edge.u;
                Vertex v = edge.v;
                Vertex orientedVertex = edge.getOrient();
                Object uCell = null;
                Object vCell = null;
                for (Object vertex : vertices) {
                    mxCell castedVertex = (mxCell) vertex;

                    if (castedVertex.getValue().equals(u.getLabel())) {
                        uCell = vertex;
                    } else if (castedVertex.getValue().equals(v.getLabel())) {
                        vCell = vertex;
                    }
                }

                if (u.equals(orientedVertex)) {
                    graph.insertEdge(parent, null, "" , vCell, uCell);
                } else if (v.equals(orientedVertex)) {
                    graph.insertEdge(parent, null, "", uCell, vCell);

                }


            }



        }
        finally
        {
            graph.getModel().endUpdate();
        }

        for (Object cell : graph.getSelectionCells()) {
            mxCell castedCell = (mxCell) cell;
            System.out.println(castedCell);
            System.out.println("bar");
            if (castedCell.isVertex()) {
                System.out.println("foo");
                System.out.println(castedCell.getValue());
            }
        }

        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        graphComponent.setConnectable(false);

        return graphComponent;
    }

    public static mxGraphComponent createGraph(Set<Vertex> intervalGraph) {
        mxGraph graph = new mxGraph();

        graph.setAllowDanglingEdges(false);
        Object parent = graph.getDefaultParent();

        int centerXY = 165;
        int width = 150;

        int numberOfVertices = intervalGraph.size();

        int deltaDegree = 360/numberOfVertices;


        int sideLength = 20;
        /*int x = 20;
        int y = 20;*/
        graph.getModel().beginUpdate();
        try
        {
            Set<Object> vertices = new HashSet<>();
            int tracker = 0;
            for (Vertex v : intervalGraph) {

                int x =  centerXY + (int) (width * Math.cos(Math.toRadians(deltaDegree * tracker)));
                int y =  centerXY + (int) (width * Math.sin(Math.toRadians(deltaDegree * tracker)));

                System.out.println(String.format("(%d, %d)", x, y));

                Object newVertex = graph.insertVertex(parent, Integer.toString(v.getLabel()), v.getLabel(), x, y, sideLength, sideLength);
                vertices.add(newVertex);

                tracker++;
            }

            Set<Set<Integer>> completedPairs = new HashSet<>();

            for (Vertex v : intervalGraph) {
                Set<Vertex> neighbors = v.getNeighbors();

                int v_value = v.getLabel();
                for (Vertex u : neighbors) {
                    int u_value = u.getLabel();

                    Set<Integer> completedPairSet = new HashSet<>();
                    completedPairSet.add(u_value);
                    completedPairSet.add(v_value);

                    if (completedPairs.contains(completedPairSet)) {
                        continue;
                    }

                    List<Object> pairs = new ArrayList<>(2);
                    for (Object o : vertices) {
                        mxCell castedO = (mxCell) o;

                        if (castedO.getValue().equals(v_value)) {
                            pairs.add(castedO);
                        } else if (castedO.getValue().equals(u_value)) {
                            pairs.add(castedO);
                        }

                    }

                    completedPairs.add(completedPairSet);

                    try {
                        graph.insertEdge(parent, null, "" , pairs.get(0), pairs.get(1));
                    } catch (IndexOutOfBoundsException ex) {
                        System.out.println(completedPairs);
                    }
                }
            }

            /*Object v1 = graph.insertVertex(parent, null, "Hello", 20, 20, 80,
                    30);
            Object v2 = graph.insertVertex(parent, null, "World!", 240, 150,
                    80, 30);
            graph.insertEdge(parent, null, "Edge", v1, v2);*/
        }
        finally
        {
            graph.getModel().endUpdate();
        }

        for (Object cell : graph.getSelectionCells()) {
            mxCell castedCell = (mxCell) cell;
            System.out.println(castedCell);
            System.out.println("bar");
            if (castedCell.isVertex()) {
                System.out.println("foo");
                System.out.println(castedCell.getValue());
            }
        }

        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        mxGraphModel graphModel  = (mxGraphModel)graphComponent.getGraph().getModel();
        Collection<Object> cells =  graphModel.getCells().values();
        mxUtils.setCellStyles(graphComponent.getGraph().getModel(),
                cells.toArray(), mxConstants.STYLE_ENDARROW, mxConstants.NONE);
        graphComponent.setConnectable(false);

        return graphComponent;
    }

    public static void main(String[] args)
    {
        /*Set<Vertex> graph = IntervalFactory.createGraph(3);
        Set<Vertex> complement = IntervalFactory.createComplement(graph);
        GraphVisualizer frame = new GraphVisualizer(graph);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 320);
        frame.setVisible(true);


        GraphVisualizer complementFrame = new GraphVisualizer(complement);
        complementFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        complementFrame.setSize(400, 320);
        complementFrame.setVisible(true);
*/

    }

}