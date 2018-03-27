package tomrad.spider;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static tomrad.spider.Config_Ch3.cCoxaLength;
import static tomrad.spider.Config_Ch3.cFemurLength;
import static tomrad.spider.Config_Ch3.cTibiaLength;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import tomrad.spider.Balance.BalanceValue;
import tomrad.spider.IkRoutines.BodyIkResult;
import tomrad.spider.IkRoutines.CalcIkResult;
import tomrad.spider.IkRoutines.LegIkResult;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IkRoutinesTest {

	@InjectMocks
	@Spy
	@Autowired
	private IkRoutines testee;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		testee.InitIK();
	}

	@Test
	public void testInitIK_resetsInternalState() {
		Whitebox.setInternalState(testee, "BodyPosX", 1.1);
		Whitebox.setInternalState(testee, "BodyPosY", 2.2);
		Whitebox.setInternalState(testee, "BodyPosZ", 3.3);
		Whitebox.setInternalState(testee, "BodyRotX", 4.4);
		Whitebox.setInternalState(testee, "BodyRotY", 5.5);
		Whitebox.setInternalState(testee, "BodyRotZ", 6.6);
		Whitebox.setInternalState(testee, "BalanceMode", true);
		testee.InitIK();
		assertEquals(0.0, Whitebox.getInternalState(testee, "BodyPosX"));
		assertEquals(0.0, Whitebox.getInternalState(testee, "BodyPosY"));
		assertEquals(0.0, Whitebox.getInternalState(testee, "BodyPosZ"));
		assertEquals(0.0, Whitebox.getInternalState(testee, "BodyRotX"));
		assertEquals(0.0, Whitebox.getInternalState(testee, "BodyRotY"));
		assertEquals(0.0, Whitebox.getInternalState(testee, "BodyRotZ"));
		assertEquals(false, Whitebox.getInternalState(testee, "BalanceMode"));
	}

	@Test
	public void testCalcIK_() {
		//	                                 cRR,    cRM,    cRF,    cLR,    cLM,    cLF
		IkRoutines.LegPosX = new double[] {  43.00,  86.00,  43.00,  43.00,  86.00,  43.00 };
		IkRoutines.LegPosY = new double[] { 141.00, 141.00, 141.00, 141.00, 141.00, 141.00 };
		IkRoutines.LegPosZ = new double[] {  74.47,   0.00, -74.47,  74.47,   0.00, -74.47 };
		BalanceValue input = new BalanceValue(0, 0, 0, 0, 0, 0);
		CalcIkResult result = testee.CalcIK(input);
		assertArrayEquals(new int[] { 0, 0, 0, 0, 0, 0 }, result.coxaAngle);
	}

	@Test
	public void testLegIK_01() {
		LegIkResult result = testee.LegIK(cCoxaLength, cTibiaLength - cFemurLength, 0);
		assertNotNull(result);
		assertFalse(result.ikSolutionError);
		assertEquals(0, result.coxaAngle, 0.0005);
		assertEquals(-90, result.femurAngle, 0.0005);
		assertEquals(-90, result.tibiaAngle, 0.0005);
	}

	@Test
	public void testLegIK_02() {
		LegIkResult result = testee.LegIK(cCoxaLength + cTibiaLength + cFemurLength, 0, 0);
		assertNotNull(result);
		assertFalse(result.ikSolutionError);
		assertEquals(0, result.coxaAngle, 0.0005);
		assertEquals(0, result.femurAngle, 0.0005);
		assertEquals(90, result.tibiaAngle, 0.0005);
	}

	@Test
	public void testLegIK_03() {
		LegIkResult result = testee.LegIK(cCoxaLength, cTibiaLength + cFemurLength, 0);
		assertNotNull(result);
		assertFalse(result.ikSolutionError);
		assertEquals(0, result.coxaAngle, 0.0005);
		assertEquals(90, result.femurAngle, 0.0005);
		assertEquals(90, result.tibiaAngle, 0.0005);
	}

	@Test
	public void testLegIK_04() {
		LegIkResult result = testee.LegIK(0, cTibiaLength - cFemurLength, cCoxaLength);
		assertNotNull(result);
		assertFalse(result.ikSolutionError);
		assertEquals(90, result.coxaAngle, 0.0005);
		assertEquals(-90, result.femurAngle, 0.0005);
		assertEquals(-90, result.tibiaAngle, 0.0005);
	}

	@Test
	public void testLegIK_05() {
		LegIkResult result = testee.LegIK(0, cTibiaLength - cFemurLength, - cCoxaLength);
		assertNotNull(result);
		assertFalse(result.ikSolutionError);
		assertEquals(-90, result.coxaAngle, 0.0005);
		assertEquals(-90, result.femurAngle, 0.0005);
		assertEquals(-90, result.tibiaAngle, 0.0005);
	}

	@Test
	public void testLegIK_06() {
		LegIkResult result = testee.LegIK(0, cTibiaLength + cFemurLength, cCoxaLength);
		assertNotNull(result);
		assertFalse(result.ikSolutionError);
		assertEquals(90, result.coxaAngle, 0.0005);
		assertEquals(90, result.femurAngle, 0.0005);
		assertEquals(90, result.tibiaAngle, 0.0005);
	}

	@Test
	public void testLegIK_07() {
		LegIkResult result = testee.LegIK(0, cTibiaLength + cFemurLength, - cCoxaLength);
		assertNotNull(result);
		assertFalse(result.ikSolutionError);
		assertEquals(-90, result.coxaAngle, 0.0005);
		assertEquals(90, result.femurAngle, 0.0005);
		assertEquals(90, result.tibiaAngle, 0.0005);
	}

	@Test
	public void testLegIK_08() {
		LegIkResult result = testee.LegIK(0, cTibiaLength + cFemurLength + 1, - cCoxaLength);
		assertNotNull(result);
		assertTrue(result.ikSolutionError);
		assertEquals(-90, result.coxaAngle, 0.0005);
		assertEquals(180, result.femurAngle, 0.0005);
		assertEquals(180, result.tibiaAngle, 0.0005);
	}

	@Test
	public void testBodyIK_01()
	{
		BodyIkResult result = testee.BodyIK(0, 0, 0, new BalanceValue(0, 0, 0, 0, 0, 0), 0, 0);
		assertNotNull(result);
		assertEquals(0, result.x, 0.0005);
		assertEquals(0, result.y, 0.0005);
		assertEquals(0, result.z, 0.0005);		
	}

	@Test
	public void testBodyIK_02()
	{
		BodyIkResult result = testee.BodyIK(0, 0, 0, new BalanceValue(0, 0, 0, 0, 0, 0), 0, 0);
		assertNotNull(result);
		assertEquals(0, result.x, 0.0005);
		assertEquals(0, result.y, 0.0005);
		assertEquals(0, result.z, 0.0005);		
	}
}
