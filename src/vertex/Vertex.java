package vertex;

import java.util.*;

/**
 * Created by jeremy on 11/9/16.
 */
public class Vertex implements Comparable<Vertex> {
    private int label;

    private Set<Vertex> neighbors;

    private boolean isOriented;

    public Vertex(int label) {
        this.label = label;
        neighbors = new HashSet<>();
    }

    public Set<Vertex> getNeighbors() {
        return neighbors;
    }


    public int getLabel() {
        return this.label;
    }

    public void addVertex(Vertex vertex) {
        neighbors.add(vertex);
    }

    public int hashCode() {
        return label;
    }

    public boolean equals(Object other) {
        return this.label == ((Vertex) other).label;
    }

    public String toString() {
        String output = label + " Neighbors: ";

        int arr[] = new int[neighbors.size()];
        int i = 0;

        for (Vertex v : neighbors) {
            arr[i] = v.label;
            i++;
        }

        output += Arrays.toString(arr);

        return output;
    }

    @Override
    public int compareTo(Vertex vertex) {
        return this.label - vertex.label;
    }
}
