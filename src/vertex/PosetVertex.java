package vertex;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by jeremy on 11/17/16.
 */
public class PosetVertex extends Vertex {

    public Set<PosetVertex> upSet = new HashSet<>();
    public Set<PosetVertex> downSet = new HashSet<>();
    public PosetVertex (int label, Set<PosetVertex> upSet, Set<PosetVertex> downSet) {
        super(label);

        this.upSet = upSet;
        this.downSet = downSet;
    }

    public PosetVertex (int label) {
        super(label);

    }

    public String toString() {
        if (upSet.size() == 0 && downSet.size() == 0) {
            return String.format("Label %d", this.getLabel());
        }

        return String.format("Label: %d | upSet: %s downSet: %s", this.getLabel(), upSet.toString(), downSet.toString());
    }




}
