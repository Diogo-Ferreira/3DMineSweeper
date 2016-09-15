/********************************************************************
Programme    : Demineur 3D TPI 2014 
Fichier      : Player.java 
---------------------------------------------------------------------
Auteur       : Ferreira Venancio Diogo  
Compilateur  : JDK 7    
Date         : 14.05.2014
Version      : 1.0
---------------------------------------------------------------------
Description  : Gère le temps et le score du joueur.
*********************************************************************/
package Player;

public class Player {
    /**
     * Temps en secondes
     */
    private int time;
    
    /**
     * Score du joueur, à implémenté prochainement
     */
    private int score;
    
    /**
     * Incrémente le temps.
     */
    public void tickTime(){
        time ++;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
