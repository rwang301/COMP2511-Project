package unsw.dungeon;

public abstract class Decorator extends Strategy {

    private Strategy strategy;

    Decorator(Dungeon dungeon, Character character, Strategy strategy) {
        super(dungeon, character);
        this.strategy = strategy;
    }

    @Override
    void move() {
        strategy.move();
    }

}