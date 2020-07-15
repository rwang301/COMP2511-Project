package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Potion extends Entity implements Pickupable, Subject {
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
    public Potion pickup(Potion potion, Player player) {
        enemies = player.getEnemies();
        pickupTime = System.currentTimeMillis();
        if (potion != null) {
            effectTime += potion.effectTime - (System.currentTimeMillis() - potion.pickupTime);
            potion.cancelTimer();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                player.setPotion(null);
                notifyObservers();
            }
        }, effectTime);
        return this;
    }

    private void cancelTimer() {
        timer.cancel();
        timer.purge();
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
}