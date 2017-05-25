package esialrobotik.ia.actions;

/**
 * Created by Guillaume on 18/05/2017.
 */
public interface ActionExecutor {

    /**
     * Execute the action.
     */
    void execute();

    /**
     * Is the current execution of the task finished.
     *
     * @return finished or not
     */
    boolean finished();

    /**
     * On fait comme si l'action avait pas démarée
     */
    void resetActionState();

}
