import ddf.minim.*;
import ddf.minim.analysis.*;
import ddf.minim.effects.*;
import ddf.minim.signals.*;
import ddf.minim.spi.*;
import ddf.minim.ugens.*;



import java.util.*;
import ddf.minim.*;

HashSet<bullet> bullets = new HashSet<bullet>();
HashSet<wall> terrain = new HashSet<wall>();
HashMap<String, character> chars = new HashMap<String, character>();
PFont f;
PVector endzone;

color origGreen = #00FF1F;
color matrixgreen;// = #00FF1F;
color backgroundcol = 0;
//maybe for colors fuckery?
String beats = "music_for_242_proj3.mp3";

Minim minim;
AudioPlayer player;

color orange  =#FF8D00;
int lastSwitch = 0;

int time = 0;//players total time

int timestart = 0;

boolean hardmode = false;



//keysmpressed
boolean w=false, a=false, s=false, d=false;
Quadtree quad;

void setup() {
      size(800, 800);  
      smooth();
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
void draw() {
      //println(millis()/1000);
      //background
      stroke(backgroundcol);      
      fill(0, 30);
      if (millis()/400 % 2 == 0 && hardmode)  fill(#FF8D00, 30 );  
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

void mousePressed() {
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
void keyPressed() {
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
void keyReleased() {
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