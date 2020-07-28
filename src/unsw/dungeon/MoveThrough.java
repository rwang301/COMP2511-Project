package unsw.dungeon;

public class MoveThrough extends Decorator {
    private Strategy strategy;

    MoveThrough(Dungeon dungeon, Gnome gnome, Strategy strategy) {
        super(dungeon, gnome);
        this.strategy = strategy;
    }

    @Override
    boolean canMove() {
        strategy.canMove();
        return true;
    }
}