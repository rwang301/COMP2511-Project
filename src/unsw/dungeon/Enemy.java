package unsw.dungeon;

import java.util.Timer;
import java.util.TimerTask;

public class Enemy extends Entity implements Observer {
    private Strategy strategy;
    private Strategy moveToward;
    private Strategy moveAway;
    private Timer timer;

    public Enemy(Dungeon dungeon, int x, int y) {
        super(x, y);
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

    /**
     * Initialise a timer for the enemy
     * @param player
     */
    public void initialise(Player player) {
        moveToward.setPlayer(player);
        moveToward.setCurrentPosition();
        moveAway.setPlayer(player);
        moveAway.setCurrentPosition();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                strategy.move();
            }
        }, 1000, 500); // Be careful when change the delay and period it will fail the JUnit tests
    }

    public void cancelTimer() {
        timer.cancel();
        timer.purge();
    }

    /**
     * When the enemy collides with a player
     * if the player does not hold a sword or potion
     * then the player dies
     * otherwise the enemy dies
     * @param player
     */
	public void collide(Player player) {
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
    }

    /**
     * Reset the visited array
     */
    public void reset() {
        strategy.reset();
    }
}
