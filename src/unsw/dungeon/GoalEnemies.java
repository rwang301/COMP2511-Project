package unsw.dungeon;

public class GoalEnemies extends Leaf {
    public static final String description = "Kill all the enemies";

    @Override
    public boolean complete(Player player) {
        return player.getEnemies().size() == 0;
    }

    @Override
    public String getDescription(int depth) {
        return description;
    }
}