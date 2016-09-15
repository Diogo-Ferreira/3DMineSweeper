/********************************************************************
Programme    : Demineur 3D TPI 2014 
Fichier      : Marker.java 
---------------------------------------------------------------------
Auteur       : Ferreira Venancio Diogo  
Compilateur  : JDK 7    
Date         : 14.05.2014
Version      : 1.0
---------------------------------------------------------------------
Description  : Cette classe s'occupe de mettre en évidence les cubes.
*********************************************************************/
package Utils;

import Shapes.CubeFactory;
import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

public class Marker {
    
    private Node highlightCubeNode = new Node("highLightCubeNode");
    private Node markCubesNode     = new Node("markedCubes");
    private CubeFactory cFactory   = null;
    
    
    public Marker(Node rootNode, AssetManager asset){
        rootNode.attachChild(highlightCubeNode);
        rootNode.attachChild(markCubesNode);
        cFactory = new CubeFactory(asset);
    }
    /**
     * Enlève la surbrillance.
     */
    public void deleteHighLight(){
        highlightCubeNode.detachAllChildren();
    }
    
    /**
     * Enlève toutes les marques.
     */
    public void deleteMark(){
        markCubesNode.detachAllChildren();
    }
    
    /**
     * Mets un cube en évidence à l'aide d'une couleur, le cube
     * n'est pas touché, la fonction réecrir un cube juste par dessus
     * à peine plus grand, un seul cube peut être mis en évidence à la fois.
     * 
     * @param g la géométrie du cube à mettre en évidence
     */
    public void HighlightSelectedGeometry(Geometry g){
        highlightCubeNode.detachAllChildren();
        cFactory.mkStdCube("HighCube", highlightCubeNode, g.getLocalTranslation(), ColorRGBA.Green, null);
        highlightCubeNode.getChild("HighCube").scale(1.01f);
    }
    

    /**
     * Marque un cube à l'aide d'une texture, Le cube sujet n'est
     * pas affécté car la fonction réecrir un cube juste par dessus
     * qui est à peine plus grand, Si le cube est déjà marquer, il est
     * simplement démarquer.
     * 
     * @param g géométrie à marquer.
     */
    public void markGeometry(Geometry g){
        
        boolean isAlreadyIn = false;
        
        //Recherche si le cube n'est déjà pas marqué
        if(markCubesNode.getChild(g.getName()) != null){
            
            //Si c'est le cas, on l'enlève
            markCubesNode.detachChildNamed(g.getName());
            isAlreadyIn = true;
        }
        
        //Si il n'est pas marqué, on le marque.
        if(!isAlreadyIn){
            cFactory.mkMarkedCube(g.getName(), markCubesNode, g.getLocalTranslation());
            markCubesNode.getChild(g.getName()).scale(1.02f);
        }
    }

    public Node getHighlightCubeNode() {
        return highlightCubeNode;
    }

    public void setHighlightCubeNode(Node highlightCubeNode) {
        this.highlightCubeNode = highlightCubeNode;
    }

    public Node getMarkCubesNode() {
        return markCubesNode;
    }

    public void setMarkCubesNode(Node markCubesNode) {
        this.markCubesNode = markCubesNode;
    }
}
