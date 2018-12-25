package tomrad.spider;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

@SpringBootApplication
public class DemoApplication {

	@Autowired
	private Logger logger;
	
	@Value("${os.arch}")
	private String osArch;
	
	@Autowired
	private GpioWiring wiringPi;
	
	@Autowired
	private Gait gait; 
	
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(DemoApplication.class, args);
		Phoenix phoenix = ctx.getBean(Phoenix.class);
		phoenix.run();
	}
	
	@PostConstruct
	public void init()
	{
		logger.info("os.arch={}", osArch);					
	}
	
	@Bean @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public Logger getLogger(InjectionPoint ip)
	{
		String clazzName = ip.getField().getDeclaringClass().getName();
		return LoggerFactory.getLogger(clazzName);
	}
	
	@Bean
	public Controller getController()
	{
		if ("x86".equals(osArch))
		{
			return new ControllerMock();
		}
		return new PhoenixControlPs2(gait, wiringPi);		
	}
	
	@Bean
	public GpioWiring getWiringPi()
	{
		if ("x86".equals(osArch))
		{
			return new WiringMock();
		}
		return new WiringPi();
	}
	
	@PreDestroy
	public void shutdown()
	{
	}
}
