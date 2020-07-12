package unsw.dungeon;

public class And extends Composite {
	@Override
	public boolean complete(Player player) {
        for (Component component: components) {
            if (!component.complete(player)) return false;
        }
		return true;
	}
}