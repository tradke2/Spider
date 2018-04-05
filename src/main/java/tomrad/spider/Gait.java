package tomrad.spider;

import static tomrad.spider.IkRoutines.GaitPosX;
import static tomrad.spider.IkRoutines.GaitPosY;
import static tomrad.spider.IkRoutines.GaitPosZ;
import static tomrad.spider.IkRoutines.GaitRotY;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Gait 
{

	@Autowired
	private Logger log;

	// [CONSTANTS]
	static final int cRR = 0;
	static final int cRM = 1;
	static final int cRF = 2;
	static final int cLR = 3;
	static final int cLM = 4;
	static final int cLF = 5;

	// --------------------------------------------------------------------
	// [gait]
	static int GaitType        = 255; // Gait type

	static int NomGaitSpeed = 0;  // Nominal speed of the gait

	static double LegLiftHeight = Double.NaN; 	// Current Travel height

	private int TLDivFactor     = 255;  // Number of steps that a leg is on the floor while walking
	private int NrLiftedPos     = 255;  // Number of steps that a single leg is lifted (1-3)
	private double HalfLiftHeigth  = Double.NaN;  // If TRUE the outer positions of the ligted legs will be half height

	private boolean GaitInMotion   = false;  // Temp to check if the gait is in motion
	private int StepsInGait     = 255;  // Number of steps in gait
	private int GaitStep        = 255; 	// Actual Gait step

	private int GaitLegNr[]     = new int[] {255, 255, 255, 255, 255, 255};  // Init position of the leg

	// --------------------------------------------------------------------
	@PostConstruct
	public void InitGait()
	{
	    GaitType = 0;
	    LegLiftHeight = 30;
	    GaitStep = 1;
	    // fall through to GaitSelect
	    GaitSelect(0);
	    log.debug("InitGait: GaitType={}, legLiftHeight={}, GaitStep={}, NomGaitSpeed={}", GaitType, LegLiftHeight, GaitStep, NomGaitSpeed);
	}

	// --------------------------------------------------------------------
	public void GaitSelect(int gaitType)
	{
		this.GaitType = gaitType;
		
	    // Gait selector
	    if (GaitType == 0)     // Ripple Gait 6 steps
	    {
	        GaitLegNr[cLR] = 1;
	        GaitLegNr[cRF] = 2;
	        GaitLegNr[cLM] = 3;
	        GaitLegNr[cRR] = 4;
	        GaitLegNr[cLF] = 5;
	        GaitLegNr[cRM] = 6;

	        NrLiftedPos = 1;
	        HalfLiftHeigth = 0;
	        TLDivFactor = 4;
	        StepsInGait = 6;
	        NomGaitSpeed = 100;
	    }
	    else if (GaitType == 1)       // Ripple Gait 12 steps
	    {
	        GaitLegNr[cLR] = 1;
	        GaitLegNr[cRF] = 3;
	        GaitLegNr[cLM] = 5;
	        GaitLegNr[cRR] = 7;
	        GaitLegNr[cLF] = 9;
	        GaitLegNr[cRM] = 11;

	        NrLiftedPos = 3;
	        HalfLiftHeigth = 0;  // 1
	        TLDivFactor = 8;
	        StepsInGait = 12;
	        NomGaitSpeed = 85;
	    }
	    else if (GaitType == 2)     // Quadripple 9 steps
	    {
	        GaitLegNr[cLR] = 1;
	        GaitLegNr[cRF] = 2;
	        GaitLegNr[cLM] = 4;
	        GaitLegNr[cRR] = 5;
	        GaitLegNr[cLF] = 7;
	        GaitLegNr[cRM] = 8;

	        NrLiftedPos = 2;
	        HalfLiftHeigth = 0;
	        TLDivFactor = 6;
	        StepsInGait = 9;
	        NomGaitSpeed = 100;
	    }
	    else if (GaitType == 3)     // Tripod 4 steps
	    {
	        GaitLegNr[cLR] = 3;
	        GaitLegNr[cRF] = 1;
	        GaitLegNr[cLM] = 1;
	        GaitLegNr[cRR] = 1;
	        GaitLegNr[cLF] = 3;
	        GaitLegNr[cRM] = 3;

	        NrLiftedPos = 1;
	        HalfLiftHeigth = 0;
	        TLDivFactor = 2;
	        StepsInGait = 4;
	        NomGaitSpeed = 150;
	    }
	    else if (GaitType == 4)     // Tripod 6 steps
	    {
	        GaitLegNr[cLR] = 4;
	        GaitLegNr[cRF] = 1;
	        GaitLegNr[cLM] = 1;
	        GaitLegNr[cRR] = 1;
	        GaitLegNr[cLF] = 4;
	        GaitLegNr[cRM] = 4;

	        NrLiftedPos = 2;
	        HalfLiftHeigth = 0;
	        TLDivFactor = 4;
	        StepsInGait = 6;
	        NomGaitSpeed = 100;
	    }
	    else if (GaitType == 5)     // Tripod 8 steps
	    {
	        GaitLegNr[cLR] = 5;
	        GaitLegNr[cRF] = 1;
	        GaitLegNr[cLM] = 1;
	        GaitLegNr[cRR] = 1;
	        GaitLegNr[cLF] = 5;
	        GaitLegNr[cRM] = 5;

	        NrLiftedPos = 3;
	        HalfLiftHeigth = 1;
	        TLDivFactor = 4;
	        StepsInGait = 8;
	        NomGaitSpeed = 85;
	    }
	    else if (GaitType == 6)     // Wave 12 steps
	    {
	        GaitLegNr[cLR] = 1;
	        GaitLegNr[cRF] = 11;
	        GaitLegNr[cLM] = 3;

	        GaitLegNr[cRR] = 7;
	        GaitLegNr[cLF] = 5;
	        GaitLegNr[cRM] = 9;

	        NrLiftedPos = 1;
	        HalfLiftHeigth = 0;
	        TLDivFactor = 10;
	        StepsInGait = 12;
	        NomGaitSpeed = 85;
	    }
	    else if (GaitType == 7)     // Wave 18 steps
	    {
	        GaitLegNr[cLR] = 4;
	        GaitLegNr[cRF] = 1;
	        GaitLegNr[cLM] = 7;

	        GaitLegNr[cRR] = 13;
	        GaitLegNr[cLF] = 10;
	        GaitLegNr[cRM] = 16;

	        NrLiftedPos = 2;
	        HalfLiftHeigth = 0;
	        TLDivFactor = 16;
	        StepsInGait = 18;
	        NomGaitSpeed = 85;
	    }
	    else
	    {
	    	throw new IllegalArgumentException(String.format("Unbekannter GaitType %d.", gaitType));
	    }
	    log.debug("GaitSelect: GaitType={}", GaitType);
	}

	// --------------------------------------------------------------------
	// [GAIT Sequence]
	public TravelLength GaitSeq(TravelLength input)
	{
	    log.debug("GaitSeq: input={}", input.toString());
		log.debug("GaitSeq: GaitPosX={}", Arrays.toString(GaitPosX));
		log.debug("GaitSeq: GaitPosY={}", Arrays.toString(GaitPosY));
		log.debug("GaitSeq: GaitPosZ={}", Arrays.toString(GaitPosZ));
	    
	    TravelLength result = new TravelLength(input);
	    // Calculate Gait sequence
	    boolean LastLeg = false;  // TRUE when the current leg is the last leg of the sequence
	    for (int LegIndex = 0; LegIndex < 6; LegIndex++)    // for all legs
	    {
	        if (LegIndex == 5)           // last leg
	        {
	            LastLeg = true;
	        }
	        result = LegGait(LegIndex, LastLeg, result);
	    }
	    
		log.debug("GaitSeq: GaitPosX={}", Arrays.toString(GaitPosX));
		log.debug("GaitSeq: GaitPosY={}", Arrays.toString(GaitPosY));
		log.debug("GaitSeq: GaitPosZ={}", Arrays.toString(GaitPosZ));
	    log.debug("GaitSeq: result={}", result.toString());
	    return result;
	}

	// --------------------------------------------------------------------
	// [GAIT]
	private TravelLength LegGait(int LegNr, boolean LastLeg, TravelLength input)
	{
	    log.trace("Gait: input={}", input.toString());
	    
	    // Check IF the Gait is in motion
	    GaitInMotion = input.isInMotion();
	    log.trace("Gait: GaitInMotion={}", GaitInMotion);

	    // Clear values under the cTravelDeadZone
	    if (!GaitInMotion)
	    {
	    	input = new TravelLength(0, 0, 0);
	    }


	    // Leg middle up position
	    // Gait in motion														  Gait NOT in motion, return to home position
	    if ((GaitInMotion && GaitStep == GaitLegNr[LegNr] && (NrLiftedPos == 1 || NrLiftedPos == 3))
	        || (!GaitInMotion && GaitStep == GaitLegNr[LegNr]
	            && ((Math.abs(GaitPosX[LegNr]) > 2) || (Math.abs(GaitPosZ[LegNr]) > 2) || (Math.abs(GaitRotY[LegNr]) > 2))))   // Up
	    {
		    log.trace("Gait: LegNr={}, LastLeg={}, Case 1", LegNr, LastLeg);
	        GaitPosX[LegNr] = 0;
	        GaitPosY[LegNr] = -LegLiftHeight;
	        GaitPosZ[LegNr] = 0;
	        GaitRotY[LegNr] = 0;
	    }
	    else
	    {
	        // Optional Half heigth Rear
	        if (((NrLiftedPos == 2 && GaitStep == GaitLegNr[LegNr])
	            || (NrLiftedPos == 3 && (GaitStep == GaitLegNr[LegNr] - 1 || GaitStep == GaitLegNr[LegNr] + (StepsInGait - 1))))
	                && GaitInMotion)
	        {
			    log.trace("Gait: LegNr={}, LastLeg={}, Case 2", LegNr, LastLeg);
	            GaitPosX[LegNr] = -input.lengthX/2;
	            GaitPosY[LegNr] = -LegLiftHeight/(HalfLiftHeigth+1);
	            GaitPosZ[LegNr] = -input.lengthZ/2;
	            GaitRotY[LegNr] = -input.rotationY/2;
	        }
	        else
	        {
	            // Optional half heigth front
	            if ((NrLiftedPos >= 2) && (GaitStep == GaitLegNr[LegNr] + 1 || GaitStep == GaitLegNr[LegNr]-(StepsInGait-1))
	                    && GaitInMotion)
	            {
				    log.trace("Gait: LegNr={}, LastLeg={}, Case 3", LegNr, LastLeg);
	                GaitPosX[LegNr] = input.lengthX/2;
	                GaitPosY[LegNr] = -LegLiftHeight/(HalfLiftHeigth+1);
	                GaitPosZ[LegNr] = input.lengthZ/2;
	                GaitRotY[LegNr] = input.rotationY/2;
	            }
	            else
	            {
	                // Leg front down position
	                if ((GaitStep == GaitLegNr[LegNr] + NrLiftedPos || GaitStep == GaitLegNr[LegNr] - (StepsInGait-NrLiftedPos)) 
	                    && GaitPosY[LegNr] < 0)
	                {
	    			    log.trace("Gait: LegNr={}, LastLeg={}, Case 4", LegNr, LastLeg);
	                    GaitPosX[LegNr] = input.lengthX/2;
	                    GaitPosY[LegNr] = 0;  // Only move leg down at once if terrain adaption is turned off
	                    GaitPosZ[LegNr] = input.lengthZ/2;
	                    GaitRotY[LegNr] = input.rotationY/2;
	                }
	                // Move body forward
	                else
	                {
	    			    log.trace("Gait: LegNr={}, LastLeg={}, Case 5", LegNr, LastLeg);
	                    GaitPosX[LegNr] = GaitPosX[LegNr] - (input.lengthX/TLDivFactor);
	                    GaitPosY[LegNr] = 0;
	                    GaitPosZ[LegNr] = GaitPosZ[LegNr] - (input.lengthZ/TLDivFactor);
	                    GaitRotY[LegNr] = GaitRotY[LegNr] - (input.rotationY/TLDivFactor);
	                }
	            }
	        }
	    }
	    
	    // Advance to the next step
	    if (LastLeg)     // The last leg in this step
	    {
	        GaitStep = GaitStep+1;
	        log.trace("Gait: LastLeg=True, GaitStep={}", GaitStep);
	        if (GaitStep > StepsInGait)
	        {
	            GaitStep = 1;
	            log.trace("Gait: GaiStep > StepsInGait, GaitStep={}", GaitStep);
	        }
	    }
	    return input;
	}
	
}
