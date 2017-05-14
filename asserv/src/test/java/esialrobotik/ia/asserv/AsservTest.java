package esialrobotik.ia.asserv;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.AbstractModule;
import org.junit.Test;

/**
 * Created by franc on 10/02/2017.
 */
public class AsservTest {
  String config = "{" +
          "\"baud\":115200,\n" +
          "\"serie\":\"lala\"" +
          "} ";


  @Test
  public void testInstantiation() {
    JsonParser parser = new JsonParser();
    JsonObject object = new JsonParser().parse(config).getAsJsonObject();

    AsservAPIConfiguration configuration = new AsservAPIConfiguration(object);
    //Injector injector = Guice.createInjector(new AsservModule(configuration));
  }
}
