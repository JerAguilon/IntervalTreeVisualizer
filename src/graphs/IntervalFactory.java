package graphs;

import vertex.PosetVertex;
import vertex.Vertex;

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

    public static void main(String[] args) {

        Set<Vertex> graph = createGraph(5);
        System.out.println(graph);
        Set<Edge> edgeList = createOrientation(graph);
        Map<Edge, Edge> edgeMap = new HashMap<>();

        for (Edge e : edgeList) {
            edgeMap.put(e, e);
        }
        System.out.println(edgeList);
        Set<PosetVertex> posetList = vertexToPoset(graph, edgeMap);

        System.out.println(posetList);

    }

    private static void findTwoTwo(Set<PosetVertex> vertexSet) {
        List<Vertex> vertexList = new ArrayList<>(vertexSet);

        for (int i = 0 ; i < vertexList.size(); i++) {
            for (int j = 1; j < vertexList.size(); j++) {

            }
        }
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
    public static Set<Vertex> createGraph(int size) {
        return createGraph(createIntervalList(size));
    }

    public static Set<Vertex> createGraph(List<Integer> intervalList) {
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
        System.out.println(orientedEdges == edgeSet.size());

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

        /*for (int i = 1; i <= topoList.size(); i++) {
            levelList.put(i, 0);
        }
        Set<Vertex> visited = new HashSet<>();
        for (Vertex v : topoList) {
            visited.add(v);
            for (Vertex neighbor : v.getNeighbors()) {
                Edge key = new Edge(v, neighbor);

                Edge value = edgeSet.get(key);

                Vertex orientation = value.getOrient();

                if (visited.contains(orientation)) continue;
                if (levelList.containsKey(neighbor.getLabel())) {
                    levelList.put(neighbor.getLabel(), levelList.get(neighbor.getLabel()) + 1);
                } else {
                    levelList.put(neighbor.getLabel(), 0);
                }
            }
        }

        Map<Integer, Set<Integer>> mappedList = new HashMap<>();

        for (int i : levelList.keySet()) {
            int level = levelList.get(i);

            if ( mappedList.containsKey(level)) {
                mappedList.get(level).add(i);
            } else {
                Set<Integer> newSet = new HashSet<>();

                newSet.add(i);
                mappedList.put(level, newSet);
            }

        }*/

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
