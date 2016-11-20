package exception;

import vertex.PosetVertex;

/**
 * Created by jeremy on 11/20/16.
 */
public class PosetException extends Exception {
    PosetVertex vertex1;
    PosetVertex vertex2;
    PosetVertex vertex3;
    PosetVertex vertex4;

    String msg;

    public PosetException(PosetVertex v1, PosetVertex v2, PosetVertex v3, PosetVertex v4) {
        this.vertex1 = v1;
        this.vertex2 = v2;
        this.vertex3 = v3;
        this.vertex4 = v4;

        this.msg = String.format("2+2 found: %d-%d | %d-%d",
                vertex1.getLabel(), vertex2.getLabel(), vertex3.getLabel(), vertex4.getLabel());
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
