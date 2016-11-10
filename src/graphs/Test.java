package graphs;

import javax.swing.JFrame;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import vertex.Vertex;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Test extends JFrame
{

    /**
     *
     */
    private static final long serialVersionUID = -2707712944901661771L;

    public Test()
    {
        super("Hello, World!");

        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();

        Set<Vertex> intervalGraph = IntervalFactory.createGraph(4);


        int sideLength = 20;
        int x = 20;
        int y = 20;

        graph.getModel().beginUpdate();
        try
        {
            Set<Object> vertices = new HashSet<>();

            for (Vertex v : intervalGraph) {
                Object newVertex = graph.insertVertex(parent, Integer.toString(v.getLabel()), v.getLabel(), x, y, sideLength, sideLength);
                vertices.add(newVertex);
                x += 35;
            }

            for (Vertex v : intervalGraph) {
                Set<Vertex> neighbors = v.getNeighbors();

                int v_value = v.getLabel();
                for (Vertex u : neighbors) {
                    int u_value = u.getLabel();

                    List<Object> pairs = new ArrayList<>(2);
                    for (Object o : vertices) {
                        mxCell castedO = (mxCell) o;

                        if (castedO.getValue().equals(v_value)) {
                            pairs.add(castedO);
                        } else if (castedO.getValue().equals(u_value)) {
                            pairs.add(castedO);
                        }

                        System.out.println(pairs);

                        graph.insertEdge(parent, null, "edge" , pairs.get(0), pairs.get(1));

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
        getContentPane().add(graphComponent);
    }

    public static void main(String[] args)
    {
        Test frame = new Test();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 320);
        frame.setVisible(true);
    }

}