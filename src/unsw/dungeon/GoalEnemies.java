package unsw.dungeon;

public class GoalEnemies extends Leaf {

    @Override
    public boolean complete(Player player) {
        return player.getEnemies().size() == 0;
    }

}