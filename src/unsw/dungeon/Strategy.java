package unsw.dungeon;


public abstract class Strategy {
    public Enemy enemy;
    private Dungeon dungeon;

    public Strategy(Dungeon d, Enemy e) {
        this.dungeon = d;
        this.enemy = e;
    }    

    public abstract void move();

    public boolean moveUp() {
        if (this.enemy.getY() > 0 && this.dungeon.isBlocked(this.enemy.getX(), this.enemy.getY() - 1)) {
            this.enemy.y().set(this.enemy.getY() - 1);
            return true;
        }
        return false;
    };

    public boolean moveDown() {
        if (this.enemy.getY() < dungeon.getHeight() - 1 && this.dungeon.isBlocked(this.enemy.getX(), this.enemy.getY() + 1)) {
            this.enemy.y().set(this.enemy.getY() + 1);
            return true;
        }
        return false;
    };

    public boolean moveLeft() {
        if (this.enemy.getX() > 0 && this.dungeon.isBlocked(this.enemy.getX() - 1, this.enemy.getY())) {
            this.enemy.x().set(this.enemy.getX() - 1);
            return true;
        }
        return false;
    };

    public boolean moveRight() {
        if (this.enemy.getX() < this.dungeon.getWidth() - 1 && this.dungeon.isBlocked(this.enemy.getX() + 1, this.enemy.getY())) {
            this.enemy.x().set(this.enemy.getX() + 1);
            return true;
        }
        return false;
    };
}