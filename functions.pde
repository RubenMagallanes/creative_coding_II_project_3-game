
//dont actually know why i have this method
void npcs() {
      npcMove();
}
void npcMove() {

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
                  if (chars.get(chk).lastShot  > frameRate*0.5) {
                        npcShotgun(chars.get(chk), 1);
                        chars.get(chk).lastShot = 0;
                  }
            } else if (chk.substring(0, 3 ).equals("uzi")) {
                  //do nothing movment wise8)
                  //THIS IS KINDA MESSY HACKY SHIT
                  //shooting check every tenth of a sec
                  if (chars.get(chk).lastShot  > frameRate*0.1 ) {
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
                  if (frameCount - chars.get(chk).lastframe > 0.8*frameRate) { // time between changing directions here
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

void lose() {
      reset();
      time = 0;
      state = "lose";
}


private void reset() {
      bullets.clear(); 
      terrain.clear();
      chars.clear();
}
void level1() { //DONE

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

void level2() {
  
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
void level3() {
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
void level4() {
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
void level5() {
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

