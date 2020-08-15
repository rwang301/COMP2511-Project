package unsw.dungeon;

public abstract class Character extends Entity implements Observer {
    final int range = 10;
    Strategy strategy;

    Character(int x, int y) {
        super(x, y);
    }

    abstract int getRange();

    abstract void initialise(Player player);
    abstract void collide(Player player);
}