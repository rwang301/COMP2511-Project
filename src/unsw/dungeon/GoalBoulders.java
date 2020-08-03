package unsw.dungeon;

public class GoalBoulders extends Leaf {
    public static final String description = "Boulders on all the switches";

    @Override
    public boolean complete(Player player) {
        return player.checkSwitches();
    }

    @Override
    public String getDescription(int depth) {
        return description;
    }
}