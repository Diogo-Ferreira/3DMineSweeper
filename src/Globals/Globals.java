/********************************************************************
Programme    : Demineur 3D TPI 2014 
Fichier      : Globals.java 
---------------------------------------------------------------------
Auteur       : Ferreira Venancio Diogo  
Compilateur  : JDK 7    
Date         : 14.05.2014
Version      : 1.0
---------------------------------------------------------------------
Description  : Ce fichier regroupe un ensemble de superGlobales.
*********************************************************************/
package Globals;

public class Globals {
    /**
     * Taille des cubes
     */
    public static float CUBE_SIZE = 1;
    
    /**
     * Nombre de cubes en X (Largeur).
     */
    public static int   NB_DIM_X = 5;
    
    /**
     * Nombre de cubes en Y (Hauteur).
     */
    public static int   NB_DIM_Y = 5;
    
    /**
     * Nombre de cubes en Z (Profondeur).
     */
    public static int   NB_DIM_Z = 5;
    
    /**
     * Nombre de mines total.
     */
    public static int   NB_MINES = (int)(NB_DIM_X * NB_DIM_Y * NB_DIM_Z * 1/8);
    
    /**
     * Espacement entre les cubes ( Doit être > 2 !!).
     */
    public static float CUBE_MARGIN = 0.7f;
    
    /**
     * Option pour vérifier les diagonales ou non.
     */
    public static boolean USE_DIAGONALS = true;
    
    /**
     * Option pour afficher les cages
     */
    public static boolean USE_CAGES = false;
    
    /**
     * Option pour cacher les valeurs autour du curseur
     */
    public static boolean USE_HIDING = false;
    
    /**
     * Permets de savoir si le joueur à lu le panneau d'aide
     */
    public static boolean PLAYER_READY = false;
}
