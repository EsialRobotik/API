package esialrobotik.ia.utils.log;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Order;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.plugins.Plugin;

import java.net.URI;

/**
 * Created by icule on 27/03/17.
 */
@Plugin(name = "DebugConfigurationFactory", category = ConfigurationFactory.CATEGORY)
@Order(50)
public class CustomConfigurationFactory extends ConfigurationFactory {

    public static final String rollingFilename =  "log.log";
    public static final String rollingFilenameArchive =  "log-%d{MM-dd-yy HH:mm}.log";
    private Level level;

    //We need it to make log4j happy (it uses introspection at one point)
    public CustomConfigurationFactory(){
        super();
    }

    public CustomConfigurationFactory(Level level){
        super();
        this.level = level;
    }

    private Configuration createConfiguration(final String name, ConfigurationBuilder<BuiltConfiguration> builder) {
        builder.setConfigurationName(name);
        builder.setStatusLevel(Level.ERROR);
        builder.add(builder.newFilter("ThresholdFilter", Filter.Result.ACCEPT, Filter.Result.NEUTRAL).
                addAttribute("level", level));
        AppenderComponentBuilder appenderBuilder = builder.newAppender("Stdout", "CONSOLE").
                addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT);
        appenderBuilder.add(builder.newLayout("PatternLayout").
                addAttribute("pattern", "%d [%t] %-5level: %msg%n%throwable"));
        appenderBuilder.add(builder.newFilter("MarkerFilter", Filter.Result.DENY,
                Filter.Result.NEUTRAL).addAttribute("marker", "FLOW"));
        builder.add(appenderBuilder);
        builder.add(builder.newLogger("org.apache.logging.log4j", level).
                add(builder.newAppenderRef("Stdout")).
                addAttribute("additivity", false));

        LayoutComponentBuilder layoutBuilder;
        layoutBuilder = builder.newLayout("PatternLayout")
                .addAttribute("pattern", "%d [%t] %-5level: %msg%n");
        ComponentBuilder triggeringPolicy = builder.newComponent("Policies")
                .addComponent(builder.newComponent("SizeBasedTriggeringPolicy").addAttribute("size", "100M"));
        appenderBuilder = builder.newAppender("rolling", "RollingFile")
                .addAttribute("fileName", rollingFilename)
                .addAttribute("filePattern", "archive/" + rollingFilenameArchive)
                .add(layoutBuilder)
                .addComponent(triggeringPolicy);
        builder.add(appenderBuilder);
        builder.add( builder.newLogger( "rolling", level )
                .add( builder.newAppenderRef( "rolling" ) )
                .addAttribute( "additivity", false ) );



        builder.add(builder.newRootLogger(level)
                .add(builder.newAppenderRef("Stdout"))
                .add(builder.newAppenderRef("rolling")));
        return builder.build();
    }

    @Override
    protected String[] getSupportedTypes() {
        return new String[] {"*"};
    }

    @Override
    public Configuration getConfiguration(final LoggerContext loggerContext, final ConfigurationSource source) {
        return getConfiguration(loggerContext, source.toString(), null);
    }

    @Override
    public Configuration getConfiguration(final LoggerContext loggerContext, final String name, final URI configLocation) {
        ConfigurationBuilder<BuiltConfiguration> builder = newConfigurationBuilder();
        return createConfiguration(name, builder);
    }
}
