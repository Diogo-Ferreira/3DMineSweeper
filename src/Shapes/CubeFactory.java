/********************************************************************
Programme    : Demineur 3D TPI 2014 
Fichier      : CubeFactory.java 
---------------------------------------------------------------------
Auteur       : Ferreira Venancio Diogo  
Compilateur  : JDK 7    
Date         : 14.05.2014
Version      : 1.0
---------------------------------------------------------------------
Description  : Cette classe s'occupe de d'afficher des cubes.
*********************************************************************/
package Shapes;

import Globals.Globals;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import java.util.HashMap;

public class CubeFactory extends ShapeFactory{
    
    public CubeFactory(AssetManager asset){
        super(asset);
        mesh = new Box(Globals.CUBE_SIZE,Globals.CUBE_SIZE,Globals.CUBE_SIZE);
        material = new Material(assets, "Common/MatDefs/Light/Lighting.j3md");
    }
    
    /**
     * Affiche un cube
     *  @param name nom du cube
     *  @param node noeud pivot dans lequel on va afficher le cube
     *  @param location position du cube
     *  @param color couleur du cube
     *  @param data données à stocker dans le cube
     */
    public void mkStdCube(String name,Node node, Vector3f location, ColorRGBA color, HashMap<String,String> data){
        geometry = new Geometry(name, mesh);
        material.setColor("Diffuse", color);
        material.setBoolean("UseMaterialColors",true);
        material.setFloat("Shininess", 528f);
        /**
         * Attention à bien cloner !
         */
        geometry.setMaterial(material);
        geometry.setLocalTranslation(location);
        putDataIntoGeom(geometry, data);
        node.attachChild(geometry);
    }
    /** 
     * Affiche un cube avec un point d'interrogation.
     * @param name nom du cube
     * @param node noeud pivot dans lequel on va afficher le cube
     * @param location position du cube
     */
    public void mkMarkedCube(String name,Node node, Vector3f location){
        geometry = new Geometry(name, mesh);  
        material.setColor("Diffuse", ColorRGBA.White);
        material.setBoolean("UseMaterialColors",true);
        material.setFloat("Shininess", 128f);
        
        //faire un .clone() car sinon on va mélanger les couleurs
        geometry.setMaterial(material.clone());
        geometry.getMaterial().setTexture("DiffuseMap", assets.loadTexture("Textures/mark.jpg"));
        geometry.setLocalTranslation(location);
        node.attachChild(geometry);
    }
}
