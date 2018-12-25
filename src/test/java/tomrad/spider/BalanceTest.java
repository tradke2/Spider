package tomrad.spider;

import static org.junit.Assert.assertEquals;
import static tomrad.spider.IkRoutines.LegPosX;
import static tomrad.spider.IkRoutines.LegPosY;
import static tomrad.spider.IkRoutines.LegPosZ;
import static tomrad.spider.IkRoutines.cInitPosX;
import static tomrad.spider.IkRoutines.cInitPosY;
import static tomrad.spider.IkRoutines.cInitPosZ;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import tomrad.spider.Balance.BalanceValue;

@RunWith(MockitoJUnitRunner.class)
public class BalanceTest {

	private static final double EPSIOLON = 0.0005;

	@Mock
	private Logger logger;

	@Spy
	@InjectMocks
	private Trig trig;

	@InjectMocks
	@Spy
	private Balance testee;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	    for (int LegIndex = 0; LegIndex < 6; LegIndex++)
	    {
	        LegPosX[LegIndex] = cInitPosX[LegIndex];  // Set start positions for each leg
	        LegPosY[LegIndex] = cInitPosY[LegIndex];
	        LegPosZ[LegIndex] = cInitPosZ[LegIndex];
	    }
		IkRoutines.GaitPosX = new double[] { 0, 0, 0, 0, 0, 0 };
		IkRoutines.GaitPosY = new double[] { 0, 0, 0, 0, 0, 0 };
		IkRoutines.GaitPosZ = new double[] { 0, 0, 0, 0, 0, 0 };
		IkRoutines.GaitRotY = new double[] { 0, 0, 0, 0, 0, 0 };
		IkRoutines.BalanceMode = true;
	}

	@Test
	public void testCalcBalance_legPosZeroGaitPosZero_returnsZero() {
		IkRoutines.LegPosX = new double[] { 0, 0, 0, 0, 0, 0 };
		IkRoutines.LegPosY = new double[] { 0, 0, 0, 0, 0, 0 };
		IkRoutines.LegPosZ = new double[] { 0, 0, 0, 0, 0, 0 };
		BalanceValue result = testee.CalcBalance();
		assertEquals(0.0, result.totalTransX, EPSIOLON);
		assertEquals(-80, result.totalTransY,EPSIOLON);
		assertEquals(0.0, result.totalTransZ, EPSIOLON);
		assertEquals(0.0, result.totalXBal, EPSIOLON);
		assertEquals(-30, result.totalYBal, EPSIOLON);
		assertEquals(0.0, result.totalZBal, EPSIOLON);
	}

	@Test
	public void testCalcBalance_legPosInitialStateGaitPosZero_returnsZero() {
		BalanceValue result = testee.CalcBalance();
		assertEquals(0.0, result.totalTransX, EPSIOLON);
		assertEquals(0.0, result.totalTransY, EPSIOLON);
		assertEquals(0.0, result.totalTransZ, EPSIOLON);
		assertEquals(0.0, result.totalXBal, EPSIOLON);
		assertEquals(-30.0, result.totalYBal, EPSIOLON);
		assertEquals(0.0, result.totalZBal, EPSIOLON);
	}

	@Test
	public void testCalcBalance_legPosSet_GaitPosZero_balanceModeOn_returnsLegPos() {
		IkRoutines.LegPosX = new double[] {10, 10, 10, 10, 10, 10};
		IkRoutines.LegPosY = new double[] {20, 20, 20, 20, 20, 20};
		IkRoutines.LegPosZ = new double[] {40, 40, 40, 40, 40, 40};
		BalanceValue result = testee.CalcBalance();
		assertEquals(0.0, result.totalTransX, EPSIOLON);
		assertEquals(-60.0, result.totalTransY, EPSIOLON);
		assertEquals(40.0, result.totalTransZ, EPSIOLON);
		assertEquals(-14.3916, result.totalXBal, EPSIOLON);
		assertEquals(-30.0, result.totalYBal, EPSIOLON);
		assertEquals(0.0, result.totalZBal, EPSIOLON);
	}
}
