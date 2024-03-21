package ui;

import mancala.MancalaDataStructure;
import mancala.GameRules;
import mancala.AyoRules;
import mancala.KalahRules;
import mancala.MancalaGame;
import mancala.Player;
//import mancala.Pit;//not currently in use
//import mancala.Store;//not currently in use
import mancala.PitNotFoundException;
// import mancala.NoSuchPlayerException;//not currently in use
import mancala.InvalidMoveException;
import mancala.GameNotOverException;
import mancala.Saver;
import mancala.UserProfile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.LineBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.Border;
import javax.swing.border.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;

import java.util.ArrayList;
import java.util.List;

public class MancalaGUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private MancalaGame game;
    private int currentPlayer=1;
    private int ruleSet=1;//when 1 kalah rules used, when 2 ayo rules used
    private KalahRules kRules;
    private AyoRules aRules;
    private Player player1;
    private Player player2;
    private Saver saver;
    private GameRules rules;
    private MancalaDataStructure data;
    private int clickedPitNumber = -1; 
    private String errorMsg="";
    private boolean onLoadScreen=false;

    private String saveFileName = "savedGame.ser";

    public MancalaGUI() {
        setUpWithRuleSet();      
        
        initializeUI();
    }
    private void setUpWithRuleSet(){//This is so we can re do this part of the set up depending on the rules set the player chose
        if (onLoadScreen){//if we are loading a game this will be true

        }else{
            game = new MancalaGame();
            kRules = new KalahRules(); 
            aRules = new AyoRules();
            
            saver = new Saver();
            if (ruleSet==1){//set up the game with kalah rules
                data = kRules.getDataStructure();//will auto set to kRules and we will change the data structure to aRules if this is needed      
                game.setBoard(kRules);
                Player player1 = new Player("Player1");
                Player player2 = new Player("Player2");
                game.setPlayers(player1, player2);
                //kRules.registerPlayers(player1, player2);//This is called threw game.setPlayers 
                game.startNewGame();
            }else if (ruleSet==2){//set up the game with Ayo rules
                data = aRules.getDataStructure();//will auto set to kRules and we will change the data structure to aRules if this is needed      
                game.setBoard(aRules);
                Player player1 = new Player("Player1");
                Player player2 = new Player("Player2");
                game.setPlayers(player1, player2);
                //kRules.registerPlayers(player1, player2);//This is called threw game.setPlayers 
                game.startNewGame();
            }
        }
        onLoadScreen=false;
 
    }

    private void initializeUI() {
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Mancala Game");  

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        //set up each page we have in the game
        createWelcomeScreen();
        createSetUpScreen();
        createLoadGameScreen();
        createBoard();

        cardLayout.show(cardPanel, "welcomeScreen");//set the current screen to welcome

        add(cardPanel);

        
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addPitMouseListeners(JPanel boardPanel) {
        Component[] components = boardPanel.getComponents();

        for (int i = 0; i < components.length; i++) {
            Component component = components[i];
            
            //if we what the JPanel to have a button attached to it
            if ((component instanceof JPanel && isPitPanel(i))||(component instanceof JPanel && i==(components.length-1))||(component instanceof JPanel && i==(components.length-2))) {
                //i will be 2 - 7 for pits number 12-7
                //i will be 12 - 17 for pits number 1-6
                if (i==(components.length-1)){//if it is the JPanel in the bottom right corrner
                    JPanel backButton = (JPanel) component;

                    backButton.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                                cardLayout.show(cardPanel, "welcomeScreen");
                            }
                    });
                }else if (i==(components.length-2)){//if it is the JPanel one to the left of the bottom right corrner we will use it as the save button
                    JPanel saveButton = (JPanel) component;

                    saveButton.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            try {
                                File assetsDir = new File("assets");
                                if (!assetsDir.exists()) {//check if there is a assest folder allready
                                    assetsDir.mkdirs();//if there is no folder we will just make a new one
                                }
                    
                                saver.saveObject(game, saveFileName);
                                //System.out.println("File saved to: " + new File(assetsDir, saveFileName).getAbsolutePath());
                            } catch (IOException e2) {
                                e2.printStackTrace(); // Handle the exception appropriately (e.g., show an error message)
                            }
                        }
                    });
                }else{//if we have a pit
                    int pitNumber = indexToPit(i);
                    boolean Clicked = false;
                    JPanel pitPanel = (JPanel) component;

                    pitPanel.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            clickedPitNumber=pitNumber;
                            int numStonesToMove = getNumMovingStones(pitNumber);
                            if (numStonesToMove>0){//if there is a stone in the pit the usr selected
                                //will be -1 for error, otherwise the number of stones added
                                int stonesBeingAddedToStore = checkValidMove(pitNumber);
                                if (stonesBeingAddedToStore>=0){//if the usr selected a valid more
                                    recallCreateBoard(pitNumber,numStonesToMove);
                                    
                                    setVisible(true);


                                }else{//tryed to move opponets pit
                                    errorMsg = "You are trying to move a pit that does not belong to you please select a diffrent one";;
                                }
                            }else{
                                //display error msg saying no stones in pit
                                errorMsg = "The pit you clicked has no stones in it!";
                            }

                        }
                    });
                }
                
            }
        }
    }

    private void recallCreateBoard(int pitNumber,int numStonesToMove) {
        //After each turn is made control will be passed back to this function

        //Remove the current Screen as this will be regenerated when we call createBoard with a updated display
        cardPanel.remove(3);        
        cardPanel.revalidate();//refresh the panel
        cardPanel.repaint();//refresh the panel
        setVisible(true);
        
        if (game.isGameOver()){//if the game has ended
            endGame();
        }else{//if the game is not over
            cycleTurns(pitNumber,numStonesToMove);//This will change whose turn it is baised on rule set and game condutions 
            createBoard();
        }
        
              
    }
    private void cycleTurns(int pitNumber,int numStonesToMove){
        if (ruleSet==1){//kalah is the game mode selected

            //player 1 moving and ends on a store
            if ((game.getCurrentPlayer() == game.getPlayers().get(0))&&(numStonesToMove+pitNumber)==7){
                //do nothing of note as the first player will get to go agin 
                errorMsg = game.getCurrentPlayer()+" will get to go again as there turn ended on there store.";//set to display later
                //if the Second player is moving and will end with a stone in there pit
            }else if ((game.getCurrentPlayer() == game.getPlayers().get(1))&&(numStonesToMove+pitNumber)==13){
                //do nothing of note as the second player will get to go agin 
                errorMsg = game.getCurrentPlayer()+" will get to go again as there turn ended on there store.";//set to display later
            }else{//if the move does not end on a store
                //change whos turn it is
                if (game.getCurrentPlayer() == game.getPlayers().get(0)){
                    game.setCurrentPlayer(game.getPlayers().get(1));
                }else if (game.getCurrentPlayer() == game.getPlayers().get(1)){
                    game.setCurrentPlayer(game.getPlayers().get(0));
                }
            }
        }else if(ruleSet==2){//ayo is the game mode selected
            //we always want to change whos turn it is for ayo is there is no second turn rule
            if (game.getCurrentPlayer() == game.getPlayers().get(0)){
                    game.setCurrentPlayer(game.getPlayers().get(1));
                }else if (game.getCurrentPlayer() == game.getPlayers().get(1)){
                    game.setCurrentPlayer(game.getPlayers().get(0));
                }
        }
    }
    private void endGame(){
        JPanel boardPanel = new JPanel(new GridLayout(3, 10));
        cardPanel.add(boardPanel, "end");
        cardLayout.show(cardPanel, "end");//set the current screen to game
        displayBoard(boardPanel); 
    }

    private void createBoard() {
        JPanel boardPanel = new JPanel(new GridLayout(3, 10));
        cardPanel.add(boardPanel, "game");
        cardLayout.show(cardPanel, "game");//set the current screen to game
        onLoadScreen=false;

        //boardPanel.setBackground(new Color(255, 255, 255));  // Light blue background
        //displayBoard will return a pit that the player clicked on (may not be valid)
        displayBoard(boardPanel); 
        

        
        
        //buttons for all of the pits
        addPitMouseListeners(boardPanel);   
    }

    private int checkValidMove (int moveingFrom){
        int stonesBeingAddedToStore = -1;
        try {
            int playerNum = game.getCurrentPlayerNumber();
            if (ruleSet==1){//kalah is the game mode selected
                stonesBeingAddedToStore=kRules.moveStones(moveingFrom,playerNum);
            }else if(ruleSet==2){//ayo is the game mode selected
                stonesBeingAddedToStore=aRules.moveStones(moveingFrom,playerNum);
            }           
        }catch (InvalidMoveException e) {// Handle the exception
            if (game.getCurrentPlayer() == game.getPlayers().get(0)){//player1
                errorMsg="Try clicking on a pit numbered 1-6";
            }else if (game.getCurrentPlayer() == game.getPlayers().get(1)){//player2
                errorMsg="Try clicking on a pit numbered 7-12";
            }
        }
        return stonesBeingAddedToStore;
    }

    private int getNumMovingStones(int moveingFrom){
        int numStones = 0;//set to 0 and will only be changed if there are more stones in the pit
        try{
            numStones=game.getNumStones(moveingFrom);
        }catch (PitNotFoundException e) {// Handle the exception  even though this should not be possible with this form of UI        
            //System.out.print(e.getMessage()+" Try entering a number beteween ");
            if (game.getCurrentPlayer() == game.getPlayers().get(0)){//player1
                //System.out.print("1 and 6.");
            }else if (game.getCurrentPlayer() == game.getPlayers().get(1)){//player2
                //System.out.print("7 and 12.");
            }
        }
        return numStones;//return the number of stones in the pit, 0 if empty or not found
    }

    private void displayBoard(JPanel boardPanel){
        // Top row of stores/pits
        boardPanel.add(createPitPanel(-1));//spacing pit (not visible)
        boardPanel.add(createStorePanel(getNumStonesInStore(2),true));//player 2's top half of store
         
        for (int i = 12; i >= 7; i--) {//all of player 2's pits
            boardPanel.add(createPitPanel(getNumStonesInPit(i)));
        }
        boardPanel.add(createStorePanel(getNumStonesInStore(1),true));//player 1's top half of store
        boardPanel.add(createPitPanel(-1));//spacing pit (not visible)

        //bottom row of storse/pits
        boardPanel.add(createPitPanel(-1));//spacing pit (not visible)

        boardPanel.add(createStorePanel(0,false));//player 2's bottom half of store     
        for (int i = 1; i < 7; i++) {//all of player 1's pits
            boardPanel.add(createPitPanel(getNumStonesInPit(i)));
        }
        boardPanel.add(createStorePanel(0,false));//player 1's bottom half of store

        boardPanel.add(createPitPanel(-1));//spacing pit (not visible)

        //start of bottom row which contains txt output and menu button
        boardPanel.add(createPitPanel(-1));//spacing pit (not visible)
        if (!game.isGameOver()){
            boardPanel.add(createMessagePanel("It is currently "+game.getCurrentPlayer()+"'s turn"));
        }else{//if the game is over it is no ones turn!
            boardPanel.add(createPitPanel(-1));//spacing pit (not visible)
        }
        
        boardPanel.add(createPitPanel(-1));//spacing pit (not visible)

        //str should be updated with the error msg if there is one, set to empty string after displaying
        if (!game.isGameOver()){//while the game is happening
            boardPanel.add(createMessagePanel(errorMsg));//To display a error message if there is one
            errorMsg="";//clear the old error message
        }else{//if the game is over there is no need for a error message
            boardPanel.add(createPitPanel(-1));//spacing pit (not visible)
        }
        
        boardPanel.add(createPitPanel(-1));//spacing pit (not visible)

        
        if (game.isGameOver()){//To display the winner
            boardPanel.add(createMessagePanel("Game Over! "+game.getWinner()+" Won the game!!"));
            boardPanel.add(createMessagePanel("Player 1 had"+getNumStonesInStore(1)+" stones in there store and player 2 had "+getNumStonesInStore(2)+" stones in theres"));

        }else{//if the game is not over this pannel will just be empty
            boardPanel.add(createPitPanel(-1));//spacing pit (not visible)
            boardPanel.add(createPitPanel(-1));//spacing pit (not visible)
        }
        boardPanel.add(createPitPanel(-1));//spacing pit (not visible)
        boardPanel.add(createMessagePanel("Save Game"));
        boardPanel.add(createMessagePanel("Return to menu"));

    }


    private int indexToPit(int i){
        if (i>=12 && i<=17){//one of player 1's pits
            return i-11;
        }else if(i>=2 && i<=7){//14-i
            return 14-i;
        }else{
            return -1;//somthign went wrong
        }
    }

    private boolean isPitPanel(int i) {
        if (i>=2 && i<=7){//one of player 2's pits
            return true;
        }else if(i>=12 && i<=17){//one of player 1's pits
            return true;
        }else{
            return false;
        }

    }

    private JPanel createMessagePanel(String message) {
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BorderLayout());
        
        JLabel messageLabel = new JLabel("<html>" + message + "</html>");
        messageLabel.setVerticalAlignment(JLabel.CENTER); // Align text to the top

        messagePanel.add(messageLabel, BorderLayout.CENTER);
        
        return messagePanel;
    }

    private JPanel createPitPanel(int stones) {
        JPanel pitPanel = new JPanel();
        pitPanel.setLayout(new BorderLayout());
        if (stones>=0){//if we are making a pit/store
            pitPanel.add(new JLabel("        " + stones + " "), BorderLayout.CENTER);
            pitPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }else{

        }
        return pitPanel;
    }

    private JPanel createStorePanel(int stones, boolean top) {
        JPanel storePanel = new JPanel();
        storePanel.setPreferredSize(new Dimension(50, 50));
        storePanel.setLayout(new BorderLayout());
        
        
        Border noBottomOrNoTop = new LineBorder(Color.BLACK, 1, true) {//so we only have lines on the top left and right sides
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                super.paintBorder(c, g, x, y, x+1, height);//left side border
                //g.drawLine(x,y,x+2,height);
                g.drawRect(width, y, width-1, height); //Right side broder
                if(top){
                    g.drawRect(x, y, width, y+1); //Top border
                }else{
                    g.drawRect(x, height, width, height-1); //Bottom border
                }
                
            }
        };

        if (stones>=0){//if we are making a pit/store
            if (top){
                storePanel.add(new JLabel("          " + stones + " "), BorderLayout.SOUTH);
            }           
            storePanel.setBorder(noBottomOrNoTop);
            //storePanel.setBorder(rightBorder);
        }else{
            storePanel.setBorder(new EmptyBorder(0, 1, 1, 1));
        }
        return storePanel;
    }

    private int getNumStonesInPit(int pitNum) {//to help display the pits
        return data.getNumStones(pitNum);
    }
    private int getNumStonesInStore(int playerNumber) {//to help display the stores
        return data.getStoreCount(playerNumber);
    }


    private void createWelcomeScreen() {
        JPanel welcomeScreen = new JPanel(null);
        welcomeScreen.setBackground(new Color(200, 220, 240));  // Light blue background
        welcomeScreen.setLayout(new BoxLayout(welcomeScreen, BoxLayout.Y_AXIS));
        welcomeScreen.setMaximumSize(new Dimension(800, 500));//so there is no overflow
        

        JLabel welcomeLabel = new JLabel("Welcome! Click a button to proceed");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 26));
        welcomeLabel.setForeground(Color.blue);//to change the color of the msg

        JButton newGameButton = new JButton("New Game");
        newGameButton.setBorderPainted(false);//false to remove the border
        JButton loadGameButton = new JButton("Load Game");
        loadGameButton.setBorderPainted(false);//false to remove the border

        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onLoadScreen=false;
                cardLayout.show(cardPanel, "setUpScreen");
            }
        });

        loadGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onLoadScreen=true;
                cardLayout.show(cardPanel, "LoadGameScreen");
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(newGameButton);
        buttonPanel.add(loadGameButton);

        welcomeScreen.add(Box.createVerticalGlue());  // Center vertically
        welcomeScreen.add(welcomeLabel);
        welcomeScreen.add(Box.createVerticalStrut(20));  // Add some vertical spacing
        welcomeScreen.add(buttonPanel);
        welcomeScreen.add(Box.createVerticalGlue());

        cardPanel.add(welcomeScreen, "welcomeScreen");
        welcomeScreen.setBackground(new Color(200, 220, 240));  // Light blue background
    }




    private void createSetUpScreen() {
        JPanel setUpScreen = new JPanel(null);
        setUpScreen.setBackground(new Color(200, 220, 240));  // Light blue background
        setUpScreen.setLayout(new BoxLayout(setUpScreen, BoxLayout.Y_AXIS));
        setUpScreen.setMaximumSize(new Dimension(800, 500));//so there is no overflow
        

        JLabel gameMode = new JLabel("Please Select the rules set you want to use");
        gameMode.setFont(new Font("Arial", Font.BOLD, 26));
        gameMode.setForeground(Color.blue);//to change the color of the msg

        JButton kalahRulesButton = new JButton("Kalah Rules");
        JButton ayoRulesButton = new JButton("Ayo Rules");
        JButton backButton = new JButton("Back");

        kalahRulesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //set rules to kalah
                ruleSet=1;
                setUpWithRuleSet();//make sure the game is set up for kalah
                createBoard();
                //cardLayout.show(cardPanel, "game");//chnage to game screen
            }
        });

        ayoRulesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //set rules to ayo
                ruleSet=2;
                setUpWithRuleSet();//make sure the game is set up for Ayo
                createBoard();
                //cardLayout.show(cardPanel, "game");//chnage to game screen
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onLoadScreen=false;
                cardLayout.show(cardPanel, "welcomeScreen");
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(kalahRulesButton);
        buttonPanel.add(ayoRulesButton);

        setUpScreen.add(Box.createVerticalGlue());  // Center vertically
        setUpScreen.add(gameMode);
        setUpScreen.add(Box.createVerticalStrut(20));  // Add some vertical spacing
        setUpScreen.add(buttonPanel);
        setUpScreen.add(Box.createVerticalGlue());
        setUpScreen.add(backButton, BorderLayout.SOUTH);

        setUpScreen.setBackground(new Color(200, 220, 240));  // Light blue background

        cardPanel.add(setUpScreen, "setUpScreen");
    }

    private void createLoadGameScreen() {       
        JPanel loadGameScreen = new JPanel(new BorderLayout());

        JLabel loadGameLabel = new JLabel("Load Game Screen Content");
        loadGameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        loadGameScreen.add(loadGameLabel, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");

        cardPanel.add(loadGameScreen, "LoadGameScreen");
        JMenuBar menuBar = createLoadGameMenuBar();
        setJMenuBar(menuBar);
        
        

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onLoadScreen=false;
                cardLayout.show(cardPanel, "welcomeScreen");
            }
        });

        loadGameScreen.add(backButton, BorderLayout.SOUTH);
        

        setVisible(true);
    }

    private JMenuBar createLoadGameMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        List<String> savedGames = getSavedGames();

        for (String gameS : savedGames) {
            JMenuItem gameMenuItem = new JMenuItem(gameS);
            gameMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (onLoadScreen){//if we are on the load item screen
                        try{
                            game = (MancalaGame) saver.loadObject(gameS);
                            setUpWithRuleSet();//make sure the game is set up for kalah
                            createBoard();
                        }catch (IOException | ClassNotFoundException e2) {
                            e2.printStackTrace(); 
                        }
                        
                    }
                }
            });
            fileMenu.add(gameMenuItem);
        }

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Add Exit menu item
        fileMenu.addSeparator(); // Separator between saved games and Exit
        fileMenu.add(exitMenuItem);

        // Add File menu to the menu bar
        menuBar.add(fileMenu);

        return menuBar;
    }

    private List<String> getSavedGames() {//will get the list of file names that are saved and return it
        List<String> savedGames = new ArrayList<>();//list of the file names        
        File folder = new File("assets");//se the folder we want to look in
        File[] files = folder.listFiles();//create a list of the files

        if (files != null) {//if there is at least one file present
            for (File file : files) {//go threw all of the files and save there names to the array list
                savedGames.add(file.getName());
            }
        }
        return savedGames;//the string array list of file names
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MancalaGUI();
            }
        });
    }
}