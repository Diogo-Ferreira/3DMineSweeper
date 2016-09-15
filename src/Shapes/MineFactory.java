/********************************************************************
Programme    : Demineur 3D TPI 2014 
Fichier      : MineFactory.java 
---------------------------------------------------------------------
Auteur       : Ferreira Venancio Diogo  
Compilateur  : JDK 7    
Date         : 14.05.2014
Version      : 1.0
---------------------------------------------------------------------
Description  : Cette classe fabrique des mines 3D.
*********************************************************************/
package Shapes;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class MineFactory extends ShapeFactory{
    public MineFactory(AssetManager asset){
        super(asset);
        material = new Material(assets, "Common/MatDefs/Light/Lighting.j3md");
        material.setColor("Diffuse", ColorRGBA.Gray);
        material.setBoolean("UseMaterialColors",true);
        model = asset.loadModel("Models/Mine/Mine.j3o");
        model.setMaterial(material);
    }
    
    /**
     * Fabrique une mine.
     * @param name nom de la mine
     * @param node pivot 
     * @param loc position de la mine
     */
    public void mkMine(String name,Node node,Vector3f loc){
        Spatial sp = model.clone();
        sp.setName(name);
        sp.setLocalTranslation(loc);
        node.attachChild(sp);
    }
}
