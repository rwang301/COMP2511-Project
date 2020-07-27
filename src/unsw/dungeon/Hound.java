package unsw.dungeon;

public class Hound extends Character {

    public Hound(Dungeon dungeon, int x, int y) {
        super(x, y);
        strategy = new MoveWith(dungeon, this);
    }

    @Override
    void initialise(Player player) {
        player.attach(this);
        strategy.setPlayer(player);
    }

    @Override
    void collide(Player player) {
        // Assume hound will never run into player
    }

    @Override
    public void update(Subject subject) {
        strategy.move();
    }
}