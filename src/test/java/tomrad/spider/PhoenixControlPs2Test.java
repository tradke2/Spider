package tomrad.spider;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PhoenixControlPs2Test {

	@Mock
	private GpioWiring wiringPi;
	
	@Mock
	private Gait gait;
	
	private PhoenixControlPs2 testee;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		wiringPi = new WiringMock(PhoenixControlPs2.PS2DAT, PhoenixControlPs2.PS2CMD);
		testee = new PhoenixControlPs2(gait, wiringPi); 
	}

	@Test
	public void testInitController_() {
		boolean result = testee.InitController();
		assertFalse(result);
	}

}
