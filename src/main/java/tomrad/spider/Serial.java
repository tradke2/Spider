package tomrad.spider;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Serial {

	@Autowired
	private Logger log;
	
	@PostConstruct
	public void init()
	{
//	    ser = serial.Serial("/dev/ttyUSB0", Config_Ch3.cSSC_BAUD, timeout=5);	
	}
	
	public String read_until(byte cr) {
		return "SSC Ver 3.2";
	}

	public void readinto(Object[] inputData) {
		// TODO Auto-generated method stub
	}

	public void write(String x) {
		String o = x.replaceAll("\r", "\\\\r");
		log.info(o);
	}

}
