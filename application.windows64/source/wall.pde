
class wall extends thing {
  int x1, y1, x2, y2;

  public wall(int tx1, int ty1, int tx2, int ty2) {
    x1 = tx1;
    y1 = ty1;
    x2 = tx2; 
    y2 = ty2;
  }

  boolean isIn(int x, int y, int extraWidth) {
    return x > (x1-extraWidth) && x < (x2+extraWidth) && y > (y1-extraWidth) && y < (y2+extraWidth);
  }

  void render() {
    fill(matrixgreen);
    stroke(origGreen);
    rect(x1, y1, x2-x1, y2-y1);
  }

  int minX() {
    return this.x1;
  }
  int maxX() {
    return this.x2;
  }
  int minY() {
    return this.y1;
  }
  int maxY() {
    return this.y2;
  }
}