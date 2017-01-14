package lab5.serverSide;

import lab5.clientSide.Point;

public class CheckPointInArea {
    private float radius;

    public CheckPointInArea(float _r) {
        radius = _r;
    }

    public int pointBelongToTheArea(Point point) {
        return (((point.getX() >= -this.radius) && (point.getX() <= 0)) && ((point.getY() >= -this.radius) && (point.getY() <= 0)) && ((Math.pow(point.getX(), 2) + Math.pow(point.getY(), 2) <= (Math.pow(this.radius, 2)))) ||
                ((point.getX() <= this.radius) && (point.getX() >= 0) && (point.getY() <= this.radius / 2.0) && (point.getY() >= 0)) ||
                ((point.getX() >= -this.radius) && (point.getX() <= 0) && (point.getY() <= this.radius) && (point.getY() >= 0) && (point.getY() <= point.getX() + this.radius)))
                ? 1 : -1;
    }
}
