package unsw.dungeon;

public class MoveWith extends Strategy {

    MoveWith(Dungeon dungeon, Hound hound) {
        super(dungeon, hound);
    }

    /**
     * Move to player's last position
     */
    @Override
    void move() {
        character.setPosition(character.x(), player.getCurrPosition().getX());
        character.setPosition(character.y(), player.getCurrPosition().getY());
    }
    
}