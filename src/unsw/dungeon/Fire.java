package unsw.dungeon;

import java.util.Timer;
import java.util.TimerTask;

public class Fire extends Entity {
    private boolean on = true;
    private Timer timer;

    public Fire(int x, int y) {
        super(x, y);
    }

    private void cancelTimer() {
        timer.cancel();
        timer.purge();
    }

    void initialise(Dungeon dungeon) {
        Fire _this = this;
        Player player = dungeon.getPlayer();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!dungeon.isPause()) {
                    if (on) {
                        dungeon.disappear(_this);
                        on = false;
                    } else {
                        if (_this.isOn(player)) player.die();
                        for (Entity boulder: dungeon.getEntities(Boulder.class)) {
                            if (_this.isOn(boulder)) {
                                cancelTimer();
                                return;
                            }
                        }

                        // TODO doesn't work with more than one fire
                        dungeon.respawn(_this);
                        on = true;
                    }
                }
            }
        }, 0, 3000);
    }
}