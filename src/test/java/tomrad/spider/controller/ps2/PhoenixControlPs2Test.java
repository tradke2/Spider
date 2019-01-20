package tomrad.spider.controller.ps2;

import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import tomrad.spider.GpioWiring;
import tomrad.spider.WiringMock;
import tomrad.spider.controller.ps2.PhoenixControlPs2;
import tomrad.spider.gait.Gait;

@RunWith(MockitoJUnitRunner.class)
public class PhoenixControlPs2Test {

	@Mock
	private GpioWiring wiringPi;
	
	@Mock
	private Gait gait;
	
	@InjectMocks
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
