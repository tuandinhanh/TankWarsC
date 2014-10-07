import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.Timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import ScreenManagement.*;

import java.io.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import  sun.audio.*;

//Game class_
public class Game implements KeyListener {
   

    public static void main(String args[]) {
        Game tankGame = new Game(); 
        tankGame.run();
        DataClass.readFile();
    }

    protected static final DisplayMode POSSIBLE_MODES[] = {
      // new DisplayMode(1280, 800, 32, 0),  
      //Support for higher Resolutions, maybe? 
     //Would need lots of additional resizing.
       new DisplayMode(800, 600, 32, 0),
       new DisplayMode(800, 600, 24, 0),
       new DisplayMode(800, 600, 16, 0),
    };
    
    // Testing 
    protected boolean testingUpdate = true;
    protected boolean testingDraw = false;
    
    //Credits running?
    public static int credits = 0;
    public static int titleScreenOpen = 1; // game starts with title screen open
    public static int helpScreenOpen = 0;
    
    //Variable Base
    public static DataClass dc = new DataClass();
    protected ScreenManager screen; // creates object to control screen
    public static MusicPlayer music;
    protected SoundPlayer soundShot;
    protected SoundPlayer clap;
    protected Image bgImage; // background image
    protected Image TankImage; // tank's image
    protected Image TankImage2;
    protected Animation TankStill = new Animation();
    protected Animation TankStill2 = new Animation();
    protected Image terrainTexture;
    protected Image headerBar; //top header image
    protected Image healthIcon; // health icon
    protected Image healthBar; // health bar full
    protected Image powerBar; // power bar full
    protected Image powerIcon; //power bar up arrow icon
    protected Image weaponIndicatorBG; // background for weapon indicator
    protected Image weaponNormal;
    protected Image weaponHersco;
    protected Image weaponNuclear;
    protected Image weaponJoker;
    protected Image modemove;
    protected Image modeangle;
    protected Image modepower;
    protected Image modeweapon;
    protected Image modeBG;
    protected Image fuelBar;
    protected Image fuelIcon;
    protected Image arrow;
    
    // Tutorial images
    protected Image blackarrowleft = loadImage("images/blackarrowleft.png");
    protected Image blackarrowright = loadImage("images/blackarrowright.png");
    protected Image blackarrowup = loadImage("images/blackarrowup.png");
    protected Image blackarrowdown = loadImage("images/blackarrowdown.png");
    protected Image keyarrowleft = loadImage("images/keyarrowleft.png");
    protected Image keyarrowright = loadImage("images/keyarrowright.png");
    protected Image keyarrowup = loadImage("images/keyarrowup.png");
    protected Image keyarrowdown = loadImage("images/keyarrowdown.png");
    protected Image keyspacebar = loadImage("images/keyspacebar.png");
    // Player 1 tutorial controls
    protected Image W = loadImage("images/keys/W.png");
    protected Image A = loadImage("images/keys/A.png");
    protected Image S = loadImage("images/keys/S.png");
    protected Image D = loadImage("images/keys/D.png");
    protected Image Q = loadImage("images/keys/Q.png");
    protected Image E = loadImage("images/keys/E.png");
    protected Image F = loadImage("images/keys/F.png");
    protected Image R = loadImage("images/keys/R.png");
    // Player 2 tutorial controls
    protected Image up = loadImage("images/keys/UP.png");
    protected Image left = loadImage("images/keys/LEFT.png");
    protected Image down = loadImage("images/keys/DOWN.png");
    protected Image right = loadImage("images/keys/RIGHT.png");
    protected Image comma = loadImage("images/keys/COMMA.png");
    protected Image period = loadImage("images/keys/PERIOD.png");
    protected Image forwardslash = loadImage("images/keys/FORWARDSLASH.png");
    protected Image shift = loadImage("images/keys/SHIFT.png");

    protected Image spaceBarKey = loadImage("images/keys/SPACEBAR.png");

    
    //Used to count shots before un-paralyzed (Start of un-paralyzed)
    int PARALYZED = 4; //edit this to change amount of shots until un-paralyzed
    int shotcounter = PARALYZED;
    int shotcounter2 = PARALYZED;
    
    // Tanks
    Tank Tank1 = new Tank();
    Tank Tank2 = new Tank();
    
    //Weapon

    
    //**Objects in Game
    protected Sprite Shot; // displays weapon 1 for player 1 in the top left corner
    protected Sprite SecondShot; //displays weapon 2 for player 1 in the top left corner
    protected Sprite Shot2; // displays weapon 1 for player 2 in the top right corner 
    protected Sprite SecondShot2; // displays weapon 2 for player 2 in the top right corner
    protected Sprite ThirdShot;
    protected Sprite ThirdShot2;  
    protected Sprite FourthShot;
    protected Sprite FourthShot2;
    protected Sprite BOOM; // explosion
    protected Sprite TankBoom; //tank explosion
    protected Sprite TankShot; //tanks shot
    protected Sprite TankShot2;
    protected Sprite SecondaryTankShot; //second tank shot
    protected Sprite SecondaryTankShot2;
    protected Sprite ThirdTankShot;//HEEEERSCOOOO BOMB
    protected Sprite ThirdTankShot2;
    protected Sprite FourthTankShot;
    protected Sprite FourthTankShot2;//blake bomb
    protected Sprite cloudSunny1; //cloud sunny
    protected Sprite cloudSunny2; //cloud sunny
    protected Sprite cloudSunny3; //cloud sunny
    protected Sprite cloudDark1; //cloud dark
    protected Sprite cloudDark2; //cloud dark
    protected Sprite cloudDark3; //cloud dark
    protected Sprite rainSprite;
    
    

    Weapon T1Weapon = new Weapon(this, Tank1);
    Weapon T2Weapon = new Weapon(this, Tank2);
    //*******************
    ////Necessary Global Variables////
    public int restartgame = 0; //added by Ryan Kleckner 4/8/08
    public boolean firstfall = true;//sees if the tank is falling for the first time
    public boolean firstfall2 = true;//sees if the tank is falling for the first time
    public boolean PauseMenuOpen = false;  // added 11/05/08 by Devin Barna
    public boolean ControlsOverlayOpen = false;
    public boolean tutorialmode = false;
    public boolean muted = false;
    
    public static String gameMusic = "ffxiii.wav";
    public static String titleMusic = "ffxiii.wav";
    public static String helpMusic = "ffxiii.wav";
    public static String creditsMusic = "ffxiii.wav";
    		
    public int GameSTATE=1; //determines what gameplay
    //0=dynamic
    //1=turn based
    //3=restart game
    public boolean GameSTATEchanged = true; // has GameSTATE been changed since starting the game
    // (needed in order to change GameSTATE during the game)
    public int turn=1;
    //TEST
    //if turn =1 player 1 turn
    //if turn =2 player 2 turn
    public int hitTest=0; 
    //hittest=2 hit ground or tank
    //hittest=0 when shot is reset
    //hittest=2 hit ground or tank
    //hittest=0 when shot is reset
    public int tankshoot=0; //only time it equals zero
    //tankshoot=1 means shot was just fired
    //tankshoot>1 means nothing besides that a shot was not just fired. need to fix this
    public int tankshoot2=0; //only time it equals zero
    //tankshoot=1 means shot was just fired
    //tankshoot>1 means nothing besides that a shot was not just fired. need to fix this
    public float Gravf; //Gravity in the game
    //////////// rename these variables...
    public static int start=0;
    public boolean TankCreated = false; //Gate to detect if a tank was randomly placed onto the map or not
    /***Holder and Counters***/
    int a=0;
    public int windcount=0;//determines if the wind needs to be changed
    public float Windf = .00f;
    public int WindVar=0;
    //////////////////////
    ///Dynamic Ground variables////
    public int basex=0;
    public int basey=900;
    public int topx=0;
    public Integer topy[] = new Integer[9500]; 
    public int AirStrikeAmmo1 = 1;
    public int AirStrikeAmmo2 = 1;
        
    public int sleepTime = 25;
        
    // moving and jumping
    public int maxMove = 100; 
    public int tankMove = maxMove;
    public boolean moving = false;
    public boolean jumpUp = false; 
    public boolean jumpFall = false;
    public int maxJump = 25; 
    public int currJump = 0;
    public int fuel = 110;
    public int curfuel = fuel;  //Fuel variable used for both tanks.
    public int fueldepletion = 2;

    
    // test suite
    public int ptLevel = 1, ptX = 0, ptY = 0;
    protected boolean testing = false;
    // used when a method doesn't set any checkable values
    protected boolean check = true;
    protected boolean caseChecks[] = new boolean[7];
    
    public boolean nextTurn = true;
    
     //added by Ryan Kleckner 4/14/08

    int p1secondShots = 2;
    int p2secondShots = 2;
    
    
    //random terrain selector
    /*******************************************************
     *Added by Ryan K
     *Selects one of the terrains at random
     *******************************************************
     */
    String[] nums = {"1", "2", "3"};
    int ran = (int)(Math.random() * nums.length);
    String levelNumber = nums[ran];
    
    int freqValue;
    int terrainNum;
    int firstNum = 900;
    int secondNum = 950;    
    

    public void ChangeLevel(String num) {
        firstNum = 600;
        secondNum = 950;    
        freqValue = 0;
        terrainNum = 0;
        basex=0;
        basey=900;
        topx=0;
        topy = new Integer[9500]; 
        topy[basex]= null;
        levelNumber = num;  
    }
    
    public void loadImages() {
        // loads images into buffer
        bgImage = loadImage("images/Background1.png"); // Background Image
        headerBar = loadImage("images/headerBarBlue.png"); // Header Image
        healthIcon = loadImage("images/healthIcon.png"); // Health Icon
        healthBar = loadImage("images/healthBar.png"); // Health Bar full
        powerBar = loadImage("images/powerBar.png"); // Power Bar full
        powerIcon = loadImage("images/uparrowicon.png"); //power icon
        fuelBar = loadImage("images/FuelBar.png"); // Fuel Bar full
        fuelIcon = loadImage("images/FuelBarrel.png"); // Icon next to fuel bar
        arrow = loadImage("images/newarrow2.png"); //Arrow indicating which tank is moving
        
        // Weapon Indicator Images
        weaponIndicatorBG = loadImage("images/weaponIndicatorBG.png"); // BG
        weaponNormal = loadImage("images/weaponNormal.png");
        weaponHersco = loadImage("images/weaponHersco.png");
        weaponNuclear = loadImage("images/weaponNuclear.png");
        weaponJoker = loadImage("images/weaponJoker.png");
        modemove = loadImage("images/MMode.png");
        modeangle = loadImage("images/TMode.png");
       modepower = loadImage("images/PMode.png");
        modeweapon = loadImage("images/WMode.png");
        modeBG = loadImage("images/ModeBG.png");
        
        TankImage = loadImage(DataClass.getP1Tank());
        TankImage2 = loadImage(DataClass.getP2Tank());
        Image Explode = loadImage("images/explosion.gif");
        Image Bullet = loadImage("images/Bullet.jpg");
        Image Bullet2 = loadImage("images/Bullet2.jpg");
        Image Bullet3 = loadImage("images/Bullet3.jpg");
        Image lilBoom = loadImage("images/lilboom.gif");
        Image bomb = loadImage("images/newweapon1.png");
        Image bomb2 = loadImage("images/newweapon2.png");
        Image bomb3 = loadImage("images/newweapon3.png");
        Image bomb4 = loadImage("images/newweapon4.png");
        Image bomb5 = loadImage("images/newweapon5.png");
        Image hersco = loadImage("images/hersco.png");
        Image smallhersco = loadImage("images/herscosmall.png");
        Image cloud1 = loadImage("images/Cloud1.png");

        //Image rainImage = loadImage("images/rain.png");
        Image blake = loadImage("images/joker.png");
        Image smallblake = loadImage("images/smalljoker.png");
        
        
        //Initiate Sounds
        soundShot = new SoundPlayer();
    
        // create sprites
        
        // Animation rainfallAnimation = new Animation();
        // rainfallAnimation.addFrame(rainImage, 300);
        
      //  Animation TankStill = new Animation();
        // The add frames need to have -1 duration for color changes to take effect (the frames of the set color are added when game starts)
        TankStill.addFrame(TankImage,-1);
         
      //  Animation TankStill2 = new Animation();
        TankStill2.addFrame(TankImage2,-1);
        
        Animation TankHit = new Animation();
        TankHit.addFrame(lilBoom,450);
        
        Animation blowUp = new Animation();
        blowUp.addFrame(Explode,300);
        
        Animation bul = new Animation();
        bul.addFrame(Bullet,300);
        bul.addFrame(Bullet2,200);
        
        Animation bul2 = new Animation();
        bul2.addFrame(bomb, 300);
        
        Animation shoot = new Animation();
            shoot.addFrame(Bullet,200);
            shoot.addFrame(Bullet2, 200);
            shoot.addFrame(Bullet3,200);  
        
        Animation shoot2 = new Animation();
        shoot2.addFrame(bomb,200);
        shoot2.addFrame(bomb2,200);
        shoot2.addFrame(bomb3,200);
        shoot2.addFrame(bomb4,200);
        shoot2.addFrame(bomb5,200);
        
        Animation shoot3 = new Animation();
        shoot3.addFrame(hersco, 200);
        
        Animation shoot5 = new Animation();
        shoot5.addFrame(blake, 200);
        
        Animation bul3 = new Animation();
        bul3.addFrame(smallhersco, 200);
        
        Animation bul5 = new Animation();
        bul5.addFrame(smallblake,200);
        Animation cloudSun1 = new Animation();
        cloudSun1.addFrame(cloud1, 200);
        
        
        // Take the Image Frames and Create and Object(Sprite). Sprites have Attributes for locating its position, movement, and State. See Sprite.Java
		  Tank1.setTankSprite(new Sprite(TankStill));
		  Tank2.setTankSprite(new Sprite(TankStill2));
		  Shot = new Sprite(bul);
		  SecondShot = new Sprite(bul2);
		  Shot2 = new Sprite(bul);
		  SecondShot2 = new Sprite(bul2);
		  ThirdShot = new Sprite(bul3);
		  ThirdShot2 = new Sprite(bul3);
		  FourthShot = new Sprite(bul5);
		  FourthShot2 = new Sprite(bul5);
		  TankShot = new Sprite(shoot);
		  TankShot2 = new Sprite(shoot);
		  SecondaryTankShot = new Sprite(shoot2);
		  SecondaryTankShot2 = new Sprite(shoot2);
		  ThirdTankShot = new Sprite(shoot3);
		  ThirdTankShot2 = new Sprite(shoot3);
		  TankBoom = new Sprite(TankHit);
		  BOOM = new Sprite(blowUp);
		  FourthTankShot = new Sprite(shoot5);//new weapon
		  FourthTankShot2 = new Sprite(shoot5); 
		  T1Weapon.setSprite(TankShot);
		  T2Weapon.setSprite(TankShot);
    }


    private Image loadImage(String fileName) {
        return new ImageIcon(fileName).getImage();
    }
    
    public void TestTurret(Sprite tank)
    {
        tank.setY(100f);
    }

    
	public class MouseClass extends MouseAdapter{
	    
	        public void mousePressed(MouseEvent e)
	        {
		}
	}

	public void run() {
        //else continue with game data...
        screen = new ScreenManager();
            

        String track = gameMusic;
    	music = new MusicPlayer(track);
		music.l();
        try
        {
        DisplayMode displayMode =
        screen.findFirstCompatibleMode(POSSIBLE_MODES);
        screen.setFullScreen(displayMode);

        Window window = screen.getFullScreenWindow();
        window.addKeyListener(this);
        window.addMouseListener(new MouseClass());
        loadImages();
        //CountdownTimer.startCounter();  //starts the timer
        while (restartgame < 1)
            {
            animationLoop();
            }
        }   
            finally
            {
            System.out.println("Escaping");
            System.out.println("RESTART Variable: "+ restartgame);
            //is only called when restartgame is set to 1 to exit the animationloop
            //and restart the game
    
                screen.restoreScreen();//restores active screen
                Game.main(null);//calls Game.java's main class to start a new game
                
            }
        
        
        // for testing: since there is no other value to check here
        if (testing)
            check = true;
    }

    public void animationLoop() {
        long startTime = System.currentTimeMillis();
        long currTime = startTime;

       // while (currTime - startTime < TIME) {
          while (restartgame < 1) { //run loop infinitely till user exits.
          //***************************************************************
          //added restartgame variable Ryan K 4/8/08
          //***************************************************************
          
            long elapsedTime =
                System.currentTimeMillis() - currTime;

            currTime += elapsedTime;

            // update the sprites
            update(elapsedTime);
            
            // draw and update the screen            
            Graphics2D g = screen.getGraphics();            
            draw(g);
           // drawFade(g, currTime-startTime);
            g.dispose();
            screen.update();
            
            // break to do tests
            if (testing) {
                check = true;
                break;
            }
            
            // take a nap for garbage
            try {
                Thread.sleep(sleepTime);
            }
            catch (InterruptedException ex) { }
        }   
          
    }
    
    public void update(long elapsedTime)
    {

		if (credits==1 && Credits.creditsOn && Credits.count < Credits.loop){
            Credits.up(); 
		}
        
        
        //Checks if a jump needs to be ended
        if (jumpUp) {
            if (currJump > maxJump) {
                currJump = 0;
                jumpUp = false;
                jumpFall = true;
            }
            else 
                currJump+=1;
        }
        
        // needed for point test
        //if the tank shot goes off the screen, set the state to 0. UPDATED 4/5
        T1Weapon.outOfBoundsCheck();
        T2Weapon.outOfBoundsCheck();
        
       //To create gravity, there needs to be a decay on the velocity at a constant rate.
       //The decay must be at the correct interval to create a natural trajectory for the shot.
        if(GameSTATE != 3 && GameSTATEchanged == false)
        {
            GameSTATE=DataClass.getGameState();
        }
        String x = DataClass.getGravity();
        Gravf = Float.valueOf(x.trim()).floatValue();
        WindVar = DataClass.getWind();
        if(WindVar==1)
        {
            if(tankshoot==1||tankshoot2==1)
            {
                Windf=.001f+.004f*(float)Math.random();
                if(Math.random()>.5)Windf=Windf*-1f;
            }
        }
        else if(WindVar==0)
                Windf=.00f;
            else if(Windf==.00f&&WindVar==2)
                {
                Windf=.001f+.004f*(float)Math.random();
                if(Math.random()>.5)Windf=Windf*-1f;
                }
            else if (WindVar==3)
            {
                if(windcount<20+(int)20*Math.random())
                    windcount++;
                else
                {
                    windcount=0;
                    Windf=.001f+.004f*(float)Math.random();
                    if(Math.random()>.5)Windf=Windf*-1f;
                }
            }

        // DONT FORGET TO GRAVITY
       // T1Weapon.GravityAndCollision();
        //T2Weapon.GravityAndCollision();
        
        if (turn == 1) T1Weapon.GravityAndCollision(Tank2);
        if (turn == 2) T2Weapon.GravityAndCollision(Tank1);
             
        // update objects on the screen bring the current time with them
       //(Can be used for timing events.
        Tank1.getTankSprite().update(elapsedTime);
        Tank2.getTankSprite().update(elapsedTime);
        Shot.update(elapsedTime);
        SecondShot.update(elapsedTime);
        Shot2.update(elapsedTime);
        SecondShot2.update(elapsedTime);
        TankShot.update(elapsedTime);
       
    }//end Update()
    
       
    public BufferedImage rotateImage(Image image, double angle) {
        // Create the BufferedImage
        BufferedImage img = new BufferedImage(100,100,BufferedImage.TYPE_INT_ARGB);
        Graphics bg = img.getGraphics();
        bg.drawImage(image, 0, 0, null);
        bg.dispose();
        
        // Rotate the image
        BufferedImage rotated = new BufferedImage(100,100,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform rot = new AffineTransform();
        rot.rotate(angle, 19, 14);
        g2d.transform(rot);
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();
        
        return rotated; 
    } //End rotateImage(Image, Angle)

    public float textVisibility=1000;
    public synchronized void draw(Graphics2D g)
    {
		if(credits==0){
			g.drawImage(bgImage, 0, 0, null);  // draws background
		
		DrawTerrain(g); // draws a flat surface for the tanks to land on. Use this function to implement a dynamic terrain generation
		
        if (TankCreated != true) // if no tank has been created, then create one.
        {
            CreateTank(g);
            Tank1.setTurretAngleX(Math.round(Tank1.getTankSprite().getX() + (int)(Tank1.getTankSprite().getWidth()*.5)));
            Tank1.setTurretAngleY(Math.round(Tank1.getTankSprite().getY() - (int)(Tank1.getTankSprite().getHeight())));
            Tank2.setTurretAngleX(Math.round(Tank2.getTankSprite().getX() + (int)(Tank2.getTankSprite().getWidth()*.5)));
            Tank2.setTurretAngleY(Math.round(Tank2.getTankSprite().getY() - (int)(Tank2.getTankSprite().getHeight())));
            TankCreated = true;
        } // end tank creation
        
        T1Weapon.setGraphics(g);
        T2Weapon.setGraphics(g);   
        
        // Update the Selected Weapon Indicator and Ammo
        updateWeaponandModeIndicator(g);
        
        // using new GroundCollision parameters -- Eric M.  
        GroundCollision(Tank1.getTankSprite(), 1);
        GroundCollision(Tank2.getTankSprite(), 2); // check if there is ground underneath a tank, if not let it fall. This function is crudely done.
        // creating a center of gravity for the objects and doing a better check on when a tank should fall can be changed in this function.

        //  Draw the tank's turret. 19 = 1/2 centering the position of the turret on the tank. 3 = making the turret at the correct height of the tank.
        float[] dash = {25f,25f};
        g.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
        		1.0f, dash, 0f));
        g.setColor(new Color(0f, 0f, 0f, 0.3f));
        AffineTransform tr = g.getTransform();
        Tank Tank = Tank1;
        if (turn == 2) Tank = Tank2;
        
        if (Mode1 == 1 || Mode2 == 1)
        {
	        // don't touch the magic numbers!
	        g.rotate(Tank.getAngle()*-0.018+1.625, Math.round(Tank.getTankSprite().getX())+(int)(Tank.getTankSprite().getWidth()*.5),
	        		Math.round(Tank.getTankSprite().getY()));
	        g.drawLine(Math.round(Tank.getTankSprite().getX())+(int)(Tank.getTankSprite().getWidth()*.5),
	        		Math.round(Tank.getTankSprite().getY()),
	        		Math.round(Tank.getTankSprite().getX())+(int)(Tank.getTankSprite().getWidth()*.5),
	        		Math.round(Tank.getTankSprite().getY())-800);
	        g.setTransform(tr);
        }
        g.setColor(Color.black);
        g.drawLine(Math.round(Tank1.getTankSprite().getX())+(int)(Tank1.getTankSprite().getWidth()*.5),
        		Math.round(Tank1.getTankSprite().getY()),
        		Tank1.getTurretAngleX(),Tank1.getTurretAngleY());
        g.drawLine(Math.round(Tank2.getTankSprite().getX())+(int)(Tank2.getTankSprite().getWidth()*.5),
        		Math.round(Tank2.getTankSprite().getY()),
        		Tank2.getTurretAngleX(),Tank2.getTurretAngleY());
        g.setStroke(new BasicStroke(1));
        
        if (nextTurn == true)
        {
        	g.setFont(new Font("SansSerif", Font.BOLD, 15));
        	g.setColor(new Color(0f, 0f, 0f, (float)textVisibility/1000f));
        	if (turn == 1)
        		g.drawString("Your turn!", Tank1.getTankSprite().getX()-15,
        					Tank1.getTankSprite().getY()-40);
        	else
        		g.drawString("Your turn!", Tank2.getTankSprite().getX()-15,
    					Tank2.getTankSprite().getY()-40);
        	if (textVisibility > 0)
        		textVisibility -= 15;
        	else
        	{
        		nextTurn = false;
        		textVisibility = 1000;
        	}
        }
             
        //redraw tank in its new position
        g.drawImage(rotateImage(Tank1.getTankSprite().getImage(), Tank1.getTankSlant()), Math.round(Tank1.getTankSprite().getX()), Math.round(Tank1.getTankSprite().getY()), null);
           
        //player 2 tank image
        g.drawImage(rotateImage(Tank2.getTankSprite().getImage(), Tank2.getTankSlant()), Math.round(Tank2.getTankSprite().getX()), Math.round(Tank2.getTankSprite().getY()), null);

    	
        g.setColor(Color.GRAY);
        
            if(Tank1.getWeapon2() == 0)
            {
                g.drawImage(Shot.getImage(), 8, 10, null);
            }
            else if(Tank1.getWeapon2() == 1)
            {
                g.drawImage(SecondShot.getImage(), 0, 0, null);
            }
            else if(Tank1.getWeapon2() == 2)
            {
                g.drawImage(ThirdShot.getImage(), 0, 0, null);
            }
            else if(Tank1.getWeapon2() == 4)
            {
                g.drawImage(FourthShot.getImage(),0,0,null);
            }
            if(Tank2.getWeapon2() == 0)
            {
                g.drawImage(Shot2.getImage(), 516, 10, null);
            }
            else if(Tank2.getWeapon2() == 1)
            {
                g.drawImage(SecondShot2.getImage(), 508, 0, null);
            }
            else if(Tank2.getWeapon2() == 2)//Can't display till image is smaller.
            {
                g.drawImage(ThirdShot2.getImage(), 508, 0, null);
            }
            else if(Tank2.getWeapon2() == 4)
            {
                g.drawImage(FourthShot2.getImage(), 508, 0, null);
            
            }
        
        DrawMessages(g); // draw messages such as tank health, power..ect
        
        //checks if Player 1's health is 0, this could be made into a function.
        checkHealth(Tank1.getHealth(), Tank1.getTankSprite(), g);
        checkHealth(Tank2.getHealth(), Tank2.getTankSprite(), g);
        
            //checks if bullet hit ground if it did creates hole and sets state=0
            if (T1Weapon.getSprite().getState()== 1)// if a shot has been fired   
                T1Weapon.fireShot();
            if (T2Weapon.getSprite().getState()== 1)// if a shot has been fired   
                T2Weapon.fireShot();
                


            //if player 1 fired checks to see what he fired and fires that shot.
         if (tankshoot == 1)
         {            
        	T1Weapon.TankFire();                           
        	tankshoot++;
         } 
         else if (tankshoot == 11)
         {
        	 T2Weapon.TankFire(); 
        	 tankshoot++;
         }

        
        //if the shot is currently in the air, update the image of the shot.
         T1Weapon.updateSpriteInAir(); 
         T2Weapon.updateSpriteInAir();
  
         T1Weapon.explosionOnContact();
         T2Weapon.explosionOnContact();
             
    
		}//end if credits == 0   
		
		if (tutorialmode) 
		{
			drawTutorial(g);
			g.setColor(Color.white);
			g.drawString("Press 'C' to view the controls", 300, 562);
			g.setColor(Color.black);
			g.drawString("Game Controls", 355, 45);
		}
		else
		{
			g.setColor(Color.white);
			g.drawString("Press 'C' to view the controls", 300, 562);
		}
		
		g.setColor(Color.white);
		g.drawString("Press 'O' for options", 300, 575);
		
		//Wouldn't have worked without Zach K. Can't break out of while loops but can break out of if loops.
		if (credits==1 && Credits.creditsOn && Credits.count < Credits.loop){// Draws credits
            Credits.draw(g); 
		}
		
		else if(titleScreenOpen == 1) {
			MainScreen.draw(g);
		}//end MainScreen drawing
		
		else if(helpScreenOpen == 1) {
			HelpScreen.draw(g);
		}// end HelpScreen open
    }//end of Draw phase...
    
    private void drawTutorial(Graphics g)
    {
    	
    	// draw text boxes and all that
    /*	if (turn == 1)    
    	{
    		g.drawImage(blackarrowleft, 170, 75, null);
    		g.drawImage(keyarrowup, 240, 70, null);
    		g.drawImage(keyarrowdown, 290, 70, null);
    		g.drawString("to change mode", 240, 140);
    	}
    	else if (turn == 2)
    	{
    		g.drawImage(blackarrowright, 580, 75, null);
    		g.drawImage(keyarrowup, 520, 70, null);
    		g.drawImage(keyarrowdown, 470, 70, null);
    		g.drawString("to change mode", 470, 140);
    	}
    	*/
    	int barX = 0;
    	int barY = 0;
        int barXOffset = 50;
        int barYOffset = 150;
        int shifty = 50;
        
        if (turn == 1)
        {
        	if (Tank1.getTankSprite().getY() > 520) barYOffset = -barYOffset;
        	barX = (int)Tank1.getTankSprite().getX()-barXOffset;
        	barY = (int)Tank1.getTankSprite().getY()-barYOffset;
        }
        else if (turn == 2)
        {
        	if (Tank2.getTankSprite().getY() > 520) barYOffset = -barYOffset;
        	barX = (int)Tank2.getTankSprite().getX()-barXOffset;
        	barY = (int)Tank2.getTankSprite().getY()-barYOffset;
        }
        if (barX < 35) barX = 35;
        else if (barX > 675) barX = 675;
        

    	
        if (turn == 1)
        {
	        g.drawString("to move!", barX+80, barY+shifty);
	        g.drawString("to aim!", barX+80, barY);
	        g.drawString("to adjust power!", barX+80, barY-shifty);
	        //2014 g.drawString("to change weapon!", barX+80, barY-(2*shifty));
            g.drawString("to fire!", barX+80, barY-(2*shifty)+6);
	     // g.drawImage(blackarrowup, 30, 110, null);
	        
	        // Fire
	        g.drawImage(spaceBarKey, barX, barY-105 ,null);
	        //2014 g.drawString("to fire!", 210, 100);        
	        // Change Weapon
	        //2014 g.drawImage(R, barX, barY-115, null);
	        // Power
	        g.drawImage(comma, barX, barY-65, null);
	        g.drawImage(period, barX+35, barY-65, null);  
	        // Aim
	        g.drawImage(up, barX, barY-15, null);
	        g.drawImage(down, barX+35, barY-15, null);
	        // Move
	        g.drawImage(left, barX, barY+35, null);
	        g.drawImage(right, barX+35, barY+35, null);
	        
	     // g.drawString("Ammo", 10, 160);
	        
        }
        else if (turn == 2)
        {
	   /*     if (Mode2 == 0) g.drawString("to move!", barX+80, barY+55);
	        if (Mode2 == 1) g.drawString("to aim!", barX+80, barY+55);
	        if (Mode2 == 2) g.drawString("to adjust power!", barX+80, barY+55);
	        if (Mode2 == 3) 
	        {

	        } */
        	
         //2014 g.drawString("to change weapon!", barX+80, barY+55);
         //2014 g.drawImage(blackarrowup, 775, 110, null);
         //2014 g.drawString("Ammo", 750, 160);
        	
	        g.drawString("to move!", barX+80, barY+shifty);
	        g.drawString("to aim!", barX+80, barY);
	        g.drawString("to adjust power!", barX+80, barY-shifty);
	        //2014 g.drawString("to change weapon!", barX+80, barY-(2*shifty));
            g.drawString("to fire!", barX+80, barY-(2*shifty)+6);
	     //2014 g.drawImage(blackarrowup, 30, 110, null);
	        
	        // Fire
	        g.drawImage(spaceBarKey, barX, barY-105 ,null);
	     //2014 g.drawString("to fire!", 525, 100);        
	        // Change Weapon
	        //2014 g.drawImage(forwardslash, barX, barY-115, null);
	        // Power
	        g.drawImage(comma, barX, barY-65, null);
	        g.drawImage(period, barX+35, barY-65, null);  
	        // Aim
	        g.drawImage(up, barX, barY-15, null);
	        g.drawImage(down, barX+35, barY-15, null);
	        // Move
	        g.drawImage(left, barX, barY+35, null);
	        g.drawImage(right, barX+35, barY+35, null);
	        
	     //   g.drawString("Ammo", 10, 160);
        	
        }
    }

    //Used to tell when to play sound... if not included sound repeats
    boolean EndingActive = false;
    int explosionFrames = 0;
    private void checkHealth(int health, Sprite player, Graphics g)//makes end of game screen.
    {
        if (health <= 0)
        {
            int playerNumber = 2;
            if (player.equals(Tank2.getTankSprite()))
                playerNumber = 1;
             Color c = new Color(1.0f, 1.0f, 1.0f, 0.6f);
            g.setColor(c);          
            g.fillRoundRect(260, 205, 250, 60, 15, 15);
            g.setColor(Color.black);
            if(explosionFrames < 150) {
            	g.drawImage(BOOM.getImage(), Math.round(player.getX() - 14), Math.round(player.getY() - 75), null);
            	explosionFrames++;
            }
            // Message that displays when game is over
            g.drawString("GAME OVER: PLAYER " + playerNumber + " WINS",300,230);
            g.drawString("Press 'Space' to play again", 310, 250);
            turn = 3; // When turn is equal to 3, neither player can go, this will prevent players from continuing to fire
             //Adds sounds after game
             if(playerNumber==1){ // Player 1 wins
                 String Ending[] = {"Winner1.wav", "1Winner2.wav", "toasty.wav", "salmonwin.wav", "herscowin.wav"};
                 int ran = (int)Math.floor((Math.random() * Ending.length));
                String EndingNumber = Ending[ran];
                 if(EndingActive == false){
                     try
                    {
                    InputStream iStream = new FileInputStream(EndingNumber);
                    AudioStream aStream = new AudioStream(iStream );
                    AudioPlayer.player.start(aStream );
                    }
                    catch(Exception e)
                    {
                    System.out.println(e);
                    }
                    EndingActive = true;
                 }
             }
             else //Player 2 Wins
             {
                 String Ending[] = {"Winner2.wav", "2Winner2.wav", "toasty.wav", "salmonwin.wav", "herscowin.wav"};
                 int ran = (int)Math.floor((Math.random() * Ending.length));
                String EndingNumber = Ending[ran];   
                 if(EndingActive == false){
                 try
                {
                InputStream iStream = new FileInputStream(EndingNumber);
                AudioStream aStream = new AudioStream(iStream );
                AudioPlayer.player.start(aStream );
                }
                catch(Exception e)
                {
                System.out.println(e);
                }
                EndingActive = true;
             }
        }
            }
    }
    
  //Creates the indentation if it hits land otherwise leaves Shot alone.
 /*   private void fireShot(Sprite shot, Graphics g)
    {   
        if(shot.getX() > 0)//inside the screen
        {   
            if(topy[Math.round(shot.getX())] < Math.round(shot.getY()))//above the top of the terrain
            {
                CreateHole(shot);
            }
        }                
    }*/
/*    
    private void CreateHole(Sprite shot)
    {
            soundShot.close(); //resets sound after being shot
            hitTest = 2;//says the object hit
            int counter3=Math.round(shot.getX())-14; //Left/Right Starting Square pos adjustment on hole
            int holderSin=0;
            int holderCos=1;
            double groundHolder=180;        
            while (groundHolder <= 359)//creates hole starting at 180 degrees and going to 360
            {
                holderSin = (int)(Math.floor((Math.sin(groundHolder/57.3))*5));//uses a length of 10 for hole
                if (counter3>3)
                {
                    topy[counter3] = topy[counter3]- holderSin;//reduces terrain
                }//depth of square //

                //creates an unequal cirlce                     
                if (holderCos==3)
                {
                    counter3++;
                    holderCos=0;    
                }

                holderCos++;
                groundHolder++; 
            }
            
    }
 */   
    //If true can change terrain
    //Switches to false when first shot is fired
    boolean terrainChange = true;
    
    //KEY COMMANDS

    
/*   	ModeIndex
Mode 0 is movement
Mode 1 is Turret angle
Mode 2 is Power
Mode 3 is Weapon Selection


	WeaponIndex
0 = Normal
1 = Nuclear
2 = Hersco
3 = Joker
*/
    int Mode1 = 0; //Mode of the arrow keys controlled by up and down
    int M1i = 0;
    int[] Modeindex1 = {0,1,2,3}; // current mode player 1
    int[] weapons1 = {0,1,2,3}; // current weapon
    int weaponindex1 = 0; // used to cycle thru weapons
    int Mode2 = 0; //Mode of the arrow keys controlled by up and down PLayer2
    int M2i = 0; // allows cycling thru modes 
    int[] Modeindex2 = {0,1,2,3};
    int[] weapons2 = {0,1,2,3,4};
    int weaponindex2 = 0;
    


    
    public void keyPressed(KeyEvent e) {
    int keyCode = e.getKeyCode();
      if (credits == 0 && titleScreenOpen == 0 && helpScreenOpen == 0) {  
        // control and n together ends game
      if ((keyCode == KeyEvent.VK_N) && (e.isControlDown())) {
            Tank1.setHealth(0);
            Tank2.setHealth(0);
            hitTest = 1;
      } 
      
      if (keyCode == KeyEvent.VK_O) {
        if (PauseMenuOpen == false)// open pause menu
        {   
            PauseMenuOpen = true;
            //CountdownTimer.counterTimer.stop();
        }
        else {
            PauseMenuOpen = false; // close pause menu
            //CountdownTimer.counterTimer.restart();
        }
      }
		
      if(keyCode == KeyEvent.VK_M) {
    	  if(!muted) {
    		  music.s();
    		  muted = true;
    	  }
    	  else {
    	        String track = gameMusic;
    	    	music = new MusicPlayer(track);
    			music.l();
  				muted = false;
    	  }

      }
      
      if (keyCode == KeyEvent.VK_U) {
            if (ControlsOverlayOpen == false) // if controls overlay isnt open
            {   
                ControlsOverlayOpen = true; // open the controls overlay
            }
            else {
                ControlsOverlayOpen = false; // close controls overlay
            }
      }
      if (keyCode == KeyEvent.VK_W) {
          if (PauseMenuOpen == true) {
               if(WindVar<3)
               {
                   WindVar++;
               }
               else
               {
                   WindVar=0;
               }
          }
          DataClass.setwind(WindVar);
      }  
   
      if (keyCode == KeyEvent.VK_L)  { // Resets game
    	  //This will reset the terrain, replenish both player's health and set turn to player 1.
    	  //Set Y sets the tanks to start on the ground
          //Therefore not falling and losing health
  	  	music.s();
	    String track = gameMusic;
		music = new MusicPlayer(track);
		music.l();
          //If a shot is fired, can't change terrain
           Tank1.getTankSprite().setY(599);
           Tank2.getTankSprite().setY(599);
           Tank1.getTankSprite().setX(200);
           Tank2.getTankSprite().setX(550);
           if (levelNumber == "1")
           {
              ChangeLevel("2");
           }
           else if (levelNumber == "2")
              ChangeLevel("3");
           else if (levelNumber == "3")
           {
              ChangeLevel("1");
           }
              //JumpUp resets the turrets look
           jumpUp = true;
           Tank1.setHealth(100);
           Tank2.setHealth(100);
           Tank1.setWeapon2(0); //Set weapon to original
           Tank2.setWeapon2(0);	//Set weapon to original
           turn = 1;
           curfuel = 110; //Sets initial fuel value to equal 110
           Mode1 = 0; // Makes it so both tanks are back to default weapon when restared
           //This will give both tanks the same power when restarted
           Tank1.setShotPower(-.3f);
           Tank2.setShotPower(-.3f);
           
           //Reset ammo for each weapon back to full after restarting game
           Tank1.setSecondWeaponAmmo(2);
           Tank2.setSecondWeaponAmmo(2);
           Tank1.setThirdWeaponAmmo(1);
           Tank2.setThirdWeaponAmmo(1);
           Tank1.setFourthWeaponAmmo(3);
           Tank2.setFourthWeaponAmmo(3);
           explosionFrames = 0;
       
           
      }
      
      if (keyCode == KeyEvent.VK_Z) {
            //Set Y sets the tanks to start on the ground
            //Therefore not falling and losing health

            //If a shot is fired, can't change terrain
          if (terrainChange == true)
          {
             Tank1.getTankSprite().setY(599);
             Tank2.getTankSprite().setY(599);
             if (levelNumber == "1")
             {
                ChangeLevel("2");
             }
             else if (levelNumber == "2")
                ChangeLevel("3");
             else if (levelNumber == "3")
             {
                ChangeLevel("1");
             }
                //JumpUp resets the turrets look
             jumpUp = true;
          }
       }

       if(!PauseMenuOpen)
       {
  
            if (GameSTATE==1)      // Fuel Turn-Based Mode
            {
                if (keyCode == KeyEvent.VK_C) 
                {
              	  tutorialmode = !(tutorialmode);
              	  
                }

                if (turn==1) // Player one's turn
                {
                	//Angle Control
                 if (keyCode == KeyEvent.VK_UP) 
                 {
                	 Mode1 = 1;
                	 // Tank1.increaseAngle();	
             		if (Tank1.getAngle() <= 170)// checks to see if turret angle has hit max (max = 172)
            		{
                  	   if(e.isControlDown())
                  		   Tank1.setAngle(Tank1.getAngle() + 4);// increases the angle by 2
                  	   else
                  		   Tank1.setAngle(Tank1.getAngle() + 1);
            			Tank1.fixTurret();
            		}
             		
                  } 
                 
                  if (keyCode == KeyEvent.VK_DOWN) 
                  {
                	  Mode1 = 1;
      	      		//  Tank1.decreaseAngle();	
               		if (Tank1.getAngle() >= 10)// checks to see if turret angle has hit max (max = 172)
              		{
               			if(e.isControlDown())
              				Tank1.setAngle(Tank1.getAngle() - 4);// decreases the angle by 2
               			else
               				Tank1.setAngle(Tank1.getAngle() - 1);
              			Tank1.fixTurret();
              		}
                  }
                  

                  //Weapon Change
                  if (keyCode == KeyEvent.VK_R) // Normal
                  { 
                	  Mode1 = 3;
                	  //removed, no new weapons made yet with the new weapon class
                    	//weaponindex1=weaponindex1+1;
                        //  Tank1.setWeapon2(weapons1[weaponindex1%4]);
                    }
                  
                    

                    //Power
                   if (keyCode == KeyEvent.VK_PERIOD) 
                   {
                	   Mode1 = 2;
                 	   if(e.isControlDown())
                 	    	Tank1.setShotPower(-.03f + Tank1.getShotPower());
                 	   else
                 	  		Tank1.setShotPower(-.01f + Tank1.getShotPower()); 
                 	   if (Tank1.getShotPower() <= -1) {
                 	  		Tank1.setShotPower(-1);
                 	   }
                  }          
                   if (keyCode == KeyEvent.VK_COMMA) 
                   {
                	   Mode1 = 2;
                	   if(e.isControlDown())
                           Tank1.setShotPower(.03f + Tank1.getShotPower());
                 	   else
                 		   Tank1.setShotPower(.01f + Tank1.getShotPower());
                       if (Tank1.getShotPower() >= -.01) {
                           Tank1.setShotPower((float)-.01);
                       }

                   }
           
                  


                   //MOVEMENT
                   if ((keyCode == KeyEvent.VK_LEFT) && (curfuel > 0)) //This will move the tank if the left key is pressed and fuel isn't empty.
                   {  
                	   Mode1 = 0;
                        //if(Tank1.getMovesLeft()>0) {
                            if (Tank1.getTankSprite().getX() >= 3){
                                System.out.println("Player 1's tank fuel is at: " + curfuel);
                                moving=true;
                                Tank1.setMovesLeft(Tank1.getMovesLeft()-3);
                                Tank1.getTankSprite().setX(Tank1.getTankSprite().getX()-3);
                                jumpUp = false;
                                curfuel -= fueldepletion; //Takes fuel away at a constant rate while the tank is moving.
                            }
                        //}
                   }             
                   if ((keyCode == KeyEvent.VK_LEFT) && (curfuel <= 0)) // If the left key is pressed and fuel is empty: Don't move
                   {  
                	   Mode1 = 0;
                      moving = false;
                   }
                  
                   if ((keyCode == KeyEvent.VK_RIGHT) && (curfuel > 0)) 
                   {
                	   Mode1 = 0;
                        //if(Tank1.getMovesLeft()>0) {
                            if (Tank1.getTankSprite().getX()<345)
                            {
                                    System.out.println("Player 1'S tank fuel is at: " + curfuel);
                                    moving=true;
                                    Tank1.setMovesLeft(Tank1.getMovesLeft()-3);
                                    Tank1.getTankSprite().setX(Tank1.getTankSprite().getX()+3);
                                    jumpUp = false;
                                    curfuel -= fueldepletion; //Takes fuel away at a constant rate while the tank is moving.
                            }
                        //}
                            }
                   if ((keyCode == KeyEvent.VK_RIGHT) && (curfuel <= 0)){
                      moving = false;
                  }
                  
                  
                  if (keyCode == KeyEvent.VK_SPACE) //When F is pressed, the turn will switch to the other player
                  {
                     // carfuel = 110; //This will make sure the other player has full fuel at the beginning of his/her turn.
                      System.out.println(curfuel);
                      shotcounter2++;
                      terrainChange = false;
                      //Makes sure tank is no longer paralyzed
                      if(shotcounter2 > PARALYZED){
                    	  if (TankShot.getState() == 0){
                    	  T1Weapon.fire();
/*                        if (TankShot.getState() == 0 && SecondaryTankShot.getState() == 0 && ThirdTankShot.getState()==0 && FourthTankShot.getState()==0)
                        {

                            if(Tank1.getWeapon2()== 0){
                                TankShot.setState(1);
                                //soundShot.load("gunshot.mid");
                                soundShot.play();
                                }
                            else if(Tank1.getWeapon2()== 1)
                            {
                                //soundShot.load("gunshot.mid");
                                soundShot.play();
                                SecondaryTankShot.setState(1);
                            }
                            else if(Tank1.getWeapon2()==2)
                            {
                                ThirdTankShot.setState(1);
                            }
                            else if(Tank1.getWeapon2()==3)
                            {
                                FourthTankShot.setState(1);
                            }
 */                           tankshoot = 1;
                            
                        }
                        
                        
                  }

                      //resets both players' mode to move mode
                      M1i = 0; 
                      Mode1 = Modeindex1[M1i % 4]; 
                      M2i = 0; 
                      Mode2 = Modeindex2[M2i % 4]; 
                    //CountdownTimer.counterTimer.restart(); //restarts the timer
                    //CountdownTimer.counter=HelpScreen.time; //sets the timer to 30
                  }
                
                }
                else if (turn==2) //Players two's turn
                {
                 
              //Change Weapons
                    if (keyCode == KeyEvent.VK_SLASH) // Normal
                      { 
                    	Mode2 = 3;
                    	//removed, no new weapons made yet with the new weapon class
                    	// weaponindex2=weaponindex2+3;
                      // Tank2.setWeapon2(weapons2[weaponindex2%4]);
                      }
                 // if (keyCode == KeyEvent.VK_PERIOD) { // Nuclear Weapon
                	//  weaponindex2=weaponindex2+1;
                      //Tank2.setWeapon2(weapons2[weaponindex2%4]);
                 // }
                
                  
                //Power
                  if (keyCode == KeyEvent.VK_PERIOD) 
                  {
                	  Mode2 = 2;
                	  if(e.isControlDown())
                          Tank2.setShotPower(-.03f + Tank2.getShotPower());
                	  else
                		  Tank2.setShotPower(-.01f + Tank2.getShotPower());
              
                      if (Tank2.getShotPower() <= -1) {
                          Tank2.setShotPower(-1);
                      }
                  }
                  
                  if (keyCode == KeyEvent.VK_COMMA) 
                  {
                	  Mode2 = 2;
                	  if(e.isControlDown())
                          Tank2.setShotPower(.03f + Tank2.getShotPower());
                	  else
                		  Tank2.setShotPower(.01f + Tank2.getShotPower());
                      if (Tank2.getShotPower() >= -.01) {
                          Tank2.setShotPower((float)-.01);
                      }

                  }
                
                    
                  //Angle
                  if (keyCode == KeyEvent.VK_DOWN) 
                    {
                	    Mode2 = 1;
                  //      Tank2.increaseAngle();   
                 		if (Tank2.getAngle() <= 170)// checks to see if turret angle has hit max (max = 172)
                		{
                      	   if(e.isControlDown())
                      		   Tank2.setAngle(Tank2.getAngle() + 4);// increases the angle by 2
                      	   else
                      		   Tank2.setAngle(Tank2.getAngle() + 1);
                			Tank2.fixTurret();
                		}
                    }
                  
                  if (keyCode == KeyEvent.VK_UP) 
                  {
                	    Mode2 = 1;
               //         Tank2.decreaseAngle();     
                   		if (Tank2.getAngle() >= 10)// checks to see if turret angle has hit max (max = 172)
                  		{
                   			if(e.isControlDown())
                  				Tank2.setAngle(Tank2.getAngle() - 4);// decreases the angle by 2
                   			else
                   				Tank2.setAngle(Tank2.getAngle() - 1);
                  			Tank2.fixTurret();
                  		}
                  }
                
                  

                  //Movement
                  if ((keyCode == KeyEvent.VK_LEFT) && (curfuel > 0)) //Makes it so tank can move when fuel isn't empty.
                  {   
                	  
                	  Mode2 = 0;
                        //if(Tank2.getMovesLeft()>0) {
                            if (Tank2.getTankSprite().getX() >395)
                            {
                                moving=true;
                                Tank2.setMovesLeft(Tank2.getMovesLeft()-3);
                                Tank2.getTankSprite().setX(Tank2.getTankSprite().getX()-3);
                                jumpUp = false;
                                curfuel -= fueldepletion; //Takes fuel away at a constant rate while the tank is moving.
                                System.out.println("Player 2's tank fuel is at: " + curfuel);
                            }
                        //}
                  }                 
                  if ((keyCode == KeyEvent.VK_LEFT) && (curfuel <= 0)) 
                  {
                	  Mode2 = 0;
                        moving = false;
                  }
                  
                  if ((keyCode == KeyEvent.VK_RIGHT) && (curfuel > 0)) 
                  {
                	  Mode2 = 0;
                        //if(Tank2.getMovesLeft()>0) {
                            if (Tank2.getTankSprite().getX()<= 796 ) {
                                    moving=true;
                                    Tank2.setMovesLeft(Tank2.getMovesLeft()-3);
                                    Tank2.getTankSprite().setX(Tank2.getTankSprite().getX()+3);
                                    jumpUp = false;
                                    curfuel -= fueldepletion; //Takes fuel away at a constant rate while the tank is moving.
                                    System.out.println("Player 2's tank fuel is at: " + curfuel);
                            }
                        //}
                  }
                  if ((keyCode == KeyEvent.VK_RIGHT) && (curfuel <= 0))
                  {
                	  Mode2 = 0;
                      moving = false;
                  }
                

                  //Shooting
                  if (keyCode == KeyEvent.VK_SPACE)
                  {
                      //curfuel = 110; //Makes sure that the other play will have full fuel at the beginning of his/her turn
                      System.out.println(curfuel);
                      shotcounter++;
                      terrainChange = false;
                      System.out.println(shotcounter);
                      
                      //Makes sure tank is no longer paralyzed
                      if(shotcounter > PARALYZED){
                          
                        if (TankShot.getState() == 0 && SecondaryTankShot2.getState() == 0 && ThirdTankShot2.getState()==0 && FourthTankShot2.getState()==0)
                        {
                        	T2Weapon.fire();

/*                            if(Tank2.getWeapon2()== 0)
                            {
                                TankShot2.setState(1);
                                //soundShot.load("gunshot.mid");
                                soundShot.play();
                            }
                            else if(Tank2.getWeapon2()== 1)
                            {
                                //soundShot.load("gunshot.mid");
                                soundShot.play();
                                SecondaryTankShot2.setState(1);
                            }
                            else if(Tank2.getWeapon2()== 2)
                            {
                                ThirdTankShot2.setState(1);
                            }
                            else if(Tank2.getWeapon2()== 3)
                            {
                                FourthTankShot2.setState(1);
                            }
  */                          tankshoot = 11;
                            
                        }
                      
                     } 

                    //resets both players' mode to move mode
                      M1i = 0; 
                      Mode1 = Modeindex1[M1i % 4]; 
                      M2i = 0; 
                      Mode2 = Modeindex2[M2i % 4]; 
                    //CountdownTimer.counterTimer.restart(); //restarts the timer
                    //CountdownTimer.counter=HelpScreen.time; //sets the timer to 30
                    
                  }
                }
            }
            
                  
               if (GameSTATE == 3) 
            {
                restartgame = 1;
            }
            

                        
                
                     
            
        // exit the program
        if (keyCode == KeyEvent.VK_ESCAPE) {
             music.s();
    		music = new MusicPlayer(creditsMusic);
    		music.l();
             credits=1;
 			if (Credits.creditsOn)
 				Credits.creditsOn = false;
			else
				Credits.creditsOn = true;
        }
        else {
            System.out.println("You Pressed: " + KeyEvent.getKeyText(keyCode) ); //print out which key the user presses.
            e.consume(); //erases the key event.
        }
       }
      }
      else if (credits==1 ){
          if (keyCode == KeyEvent.VK_ESCAPE) {
              System.exit(-1);
         }
      }
      if (titleScreenOpen == 1) {
    	  if (keyCode == KeyEvent.VK_SPACE) {
    		  
    		  // This needs to be here in order for the tank color changes to take effect
			  DataClass.setP1Tank(HelpScreen.p1Color);
			  DataClass.setP2Tank(HelpScreen.p2Color);
			  DataClass.setwind(HelpScreen.wind);
		      TankImage = loadImage(DataClass.getP1Tank());
		      TankImage2 = loadImage(DataClass.getP2Tank());
		      TankStill.addFrame(TankImage,300);
		      TankStill2.addFrame(TankImage2,300);

    		  titleScreenOpen = 0;
    	  }
    	  
    	  if (keyCode == KeyEvent.VK_ESCAPE) 
          {
	    	  System.exit(-1);
		  }
    	  
    	  if (keyCode == KeyEvent.VK_H)
 	      {
    		  helpScreenOpen = 1;
    		  titleScreenOpen = 0;
 	      }
      }
      if(helpScreenOpen == 1)
      {
		   if (keyCode == KeyEvent.VK_ESCAPE) 
		   {
			   titleScreenOpen = 1;
			   helpScreenOpen = 0;
		//	   DataClass.setGameState(GameState);
		//	   setState = 1;
		   }
		   else if (keyCode == KeyEvent.VK_W)
		   {
			   if(HelpScreen.wind < 3)
			   {
				   HelpScreen.wind++;
			   }
			   else
			   {
				   HelpScreen.wind=0;
			   }
		   }
		/*   else if (keyCode == KeyEvent.VK_G)
		   {
			   if(GameState==0)
			   {
				   GameState=1;
			   }
			   else
			   {
				   GameState=0;
			   }
		   } */
		   else if (keyCode == KeyEvent.VK_LEFT)
		   {
			   if(HelpScreen.p2Color > 0)
				   HelpScreen.p2Color--;
		   }
		   else if (keyCode == KeyEvent.VK_RIGHT)
		   {
			   if(HelpScreen.p2Color < HelpScreen.tankColors.length - 1)
				   HelpScreen.p2Color++;
		   }
		   else if (keyCode == KeyEvent.VK_UP)
		   {
			   if(HelpScreen.p1Color < HelpScreen.tankColors.length - 1)
				   HelpScreen.p1Color++;
		   }
		   else if (keyCode == KeyEvent.VK_DOWN)
		   {
			   if(HelpScreen.p1Color > 0)
				   HelpScreen. p1Color--;
		   }
      }
      

  }
            
        

 
  public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        
        
        if (keyCode == KeyEvent.VK_LEFT)
        {
            moving = false;
            //jump up resets turret after moving need to add a method that
            //rests the turret constantly while moving jump up doesn't work
            jumpUp = true;
        }
        if (keyCode == KeyEvent.VK_RIGHT) 
        {
            moving = false;
            //jump up resets turret after moving need to add a method that
            //rests the turret constantly while moving jump up doesn't work
            jumpUp = true;
        }
        
                     e.consume(); //erases the key event.
    }

    public void keyTyped(KeyEvent e) {
// takes the input and eats it

        e.consume();
    }
    ///////////////////////////////////////
    ///Moved to KeyPressed for enter key///
    ////////Ryan Crean 4/3/08//////////////
    ///////////////////////////////////////
    
       
    //This method draws the in-game display and menus.
    public void DrawMessages(Graphics g)
    {   

    	
        g.drawImage(headerBar, 0, 0, null); // header background
        
        // Player 1 Health Bar
        int healthBar1X = 35;
        int healthBar1Y = 29;
        
        int healthBar1Value = (int) (110 - (Tank1.getHealth()*1.1)); // Used to determine how much of the health bar is covered
        int healthBar1Display = (int) (Tank1.getHealth()); // Used to display the value of a player 1's health
        
        g.drawImage(healthIcon, 5, 28, null); // health icon
        g.drawImage(healthBar, healthBar1X, healthBar1Y, null); // health bar
        g.setFont(new Font("SansSerif", Font.BOLD, 20)); // Sets the font and size of displayed health number
        g.setColor(Color.darkGray); // Makes the rectangle that covers health dark gray
        
        // Draws rectangle that covers Player 1 Health Bar when P1 loses health
        g.drawRect(healthBar.getWidth(null)+(healthBar1X-1)-healthBar1Value,
                healthBar1Y+1, healthBar1Value - 1, 20);
        g.fillRect(healthBar.getWidth(null)+(healthBar1X-1)-healthBar1Value,
                healthBar1Y+1, healthBar1Value - 1, 20);
        g.setColor(Color.white); // Makes displayed health number white
        g.drawString(Integer.toString(healthBar1Display), (healthBar1X + 5), (healthBar1Y + 20)); // Displays the number value of player 1's health
        
        
        //Fuel display in the center of the screen
        int barX = 355; //Location of fuel bar on x axis
        int barY = 29;  //Location on y axis
        
        int barXOffset = 30;
        int barYOffset = 40;
        
        if (turn == 1)
        {
        	if (Tank1.getTankSprite().getY() > 520) barYOffset = -barYOffset;
        	barX = (int)Tank1.getTankSprite().getX()-barXOffset;
        	barY = (int)Tank1.getTankSprite().getY()+barYOffset;
        }
        else if (turn == 2)
        {
        	if (Tank2.getTankSprite().getY() > 520) barYOffset = -barYOffset;
        	barX = (int)Tank2.getTankSprite().getX()-barXOffset;
        	barY = (int)Tank2.getTankSprite().getY()+barYOffset;
        }
        if (barX < 35) barX = 35;
        else if (barX > 675) barX = 675;
        
        int fuelBarValue = (int) (110 - curfuel);
        if ((Mode1 == 0) && (Mode2 == 0))
        {
	        g.drawImage(fuelIcon, barX-25, barY-7, null); // fuel icon
	        g.drawImage(fuelBar, barX, barY, null); // Place fuel bar at specified part of the screen
	        g.setColor(new Color( (fuel-curfuel)/(float)fuel * 0.5f,0,0) ); // Makes color behind fuel bar black
	        // Draws a rectangle to cover what has been used of the fuel so far.
	        g.drawRect(fuelBar.getWidth(null)+(barX-1)-fuelBarValue,
	        		barY+1, fuelBarValue - 1, 20);
	        g.fillRect(fuelBar.getWidth(null)+(barX-1)-fuelBarValue,
	        		barY+1, fuelBarValue - 1, 20);
	        
	        if (curfuel <= 0) 
	        {
	            g.setColor(Color.WHITE);
	            g.setFont(new Font("SansSerif", Font.BOLD, 15));
	        	g.drawString("Out of fuel!", barX+5, barY+17);
	        }
        }
        
        
        int powerBarValue = 0;
        if (turn == 1) powerBarValue = (int) (Tank1.getShotPower() * -111);
        else if (turn == 2) powerBarValue = (int) (Tank2.getShotPower() * -111);
        
        if ((Mode1 == 2) || (Mode2 == 2))
        {
	        g.drawImage(powerIcon, barX-28, barY-3, null); //power icon
	        g.drawImage(powerBar, barX, barY, null); // power bar
	        
	        // Draws rectangle that covers Player 1 Power Bar
	        g.setColor(Color.black);
	        g.drawRect(barX + powerBarValue, barY+1, 
	                barX - (barX-110) - powerBarValue, 20);
	        g.fillRect(barX + powerBarValue, barY+1, 
	                barX - (barX-110) - powerBarValue, 20);
        }
        
        // Player 2 Health Bar
        int healthBar2X = 682;
        int healthBar2Y = 29;
        
        int healthBar2Value = (int) (110 - (Tank2.getHealth()*1.1)); //Used to determine how much of player 2's health bar is covered
        int healthBar2Display = (int) (Tank2.getHealth());  // Used to display the number value of player 2's health
        
        g.drawImage(healthIcon, 652, 28, null); // health icon
        g.drawImage(healthBar, healthBar2X, healthBar2Y, null); // health bar
        g.setFont(new Font("SansSerif", Font.BOLD, 20)); // Sets the font and size of the displayed number
        g.setColor(Color.darkGray); // Makes the rectangle that covers health dark gray
        // Draws rectangle that covers Player 2 Health Bar when P2 loses health
        g.drawRect(healthBar.getWidth(null)+(healthBar2X-1)-healthBar2Value,
                healthBar2Y+1, healthBar2Value - 1, 20);
        g.fillRect(healthBar.getWidth(null)+(healthBar2X-1)-healthBar2Value,
                healthBar2Y+1, healthBar2Value - 1, 20);
        g.setColor(Color.white); // Makes displayed health number white
        g.drawString(Integer.toString(healthBar2Display), (healthBar2X + 5), (healthBar2Y + 20)); // Displays the number value of player 2's health
        g.setColor(Color.darkGray); // For player 2's power bar
        
        int textAlignLeft = 230;
        g.setFont(new Font("SansSerif", Font.BOLD, 13));
        g.setColor(Color.WHITE);
        
        //Player One Display
        if(GameSTATE==0||GameSTATE==1){
        g.drawString("Player 1", 5, 15);
         
         
         // Player Two Display
         g.drawString("Player 2", 743, 15);
         
        }
         if(Windf>0f)
            g.drawString("Wind: " + (int)(Windf*10000f) + " mph W" ,353, 15);
         else
            g.drawString("Wind: " + (int)(Windf*-10000f) + " mph E" ,353, 15);
         
        
        if (GameSTATE==1&&turn==1) // If Player 1's Turn
         {
             g.setColor(Color.BLACK);
             //g.drawString("<= PLAYER1", 350, 75);
                     }
         else if (GameSTATE==1&&turn==2) // If Player 2's Turn
         {
             g.setColor(Color.BLACK);
             //g.drawString("PLAYER2 =>", 350, 75);
            
         }
         
         if (PauseMenuOpen == true) {
            Color c = new Color(1.0f, 1.0f, 1.0f, 0.6f);
            g.setColor(c);
            g.fillRoundRect(200, 100, 400, 360, 15, 17);
            g.setColor(Color.darkGray);
            Font f = g.getFont();
            g.setFont(new Font("SansSerif", Font.BOLD, 17));
            g.drawString("OPTIONS", textAlignLeft, 120);
            g.setFont(f);
            g.setColor(Color.darkGray);
            g.setFont(new Font("SansSerif", Font.BOLD, 14));
            g.drawString("Controls:", textAlignLeft, 140);
            g.setFont(f);
            g.setColor(Color.black);
            g.drawString("t: enter tutorial mode to view the controls.", textAlignLeft, 165);
            g.drawString("Spacebar: restart the round", textAlignLeft, 190);
            g.drawString("z: before a game starts, this will change the terrain", textAlignLeft, 215);
            g.drawString("Control: hold to accelerate adjusting power/angle",textAlignLeft, 240);
            g.drawString("m: mute/unmute music", textAlignLeft, 265);
    //       g.drawString("Spacebar to fire & end turn.", textAlignLeft, 290);
            g.setColor(Color.darkGray);
            g.setFont(new Font("SansSerif", Font.BOLD, 14));
            g.setFont(f);
            g.setColor(Color.darkGray);
            g.setFont(new Font("SansSerif", Font.BOLD, 12));
            g.drawString("Press 'W' to cycle wind options:", textAlignLeft, 335);
            g.setFont(f);
            g.setColor(Color.black);

            if (WindVar == 2){
                g.drawString("Wind is on and constant.", textAlignLeft, 350);
            }else if (WindVar == 1){
                g.drawString("Wind is randomized after every shot.", textAlignLeft, 350);
            }else if (WindVar == 3){
                g.drawString("Wind is completely randomized.", textAlignLeft, 350);
            }else{
                g.drawString("Wind is Off.", textAlignLeft, 350);
            }
            g.setColor(Color.darkGray);
            g.setFont(new Font("SansSerif", Font.BOLD, 12));
          //  g.drawString("Press 'M' to turn off Music:", textAlignLeft, 367);
            g.setFont(f);
            g.setColor(Color.black);
//            g.drawString(playlistnames[HelpScreen.tracknumber], textAlignLeft, 382);

            // for testing because no values changed
            check = true;
            
            g.setColor(Color.DARK_GRAY);
            g.setFont(new Font("SansSerif", Font.BOLD, 14));
        //    g.drawString("Press Z to change terrain.", textAlignLeft, 410);
        //`   g.drawString("Press R to restart the game.", textAlignLeft, 425);
            g.drawString("Press Esc to end game.", textAlignLeft, 440);
            g.drawString("Press O to close option screen.", textAlignLeft, 455);
         }
         if (ControlsOverlayOpen == true) {
         
         }
    }
    
    // Update/Draw the Selected Weapon Indicator
    public void updateWeaponandModeIndicator(Graphics g) {
        int currentWeapon = 0;
        int currentAmmo = 0;
        int tankX;
        int tankY;
        Tank currentTank;
        String strAmmo;
        int mi = 0;
        int mi2 = 0;
        Image[] modeindicator = {modemove,modeangle,modepower,modeweapon};
        
        g.drawImage(weaponIndicatorBG, -4, 60, null); // P1 Background
        g.drawImage(weaponIndicatorBG, 743, 60, null); // P2 Background
        g.drawImage(modeBG, 638, 53, null); // P1 ModeBackground
        g.drawImage(modeBG, 66, 53, null); // P2 ModeBackground
        
        for(int i=0; i<2; i++) { // for each of the two tanks
            
            if(i==0) {
                currentTank = Tank1;
                // Position Weapon Images for Player 1
                tankX = 5;
                tankY = 65;
            } else {
                //Position Weapon Images for Player 2
                currentTank = Tank2;
                tankX = 751;
                tankY = 65;
            }
            
            currentWeapon = currentTank.getWeapon2();
            
            if(currentWeapon == 0) { // Normal
                g.drawImage(weaponNormal, tankX, tankY, null);
                currentAmmo = 1;
            } else if (currentWeapon == 1) { // Nuclear
                g.drawImage(weaponNuclear, tankX, tankY, null);
                currentAmmo = currentTank.getSecondWeaponAmmo();
            } else if(currentWeapon == 2) { // Hersco
                g.drawImage(weaponHersco, tankX, tankY, null);
                currentAmmo = currentTank.getThirdWeaponAmmo();
            } else { // Joker
                g.drawImage(weaponJoker, tankX, tankY, null);
                currentAmmo = currentTank.getFourthWeaponAmmo();
            }
            
            if(currentWeapon==0) {
                strAmmo = "x";
            } else {
                strAmmo = Integer.toString(currentAmmo); // Convert int to String
            }
            
            if(currentTank==Tank1) { // For P1 side indicator
                g.drawString(strAmmo, 40, 100);
            } else { // it's P2's Indicator
                g.drawString(strAmmo, 790, 100);
            }
            
            if(Mode1==0){
            	mi = 0;
            }
            else if(Mode1==1){
            	mi = 1;
            }
            else if(Mode1==2){
            	mi = 2;
            }
            else if(Mode1==3){
            	mi = 3;
            }
            
            if(Mode2==0){
            	mi2 = 0;
            }
            else if(Mode2==1){
            	mi2 = 1;
            }
            else if(Mode2==2){
            	mi2 = 2;
            }
            else if(Mode2==3){
            	mi2 = 3;
            }
            
            if(currentTank==Tank1) { // For P1 side indicator
                g.drawImage(modeindicator[mi], 77, 83, null);
            } 
            else{ // it's P2's Indicator
                    g.drawImage(modeindicator[mi2], 650, 83, null);
                }
           
        }
    }
    
    
    
    
    
    // Update/Draw the Selected Weapon Indicator

 
//Creates and sets the Shot that is fired
    public void TankFire(Graphics g, int player)
        {
        float p1xveloc;
        float p1yveloc;
        float p2xveloc;
        float p2yveloc;
        if (player==1)
            {
            //TEST
            //g.drawImage(TankShot.getImage(), xcoord, ycoord, null);
            
            //changing Player1AngleX and Player1AngleY does nothing
            
            g.drawImage(TankShot.getImage(), Tank1.getTurretAngleX(), Tank1.getTurretAngleY(), null);
            TankShot.setState(1);
            TankShot.setX(Tank1.getTurretAngleX());
            TankShot.setY(Tank1.getTurretAngleY());
            p1xveloc=(float)((-1)*Tank1.getShotPower()*Math.cos(Tank1.getAngle()/57.3));
            p1yveloc=(float)(Tank1.getShotPower()*Math.sin(Tank1.getAngle()/57.3));

            TankShot.setVelocityX(p1xveloc); 
            TankShot.setVelocityY(p1yveloc); 
            
            //soundShot.load("gunshot.mid");
            soundShot.play();
            
            }
            else
                {
                //g.drawImage(TankShot.getImage(), 400, 400, null);
                g.drawImage(TankShot2.getImage(), Tank2.getTurretAngleX(), Tank2.getTurretAngleY(), null);
                TankShot2.setState(1);
                TankShot2.setX(Tank2.getTurretAngleX());
                TankShot2.setY(Tank2.getTurretAngleY());
                p2xveloc=(float)((-1)*Tank2.getShotPower()*Math.cos(Tank2.getAngle()/57.3));
                p2yveloc=(float)(Tank2.getShotPower()*Math.sin(Tank2.getAngle()/57.3));

                TankShot2.setVelocityX(p2xveloc); //.7 //ANGLE Adjustment
                TankShot2.setVelocityY(p2yveloc); //.3 //POWER
                
                //soundShot.load("gunshot.mid");
                soundShot.play();
                
                }
        }
    
    
    
  //Creates and sets the Shot that is fired
    public void TankFire2(Graphics g,int player)
        {
        float p1xveloc;
        float p1yveloc;
        float p2xveloc;
        float p2yveloc;
        
        //soundShot.load("gunshot.mid");
        soundShot.play();
        
        
        if (player==1)
                {
                g.drawImage(SecondaryTankShot.getImage(), Tank1.getTurretAngleX(), Tank1.getTurretAngleY(), null);
                SecondaryTankShot.setState(1);
                SecondaryTankShot.setX(Tank1.getTurretAngleX());
                SecondaryTankShot.setY(Tank1.getTurretAngleY());
                p1xveloc=(float)((-1)*Tank1.getShotPower()*Math.cos(Tank1.getAngle()/57.3));
                p1yveloc=(float)(Tank1.getShotPower()*Math.sin(Tank1.getAngle()/57.3));
                
                
                SecondaryTankShot.setVelocityX(p1xveloc); //.7 //Player1AngleX Adjustment
                SecondaryTankShot.setVelocityY(p1yveloc); //.3 //POWER
                }
        else
                {
                g.drawImage(SecondaryTankShot2.getImage(), Tank2.getTurretAngleX(), Tank2.getTurretAngleY(), null);
                SecondaryTankShot2.setState(1);
                SecondaryTankShot2.setX(Tank2.getTurretAngleX());
                SecondaryTankShot2.setY(Tank2.getTurretAngleY());
                p2xveloc=(float)((-1)*Tank2.getShotPower()*Math.cos(Tank2.getAngle()/57.3));
                p2yveloc=(float)(Tank2.getShotPower()*Math.sin(Tank2.getAngle()/57.3));
                
                SecondaryTankShot2.setVelocityX(p2xveloc); //.7 //ANGLE Adjustment
                SecondaryTankShot2.setVelocityY(p2yveloc); //.3 //POWER
                }
        }


    public void TankFire3(Graphics g,int player)
    {
    
        try
        {
        InputStream iStream = new FileInputStream("hersco.wav");
        AudioStream aStream = new AudioStream(iStream );
        AudioPlayer.player.start(aStream );
        }
        catch(Exception e)
        {
        System.out.println(e);
        }
    if (player==1)
            {
            g.drawImage(ThirdTankShot.getImage(), (int)(Tank2.getTankSprite().getX()), -200, null);
            ThirdTankShot.setState(1);
            ThirdTankShot.setX(Tank2.getTankSprite().getX());
            
            ThirdTankShot.setY(0);
            ThirdTankShot.setVelocityY(.2f);
            }
    else
            {
            g.drawImage(ThirdTankShot2.getImage(),(int)(Tank1.getTankSprite().getX()), -200, null);
            ThirdTankShot2.setState(1);
            ThirdTankShot2.setX(Tank1.getTankSprite().getX());
            ThirdTankShot2.setY(0);
            ThirdTankShot2.setVelocityY(.2f);
            }
    }
    
    public void TankFire4(Graphics g,int player)
    {
    
        try
        {
        InputStream iStream = new FileInputStream("a.wav");//for sound
        AudioStream aStream = new AudioStream(iStream );
        AudioPlayer.player.start(aStream );
        }
        catch(Exception e)
        {
        System.out.println(e);
        }
    if (player==1)
            {
            g.drawImage(FourthTankShot.getImage(), (int)(Tank2.getTankSprite().getX()), -100, null);
            FourthTankShot.setState(1);
            FourthTankShot.setX(Tank2.getTankSprite().getX());
            
            FourthTankShot.setY(0);
            FourthTankShot.setVelocityY(.25f);
            }
    else
            {
            g.drawImage(FourthTankShot2.getImage(),(int)(Tank1.getTankSprite().getX()), -100, null);
            FourthTankShot2.setState(1);
            FourthTankShot2.setX(Tank1.getTankSprite().getX());
            
            FourthTankShot2.setY(0);
            FourthTankShot2.setVelocityY(.25f);
            }
    }
    
    
        
    // testing note: to make it testable the caseChecks have been added
    public void DrawTerrain(Graphics g)
        {   
        double terrainHeight = Math.random()+3.42;
        double terrainHeight2 = Math.random()+5;
        int finalTerrain = Math.round((float)((terrainHeight2+terrainHeight)/2)*100);
        terrainNum = finalTerrain;
        int freq = Math.round((float)Math.random()*100);
        
        int t=15;
        int tester=342;
        boolean up = true;
        double ang;
        while (basex < 900)
            {
                if (t > 15)
                {
                    if(t < 90 )
                    {
                ang = (t/57.3);
                tester = Math.abs((int)(Math.floor((Math.sin(ang))*freq)));
                
                //***********************************************************
                //Added by Ryan K
                freqValue = tester;
                //***********************************************************
                
                caseChecks[0] = true;
                //tester = Math.abs((int)(Math.floor((Math.sin(ang)*200))));
                //changing 50 to higher # makes higher terrain and lower makes flatter
                }
            }
            if (t > 90)
                {
                up=false;
                caseChecks[1] = true;
                }
            if(t<15)
            {up=true;}
            if (topy[basex] == null)
                {
                    //***********************************************************
                    //Added by Ryan K to call level function
                    //***********************************************************
                    level();
                    //***********************************************************   
                } 
             if(levelNumber == "1" || levelNumber == "2" || levelNumber == "5")
            {
                Color terrainColor = new Color(0x5e3b1f);//Terrain Color Adjustment
                
                g.setColor(terrainColor);
                g.drawImage(terrainTexture, 0, 400, null);

            }
            
            //sunny
            
            if(levelNumber == "3" || levelNumber == "4")
            {
                Color terrainColor = new Color(0x5e3b1f);//Terrain Color Adjustment
                //Image terrainTexture = loadImage("images/terrainGreen.jpg");
                //g.drawImage(terrainTexture, 0, 400, null);
                g.setColor(terrainColor);

            }
            g.drawLine(basex,basey,topx,topy[basex]);
            basex++;
            topx++;
            if (up==true)
                {
                t++;
                caseChecks[2] = true;
                }
            if(up==false)
                {
                t--;
                caseChecks[3] = true;
                }           
            }
        while (basex < 2000)
            {
            topy[basex]=342;//342
            basex++;
            caseChecks[4] = true;
            }
        basex=0;
        basey=900;
        topx=0;
        // sets turret color
        g.setColor(Color.gray);
        caseChecks[6] = true;   
        return;
      }
     
      // new DrawTerrain() is below
      // I've added the testing lines to it, but as it generates a different terrain I think it should be turned into a new 
      // function.  
      
        /*public void DrawTerrain2(Graphics g)
        {   
        String enter = "DrawTerrain has been called!!";
        double terrainHeight = Math.random()+3.42;
        double terrainHeight2 = Math.random()+5;
        double abc = Math.random()*100;

        
        int freq = Math.round((float)Math.random()*100);
        int q = 200;
        int t=15;
        int v=0;
        int tester=342;
        boolean up = true;
        double ang;
        while (basex < 900)
            {
                if (t > 15)
                {
                    if(t < 90 )
                    {
                
                ang = (t/57.3);//abc
                tester = Math.abs((int)(Math.floor((Math.sin(ang))*freq)));
                //tester = Math.abs((int)(Math.floor((Math.sin(ang)*200))));
                //changing 50 to higher # makes higher terrain and lower makes flatter
                caseChecks[0] = true;
                }
            }
            if (t > 90)
                {
                up=false;
                caseChecks[1] = true;
                }
            if(t<15)
            {up=true;}
            if (topy[basex] == null)
                {
                    //topy[basex]= finalTerrain-tester;//normal random terrain
                    topy[basex]= q++/2;//decline
                    //topy[basex]= r--/2;//incline
                    //topy[basex]= 400;//constant
                    caseChecks[5] = true;
                } //342 - create a random terrain function from here.
            g.setColor(Color.red);
            g.drawLine(basex,basey,topx,topy[basex]);
            basex++;
            topx++;
   
            v++;
            if (up==true)
                {
                    caseChecks[2] = true;
                    t++;
                    }
            if(up==false)
                {   
                    caseChecks[3] = true;
                    t--;
                }           
            }
        while (basex < 2000)
            {
            topy[basex]=342;//342
            basex++;
            caseChecks[4] = true;
            }
        basex=0;
        basey=900;
        topx=0;
        g.setColor(Color.green);
        
        caseChecks[6] = true;
        return; 
      }*/

   //*******************************************************
    
    /*****************************************************
        *The following blocks of code correspond to the different levels
        *Ryan K
        *****************************************************
        */
    public void level() {
        if(levelNumber == "1")//level 1
            {
                topy[basex]= terrainNum-freqValue;//normal random terrain
            }
        if(levelNumber == "2")//level 3
            {
            int oo = 1080;
            for(int ko = 000; ko <= 400; ko++)
                {
                topy[ko] = oo--/2;
                }
                    
            for(int ko = 401; ko <= 801; ko++)
                {
                topy[ko] = oo++/2;
                }
            topy[basex]= terrainNum-freqValue;
            }                    
        if(levelNumber == "3")//level 5
            {
            topy[basex]= 400;
            }
        
            caseChecks[5] = true;
    }
   
    
    public void CreateTank(Graphics g)
        {
                
        String call = "The Program Create Tank has been called!";
        float rand = 0;
        float posY = 0;
        float p1rand = 0;
        float p2rand = 0;
        float finalrand = 0;
        float p1y = 0;
        float p2y = 0;
        
        rand = Math.round((float)(Math.random()*200));
        p1rand = rand;
        Tank1.getTankSprite().setX(rand);
        
        posY=(float)(topy[(int)rand]-13);
        Tank1.getTankSprite().setY(550);
        p1y = posY;
        
        rand = Math.round((float)(Math.random()*300));
        p2rand = rand;
        rand = 700-rand;
        
        finalrand = p2rand-p1rand;
 
            Tank2.getTankSprite().setX(rand);
        posY=(float)(topy[(int)rand]-13);
        Tank2.getTankSprite().setY(550);
        p2y = posY;
        TankCreated = true;
        
        try {
            if (testing) {
                FileWriter file = new FileWriter("output.doc", true);
                BufferedWriter out = new BufferedWriter(file);
                out.write("\n" + call + "\n");
                out.write("Player 1 Tank X has been set to " + p1rand + "\n");
                out.write("Player 1 Tank Y is set to " + p1y + "\n");
                out.write("Player 2 Tank X has been set to " + p2rand + "\n");
                out.write("Player 2 Tank Y is set to " + p2y + "\n");
                out.write("Distance between tanks is: " + finalrand + "\n");
                out.close();
            }
        }
        catch(Exception e){
             System.err.println("Error: " + e.getMessage());
        }
        return;
        }
    
    /*
     * GroundCollision()
     * By Eric Marcarelli, Alex Geden, Ryan Kleckner
     * Starts and stops the tank from falling, adjusts health, sets rotation 
     * angle for more realistic landings.
     */
  
    public void GroundCollision(Sprite tank, int player)
    {           
        double angle = 1;  
        boolean falling = false; 
        int ptx = 0, opp = 0, adj = 0, highest = 1000; // 1000 is an arbitrarly high value that will always be > tank.getY()
    
        // If the tank has fallen below ground (it moves multiple pixels between 
        // GroundCollision() calls), realign it with the center pixel. This is 
        // important for angle calculation below. 

            if (topy[(int)tank.getX()+13]-13 < (int)tank.getY()) //Allows tank to go up inclines
                tank.setY(topy[(int)tank.getX()+13]-13);
            if(tank.getY()<585) //makes the tank so the tank can't go below the bottom.
            {
            // If the tank's center point is above the ground make the tank fall. Checks to see if it should still be falling
            if (topy[(int)tank.getX()+13]-13 > (int)tank.getY())
                falling = true;
            }
        // Possibly add addional cases here that will stop it from falling for realism. 
        
        if (falling && !jumpUp) { 
            if (player == 1) {
                Tank1.fixTurret();  
            }
            else {
                Tank2.fixTurret();
            }
                
            tank.setVelocityY(.1f);
            
            /*if (player == 1 && !firstfall ) {
                if (!moving && !jumpFall)   
                    Tank1.setHealth(Tank1.getHealth()-1);//originally 5 changed to make for a more balanced game
                }   
            if (player == 2&& !firstfall2) {                    
                if (!moving && !jumpFall)
                    Tank2.setHealth(Tank2.getHealth()-1);//originally 5 changed to make for a more balanced game
            
            }*/
        
        } 
        else if (!falling && !jumpUp) {
            if(player ==1) firstfall=false;
            if(player==2)firstfall2=false;
            
            tank.setVelocityY(0f);
                   
            // Now that it stopped, calculate the rotation angle. This is done by 
            // forming a right triangle and calculating the angle using arctan(opposite/adjacent).                      
                        
            // We first determine if it will rotate to the left or right side. 
            // It makes the decision based on the ground at the x location of each side 
            // and the mid pt between middle and side. More cases may be necessary for 
            // additional realism. 
            
            // These loops find the "highest" (actually lowest on the game's y axis) ground
            // point within the range. It will use this to build the opposide side in the
            // triangle. The adjacent side is formed from that pt's x to the center point.
            
            if ((topy[(int)tank.getX()]-13 < (int)tank.getY()) && (topy[(int)tank.getX()+9]-13 < (int)tank.getY())) {
                // Rotating right
                for (int i = (int)(tank.getX()+13); i > tank.getX(); i--) {
                    if (topy[i] < (tank.getY()+13))  {
                        if (topy[i] < highest) {
                            highest = topy[i];
                            ptx = i;
                        }
                    }
                }
                
                opp = 13 - (topy[ptx] - (int)tank.getY());
                adj = 13 - (ptx - (int)tank.getX());                                
            }
            
            if ((topy[(int)tank.getX()+38]-13 < (int)tank.getY()) && (topy[(int)tank.getX()+29]-14 < (int)tank.getY())) {
                // Rotating left
                
                for (int i = (int)(tank.getX()+13); i < (tank.getX()+38); i++) {
                    if (topy[i] < (tank.getY()+13))  {
                        if (topy[i] < highest) {
                            highest = topy[i];
                            ptx = i;
                        }
                    }
                }
            
                opp = 13 - (topy[ptx] - (int)tank.getY());
                adj = ptx - (int)tank.getX() - 13;  
                angle *= -1;        
            }
            
            // Calculate the angle. Multiplying by the number will let us preserve the angle's sign. 
            if (adj != 0) 
                angle *= Math.atan((double)opp/adj); 
            else 
                angle = 0;
            //commented out to try no jumping game play
            // If the tank lands at the bottom of a cliff and there is room, straighten it out 
            if (((topy[(int)tank.getX()+57]) > (int)tank.getY()) && (angle > 1.5)) 
                tank.setX(tank.getX()+14);
            
            if (tank.getX() > 57) { // fix 56px bug
                if (((topy[(int)tank.getX()-57]) > (int)tank.getY()) && (angle < -1.5)) 
                    tank.setX(tank.getX()-13);
            }
            
            if (player == 1){
                Tank1.setTankSlant(angle);
                Tank1.fixTurret();
            }else{
                Tank2.setTankSlant(angle);
                Tank2.fixTurret();
            }
                
        }
        else if (jumpUp) {
            if (player == 1) {
                Tank1.fixTurret();
                }
            else {
                Tank2.fixTurret();

            }           
        }
                
        return;
    }


    
    public void setTesting(boolean t) { testing = t; }
    
    public void Restart()
    {
        restartgame=1;
    }   
    
       class FloatHealth {
        int health1counter = (int)Tank1.getTankSprite().getY()-50;  
        private final static int HCOUNTER_DELAY=10000; 
        Timer health1Timer;
        
        public void paintComponent(Graphics g)
        {
            if (health1Timer.isRunning())
            {
                health1counter--;  
                if (health1counter<=40)
                    health1Timer.stop();          
                
            }
            
        }
        public void startCounter() {
            if (health1Timer==null){
                health1counter = (int) Tank1.getTankSprite().getY()-50;
                health1Timer=new Timer(HCOUNTER_DELAY, new TimeHandler()); 
                health1Timer.start();    
            }
            else {
               if (!health1Timer.isRunning())
               {
                   health1Timer.restart();
                   
               }
            } 
           
        }
        
        public  void stopCounter(){
            health1Timer.stop();
        }
        private class TimeHandler implements ActionListener
        {
            public void actionPerformed(ActionEvent actionEvent)
            {
                paintComponent(null);   
            }
        }
        
    }
    
    public class FloatHealth2 {
    
        int health2counter=(int)Tank2.getTankSprite().getY()-50;
        private final static int H2COUNTER_DELAY=10000; 
        Timer health2Timer;
        
        public void paintComponent(Graphics g)
        {
            
            if (health2Timer.isRunning())
            {
                health2counter--;
                

                if (health2counter<=40)
                    health2Timer.stop();
            }
        }
        
        public void start2Counter(){
            if (health2Timer==null)
               {
                  health2counter=(int)Tank2.getTankSprite().getY()-50;
                  health2Timer=new Timer(H2COUNTER_DELAY, new TimeHandler());
                  health2Timer.start();
               }
               else
               {
                   if (!health2Timer.isRunning())
                       health2Timer.restart();
               }
        }
        public void stopCounter(){
            health2Timer.stop();
        }
        private class TimeHandler implements ActionListener
        {
            public void actionPerformed(ActionEvent actionEvent)
            {
                paintComponent(null);   
            }
        }
        
    


    }
}
