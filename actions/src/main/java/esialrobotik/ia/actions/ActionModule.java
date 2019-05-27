package esialrobotik.ia.actions;

import com.google.inject.AbstractModule;
import esialrobotik.ia.actions.a2019.ActionFileBinder;

/**
 * Created by icule on 21/05/17.
 */
public class ActionModule extends AbstractModule{
    private ActionModuleConfiguration actionModuleConfiguration;

    public ActionModule(ActionModuleConfiguration actionModuleConfiguration) {
        this.actionModuleConfiguration = actionModuleConfiguration;
    }


    @Override
    protected void configure() {
        bind(ActionModuleConfiguration.class).toInstance(actionModuleConfiguration);
        bind(ActionInterface.class).to(ActionFileBinder.class);
    }
}
