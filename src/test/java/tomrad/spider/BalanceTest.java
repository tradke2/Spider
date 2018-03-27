package tomrad.spider;

import static org.junit.Assert.assertEquals;
import static tomrad.spider.IkRoutines.LegPosX;
import static tomrad.spider.IkRoutines.LegPosY;
import static tomrad.spider.IkRoutines.LegPosZ;
import static tomrad.spider.IkRoutines.cInitPosX;
import static tomrad.spider.IkRoutines.cInitPosY;
import static tomrad.spider.IkRoutines.cInitPosZ;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import tomrad.spider.Balance.BalanceValue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BalanceTest {

	@InjectMocks
	@Spy
	@Autowired
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
	@Ignore
	public void testCalcBalance_legPosZeroGaitPosZero_returnsZero() {
		IkRoutines.LegPosX = new double[] { 0, 0, 0, 0, 0, 0 };
		IkRoutines.LegPosY = new double[] { 0, 0, 0, 0, 0, 0 };
		IkRoutines.LegPosZ = new double[] { 0, 0, 0, 0, 0, 0 };
		BalanceValue result = testee.CalcBalance();
		assertEquals(0.0, result.totalTransX, 0);
		assertEquals(-80, result.totalTransY, 0);
		assertEquals(0.0, result.totalTransZ, 0);
		assertEquals(-90, result.totalXBal, 0);
		assertEquals(0.0, result.totalYBal, 0);
		assertEquals(90, result.totalZBal, 0);
	}

	@Test
	@Ignore
	public void testCalcBalance_legPosInitialStateGaitPosZero_returnsZero() {
		BalanceValue result = testee.CalcBalance();
		assertEquals(0.0, result.totalTransX, 0);
		assertEquals(0.0, result.totalTransY, 0);
		assertEquals(0.0, result.totalTransZ, 0);
		assertEquals(-90.0, result.totalXBal, 0);
		assertEquals(0.0, result.totalYBal, 0.001);
		assertEquals(90.0, result.totalZBal, 0);
	}

	@Test
	@Ignore
	public void testCalcBalance_legPosSet_GaitPosZero_balanceModeOn_returnsLegPos() {
		IkRoutines.LegPosX = new double[] {10, 10, 10, 10, 10, 10};
		IkRoutines.LegPosY = new double[] {20, 20, 20, 20, 20, 20};
		IkRoutines.LegPosZ = new double[] {40, 40, 40, 40, 40, 40};
		BalanceValue result = testee.CalcBalance();
		assertEquals(0.0, result.totalTransX, 0);
		assertEquals(-60.0, result.totalTransY, 0);
		assertEquals(40.0, result.totalTransZ, 0);
		assertEquals(-75.61, result.totalXBal, 0.01);
		assertEquals(0.0, result.totalYBal, 0.01);
		assertEquals(90.0, result.totalZBal, 0);
	}
}
