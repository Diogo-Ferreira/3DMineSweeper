/********************************************************************
Programme    : Demineur 3D TPI 2014 
Fichier      : Grid.java 
---------------------------------------------------------------------
Auteur       : Ferreira Venancio Diogo  
Compilateur  : JDK 7    
Date         : 14.05.2014
Version      : 1.0
---------------------------------------------------------------------
Description  : Modèle qui contient la grille de données (Valeurs et mines).
*********************************************************************/
package Grid;

import Globals.Globals;
import com.jme3.math.Vector3f;
import java.util.LinkedList;
import java.util.Random;

public class Grid {
    
    
    /**
     * ATTENTION !
     * i ==> Z
     * j ==> Y
     * k ==> X
     */
    
    /**
     * Tableau de données du jeu, ici seront stocker les valeurs de chaque case,
     * pour le mines, un nombre spécial est utilisé avec la variable <code>mineCode</code>,
     * et le cubes explosé de même avec la variable openCubeCode, ceux qui sont dévoilés
     * sont simplement inversé (ex. 4 => -4). 
     */
    private int dataGrid[][][] = null;
    
    /**
     * Valeur pour identifier les mines.
     */
    public static int mineCode = 999;
    
    /**
     * Valeur pour identifier les cube explosés.
     */
    public static int openCubeCode = 998;
    
    
    /**
     * Remplit le tableau le tableau de données,
     * les mines ne sont pas altérées.
     */
    public void initGrid(){
        dataGrid = new int[Globals.NB_DIM_Z][Globals.NB_DIM_Y][Globals.NB_DIM_X];
        fillMineGrid();
    }
    
    /**
     * Fonction qui vérifie si le terrain est déminé
     * @return Vrai ou Faux si le terrain est déminé.
     */
    public boolean isDemined(){
        
        for(int i = 0; i < Globals.NB_DIM_Z; i++){
            for(int j = 0; j < Globals.NB_DIM_Y; j++){
                for(int k = 0; k < Globals.NB_DIM_X ; k++){
                    if(dataGrid[i][j][k] > 0 && dataGrid[i][j][k]!= openCubeCode && dataGrid[i][j][k] != mineCode )
                        return false;
                }   
            }
        }
        
        return true;
        
    }
    
    /**
     * Supprimer les cubes vides adjacent au cube sujet, et dévoile ceux
     * qui on une valeur > 0.
     * @param gridLocation position du cube par rapport au tableau de données
     */
    public void deleteAdjacentCube(Vector3f gridLocation){
        for(int i = -1 ; i <= 1 ; i++){
                for(int j = -1 ; j <= 1; j++){
                    for(int k = -1; k <= 1; k++){

                        //Evite qu'on déborde sur le tableau 3D
                        if(
                                   gridLocation.y + j >= 0 && gridLocation.y + j < Globals.NB_DIM_Y // 1er Dim
                                && gridLocation.x + k >= 0 && gridLocation.x + k < Globals.NB_DIM_X // 2ème Dim
                                && gridLocation.z + i >= 0 && gridLocation.z + i < Globals.NB_DIM_Z // 3ème Dim
                         ){

                            //Si le cube est vide
                            if(dataGrid[(int)gridLocation.z + i][(int)gridLocation.y + j][(int)gridLocation.x + k] == 0 ){
                                
                                //on l'ouvre
                               dataGrid[(int)gridLocation.z + i][(int)gridLocation.y + j][(int)gridLocation.x + k] = openCubeCode;
                               
                               //Ensuite on réapelle la fonction avec ce cube là
                               deleteAdjacentCube(new Vector3f((int)gridLocation.x + k, (int)gridLocation.y + j, (int)gridLocation.z + i));
                            }
                            
                            //Si le cube à une valeurs plus grande que 0, on le dévoile.
                            else if(dataGrid[(int)gridLocation.z + i][(int)gridLocation.y + j][(int)gridLocation.x + k] > 0
                                    && dataGrid[(int)gridLocation.z + i][(int)gridLocation.y + j][(int)gridLocation.x + k] <= 26
                                    ){
                                
                                dataGrid[(int)gridLocation.z + i][(int)gridLocation.y + j][(int)gridLocation.x + k] *= -1;
                                
                            }

                        }

                    }
                }
            }
    }
    
    /**
     * Remplit le tableau de données de mines aléatoirement
     * 
     */
    private void fillMineGrid(){
        
        //Variables
        Random rnd = new Random();
        
        LinkedList<Vector3f> temps = new LinkedList<>();
        
        //Copiage du tableau 3 Dimension dans une liste
        for(int i = 0; i < Globals.NB_DIM_Z; i++){
            for(int j = 0; j < Globals.NB_DIM_Y; j++){
                for(int k = 0; k < Globals.NB_DIM_X ; k++){
                    temps.add(new Vector3f(k, j, i));
                }   
            }
        }
        
        //Ajout de mines aléatoirement
        for(int i = 0; i < Globals.NB_MINES; i++){
            int rand = rnd.nextInt(temps.size());
            dataGrid[(int)temps.get(rand).z][(int)temps.get(rand).y][(int)temps.get(rand).x] = mineCode;
            calculateValuesAround(temps.get(rand));
            temps.remove(rand);
        }
    }
    
    /**
     * Calcule les valeurs des cubes adjacent à la mine en question.
     * @param gridLocation Vecteur composant la position (SUR LE TABLEAU -> INT) de la mine
     *
     */
    private void calculateValuesAround(Vector3f gridLocation){

        if(Globals.USE_DIAGONALS){
            for(int i = -1 ; i <= 1 ; i++){
                for(int j = -1 ; j <= 1; j++){
                    for(int k = -1; k <= 1; k++){

                        //Evite qu'on déborde sur le tableau 3D
                        if(
                                   gridLocation.x + k >= 0 && gridLocation.x + k < Globals.NB_DIM_X // 1er Dim
                                && gridLocation.y + j >= 0 && gridLocation.y + j < Globals.NB_DIM_Y // 2ème Dim
                                && gridLocation.z + i >= 0 && gridLocation.z + i < Globals.NB_DIM_Z // 3ème Dim
                         ){

                            if(dataGrid[(int)gridLocation.z + i][(int)gridLocation.y + j][(int)gridLocation.x + k] != mineCode){
                               dataGrid[(int)gridLocation.z + i][(int)gridLocation.y + j][(int)gridLocation.x + k] ++;
                            }

                        }

                    }
                }
            }
        }else{
            
            /*
             * Sans diagonales
             */
            
            if((int)gridLocation.y + 1 < Globals.NB_DIM_Y)
                dataGrid[(int)gridLocation.x ][(int)gridLocation.y + 1][(int)gridLocation.z] ++;
            
            if((int)gridLocation.x - 1 >= 0)
                dataGrid[(int)gridLocation.x -1][(int)gridLocation.y ][(int)gridLocation.z] ++;
            
            if((int)gridLocation.y + 1 < Globals.NB_DIM_Y)
                dataGrid[(int)gridLocation.x ][(int)gridLocation.y +1][(int)gridLocation.z] ++;
            
            if((int)gridLocation.x + 1 < Globals.NB_DIM_X)
                dataGrid[(int)gridLocation.x +1][(int)gridLocation.y][(int)gridLocation.z] ++;
            
            if((int)gridLocation.z + 1 < Globals.NB_DIM_Z)
                dataGrid[(int)gridLocation.x ][(int)gridLocation.y ][(int)gridLocation.z +1] ++;
            
            if((int)gridLocation.z -1 >= 0)
                dataGrid[(int)gridLocation.x ][(int)gridLocation.y ][(int)gridLocation.z -1] ++;
        }
    }
    
    
    /**
     * Supprime un cube.
     */
    public void deleteCube(int x,int y,int z){
        dataGrid[y][x][z] = openCubeCode;
    }

    public int[][][] getDataGrid() {
        return dataGrid;
    }

    public void setDataGrid(int[][][] mineGrid) {
        this.dataGrid = mineGrid;
    }
    
}
