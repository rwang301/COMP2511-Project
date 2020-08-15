package unsw.dungeon;

public class Hound extends Character {

    public Hound(Dungeon dungeon, int x, int y) {
        super(x, y);
        strategy = new MoveWith(dungeon, this);
    }

    @Override
    int getRange() {
        return range;
    }

    @Override
    void initialise(Player player) {
        player.attach(this);
        strategy.setPlayer(player);
    }

    @Override
    void collide(Player player) {
        if (player.getHound() == null) initialise(player);
    }

    @Override
    public void update(Subject subject) {
        strategy.move();
    }
}