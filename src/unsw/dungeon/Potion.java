package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import unsw.ui.DungeonController;

public class Potion extends Entity implements Pickupable, Subject, Observer {
    private long pickupTime;
    private long effectTime = 5000;
    private Timer timer;
    private List<Observer> enemies = new ArrayList<>();

    public Potion(int x, int y) {
        super(x, y);
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
                player.useupPotion(_this);
                notifyObservers();
            }
        }, effectTime);
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
