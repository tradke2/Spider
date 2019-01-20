package tomrad.spider;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import tomrad.spider.controller.Controller;
import tomrad.spider.controller.mock.ControllerMock;
import tomrad.spider.gait.Gait;

@RunWith(MockitoJUnitRunner.class)
public class PhoenixTest {

	@InjectMocks
	private Phoenix testee;

	@Mock
	private Logger log;
	
	@Spy
	private Controller controller = new ControllerMock();
	
	@Mock
	private Servo servo;

	@Mock
	private SingleLeg singleLeg;

	@Mock
	private IkRoutines ikRoutines;

	@Mock
	private Gait gait;

	@Mock
	private Balance balance;

	@Test
	public void testInit() {
		Whitebox.setInternalState(testee, "remainingLoops", 1);
		testee.Init();
	}

	@Test
	public void testRun() {
		Whitebox.setInternalState(controller, "hexOn", true);
		Whitebox.setInternalState(testee, "remainingLoops", 1);
		testee.run();
	}

	@Test
	public void testMainLoop() {
		Whitebox.setInternalState(controller, "hexOn", true);
		Whitebox.setInternalState(testee, "remainingLoops", 1);
		try {
			testee.MainLoop();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}

}
