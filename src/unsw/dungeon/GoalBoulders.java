package unsw.dungeon;

public class GoalBoulders extends Leaf {

    @Override
    public boolean complete(Player player) {
        return player.checkSwitches();
    }

}