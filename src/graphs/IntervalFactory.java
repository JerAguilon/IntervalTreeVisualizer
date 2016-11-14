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
        Map<Integer, Integer> islandIndices = new HashMap<>();

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

                    /*int newEnd = islandIndices.get(endInd + 1);

                    islandIndices.remove(endInd + 1);
                    islandIndices.put(startInd, newEnd);*/

                    skipKeys.add(endInd + 1);
                } else if (startInd != 0 && !skipKeys.contains(startInd)) {
                    Collections.swap(list, startInd, startInd - 1);
                }

            }
        }

        System.out.println("FIXED Number of islands: " +  islandIndices.size());
        System.out.println(list);
        return list;
    }

    public static void main(String[] args) {
        createIntervalList(6);
    }


    public static Set<Vertex> createComplement(Set<Vertex> vertexList) {
        Set<Vertex> output = new TreeSet<>();

        for (Vertex v : vertexList) {
            Set<Vertex> neighbors = v.getNeighbors();

            Set<Vertex> completeSet = new HashSet<>();

            for (int i = 0 ; i < vertexList.size(); i++) {
                completeSet.add(new Vertex(i));
            }

            completeSet.removeAll(neighbors);

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
}
