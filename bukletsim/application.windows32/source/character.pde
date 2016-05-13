
class character extends thing {

  PVector position;
  PVector velocity;
  PVector accel;
  boolean left, right, up, down;
  boolean player;
  int size;
  String name_key;
  public int lastframe;
  public int lastShot =0;//frames since last shot
  public int uzicount = 0;
  String direction = "right";//npcs default to moving right. player isnt affected by this variable

  character(int x, int y, int siz, boolean isPlayer, String nk) {
    this.position = new PVector(x, y);
    velocity = new PVector();
    accel = new PVector();
    this.size = siz;
    player = isPlayer;
    name_key = nk;

    lastframe = frameCount;
  }

  void update() {
lastShot++;
    PVector oldpos = new PVector(position.x, position.y);
    //accel .mult(0);
    accel.normalize();
    accel.mult(-1);

    if (left && !right) {
      accel.add(-2.0, 0.0, 0.0);
    } 

    if (!left && right) {
      accel.add(2.0, 0.0, 0.0);
    }
    if (up && !down) {
      accel.add(0.0, -2.0, 0.0);
    }
    if (!up && down) {
      accel.add(0.0, 2.0, 0.0);
    }

    accel.normalize();
    velocity.add(accel);

    velocity.mult(0.8); 
    position.add(velocity);

    if (position.x < 0+(size/2)) position.x = 0+(size/2);
    if (position.x > width-(size/2)) position.x = width-(size/2);
    if (position.y < 0+(size/2)) position.y = 0+(size/2);
    if (position.y > height-(size/2)) position.y =  height-(size/2);

    for (wall w : terrain) {
      if (w.isIn((int)position.x, (int)oldpos.y, size/2 )) {
        position.x = oldpos.x;
      }
      if (w.isIn((int) oldpos.x, (int)position.y, size/2)) {
        position.y = oldpos.y;
      }
    }


    reDraw();
  }

  void reDraw()
  {
    if (this.player) {
      fill(#2199FF);
      stroke(#2199FF);
    } else {
      fill(240);
      stroke(150);
    }
    ellipse(this.position.x, this.position.y, size, size);
  }

  void changeDir() {
    if (this.direction.equals("right")) this.direction = "left";
    else this.direction = "right";
  }

  void key(boolean l, boolean r, boolean u, boolean d) {
    left = l;
    up = u;
    right = r;
    down = d;
  }

  boolean isIn(int x, int y, int extraWidth) {

    PVector dist = new PVector(x-position.x, y-position.y);
    return dist.mag() < (this.size/2 + extraWidth);
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