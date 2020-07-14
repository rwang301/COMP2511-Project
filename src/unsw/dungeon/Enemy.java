package unsw.dungeon;

import java.util.Timer;
import java.util.TimerTask;

public class Enemy extends Entity implements Observer {
    private Strategy strategy;
    private Dungeon dungeon;
    private Player player;

    public Enemy(Dungeon dungeon, int x, int y) {
        super(x, y);
        this.dungeon = dungeon;
        strategy = new MoveToward(dungeon, this);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

	public void collide(Player player) {
        if (player.getSword() != null || player.getPotion() != null) {
            if (player.getPotion() == null) player.hit(); // if the player doesn't have a potion they must've had a sword
            player.disappear(this);
            player.detach(this);
            player.complete();
        } else {
            player.die();
        }
    }

    public void startMoving() {
        strategy.setPlayer(player);
        strategy.setCurrentPosition();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                strategy.move();
            }
        }, 3000, 500);
    }

    @Override
    public void update(Subject subject) {
        strategy = new MoveAway(dungeon, this);
        strategy.setPlayer(player);
    }

    public void reset(Subject subject) {
        strategy.reset();
    }
}