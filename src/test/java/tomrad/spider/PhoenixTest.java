package tomrad.spider;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PhoenixTest {

	@InjectMocks
	@Spy
	@Autowired
	private Phoenix testee;

	@Autowired
	private Controller controller;
	
	@Test
	public void testInit() {
		testee.Init();
	}

	@Test
	public void testRun() {
		Whitebox.setInternalState(controller, "HexOn", 1);
		Whitebox.setInternalState(testee, "remainingLoops", 1);
		testee.run();
	}

	@Test
	public void testMainLoop() {
		Whitebox.setInternalState(controller, "HexOn", 1);
		Whitebox.setInternalState(testee, "remainingLoops", 1);
		try {
			testee.MainLoop();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}

}
