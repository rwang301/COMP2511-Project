package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import unsw.ui.DungeonController;

public class Potion extends Entity implements Pickupable, Subject, Observer {
    private long pickupTime;
    private long effectTime = 5000;
    private LongProperty tick = new SimpleLongProperty(effectTime / 1000);
    private Timer timer;
    private List<Observer> enemies = new ArrayList<>();

    public Potion(int x, int y) {
        super(x, y);
    }

    LongProperty getTick() {
        return tick;
    }

    public void setTick() {
        Platform.runLater(() -> tick.set(tick.get() - 1));
    }

    /**
     * Record the time of pickup in milliseconds.
     * If the player already had a potion extend the effect time
     * @return the new potion which just got picked up
     */
    Potion pickup(Potion potion, Player player) {
        enemies = player.getEnemies();
        if (potion != null) {
            effectTime += potion.effectTime - (System.currentTimeMillis() - potion.pickupTime);
            tick = new SimpleLongProperty(effectTime / 1000);
            potion.cancelTimer();
        }
        timer = scheduleTimer(player);
        return this;
    }

    private void cancelTimer() {
        timer.cancel();
        timer.purge();
    }

    private Timer scheduleTimer(Player player) {
        Potion _this = this;
        pickupTime = System.currentTimeMillis();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // TODO count down starts from 4 instead of 5
                if (tick.get() < 1) {
                    player.useUpPotion(_this);
                    notifyObservers();
                    cancelTimer();
                } else {
                    setTick();
                }
            }
        }, 0, 1000);
        return timer;
    }

    @Override
    public void attach(Observer enemy) {
        enemies.add(enemy);
    };

    @Override
    public void detach(Observer enemy) {
	    enemies.remove(enemy);
    }

    @Override
    public void notifyObservers() {
        enemies.forEach(enemy -> enemy.update(this));
    }

    @Override
    public void update(Subject subject) {
        DungeonController controller = (DungeonController) subject;
        Player player = controller.getPlayer();
        if (player.getPotion() != this) return; // potion hasn't been picked up yet

        if (controller.isPause()) {
            cancelTimer();
            effectTime = effectTime - (System.currentTimeMillis() - pickupTime);
        } else {
            timer = scheduleTimer(player);
        }
    }
}
