package unsw.dungeon;

public class MoveToward extends Strategy {

    public MoveToward(Dungeon d, Enemy e) {
        super(d, e);
    }

    @Override
    public void move() {
        moveUp();
    }
    
}
