package graphs;

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
        /*Map<Integer, Integer> islandIndices = new HashMap<>();

        boolean newIsland = true;
        int start = -1;
        Set<Integer> toComplete = new HashSet<>();
        for (int i = 0; i < list.size(); i++) {
            if (newIsland) {
                start = i;
                newIsland = false;
                toComplete.add(list.get(i));
            } else {
                int val = list.get(i);
                if (toComplete.contains(val)) {
                    toComplete.remove(val);
                } else {
                    toComplete.add(val);
                }

                if (toComplete.size() == 0) {
                    islandIndices.put(start, i);
                    newIsland = true;
                }
            }
        }

        System.out.println("Number of islands: " +  islandIndices.size());
        System.out.println(list);
        if (size > 1) {
            Set<Integer> skipKeys = new HashSet<>();

            for (int startInd : islandIndices.keySet()) {
                int endInd = islandIndices.get(startInd);

                if (endInd != list.size() - 1 && !skipKeys.contains(startInd)) {
                    Collections.swap(list, endInd, endInd + 1);

                    *//*int newEnd = islandIndices.get(endInd + 1);

                    islandIndices.remove(endInd + 1);
                    islandIndices.put(startInd, newEnd);*//*

                    skipKeys.add(endInd + 1);
                } else if (startInd != 0 && !skipKeys.contains(startInd)) {
                    Collections.swap(list, startInd, startInd - 1);
                }

            }
        }

        System.out.println("FIXED Number of islands: " +  islandIndices.size());
        System.out.println(list);*/
        return list;
    }

    public static void main(String[] args) {
        int[] input = {1,3,4,3,4,2,2,1};

        ArrayList<Integer> vertexList = new ArrayList<>();

        for (int i : input) {
            vertexList.add(i);
        }

        Set<Vertex> graph = createGraph(vertexList);

        Collection<Edge> edgeList = createOrientation(graph);

        System.out.println(edgeList);

        //System.out.println(createOrientation(createGraph(4)));
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

        Vertex firstVertex = (Vertex) output.toArray()[0];



        System.out.println("Graph created\n\t" + output);

        return output;
    }

    public static Collection<Edge> createOrientation(Set<Vertex> vertices) {
        Map<Edge, Edge> edgeSet = new HashMap<>();

        for (Vertex v : vertices) {
            for (Vertex u : v.getNeighbors()) {
                Edge edge = new Edge(u, v);
                Edge edge2 = new Edge(v, u);
                if (edgeSet.containsKey(edge) || edgeSet.containsKey(edge2)) continue;

                edgeSet.put(new Edge(u, v), new Edge(u, v));
            }
        }

        int orientedEdges = 0;

        while (orientedEdges != edgeSet.size()) {
            //1. arbitrarily orient an edge
            for (Edge edge : edgeSet.values()) {
                Vertex u = edge.u;
                Vertex v = edge.v;

                Set<Vertex> uNeighbors = u.getNeighbors();
                Set<Vertex> vNeighbors = v.getNeighbors();

                boolean uOriented = false;
                boolean vOriented = false;
                boolean arbitrary = true;

                for (Vertex uNeighbor : uNeighbors) {
                    if (uNeighbor.equals(v)) {
                        continue;
                    }

                    boolean shouldSkip = false;
                    for (Vertex vertexIn : vertices) {
                        if (vertexIn.equals(uNeighbor) && vertexIn.getNeighbors().contains(v)) {
                            shouldSkip = true;
                            break;
                        }
                    }

                    if (shouldSkip) continue;

                    Edge neighbor = new Edge(u, uNeighbor);

                    if (edgeSet.get(neighbor).getOrient() != null) {
                        Vertex orientedVertex = edgeSet.get(neighbor).getOrient();

                        if (orientedVertex.equals(u)) {
                            edge.orient(u);
                            arbitrary = false;
                            uOriented = true;
                        } else {
                            edge.orient(v);
                            arbitrary = false;
                            vOriented = true;
                        }
                    }
                }

                for (Vertex vNeighbor : vNeighbors) {
                    if (vNeighbor.equals(v)) {
                        continue;
                    }

                    boolean shouldSkip = false;
                    for (Vertex vertexIn : vertices) {
                        if (vertexIn.equals(vNeighbor) && vertexIn.getNeighbors().contains(u)) {
                            shouldSkip = true;
                            break;
                        }
                    }

                    if(shouldSkip) continue;

                    Edge neighbor = new Edge(v, vNeighbor);

                    if (edgeSet.get(neighbor).getOrient() != null) {
                        Vertex orientedVertex = edgeSet.get(neighbor).getOrient();

                        if (orientedVertex.equals(v)) {
                            edge.orient(v);
                            arbitrary = false;
                            vOriented = true;
                        } else {
                            edge.orient(u);
                            arbitrary = false;
                            uOriented = true;
                        }
                    }
                }

                if (uOriented && vOriented) {
                    throw new RuntimeException(u.getLabel() + " " + v.getLabel());
                }


                if (arbitrary) {
                    edge.orient(u);
                }
                orientedEdges++;
            }
        }
        return edgeSet.values();

    }
}
