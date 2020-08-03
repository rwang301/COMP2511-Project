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
	public String getDescription(int depth) {
		description = "";
		return recursion(depth);
	}
	
	private String recursion(int depth) {
		description +=  "Both:\n";
		components.forEach(goal -> {
			for (int i = 0; i < depth; i++) description += "\t";
			description += "- ";
			description += goal.getDescription(depth + 1) + "\n";
		});
		return description;
	}
}