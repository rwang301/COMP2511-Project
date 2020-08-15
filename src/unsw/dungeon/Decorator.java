package unsw.dungeon;

public abstract class Decorator extends Strategy {

    Strategy strategy;

    Decorator(Dungeon dungeon, Character character) {
        super(dungeon, character);
    }

    @Override
    abstract void move();
}