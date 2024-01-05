/**
 * The Triangle class is used to model triangle
 */

public class Triangle extends Shape {

    /**
     * a public void method for setting the local coordinates of the vertices of a
     * shape.
     * For Triange, the center is (0,0) in local coordinates, so its vertices should
     * be (d,0), (-d cos(pi/3), -d sin(pi/3)), (-d cos(pi/3), d sin(pi/3))
     * The value is stored in the xLocal and yLocal array
     * 
     * @param d the distance from the center of the triangle to any of its vertices.
     */

    public void setVertices(double d) {
        xLocal = new double[] { d, (-d * Math.cos(Math.PI / 3)), (-d * Math.cos(Math.PI / 3)) };
        yLocal = new double[] { 0, (-d * Math.sin(Math.PI / 3)), (d * Math.sin(Math.PI / 3)) };
    }
}