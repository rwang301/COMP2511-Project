package unsw.dungeon;

public class Gnome extends Character {

    public Gnome(Dungeon dungeon, int x, int y) {
        super(x, y);
        strategy = new MoveRandom(dungeon, this);
    }
    
    @Override
    public void initialise(Player player) {
        strategy.setPlayer(player);
    }

    @Override
    void collide(Player player) {
        if (player.getPotion() != null) player.kill(this);
        else player.die();
    }

    @Override
    public void update(Subject subject) {
        strategy.move();
    }
}