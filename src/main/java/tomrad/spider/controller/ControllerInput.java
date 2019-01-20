package tomrad.spider.controller;

import tomrad.spider.gait.AbstractPhoenixGait;

public class ControllerInput {
	
	  boolean		hexOn = false;			 //Switch to turn on Phoenix
	  boolean		prevHexOn = false;		 //Previous loop state
	  
	  Triple       BodyPos;			//Body position
	  Triple       BodyRotOffset;      // Body rotation offset;

	  //Body Inverse Kinematics
	  Triple       BodyRot1;            // X -Pitch, Y-Rotation, Z-Roll

	  //[gait]
	  byte			GaitType;			 //Gait type
	  byte          GaitStep;            //Actual current step in gait
	  AbstractPhoenixGait 	gaitCur;             // Definition of the current gait

	  short       	LegLiftHeight;		 //Current Travel height
	  Triple       TravelLength;        // X-Z or Length, Y is rotation.


	  //[Single Leg Control]
	  byte			SelectedLeg;
	  Triple       SLLeg;               // 
	  boolean		fSLHold;		 	 //Single leg control mode

	  //[Balance]
	  boolean       BalanceMode;

	  //[TIMING]
	  byte			InputTimeDelay;	//Delay that depends on the input to get the "sneaking" effect
	  short			SpeedControl;	//Adjustible Delay
	  byte       	ForceGaitStepCnt;          // new to allow us to force a step even when not moving

}
