package tomrad.spider;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// File PiPS2.h
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class Ps2ControllerConstants {

	// Bit Operation Macros
//	def SET(x,y):
//	    return x | (1<<y)	// Set bit y in x
//
//	def CLR(x,y):
//	    return x & (~(1<<y)) // Clear bit y in x
//
//	def CHK(x,y):
//	    return (x>>y) & 1 	// Check if bit y in x is set
//
//	def TOG(x,y):
//	    return x ^ (1<<y) 	// Toggle bit y in x

	// Delays
	public static final int CLK_DELAY       = 4;
	public static final int BYTE_DELAY      = 20;
	public static final int MAX_READ_DELAY  = 10;

	// Maximum number of init tries
	public static final int MAX_INIT_ATTEMPT = 10;

	// Controller Modes - From: http://www.lynxmotion.com/images/files/ps2cmd01.txt 
	public static final short DIGITALMODE     = 0x41;
	public static final short ANALOGMODE      = 0x73;
	public static final short ALLPRESSUREMODE = 0x79;
	public static final short DS2NATIVEMODE   = 0xF3;

	// Button Masks
	//// 		From data bit 0 (PS2data[3])
	public static final int BTN_SELECT    = 0;
	public static final int BTN_LEFT_JOY  = 1;
	public static final int BTN_RIGHT_JOY = 2;
	public static final int BTN_START     = 3;
	public static final int BTN_UP        = 4;
	public static final int BTN_RIGHT     = 5;
	public static final int BTN_DOWN      = 6;
	public static final int BTN_LEFT      = 7;
	////   From data bit 1 (PSdata[4])
	public static final int BTN_L2        = 0;
	public static final int BTN_R2        = 1;
	public static final int BTN_L1        = 2;
	public static final int BTN_R1        = 3;
	public static final int BTN_TRIANGLE  = 4;
	public static final int BTN_CIRCLE    = 5;
	public static final int BTN_X         = 6;
	public static final int BTN_SQUARE    = 7;

	// Byte Numbers of PSdata[] For Button Pressures
	public static final int PRES_RIGHT    = 10;
	public static final int PRES_LEFT     = 11;
	public static final int PRES_UP       = 12;
	public static final int PRES_DOWN     = 13;
	public static final int PRES_TRIANGLE = 14;
	public static final int PRES_CIRCLE   = 15;
	public static final int PRES_X        = 16;
	public static final int PRES_SQUARE   = 17;
	public static final int PRES_L1       = 18;
	public static final int PRES_R1       = 19;
	public static final int PRES_L2       = 20;
	public static final int PRES_R2       = 21;

	// Controller Commands
	public static final short[] enterConfigMode             = new short[] {0x01,0x43,0x00,0x01,0x00};
	public static final short[] setModeAnalogLockMode       = new short[] {0x01,0x44,0x00,0x01,0x03,0x00,0x00,0x00,0x00};
	public static final short[] setAllPressureMode          = new short[] {0x01,0x4F,0x00,0xFF,0xFF,0x03,0x00,0x00,0x00};    // aka DS2_NATIVE_MODE
	public static final short[] exitConfigMode              = new short[] {0x01,0x43,0x00,0x00,0x5A,0x5A,0x5A,0x5A,0x5A};
	public static final short[] exitConfigMode2             = new short[] {0x01,0x43,0x00,0x00,0x00,0x00,0x00,0x00,0x00};    // aka CONFIG_MODE_EXIT (Lynx)
	public static final short[] exitConfigAllPressureMode   = new short[] {0x01,0x43,0x00,0x00,0x5A,0x5A,0x5A,0x5A,0x5A};    // aka CONFIG_MODE_EXIT_DS2_NATIVE
	public static final short[] typeRead                    = new short[] {0x01,0x45,0x00,0x5A,0x5A,0x5A,0x5A,0x5A,0x5A};
	public static final short[] pollMode                    = new short[] {0x01,0x42,0x00,0x00,0x00};
	public static final short[] pollAllPressurMode          = new short[] {0x01,0x42,0x00,0x00,0x00,0x00,0x00,0x00,0x00};

}
