package tomrad.spider.gait;

import static tomrad.spider.IkRoutines.GaitPosX;
import static tomrad.spider.IkRoutines.GaitPosY;
import static tomrad.spider.IkRoutines.GaitPosZ;
import static tomrad.spider.IkRoutines.GaitRotY;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tomrad.spider.TravelLength;

@Component
public class Gait 
{

	@Autowired
	private Logger log;

	// [CONSTANTS]
	public static final int cRR = 0;
	public static final int cRM = 1;
	public static final int cRF = 2;
	public static final int cLR = 3;
	public static final int cLM = 4;
	public static final int cLF = 5;

	// --------------------------------------------------------------------

	/** Current Travel height */
	public static double LegLiftHeight = Double.NaN; 	
	
	/** Array of known gaits. */
	private AbstractPhoenixGait[] gaits = {
			new Ripple6Gait(),
			new Ripple12Gait(),
			new Quadripple9Gait(),
			new Tripod4Gait(),
			new Tripod6Gait(),
			new Tripod8Gait(),
			new Wave12Gait(),
			new Wave18Gait()
	};
	
	/** Currently selected gait. Works as index into {@link gaits}. */
	private int gaitType = 0;
	
	/** Actual Gait step */
	private int GaitStep        = 255; 	
	/** Temp to check if the gait is in motion */
	private boolean GaitInMotion   = false;
	

	// --------------------------------------------------------------------
	@PostConstruct
	public void InitGait()
	{
	    GaitSelect(0);
	    LegLiftHeight = 30;
	    GaitStep = 1;
	    log.debug("InitGait: GaitType={}, legLiftHeight={}, GaitStep={}, NomGaitSpeed={}", gaitType, LegLiftHeight, GaitStep, gaits[gaitType].NomGaitSpeed);
	}
	
	public int GaitSelectNext()
	{
		int nextGaitType = (gaitType + 1) % gaits.length;
//		if (nextGaitType == 0)
//		{
//			// Sound P9,[50\4000, 50\4500]			
//		}
//		else {
//			// Sound P9,[50\4000]			
//		}
		GaitSelect(nextGaitType);
		return nextGaitType;
	}

	// --------------------------------------------------------------------
	protected void GaitSelect(int gaitType)
	{		
	    if (gaitType < 0 || gaitType >= gaits.length)
	    {
	    	throw new IllegalArgumentException(String.format("Unbekannte Gangart %d.", gaitType));
	    }
		this.gaitType = gaitType;
	    log.info("GaitSelect: gaitType={} ({})", gaitType, gaits[gaitType].Name);
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

	    AbstractPhoenixGait currentGait = gaits[gaitType];
	    int gaitLegNr = currentGait.GaitLegNr[LegNr];
	    int nrLiftedPos = currentGait.NrLiftedPos;
	    int stepsInGait = currentGait.StepsInGait;
	    int halfLiftHeight = currentGait.HalfLiftHeight;
	    int tlDivFactor = currentGait.TLDivFactor;
	    
	    // Leg middle up position
	    // Gait in motion														  Gait NOT in motion, return to home position
	    if ((GaitInMotion && GaitStep == gaitLegNr && (nrLiftedPos == 1 || nrLiftedPos == 3))
	        || (!GaitInMotion && GaitStep == gaitLegNr
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
	        if (((nrLiftedPos == 2 && GaitStep == gaitLegNr)
	            || (nrLiftedPos == 3 && (GaitStep == gaitLegNr - 1 || GaitStep == gaitLegNr + (stepsInGait - 1))))
	                && GaitInMotion)
	        {
			    log.trace("Gait: LegNr={}, LastLeg={}, Case 2", LegNr, LastLeg);
	            GaitPosX[LegNr] = -input.lengthX/2;
	            GaitPosY[LegNr] = -LegLiftHeight/(halfLiftHeight+1);
	            GaitPosZ[LegNr] = -input.lengthZ/2;
	            GaitRotY[LegNr] = -input.rotationY/2;
	        }
	        else
	        {
	            // Optional half heigth front
	            if ((nrLiftedPos >= 2) && (GaitStep == gaitLegNr + 1 || GaitStep == gaitLegNr-(stepsInGait-1))
	                    && GaitInMotion)
	            {
				    log.trace("Gait: LegNr={}, LastLeg={}, Case 3", LegNr, LastLeg);
	                GaitPosX[LegNr] = input.lengthX/2;
	                GaitPosY[LegNr] = -LegLiftHeight/(halfLiftHeight+1);
	                GaitPosZ[LegNr] = input.lengthZ/2;
	                GaitRotY[LegNr] = input.rotationY/2;
	            }
	            else
	            {
	                // Leg front down position
	                if ((GaitStep == gaitLegNr + nrLiftedPos || GaitStep == gaitLegNr - (stepsInGait-nrLiftedPos)) 
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
	                    GaitPosX[LegNr] = GaitPosX[LegNr] - (input.lengthX/tlDivFactor);
	                    GaitPosY[LegNr] = 0;
	                    GaitPosZ[LegNr] = GaitPosZ[LegNr] - (input.lengthZ/tlDivFactor);
	                    GaitRotY[LegNr] = GaitRotY[LegNr] - (input.rotationY/tlDivFactor);
	                }
	            }
	        }
	    }
	    
	    // Advance to the next step
	    if (LastLeg)     // The last leg in this step
	    {
	        GaitStep = GaitStep+1;
	        log.trace("Gait: LastLeg=True, GaitStep={}", GaitStep);
	        if (GaitStep > stepsInGait)
	        {
	            GaitStep = 1;
	            log.trace("Gait: GaiStep > StepsInGait, GaitStep={}", GaitStep);
	        }
	    }
	    return input;
	}

	public int getNominalSpeed() {
		return gaits[gaitType].NomGaitSpeed;
	}
	
}
