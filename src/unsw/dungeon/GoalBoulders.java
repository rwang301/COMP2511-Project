package unsw.dungeon;

import java.util.List;

public class GoalBoulders extends Leaf {

    @Override
    public boolean complete(Player player) {
        List<Entity> switches = player.getEntityType(Switch.class);
        for (Entity e : switches) {
            if(!((Switch)e).isTriggered(player)) {
                return false;
            }
        }
        return true;
    }
    
}
