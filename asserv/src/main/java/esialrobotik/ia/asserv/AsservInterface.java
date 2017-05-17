package esialrobotik.ia.asserv;

/**
 * Created by franc on 10/02/2017.
 */
public interface AsservInterface {


    enum AsservStatus {
        STATUS_IDLE,
        STATUS_RUNNING,
        STATUS_HALTED,
        STATUS_BLOCKED
    }

    enum MovementDirection {
        BACKWARD,
        FORWARD,
        NONE
    }

    /***********************************************************************
     * Commande basique
     ************************************************************************/

    /**
     * Arrêt d'urgence de l'asserv, défini la consigne comme étant la position courante du robot
     */
    void emergencyStop();

    /**
     * Annule l'état d'arrêt d'urgence.
     * Tout les commandes seront ignorées en mode Arrêt d'urgence avant l'appel à cette méthode
     */
    void emergencyReset();

    /**
     * Déplace le robot en ligne droite
     * @param dist distance en mm
     */
    void go(int dist);

    /**
     * Fait tourner le robot
     * @param degree angle en degrés
     */
    void turn(int degree);

    /***********************************************************************
     * Commande GoTo
     ************************************************************************/

    /**
     * Envoie le robot à une position définie en marche avant
     * @param position position en (x,y) à atteindre, l'angle n'est pas pris en compte
     */
    void goTo(Position position);

    /**
     * Envoie le robot à une position définie en marche avant avec enchainement des GoTo
     * @param position position en (x,y) à atteindre, l'angle n'est pas pris en compte
     */
    void goToChain(Position position);

    /**
     * Envoie le robot à une position définie en marche arrière
     * @param position position en (x,y) à atteindre, l'angle n'est pas pris en compte
     */
    void goToReverse(Position position);

    /**
     * Aligne le robot avec une position
     * @param position position en (x,y) pour l'alignement
     */
    void face(Position position);

    /***********************************************************************
     * Commande odométrie
     ************************************************************************/

    /**
     * Définie l'angle théta du robot
     * @param theta Angle theta en radian
     */
    void setOdometireTheta(double theta);

    /**
     * Définie la position X du robot
     * @param x X en mm
     */
    void setOdometrieX(int x);

    /**
     * Définie la position Y du robot
     * @param y Y en mm
     */
    void setOdometrieY(int y);

    /***********************************************************************
     * Commande régulateur
     ************************************************************************/

    /**
     * Active ou désactive le mode low speed
     * @param enable true pour activer, false pour désactiver
     */
    void enableLowSpeed(boolean enable);

    /**
     * Active ou désactive le régulateur d'angle
     * @param enable true pour activer, false pour désactiver
     */
    void enableRegulatorAngle(boolean enable);

    /**
     * Reset le régulateur d'angle
     */
    void resetRegulatorAngle();

    /**
     * Active ou désactive le régulateur de distance
     * @param enable true pour activer, false pour désactiver
     */
    void enableRegulatorDistance(boolean enable);

    /**
     * Reset le régulateur d'angle
     */
    void resetRegulatorDistance();

    /***********************************************************************
     * Statut de l'asservissement
     ************************************************************************/

    /**
     * Retourne le statue de la dernière commande de l'asservissement
     * @return AsservStatus
     */
    int getAsservStatus();

    /**
     * Retourne la taille de file de commandes
     * @return Taille de la file de commandes
     */
    int getQueueSize();

    /**
     * Renvoit la position du robot stockée par l'API
     * @return position stockée du robot
     */
    Position getPosition();

    /**
     * Renvoit la dernière direction du mouvement lu sur la connection série
     * @return Direction d movement
     */
    MovementDirection getMovementDirection();
}
