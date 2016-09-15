/********************************************************************
Programme    : Demineur 3D TPI 2014 
Fichier      : LabelControl.java 
---------------------------------------------------------------------
Auteur       : Ferreira Venancio Diogo  
Compilateur  : JDK 7    
Date         : 14.05.2014
Version      : 1.0
---------------------------------------------------------------------
Description  : Cette classe gèrer l'état d'affichage des chiffres 3D.
*********************************************************************/
package Controllers;

import Globals.Globals;
import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import menu.elements.Label;

public class LabelControl extends AbstractControl{
    
    private Label label;
    private Geometry cage;
    private Camera cam;
    private SimpleApplication app;
    private Vector3f camLabelVect,pointerVector,direction,origin;
    private Material labelMat;
    private float DISTANCE = 5;
    
    public LabelControl(Label label,Material mat, Geometry cage,Camera cam,SimpleApplication app){
        this.label = label;
        this.cam = cam;
        this.cage = cage;
        this.app = app;
        this.labelMat = mat;
    }

    @Override
    protected void controlUpdate(float tpf) {
        
        if(Globals.USE_HIDING){
            
            /*
            *  Si le filtrage du curseur est activé, on va calculer
            * les chiffres les plus proches du curseur. Pour cela on va trouver la 
            * distance perpendiculaire par rapport au vecteur de direction du curseur
            * ou de la caméra.
            */

           origin    = cam.getWorldCoordinates(app.getInputManager().getCursorPosition(), 0.0f);
           direction = cam.getWorldCoordinates(app.getInputManager().getCursorPosition(), 0.3f);
           direction.subtractLocal(origin).normalizeLocal();
           DISTANCE = 3;


           //Calculs vectoriel (Regarder rapport vers page 19 : Recerche affichage)
           camLabelVect = origin.subtract(label.getWorldTranslation());

           float angleCamDirLabel = camLabelVect.angleBetween(direction);

           float length = FastMath.cos(angleCamDirLabel) * camLabelVect.length();

           pointerVector = direction.mult(length);

           float distance = camLabelVect.subtract(pointerVector).length();
           
           //Si le chiffre est trop loin, on le cache
           if(distance >= DISTANCE){
               label.setText(".");
           }

           else{
               label.setText((String) label.getUserData("value"));
           }
           
        }else{
            
            /*
             * Suivant la distance du chiffre par rapport à la caméra,
             * on décide de l'afficher ou non, ceci peut aider le joueur a voir
             * plus clair plus qu'un partie des chiffres seront "réduits".
             */
            
           float distanceFromCam = cam.getLocation().distance(label.getLocalTranslation());
        
            if((distanceFromCam >= 17 && distanceFromCam <= 25)|| distanceFromCam <= 6 ){
                label.setText(".");
            }

            else{
                label.setText((String) label.getUserData("value"));
            } 
        }
        
        /**
         * Affichage ou non des cages.
         */
        
        if(!Globals.USE_CAGES){
            cage.setCullHint(Spatial.CullHint.Always);
        }
        
        else {
            cage.setCullHint(Spatial.CullHint.Never);
        }
        
        
        
        /**
         * La librairie de texte3D à tendance à remplacer tout le matériel de ses
         * enfants par celui par défault lors d'un quelqu'on que changement. Une
         * solution rapide et remettre le matériel du texte à jour ici.
         */
        label.setMaterial(labelMat);
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {}
    
}
