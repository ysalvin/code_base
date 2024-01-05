// the square class
public class Square extends Shape {
    public void setVertices(double d) {
        this.xLocal=new double [4];
        this.xLocal={d,d,-d,-d};
        this.yLocal=new double [4];
        this.yLocal={d,-d,-d,d};
    }
}