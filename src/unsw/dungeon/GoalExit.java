package unsw.dungeon;

public class GoalExit extends Leaf {
    public static final String description = "Get to the exit last";

    @Override
    public boolean complete(Player player) {
        return player.isOn(Exit.class);
    }

    @Override
    public String getDescription(int depth) {
        return description;
    }
}