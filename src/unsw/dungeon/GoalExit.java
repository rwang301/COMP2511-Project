package unsw.dungeon;

public class GoalExit extends Leaf {

    @Override
    public boolean complete(Player player) {
        return player.isOn(Exit.class);
    }
    
}
