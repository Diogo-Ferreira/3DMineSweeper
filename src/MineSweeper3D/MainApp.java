/********************************************************************
Programme    : Demineur 3D TPI 2014 
Fichier      : MainApp.java 
---------------------------------------------------------------------
Auteur       : Ferreira Venancio Diogo  
Compilateur  : JDK 7    
Date         : 14.05.2014
Version      : 1.0
---------------------------------------------------------------------
Description  : Classe Main du programme.
*********************************************************************/
package MineSweeper3D;

import States.DeminerAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.system.AppSettings;
import java.io.IOException;

public class MainApp extends SimpleApplication {
    
    private DeminerAppState deminerApp;

    public static void main(String[] args) throws IOException {
        MainApp app = new MainApp();
        app.setDisplayStatView(false);
        app.setDisplayFps(false);
        AppSettings appSets = new AppSettings(true);
        appSets.setTitle("Demineur 3D Ferreira Venancio Diogo TPI 2014 CEFF-Industrie (C)");
        appSets.setSettingsDialogImage("Textures/SplashDeminer3D.PNG");        
        appSets.setResolution(1360, 768);
        appSets.setVSync(true);
        appSets.setSamples(16);
        app.setSettings(appSets);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        //Init le jeu
        deminerApp = new DeminerAppState();
        stateManager.attach(deminerApp);
        //Couleur de fond
        viewPort.setBackgroundColor(ColorRGBA.White);
    }
    
    
    /**
     * Quite le jeu.
     */
    public void exit(){
        System.exit(0);
    }
}
