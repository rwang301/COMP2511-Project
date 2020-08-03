package unsw.dungeon;

public class GoalTreasure extends Leaf {
    public static final String description = "Collect all the treasure";

    @Override
    public boolean complete(Player player) {
        return player.getTreasure() == player.getTotalTreasure();
    }

    @Override
    public String getDescription(int depth) {
        return description;
    }
}