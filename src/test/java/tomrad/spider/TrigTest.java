package tomrad.spider;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import tomrad.spider.Trig.SinCos;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TrigTest {

	@Mock
	private Logger logger;

	@InjectMocks
	@Spy
	@Autowired
	private Trig testee;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	double[] angles = new double[] { //
			-361, -360, -359, //
			-271, -270, -269, //
			-181, -180, -179, //
			-91, -90, -89, //
			-1, 0, 1, //
			89, 90, 91, //
			179, 180, 181, //
			269, 270, 271, //
			359, 360, 361 //
	};
	double[] sinus = new double[] { //
			-0.01745, 0, 0.01745, //
			0.99984, 1, 0.99984, //
			0.01745, 0, -0.01745, //
			-0.99984, -1, -0.99984, //
			-0.01745, 0, 0.01745, //
			0.99984, 1, 0.99984, //
			0.01745, 0, -0.01745, //
			-0.99984, -1, -0.99984, //
			-0.01745, 0, 0.01745//
	};
	double[] cosinus = new double[] { //
			0.99984, 1, 0.99984, //
			0.01745, 0, -0.01745, //
			-0.99984, -1, -0.99984, //
			-0.01745, 0, 0.01745, //
			0.99984, 1, 0.99984, //
			0.01745, 0, -0.01745, //
			-0.99984, -1, -0.99984, //
			-0.01745, 0, 0.01745, //
			0.99984, 1, 0.99984, //
	};

	@Test
	public void testGetSinCos() {
		for (int i = 0; i < angles.length; i++) {
			SinCos result = testee.getSinCos(angles[i]);
			assertEquals("Sinus(" + angles[i] + ")", sinus[i], result.sin, 0.00005);
			assertEquals("Cosinus(" + angles[i] + ")", cosinus[i], result.cos, 0.00005);
		}
	}

	@Test
	public void testGetSinCos_multipleSpins() {
		for (int i = 0; i < angles.length; i++) {
			for (int s = -3; s <= 3; s++) {
				double angle = angles[i] + s * 360;
				SinCos result = testee.getSinCos(angle);
				assertEquals("Sinus(" + angle + ")", sinus[i], result.sin, 0.00005);
				assertEquals("Cosinus(" + angle + ")", cosinus[i], result.cos, 0.00005);
			}
		}
	}

	@Test
	public void testGetBoogTan() {

		// x = 0, y > 0
		double result = testee.getBoogTan(0, 1);
		assertEquals(0, result, 0.0005);

		// x = 0, y = 0
		result = testee.getBoogTan(0, 0);
		assertEquals(0, result, 0.0005);

		// x = 0, y < 0
		result = testee.getBoogTan(0, -1);
		assertEquals(Math.PI, result, 0.0005);

		// y = 0, x > 0
		result = testee.getBoogTan(1, 0);
		assertEquals(Math.PI / 2, result, 0.0005);

		// y = 0, x < 0
		result = testee.getBoogTan(-1, 0);
		assertEquals(-Math.PI / 2, result, 0.0005);

		// y > 0, x egal
		result = testee.getBoogTan(-1, 1);
		assertEquals(-0.78539, result, 0.0005);

		// y < 0, x > 0
		result = testee.getBoogTan(1, -1);
		assertEquals(-0.78539 + Math.PI, result, 0.0005);
		// assertEquals(0.78539, result, 0.0005);

		// y < 0, x < 0
		result = testee.getBoogTan(-1, -1);
		assertEquals(0.78539 - Math.PI, result, 0.0005);
		// assertEquals(-0.78539+Math.PI/2, result, 0.0005);
	}
	// '--------------------------------------------------------------------
	// '[BOOGTAN2] Gets the Inverse Tangus from X/Y with the where Y can be zero or
	// negative
	// 'BoogTanX - Input X
	// 'BoogTanY - Input Y
	// 'BoogTan - Output BOOGTAN2(X/Y)
	// GetBoogTan [BoogTanX, BoogTanY]
	// IF(BoogTanX = 0) THEN ' X=0 -> 0 or PI
	// IF(BoogTanY >= 0) THEN
	// BoogTan = 0.0
	// ELSE
	// BoogTan = 3.141592
	// ENDIF
	// ELSE
	//
	// IF(BoogTanY = 0) THEN ' Y=0 -> +/- Pi/2
	// IF(BoogTanX > 0) THEN
	// BoogTan = 3.141592 / 2.0
	// ELSE
	// BoogTan = -3.141592 / 2.0
	// ENDIF
	// ELSE
	//
	// IF(BoogTanY > 0) THEN 'BOOGTAN(X/Y)
	// BoogTan = FATAN(TOFLOAT(BoogTanX) / TOFLOAT(BoogTanY))
	// ELSE
	// IF(BoogTanX > 0) THEN 'BOOGTAN(X/Y) + PI
	// BoogTan = FATAN(TOFLOAT(BoogTanX) / TOFLOAT(BoogTanY)) + 3.141592
	// ELSE 'BOOGTAN(X/Y) - PI
	// BoogTan = FATAN(TOFLOAT(BoogTanX) / TOFLOAT(BoogTanY)) - 3.141592
	// ENDIF
	// ENDIF
	// ENDIF
	// ENDIF
	// return

}
