public class Triangle extends Shape {
    public void setVertices(double d) {
        this.xLocal = new double[3];
        this.yLocal = new double[3];
        this.xLocal={d,(-d*Math.cos(Math.PI/3)),(-d*Math.cos(Math.PI/3))};
        this.yLocal={0,(-d*Math.sin(Math.PI/3)),(d*Math.sin(Math.PI/3))};
    }
}