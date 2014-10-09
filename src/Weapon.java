import java.awt.*;
import java.util.Random;

import ScreenManagement.*;

//Created by Andrew Searles
/*
	No i did not write or design this crapy method for shooting. 
	I just took the very unclear code and made it a little bit
	more modular. hopefully if this code has not died yet you will
	have a lot easier of a time figuring out whats going on here
	and how easy it is to actually fix or add different types of
	weapons. good luck and just remember it was 15x worse when we
	got it. peace free love and allthat nonsense

*/
//Weapon Class
public class Weapon 
{
	
	//Variables
	private Game TG;
	private Sprite TS;
	private Tank T;
	private Graphics g;
	private static final Random random = new Random();

	//constructor for weapon class
	public Weapon(Game G, Tank tank){
		TG = G;
		T = tank;				
	}//end constructor
	
    //Sets the state = 1 for firing
	public void fire() {
		TS.setState(1);
		//TG.tankshoot = 1; Dont forget to think about how bad tankshoot is for firing
	}
	
	//sets the Sprite
	public void setSprite(Sprite S) {
		TS = S;
	}
	
	//sets the graphics
	public void setGraphics(Graphics newG) {
		g = newG;
	}
	
	//Simple Method to return the Sprite
	public Sprite getSprite() {
		return TS;
	}
	
	//Simple method to return the tank
	public Tank getTank() {
		return T;
	}
	
	
	//Update the Sprite While it travels through the air
	public void updateSpriteInAir() {
        if (TS.getState()==1 )
        {
        	g.setColor(Color.black);
            g.drawImage(TS.getImage(), Math.round(TS.getX()), Math.round(TS.getY()), null); 
        }		
	}//end updateSpriteInAir Method


	//lol name of method says whats going on here
	public void outOfBoundsCheck() {
        if ((TS.getState()==1))
        {
	        if(TS.getX() < 0 || TS.getX() > 800)
	        { 
	        	TG.hitTest = 2;
	            TS= resetShot(TS);            
	        }
        }
	}//end outOfBoundsCheck() Method	
	

	//Checks for hit test to use for explosions 
	public void explosionOnContact() {
	      //Draws explosion on contact
        if (TG.hitTest == 2)
        {
                if(TS.getState()==1)
                {
                    g.drawImage(TG.TankBoom.getImage(), Math.round(TS.getX()), (Math.round(TS.getY())), null);
                    TS = resetShot(TS);
                } //end if tankshot get state                                                          
        }//end if hittest
	}//end explosionOnContact Method
	

	//this method is also named well
	private Sprite resetShot(Sprite shot)
    {
        //IF contact has been made switch turnes
		if (TG.hitTest == 2)
        {       	
            TG.turn=(2/TG.turn);  //switches the other player's turn
            TG.curfuel = 110;
            TG.nextTurn = true;
        }
		//Disable shot properties
        shot.setState(0);
        shot.setVelocityX(0f);
        shot.setVelocityY(0f);
        TG.hitTest = 0;
        shot.setX(1f);
        shot.setY(-4000f);
        
        return shot;
    }//End resetShot
	
	
	//Fires the shot
	public void TankFire()
    {
		//variables for velocity
		float Xveloc;
		float Yveloc;
		//obtain the velocity from the power of the tank
        Xveloc=(float)((-1)*T.getShotPower()*Math.cos(T.getAngle()/57.3));
        Yveloc=(float)(T.getShotPower()*Math.sin(T.getAngle()/57.3));
        //draw the sprite
        g.drawImage(TS.getImage(), T.getTurretAngleX(), T.getTurretAngleY(), null);
        TS.setState(1);
        TS.setX(T.getTurretAngleX());
        TS.setY(T.getTurretAngleY());


        // set the velocity
        TS.setVelocityX(Xveloc); 
        TS.setVelocityY(Yveloc); 
        
        //play the firing sound
        //TG.soundShot.play();
    }//end TankFire Method
	
	//unfinished method trying to make more shots on collision (volcano bomb) or cluster Bomb
	//consider using a stack
	public void ClusterFire() {
		
		g.drawImage(TS.getImage(), (int)TS.getX(), (int)TS.getY()+20, null);
		TS.setState(1);
		TS.setX(TS.getX());
        TS.setY(TS.getY()+20);
        TS.setVelocityX(1); 
        TS.setVelocityY(7);
	}//end method ClusterFire()
	
	
	//makes bullet fall and collide
	//This is where health is calculated
	public void GravityAndCollision(Tank Tnk)
	{
        if(TS.getState()==1)
        {
        	//Makes shot gravity
            TS.setVelocityX(TS.getVelocityX() - TG.Windf); //WIND
            TS.setVelocityY(TS.getVelocityY() + (TG.Gravf));   //Gravity
           
            // Shot collision detection
            if (ShotCollision(Tnk.getTankSprite(), TS)) {
               Tnk.setHealth(Tnk.getHealth() - (10 + random.nextInt(7)));// subtract 10 points off player 1's health if hit
               TG.hitTest=2;                 
           }
            
            if (ShotCollision(T.getTankSprite(), TS)) {
               T.setHealth(T.getHealth() - (10 + random.nextInt(7)));// subtract 10 points off player 1's health if hit
               TG.hitTest=2;                 
           }
        }
        
	}//GravityAndCollision end
	  
	
	//Creates the indentation if it hits land otherwise leaves Shot alone.
    public void fireShot()
    {   
        if(TS.getX() > 0)//inside the screen
        {   
            if(TG.topy[Math.round(TS.getX())] < Math.round(TS.getY()))//above the top of the terrain
            {
                CreateHole(TS);
            }
        }                
    }//end fireShot() Method
    
    
    //checks if a shot collides with a tank
    public boolean ShotCollision(Sprite tank, Sprite shot) {        
        boolean ret = false; 
    
        int radius = 20; //pixels
        int PlayerLowX = ((Math.round(tank.getX())));
        int PlayerLowY = ((Math.round(tank.getY())));
        int PlayerHighX = ((Math.round(tank.getX())) + (tank.getWidth()));
        int PlayerHighY = ((Math.round(tank.getY())) + (tank.getHeight()));
        int ShotLowX = ((Math.round(shot.getX())));
        int ShotLowY = ((Math.round(shot.getY())));
        int ShotHighX = ((Math.round(shot.getX())) + (shot.getWidth()));
        
        //added by Ryan Kleckner to make the second shot path act like a homing missile
        //4/29/08
    //    if(SecondaryTankShot.getState()==1 && Math.round(SecondaryTankShot.getX()) >= Math.round(Tank2.getTankSprite().getX()))
    //        {
    //            SecondaryTankShot.setVelocityX(0f);
     //           SecondaryTankShot.setVelocityY(0.5f);
     //       }
   //     if(SecondaryTankShot2.getState()==1 && Math.round(SecondaryTankShot2.getX()) <= Math.round(Tank1.getTankSprite().getX()+30))
   //         {
    //            SecondaryTankShot2.setVelocityX(0f);
    //            SecondaryTankShot2.setVelocityY(0.5f);
    //        }
        
        if (ShotLowX > PlayerLowX - radius|| ShotHighX > PlayerLowX - radius) {
            if(ShotLowY > PlayerLowY) {         
                if (ShotHighX < PlayerHighX + radius || ShotLowX < PlayerHighX - radius) { 
	                    if (ShotLowY < PlayerHighY + radius) {
	                        CreateHole(shot);
	                        ret = true;    
	                        
                	}
                }
            }
        }        
        return ret; 
    }//end ShotCollision Method
    
    
    
    //creates a crater when it is called by collision
    private void CreateHole(Sprite shot)
    {
            TG.soundShot.close(); //resets sound after being shot
            TG.hitTest = 2;//says the object hit
            int counter3=Math.round(shot.getX())-14; //Left/Right Starting Square pos adjustment on hole
            int holderSin=0;
            int holderCos=1;
            double groundHolder=180;        
            while (groundHolder <= 359)//creates hole starting at 180 degrees and going to 360
            {
                holderSin = (int)(Math.floor((Math.sin(groundHolder/57.3))*5));//uses a length of 10 for hole
                if (counter3>3)
                {
                    TG.topy[counter3] = TG.topy[counter3]- holderSin;//reduces terrain
                }//depth of square //

                //creates an unequal cirlce  <--- lol this guy cant spell                   
                if (holderCos==3)
                {
                    counter3++;
                    holderCos=0;    
                }

                holderCos++;
                groundHolder++; 
            }//end while loop           
    }//end CreateHole method
    


}//end Weapon.java Class