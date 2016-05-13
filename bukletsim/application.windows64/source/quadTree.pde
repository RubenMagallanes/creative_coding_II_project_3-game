private class rectangle {
      int x, y, w, h;
      rectangle(int X, int Y, int W, int H) {
            this.x = X;
            this.y = Y;
            this.w = W;
            this.h = H;
      }
      int getX() {
            return this.x;
      }
      int getY() {
            return this.y;
      }
      int getWidth() {
            return this.w;
      }
      int getHeight() {
            return this.h;
      }
}
/**  code copied from (then changed slightly):
 http://gamedevelopment.tutsplus.com/tutorials/quick-tip-use-quadtrees-to-detect-likely-collisions-in-2d-space--gamedev-374
 written by Steven Lambert,
 although by no means did he invent this data structure.
 */
public class Quadtree {

      private int MAX_OBJECTS = 5;
      private int MAX_LEVELS = 5;

      private int level;
      private List<thing> objects;
      private rectangle bounds;
      private Quadtree[] nodes;

      /*
  * Constructor
       */
      public Quadtree(int pLevel, rectangle pBounds) {
            level = pLevel;
            objects = new ArrayList<thing>();
            bounds = pBounds;
            nodes = new Quadtree[4];
      }
      /*
 * Clears the quadtree
       */
      public void clear() {
            objects.clear();

            for (int i = 0; i < nodes.length; i++) {
                  if (nodes[i] != null) {
                        nodes[i].clear();
                        nodes[i] = null;
                  }
            }
      }

      /*
 * Splits the node into 4 subnodes
       */
      private void split() {
            int subWidth = (int)(bounds.getWidth() / 2);
            int subHeight = (int)(bounds.getHeight() / 2);
            int x = (int)bounds.getX();
            int y = (int)bounds.getY();

            nodes[0] = new Quadtree(level+1, new rectangle(x + subWidth, y, subWidth, subHeight));
            nodes[1] = new Quadtree(level+1, new rectangle(x, y, subWidth, subHeight));
            nodes[2] = new Quadtree(level+1, new rectangle(x, y + subHeight, subWidth, subHeight));
            nodes[3] = new Quadtree(level+1, new rectangle(x + subWidth, y + subHeight, subWidth, subHeight));
      }
      /*
 * Determine which node the object belongs to. -1 means
       * object cannot completely fit within a child node and is part
       * of the parent node
       */
      private int getIndex(thing th) {
            int index = -1;
            double verticalMidpoint = bounds.getX() + (bounds.getWidth() / 2);
            double horizontalMidpoint = bounds.getY() + (bounds.getHeight() / 2);

            // Object can completely fit within the top quadrants
            boolean topQuadrant = (th.maxY()< horizontalMidpoint);
            // Object can completely fit within the bottom quadrants
            boolean bottomQuadrant = (th.minY() > horizontalMidpoint);

            // Object can completely fit within the left quadrants
            if (th.maxX()< verticalMidpoint) {
                  if (topQuadrant) {
                        index = 1;
                  } else if (bottomQuadrant) {
                        index = 2;
                  }
            }
            // Object can completely fit within the right quadrants
            else if (th.minX() > verticalMidpoint) {
                  if (topQuadrant) {
                        index = 0;
                  } else if (bottomQuadrant) {
                        index = 3;
                  }
            }

            return index;
      }

      /*
 * Insert the object into the quadtree. If the node
       * exceeds the capacity, it will split and add all
       * objects to their corresponding nodes.
       */
      public void insert(thing th) {
            if (nodes[0] != null) {
                  int index = getIndex(th);

                  if (index != -1) {
                        nodes[index].insert(th);

                        return;
                  }
            }

            objects.add(th);

            if (objects.size() > MAX_OBJECTS && level < MAX_LEVELS) {
                  if (nodes[0] == null) { 
                        split();
                  }

                  int i = 0;
                  while (i < objects.size ()) {
                        int index = getIndex(objects.get(i));
                        if (index != -1) {
                              nodes[index].insert(objects.remove(i));
                        } else {
                              i++;
                        }
                  }
            }
      }

      /*
 * Return all objects that could collide with the given object
       */
      public List<thing> retrieve(List<thing> returnObjects, thing th) {
            int index = getIndex(th);
            if (index != -1 && nodes[0] != null) {
                  nodes[index].retrieve(returnObjects, th);
            }

            returnObjects.addAll(objects);

            return returnObjects;
      }
}