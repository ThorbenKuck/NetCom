package de.thorbenkuck.netcom.logging;

import de.thorbenkuck.netcom.datatypes.interfaces.QueuedAction;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

public class Logging {

	private static boolean initialised = false;

	public static void initLog() {
		if(!initialised) {
			defaultInit();
			initialised = true;
		}
	}

	private static void defaultInit() {
		ConfigurationBuilder<BuiltConfiguration> configurationBuilder = ConfigurationBuilderFactory.newConfigurationBuilder();
		configurationBuilder.setStatusLevel(Level.INFO);
		configurationBuilder.setConfigurationName("DefaultConfig");
		configurationBuilder.add(configurationBuilder.newFilter("ThresholdFilter", Filter.Result.ACCEPT, Filter.Result.NEUTRAL)
				.addAttribute("level", Level.DEBUG));
		AppenderComponentBuilder appenderBuilder = configurationBuilder.newAppender("Stdout", "CONSOLE").addAttribute("target",
				ConsoleAppender.Target.SYSTEM_OUT);
		appenderBuilder.add(configurationBuilder.newLayout("PatternLayout")
				.addAttribute("pattern", "%d [%t] %-5level: %msg%n%throwable"));
		appenderBuilder.add(configurationBuilder.newFilter("MarkerFilter", Filter.Result.DENY, Filter.Result.NEUTRAL)
				.addAttribute("marker", "FLOW"));
		configurationBuilder.add(appenderBuilder);
		configurationBuilder.add(configurationBuilder.newLogger("org.apache.logging.log4j", Level.DEBUG)
				.add(configurationBuilder.newAppenderRef("Stdout")).addAttribute("additivity", false));
		configurationBuilder.add(configurationBuilder.newRootLogger(Level.ERROR).add(configurationBuilder.newAppenderRef("Stdout")));
		Configurator.initialize(configurationBuilder.build());
	}

	public static void customInit(QueuedAction queuedAction) {
		queuedAction.doBefore();
		queuedAction.doAction();
		queuedAction.doAfter();
	}
}
