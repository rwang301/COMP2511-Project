package unsw.dungeon;

public class GoalTreasure extends Leaf {

    @Override
    public boolean complete(Player player) {
        return player.getTreasure() == player.getTotalTreasure();
    }

}