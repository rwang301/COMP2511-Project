package unsw.dungeon;

public class Enemy extends Entity implements Observer {
    //subject is player, observer is enemy
    public Enemy(int x, int y) {
        super(x, y);
    }

    @Override
    public void update(Subject subject) {
        // TODO Auto-generated method stub
        System.out.println(this);
    }
}