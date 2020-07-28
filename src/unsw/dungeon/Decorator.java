package unsw.dungeon;

public abstract class Decorator extends Strategy {

    Decorator(Dungeon dungeon, Gnome gnome) {
        super(dungeon, gnome);
    }

    @Override
    abstract void move();
}