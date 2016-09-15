/********************************************************************
Programme    : Demineur 3D TPI 2014 
Fichier      : GuiAppState.java 
---------------------------------------------------------------------
Auteur       : Ferreira Venancio Diogo  
Compilateur  : JDK 7    
Date         : 14.05.2014
Version      : 1.0
---------------------------------------------------------------------
Description  : Cette classe gère l'interface graphique(Chargement XML + Events). 
*********************************************************************/
package States;

import Globals.Globals;
import static Globals.Globals.NB_DIM_X;
import static Globals.Globals.NB_DIM_Y;
import static Globals.Globals.NB_DIM_Z;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Button;
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import MineSweeper3D.MainApp;


public class GuiAppState extends AbstractAppState implements ScreenController{
    
    /*
     * Constantes
     */
    private final int           MAX_DIM = 12;
    private final int           MIN_DIM = 3;
    
    /*Variables*/
    private Nifty               nifty;
    private MainApp             mainApp;
    private String              message = "";
    
    
    @Override
    public void bind(Nifty nifty, Screen screen) {
         this.nifty = nifty;
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        mainApp     =(MainApp)app;
        
        /*Init de nifty*/
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
                app.getAssetManager(),
                app.getInputManager(),
                app.getAudioRenderer(),
                app.getGuiViewPort()
        );
        
        nifty = niftyDisplay.getNifty();
        nifty.registerScreenController(this);
        nifty.fromXml("Interface/OptionsPanel.xml", "optionsPanel");
        nifty.addXml("Interface/RestartPanel.xml");
        nifty.addXml("Interface/MessagePanel.xml");
        app.getGuiViewPort().addProcessor(niftyDisplay);
        
        showHelp();
    }
    
    /**
     * Mets les données de jeu à jour par rapport à l'interface.
     * /!\<b> Le jeu sera relancé !</b>/!\
     */
    public void updateValues(){
        /**
         * Contrôles de l'utilisateur
         */
        try{
            Globals.NB_DIM_X = Integer.parseInt(((TextField)nifty.getCurrentScreen().findNiftyControl("xInput", TextField.class)).getRealText());
            Globals.NB_MINES = Integer.parseInt(((TextField)nifty.getCurrentScreen().findNiftyControl("nbMinesInput", TextField.class)).getRealText());
            Globals.NB_DIM_Z = Integer.parseInt(((TextField)nifty.getCurrentScreen().findNiftyControl("zInput", TextField.class)).getRealText());
            Globals.NB_DIM_Y = Integer.parseInt(((TextField)nifty.getCurrentScreen().findNiftyControl("yInput", TextField.class)).getRealText());
        }catch(Exception e){
            System.err.println(e.toString());
            showMessagePanel("Valeur incorrecte !");
        }
        
        
        //Contrôle des dimensions
        if(Globals.NB_DIM_X < MIN_DIM){
            Globals.NB_DIM_X = MIN_DIM;
        }
        else if(Globals.NB_DIM_X > MAX_DIM){
            Globals.NB_DIM_X = MAX_DIM;
        }
        if(Globals.NB_DIM_Y < MIN_DIM){
            Globals.NB_DIM_Y = MIN_DIM;
        }
        else if(Globals.NB_DIM_Y > MAX_DIM){
            Globals.NB_DIM_Y = MAX_DIM;
        }
        if(Globals.NB_DIM_Z < MIN_DIM){
            Globals.NB_DIM_Z = MIN_DIM;
        }
        else if(Globals.NB_DIM_Z > MAX_DIM){
            Globals.NB_DIM_Z = MAX_DIM;
        }
        
        //Si la valeur des mines est 0, le programme choisi le nombre de mines
        if(Globals.NB_MINES <= 0 ){
            Globals.NB_MINES = (int)(NB_DIM_X * NB_DIM_Y * NB_DIM_Z * 1/8);   
        }
        else if(Globals.NB_MINES > Globals.NB_DIM_X * Globals.NB_DIM_Y * Globals.NB_DIM_Z){
            showMessagePanel("C'est trop de mines ! Tu peux en mettre " + (Globals.NB_DIM_X * Globals.NB_DIM_Y * Globals.NB_DIM_Z) + " au maximum");
            Globals.NB_MINES = (int)(NB_DIM_X * NB_DIM_Y * NB_DIM_Z * 1/8);
        }
        
        mainApp.getStateManager().getState(DeminerAppState.class).restartGame();
        
        //Mise à jour
        onStartScreen();
    }
    
    /**
     * Permet juste de dire OK pour incrémenter le temps.
     */
    public void playerReady(){
        Globals.PLAYER_READY = true;
        showOptions();
    }
    
    public void showHelp(){
        nifty.gotoScreen("messageScreenImage");
    }
    
    /**
     * Montre le panneau d'options.
     */
    public void showOptions(){
        nifty.gotoScreen("optionsPanel");
    }
    
    /**
     * Cache le panneau d'options et affiche le petit bouton.
     */
    public void hideOptions(){
        nifty.gotoScreen("gearsPanel");
    }
    
    /**
     * Affiche une boîte de dialogue Oui / Non.
     * @param msg message à afficher
     */
    public void showRestartGamePopUp(String msg){
        message = msg;
        nifty.gotoScreen("restartScreen");
    }
    
    /**
     * Affiche une boîte de dialogue.
     * @param msg message à afficher
     */
    public void showMessagePanel(String msg){
        message = msg;
        nifty.gotoScreen("messageScreen");
    }
    
    /**
     * Relance le jeu
     */
    public void relaunchGame(){
        showOptions();
        mainApp.getStateManager().getState(DeminerAppState.class).restartGame();
    }
    /**
     * Arrête le jeu
     */
    public void quit(){
        mainApp.exit();
    }

    @Override
    public void onStartScreen() {
        //Mets à jour les données de l'interface par rapport au données.
        switch (nifty.getCurrentScreen().getScreenId().toString()) {
            case "optionsPanel":
                TextField text = (TextField)nifty.getCurrentScreen().findNiftyControl("nbMinesInput", TextField.class);
                text.setText(""+Globals.NB_MINES);
                text = (TextField)nifty.getCurrentScreen().findNiftyControl("xInput", TextField.class);
                text.setText(""+Globals.NB_DIM_X);
                text = (TextField)nifty.getCurrentScreen().findNiftyControl("yInput", TextField.class);
                text.setText(""+Globals.NB_DIM_Y);
                text = (TextField)nifty.getCurrentScreen().findNiftyControl("zInput", TextField.class);
                text.setText(""+Globals.NB_DIM_Z);
                CheckBox chBox = (CheckBox)nifty.getCurrentScreen().findNiftyControl("useDiag", CheckBox.class);
                chBox.setChecked(Globals.USE_DIAGONALS);
                nifty.getCurrentScreen().findNiftyControl("btnCancel", Button.class).setFocus();
                break;
            case "restartScreen":
                Label lbl = (Label)nifty.getCurrentScreen().findNiftyControl("restartPanelMsg", Label.class);
                lbl.setText(message);
                break;
            case "messageScreen":
                Label lblMsg = (Label)nifty.getCurrentScreen().findNiftyControl("messagePanelMsg", Label.class);
                lblMsg.setText(message);
                break;
        }
    }

    @Override
    public void onEndScreen() {}
    
    @Override
    public void update(float tpf){
        if(!nifty.getCurrentScreen().getScreenId().equals("restartScreen")){
            //Mise à jour du temps de jeu
            ((Label)nifty.getCurrentScreen().findNiftyControl("timeLabel", Label.class)).setText("Time "+ mainApp.getStateManager().getState(DeminerAppState.class).getPlayer().getTime());
        }
    }
}
