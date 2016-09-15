/********************************************************************
Programme    : Demineur 3D TPI 2014 
Fichier      : UserControl.java 
---------------------------------------------------------------------
Auteur       : Ferreira Venancio Diogo  
Compilateur  : JDK 7    
Date         : 14.05.2014
Version      : 1.0
---------------------------------------------------------------------
Description  : Cette classe s'occupe de récupérer le cube pointé
*              par le curseur de la souris
*********************************************************************/
package Controllers;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;

public class UserControl extends AbstractControl {
    
    private Node                targetNode;
    private Camera              cam;
    private CollisionResults    results;
    private Geometry            currentPointingCube;
    private InputManager        input;

    
    public UserControl(InputManager input,Node targetNode,Camera cam){
        this.cam = cam;
        this.input = input;
        this.targetNode = targetNode;
    }
    @Override
    protected void controlUpdate(float tpf) {   
        currentPointingCube = rayTrace2D();
    }
    /**
     * Calcule l'élement pointer par le curseur de la souris
     */
    public Geometry rayTrace2D(){
        Vector3f origin    = cam.getWorldCoordinates(input.getCursorPosition(), 0.0f);
        Vector3f direction = cam.getWorldCoordinates(input.getCursorPosition(), 0.3f);
        direction.subtractLocal(origin).normalizeLocal();
        
        Ray ray = new Ray(origin, direction);
        results = new CollisionResults();
        
        targetNode.collideWith(ray, results);
        CollisionResult result = results.getClosestCollision();
        
        if(result != null){
            return result.getGeometry();
        }
        return null;

    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {}

    public Geometry getCurrentPointingCube() {
        return currentPointingCube;
    }

    public void setCurrentPointingCube(Geometry currentPointingCube) {
        this.currentPointingCube = currentPointingCube;
    }
}