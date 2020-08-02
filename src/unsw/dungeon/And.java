package unsw.dungeon;

public class And extends Composite {
	private String description = "";

	@Override
	public boolean complete(Player player) {
        for (Component component: components) {
            if (!component.complete(player)) return false;
        }
		return true;
	}

	@Override
	public String getDescription() {
		components.forEach(goal -> {
			if (description != "") description +=  "\nAND\n";
			description += goal.getDescription();
		});
		return description + '\n';
	}
}