class bullet extends thing {

  color playerc = #16EDC7;

  // 0 = player, 1 = enemytype 1 etc...
  final int[] velocitys = {
    20, 5, 10, 10
  };
  final int[] sizes = {
    10, 30, 15, 5
  };

  int length = 0; 
  PVector position;
  PVector velocity;
  int kind;
  character owner;
  int size; 

  public bullet(int sx, int sy, int dirx, int diry, character own, int kind) {
    position = new PVector(sx, sy);
    velocity = new PVector(dirx, diry);
    velocity.normalize();    
    velocity.mult(velocitys[kind]);
    size = sizes[kind];
    this.owner = own;
    this.kind = kind;
  }

  boolean update() {
    this.position.add(velocity);
    length = length + (int) velocity.mag();

    stroke (200);
    if (this.owner == chars.get("player")) {
      fill(playerc);
      stroke(#12E5FF);
    } else if (this.kind == 1) {
      stroke(#07B71D);
      fill(origGreen);
    } else if (this.kind == 2 || kind == 3) {
      stroke(origGreen);
      fill(100);
    }

    ellipse(position.x, position.y, size, size);
    return this.kill();
  }


  private boolean kill() {

    if (length > 700) return true;
    for (wall w : terrain)
      if (w.isIn((int)position.x, (int)position.y, size/2)) return true;
    //for (String)//TODO
      return false;
    }

    public int getSize() {
      return this.size;
    }

  public String getOwner() {
    switch (this.kind) {
    case 0: 
      return "player";

    default: 
      return "other";
    }
  }
  boolean isIn(int x, int y, int extraWidth) {
    //returns weather the point is within a certain distance of this center, adding extrad wiodth
    return false;
  }

  int minX() {
    return (int)this.position.x - (size/2);
  }
  int maxX() {
    return (int)this.position.x + (size/2);
  }
  int minY() {
    return (int)this.position.y - (size/2);
  }
  int maxY() {
    return (int)this.position.y + (size/2);
  }
}