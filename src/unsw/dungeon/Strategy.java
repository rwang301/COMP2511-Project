package unsw.dungeon;


public abstract class Strategy {
    public Enemy enemy;
    public Player player;
    private Dungeon dungeon;

    public Strategy(Dungeon d, Enemy e) {
        this.dungeon = d;
        this.enemy = e;
    }    

    public void setPlayer(Player player) {
        this.player = player;
    }

    public abstract void move();

    public void moveUp() {
        if (this.enemy.getY() > 0 && this.dungeon.isBlocked(this.enemy.getX(), this.enemy.getY() - 1)) {
            this.enemy.y().set(this.enemy.getY() - 1);
        }
    };

    public void moveDown() {
        if (this.enemy.getY() < dungeon.getHeight() - 1 && this.dungeon.isBlocked(this.enemy.getX(), this.enemy.getY() + 1)) {
            this.enemy.y().set(this.enemy.getY() + 1);
        }
    };

    public void moveLeft() {
        if (this.enemy.getX() > 0 && this.dungeon.isBlocked(this.enemy.getX() - 1, this.enemy.getY())) {
            this.enemy.y().set(this.enemy.getX() - 1);
        }
    };

    public void moveRight() {
        if (this.enemy.getX() < this.dungeon.getWidth() - 1 && this.dungeon.isBlocked(this.enemy.getX() + 1, this.enemy.getY())) {
            this.enemy.y().set(this.enemy.getX() + 1);
        }
    };
}