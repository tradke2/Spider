package tomrad.spider;

/**
 * Project Lynxmotion Phoenix
 * Description: chr-3, configuration file.
 *       All Hardware connections (excl controls) and body dimensions 
 *       are configurated in this file. Can be used with V2.0 and above
 * Configuration version: V1.0
 * Date: Nov 1, 2009
 * Programmer: Kurt (aka KurtE)
 * 
 * Hardware setup: ABB2 with ATOM 28 Pro, SSC32 V2, (See further for connections)
 *
 * NEW IN V1.0
 *   - First Release
 *
 */
public class Config_Ch3 {
	// --------------------------------------------------------------------
	// [SERIAL CONNECTIONS]
	// cSSC_OUT         con P10      // Output pin for (SSC32 RX) on BotBoard (Yellow)
	// cSSC_IN          con P11      // Input pin for (SSC32 TX) on BotBoard (Blue)
	public final static String cSSC_DEVICE = "/dev/ttyUSB0";
	public final static int cSSC_BAUD      = 115200;  // SSC32 BAUD rate
	// --------------------------------------------------------------------
	// [BB2 PIN NUMBERS]
	// cEyesPin      con P8
	// --------------------------------------------------------------------
	// [SSC PIN NUMBERS]
	/** Pin number Rear Right leg Hip Horizontal */
	public final static String cRRCoxaPin     = "0";    
	/** Rear Right leg Hip Vertical */
	public final static String cRRFemurPin    = "1";    
	/** Rear Right leg Knee */
	public final static String cRRTibiaPin    = "2";    

	/** Middle Right leg Hip Horizontal */
	public final static String cRMCoxaPin     = "4";    
	/** Middle Right leg Hip Vertical */
	public final static String cRMFemurPin    = "5";    
	/** Middle Right leg Knee */
	public final static String cRMTibiaPin    = "6";    

	/** Front Right leg Hip Horizontal */
	public final static String cRFCoxaPin     = "8";    
	/** Front Right leg Hip Vertical */
	public final static String cRFFemurPin    = "9";    
	/** Front Right leg Knee */
	public final static String cRFTibiaPin    = "10";   

	 /** Rear Left leg Hip Horizontal */
	public final static String cLRCoxaPin     = "16";  
	/** Rear Left leg Hip Vertical */
	public final static String cLRFemurPin    = "17";   
	/** Rear Left leg Knee */
	public final static String cLRTibiaPin    = "18";   

	/** Middle Left leg Hip Horizontal */
	public final static String cLMCoxaPin     = "20";   
	/** Middle Left leg Hip Vertical */
	public final static String cLMFemurPin    = "21";   
	/** Middle Left leg Knee */
	public final static String cLMTibiaPin    = "22";   

	/** Front Left leg Hip Horizontal */
	public final static String cLFCoxaPin     = "24";   
	/** Front Left leg Hip Vertical */
	public final static String cLFFemurPin    = "25";   
	/** Front Left leg Knee */
	public final static String cLFTibiaPin    = "26";   
	// --------------------------------------------------------------------
	// [MIN/MAX ANGLES]
	public final static int cRRCoxaMin    = -75;      // Mechanical limits of the Right Rear Leg
	public final static int cRRCoxaMax    = 75;
	public final static int cRRFemurMin   = -90;
	public final static int cRRFemurMax   = 55;
	public final static int cRRTibiaMin   = -40;
	public final static int cRRTibiaMax   = 75;

	public final static int cRMCoxaMin    = -75;      // Mechanical limits of the Right Middle Leg
	public final static int cRMCoxaMax    = 75;
	public final static int cRMFemurMin   = -90;
	public final static int cRMFemurMax   = 55;
	public final static int cRMTibiaMin   = -40;
	public final static int cRMTibiaMax   = 75;

	public final static int cRFCoxaMin    = -75;      // Mechanical limits of the Right Front Leg
	public final static int cRFCoxaMax    = 75;
	public final static int cRFFemurMin   = -90;
	public final static int cRFFemurMax   = 55;
	public final static int cRFTibiaMin   = -40;
	public final static int cRFTibiaMax   = 75;

	public final static int cLRCoxaMin    = -75;      // Mechanical limits of the Left Rear Leg
	public final static int cLRCoxaMax    = 75;
	public final static int cLRFemurMin   = -90;
	public final static int cLRFemurMax   = 55;
	public final static int cLRTibiaMin   = -40;
	public final static int cLRTibiaMax   = 75;

	public final static int cLMCoxaMin    = -75;      // Mechanical limits of the Left Middle Leg
	public final static int cLMCoxaMax    = 75;
	public final static int cLMFemurMin   = -90;
	public final static int cLMFemurMax   = 55;
	public final static int cLMTibiaMin   = -40;
	public final static int cLMTibiaMax   = 75;

	public final static int cLFCoxaMin    = -75;      // Mepublic final static int chanipublic final static int cal limits of the Left Front Leg
	public final static int cLFCoxaMax    = 75;
	public final static int cLFFemurMin   = -90;
	public final static int cLFFemurMax   = 55;
	public final static int cLFTibiaMin   = -40;
	public final static int cLFTibiaMax   = 75;
	// --------------------------------------------------------------------
	// [BODY DIMENSIONS]
	public final static int cCoxaLength      =  29;     // 1.14" = 29mm (1.14 * 25.4)
	public final static int cFemurLength     =  57;     // 2.25" = 57mm (2.25 * 25.4)
	public final static int cTibiaLength     = 141;     // 5.55" = 141mm (5.55 * 25.4)
	
	/** Default Coxa offset angle */
	public final static int cRRCoxaAngle    = -60;    
	/** Default Coxa offset angle */
	public final static int cRMCoxaAngle    = 0;      
	/** Default Coxa offset angle */
	public final static int cRFCoxaAngle    = 60;     
	/** Default Coxa offset angle */
	public final static int cLRCoxaAngle    = -60;    
	/** Default Coxa offset angle */
	public final static int cLMCoxaAngle    = 0;      
	/** Default Coxa offset angle */
	public final static int cLFCoxaAngle    = 60;     
	
	public final static int cRROffsetX       = -69;     // Distance X from center of the body to the Right Rear coxa
	public final static int cRROffsetZ       = 119;     // Distance Z from center of the body to the Right Rear coxa
	public final static int cRMOffsetX       = -138;    // Distance X from center of the body to the Right Middle coxa
	public final static int cRMOffsetZ       = 0;       // Distance Z from center of the body to the Right Middle coxa
	public final static int cRFOffsetX       = -69;     // Distance X from center of the body to the Right Front coxa
	public final static int cRFOffsetZ       = -119;    // Distance Z from center of the body to the Right Front coxa
	
	public final static int cLROffsetX       = 69;      // Distance X from center of the body to the Left Rear coxa
	public final static int cLROffsetZ       = 119;     // Distance Z from center of the body to the Left Rear coxa
	public final static int cLMOffsetX       = 138;     // Distance X from center of the body to the Left Middle coxa
	public final static int cLMOffsetZ       = 0;       // Distance Z from center of the body to the Left Middle coxa
	public final static int cLFOffsetX       = 69;      // Distance X from center of the body to the Left Front coxa
	public final static int cLFOffsetZ       = -119;    // Distance Z from center of the body to the Left Front coxa

	// --------------------------------------------------------------------
	// [START POSITIONS FEET]
	public final static int cRRInitPosX    = 52;      // Start positions of the Right Rear leg
	public final static int cRRInitPosY    = 80;
	public final static int cRRInitPosZ    = 91;
	
	public final static int cRMInitPosX    = 105;      // Start positions of the Right Middle leg
	public final static int cRMInitPosY    = 80;
	public final static int cRMInitPosZ    = 0;
	
	public final static int cRFInitPosX    = 52;      // Start positions of the Right Front leg
	public final static int cRFInitPosY    = 80;
	public final static int cRFInitPosZ    = -91;
	
	public final static int cLRInitPosX    = 52;      // Start positions of the Left Rear leg
	public final static int cLRInitPosY    = 80;
	public final static int cLRInitPosZ    = 91;
	
	public final static int cLMInitPosX    = 105;      // Start positions of the Left Middle leg
	public final static int cLMInitPosY    = 80;
	public final static int cLMInitPosZ    = 0;
	
	public final static int cLFInitPosX    = 52;      // Start positions of the Left Front leg
	public final static int cLFInitPosY    = 80;
	public final static int cLFInitPosZ    = -91;
	// --------------------------------------------------------------------

	/** Servo characteristic: degrees per microsecond pulse width */
	public final static double cDegreesPerMicroSecond = 0.102;	// Hitec HS-485HB
	
	
	
}
