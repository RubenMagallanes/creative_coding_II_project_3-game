import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ddf.minim.*; 
import ddf.minim.analysis.*; 
import ddf.minim.effects.*; 
import ddf.minim.signals.*; 
import ddf.minim.spi.*; 
import ddf.minim.ugens.*; 
import java.util.*; 
import ddf.minim.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class bukletsim extends PApplet {













HashSet<bullet> bullets = new HashSet<bullet>();
HashSet<wall> terrain = new HashSet<wall>();
HashMap<String, character> chars = new HashMap<String, character>();
PFont f;
PVector endzone;

int origGreen = 0xff00FF1F;
int matrixgreen;// = #00FF1F;
int backgroundcol = 0;
//maybe for colors fuckery?
String beats = "music_for_242_proj3.mp3";

Minim minim;
AudioPlayer player;

int orange  =0xffFF8D00;
int lastSwitch = 0;

int time = 0;//players total time

int timestart = 0;

boolean hardmode = false;



//keysmpressed
boolean w=false, a=false, s=false, d=false;
Quadtree quad;

public void setup() {
        
      
      quad = new Quadtree(0, new rectangle(0, 0, 600, 600)); 

      // fill(matrixgreen);
      // stroke(matrixgreen);
      // rect(0, 0, width, height);
      f =createFont("Arial", 16, true);

      minim = new Minim(this);
      player = minim.loadFile(beats);


      introSplash();
      //level1();
      frameRate(60);

      noCursor();
      ellipseMode(CENTER);
}

HashSet<bullet> killList = new HashSet<bullet>();
PVector clicked = new PVector();
PVector plyr = new PVector();

int c= 0;
List<thing> returnObjects = new ArrayList<thing>();
int level = 1;

String state = "splash";
//boolean clicked = false;
public void draw() {
      //println(millis()/1000);
      //background
      stroke(backgroundcol);      
      fill(0, 30);
      if (millis()/400 % 2 == 0 && hardmode)  fill(0xffFF8D00, 30 );  
      rect(0, 0, width, height);

      if (frameCount%2==0) c++;
      if (c > 50) c= 10;   
      matrixgreen = color(origGreen, c/2 );      
      //cool lines 
     
      for (int i = 0; i< width; i++)
      {
            stroke(matrixgreen);
            if (i%40 == 0) {
                  line (i, 0, i, height);
                  line(0, i, height, i);
            }
      }

      if (state.equals ("lose")) {
            fill(origGreen);
            textFont(f, 15);
            //text();
            text("you we're killed and failed your mission."+
                  "\nWhat a surprise..."+
                  "\nLucky for you, we have another meatwad, er, agent ready to replace you. ", 200, 200);
            textFont(f, 20);
            text("press 'r' to retry", 350, 400);
            textFont(f, 15);
      } else if (state.equals("intro")) {
            fill(origGreen);
            textFont(f, 15);
            text("You are a secret agent on a mission to eliminate all terrorists in their home base. "+
                  "\nThe words 'suicide mission' and 'certain death' have been pssed around but they mean nothing to you. "+
                  "\nTime is of the essence as you currently posess the element of surprise, "+
                  "\nand you need all the advantages you can get. Good luck, you're going to need it.", 100, 50 );
            text("controls: \nw,a,s,d to move, mouse to aim and click to shoot. "+
            "\n 'g' activates baller mode, press it if youre AWESOME"+
                  "\nIn all missions, the blue area represents where you need to go to advance to the next area.", 100, 200);
            textFont(f, 20);
            text("click to start", 350, 400);
            textFont(f, 15);
      } else if (state.equals("splash1")) {   
            fill(origGreen);
            textFont(f, 15); 
            text("Your first mission: get inside. "+
                  "\nYou will be dropped off outside the entrance to the warehouse where it is suspected"+
                  "\nthe terrorists are hiding. "+
                  "Kill anyone guarding the entrance and get inside."
                  , 100, 200);
            textFont(f, 20);
            text("click to continue", 350, 400);
            textFont(f, 15);
      } else if (state.equals("splash2")) {
            fill(origGreen);
            textFont(f, 15); 
            text("Your second mission: infiltrate and head underground. "+
                  "\nHead inside the warehouse, kill all opposition and then find the stairs leading underground."+
                  "\nWatch out, these people wont hesitate to shoot you, and likely posess illegal weapons."+
                  "\nAnd illegal weapons hurt."                 
                  , 100, 200);
            textFont(f, 20);
            text("click to continue", 350, 400);
            textFont(f, 15);
            text("time so far : " + time/1000 + " seconds ", 100, 100);
      } else if (state.equals("splash3")) {
            fill(origGreen);
            textFont(f, 15); 
            text("Your third mission: Dont get shot. "+
                  "\nNo, Seriously, there will be heaps of people in there who all want to kill you."+
                  "\nEliminate them all and head deeper into their lair."+
                  "\nDid i mention to not die? Because if you do thats a lot of expensive equipment lost. "+
                  "\np.s. run to the right as soon as you get in. Don't say i'm not helpful. "   
                  , 100, 200);
            textFont(f, 20);
            text("click to continue", 350, 400);
            textFont(f, 15);
            text("time so far : " + time/1000 + " seconds ", 100, 100);
      } else if (state.equals("splash4")) {
            fill(origGreen);
            textFont(f, 15); 
            text("Your fourth mission: that epic hallway scene there is in movies "+
                  "\nHonestly it's surprising you've made it this far, but you'll most definitely die here. "+
                  "\nLets be real, this isn't the movies and they outnumber you. "+
                  "\nIf, IF! you can make it through, the next room is the final one. "+
                  "\nIF...   Don't get your hopes up. "                 
                  , 100, 200);
            textFont(f, 20);
            text("click to continue and probably die", 350, 400);
            textFont(f, 15);
            text("time so far : " + time/1000 + " seconds ", 100, 100);
      } else if (state.equals("splash5")) {
            fill(origGreen);
            textFont(f, 15); 
            text("Your fifth and final mission: eliminate the last of them and get out "+
                  "\n\nAt this point you can probably do anything, so this'll be easy."+
                  "\nEliminate all opposition and return home for a nice cup of tea"                       
                  , 100, 200);
            textFont(f, 20);
            text("click to continue and clear this easy :^)", 350, 400);
            textFont(f, 15);
            text("time so far : " + time/1000 + " seconds ", 100, 100);
      } else if (state.equals("splash6")) {
            fill(origGreen);
            textFont(f, 15); 
            text("You did it! You can come home a hero!"   +
                  "\n..or you could... i regret to inform you that we've located another terrorist base that looks "+
                  "\nalmost exactly the same as this one.. "+
                  "\nSince you're the only person with the skills needed, you can been selected to deal"+
                  "\nwith this inconvenience. "+
                  "\nBetter forget about that cup of tea..."                         
                  , 100, 200);
            textFont(f, 20);
            text("press 'r' to start all over again", 350, 400);
            textFont(f, 15);
            text("time taken: " + time/1000 + " seconds!\n well done! ", 100, 100);
      } else if (state.equals("playing")) {
            //check player has cleared enemies
            int enemiesremaining = 0;
            for (String s : chars.keySet ()) {
                  if (!s.equals("player"))
                        enemiesremaining++;
            }
            if (enemiesremaining == 0 && chars.get("player").position.dist(endzone) < 50) { // check win condtn. 

                  splash(level);

                  return;
            }
            fill(origGreen);
            textFont(f, 15); 
            text("time so far : " + (time/1000 + (millis()-timestart)/1000)+  " seconds ", 10, 15);




            //quad tree for hit detection
            quad.clear();
            for (wall w : terrain) {
                  quad.insert(w);
            }
            for (String k : chars.keySet ()) {
                  quad.insert(chars.get(k));
            }
            for (bullet b : bullets) {
                  quad.insert(b);
            }
            List<bullet> kilList = new ArrayList<bullet>();

            try {
                  for (bullet b : bullets) {

                        returnObjects.clear();
                        quad.retrieve(returnObjects, b);

                        for (int x = 0; x < returnObjects.size (); x++) {
                              // Run collision detection algorithm between bullets and walls/people
                              //kill people and bukllet if bullet hits char, just bullet if wall                
                              if (returnObjects.get(x) instanceof character) {
                                    character c = (character)returnObjects.get(x);
                                    if (c.isIn((int)b.position.x, (int)b.position.y, b.size/2)) {
                                          if (b.owner.player && !c.player) {
                                                chars.remove(c.name_key);
                                                kilList.add(b);
                                                break;
                                          } else if (!b.owner.player && c.player) {
                                                lose();
                                          }
                                    }
                              }
                        }
                  }
            } 
            catch (Exception e) {
                  //println(e.getStackTrace()); //DO NOTHING AYYYYYYYYYYY
            }
            for (bullet b : kilList)
                  bullets.remove(b);


            //rendering
            for (wall w : terrain) {     
                  w.render();
            }

            stroke(0, 0, 150, 200);
            fill(0, 0, 150, 50);
            ellipse(endzone.x, endzone.y, 100, 100);

            //player
            if (chars.get("player")!= null) {
                  chars.get("player").key(a, d, w, s);
            }
            npcs();
            //npc behaviour
            //random little jiggles for npcs? //,maybe later in draw 
            if (frameCount % 20 == 0) {
                  for (String s : chars.keySet ()) {
                        int rng = (int) random(4);//0-5
                        if (rng == 0)
                              chars.get(s).key(true, false, false, false);
                        if (rng == 1)
                              chars.get(s).key(false, true, false, false);
                        if (rng == 2)
                              chars.get(s).key(false, false, true, false);
                        if (rng == 3)
                              chars.get(s).key(false, false, false, true);
                  }
            } else if (frameCount % 20 == 5) {
                  for (String s : chars.keySet ()) {
                        chars.get(s).key(false, false, false, false);
                  }
            }


            for (String c : chars.keySet ()) {
                  chars.get(c).update();
            }

            //bullets
            for (bullet b : bullets) {
                  if (b.update()) {
                        killList.add(b);
                  }
            }

            for (bullet b : killList) {
                  bullets.remove(b);
            }

            clicked.x = mouseX;
            clicked.y = mouseY;
            if (chars.get("player")!= null) {
                  plyr.x = chars.get("player").position.x;
                  plyr.y = chars.get("player").position.y;
                  plyr.sub(clicked);
            }
      }
      stroke(150);
      pushStyle();
      strokeWeight(1);
      line(mouseX-10, mouseY, mouseX+10, mouseY);
      line(mouseX, mouseY-10, mouseX, mouseY+10); 
      popStyle();
}

public void mousePressed() {
      //println(state +" " + level );
      PVector clicked = new PVector(mouseX + random(30)-15, mouseY + random(30)-15);
      if (state.equals("intro")) {  
            time = 0;      
            //state = "playing";
            splash(1);
      } else if (state.equals("splash1") ) {
            state = "playing";
            level1();
            timestart = millis();
      } else if (state.equals("splash2")) {
            state="playing";
            level2();
            timestart = millis();
      } else if (state.equals("splash3")) {
            state = "playing";
            level3();
            timestart = millis();
      } else if (state.equals("splash4")) {
            state = "playing";
            level4();
            timestart = millis();
      } else if (state.equals("splash5") ) {
            state = "playing";
            level5();
            timestart = millis();
      }
      if (state.equals("playing")) {        
            PVector dir = new PVector(chars.get("player").position.x, chars.get("player").position.y);
            dir.sub(clicked);
            dir.mult(-1);
            bullet b = new bullet((int)chars.get("player").position.x, (int)chars.get("player").position.y, 
            (int)dir.x, (int)dir.y, chars.get("player"), 0);
            bullets.add(b);
      }
} 
public void keyPressed() {
      switch(key) {
      case 'w' : 
      case 'W' :
            w = true;
            break;
      case 'a' : 
      case 'A' :
            a = true;
            break;
      case 's' : 
      case 'S' :
            s = true;
            break;
      case 'd' : 
      case 'D' :
            d = true;
            break;
      }
}
public void keyReleased() {
      switch(key) {
      case 'w' : 
      case 'W' :
            w = false;
            break;
      case 'a' : 
      case 'A' :
            a = false;
            break;
      case 's' : 
      case 'S' :
            s = false;
            break;
      case 'd' : 
      case 'D' :
            d = false;
            break;

      case 'r':
      case 'R':
            introSplash();
            player.rewind();
            break;
      case 'p': //CHEATING SHOT
            for (String  c : chars.keySet ()) {
                  PVector clicked = new PVector(chars.get(c).position.x, chars.get(c).position.y);
                  PVector dir = new PVector(chars.get("player").position.x, chars.get("player").position.y);
                  dir.sub(clicked);
                  dir.mult(-1);
                  bullet b = new bullet((int)chars.get("player").position.x, (int)chars.get("player").position.y, 
                  (int)dir.x, (int)dir.y, chars.get("player"), 0);
                  bullets.add(b);
            } 
            break;
      case 'l':
            //splash(level); //NO CHEATING 
            break;
      case 'g': hardmode = !hardmode;
      }
}
class bullet extends thing {

  int playerc = 0xff16EDC7;

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

  public boolean update() {
    this.position.add(velocity);
    length = length + (int) velocity.mag();

    stroke (200);
    if (this.owner == chars.get("player")) {
      fill(playerc);
      stroke(0xff12E5FF);
    } else if (this.kind == 1) {
      stroke(0xff07B71D);
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
  public boolean isIn(int x, int y, int extraWidth) {
    //returns weather the point is within a certain distance of this center, adding extrad wiodth
    return false;
  }

  public int minX() {
    return (int)this.position.x - (size/2);
  }
  public int maxX() {
    return (int)this.position.x + (size/2);
  }
  public int minY() {
    return (int)this.position.y - (size/2);
  }
  public int maxY() {
    return (int)this.position.y + (size/2);
  }
}

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

  public void update() {
lastShot++;
    PVector oldpos = new PVector(position.x, position.y);
    //accel .mult(0);
    accel.normalize();
    accel.mult(-1);

    if (left && !right) {
      accel.add(-2.0f, 0.0f, 0.0f);
    } 

    if (!left && right) {
      accel.add(2.0f, 0.0f, 0.0f);
    }
    if (up && !down) {
      accel.add(0.0f, -2.0f, 0.0f);
    }
    if (!up && down) {
      accel.add(0.0f, 2.0f, 0.0f);
    }

    accel.normalize();
    velocity.add(accel);

    velocity.mult(0.8f); 
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

  public void reDraw()
  {
    if (this.player) {
      fill(0xff2199FF);
      stroke(0xff2199FF);
    } else {
      fill(240);
      stroke(150);
    }
    ellipse(this.position.x, this.position.y, size, size);
  }

  public void changeDir() {
    if (this.direction.equals("right")) this.direction = "left";
    else this.direction = "right";
  }

  public void key(boolean l, boolean r, boolean u, boolean d) {
    left = l;
    up = u;
    right = r;
    down = d;
  }

  public boolean isIn(int x, int y, int extraWidth) {

    PVector dist = new PVector(x-position.x, y-position.y);
    return dist.mag() < (this.size/2 + extraWidth);
  }

  public int minX() {
    return (int)this.position.x - (size/2);
  }
  public int maxX() {
    return (int)this.position.x + (size/2);
  }
  public int minY() {
    return (int)this.position.y - (size/2);
  }
  public int maxY() {
    return (int)this.position.y + (size/2);
  }
}

//dont actually know why i have this method
public void npcs() {
      npcMove();
}
public void npcMove() {

      //move, shoot - check string
      for (String chk : chars.keySet ()) {
            if (chk.substring(0, 6).equals("patrol")) {
                  if (chars.get(chk).direction.equals("right")) {
                        chars.get(chk).key( false, true, false, false) ;
                  } else {
                        chars.get(chk).key( true, false, false, false) ;
                  }
                  if (chars.get(chk).lastShot  > frameRate*2) {
                        npcShotgun(chars.get(chk), 3);
                        chars.get(chk).lastShot = 0;
                  }
            } else if (chk.substring(0, 5).equals("still")) {
                  //do nothing movment wise8)
                  if (chars.get(chk).lastShot  > frameRate*0.5f) {
                        npcShotgun(chars.get(chk), 1);
                        chars.get(chk).lastShot = 0;
                  }
            } else if (chk.substring(0, 3 ).equals("uzi")) {
                  //do nothing movment wise8)
                  //THIS IS KINDA MESSY HACKY SHIT
                  //shooting check every tenth of a sec
                  if (chars.get(chk).lastShot  > frameRate*0.1f ) {
                        chars.get(chk).uzicount++;//increment uzicount
                        if (chars.get(chk).uzicount <10) {//if count smaller than 60, shoot 
                              npcShotgun(chars.get(chk), 1);
                              chars.get(chk).lastShot = 0; // reset shot thing
                        }
                        //println(chars.get(chk).uzicount );//print shit
                  }
                  if (chars.get(chk).uzicount > 160)chars.get(chk).uzicount = 0;
            } else if (chk.substring(0, 5).equals("shgun")) {
                  if (chars.get(chk).direction.equals("right")) {
                        chars.get(chk).key( false, true, false, false) ;
                  } else {
                        chars.get(chk).key( true, false, false, false) ;
                  }
                  if (chars.get(chk).lastShot  > frameRate*2) {
                        npcShotgun(chars.get(chk), 20);
                        chars.get(chk).lastShot = 0;
                  }
            }
      }
      //check if should change dir- change string
      for (String chk : chars.keySet ()) {
            if (chk.substring(0, 6).equals("patrol") || chk.substring(0, 5).equals("shgun") ) {
                  if (frameCount - chars.get(chk).lastframe > 0.8f*frameRate) { // time between changing directions here
                        chars.get(chk).changeDir();
                        chars.get(chk).lastframe = frameCount;
                  }
            }
      }
}


//shoot at player
private void npcShotgun(character nme, int shots) {
      for (int i = 0; i<shots; i++) {
            PVector at = new PVector(chars.get("player").position.x + random(50)-25, chars.get("player").position.y+random(50)-25);

            PVector dir = new PVector(nme.position.x, nme.position.y);
            dir.sub(at);
            dir.mult(-1);  
            bullet b = new bullet((int)nme.position.x, (int)nme.position.y, (int)dir.x, (int)dir.y, nme, 3);
            bullets.add(b);
      }
}

private void introSplash() {
      player.play();
      player.loop();
      state = "intro";
      level = 1;
      time = 0;
} 

/**
 displays level splash screen 
 */
private void splash(int lvl) {
      if (lvl != 1)time += (millis() - timestart);
      level++;
      state = "splash" + lvl;
}

public void lose() {
      reset();
      time = 0;
      state = "lose";
}


private void reset() {
      bullets.clear(); 
      terrain.clear();
      chars.clear();
}
public void level1() { //DONE

      reset();
      endzone = new PVector(400, 0);

      terrain.add(new wall(000, 680, 250, 730));
      terrain.add(new wall(000, 780, 250, 800));
      terrain.add(new wall(250, 680, 500, 730));
      terrain.add(new wall(250, 780, 500, 800));
      terrain.add(new wall(550, 680, 800, 730));
      terrain.add(new wall(550, 780, 800, 800));

      terrain.add(new wall(200, 350, 450, 400));  
      terrain.add(new wall(550, 300, 800, 350));  
      terrain.add(new wall(550, 360, 800, 410));  

      terrain.add(new wall(0, 20, 360, 30));
      terrain.add(new wall(440, 20, 800, 30));

      character player = new character(30, height-40, 20, true, "player");

      chars.put("player", player);


      character pat1 = new character(200, 50, 30, false, "patrol1");
      chars.put ("patrol1", pat1);

      character still1 = new character(100, 400, 30, false, "still1");
      chars.put ("still1", still1);

      character still2 = new character(80, 520, 30, false, "still2");
      chars.put ("still2", still2);

      //character still3 = new character(100, 400, 30, false, "still3");
     // chars.put ("still3", still3);
}

public void level2() {
  
      reset();    

      endzone = new PVector(250, 80);
      //stairs
      terrain.add(new wall(220, 50, 280, 110));
      terrain.add(new wall(220, 50, 260, 110));
      terrain.add(new wall(220, 50, 240, 110));
      //walls
      //bottom
      terrain.add(new wall(0, 760, 260, 770));
      terrain.add(new wall(340, 760, 800, 770));
      //left
      terrain.add(new wall(20, 0, 30, 100));
      terrain.add(new wall(20, 150, 30, 760));
      //right
      terrain.add(new wall(680, 0, 690, 380));
      terrain.add(new wall(680, 450, 690, 760));
      //middle
      terrain.add(new wall(100, 150, 400, 200));
      terrain.add(new wall(200, 250, 300, 260));

      terrain.add(new wall(500, 200, 530, 230));
      terrain.add(new wall(600, 150, 630, 180));
      //terrain.add(new wall(500,200,530,230));
      terrain.add(new wall(230, 670, 260, 700));

      character turret = new character(550, 400, 30, false, "shgun1");
      chars.put ("shgun1", turret);
      character turret2 = new character(250, 225, 30, false, "shgun2");
      chars.put ("shgun2", turret2);

      character still1 = new character(50, 200, 30, false, "uzi1___");
      chars.put ("uzi1___", still1);
      character still2 = new character(80, 400, 30, false, "uzi2___");
      chars.put ("uzi2___", still2);

      character still3 = new character(400, 20, 30, false, "still1");
      chars.put ("still1", still3);


      character player = new character(300, 780, 20, true, "player");
      chars.put("player", player);
} 
public void level3() {
      reset();    

      endzone = new PVector(400, 750);
      //stairs
      terrain.add(new wall(370, 720, 430, 780));
      terrain.add(new wall(370, 720, 410, 780));
      terrain.add(new wall(370, 720, 390, 780));

      //spoarse elements
      terrain.add(new wall(0, 0, 799, 40));
      terrain.add(new wall(100, 350, 130, 480));//table      
      terrain.add(new wall(300, 200, 330, 230));//small box

      terrain.add(new wall(500, 400, 530, 430));
      terrain.add(new wall(630, 500, 660, 530));
      terrain.add(new wall(620, 300, 650, 480));

      terrain.add(new wall(350, 670, 450, 675));


      character pat1 = new character(50, 510, 30, false, "patrol1");
      chars.put ("patrol1", pat1);
      character still1 = new character(50, 320, 30, false, "uzi1___");
      chars.put ("uzi1___", still1);
      character still4 = new character(750, 780, 30, false, "uzi2___");
      chars.put ("uzi2___", still4);

      character still3 = new character(320, 750, 30, false, "still1");
      chars.put ("still1", still3);
      character still2 = new character(480, 750, 30, false, "still2");
      chars.put ("still2", still2);

      character player = new character(400, 50, 20, true, "player");
      chars.put("player", player);
}
public void level4() {
      reset();    

      endzone = new PVector(400, 750);
      //stairs
      terrain.add(new wall(370, 720, 430, 780));
      terrain.add(new wall(370, 720, 410, 780));
      terrain.add(new wall(370, 720, 390, 780));

      terrain.add(new wall (600, 40, 620, 60));

      terrain.add(new wall(00, 120, 300, 128));
      terrain.add(new wall(400, 120, 500, 128));
      terrain.add(new wall(400, 250, 800, 258));

      terrain.add(new wall(50, 450, 500, 458));


      character still1 = new character(720, 50, 30, false, "still1");
      chars.put ("still1", still1);
      character still2 = new character(750, 230, 30, false, "still2");
      chars.put ("still2", still2);
      character still3 = new character(50, 260, 30, false, "uzi1___");
      chars.put ("uzi1___", still3);
      character turret = new character(200, 400, 30, false, "shgun1");
      chars.put ("shgun1", turret);
      character turret2 = new character(100, 600, 30, false, "shgun2");
      chars.put ("shgun2", turret2);
      character still4 = new character(750, 750, 30, false, "uzi3___");
      chars.put ("uzi3___", still4);
       character still5 = new character(100, 760, 30, false, "still3");
      chars.put ("still3", still5);

      character player = new character(150, 50, 20, true, "player");
      chars.put("player", player);
}
public void level5() {
      reset();    
      endzone = new PVector(770, 30 );

      terrain.add(new wall (280, 50, 330, 100));//boxes in ring around endzone
      terrain.add(new wall (300, 180, 350, 230));
      terrain.add(new wall (330, 310, 380, 360));
      terrain.add(new wall (380, 450, 430, 500));
      terrain.add(new wall (480, 580, 530, 630));
      terrain.add(new wall (620, 710, 670, 760));    

      terrain.add(new wall (500,100,550,150));   
      terrain.add(new wall (600,300,650,350));   

      terrain.add(new wall (0, 620, 150, 630));//boxes by player
      terrain.add(new wall (180, 680, 190, 799));      

      character turret = new character(400, 180, 30, false, "shgun1");
      chars.put ("shgun1", turret);
      character still1 = new character(300, 750, 30, false, "still1");
      chars.put ("still1", still1);
      character still3 = new character(750, 680, 30, false, "uzi1___");
      chars.put ("uzi1___", still3);
      
      character still2 = new character(100, 100, 30, false, "still2");
      chars.put ("still2", still2);
      character still4 = new character(200, 200, 30, false, "still3");
      chars.put ("still3", still4);
      character still5 = new character(550, 20, 30, false, "still4");
      chars.put ("still4", still5);
      character still6 = new character(720, 325, 30, false, "still6");
      chars.put ("uzi____6", still6);
      character turret6 = new character(20, 380, 30, false, "shgun1");
      chars.put ("shgun1", turret6);
      character turret7 = new character(400, 780, 30, false, "shgun1");
      chars.put ("shgun6", turret7);


      character player = new character(20, 780, 20, true, "player");
      chars.put("player", player);
}
private class rectangle {
      int x, y, w, h;
      rectangle(int X, int Y, int W, int H) {
            this.x = X;
            this.y = Y;
            this.w = W;
            this.h = H;
      }
      public int getX() {
            return this.x;
      }
      public int getY() {
            return this.y;
      }
      public int getWidth() {
            return this.w;
      }
      public int getHeight() {
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
abstract class thing{

  public abstract boolean isIn(int x, int y, int extraWidth);
  
  public abstract int minX();
  public abstract int maxX();
  public abstract int minY();
  public abstract int maxY();
}

class wall extends thing {
  int x1, y1, x2, y2;

  public wall(int tx1, int ty1, int tx2, int ty2) {
    x1 = tx1;
    y1 = ty1;
    x2 = tx2; 
    y2 = ty2;
  }

  public boolean isIn(int x, int y, int extraWidth) {
    return x > (x1-extraWidth) && x < (x2+extraWidth) && y > (y1-extraWidth) && y < (y2+extraWidth);
  }

  public void render() {
    fill(matrixgreen);
    stroke(origGreen);
    rect(x1, y1, x2-x1, y2-y1);
  }

  public int minX() {
    return this.x1;
  }
  public int maxX() {
    return this.x2;
  }
  public int minY() {
    return this.y1;
  }
  public int maxY() {
    return this.y2;
  }
}
      public void settings() {  size(800, 800);  smooth(); }
      static public void main(String[] passedArgs) {
            String[] appletArgs = new String[] { "bukletsim" };
            if (passedArgs != null) {
              PApplet.main(concat(appletArgs, passedArgs));
            } else {
              PApplet.main(appletArgs);
            }
      }
}
