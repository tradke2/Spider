package tomrad.spider;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.internal.util.reflection.Whitebox;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GaitTest {

	@Mock
	private Logger logger;
	
	@InjectMocks
	@Spy
	@Autowired
	private Gait testee;

	private final int[] gait0LegNr = new int[] {4, 6, 2, 1, 3, 5};
	private final int[] gait1LegNr = new int[] {7, 11, 3, 1, 5, 9};
	private final int[] gait2LegNr = new int[] {5, 8, 2, 1, 4, 7};
	private final int[] gait3LegNr = new int[] {1, 3, 1, 3, 1, 3};
	private final int[] gait4LegNr = new int[] {1, 4, 1, 4, 1, 4};
	private final int[] gait5LegNr = new int[] {1, 5, 1, 5, 1, 5};
	private final int[] gait6LegNr = new int[] {7, 9, 11, 1, 3, 5};
	private final int[] gait7LegNr = new int[] {13, 16, 1, 4, 7, 10};

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		for (int i = 0; i < 6; i++)
		{
			IkRoutines.GaitPosX[i] = 0;
			IkRoutines.GaitPosY[i] = 0;
			IkRoutines.GaitPosZ[i] = 0;
			IkRoutines.GaitRotY[i] = 0;
		}
	}

	@Test
	public void testInitGait() {
		testee.InitGait();
		assertEquals(0, Whitebox.getInternalState(testee, "GaitType"));
		assertEquals(30.0, Whitebox.getInternalState(testee, "LegLiftHeight"));
		assertEquals(1, Whitebox.getInternalState(testee, "GaitStep"));
		Mockito.verify(testee).GaitSelect(Mockito.anyInt());
	}

	@Test
	public void testGaitSelect_invalidArgument_throwsException() {
		try 
		{
			testee.GaitSelect(255);
			fail("IllegalArgumentException erwartet");
		}
		catch (IllegalArgumentException e)
		{
			// ok
		}
	}

	@Test
	public void testGaitSelect_Gait0Selected_initsStateCorrectly() {
		testee.GaitSelect(0);
		assertEquals(0, Whitebox.getInternalState(testee, "GaitType"));
		assertArrayEquals(gait0LegNr , (int[]) Whitebox.getInternalState(testee, "GaitLegNr"));
		assertEquals(1, Whitebox.getInternalState(testee, "NrLiftedPos"));
		assertEquals(0.0, Whitebox.getInternalState(testee, "HalfLiftHeigth"));
		assertEquals(4, Whitebox.getInternalState(testee, "TLDivFactor"));
		assertEquals(6, Whitebox.getInternalState(testee, "StepsInGait"));
		assertEquals(100, Whitebox.getInternalState(testee, "NomGaitSpeed"));
	}

	@Test
	public void testGaitSelect_Gait1Selected_initsStateCorrectly() {
		testee.GaitSelect(1);
		assertEquals(1, Whitebox.getInternalState(testee, "GaitType"));
		assertArrayEquals(gait1LegNr , (int[]) Whitebox.getInternalState(testee, "GaitLegNr"));
		assertEquals(3, Whitebox.getInternalState(testee, "NrLiftedPos"));
		assertEquals(0.0, Whitebox.getInternalState(testee, "HalfLiftHeigth"));
		assertEquals(8, Whitebox.getInternalState(testee, "TLDivFactor"));
		assertEquals(12, Whitebox.getInternalState(testee, "StepsInGait"));
		assertEquals(85, Whitebox.getInternalState(testee, "NomGaitSpeed"));
	}

	@Test
	public void testGaitSelect_Gait2Selected_initsStateCorrectly() {
		testee.GaitSelect(2);
		assertEquals(2, Whitebox.getInternalState(testee, "GaitType"));
		assertArrayEquals(gait2LegNr , (int[]) Whitebox.getInternalState(testee, "GaitLegNr"));
		assertEquals(2, Whitebox.getInternalState(testee, "NrLiftedPos"));
		assertEquals(0.0, Whitebox.getInternalState(testee, "HalfLiftHeigth"));
		assertEquals(6, Whitebox.getInternalState(testee, "TLDivFactor"));
		assertEquals(9, Whitebox.getInternalState(testee, "StepsInGait"));
		assertEquals(100, Whitebox.getInternalState(testee, "NomGaitSpeed"));
	}

	@Test
	public void testGaitSelect_Gait3Selected_initsStateCorrectly() {
		testee.GaitSelect(3);
		assertEquals(3, Whitebox.getInternalState(testee, "GaitType"));
		assertArrayEquals(gait3LegNr , (int[]) Whitebox.getInternalState(testee, "GaitLegNr"));
		assertEquals(1, Whitebox.getInternalState(testee, "NrLiftedPos"));
		assertEquals(0.0, Whitebox.getInternalState(testee, "HalfLiftHeigth"));
		assertEquals(2, Whitebox.getInternalState(testee, "TLDivFactor"));
		assertEquals(4, Whitebox.getInternalState(testee, "StepsInGait"));
		assertEquals(150, Whitebox.getInternalState(testee, "NomGaitSpeed"));
	}

	@Test
	public void testGaitSelect_Gait4Selected_initsStateCorrectly() {
		testee.GaitSelect(4);
		assertEquals(4, Whitebox.getInternalState(testee, "GaitType"));
		assertArrayEquals(gait4LegNr , (int[]) Whitebox.getInternalState(testee, "GaitLegNr"));
		assertEquals(2, Whitebox.getInternalState(testee, "NrLiftedPos"));
		assertEquals(0.0, Whitebox.getInternalState(testee, "HalfLiftHeigth"));
		assertEquals(4, Whitebox.getInternalState(testee, "TLDivFactor"));
		assertEquals(6, Whitebox.getInternalState(testee, "StepsInGait"));
		assertEquals(100, Whitebox.getInternalState(testee, "NomGaitSpeed"));
	}

	@Test
	public void testGaitSelect_Gait5Selected_initsStateCorrectly() {
		testee.GaitSelect(5);
		assertEquals(5, Whitebox.getInternalState(testee, "GaitType"));
		assertArrayEquals(gait5LegNr , (int[]) Whitebox.getInternalState(testee, "GaitLegNr"));
		assertEquals(3, Whitebox.getInternalState(testee, "NrLiftedPos"));
		assertEquals(1.0, Whitebox.getInternalState(testee, "HalfLiftHeigth"));
		assertEquals(4, Whitebox.getInternalState(testee, "TLDivFactor"));
		assertEquals(8, Whitebox.getInternalState(testee, "StepsInGait"));
		assertEquals(85, Whitebox.getInternalState(testee, "NomGaitSpeed"));
	}

	@Test
	public void testGaitSelect_Gait6Selected_initsStateCorrectly() {
		testee.GaitSelect(6);
		assertEquals(6, Whitebox.getInternalState(testee, "GaitType"));
		assertArrayEquals(gait6LegNr , (int[]) Whitebox.getInternalState(testee, "GaitLegNr"));
		assertEquals(1, Whitebox.getInternalState(testee, "NrLiftedPos"));
		assertEquals(0.0, Whitebox.getInternalState(testee, "HalfLiftHeigth"));
		assertEquals(10, Whitebox.getInternalState(testee, "TLDivFactor"));
		assertEquals(12, Whitebox.getInternalState(testee, "StepsInGait"));
		assertEquals(85, Whitebox.getInternalState(testee, "NomGaitSpeed"));
	}

	@Test
	public void testGaitSelect_Gait7Selected_initsStateCorrectly() {
		testee.GaitSelect(7);
		assertEquals(7, Whitebox.getInternalState(testee, "GaitType"));
		assertArrayEquals(gait7LegNr , (int[]) Whitebox.getInternalState(testee, "GaitLegNr"));
		assertEquals(2, Whitebox.getInternalState(testee, "NrLiftedPos"));
		assertEquals(0.0, Whitebox.getInternalState(testee, "HalfLiftHeigth"));
		assertEquals(16, Whitebox.getInternalState(testee, "TLDivFactor"));
		assertEquals(18, Whitebox.getInternalState(testee, "StepsInGait"));
		assertEquals(85, Whitebox.getInternalState(testee, "NomGaitSpeed"));
	}

	@Test
	public void testGaitSeq_noMotionInput_returnsNull() {
		for (int i = 0; i < 8; i++)
		{
			testee.GaitSelect(i);
			TravelLength result = testee.GaitSeq(new TravelLength(0, 0, 0));
			assertEquals(0.0, result.lengthX, 0.0);
			assertEquals(0.0, result.lengthZ, 0.0);
			assertEquals(0.0, result.rotationY, 0.0);
		}
	}

	@Test
	public void testGaitSeq_motionInputTooSmall_returnsNull() {
		for (int i = 0; i < 8; i++)
		{
			for (int lengthX : new int[] { -4, 4})
			{
				for (int lengthZ : new int[] { -4, 4})
				{
					for (int rotY : new int[] { -4, 4})
					{
						testee.GaitSelect(i);
						TravelLength result = testee.GaitSeq(new TravelLength(lengthX, lengthZ, rotY));
						assertEquals(0.0, result.lengthX, 0.0);
						assertEquals(0.0, result.lengthZ, 0.0);
						assertEquals(0.0, result.rotationY, 0.0);
					}
				}
			}
		}
	}

	@Test
	public void testGaitSeq_motionInputAboveThreshold_returnsNonNull() {
		for (int i = 0; i < 8; i++)
		{
			for (int input : new int[] { -5, 5})
			{
				testee.GaitSelect(i);
				TravelLength result = testee.GaitSeq(new TravelLength(input, 0, 0));
				assertNotEquals(0.0, result.lengthX, 0.0);
				assertEquals(0.0, result.lengthZ, 0.0);
				assertEquals(0.0, result.rotationY, 0.0);
				testee.GaitSelect(i);
				result = testee.GaitSeq(new TravelLength(0, input, 0));
				assertEquals(0.0, result.lengthX, 0.0);
				assertNotEquals(0.0, result.lengthZ, 0.0);
				assertEquals(0.0, result.rotationY, 0.0);
				testee.GaitSelect(i);
				result = testee.GaitSeq(new TravelLength(0, 0, input));
				assertEquals(0.0, result.lengthX, 0.0);
				assertEquals(0.0, result.lengthZ, 0.0);
				assertNotEquals(0.0, result.rotationY, 0.0);
			}
		}
	}

	@Test
	public void testGaitSeq_Gait0_fullTravelLength()
	{
		TravelLength expectedResult = new TravelLength(100, 100, 100);
		double[][] expectedPosY = {
				//RR    RM   RF   LR   LM,   LF
				{  0,    0,   0, -30,   0,    0}, // Step 1	
				{  0,    0, -30,   0,   0,    0}, // Step 2
				{  0,    0,   0,   0, -30,    0}, // Step 3
				{-30,    0,   0,   0,   0,    0}, // Step 4
				{  0,    0,   0,   0,   0,  -30}, // Step 5
				{  0,  -30,   0,   0,   0,    0}, // Step 6
				{  0,    0,   0, -30,   0,    0}, // Step 7 == 1
				{  0,    0, -30,   0,   0,    0}, // Step 8 == 2
				{  0,    0,   0,   0, -30,    0}, // Step 9 == 3
				{-30,    0,   0,   0,   0,    0}, // Step 10 == 4
				{  0,    0,   0,   0,   0,  -30}, // Step 11 == 5
				{  0,  -30,   0,   0,   0,    0}, // Step 12 == 6
				{  0,    0,   0, -30,   0,    0}, // Step 13 == 1
				{  0,    0, -30,   0,   0,    0}, // Step 14 == 2
				{  0,    0,   0,   0, -30,    0}, // Step 15 == 3
				{-30,    0,   0,   0,   0,    0}, // Step 16 == 4
				{  0,    0,   0,   0,   0,  -30}, // Step 17 == 5
				{  0,  -30,   0,   0,   0,    0}, // Step 18 == 6
		};
		double[][] expectedPosX = {
				//RR    RM   RF   LR   LM,   LF
				{-25,  -25, -25,   0, -25,  -25}, // Step 1
				{-50,  -50,   0,  50, -50,  -50}, // Step 2
				{-75,  -75,  50,  25,   0,  -75}, // Step 3
				{  0, -100,  25,   0,  50, -100}, // Step 4
				{ 50, -125,   0, -25,  25,    0}, // Step 5
				{ 25,    0, -25, -50,   0,   50}, // Step 6
				{  0,   50, -50,   0, -25,   25}, // Step 7 == 1
				{-25,   25,   0,  50, -50,    0}, // Step 8 == 2
				{-50,    0,  50,  25,   0,  -25}, // Step 9 == 3
				{  0,  -25,  25,   0,  50,  -50}, // Step 10 == 4
				{ 50,  -50,   0, -25,  25,    0}, // Step 11 == 5
				{ 25,   0,  -25, -50,   0,   50}, // Step 12 == 6
				{  0,   50, -50,   0, -25,   25}, // Step 13 == 1
				{-25,   25,   0,  50, -50,    0}, // Step 14 == 2
				{-50,    0,  50,  25,   0,  -25}, // Step 15 == 3
				{  0,  -25,  25,   0,  50,  -50}, // Step 16 == 4
				{ 50,  -50,   0, -25,  25,    0}, // Step 17 == 5
				{ 25,    0, -25, -50,   0,   50}, // Step 18 == 6
		};
		testee.GaitSelect(0);
		for (int step = 1; step <= 18; step ++)
		{
			String msg = "Step " + step;
			TravelLength result = testee.GaitSeq(new TravelLength(100, 100, 100));
			assertEquals(msg, expectedResult, result);
			assertArrayEquals(msg, expectedPosX[step-1], IkRoutines.GaitPosX, 0.0); 
			assertArrayEquals(msg, expectedPosY[step-1], IkRoutines.GaitPosY, 0.0); 			
			assertArrayEquals(msg, expectedPosX[step-1], IkRoutines.GaitPosZ, 0.0); 
			assertArrayEquals(msg, expectedPosX[step-1], IkRoutines.GaitRotY, 0.0); 
		}		
	}

	@Test
	public void testGaitSeq_Gait0_xTravelLength()
	{
		TravelLength expectedResult = new TravelLength(100, 0, 0);
		double[][] expectedPosY = {
				//RR    RM   RF   LR   LM,   LF
				{  0,    0,   0, -30,   0,    0}, // Step 1	
				{  0,    0, -30,   0,   0,    0}, // Step 2
				{  0,    0,   0,   0, -30,    0}, // Step 3
				{-30,    0,   0,   0,   0,    0}, // Step 4
				{  0,    0,   0,   0,   0,  -30}, // Step 5
				{  0,  -30,   0,   0,   0,    0}, // Step 6
				{  0,    0,   0, -30,   0,    0}, // Step 7 == 1
				{  0,    0, -30,   0,   0,    0}, // Step 8 == 2
				{  0,    0,   0,   0, -30,    0}, // Step 9 == 3
				{-30,    0,   0,   0,   0,    0}, // Step 10 == 4
				{  0,    0,   0,   0,   0,  -30}, // Step 11 == 5
				{  0,  -30,   0,   0,   0,    0}, // Step 12 == 6
				{  0,    0,   0, -30,   0,    0}, // Step 13 == 1
				{  0,    0, -30,   0,   0,    0}, // Step 14 == 2
				{  0,    0,   0,   0, -30,    0}, // Step 15 == 3
				{-30,    0,   0,   0,   0,    0}, // Step 16 == 4
				{  0,    0,   0,   0,   0,  -30}, // Step 17 == 5
				{  0,  -30,   0,   0,   0,    0}, // Step 18 == 6
		};
		double[][] expectedPosX = {
				//RR    RM   RF   LR   LM,   LF
				{-25,  -25, -25,   0, -25,  -25}, // Step 1
				{-50,  -50,   0,  50, -50,  -50}, // Step 2
				{-75,  -75,  50,  25,   0,  -75}, // Step 3
				{  0, -100,  25,   0,  50, -100}, // Step 4
				{ 50, -125,   0, -25,  25,    0}, // Step 5
				{ 25,    0, -25, -50,   0,   50}, // Step 6
				{  0,   50, -50,   0, -25,   25}, // Step 7 == 1
				{-25,   25,   0,  50, -50,    0}, // Step 8 == 2
				{-50,    0,  50,  25,   0,  -25}, // Step 9 == 3
				{  0,  -25,  25,   0,  50,  -50}, // Step 10 == 4
				{ 50,  -50,   0, -25,  25,    0}, // Step 11 == 5
				{ 25,   0,  -25, -50,   0,   50}, // Step 12 == 6
				{  0,   50, -50,   0, -25,   25}, // Step 13 == 1
				{-25,   25,   0,  50, -50,    0}, // Step 14 == 2
				{-50,    0,  50,  25,   0,  -25}, // Step 15 == 3
				{  0,  -25,  25,   0,  50,  -50}, // Step 16 == 4
				{ 50,  -50,   0, -25,  25,    0}, // Step 17 == 5
				{ 25,    0, -25, -50,   0,   50}, // Step 18 == 6
		};
		testee.GaitSelect(0);
		for (int step = 1; step <= 18; step ++)
		{
			String msg = "Step " + step;
			TravelLength result = testee.GaitSeq(new TravelLength(100, 0, 0));
			assertEquals(msg, expectedResult, result);
			assertArrayEquals(msg, expectedPosX[step-1], IkRoutines.GaitPosX, 0.0); 
			assertArrayEquals(msg, expectedPosY[step-1], IkRoutines.GaitPosY, 0.0); 			
			assertArrayEquals(new double[] {  0,    0,   0,   0,   0,    0}, IkRoutines.GaitPosZ, 0.0); 
			assertArrayEquals(new double[] {  0,    0,   0,   0,   0,    0}, IkRoutines.GaitRotY, 0.0); 
		}		
	}

	@Test
	public void testGaitSeq_Gait1_fullTravelLength()
	{
		TravelLength expectedResult = new TravelLength(80, 80, 80);
		double[][] expectedPosY = {
				//RR    RM   RF   LR   LM,   LF
				{  0,    0,   0, -30,   0,    0}, // Step 1	
				{  0,    0, -30, -30,   0,    0}, // Step 2
				{  0,    0, -30,   0,   0,    0}, // Step 3
				{  0,    0, -30,   0, -30,    0}, // Step 4
				{  0,    0,   0,   0, -30,    0}, // Step 5
				{-30,    0,   0,   0, -30,    0}, // Step 6
				{-30,    0,   0,   0,   0,    0}, // Step 7
				{-30,    0,   0,   0,   0,  -30}, // Step 8
				{  0,    0,   0,   0,   0,  -30}, // Step 9
				{  0,  -30,   0,   0,   0,  -30}, // Step 10	
				{  0,  -30,   0,   0,   0,    0}, // Step 11
				{  0,  -30,   0, -30,   0,    0}, // Step 12
				{  0,    0,   0, -30,   0,    0}, // Step 13 == 1
				{  0,    0, -30, -30,   0,    0}, // Step 14 == 2
				{  0,    0, -30,   0,   0,    0}, // Step 15 == 3
				{  0,    0, -30,   0, -30,    0}, // Step 16 == 4
				{  0,    0,   0,   0, -30,    0}, // Step 17 == 5
				{-30,    0,   0,   0, -30,    0}, // Step 18 == 6
		};
		double[][] expectedPosX = {
				//RR    RM   RF   LR   LM,   LF
				{-10,  -10, -10,   0, -10,  -10}, // Step 1
				{-20,  -20, -40,  40, -20,  -20}, // Step 2
				{-30,  -30,   0,  30, -30,  -30}, // Step 3
				{-40,  -40,  40,  20, -40,  -40}, // Step 4
				{-50,  -50,  30,  10,   0,  -50}, // Step 5
				{-40,  -60,  20,   0,  40,  -60}, // Step 6
				{  0,  -70,  10, -10,  30,  -70}, // Step 7
				{ 40,  -80,   0, -20,  20,  -40}, // Step 8
				{ 30,  -90, -10, -30,  10,    0}, // Step 9
				{ 20,  -40, -20, -40,   0,   40}, // Step 10
				{ 10,    0, -30, -50, -10,   30}, // Step 11
				{  0,   40, -40, -40, -20,   20}, // Step 12
				{-10,   30, -50,   0, -30,   10}, // Step 13 == 1
				{-20,   20, -40,  40, -40,    0}, // Step 14 == 2
				{-30,   10,   0,  30, -50,  -10}, // Step 15 == 3
				{-40,    0,  40,  20, -40,  -20}, // Step 16 == 4
				{-50,  -10,  30,  10,   0,  -30}, // Step 17 == 5
				{-40,  -20,  20,   0,  40,  -40}, // Step 18 == 6
		};
		
		testee.GaitSelect(1);

		for (int step = 1; step <= 18; step ++)
		{
			String msg = "Step " + step;
			TravelLength result = testee.GaitSeq(new TravelLength(80, 80, 80));
			assertEquals(msg, expectedResult, result);
			assertArrayEquals(msg, expectedPosX[step-1], IkRoutines.GaitPosX, 0.0); 
			assertArrayEquals(msg, expectedPosY[step-1], IkRoutines.GaitPosY, 0.0); 			
		}		
	}

	@Test
	public void testGaitSeq_Gait2_fullTravelLength()
	{
		TravelLength expectedResult = new TravelLength(60, 60, 60);
		double[][] expectedPosY = {
				//RR    RM   RF   LR   LM,   LF
				{  0,    0,   0, -30,   0,    0}, // Step 1	
				{  0,    0, -30, -30,   0,    0}, // Step 2
				{  0,    0, -30,   0,   0,    0}, // Step 3
				{  0,    0,   0,   0, -30,    0}, // Step 4
				{-30,    0,   0,   0, -30,    0}, // Step 5
				{-30,    0,   0,   0,   0,    0}, // Step 6
				{  0,    0,   0,   0,   0,  -30}, // Step 7
				{  0,  -30,   0,   0,   0,  -30}, // Step 8
				{  0,  -30,   0,   0,   0,    0}, // Step 9
				{  0,    0,   0, -30,   0,    0}, // Step 10 == 1	
				{  0,    0, -30, -30,   0,    0}, // Step 11 == 2
				{  0,    0, -30,   0,   0,    0}, // Step 12 == 3
				{  0,    0,   0,   0, -30,    0}, // Step 13 == 4
				{-30,    0,   0,   0, -30,    0}, // Step 14 == 5
				{-30,    0,   0,   0,   0,    0}, // Step 15 == 6
				{  0,    0,   0,   0,   0,  -30}, // Step 16 == 7
				{  0,  -30,   0,   0,   0,  -30}, // Step 17 == 8
				{  0,  -30,   0,   0,   0,    0}, // Step 18 == 9

		};
		
		double[][] expectedPosX = {
				//RR    RM   RF   LR   LM,   LF
				{-10,  -10, -10, -30, -10,  -10}, // Step 1
				{-20,  -20, -30,  30, -20,  -20}, // Step 2
				{-30,  -30,  30,  30, -30,  -30}, // Step 3
				{-40,  -40,  30,  20, -30,  -40}, // Step 4
				{-30,  -50,  20,  10,  30,  -50}, // Step 5
				{ 30,  -60,  10,   0,  30,  -60}, // Step 6
				{ 30,  -70,   0, -10,  20,  -30}, // Step 7
				{ 20,  -30, -10, -20,  10,   30}, // Step 8
				{ 10,   30, -20, -30,   0,   30}, // Step 9
				{  0,   30, -30, -30, -10,   20}, // Step 10 == 1
				{-10,   20, -30,  30, -20,   10}, // Step 11 == 2
				{-20,   10,  30,  30, -30,    0}, // Step 12 == 3
				{-30,    0,  30,  20, -30,  -10}, // Step 13 == 4
				{-30,  -10,  20,  10,  30,  -20}, // Step 14 == 5
				{ 30,  -20,  10,   0,  30,  -30}, // Step 15 == 6
				{ 30,  -30,   0, -10,  20,  -30}, // Step 16 == 7
				{ 20,  -30, -10, -20,  10,   30}, // Step 17 == 8
				{ 10,   30, -20, -30,   0,   30}, // Step 18 == 9
		};
		
		testee.GaitSelect(2);
		
		for (int step = 1; step <= 18; step ++)
		{
			String msg = "Step " + step;
			TravelLength result = testee.GaitSeq(new TravelLength(60, 60, 60));
			assertEquals(msg, expectedResult, result);
			assertArrayEquals(msg, expectedPosX[step-1], IkRoutines.GaitPosX, 0.0); 
			assertArrayEquals(msg, expectedPosY[step-1], IkRoutines.GaitPosY, 0.0); 			
		}
	}
	
	@Test
	public void testGaitSeq_Gait3_fullTravelLength()
	{
		TravelLength expectedResult = new TravelLength(80, 80, 80);
		double[][] expectedPosY = {
				//RR    RM   RF   LR   LM,   LF
				{-30,    0, -30,   0, -30,    0}, // Step 1	
				{  0,    0,   0,   0,   0,    0}, // Step 2
				{  0,  -30,   0, -30,   0,  -30}, // Step 3
				{  0,    0,   0,   0,   0,    0}, // Step 4
				{-30,    0, -30,   0, -30,    0}, // Step 5 == 1
				{  0,    0,   0,   0,   0,    0}, // Step 6 == 2
				{  0,  -30,   0, -30,   0,  -30}, // Step 7 == 3
				{  0,    0,   0,   0,   0,    0}, // Step 8 == 4
				{-30,    0, -30,   0, -30,    0}, // Step 9 == 1
				{  0,    0,   0,   0,   0,    0}, // Step 10 == 2	
				{  0,  -30,   0, -30,   0,  -30}, // Step 11 == 3
				{  0,    0,   0,   0,   0,    0}, // Step 12 == 4
				{-30,    0, -30,   0, -30,    0}, // Step 13 == 1
				{  0,    0,   0,   0,   0,    0}, // Step 14 == 2
				{  0,  -30,   0, -30,   0,  -30}, // Step 15 == 3
				{  0,    0,   0,   0,   0,    0}, // Step 16 == 4
				{-30,    0, -30,   0, -30,    0}, // Step 17 == 1
				{  0,    0,   0,   0,   0,    0}, // Step 18 == 2

		};
		double[][] expectedPosX = {
				//RR    RM   RF   LR   LM,   LF
				{  0,  -40,   0, -40,   0,  -40}, // Step 1
				{ 40,  -80,  40, -80,  40,  -80}, // Step 2
				{  0,    0,   0,   0,   0,    0}, // Step 3
				{-40,   40, -40,  40, -40,   40}, // Step 4
				{  0,    0,   0,   0,   0,    0}, // Step 5 == 1
				{ 40,  -40,  40, -40,  40,  -40}, // Step 6 == 2
				{  0,    0,   0,   0,   0,    0}, // Step 7 == 3
				{-40,   40, -40,  40, -40,   40}, // Step 8 == 4
				{  0,    0,   0,   0,   0,    0}, // Step 9 == 1
				{ 40,  -40,  40, -40,  40,  -40}, // Step 10 == 2
				{  0,    0,   0,   0,   0,    0}, // Step 11 == 3
				{-40,   40, -40,  40, -40,   40}, // Step 12 == 4
				{  0,    0,   0,   0,   0,    0}, // Step 13 == 1
				{ 40,  -40,  40, -40,  40,  -40}, // Step 14 == 2
				{  0,    0,   0,   0,   0,    0}, // Step 15 == 3
				{-40,   40, -40,  40, -40,   40}, // Step 16 == 4
				{  0,    0,   0,   0,   0,    0}, // Step 17 == 1
				{ 40,  -40,  40, -40,  40,  -40}, // Step 18 == 2
		};
		
		testee.GaitSelect(3);
		
		for (int step = 1; step <= 18; step ++)
		{
			String msg = "Step " + step;
			TravelLength result = testee.GaitSeq(new TravelLength(80, 80, 80));
			assertEquals(msg, expectedResult, result);
			assertArrayEquals(msg, expectedPosX[step-1], IkRoutines.GaitPosX, 0.0); 
			assertArrayEquals(msg, expectedPosY[step-1], IkRoutines.GaitPosY, 0.0); 			
		}
	}

	@Test
	public void testGaitSeq_Gait4_fullTravelLength()
	{
		TravelLength expectedResult = new TravelLength(80, 80, 80);
		double[][] expectedPosY = {
				//RR    RM   RF   LR   LM,   LF
				{-30,    0, -30,   0, -30,    0}, // Step 1	
				{-30,    0, -30,   0, -30,    0}, // Step 2
				{  0,    0,   0,   0,   0,    0}, // Step 3
				{  0,  -30,   0, -30,   0,  -30}, // Step 4
				{  0,  -30,   0, -30,   0,  -30}, // Step 5
				{  0,    0,   0,   0,   0,    0}, // Step 6
				{-30,    0, -30,   0, -30,    0}, // Step 7 == 1
				{-30,    0, -30,   0, -30,    0}, // Step 8 == 2
				{  0,    0,   0,   0,   0,    0}, // Step 9 == 3
				{  0,  -30,   0, -30,   0,  -30}, // Step 10 == 4	
				{  0,  -30,   0, -30,   0,  -30}, // Step 11 == 5
				{  0,    0,   0,   0,   0,    0}, // Step 12 == 6
				{-30,    0, -30,   0, -30,    0}, // Step 13 == 1
				{-30,    0, -30,   0, -30,    0}, // Step 14 == 2
				{  0,    0,   0,   0,   0,    0}, // Step 15 == 3
				{  0,  -30,   0, -30,   0,  -30}, // Step 16 == 4
				{  0,  -30,   0, -30,   0,  -30}, // Step 17 == 5
				{  0,    0,   0,   0,   0,    0}, // Step 18 == 6

		};
		double[][] expectedPosX = {
				//RR    RM   RF   LR   LM,   LF
				{ -40,  -20, -40, -20, -40,  -20}, // Step 1
				{  40,  -40,  40, -40,  40,  -40}, // Step 2
				{  40,  -60,  40, -60,  40,  -60}, // Step 3
				{  20,  -40,  20, -40,  20,  -40}, // Step 4
				{   0,   40,   0,  40,   0,   40}, // Step 5
				{ -20,   40, -20,  40, -20,   40}, // Step 6
				{ -40,   20, -40,  20, -40,   20}, // Step 7 == 1
				{  40,    0,  40,   0,  40,    0}, // Step 8 == 2
				{  40,  -20,  40, -20,  40,  -20}, // Step 9 == 3
				{  20,  -40,  20, -40,  20,  -40}, // Step 10 == 4
				{   0,   40,   0,  40,   0,   40}, // Step 11 == 5
				{ -20,   40, -20,  40, -20,   40}, // Step 12 == 6
				{ -40,   20, -40,  20, -40,   20}, // Step 13 == 1
				{  40,    0,  40,   0,  40,    0}, // Step 14 == 2
				{  40,  -20,  40, -20,  40,  -20}, // Step 15 == 3
				{  20,  -40,  20, -40,  20,  -40}, // Step 16 == 4
				{   0,   40,   0,  40,   0,   40}, // Step 17 == 5
				{ -20,   40, -20,  40, -20,   40}, // Step 18 == 6
		};
		
		testee.GaitSelect(4);
		
		for (int step = 1; step <= 18; step ++)
		{
			String msg = "Step " + step;
			TravelLength result = testee.GaitSeq(new TravelLength(80, 80, 80));
			assertEquals(msg, expectedResult, result);
			assertArrayEquals(msg, expectedPosX[step-1], IkRoutines.GaitPosX, 0.0); 
			assertArrayEquals(msg, expectedPosY[step-1], IkRoutines.GaitPosY, 0.0); 			
		}
	}

	@Test
	public void testGaitSeq_Gait5_fullTravelLength()
	{
		TravelLength expectedResult = new TravelLength(80, 80, 80);
		double[][] expectedPosY = {
				//RR    RM   RF   LR   LM,   LF
				{-30,    0, -30,   0, -30,    0}, // Step 1	
				{-15,    0, -15,   0, -15,    0}, // Step 2
				{  0,    0,   0,   0,   0,    0}, // Step 3
				{  0,  -15,   0, -15,   0,  -15}, // Step 4
				{  0,  -30,   0, -30,   0,  -30}, // Step 5
				{  0,  -15,   0, -15,   0,  -15}, // Step 6
				{  0,    0,   0,   0,   0,    0}, // Step 7
				{-15,    0, -15,   0, -15,    0}, // Step 8
				{-30,    0, -30,   0, -30,    0}, // Step 9 == 1
				{-15,    0, -15,   0, -15,    0}, // Step 10 == 2	
				{  0,    0,   0,   0,   0,    0}, // Step 11 == 3
				{  0,  -15,   0, -15,   0,  -15}, // Step 12 == 4
				{  0,  -30,   0, -30,   0,  -30}, // Step 13 == 5
				{  0,  -15,   0, -15,   0,  -15}, // Step 14 == 6
				{  0,    0,   0,   0,   0,    0}, // Step 15 == 7
				{-15,    0, -15,   0, -15,    0}, // Step 16 == 8
				{-30,    0, -30,   0, -30,    0}, // Step 17 == 1
				{-15,    0, -15,   0, -15,    0}, // Step 18 == 2

		};
		double[][] expectedPosX = {
				//RR    RM   RF   LR   LM,   LF
				{   0,  -20,   0, -20,   0,  -20}, // Step 1
				{  40,  -40,  40, -40,  40,  -40}, // Step 2
				{  20,  -60,  20, -60,  20,  -60}, // Step 3
				{   0,  -40,   0, -40,   0,  -40}, // Step 4
				{ -20,    0, -20,   0, -20,    0}, // Step 5
				{ -40,   40, -40,  40, -40,   40}, // Step 6
				{ -60,   20, -60,  20, -60,   20}, // Step 7
				{ -40,    0, -40,   0, -40,    0}, // Step 8
				{   0,  -20,   0, -20,   0,  -20}, // Step 9 == 1
				{  40,  -40,  40, -40,  40,  -40}, // Step 10 == 2
				{  20,  -60,  20, -60,  20,  -60}, // Step 11 == 3
				{   0,  -40,   0, -40,   0,  -40}, // Step 12 == 4
				{ -20,    0, -20,   0, -20,    0}, // Step 13 == 5
				{ -40,   40, -40,  40, -40,   40}, // Step 14 == 6
				{ -60,   20, -60,  20, -60,   20}, // Step 15 == 7
				{ -40,    0, -40,   0, -40,    0}, // Step 16 == 8
				{   0,  -20,   0, -20,   0,  -20}, // Step 17 == 1
				{  40,  -40,  40, -40,  40,  -40}, // Step 18 == 2
		};
		
		testee.GaitSelect(5);
		
		for (int step = 1; step <= 18; step ++)
		{
			String msg = "Step " + step;
			TravelLength result = testee.GaitSeq(new TravelLength(80, 80, 80));
			assertEquals(msg, expectedResult, result);
			assertArrayEquals(msg, expectedPosX[step-1], IkRoutines.GaitPosX, 0.0); 
			assertArrayEquals(msg, expectedPosY[step-1], IkRoutines.GaitPosY, 0.0); 			
		}
	}
	
	@Test
	public void testGaitSeq_Gait6_fullTravelLength()
	{
		TravelLength expectedResult = new TravelLength(80, 80, 80);
		double[][] expectedPosY = {
				//RR    RM   RF   LR   LM,   LF
				{  0,    0,   0, -30,   0,    0}, // Step 1	
				{  0,    0,   0,   0,   0,    0}, // Step 2
				{  0,    0,   0,   0, -30,    0}, // Step 3
				{  0,    0,   0,   0,   0,    0}, // Step 4
				{  0,    0,   0,   0,   0,  -30}, // Step 5
				{  0,    0,   0,   0,   0,    0}, // Step 6
				{-30,    0,   0,   0,   0,    0}, // Step 7
				{  0,    0,   0,   0,   0,    0}, // Step 8
				{  0,  -30,   0,   0,   0,    0}, // Step 9
				{  0,    0,   0,   0,   0,    0}, // Step 10	
				{  0,    0, -30,   0,   0,    0}, // Step 11
				{  0,    0,   0,   0,   0,    0}, // Step 12
				{  0,    0,   0, -30,   0,    0}, // Step 13 == 1
				{  0,    0,   0,   0,   0,    0}, // Step 14 == 2
				{  0,    0,   0,   0, -30,    0}, // Step 15 == 3
				{  0,    0,   0,   0,   0,    0}, // Step 16 == 4
				{  0,    0,   0,   0,   0,  -30}, // Step 17 == 5
				{  0,    0,   0,   0,   0,    0}, // Step 18 == 6

		};
		double[][] expectedPosX = {
				// RR    RM   RF   LR   LM,   LF
				{  -8,   -8,  -8,   0,  -8,   -8}, // Step 1
				{ -16,  -16, -16,  40, -16,  -16}, // Step 2
				{ -24,  -24, -24,  32,   0,  -24}, // Step 3
				{ -32,  -32, -32,  24,  40,  -32}, // Step 4
				{ -40,  -40, -40,  16,  32,    0}, // Step 5
				{ -48,  -48, -48,   8,  24,   40}, // Step 6
				{   0,  -56, -56,   0,  16,   32}, // Step 7
				{  40,  -64, -64,  -8,   8,   24}, // Step 8
				{  32,    0, -72, -16,   0,   16}, // Step 9
				{  24,   40, -80, -24,  -8,    8}, // Step 10
				{  16,   32,   0, -32, -16,    0}, // Step 11
				{   8,   24,  40, -40, -24,   -8}, // Step 12
				{   0,   16,  32,   0, -32,  -16}, // Step 13 == 1
				{  -8,    8,  24,  40, -40,  -24}, // Step 14 == 2
				{ -16,    0,  16,  32,   0,  -32}, // Step 15 == 3
				{ -24,   -8,   8,  24,  40,  -40}, // Step 16 == 4
				{ -32,  -16,   0,  16,  32,    0}, // Step 17 == 5
				{ -40,  -24,  -8,   8,  24,   40}, // Step 18 == 6
		};
		
		testee.GaitSelect(6);
		
		for (int step = 1; step <= 18; step ++)
		{
			String msg = "Step " + step;
			TravelLength result = testee.GaitSeq(new TravelLength(80, 80, 80));
			assertEquals(msg, expectedResult, result);
			assertArrayEquals(msg, expectedPosY[step-1], IkRoutines.GaitPosY, 0.0); 			
			assertArrayEquals(msg, expectedPosX[step-1], IkRoutines.GaitPosX, 0.0); 
		}
	}
	
	@Test
	public void testGaitSeq_Gait7_fullTravelLength()
	{
		TravelLength expectedResult = new TravelLength(80, 80, 80);
		double[][] expectedPosY = {
				//RR    RM   RF   LR   LM,   LF
				{  0,    0, -30,   0,   0,    0}, // Step 1	
				{  0,    0, -30,   0,   0,    0}, // Step 2
				{  0,    0,   0,   0,   0,    0}, // Step 3
				{  0,    0,   0, -30,   0,    0}, // Step 4
				{  0,    0,   0, -30,   0,    0}, // Step 5
				{  0,    0,   0,   0,   0,    0}, // Step 6
				{  0,    0,   0,   0, -30,    0}, // Step 7
				{  0,    0,   0,   0, -30,    0}, // Step 8
				{  0,    0,   0,   0,   0,    0}, // Step 9
				{  0,    0,   0,   0,   0,  -30}, // Step 10	
				{  0,    0,   0,   0,   0,  -30}, // Step 11
				{  0,    0,   0,   0,   0,    0}, // Step 12
				{-30,    0,   0,   0,   0,    0}, // Step 13
				{-30,    0,   0,   0,   0,    0}, // Step 14
				{  0,    0,   0,   0,   0,    0}, // Step 15
				{  0,  -30,   0,   0,   0,    0}, // Step 16
				{  0,  -30,   0,   0,   0,    0}, // Step 17
				{  0,    0,   0,   0,   0,    0}, // Step 18

		};
		double[][] expectedPosX = {
				// RR    RM   RF   LR   LM,   LF
				{  -5,   -5, -40,  -5,  -5,   -5}, // Step 1
				{ -10,  -10,  40, -10, -10,  -10}, // Step 2
				{ -15,  -15,  40, -15, -15,  -15}, // Step 3
				{ -20,  -20,  35, -40, -20,  -20}, // Step 4
				{ -25,  -25,  30,  40, -25,  -25}, // Step 5
				{ -30,  -30,  25,  40, -30,  -30}, // Step 6
				{ -35,  -35,  20,  35, -40,  -35}, // Step 7
				{ -40,  -40,  15,  30,  40,  -40}, // Step 8
				{ -45,  -45,  10,  25,  40,  -45}, // Step 9
				{ -50,  -50,   5,  20,  35,  -40}, // Step 10
				{ -55,  -55,   0,  15,  30,   40}, // Step 11
				{ -60,  -60,  -5,  10,  25,   40}, // Step 12
				{ -40,  -65, -10,   5,  20,   35}, // Step 13
				{  40,  -70, -15,   0,  15,   30}, // Step 14
				{  40,  -75, -20,  -5,  10,   25}, // Step 15
				{  35,  -40, -25, -10,   5,   20}, // Step 16
				{  30,   40, -30, -15,   0,   15}, // Step 17
				{  25,   40, -35, -20,  -5,   10}, // Step 18
		};
		
		testee.GaitSelect(7);
		
		for (int step = 1; step <= 18; step ++)
		{
			String msg = "Step " + step;
			TravelLength result = testee.GaitSeq(new TravelLength(80, 80, 80));
			assertEquals(msg, expectedResult, result);
			assertArrayEquals(msg, expectedPosY[step-1], IkRoutines.GaitPosY, 0.0); 			
			assertArrayEquals(msg, expectedPosX[step-1], IkRoutines.GaitPosX, 0.0); 
		}
	}
	
}
