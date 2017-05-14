package esialrobotik.ia.asserv;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import esialrobotik.ia.asserv.raspberry.Asserv;

/**
 * Created by Guillaume on 14/05/2017.
 */
public class AsservModule extends AbstractModule {
  private AsservAPIConfiguration asservAPIConfiguration;

  public AsservModule(AsservAPIConfiguration asservAPIConfiguration) {
    this.asservAPIConfiguration = asservAPIConfiguration;
  }
  @Override
  protected void configure() {
    bind(AsservAPIConfiguration.class).toInstance(asservAPIConfiguration);
    bind(AsservInterface.class).to(Asserv.class).in(Singleton.class);
  }
}
