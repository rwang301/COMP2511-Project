package unsw.dungeon;

public class MoveTwice extends Decorator {

    MoveTwice(Dungeon dungeon, Character character, Strategy strategy) {
        super(dungeon, character, strategy);
    }

    @Override
    void move() {
        super.move();
        super.move();
    }
    
}