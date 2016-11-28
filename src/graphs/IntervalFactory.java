package graphs;

import exception.PosetException;
import vertex.PosetVertex;
import vertex.Vertex;

import java.security.InvalidParameterException;
import java.util.*;

/**
 * Created by jeremy on 11/8/16.
 */
public class IntervalFactory {


    public static List<Integer> createIntervalList(int size) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            for (int j = 1; j <= size; j++) {
                list.add(j);
            }
        }

        Collections.shuffle(list);
        System.out.println(list);

        return list;
    }

    public static void main(String[] args) throws Exception {

        Set<Vertex> graph = createComplement(createIntervalGraph(50));
        Set<Edge> edgeList = createOrientation(graph);
        Map<Edge, Edge> edgeMap = new HashMap<>();

        for (Edge e : edgeList) {
            edgeMap.put(e, e);
        }

        findTwoTwo(graph, edgeMap);
        System.out.println("SUCCESS");


        System.out.println(edgeList);


    }

    public static void findTwoTwo(Set<Vertex> unchangedVertexSet, Map<Edge, Edge> edgeSet) throws PosetException {

        System.out.println("Finding a 2-2. Warning: this a non-polynomial algorithm");
        Set<PosetVertex> vertexSet = vertexToPoset(unchangedVertexSet, edgeSet);

        Map<PosetVertex, PosetVertex> vertexMap = new HashMap<>();
        for (PosetVertex v : vertexSet) {
            vertexMap.put(v, v);
        }


        List<PosetVertex> vertexList = new ArrayList<>(vertexSet);

        for (int i = 0 ; i < vertexList.size(); i++) {
            PosetVertex vertex1 = vertexList.get(i);

            if (vertex1.upSet.isEmpty() && vertex1.downSet.isEmpty()) {
                continue;
            }
            for (int j = i + 1; j < vertexList.size(); j++) {
                PosetVertex vertex2 = vertexList.get(2);

                if (vertex1.upSet.contains(vertex1) || vertex1.downSet.contains(vertex2)
                        || vertex2.upSet.contains(vertex1) || vertex2.downSet.contains(vertex1)) {
                    continue;
                }

                if (vertex2.upSet.isEmpty() && vertex2.downSet.isEmpty()) {
                    continue;
                }

                boolean result;
                for (PosetVertex vertex1_1 : vertex1.upSet) {
                    for (PosetVertex vertex2_1 : vertex2.upSet) {
                        result = checkTwoTwo(vertex1, vertex1_1, vertex2, vertex2_1, vertexMap);
                        if (!result) {
                            throw new PosetException(vertex1, vertex1_1, vertex2, vertex2_1);
                        }
                    }
                }



                for (PosetVertex vertex1_1 : vertex1.downSet) {
                    for (PosetVertex vertex2_1 : vertex2.downSet) {
                        result = checkTwoTwo(vertex1, vertex1_1, vertex2, vertex2_1, vertexMap);
                        if (!result) {
                            throw new PosetException(vertex1, vertex1_1, vertex2, vertex2_1);
                        }
                    }
                }

                for (PosetVertex vertex1_1 : vertex1.upSet) {
                    for (PosetVertex vertex2_1 : vertex2.downSet) {
                        result = checkTwoTwo(vertex1, vertex1_1, vertex2, vertex2_1, vertexMap);
                        if (!result) {
                            throw new PosetException(vertex1, vertex1_1, vertex2, vertex2_1);
                        }
                    }
                }

                for (PosetVertex vertex1_1 : vertex1.downSet) {
                    for (PosetVertex vertex2_1 : vertex2.upSet) {
                        result = checkTwoTwo(vertex1, vertex1_1, vertex2, vertex2_1, vertexMap);
                        if (!result) {
                            throw new PosetException(vertex1, vertex1_1, vertex2, vertex2_1);
                        }
                    }
                }



            }
        }
    }

    public static boolean checkTwoTwo(PosetVertex vertex1, PosetVertex vertex1_1,
                                   PosetVertex vertex2, PosetVertex vertex2_2,
                                   Map<PosetVertex, PosetVertex> vertexSet) {
        //do bfs on the upmap of vertex1
        Queue<PosetVertex> posetQueue = new LinkedList<>();
        Set<Vertex> visited = new HashSet<>();

        PosetVertex firstVertex;
        if (vertex1.upSet.contains(vertex1_1)) {
            firstVertex = vertex1;
        } else {
            firstVertex = vertex1_1;
        }

        visited.add(firstVertex);
        posetQueue.add(firstVertex);

        boolean found = false;
        while (!posetQueue.isEmpty() && !found) {
            PosetVertex curr = vertexSet.get(posetQueue.remove());
            visited.add(curr);

            if (curr.equals(vertex2) || curr.equals(vertex2_2)) {
                found = true;
                break;
            }

            for (PosetVertex v : curr.upSet) {
                visited.add(curr);
                posetQueue.add(v);

            }
        }

        if (vertex2.upSet.contains(vertex2_2)) {
            firstVertex = vertex2;
        } else {
            firstVertex = vertex2_2;
        }

        visited.add(firstVertex);
        posetQueue.add(firstVertex);

        visited.add(vertex2);
        posetQueue.add(vertex2);
        while (!posetQueue.isEmpty() && !found) {
            PosetVertex curr = vertexSet.get(posetQueue.remove());
            visited.add(curr);

            if (curr.equals(vertex1) || curr.equals(vertex1_1)) {
                found = true;
                break;
            }

            for (PosetVertex v : curr.upSet) {
                visited.add(curr);
                posetQueue.add(v);
            }
        }

        if (!found) {
            System.out.println("EXCEPTION: " + vertex1.getLabel() + " " + vertex1_1.getLabel() + " | "
                + vertex2.getLabel() + " " + vertex2_2.getLabel());
            return false;
        }

        return true;

        //do bfs on the update of vertex2
    }

    private static Set<PosetVertex> vertexToPoset(Set<Vertex> vertexList, Map<Edge, Edge> edgeList) {
        Set<PosetVertex> output = new HashSet<>();
        for (Vertex v : vertexList) {
            Set<Vertex> downSet = new HashSet<>();
            for (Vertex u : v.getNeighbors()) {
                Edge edge = new Edge(u, v);
                edge = edgeList.get(edge);

                if (edge.getOrient().equals(v)) {
                    downSet.add(u);
                }
            }

            v.getNeighbors().removeAll(downSet);

            Set<Vertex> upSet = v.getNeighbors();

            Set<PosetVertex> posetUpSet = new HashSet<>();

            Set<PosetVertex> posetDownSet = new HashSet<>();

            for (Vertex up : upSet) {
                posetUpSet.add(new PosetVertex(up.getLabel()));
            }

            for (Vertex down : downSet) {
                posetDownSet.add(new PosetVertex(down.getLabel()));
            }

            output.add(new PosetVertex(v.getLabel(), posetUpSet, posetDownSet));
        }
        return output;
    }


    public static Set<Vertex> createComplement(Set<Vertex> vertexList) {
        Set<Vertex> output = new TreeSet<>();

        for (Vertex v : vertexList) {
            Set<Vertex> neighbors = v.getNeighbors();

            Set<Vertex> completeSet = new HashSet<>();

            for (int i = 1 ; i <= vertexList.size(); i++) {
                completeSet.add(new Vertex(i));
            }

            completeSet.removeAll(neighbors);
            completeSet.remove(v);

            Vertex newVertex = new Vertex(v.getLabel());

            for (Vertex complementNeighbor : completeSet) {
                newVertex.addVertex(complementNeighbor);
            }

            output.add(newVertex);
        }




        System.out.println("Complement graph created\n\t" + output);

        return output;
    }

    public static Set<Vertex> createGraph(int size, GraphOptions option) {
        if (option.equals(GraphOptions.INTERVAL)) return createIntervalGraph(size);
        if (option.equals(GraphOptions.RANDOM)) return createRandomGraph(size);
        if (option.equals(GraphOptions.FIFTYFIFTY)) {
            int result = new Random().nextInt(2);

            if (result == 0) {
                return createIntervalGraph(size);
            } else {
                return createRandomGraph(size);
            }
        }

        throw new InvalidParameterException("Can't have any other options!");
    }

    public static Set<Vertex> createRandomGraph(int size) {
        int[][] arrRep = new int[size][size];

        int problemStart = 1;
        Random random = new Random();
        for (int i = 0; i < arrRep.length; i++) {
            for (int j = problemStart; j < arrRep.length; j++) {
                arrRep[i][j]= random.nextInt(2);
            }

            problemStart++;
        }

        Map<Integer, Vertex> vertexMap = new HashMap<>();

        for (int i = 1; i <= size; i++) {
            vertexMap.put(i, new Vertex(i));
        }

        problemStart = 1;

        for (int i = 0; i < arrRep.length; i++) {
            for (int j = problemStart; j < arrRep.length; j++) {
                if (arrRep[i][j] == 1) {
                    vertexMap.get(i).getNeighbors().add(new Vertex(j));
                    vertexMap.get(j).getNeighbors().add(new Vertex(i));
                }
            }

            problemStart++;
        }

        return new HashSet<>(vertexMap.values());
    }

    public static Set<Vertex> createIntervalGraph(int size) {
        return createIntervalGraph(createIntervalList(size));
    }

    public static Set<Vertex> createIntervalGraph(List<Integer> intervalList) {
        Set<Vertex> output = new TreeSet<>();
        TreeSet<Vertex> activeVertices = new TreeSet<>();
        for (int i : intervalList) {
            Vertex v = new Vertex(i);
            if (activeVertices.contains(v)) {
                activeVertices.remove(v);
                continue;
            }

            for (Vertex vSet : activeVertices) {
                vSet.addVertex(new Vertex(i));
                v.addVertex(vSet);
            }

            activeVertices.add(v);
            output.add(v);
        }



        System.out.println("Graph created\n\t" + output);

        return output;
    }

    public static Set<Edge> createOrientation(Set<Vertex> vertices) {
        Map<Edge, Edge> edgeSet = new HashMap<>();
        Map<Vertex, Vertex> vertexSet = new HashMap<>();

        for (Vertex v : vertices) {
            for (Vertex u : v.getNeighbors()) {
                Edge edge = new Edge(u, v);
                Edge edge2 = new Edge(v, u);
                if (edgeSet.containsKey(edge) || edgeSet.containsKey(edge2)) continue;

                edgeSet.put(new Edge(u, v), new Edge(u, v));
            }
        }

        for (Vertex v : vertices) {
            vertexSet.put(v, v);
        }

        int orientedEdges = 0;
        Queue<Edge> edgeQueue = new LinkedList<>();

        while (orientedEdges != edgeSet.size()) {
            Edge foundEdge = null;

            for (Edge edge : edgeSet.values()) {
                if (edgeSet.get(edge).getOrient() == null) {
                    foundEdge = edge;
                    break;
                }
            }

            if (foundEdge == null) break;

            edgeQueue.add(foundEdge);
            while (!edgeQueue.isEmpty()) {
                Edge currentEdge = edgeQueue.remove();

                currentEdge = edgeSet.get(currentEdge);

                Set<Vertex> uNeighbors = vertexSet.get(currentEdge.u).getNeighbors();
                Set<Vertex> vNeighbors = vertexSet.get(currentEdge.v).getNeighbors();

                boolean pointU = false;
                boolean pointV = false;

                for (Vertex uNeighbor : uNeighbors) {
                    if (uNeighbor.equals(currentEdge.v)) continue;

                    Edge neighborEdgeKey = new Edge(currentEdge.u, uNeighbor);
                    Edge neighborEdgeValue = edgeSet.get(neighborEdgeKey);

                    Set<Vertex> extraneousNeighbors = vertexSet.get(uNeighbor).getNeighbors();

                    if (extraneousNeighbors.contains(currentEdge.v)) continue;

                    if (neighborEdgeValue.getOrient() != null) {
                        if (neighborEdgeValue.getOrient().equals(currentEdge.u)) {
                            pointU = true;
                        } else {
                            pointV = true;
                        }
                    } else  {
                        edgeQueue.add(neighborEdgeValue);
                    }
                }

                for (Vertex vNeighbor : vNeighbors) {
                    if (vNeighbor.equals(currentEdge.u)) continue;

                    Edge neighborEdgekey = new Edge(currentEdge.v, vNeighbor);
                    Edge neighborEdgeValue = edgeSet.get(neighborEdgekey);

                    Set<Vertex> extraneousNeighbors = vertexSet.get(vNeighbor).getNeighbors();

                    if (extraneousNeighbors.contains(currentEdge.u)) continue;

                    if (neighborEdgeValue.getOrient() != null) {
                        if (neighborEdgeValue.getOrient().equals(currentEdge.v)) {
                            pointV = true;
                        } else {
                            pointU = true;
                        }
                    } else {
                        edgeQueue.add(neighborEdgeValue);
                    }
                }

                if (pointU) {
                    currentEdge.orient(currentEdge.u);
                }
                if (pointV) {
                    currentEdge.orient(currentEdge.v);
                }

                if (!pointU && !pointV) {
                    currentEdge.orient(currentEdge.u);
                }

                if (pointU && pointV) {
                    throw new RuntimeException(currentEdge.u.toString() + " " + currentEdge.v.toString());
                }

                orientedEdges++;
            }
        }
        System.out.println("UNORIENTATED EDGES: ");

        for (Edge e : edgeSet.values()) {
            if (e.getOrient() == null) {
                System.out.println(e);
            }
        }

        Set<Edge> output = new HashSet<>(edgeSet.values());

        return output;

    }

    public static Map<Integer, Set<Integer>> createLevelList(Set<Vertex> vertices, Set<Edge> edges) {
        List<Vertex> topoList = topologicalSort(vertices, edges);
        Map<Edge, Edge> edgeSet = new LinkedHashMap<>();
        Map<Vertex, Vertex> vertexSet = new HashMap<>();

        for (Edge edge : edges) {
            edgeSet.put(edge, edge);
        }
        for (Vertex v : vertices) {
            vertexSet.put(v, v);
        }

        Map<Vertex, Integer> levelMap = new HashMap<>();




        for (int i = 0; i < vertices.size(); i++) {
            Vertex currentVertex = topoList.get(i);
            int level = -1;
            for (int j = 0; j < i; j++) {
                Vertex previousVertex = vertexSet.get(topoList.get(j));

                if (previousVertex.getNeighbors().contains(currentVertex)) {
                    Edge e = new Edge (currentVertex, previousVertex);
                    e = edgeSet.get(e);

                    if (e.getOrient().equals(currentVertex)) {
                        level = Math.max(level, levelMap.get(previousVertex));

                    }

                    for (Vertex previousNeighbor : previousVertex.getNeighbors()) {
                        if (!currentVertex.getNeighbors().contains(previousNeighbor)) {
                            continue;
                        }

                        Edge previousEdge = edgeSet.get(new Edge(previousVertex, previousNeighbor));
                        Edge currentEdge = edgeSet.get(new Edge(currentVertex, previousNeighbor));

                        if (previousEdge.getOrient().equals(currentEdge.getOrient())) {
                            edges.remove(previousEdge);
                        }
                    }
                }

            }

            level++;

            levelMap.put(currentVertex, level);
        }



        Map<Integer, Set<Integer>> levelList = new HashMap<>();

        for (Vertex v : levelMap.keySet()) {
            int level = levelMap.get(v);

            if (levelList.containsKey(level)) {
                levelList.get(level).add(v.getLabel());
            } else {
                Set<Integer> set = new HashSet<>();

                set.add(v.getLabel());
                levelList.put(level, set);
            }
        }

        return levelList;
    }

    private static List<Vertex> topologicalSort(Set<Vertex> intervalGraph, Set<Edge> orientation) {

        Map<Edge, Edge> edgeSet = new HashMap<>();

        for (Edge edge : orientation) {
            edgeSet.put(edge, edge);
        }

        Map<Vertex, Vertex> vertexSet = new HashMap<>();

        for (Vertex v : intervalGraph) {
            vertexSet.put(v, v);
        }


        Set<Vertex> visitedVertices = new HashSet<>();

        Stack<Vertex> stack = new Stack<>();

        List<Vertex> output = new ArrayList<>();

        for (Vertex v : intervalGraph) {
            recurse(v, edgeSet, vertexSet, visitedVertices, stack);
        }

        while(!stack.isEmpty()) {
            output.add(stack.pop());
        }

        System.out.println("Topological sort: " + output);
        return output;
    }

    private static void recurse(Vertex v, Map<Edge, Edge> edgeSet, Map<Vertex, Vertex> vertexSet, Set<Vertex> visitedSet, Stack<Vertex> stack) {
        if (visitedSet.contains(v)) return;

        visitedSet.add(v);

        v = vertexSet.get(v);

        Set<Vertex> neighbors = v.getNeighbors();
        for (Vertex neighbor : neighbors) {
            Edge edgeKey = new Edge(v, neighbor);

            if (!edgeSet.containsKey(edgeKey)) continue;

            Edge edgeValue = edgeSet.get(edgeKey);

            if (edgeValue.getOrient().equals(v)) continue;

            //we can now recurse on the neighbor
            recurse(neighbor, edgeSet, vertexSet, visitedSet, stack);

        }

        stack.push(v);
    }
}
