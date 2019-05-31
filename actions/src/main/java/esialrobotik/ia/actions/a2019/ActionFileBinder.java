package esialrobotik.ia.actions.a2019;

import esialrobotik.ia.actions.ActionExecutor;
import esialrobotik.ia.actions.ActionInterface;
import esialrobotik.ia.actions.ActionModuleConfiguration;
import esialrobotik.ia.actions.a2019.ax12.AX12;
import esialrobotik.ia.actions.a2019.ax12.AX12Exception;
import esialrobotik.ia.actions.a2019.ax12.AX12LinkException;
import esialrobotik.ia.actions.a2019.ax12.AX12LinkSerial;
import gnu.io.SerialPort;

import javax.inject.Inject;
import java.io.File;

public class ActionFileBinder implements ActionInterface {
	
	protected ActionExecutor[] actionsList;
	protected File dataDir;
	protected AX12LinkSerial ax12Link;
	
	public enum ActionFile {
		PREPARATION_PDISTRIB("preparation_pdistrib.json", true), // 0
		RECUPERATION_PDISTRIB("recuperation_pdistrib.json"), // 1
		INIT("init.json"), // 2
		PREPARATION_GDISTRIB_BLEU("preparation_gdistrib_bleu.json"), // 3
		RECUPERATION_GDISTRIB_BLEU("recuperation_gdistrib_bleu.json"), // 4
		RANGEMENT_BRAS_PILE_1("rangement_bras_pile_1.json"), // 5
		RANGEMENT_BRAS_PILE_2("rangement_bras_pile_2.json"), // 6
		RANGEMENT_BRAS_PILE_3("rangement_bras_pile_3.json"), // 7
		PREPARATION_GDISTRIB_ROUGES("preparation_gdistrib_rouges.json"), // 8
		RECUPERATION_GDISTRIB_ROUGES("recuperation_gdistrib_rouges.json"), // 9
		LARGAGE_GDISTRIB_1("largage_gdistrib_1.json"), // 10 --> 1 - 2 - 3
		LARGAGE_GDISTRIB_2("largage_gdistrib_2.json"), // 11
		LARGAGE_GDISTRIB_3("largage_gdistrib_3.json"), // 12
		LARGAGE_GDISTRIB_4("largage_gdistrib_4.json"), // 13 --> 4 - 2 - 3
		LARGAGE_GDISTRIB_5("largage_gdistrib_5.json"), // 14 --> 4 - 2 - 3
		PREPARATION_RECUR_GOLD("preparation_recup_gold.json"), // 15
		PREPARATION_LARGAGE_GOLD("preparation_largage_gold.json"), // 16
		LARGAGE_GOLD("largage_gold.json"), // 17
		LARGAGE_GDISTRIB_MIROIR_2("largage_gdistrib_2_miroir.json"), // 18
		PREPARATION_RECUR_GOLD_MIROIR("preparation_recup_gold_miroir.json"), // 19
		PREPARATION_LARGAGE_GOLD_MIROIR("preparation_largage_gold_miroir.json"); // 20

		public final String nomFichier;
		public final boolean instantReturn;
		private ActionFile(String nomFichier) {
			this.nomFichier = nomFichier;
			this.instantReturn = false;
		}
		private ActionFile(String nomFichier, boolean instantReturn) {
			this.nomFichier = nomFichier;
			this.instantReturn = instantReturn;
		}
	}
	
	@Inject
	public ActionFileBinder(ActionModuleConfiguration actionModuleConfiguration) throws AX12LinkException {
		SerialPort sp = AX12LinkSerial.getSerialPort(actionModuleConfiguration.getSerialPort());
		ax12Link = new AX12LinkSerial(sp, actionModuleConfiguration.getBaud());
		this.dataDir = actionModuleConfiguration.getDataDir();
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
			actionsList[i] = new ActionAX12Json(ax12Link, f, files[i].instantReturn);
		}
	}
	
	public int getActionExecutorIdForActionFile(ActionFile af) {
		return af.ordinal();
	}
	
	@Override
	public ActionExecutor getActionExecutor(int id) {
		return actionsList[id];
	}

	@Override
	public void stopActions() {
		this.ax12Link.disableAx12AndShutdownLink();
		ax12Link.enableDtr(false);
		ax12Link.enableRts(false);
	}

}
