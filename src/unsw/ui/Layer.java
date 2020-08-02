package unsw.ui;

public enum Layer {
    FLYABLE(0), // Gnome
    THROUGHABLE(1), // Exit, Door
    MOVEABLE(2), // Player, Enemy, hound, boulder
    PICKUPABLE(3), // Potion, Medicine, Sword, Key, Treasure
    STATIC(4), // Wall, Portal, Switch
    GROUND(5);

    private final int z;

    private Layer(int z) {
        this.z = z;
    }

    public int getZIndex() {
        return z;
    }
}