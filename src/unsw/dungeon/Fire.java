package unsw.dungeon;

import java.util.Timer;
import java.util.TimerTask;

public class Fire extends Entity {
    private boolean on = true;

    public Fire(int x, int y) {
        super(x, y);
    }

    void initialise(Dungeon dungeon) {
        Fire _this = this;
        Player player = dungeon.getPlayer();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!dungeon.isPause()) {
                    if (on) {
                        dungeon.disappear(_this);
                        on = false;
                    } else {
                        dungeon.respawn(_this);
                        if (_this.isOn(player)) player.die();
                        on = true;
                    }
                }
            }
        }, 0, 3000); // Be careful when change the delay and period it will fail the JUnit tests
    }
}