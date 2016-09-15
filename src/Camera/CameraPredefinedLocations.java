/********************************************************************
Programme    : Demineur 3D TPI 2014 
Fichier      : CameraPredefinedLocations.java 
---------------------------------------------------------------------
Auteur       : Ferreira Venancio Diogo  
Compilateur  : JDK 7    
Date         : 14.05.2014
Version      : 1.0
---------------------------------------------------------------------
Description  : Cette classe gère des positions prédéfinit de la caméra.
*********************************************************************/
package Camera;

import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import java.util.HashMap;

public class CameraPredefinedLocations {
    
    private Camera cam;
    private HashMap<String,Vector3f> locations = new HashMap<>();
    private Vector3f reference;
    
    public CameraPredefinedLocations(Camera cam){
        this.cam = cam;
        
    }
    /**
     * Initialisations des position par rapport à une position.
     * @param  ref position référence
     */
    public void initLocs(Vector3f ref){
        this.reference = ref;
        locations.put("N", new Vector3f(reference.x, reference.y+30,reference.z-30));
        locations.put("S", new Vector3f(reference.x, reference.y+30, reference.z+30));
        locations.put("W", new Vector3f(reference.x-30, reference.y+30, reference.z));
        locations.put("E", new Vector3f(reference.x+30, reference.y+30, reference.z));
    }
    
    /**
     * Position la caméra à la position voulu
     * @param locationName nom de la position enregistré
     */
    public void changeCamToLocation(String locationName){
        cam.setLocation(locations.get(locationName));
        cam.lookAt(reference, Vector3f.UNIT_Y);
    }
    
}
