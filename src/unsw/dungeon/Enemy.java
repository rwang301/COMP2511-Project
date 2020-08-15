package unsw.dungeon;

import java.util.Timer;
import java.util.TimerTask;

public class Enemy extends Character {
    final int range = 3;
    private Strategy moveToward;
    private Strategy moveAway;
    private Strategy curr;
    private Timer timer;
    private Dungeon dungeon;

    public Enemy(Dungeon dungeon, int x, int y) {
        super(x, y);
        this.dungeon = dungeon;
        moveToward = new MoveToward(dungeon, this);
        moveAway = new MoveAway(dungeon, this);
        strategy = moveToward;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public Strategy getMoveToward() {
        return moveToward;
    }

    public Strategy getMoveAway() {
        return moveAway;
    }

    void cancelTimer() {
        timer.cancel();
        timer.purge();
    }

    /**
     * Reset the visited array
     */
    void reset() {
        strategy.reset();
    }

    @Override
    int getRange() {
        return range;
    }

    /**
     * Initialise a timer for the enemy
     * @param player
     */
    @Override
    public void initialise(Player player) {
        Enemy _this = this;
        moveToward.setPlayer(player);
        moveAway.setPlayer(player);
        moveToward.setCurrentPosition();
        moveAway.setCurrentPosition();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!dungeon.isPause()) {
                    if (player.withinRange(_this)) curr = new MoveTwice(dungeon, _this, strategy);
                    else curr = strategy;
                    curr.move();
                }
            }
        }, 1000, 500); // Be careful when change the delay and period it will fail the JUnit tests
    }

    /**
     * When the enemy collides with a player
     * if the player does not hold a sword or potion
     * then the player dies
     * otherwise the enemy dies
     * @param player
     */
    @Override
	void collide(Player player) {
        if (player.getSword() != null || player.getPotion() != null) {
            if (player.getPotion() == null) player.hit(); // if the player doesn't have a potion they must've had a sword
            player.kill(this);
        } else {
            player.die();
        }
    }

    @Override
    public void update(Subject subject) {
        if (subject.getClass() == Player.class) strategy = moveAway;
        else if (subject.getClass() == Potion.class) strategy = moveToward;
        reset();
    }
}
