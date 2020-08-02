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
    public String getDescription() {
		components.forEach(goal -> {
			if (description != "") description +=  "\nOR\n";
			description += goal.getDescription();
		});
		return description + '\n';
    }
}