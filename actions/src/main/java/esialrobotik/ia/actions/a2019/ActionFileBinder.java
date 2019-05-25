package esialrobotik.ia.actions.a2019;

import java.io.File;

import esialrobotik.ia.actions.ActionExecutor;
import esialrobotik.ia.actions.ActionInterface;
import esialrobotik.ia.actions.a2019.ax12.AX12Link;
import esialrobotik.ia.actions.a2019.ax12.AX12LinkException;

public class ActionFileBinder implements ActionInterface {
	
	protected ActionExecutor[] actionsList;
	
	public enum ActionFile {
		SUPER("monsuperfichier.json");
		
		
		public final String nomFichier;
		private ActionFile(String nomFichier) {
			this.nomFichier = nomFichier;
		}
	}

	public ActionFileBinder(AX12Link ax12Link, File dataDirectory) {
		// Disable pumps
		try {
			ax12Link.enableDtr(false);
			ax12Link.enableRts(false);
		} catch (AX12LinkException e) {
			e.printStackTrace();
		}
		
		ActionFile[] files = ActionFile.values();
		actionsList = new ActionExecutor[files.length];
		
		for (int i=0; i<files.length; i++) {
			File f = new File(dataDirectory.getAbsolutePath() + File.separator + files[i].nomFichier);
			actionsList[i] = new ActionAX12Json(ax12Link, f);
		}
	}
	
	public int getActionExecutorIdForActionFile(ActionFile af) {
		return af.ordinal();
	}
	
	@Override
	public ActionExecutor getActionExecutor(int id) {
		return actionsList[id];
	}

}
