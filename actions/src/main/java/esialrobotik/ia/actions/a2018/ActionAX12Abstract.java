package esialrobotik.ia.actions.a2018;

import java.io.IOException;
import java.util.ArrayList;

import esialrobotik.ia.actions.ActionExecutor;

/**
 * 
 * @author gryttix
 *
 */
public abstract class ActionAX12Abstract implements ActionExecutor {

	// La liaison série vers les AX12
    private AX12Link serialAX12;
    
    // Utilisé pour la lecture des réponses des ax12
    protected ArrayList<Byte> lecture;
    
    // Une seule instance de l'ax12 : on change son adresse pour chaque commande
    private AX12 ax12;
    
    protected boolean fini = false;
    
    protected enum AX12_NAME {
    	RAIL(1),
    	PENTE(2),
    	ORIENTATION(3),
    	BRAS_GAUCHE(4),
    	BRAS_DROIT(5);
    	
    	public final int adresse;
    	private AX12_NAME(int adresse) {
    		this.adresse = adresse;
    	}
    }
    
    // Les diverses actions possibles par AX12
	protected enum ACTION_AX12 {
		// Gère la crémaillère pour faire tranlster les tubes
		EAU_RAIL_GARAGE(AX12_NAME.RAIL, 267.7),
		EAU_RAIL_REMPLISSAGE_1(AX12_NAME.RAIL, 173.0),
		EAU_RAIL_REMPLISSAGE_2(AX12_NAME.RAIL, 57.5),
		EAU_RAIL_MILIEU_VIDANGE(AX12_NAME.RAIL, 131.4),
		EAU_RAIL_LANCEUR_GAUCHE(AX12_NAME.RAIL, 300.0),
		EAU_RAIL_LANCEUR_DROIT(AX12_NAME.RAIL, 183.9),
		EAU_RAIL_EXTREME_GAUCHE(AX12_NAME.RAIL, 0.0),
		
		// Permet de faire tourner les tubes comme des aiguilles sur un cadran d'horloge
		EAU_ORIENTATION_DROIT(AX12_NAME.ORIENTATION, 151.5),
		EAU_ORIENTATION_VIDANGE_GAUCHE(AX12_NAME.ORIENTATION, 52.5),
		EAU_ORIENTATION_VIDANGE_DROIT(AX12_NAME.ORIENTATION, 248.1),
		EAU_ORIENTATION_LANCEUR_GAUCHE(AX12_NAME.ORIENTATION, 146.8),
		EAU_ORIENTATION_LANCEUR_DROIT(AX12_NAME.ORIENTATION, 146.9),
		EAU_ORIENTATION_HORIZONTAL_GAUCHE(AX12_NAME.ORIENTATION, 240.2),
		EAU_ORIENTATION_HORIZONTAL_DROIT(AX12_NAME.ORIENTATION, 60.3),
		EAU_ORIENTATION_REMPLISSAGE_INCLINAISON_GAUCHE(AX12_NAME.ORIENTATION, 145.5),
		EAU_ORIENTATION_REMPLISSAGE_INCLINAISON_DROITE(AX12_NAME.ORIENTATION, 154.5),
		
		// Gère l'inclinaison des tubes à l'intérieur du robot
		EAU_PENTE_HORIZONTALE(AX12_NAME.PENTE, 142.3),
		EAU_PENTE_QUASI_HORIZONTALE(AX12_NAME.PENTE, 150.0),
		EAU_PENTE_VERTICALE(AX12_NAME.PENTE, 231.7),
		EAU_PENTE_DOUCE(AX12_NAME.PENTE, 139.6),
		EAU_PENTE_FORTE_GAUCHE(AX12_NAME.PENTE, 127.2),
		EAU_PENTE_FORTE_DROIT(AX12_NAME.PENTE, 124.5),
		EAU_PENTE_REMPLISSAGE(AX12_NAME.PENTE, 242.3),
		EAU_PENTE_INTERRUPTEUR(AX12_NAME.PENTE, 190.0),
		
		// Gère le bras gauche du robot (du point de vue du robot)
		BRAS_GAUCHE_SORTIR(AX12_NAME.BRAS_GAUCHE, 145.7),
		BRAS_GAUCHE_RENTRER(AX12_NAME.BRAS_GAUCHE, 242.0),
		
		// Gère le bras droit du robot (du point de vue du robot)
		BRAS_DROIT_SORTIR(AX12_NAME.BRAS_DROIT, 247.7),
		BRAS_DROIT_RENTRER(AX12_NAME.BRAS_DROIT, 153.5);
		
		public final AX12_NAME ax12;
		public final double angle;
		ACTION_AX12(AX12_NAME ax12, double angle) {
			this.ax12 = ax12;
			this.angle = angle;
		}
	}

    public ActionExecutor init(AX12Link serialAX12) {
        this.serialAX12 = serialAX12;
        ax12 = new AX12(1, serialAX12);
        return this;
    }
    
    @Override
    public void execute() {
		fini = false;
    	this.childExecution();
    	fini = true;
    }
    
    @Override
    public boolean finished() {
        return fini;
    }
	
	/**
	 * Applique l'état demandé
	 * Cette fonction s'appelle go parce que do est déjà pris :'(
	 * @param et
	 */
	protected void go(ACTION_AX12 et) {
		if (ax12 == null) {
			return;
		}
		
		int essaisRestants = 5;
		
		ax12.setAddress(et.ax12.adresse);
		
		while(essaisRestants > 0) {
			try {
				// Ptit hack dégueu : on ajoute de l'élasticité à l'ax12 qui lève les tubes
				// Ça évite de perdre les balles du dessus à cause des secousses
				if (et.ax12 == AX12_NAME.PENTE) {
					ax12.setCwComplianceSlope(99);
					ax12.setCcwComplianceSlope(99);
				}
				if (et.ax12 == AX12_NAME.ORIENTATION) {
					ax12.setCwComplianceSlope(100);
					ax12.setCcwComplianceSlope(100);
				}
				ax12.setServoPositionInDegrees(et.angle);
				essaisRestants = 0;
			} catch (AX12LinkException e) {
				e.printStackTrace();
				essaisRestants--;
				System.out.println("essais restant : "+essaisRestants);
			} catch (AX12Exception e) {
				e.printStackTrace();
				essaisRestants--;
				System.out.println("essais restant : "+essaisRestants);
			}	
		}
	}
	
	/**
	 * Attend une certaine durée en ms
	 * @param duree tps à attendre en ms
	 */
	protected void attend(long duree) {
		try {
			Thread.sleep(duree);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Attend que tous les ax12 de la liste aient fini de bouger
	 * Attention aux blagues avec le mode rotation continue ;)
	 * @param ax12
	 */
	protected void attendreImmobilisation(AX12_NAME... liste) {
		boolean bouge = false;
		int maxExceptionTolerance = 10;
		
		do {
			if (bouge) {
				// Pour éviter de spammer la liaison série, on est pas à 50ms près
				attend(50);
			}
			bouge = false;
			for (AX12_NAME ax : liste) {
				ax12.setAddress(ax.adresse);
				try {
					if (ax12.isMoving()) {
						bouge = true;
						break;
					}
				} catch (AX12LinkException e) {
					e.printStackTrace();
					if (maxExceptionTolerance-- < 0) {
						bouge = true;
					}
				} catch (AX12Exception e) {
					e.printStackTrace();
					if (maxExceptionTolerance-- < 0) {
						bouge = true;
					}
				}
			}
		} while (bouge);
	}
    
	/**
	 * Les commandes utiles des classes enfant
	 */
    protected abstract void childExecution();
    
    /**
     * Allume ou éteint le lanceur
     * Oui ça n'a normalement rien à voir avec un AX12 mais c'est contrôlé par le pin DTR de l'UART :p
     * @param allumer
     */
    protected void allumerLanceur(boolean allumer) {
    	try {
			this.serialAX12.enableLanceur(allumer);
		} catch (AX12LinkException e) {
			e.printStackTrace();
		}
    }

	@Override
	public void resetActionState() {
		this.fini = false;
	}

}
