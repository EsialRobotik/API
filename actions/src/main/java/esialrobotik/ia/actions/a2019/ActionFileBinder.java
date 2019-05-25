package esialrobotik.ia.actions.a2019;

import java.io.File;

import javax.inject.Inject;

import esialrobotik.ia.actions.ActionExecutor;
import esialrobotik.ia.actions.ActionInterface;
import esialrobotik.ia.actions.ActionModuleConfiguration;
import esialrobotik.ia.actions.a2019.ax12.AX12Link;
import esialrobotik.ia.actions.a2019.ax12.AX12LinkException;
import esialrobotik.ia.actions.a2019.ax12.AX12LinkSerial;
import gnu.io.SerialPort;

public class ActionFileBinder implements ActionInterface {
	
	protected ActionExecutor[] actionsList;
	protected File dataDir;
	
	public enum ActionFile {
		SUPER("monsuperfichier.json");
		
		
		public final String nomFichier;
		private ActionFile(String nomFichier) {
			this.nomFichier = nomFichier;
		}
	}
	
	@Inject
	public ActionFileBinder(ActionModuleConfiguration actionModuleConfiguration) throws AX12LinkException {
		SerialPort sp = AX12LinkSerial.getSerialPort(actionModuleConfiguration.getSerialPort());
		AX12LinkSerial ax12Link = new AX12LinkSerial(sp, actionModuleConfiguration.getBaud());
		loadFiles(ax12Link);
	}
	
	public ActionFileBinder(AX12LinkSerial link) {
		loadFiles(link);
	}
	
	protected void loadFiles(AX12LinkSerial ax12Link) {
		ax12Link.enableDtr(false);
		ax12Link.enableRts(false);

		ActionFile[] files = ActionFile.values();
		actionsList = new ActionExecutor[files.length];

		for (int i = 0; i < files.length; i++) {
			File f = new File(this.dataDir.getAbsolutePath() + File.separator + files[i].nomFichier);
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
