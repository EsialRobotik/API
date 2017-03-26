package esialrobotik.ia.asserv;

/**
 * Created by franc on 10/02/2017.
 */
public interface AsservInterface {

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
     * Envoie le robot à une position définie en marche avant
     * @param position position en (x,y) à atteindre, l'angle n'est pas pris en compte
     */
    void goTo(Position position);

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

    /**
     * Récupère la position courante du robot
     * @return position en (x,y) en mm et angle theta en radians du robot
     */
    Position getPosition();

}
