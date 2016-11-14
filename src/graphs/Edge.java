package graphs;

import vertex.Vertex;

/**
 * Created by jeremy on 11/14/16.
 */
public class Edge {
    public Vertex u;
    public Vertex v;
    private Vertex orient ;


    public Edge(Vertex u, Vertex v) {
        this.u = u;
        this.v = v;
    }

    public void orient(Vertex vertex) {
        if (vertex.equals(this.v)) {
            orient = v;
        } else if (vertex.equals(this.u)) {
            orient = u;
        } else {
            throw new RuntimeException("Unable to orient vertices.");
        }
    }

    public Vertex getOrient() {
        return orient;
    }

    public int hashCode() {
        return this.u.hashCode() + this.v.hashCode();
    }

    public String toString() {
        String output = "U:" + u.getLabel() + " V:" + v.getLabel() + " Orient:";
        if (orient == null) output += null;
        if (orient == u) output += u.getLabel();
        if (orient == v) output += v.getLabel();

        return output;
    }



    public boolean equals(Object other) {
        Edge that = (Edge) other;

        if ((this.u.equals(that.u) && this.v.equals(that.v)) ||
            (this.u.equals(that.v) && this.v.equals(that.u))) {
            return true;
        }

        return false;
    }




}
