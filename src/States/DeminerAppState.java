/********************************************************************
Programme    : Demineur 3D TPI 2014 
Fichier      : DeminerAppState.java 
---------------------------------------------------------------------
Auteur       : Ferreira Venancio Diogo  
Compilateur  : JDK 7    
Date         : 14.05.2014
Version      : 1.0
---------------------------------------------------------------------
Description  : Cette classe gère l'affichage et les intéractions du jeu.
*********************************************************************/
package States;

import Camera.CameraPredefinedLocations;
import Controllers.UserControl;
import Globals.Globals;
import Grid.Grid;
import Player.Player;
import Shapes.CubeFactory;
import Shapes.MineFactory;
import Shapes.TextFactory;
import Utils.Marker;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.util.SkyFactory;
import java.util.HashMap;
import MineSweeper3D.MainApp;


public class DeminerAppState extends AbstractAppState implements ActionListener,AnalogListener{
    
    /**
     * Pointeur sur la classe principale.
     */
    private MainApp app;
    /**
     * Noeud contenant les cubes.
     */
    private Node cubesNode;
    /**
     * Noeud contenant les mines.
     */
    private Node minesNode;
    /**
     * Noeud contenant le texte.
     */
    private Node textNode;
    /**
     * Classe joueur, contenant temps etc...
     */
    private Player player;
    /**
     * Grille de données du jeu.
     */
    private Grid grid;
    /**
     * Marqueur de couleur pour les cubes.
     */
    private Marker marker;
    /**
     * Usines de cubes.
     */
    private CubeFactory cFactory;
    /**
     * Usine de mines.
     */
    private MineFactory mFactory;
    /**
     * Usine de text 3D
     */
    private TextFactory tFactory;
    /**
     * Lumière du jeu, accroché à la camera.
     */
    private PointLight light = new PointLight();
    /**
     * Interface Graphique
     */
    private GuiAppState guiState;
    /**
     * Variable tampon pour compter le temps.
     */
    private float timeTemp = 0;
    /**
     * Etat du jeu.
     */
    private boolean gameRunning = true;
    /**
     * Position prédéfini de la camera.
     */
    private CameraPredefinedLocations camLocs = null;
    
    /**
     * Noeud de la chaseCamera
     */
    private Node chaseCamNode;
    /**
     * Camera tracking
     */
    private ChaseCamera chaseCam;
    

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        
        //Init de l'appState
        super.initialize(stateManager, app);
        this.app = (MainApp) app;
        
        
        /**
         * Init de la caméra (Mode tracking)
         */
        
        //Désactive la caméra par défaut
        this.app.getFlyByCamera().setEnabled(false);
        
        chaseCamNode =  new Node("chaseCam");
        chaseCam = new ChaseCamera(app.getCamera(), chaseCamNode, app.getInputManager());
        chaseCam.setSmoothMotion(false);
        chaseCam.setLookAtOffset(Vector3f.UNIT_Y.mult(-4));
        
        //On définit le bouton milieu pour faire un rotation de la caméra
        chaseCam.setToggleRotationTrigger(new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE));
        
        //Init variables
        cubesNode   = new Node("cubesNode");
        minesNode   = new Node("MinesNode");
        textNode    = new Node("textNode");
        cFactory    = new CubeFactory(this.app.getAssetManager());
        mFactory    = new MineFactory(this.app.getAssetManager());
        tFactory    = new TextFactory(this.app.getAssetManager(), (SimpleApplication) app,textNode);
        grid        = new Grid();
        guiState    = new GuiAppState();
        marker      = new Marker(this.app.getRootNode(),this.app.getAssetManager());
        player      = new Player();
        camLocs     = new CameraPredefinedLocations(app.getCamera());
        
        //Ajout du contrôleur de l'utilisateur (RayTrace2d) au noeud des cubes
        cubesNode.addControl(new UserControl(app.getInputManager(),cubesNode, app.getCamera()));
        
        //Ajoute l'interface graphique
        stateManager.attach(guiState);
        
        //Touches
        initKeys();
        
        //Init de la grille de jeu
        initGameGrid();
        
        //Affichage
        showCubeGrid();
        
        //Attachements
        this.app.getRootNode().attachChild(cubesNode);
        this.app.getRootNode().attachChild(minesNode);
        this.app.getRootNode().attachChild(chaseCamNode);
        this.app.getRootNode().addLight(light);
        
        //Lumières
        light.setRadius(90);
        
        // Distance de la caméra par rapport au centre
        chaseCam.setMaxDistance(150);
    }
    
    /**
     * Initialise les events.
     */
    public void initKeys(){
        /*
         * Event pour l'ajout du "?"
         */
        app.getInputManager().addMapping("addMark", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        app.getInputManager().addListener(this, "addMark");
        
        /**
         * Event pour le clic du joueur
         */
        app.getInputManager().addMapping("leftClick", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        app.getInputManager().addListener(this, "leftClick");
        
        /**
         * Event pour les déplacements latéraux de la caméra
         */
        app.getInputManager().addMapping("w", new KeyTrigger(KeyInput.KEY_W));
        app.getInputManager().addListener(this, "w");
        app.getInputManager().addMapping("a", new KeyTrigger(KeyInput.KEY_A));
        app.getInputManager().addListener(this, "a");
        app.getInputManager().addMapping("s", new KeyTrigger(KeyInput.KEY_S));
        app.getInputManager().addListener(this, "s");
        app.getInputManager().addMapping("d", new KeyTrigger(KeyInput.KEY_D));
        app.getInputManager().addListener(this, "d");
        
        /**
         * Event pour montre les cages
         */
        app.getInputManager().addMapping("showCages", new KeyTrigger(KeyInput.KEY_C));
        app.getInputManager().addListener(this, "showCages");
        
        /**
         * Event pour le filtrage par la souris
         */
        app.getInputManager().addMapping("useHiding", new KeyTrigger(KeyInput.KEY_H));
        app.getInputManager().addListener(this, "useHiding");
    }
    
    /**
     * Génère une skyBox.
     */
    private void initSky(){
        app.getRootNode().attachChild(SkyFactory.createSky(app.getAssetManager(),
                app.getAssetManager().loadTexture("Textures/Degradee2.png"),
                app.getAssetManager().loadTexture("Textures/Degradee2.png"),
                app.getAssetManager().loadTexture("Textures/Degradee2.png"),
                app.getAssetManager().loadTexture("Textures/Degradee2.png"),
                app.getAssetManager().loadTexture("Textures/Degradee2.png"),
                app.getAssetManager().loadTexture("Textures/Degradee2.png"))
         );
    }
    
    /**
     * Redémarre le jeu
     */
    public void restartGame(){
        minesNode.detachAllChildren();
        tFactory.deleteTexts();
        textNode.detachAllChildren();
        marker.deleteMark();
        player.setTime(0);
        initGameGrid();
        gameRunning = true;
        showCubeGrid();
    }
    
    /**
     * Init la grille de jeu
     */
    public void initGameGrid(){
        cubesNode.detachAllChildren();
        grid.initGrid();
        
        /**
         * Cube de réfence au millieu pour les position des caméras.
         */
        float value =  (Globals.CUBE_SIZE + 2*Globals.CUBE_MARGIN) * Globals.NB_DIM_X;
        Vector3f loc = new Vector3f(value/2,value/2,value/2);
        camLocs.initLocs(loc);
    }

    
    /**
     * Contrôle le jeu.
     * @param clickedCube cube touché par l'utilisateur.
     */
    public void checkGame(Geometry clickedCube) {
        
        int x = Integer.parseInt(clickedCube.getUserData("x").toString());
        int y = Integer.parseInt(clickedCube.getUserData("y").toString());
        int z = Integer.parseInt(clickedCube.getUserData("z").toString());
        
        
        //Controle si c'est une bombe.
        if(grid.getDataGrid()[z][y][x] == Grid.mineCode){
            
            //Arrête de rafraichir le jeu
            gameRunning = false;
            
            //Enlève les marques
            marker.deleteMark();
            
            //Affiche les mines
            showMines();
            
            //Perdue.... affichage des bombes, demande si le joueur veut recommencer...
            guiState.showRestartGamePopUp("Dommage, Rejouer ?");
        }else{
            grid.deleteAdjacentCube(new Vector3f(x, y, z));
            
            if(grid.isDemined()){
                
                //Enlève les marques
                marker.deleteMark();
                
                //Affiche les mines
                showMines();
                
                //Ainsi on ne voit plus que les mines quand on gagne
                cubesNode.detachAllChildren();
                
                //Perdu.... affichage des bombes, demande si le joueur veut recommencer...
                guiState.showRestartGamePopUp("Gagné, vous avez mis "+player.getTime()+ " Secondes pour terminer ! Rejouer ?");
                
                
            }else{
                //Affichage et mise à jour des cubes
                showCubeGrid();
            }
            
        }
        
        
    }
    /**
     * Affiche les mines.
     */
    private void showMines(){
        
        //Stocke les noms des cubes à supprimer pour faire placer aux mines
        String indexes[] = new String[Globals.NB_MINES];
        
        int l =0;
        for(int i = 0; i < cubesNode.getChildren().size(); i++){
            int x = Integer.parseInt(cubesNode.getChild(i).getUserData("x").toString());
            int y = Integer.parseInt(cubesNode.getChild(i).getUserData("y").toString());
            int z = Integer.parseInt(cubesNode.getChild(i).getUserData("z").toString());
            
            
            if(grid.getDataGrid()[z][y][x] == Grid.mineCode){
                
                /**
                 * Pour supprimer le cube, on garde son nom en mémoire dans un tableau,
                 * ceci car, on ne peut supprimer un élément d'une liste lorsque on la
                 * parcourt.
                 */
                
                Vector3f loc = cubesNode.getChild(i).getLocalTranslation();
                
                if(cubesNode.getChild(i).getName() == null){
                    System.err.println("DeminerAppState::ShowMines");
                }
                indexes[l] = cubesNode.getChild(i).getName();
                mFactory.mkMine("Mine", minesNode, loc);
                l++;
            }
        }
        
        //Suppresseion des cubes dans le noeud
        for(int i = 0; i < indexes.length;i++){
            cubesNode.detachChildNamed(indexes[i]);
        }
    }
    
    
    /**
     * Affiche la grille de cube, suivant les dimensions définit dans la 
     * classe Globals, cette fonction s'occupe aussi de mettre à jour l'états
     * des cubes, et d'afficher les textes 3D, suivant les données contenu dans
     * la dataGrid de la classe Grid.
     * 
     * /!\ Cette boucle est gourmande, ne l'appeler que quand cela est vraiment,
     * nécéssaire ! /!\
     */
    public void showCubeGrid(){
        
        for(int i = 0; i < Globals.NB_DIM_Z; i++){
            
            for(int j = 0; j < Globals.NB_DIM_Y; j++){
                
                for(int k = 0; k < Globals.NB_DIM_X ; k++){
                    
                    
                    //Vérifie si un cube à changer de valeur
                    if(cubesNode.getChild("Cube"+i+"#"+j+"#"+k) != null){
                        //Si c'est le cas, le cube est supprimer afin de se refaire afficher juste après
                        if(Integer.parseInt(cubesNode.getChild("Cube"+i+"#"+j+"#"+k).getUserData("value").toString()) != grid.getDataGrid()[i][j][k]){
                            cubesNode.detachChildNamed("Cube"+i+"#"+j+"#"+k);
                        }   
                    }

                    // Regarde si le cube en question  n'existe déjà pas, et le texte aussi    
                    if(cubesNode.getChild("Cube"+i+"#"+j+"#"+k) == null
                            && grid.getDataGrid()[i][j][k] != Grid.openCubeCode
                            && app.getRootNode().getChild("Text"+i+"#"+j+"#"+k) == null){    
                        
                        /*
                         * Données du cube (Position, si c'est une mine)
                         */
                        HashMap<String,String> data = new HashMap<>();

                        data.put("x",Integer.toString(k));
                        data.put("y",Integer.toString(j));
                        data.put("z",Integer.toString(i));
                        data.put("value",Integer.toString(grid.getDataGrid()[i][j][k]));
                        data.put("isMarked", "false");

                        //Définit une couleur suivant la valeur du cube (Debug seulement)
//                        ColorRGBA color = debugColor(grid.getDataGrid()[i][j][k]);
                        
                        //Couleur pour chaque cube
                        ColorRGBA color = ColorRGBA.LightGray;

                        /*
                         * Positions (Centrées)
                         */
                        float value =  Globals.CUBE_SIZE + 2* Globals.CUBE_MARGIN;
                        float offsetX = (Globals.NB_DIM_X) * value / 2 - Globals.CUBE_SIZE; 
                        float offsetY = (Globals.NB_DIM_Y) * value / 2 - Globals.CUBE_SIZE; 
                        float offsetZ = (Globals.NB_DIM_Z) * value / 2 - Globals.CUBE_SIZE; 
                        Vector3f loc = new Vector3f(offsetX-k* value,offsetY-j * value,offsetZ-i * value);
                        
                        if(grid.getDataGrid()[i][j][k] < 0){
                            //Fabrication du Texte 3D
                            tFactory.write3DText(""+(grid.getDataGrid()[i][j][k]*-1), "Text"+i+"#"+j+"#"+k, cubesNode, loc,0.3f, true);
                            
                            //On retire le cube
                            cubesNode.detachChildNamed("Cube"+i+"#"+j+"#"+k);
                        }
                       
                        else{
                            //Fabrication du cube
                            cFactory.mkStdCube("Cube"+i+"#"+j+"#"+k, cubesNode, loc, color,data);
                        }
                    }
                }   
            }
        }
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
            color = ColorRGBA.Cyan;
        else if(value == 3)
            color = ColorRGBA.Red;
        else if(value == 4)
            color = ColorRGBA.Orange;
        else if(value == Grid.mineCode)
            color = ColorRGBA.Yellow;
        else if(value < 0)
            color = ColorRGBA.Pink;
        else 
            color = ColorRGBA.Black;
        
        return color;
    }

    @Override
    public void update(float tpf) {
        /*
         * TEMPS
         */
        
        if(Globals.PLAYER_READY){
            timeTemp += tpf;
        
            if(timeTemp >= 1){
                timeTemp = 0;
                player.tickTime();
            }  
        }
       
        
        /**
         * Surlignage du cube
         */
        Geometry g = cubesNode.getControl(UserControl.class).getCurrentPointingCube();
        
        if(g != null && app.getInputManager().isCursorVisible()){
            marker.HighlightSelectedGeometry(g);
        }else{
            marker.deleteHighLight();
        }
        
        //Mise à jour de la postion de la lumière
        light.setPosition(app.getCamera().getLocation());
        
        
        /*
         * La fonction showCubeGrid() à été déplacer dans checkGame, car il est,
         * plus logique et moins gourmand de l'appeler à chaque action de
         * l'utilisateur, que de l'appeler ici.
         */
    }
    
    /**
     * Actions de jeu digital.
     */
    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        
        if(name.equals("addMark") && !isPressed && Globals.PLAYER_READY){
            Geometry g = cubesNode.getControl(UserControl.class).getCurrentPointingCube();
            if(g != null && gameRunning){
                marker.markGeometry(g);
            }
        }
        
        else if(name.equals("leftClick") && isPressed && Globals.PLAYER_READY){
            Geometry g = cubesNode.getControl(UserControl.class).getCurrentPointingCube();
            if(g != null && gameRunning ){
                
                //Evite qu'on puisse selectionner un cube avec un "?" dessus
                if(marker.getMarkCubesNode().getChild(g.getName()) == null){
                    checkGame(g);
                }
                
            }
        }
        
        else if(name.equals("showCages") && !isPressed){
            Globals.USE_CAGES = !Globals.USE_CAGES;
        }
        else if(name.equals("useHiding") && !isPressed){
            Globals.USE_HIDING = !Globals.USE_HIDING;
        }
    }
    /**
     * Déplacements caméra (Events analogiques).
     */
    @Override
    public void onAnalog(String name, float value, float tpf) {
        
        switch (name) {
            case "w":
                chaseCamNode.move(0, 10f*tpf,0);
                break;
            case "s":
                chaseCamNode.move(0, -10f*tpf,0);
                break;
            case "a":
                chaseCamNode.move(app.getCamera().getLeft().mult(10f * tpf));
                break;
            case "d":
                chaseCamNode.move(app.getCamera().getLeft().mult(-10f * tpf));
                break;
        }
    }

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean isGameRunning() {
        return gameRunning;
    }

    public void setGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }
}
