package esialrobotik.ia.utils.lcd;

public interface LCD {

    /**
     * Affiche un texte sur l'écran LCD sur une nouvelle ligne
     * @param str Le texte à afficher
     */
    public void println(String str);

    /**
     * Efface l'écran LCD
     */
    public void clear();

}
