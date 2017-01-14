package lab5.clientSide;

class StateOfPoints {
    private static int index = 0;
    private final int id;
    public Point point;
    private State state = State.POINT_STATE_UNKNOWN;
    private float radius;

    StateOfPoints(Point point, float radius, State state) {
        id = index++;
        this.point = point;
        this.setRadius(radius);
        this.setState(state);
    }

    public static int getIndex() {
        return index;
    }

    public static void setIndex(int index) {
        StateOfPoints.index = index;
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
