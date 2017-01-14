package lab5.clientSide;

public class Point {
    private float X;
    private float Y;

    public Point(float _x, float _y) {
        setX(_x);
        setY(_y);
    }

    public float getX() {
        return X;
    }

    public void setX(float x) {
        X = x;
    }

    public float getY() {
        return Y;
    }

    public void setY(float y) {
        Y = y;
    }
}
