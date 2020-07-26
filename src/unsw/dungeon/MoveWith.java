package unsw.dungeon;

public class MoveWith extends Strategy {

    MoveWith(Dungeon dungeon, Hound hound) {
        super(dungeon, hound);
    }

    @Override
    void move() {
        //set to players original position
        System.out.println(player.getCurrPosition());
        System.out.println(player.getPrevPosition());
    }
    
}