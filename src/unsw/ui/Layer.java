package unsw.ui;

public enum Layer {
    PORTAL(0),
    FLYABLE(1),
    DOOR(2),
    MOVEABLE(3),
    PICKUPABLE(4),
    SWITCH(5);

    private final int z;

    private Layer(int z) {
        this.z = z;
    }

    public int getZIndex() {
        return z;
    }
}