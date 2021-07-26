package unsw.dungeon;

public class Or extends Composite {
    private String description = "";

    @Override
    public boolean complete(Player player) {
        for (Component component: components) {
            if (component.complete(player)) return true;
        }
        return false;
    }

	@Override
	public String getDescription(int depth) {
		description = "";
		return recursion(depth);
	}

	private String recursion(int depth) {
		description += "Either:\n";
		components.forEach(goal -> {
			for (int i = 0; i < depth; i++) description += "\t";
			description += "- ";
			description += goal.getDescription(depth + 1) + "\n";
		});
		return description;
	}
}