/**
 * The Square class is used to model square
 */

public class Square extends Shape {

    /**
     * a public void method for setting the local coordinates of the vertices of a
     * square.
     * For Triange, the center is (0,0) in local coordinates, so its vertices should
     * be (d,d), (d,-d), (-d,-d), (-d,d).
     * The value is stored in the xLocal and yLocal array
     * 
     * @param d the distance from the center of the square to any of its vertices.
     */

    public void setVertices(double d) {
        xLocal = new double[] { d, d, -d, -d };
        yLocal = new double[] { d, -d, -d, d };
    }
}