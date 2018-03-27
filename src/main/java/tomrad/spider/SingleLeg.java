package tomrad.spider;

import static tomrad.spider.Gait.cLF;
import static tomrad.spider.Gait.cLM;
import static tomrad.spider.Gait.cLR;
import static tomrad.spider.Gait.cRF;
import static tomrad.spider.Gait.cRM;
import static tomrad.spider.Gait.cRR;
import static tomrad.spider.IkRoutines.LegPosX;
import static tomrad.spider.IkRoutines.LegPosY;
import static tomrad.spider.IkRoutines.LegPosZ;
import static tomrad.spider.IkRoutines.cInitPosX;
import static tomrad.spider.IkRoutines.cInitPosY;
import static tomrad.spider.IkRoutines.cInitPosZ;
import static tomrad.spider.PhoenixControlPs2.SLHold;
import static tomrad.spider.PhoenixControlPs2.SLLegX;
import static tomrad.spider.PhoenixControlPs2.SLLegY;
import static tomrad.spider.PhoenixControlPs2.SLLegZ;

import java.util.Arrays;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//from Gait import cLR, cLF, cLM, cRF, cRM, cRR
//from IkRoutines import LegPosX, LegPosY, LegPosZ, cInitPosX, cInitPosY, cInitPosZ
//from PhoenixControlPs2 import SLHold, SLLegX, SLLegY, SLLegZ
//
//import logging
@Component
public class SingleLeg {

	@Autowired
	private Logger log;

	// [Single Leg Control]
	static int SelectedLeg = 255;
	private int Prev_SelectedLeg = 255;
	public static boolean AllDown = false;


	// --------------------------------------------------------------------
	// Single leg control. Make sure no leg is selected
	public void InitSingleLeg()
	{
	    SelectedLeg = 255;  // No Leg selected
	    Prev_SelectedLeg = 255;
	    log.debug("InitSingleLeg: SelectedLeg={}, Prev_SelectedLeg={}", SelectedLeg, Prev_SelectedLeg);
	}


	// --------------------------------------------------------------------
	// [SINGLE LEG CONTROL]
	public void SingleLegControl()
	{
	    // Check if all legs are down
	    AllDown = LegPosY[cRF] == cInitPosY[cRF] && LegPosY[cRM] == cInitPosY[cRM] 
	        && LegPosY[cRR] == cInitPosY[cRR] && LegPosY[cLR] == cInitPosY[cLR] 
	        && LegPosY[cLM] == cInitPosY[cLM] && LegPosY[cLF] == cInitPosY[cLF];

	    log.debug("SingleLegControl: AllDown={}, SelectedLeg={}, Prev_SelectedLeg={}", AllDown, SelectedLeg, Prev_SelectedLeg);

	    if (0 <= SelectedLeg && SelectedLeg <= 5)
	    {
	        if (SelectedLeg != Prev_SelectedLeg)
	        {
	            if (AllDown)  // Lift leg a bit when it got selected
	            {
	                LegPosY[SelectedLeg] = cInitPosY[SelectedLeg] - 20;

	                // Store current status
	                Prev_SelectedLeg = SelectedLeg;
	            }
	            else  // Return prev leg back to the init position
	            {
	                LegPosX[Prev_SelectedLeg] = cInitPosX[Prev_SelectedLeg];
	                LegPosY[Prev_SelectedLeg] = cInitPosY[Prev_SelectedLeg];
	                LegPosZ[Prev_SelectedLeg] = cInitPosZ[Prev_SelectedLeg];
	            }
	        }
	        else if (!SLHold)
	        {
	            LegPosY[SelectedLeg] = LegPosY[SelectedLeg] + SLLegY;
	            LegPosX[SelectedLeg] = cInitPosX[SelectedLeg] + SLLegX;
	            LegPosZ[SelectedLeg] = cInitPosZ[SelectedLeg] + SLLegZ;
	        }
	    }
	    else  // All legs to init position
	    {
	        if (!AllDown)
	        {
	            for (int legIndex = 0; legIndex < 6; legIndex++)
	            {
	                LegPosX[legIndex] = cInitPosX[legIndex];
	                LegPosY[legIndex] = cInitPosY[legIndex];
	                LegPosZ[legIndex] = cInitPosZ[legIndex];
	            }
	        }

	        if (Prev_SelectedLeg != 255)
	        {
	            Prev_SelectedLeg = 255;
	        }
	    }

	    log.debug("SingleLegControl: LegPosX={}", Arrays.toString(LegPosX));
	    log.debug("SingleLegControl: LegPosY={}", Arrays.toString(LegPosY));
	    log.debug("SingleLegControl: LegPosZ={}", Arrays.toString(LegPosZ));
	}
}
