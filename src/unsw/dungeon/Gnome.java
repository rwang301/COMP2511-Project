package unsw.dungeon;

public class Gnome extends Character {
    private Strategy moveThrough;

    public Gnome(Dungeon dungeon, int x, int y) {
        super(x, y);
        strategy = new MoveRandom(dungeon, this);
        moveThrough = new MoveThrough(dungeon, this, strategy);
    }

    @Override
    int getRange() {
        return range;
    }

    @Override
    public void initialise(Player player) {
        strategy.setPlayer(player);
        moveThrough.setPlayer(player);
    }

    @Override
    void collide(Player player) {
        if (player.getPotion() != null) player.kill(this);
        else player.die();
    }

    @Override
    public void update(Subject subject) {
        if (((Player) subject).withinRange(this)) moveThrough.move();
        else strategy.move();
    }
}