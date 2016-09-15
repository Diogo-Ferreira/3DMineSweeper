/********************************************************************
Programme    : Demineur 3D TPI 2014 
Fichier      : ShapeFactory.java 
---------------------------------------------------------------------
Auteur       : Ferreira Venancio Diogo  
Compilateur  : JDK 7    
Date         : 14.05.2014
Version      : 1.0
---------------------------------------------------------------------
Description  : Classe abstraite pour toute classe affichange une forme 3D.
*********************************************************************/
package Shapes;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import java.util.HashMap;

public abstract class ShapeFactory {
    protected Geometry geometry;
    protected Material material;
    protected Mesh mesh;
    protected AssetManager assets;
    protected Spatial model;
    
    public ShapeFactory(AssetManager asset){
        assets = asset;
    }
    
    /**
     * Introduit les données dans la géométrie.
     * @param g Géometrie ou on veut mettre les données
     * @param data données
     */
    public void putDataIntoGeom(Geometry g, HashMap<String,String> data){
        
        if(data != null){
            for(String s : data.keySet()){
                g.setUserData(s, data.get(s));   
            }
        }
        
    }
}
