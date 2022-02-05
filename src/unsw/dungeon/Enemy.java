package unsw.dungeon;

import java.util.Timer;
import java.util.TimerTask;

public class Enemy extends Entity implements Observer {

    private Strategy strategy;
    private Player player;

    public Enemy(Dungeon d, int x, int y) {
        super(x, y);
        strategy = new MoveToward(d, this);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void update(Subject subject) {
        // TODO Auto-generated method stub
        
    }

    public void collide(Player p, Dungeon d) {
        if (p.getSword() != null || p.getPotion() != null) {
            d.disappear(this);
            p.detach(this);
        } else {
            d.die();
        }
    }

    public void startMoving() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                strategy.move();
            }
        }, 500, 3000);
    }
    
}
