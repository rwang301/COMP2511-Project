package unsw.dungeon;

public class MoveWith extends Strategy {

    MoveWith(Dungeon dungeon, Hound hound) {
        super(dungeon, hound);
    }

    @Override
    void move() {
        //set to players original position
        character.setPosition(character.x(), player.getCurrPosition().getX());
        character.setPosition(character.y(), player.getCurrPosition().getY());
    }
    
}