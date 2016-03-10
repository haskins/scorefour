/**
 * lass for the Event dispatcher thread.
 * 
 * @author Devon Harker, Josh Haskins, Rafael Roman Otero, Vincent Tennant, Thanh Minh Vo
 * @version 03-15-2012
 */

package gui;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.*;
import javax.swing.SwingWorker.StateValue;
import board.Board;
import scoreboard.*;

public class MainGUI implements ActionListener, ItemListener {


    //THE QUEUE (COMMUNICATION CHANNEL BETWEEN REFEEE AND GUI )
    private LinkedBlockingQueue<String> sendQueue;
    private LinkedBlockingQueue<String> recvQueue;
    //WORKERS
    LoadImage imgRetriever;
    //VARS
    private JFrame frame;
    Panel gamePanel;
    private String playerName;
    private JMenuBar menuBar;
    private Image imgToShow;
    private boolean isFirstGame;
    //CONSTANTS
    public final int BOARD_INITIAL_X = 40;
    public final int BOARD_INITIAL_Y = 225;
    private static final int WINDOW_WIDTH = 680;
    private static final int WINDOW_HEIGHT = 700;
    private static final String COMPUTER_NAME = "Gertrudis";
    private static final String YOU_LOSE_IMAGE = "computer_wins.jpeg";
    private static final String ICON_IMAGE = "icon.gif";
    private static final int YOU_LOSE_X = 120;
    private static final int YOU_LOSE_Y = 160;
    //dialogs
    private static final String INSTRUCTIONS_DIALOG = "The object of Score Four is to position four beads of the same color in a straight or diagonal line on any level or any angle.";
    private static final String ABOUT_DIALOG = "Score 4 v1.0 "
            + "\n\n Team:"
            + "\n Supreme Double Double "
            + "\n\n Team Members: "
            + "\n Thanh Minh Vo"
            + "\n Josh Haskins"
            + "\n Devon Harker"
            + "\n Vincent Tennant "
            + "\n Rafael Román Otero ";
    ////////////////////////////////////////////////////////////
    // 			CONSTRUCTOR
    ////////////////////////////////////////////////////////////

    public MainGUI(char colourPlayer1, char colourPlayer2, LinkedBlockingQueue<String> recvQueue,   LinkedBlockingQueue<String> sendQueue) throws InterruptedException, ExecutionException {
        playerName = "uninitialized";
        this.sendQueue = sendQueue;
        this.recvQueue = recvQueue;
	isFirstGame = true;
    }

    ////////////////////////////////////////////////////////////
    // 				MENU
    ////////////////////////////////////////////////////////////
    private JMenuBar createMenuBar() {

        JMenu menu;
        JMenuItem menuItem;

        //Create the menu bar.
        menuBar = new JMenuBar();

        //Main Menu
        menu = new JMenu("Main Menu");
        menu.setMnemonic(KeyEvent.VK_M);
        menuBar.add(menu);

        //Humand vd Gertrudis
        menuItem = new JMenuItem("Human vs Gertrudis", KeyEvent.VK_H);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);

		//Gertrudis vs Computer
        menuItem = new JMenuItem("Gertrudis vs Computer", KeyEvent.VK_G);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);

        //Leave Game
        menuItem = new JMenuItem("Leave Game", KeyEvent.VK_L);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);

        menuItem.setEnabled(false); //starts disabled

        menu.addSeparator();

        //Quit
        menuItem = new JMenuItem("Exit", KeyEvent.VK_E);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);

        //Game
        menu = new JMenu("Game");
        menu.setMnemonic(KeyEvent.VK_G);
        menuBar.add(menu);

        //High Scores
        menuItem = new JMenuItem("High Scores", KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);

        //Help Menu
        menu = new JMenu("Help");
        menu.setMnemonic(KeyEvent.VK_H);
        menuBar.add(menu);

        //Instructions
        menuItem = new JMenuItem("Instructions", KeyEvent.VK_I);
        menuItem.addActionListener(this);
        menu.add(menuItem);

        menu.addSeparator();

        //About
        menuItem = new JMenuItem("About", KeyEvent.VK_A);
        menuItem.addActionListener(this);
        menu.add(menuItem);

        return menuBar;
    }

    ////////////////////////////////////////////////////////////
    //			MENU EVENT PROCESSING
    ////////////////////////////////////////////////////////////
    public void actionPerformed(ActionEvent e) {
        JMenuItem source = (JMenuItem) (e.getSource());

        String cmd = source.getText();

        //------------------------------
        //process commands from the menu
        //------------------------------
        if (cmd.equals("Exit")) {

            Object[] options = {"Yes, take me out of here! ",
                "Ok, I'll stay"};

            //asks for confirmation
            int response = JOptionPane.showOptionDialog(frame,
                    "Do you really want to exit?",
                    "I thought you were having fun",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, //do not use a custom Icon
                    options, //the titles of buttons
                    options[0]); //default button title

            //exits
            if (response == 0) {
                System.exit(0);
            }

        } else if (cmd.equals("Human vs Gertrudis")) {

            //asks players name
            playerName = (String) JOptionPane.showInputDialog(
                    frame,
                    "Player's Name:\n",
                    "Score 4",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    null,
                    "Fred");

            //if invalid name, then uses defaults default name
            if (playerName.equals("")) {
                playerName = "Human Player";
            }

            //SEND PLAYER NAMES TO REFEREE
            //which at this point is waiting for them 

		if( !isFirstGame ){
			System.out.println( "GUI, I sent restart game" );
			sendToReferee("restart game");
		}	
		else{
			sendToReferee( "Human vs Gertrudis" ); 
			sendToReferee(playerName);
            		sendToReferee(COMPUTER_NAME);
			isFirstGame = false;
		}

            //disables and enables different menus 
            menuBar.getMenu(0).getMenuComponent(0).setEnabled(false);
            menuBar.getMenu(0).getMenuComponent(1).setEnabled(false);

            //starts new game
            try {
                startNewGame( "Human vs Gertrudis" );
            } catch (InterruptedException e1) {
                System.err.print("Cldnt stsrta neww game, error:" + e1.getMessage());
            } catch (ExecutionException e1) {
                System.err.print("Cldnt stsrta neww game, error:" + e1.getMessage());
            }

        } else if (cmd.equals("Gertrudis vs Computer")) {

            //if invalid name, then uses defaults default name
             playerName = "Computer";

            //SEND PLAYER NAMES TO REFEREE
            //which at this point is waiting for them 

		if( !isFirstGame ){
			System.out.println( "GUI, I sent restart game" );
			sendToReferee("restart game");
		}	
		else{
			sendToReferee( "Gertrudis vs Computer" ); 
			sendToReferee(playerName);
            		sendToReferee(COMPUTER_NAME);
			isFirstGame = false;
		}
		      
            //disables and enables different menus 
            menuBar.getMenu(0).getMenuComponent(0).setEnabled(false);
            menuBar.getMenu(0).getMenuComponent(1).setEnabled(false);

            //starts new game
            try {
                startNewGame("Gertrudis vs Computer");
            } catch (InterruptedException e1) {
                System.err.print("Cldnt stsrta neww game, error:" + e1.getMessage());
            } catch (ExecutionException e1) {
                System.err.print("Cldnt stsrta neww game, error:" + e1.getMessage());
            }

        } else if (cmd.equals("About")) {

            JOptionPane.showMessageDialog(frame, ABOUT_DIALOG);

        } else if (cmd.equals("Instructions")) {

            JOptionPane.showMessageDialog(frame, INSTRUCTIONS_DIALOG);

        } else if (cmd.equals("Leave Game")) {

            //disables and enables different menus 
            menuBar.getMenu(0).getMenuComponent(0).setEnabled(true);
            menuBar.getMenu(0).getMenuComponent(1).setEnabled(false);

            leaveGame();

        } else if (cmd.equals("High Scores")) {

		 JOptionPane.showMessageDialog(frame, Scoreboard.getScores() );
        }

    }

    ////////////////////////////////////////////////////////////
    // 			MENU ACTIONS
    ////////////////////////////////////////////////////////////
    
   //---------new game---------
    private void startNewGame( String typeOfGame ) throws InterruptedException, ExecutionException {

        gamePanel = new Panel(playerName, COMPUTER_NAME, sendQueue, recvQueue, menuBar );

        //displays figures

        //frame.setBackground(new JLabel(" ", new ImageIcon("background.jpg"), JLabel.CENTER));
        frame.add(gamePanel);				//adds container panel
        frame.invalidate();
        frame.validate();
        frame.repaint();

	if( typeOfGame.equals( "Gertrudis vs Computer" ) ){
		gamePanel.setGertrudeVsComputer();
	}
    }
    
    //--------- leave game (gertrudis wins)---------
    private void leaveGame() {
        startImageLoader();

    }
    public void itemStateChanged(ItemEvent e) {
        //empty 
    }
    ////////////////////////////////////////////////////////////
    // 				 INTERFACE
    ////////////////////////////////////////////////////////////

    public void createAndShowGUI() {
        //creates and shows window
        frame = new JFrame("Double Double Supreme Score 4");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //add menu
        frame.setJMenuBar(this.createMenuBar());
	frame.setResizable(false);
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		

	Image icon = new ImageIcon(ICON_IMAGE).getImage();

 	frame.setIconImage(icon);

        frame.setVisible(true);
    }

    private void sendToReferee(String msg) {
        sendQueue.add(msg);
    }

    public void startImageLoader() {
        final LoadImage imgRetriever = new LoadImage(YOU_LOSE_IMAGE);

        //adds listener!
        imgRetriever.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {

                if (imgRetriever.getState() == StateValue.DONE) {

                    javax.swing.SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            try {
                                imgToShow = imgRetriever.get();
   
                                //SHOWS IMAGE
                                restartEverything();

                                frame.getGraphics().drawImage(imgToShow, YOU_LOSE_X, YOU_LOSE_Y, null, null);
                            } catch (InterruptedException e) {
                                System.err.println("Couldnt display board 1 ");
                            } catch (ExecutionException e) {
                                System.err.println("Couldnt display board 2");
                            }

                        }
                    });
                }

            }
        });

        //executes
        imgRetriever.execute();

    }//end of startIMageLoader

    public void restartEverything() 
	throws InterruptedException, java.util.concurrent.ExecutionException {
	
	gamePanel.restart();
	gamePanel = null;
	gamePanel = new Panel(playerName, COMPUTER_NAME, sendQueue, recvQueue, menuBar );
	gamePanel.validate();
	
    }
}
