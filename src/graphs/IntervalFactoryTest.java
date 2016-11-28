package graphs;

import org.junit.Test;
import vertex.Vertex;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by jeremy on 11/20/16.
 */
public class IntervalFactoryTest {
    @Test
    public void bruteForce22() throws Exception {
        for (int i = 0; i < 500; i++) {
            Set<Vertex> graph = IntervalFactory.createGraph(50, GraphOptions.INTERVAL);
            graph = IntervalFactory.createComplement(graph);

            Map<Edge, Edge> edgeMap = new HashMap<>();
            Set<Edge> edgeSet = IntervalFactory.createOrientation(graph);
            for (Edge e : edgeSet) {
                edgeMap.put(e, e);
            }

            IntervalFactory.findTwoTwo(graph, edgeMap);
        }
    }

}