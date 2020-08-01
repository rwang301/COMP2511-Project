package unsw.ui;

public enum Layer {
    PORTAL(0),
    FLYABLE(1),
    THROUGHABLE(2),
    MOVEABLE(3),
    PICKUPABLE(4),
    STATIC(5),
    GROUND(6);

    private final int z;

    private Layer(int z) {
        this.z = z;
    }

    public int getZIndex() {
        return z;
    }
}