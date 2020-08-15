package unsw.dungeon;

public class MoveTwice extends Decorator {

    MoveTwice(Dungeon dungeon, Character character, Strategy strategy) {
        super(dungeon, character);
        this.strategy = strategy;
    }

    @Override
    void move() {
        strategy.move();
        strategy.move();
    }
    
}