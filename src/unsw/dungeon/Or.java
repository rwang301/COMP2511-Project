package unsw.dungeon;

public class Or extends Composite {

    @Override
    public boolean complete(Player player) {
        for (Component component : this.components) {
            if (component.complete(player)) return true;
        }
        return false;
    }
    
}
