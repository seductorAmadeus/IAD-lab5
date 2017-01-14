package lab5.clientSide;

class StateOfPoints {
    private static int index = 0;
    private final int id;
    private State state = State.POINT_STATE_UNKNOWN;
    private float radius;
    public Point point;

    StateOfPoints(Point point, float radius, State state) {
        id = index++;
        this.point = point;
        this.setRadius(radius);
        this.setState(State.POINT_STATE_UNKNOWN);
    }

    public int getId() {
        return id;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}
