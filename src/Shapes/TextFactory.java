/********************************************************************
Programme    : Demineur 3D TPI 2014 
Fichier      : TextFactory.java 
---------------------------------------------------------------------
Auteur       : Ferreira Venancio Diogo  
Compilateur  : JDK 7    
Date         : 14.05.2014
Version      : 1.0
---------------------------------------------------------------------
Description  : Cette classe  affiche du texte en 3D.
*********************************************************************/
package Shapes;

import Controllers.LabelControl;
import Globals.Globals;
import Grid.Grid;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.BillboardControl;
import com.jme3.scene.debug.WireBox;
import menu.elements.Label;
import menu.elements.Panel;
import menu.utils.Jme3DFont;

public class TextFactory extends ShapeFactory{
    
    private Panel panel;
    private SimpleApplication app;
    
    public TextFactory(AssetManager asset, SimpleApplication app,Node node){
        super(asset);
        init3DtextPanel(app,node);
        this.app = app;
        //Ceci est pour le cube en wireframe contenant le texte.
        mesh = new WireBox(Globals.CUBE_SIZE,Globals.CUBE_SIZE,Globals.CUBE_SIZE);
        material = new Material(assets, "Common/MatDefs/Light/Lighting.j3md");
        material.setBoolean("UseMaterialColors", true);
        mesh.setLineWidth(3);
    }
    
    /**
     * Initialise le panneau d'affichage text 3D
     */
    private void init3DtextPanel(SimpleApplication app,Node node){
        panel = new Panel(app.getCamera(), node, 5);
        app.getRootNode().attachChild(panel);
        Material simpleLightMaterial = new Material(assets, "Common/MatDefs/Light/Lighting.j3md");
        simpleLightMaterial.setColor("Diffuse", ColorRGBA.Blue);
        panel.setMenuMaterial(simpleLightMaterial);
        panel.setMenuFont(Jme3DFont.standardExtrudedFont);
        
        //Importante, sinon le texte est décalé
        panel.setLocalTranslation(0, 0, 0);
    }
    
    /**
     * Supprime le texte en 3D.
     */
    public void deleteTexts(){
        panel.detachAllChildren();
    }
    
    /**
     * Affiche du texte en 3D.
     * @param textToWrite texte à écrire
     * @param labelName nom de la géométrie
     * @param location position du texte
     * @param size taille du texte
     * @param useBillboard utilisé un controleur de type billboard
     */
    public void write3DText(String textToWrite,String labelName,Node node, Vector3f location,float size,boolean useBillboard){
        
        /*
         * WireCube
         */
        geometry = new Geometry("wire", mesh);
        material.setColor("Diffuse", ColorRGBA.Black); 
        geometry.setMaterial(material.clone());
        geometry.setLocalTranslation(location);
        panel.attachChild(geometry);
        
        
        
        Label label = new Label(textToWrite);
        panel.add(label);
        label.setHeight(size);
        label.setUserData("value", textToWrite);
        Material m = material.clone();
        m.setColor("Diffuse", debugColor(Integer.parseInt(textToWrite)));
        label.setMaterial(m);
        
        //Met le chiffre au centre
        location.y -= size+.1f;
        
        label.setLocalTranslation(location);
        
        // Ce contrôleur permet que le chiffre soit toujours en face de la caméra
        if(useBillboard)
            label.addControl(new BillboardControl());
        
        if(labelName != null)
            label.setName(labelName);
        
        label.addControl(new LabelControl(label,m,geometry,app.getCamera(),app));
        
    }
    
    /**
     * Définit une couleur pour chaque valeur du cube, à utiliser en débug.
     * @param value valeur
     */
    public ColorRGBA debugColor(int value){
        ColorRGBA color;

        if(value == 0)
            color = ColorRGBA.Green;
        else if(value == 1)
            color = ColorRGBA.Blue;
        else if(value == 2)
            color = ColorRGBA.Green;
        else if(value == 3)
            color = ColorRGBA.Red;
        else if(value == 4)
            color = ColorRGBA.Pink;
        else if(value == 5)
            color = ColorRGBA.Black;
        else if(value == Grid.mineCode)
            color = ColorRGBA.Yellow;
        else if(value < 0)
            color = ColorRGBA.Pink;
        else 
            color = ColorRGBA.Orange;
        
        return color;
    }
}
