import javax.swing.*;
import java.util.ArrayList;

import java.awt.*;

import java.awt.event.*;
import javax.swing.border.*;

/**
 * The BigTwoGUI class implements the CardGameUI interface. It is used to build
 * a GUI for the Big Two card game and handle all user actions of a Big Two game
 */

public class BigTwoGUI implements CardGameUI {

    // private instance variables
    private final static int MAX_CARD_NUM = 13; // max. no. of cards each player holds
    /**
     * a Big Two card game associates with this GUI.
     */
    private BigTwo game;

    /**
     * a array of boolean indicated the selected card in plater card list
     */
    private boolean[] selected = new boolean[MAX_CARD_NUM];;

    /**
     * an integer specifying the index of the active player.
     */
    private int activePlayer = -1;

    /**
     * the main window of the GUI.
     */
    private JFrame frame;

    /**
     * a panel for showing the cards of each player and the cards played on the
     * table.
     */
    private JPanel bigTwoPanel;

    /**
     * a “Play” button for the active player to play their selected cards.
     */
    private JButton playButton;

    /**
     * a “Pass” button for the active player to pass his/her turn to the
     * next player.
     */
    private JButton passButton;

    /**
     * a text area for showing the current game status as well as end of game
     * messages.
     */
    private JTextArea msgArea;

    /**
     * a text area for showing chat messages sent by the players.
     */
    private JTextArea chatArea;

    /**
     * a text field for player to enter their chat messages and sent to other
     * players.
     */
    private JTextField chatInput; // chat input box in JTextField
    private JPanel[] panels;
    private JLabel[] avatars = new JLabel[4];
    private JLabel[] name;
    private ArrayList<ArrayList<JLabel>> playerCards;
    private ArrayList<Hand> handsOnTable;
    private ArrayList<CardGamePlayer> playerList;
    private JLabel[][] cardsImage = new JLabel[4][14];
    private final static char[] suitArray = { 'd', 'c', 'h', 's' };
    private final static char[] rankArray = { 'a', '2', '3', '4', '5', '6', '7', '8', '9', 't', 'j', 'q', 'k' };

    // public constructor
    /**
     * a constructor for creating a BigTwoGUI.
     * 
     * @param game the BigTwo game
     */
    public BigTwoGUI(BigTwo game) {
        this.game = game;
        // setActivePlayer(game.getCurrentPlayerIdx());
        // System.out.println("TESTTESTTEST" + getActivePlayer());
        setupGUI();
        this.handsOnTable = game.getHandsOnTable();
    }

    /**
     * for creating setting up the GUI, adding different JPanel and element to the
     * frame.
     */
    private void setupGUI() {

        // set up frame
        JFrame frame = new JFrame();
        this.frame = frame;
        frame.setLayout(new BorderLayout());
        frame.setBackground(Color.green);

        frame.setTitle("Big Two");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.setSize(1300, 900);
        // frame.setResizable(false);
        // this.frame = frame;

        // set border style
        Border borderLine = BorderFactory.createLineBorder(Color.BLACK, 1);

        // set up top menu bar
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        // set up game buttom on menuBar
        JMenu menuGame = new JMenu("Game");

        JMenuItem menuGameItemRestart = new JMenuItem("Restart");
        menuGameItemRestart.addActionListener(new RestartMenuItemListener());

        JMenuItem menuGameItemQuit = new JMenuItem("Quit");
        menuGameItemQuit.addActionListener(new QuitMenuItemListener());

        menuGame.add(menuGameItemRestart);
        menuGame.add(menuGameItemQuit);

        // set up Msg buttom on menuBar
        JMenu menuMsg = new JMenu("Message");

        JMenuItem textClear = new JMenuItem("Clear Text Box");
        textClear.addActionListener(new TextClearListener());
        menuMsg.add(textClear);

        JMenuItem chatClear = new JMenuItem("Clear Chat Box");
        chatClear.addActionListener(new ChatClearListener());
        menuMsg.add(chatClear);

        menuBar.add(menuGame);
        menuBar.add(menuMsg);

        // Set up message and chat panel
        JPanel msgAndChat = new JPanel();
        msgAndChat.setLayout(new BoxLayout(msgAndChat, BoxLayout.PAGE_AXIS));

        // Message area
        this.msgArea = new JTextArea(10, 30);
        // this.msgArea.setPreferredSize(new Dimension(380, 400));
        // this.msgArea.setMaximumSize(new Dimension(380, 4000000));
        this.msgArea.setLineWrap(true);
        // msgArea.setMaximumSize(new Dimension(300, 400));
        msgArea.setEditable(false);
        // msgArea.setPreferredSize(new Dimension(300, 400));
        // msgArea.setMaximumSize(new Dimension(300, 400));
        // msgArea.setEnabled(false);

        JScrollPane msgscroller = new JScrollPane(msgArea);
        msgArea.setLineWrap(true);
        msgscroller.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        msgscroller.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        msgAndChat.add(msgscroller, BorderLayout.NORTH);
        msgArea.setBorder(borderLine);

        // Chat area
        this.chatArea = new JTextArea(10, 30);
        // this.chatArea.setPreferredSize(new Dimension(380, 400));
        // this.chatArea.setMaximumSize(new Dimension(380, 40000000));
        this.chatArea.setEditable(false);
        this.chatArea.setLineWrap(true);
        // chatArea.setEnabled(false);

        JScrollPane chatscroller = new JScrollPane(chatArea);
        chatArea.setLineWrap(true);
        chatscroller.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        chatscroller.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        msgAndChat.add(chatscroller, BorderLayout.SOUTH);
        chatArea.setBorder(borderLine);

        // Message label near the text field (on chat input panel)
        JPanel LabalAndText = new JPanel();
        LabalAndText.setLayout(new FlowLayout());

        LabalAndText.add(new JLabel("Message:"));
        JTextField chatInput = new chatAreaText(30);
        chatInput.getDocument().putProperty("filterNewlines", Boolean.TRUE);

        chatInput.setPreferredSize(new Dimension(200, 40));

        LabalAndText.add(chatInput);
        this.chatInput = chatInput;

        // msgAndChat.add(msgArea);
        msgAndChat.add(msgscroller); // add scroller, but not text
        // msgAndChat.add(chatArea);
        msgAndChat.add(chatscroller); // add scroller, but not text
        msgAndChat.add(LabalAndText);

        // set up play and pass buttoms at the bottom of the frame
        JPanel bottomButtons = new JPanel();
        playButton = new JButton("Play");
        playButton.addActionListener(new PlayButtonListener());
        passButton = new JButton("Pass");
        passButton.addActionListener(new PassButtonListener());
        bottomButtons.add(playButton);
        bottomButtons.add(passButton);

        bigTwoPanel = new JPanel();
        bigTwoPanel.setBackground(Color.green);
        bigTwoPanel.setSize(2000, 600);
        bigTwoPanel.setLayout(new BoxLayout(bigTwoPanel, BoxLayout.Y_AXIS));
        panels = new JPanel[5];
        avatars = new JLabel[4];
        this.name = new JLabel[5];
        for (int i = 0; i < 4; i++) {

            ImageIcon imageIcon = new ImageIcon(
                    new ImageIcon("src/avatars/" + (i + 1) + ".png").getImage().getScaledInstance(80, 100,
                            Image.SCALE_DEFAULT));

            avatars[i] = new JLabel();
            avatars[i].setIcon(imageIcon);
            avatars[i].setBorder(new EmptyBorder(0, 0, 0, 40));

            name[i] = new JLabel();
            name[i].setBorder(new EmptyBorder(0, 40, 0, 0));
            name[i].setHorizontalAlignment(JLabel.LEFT);
            name[i].setText(game.getPlayerList().get(i).getName());

            panels[i] = new JPanel();
            panels[i].setSize(new Dimension(10000, 150));

            panels[i].setMaximumSize(new Dimension(10000, 150));
            panels[i].setBackground(Color.green);
            panels[i].setLayout(new BoxLayout(panels[i], BoxLayout.X_AXIS));
            panels[i].setBorder(new EmptyBorder(15, 15, 15, 15));
            panels[i].setPreferredSize(new Dimension(600, 140));
            Border borderLine2 = BorderFactory.createLineBorder(Color.BLACK, 1);
            panels[i].setBorder(borderLine2);

            panels[i].add(avatars[i]);
            panels[i].add(name[i]);
            bigTwoPanel.add(panels[i]);

        }
        name[4] = new JLabel();
        name[4].setBorder(new EmptyBorder(0, 10, 100, 0));
        name[4].setHorizontalAlignment(JLabel.LEFT);
        name[4].setText("Cards on table");

        panels[4] = new JPanel();
        panels[4].setMaximumSize(new Dimension(1000, 150));
        panels[4].setBackground(Color.green);
        panels[4].setLayout(new BoxLayout(panels[4], BoxLayout.X_AXIS));
        panels[4].setBorder(new EmptyBorder(15, 15, 15, 15));
        panels[4].setPreferredSize(new Dimension(600, 140));
        Border borderLine2 = BorderFactory.createLineBorder(Color.BLACK, 1);
        panels[4].setBorder(borderLine2);

        panels[4].add(name[4]);
        bigTwoPanel.add(panels[4]);

        // imagesLoad();
        cardimageLoad();

        frame.add(bigTwoPanel, BorderLayout.CENTER);
        frame.add(msgAndChat, BorderLayout.EAST);
        frame.add(bottomButtons, BorderLayout.SOUTH);
        // set border style
        // Border borderLine = BorderFactory.createLineBorder(Color.BLACK, 1);

        frame.setSize(1300, 900);
        frame.setVisible(true);
        frame.repaint();
    };

    // load cards image and store them in ArrayList<ArrayList<JLabel>> cards;

    /**
     * a method to store the avatars and card image in two seperate ArrayList f
     */
    private void cardimageLoad() {
        final String[] suit = { "d", "c", "h", "s" };
        final String[] rank = { "a", "2", "3", "4", "5", "6", "7", "8", "9", "t", "j", "q", "k" };
        String path = new String();
        // load cards image
        for (int i = 0; i < 4; i++) {
            // this.cardsImage[i] = new JLabel[14];
            for (int j = 0; j < 13; j++) {
                path = "src/" + rank[j] + suit[i] + ".gif";
                JLabel image = new JLabel();
                image.setIcon(new ImageIcon(path));
                this.cardsImage[i][j] = image;
            }
            path = "src/b.gif";
            JLabel image = new JLabel();
            image.setIcon(new ImageIcon(path));
            this.cardsImage[i][13] = image;
        }
    }

    // set up an array for storing addresses for cards image
    private void imagesLoad() {
        // this.cardBack = new ImageIcon("src/b.gif").getImage();
        // avatars[0] = new ImageIcon("src/avatars/1.png").getImage();
        // avatars[1] = new ImageIcon("src/avatars/2.png").getImage();
        // avatars[2] = new ImageIcon("src/avatars/3.png").getImage();
        // avatars[3] = new ImageIcon("src/avatars/4.png").getImage();

        final String[] suit = { "d", "c", "h", "s" };
        final String[] rank = { "a", "2", "3", "4", "5", "6", "7", "8", "9", "t", "j", "q", "k" };

        String path = new String();
        // adding card image addresses to array
        for (int i = 0; i < 4; i++) {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
            JLabel imagea = new JLabel();
            ImageIcon imageIcon = new ImageIcon(
                    new ImageIcon("src/avatars/" + (i + 1) + ".png").getImage().getScaledInstance(80, 100,
                            Image.SCALE_DEFAULT));
            imagea.setIcon(imageIcon);
            panel.add(imagea);
            for (int j = 0; j < 13; j++) {
                path = "src/" + rank[j] + suit[i] + ".gif";
                // this.cardImages[i][j] = new ImageIcon(path).getImage();
                JLabel image = new JLabel();
                image.setIcon(new ImageIcon(path));
                if (j == 12) {
                    image.setBorder(new EmptyBorder(0, 0, 0, 0));
                } else {
                    image.setBorder(new EmptyBorder(0, 0, 0, -60));
                }
                panel.add(image);
            }
            bigTwoPanel.add(panel);
        }
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        JLabel image = new JLabel();
        ImageIcon imageIcon = new ImageIcon(
                new ImageIcon("src/avatars/1.png").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        image.setIcon(imageIcon);

        JLabel image2 = new JLabel();
        ImageIcon imageIcon2 = new ImageIcon(
                new ImageIcon("src/avatars/2.png").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        image2.setIcon(imageIcon2);

        JLabel image3 = new JLabel();
        ImageIcon imageIcon3 = new ImageIcon(
                new ImageIcon("src/avatars/3.png").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        image3.setIcon(imageIcon3);

        JLabel image4 = new JLabel();
        ImageIcon imageIcon4 = new ImageIcon(
                new ImageIcon("src/avatars/4.png").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        image4.setIcon(imageIcon4);

        panel.add(image);
        panel.add(image2);
        panel.add(image3);
        panel.add(image4);
        bigTwoPanel.add(panel);

    }

    /**
     * a method for setting the index of the current active player
     * 
     */
    public void setActivePlayer(int activePlayer) {
        if (activePlayer < 0 || activePlayer >= game.getPlayerList().size()) {
            this.activePlayer = -1;
        } else {
            this.activePlayer = activePlayer;
        }
    }

    /**
     * a method for getting the index of the current active player
     * 
     * @return an integer specifying the index of the active player
     */
    public int getActivePlayer() {
        return this.activePlayer;
    }

    /**
     * a method of getting the selected array (an array of boolean indicated the
     * selected card in player card list)
     * 
     * @return a array of boolean indicated the selected card in plater card list
     */
    public boolean[] getSelected() {
        return this.selected;
    }

    /**
     * a private method of resetting the selected array (an array of boolean
     * indicated the selected card in plater card list)
     */
    private void resetSelected() {
        for (int i = 0; i < selected.length; i++) {
            selected[i] = false;
        }
    }

    /**
     * a method of getting the number cards which the player have selected
     * 
     * @return an interger which indicates the number cards which the player have
     *         selected
     */
    public int getSelectedTrueLength() {
        int counter = 0;
        for (int i = 0; i < this.selected.length; i++) {
            if (this.selected[i]) {
                counter++;
            }
        }
        return counter;
    }

    private int[] getcardIdx() {
        int numberofCards = getSelectedTrueLength();
        if (numberofCards == 0) {
            return null;
        } else {
            int[] cardIdx = new int[numberofCards];
            int index = 0;
            for (int i = 0; i < 13; i++) {
                if (selected[i]) {
                    cardIdx[index] = i;
                    index++;
                }
            }
            return cardIdx;
        }
    }

    /**
     * a method for repaint the GUI
     */
    public void repaint() {
        enable();
        bigTwoPanel.setEnabled(true);
        activePlayer = this.game.getCurrentPlayerIdx();
        this.playerList = game.getPlayerList();
        playerCards = new ArrayList<ArrayList<JLabel>>();
        playerCards.add(new ArrayList<JLabel>());
        playerCards.add(new ArrayList<JLabel>());
        playerCards.add(new ArrayList<JLabel>());
        playerCards.add(new ArrayList<JLabel>());
        for (int i = 0; i < playerCards.size(); i++) {
            CardGamePlayer player = this.playerList.get(i);
            String name = player.getName();
            panels[i].removeAll();
            panels[i].add(avatars[i]);
            if (activePlayer == i) {
                // playerCards.get(i).add(new JLabel());
                for (int j = 0; j < player.getNumOfCards(); j++) {
                    // playerCards.get(i).add(new JLabel());
                    playerCards.get(i).add(cardsImage[player.getCardsInHand().getCard(j).getSuit()][player
                            .getCardsInHand().getCard(j).getRank()]);
                    if (j != player.getNumOfCards() - 1) {
                        playerCards.get(i).get(j).setBorder(new EmptyBorder(0, 0, 0, -40));
                    } else {
                        playerCards.get(i).get(j).setBorder(new EmptyBorder(0, 0, 0, 0));
                    }
                    panels[i].add(playerCards.get(i).get(j));
                    playerCards.get(i).get(j).addMouseListener(new CardClickListener());
                    panels[i].add(this.name[i]);
                }

                System.out.println("<" + name + ">");
                System.out.print("==> ");
                player.getCardsInHand().print(true, true);
            } else if (activePlayer == -1) {
                System.out.println("<" + name + ">");
                System.out.print("    ");
                player.getCardsInHand().print(true, true);
            } else {
                for (int j = 0; j < player.getNumOfCards(); j++) {
                    // System.out.println("this is player" + i + "cards N.O." + j);
                    // playerCards.get(i).add(cardsImage[1][13]);
                    playerCards.get(i).add(new JLabel());
                    playerCards.get(i).get(j).setIcon(new ImageIcon("src/b.gif"));

                    if (j != player.getNumOfCards() - 1) {
                        playerCards.get(i).get(j).setBorder(new EmptyBorder(0, 0, 0, -40));
                    } else {
                        playerCards.get(i).get(j).setBorder(new EmptyBorder(0, 0, 0, 0));
                    }
                    panels[i].add(playerCards.get(i).get(j));
                    playerCards.get(i).get(j).addMouseListener(new CardClickListener());
                    panels[i].add(this.name[i]);
                }

                System.out.println("<" + name + ">");
                System.out.print("    ");
                player.getCardsInHand().print(true, true);
            }
            frame.validate();
            frame.repaint();
        }
        System.out.println("<Table>");
        Hand lastHandOnTable = game.getLastHand();
        panels[4].removeAll();
        panels[4].setLayout(new BoxLayout(panels[4], BoxLayout.X_AXIS));
        panels[4].add(this.name[4]);
        JLabel[] cardsOnTableImage = new JLabel[5];
        if (lastHandOnTable != null) {
            for (int i = 0; i < lastHandOnTable.size(); i++) {
                System.out.println("Test");

                System.out.println(lastHandOnTable.getCard(i).getSuit());
                System.out.println(lastHandOnTable.getCard(i).getRank());

                cardsOnTableImage[i] = cardsImage[lastHandOnTable.getCard(i).getSuit()][lastHandOnTable.getCard(i)
                        .getRank()];

                panels[4].add(cardsOnTableImage[i]);
                panels[4].repaint();
            }

            frame.repaint();
            enable();

            System.out
                    .print("    <" + lastHandOnTable.getPlayer().getName() + "> {" + lastHandOnTable.getType() + "} ");
            lastHandOnTable.print(true, false);
        } else {
            System.out.println("  [Empty]");
        }
    };

    /**
     * a method to print message to the messag area of the GUI with a specified
     * string
     * 
     * @param msg the String to print to the message area
     */
    public void printMsg(String msg) {
        msgArea.append(msg + "\n");
    };

    /**
     * a method to claer the message area of the GUI
     */
    public void clearMsgArea() {
        msgArea.setText("");
    };

    /**
     * a method to print message to the chat area of the GUI with a specified string
     * 
     * @param msg the String to print to the chat area
     */
    public void printchatArea(String msg) {
        chatArea.append(msg + "\n");
    }

    /**
     * a method to claer the chat area of the GUI
     */
    public void clearchatArea() {
        chatArea.setText("");
    }

    /**
     * a method for resetting the GUI
     */
    public void reset() {
        // (i)reset list of selected cards
        for (int i = 0; i < MAX_CARD_NUM; i++) {
            selected[i] = false;
        }
        // (ii)clear the message area
        clearMsgArea();
        // (iii)enable user interactions
        // enable();
    };

    /**
     * a method to enabling user interactions with the GUI. It will enable the
     * "Plat" button and "Pass" button to enable
     */
    public void enable() {
        // (i) enable the “Play” button
        playButton.setEnabled(true);
        // enable the “Pass” button
        passButton.setEnabled(true);
        // (ii) enable the chat input
        chatInput.setEnabled(true);
        // (iii) enable the BigTwoPanel for selection of cards through mouse clicks.
        bigTwoPanel.setEnabled(true);

    };

    /**
     * a method to disable user interactions with the GUI. It will disable the
     * "Plat" button and "Pass" button to enable
     */
    public void disable() {
        // (i) enable the “Play” button
        playButton.setEnabled(false);
        // enable the “Pass” button
        passButton.setEnabled(false);
        // (ii) enable the chat input
        chatInput.setEnabled(false);
        // (iii) enable the BigTwoPanel for selection of cards through mouse clicks.
        bigTwoPanel.setEnabled(false);

    };

    /**
     * a method for prompting the active player to select cards and make his/her
     * move. A message should be displayed in the message area showing it is the
     * active player’s turn.
     */
    public void promptActivePlayer() {
        // enable();
        this.activePlayer = game.getCurrentPlayerIdx();
        printMsg(game.getPlayerList().get(activePlayer).getName() + "'s turn: ");
        repaint();
        String printStr = "";
        Hand lastHandOnTable = game.getLastHand();
        if (lastHandOnTable != null) {
            for (int i = 0; i < lastHandOnTable.size(); i++) {
                printStr += "[" + lastHandOnTable.getCard(i).toString() + "] ";
            }
            printMsg("{" + lastHandOnTable.getType() + "} " + printStr);
            // print(true, true, (CardList) lastHandOnTable);
        }
        frame.validate();
        repaint();
        resetSelected();
    };

    // private void print(boolean printFront, boolean printIndex, CardList cards) {
    // if (cards.size() > 0) {
    // for (int i = 0; i < cards.size(); i++) {
    // String string = "";
    // if (printIndex) {
    // string = i + " ";
    // }
    // if (printFront) {
    // string = string + "[" + cards.getcards(i) + "]";
    // } else {
    // string = string + "[ ]";
    // }
    // if (i % 13 != 0) {
    // string = " " + string;
    // }
    // printMsg(string);
    // if (i % 13 == 12 || i == cards.size() - 1) {
    // printMsg("");
    // }
    // }
    // } else {
    // printMsg("[Empty]");
    // }
    // }

    // inner classes:
    /**
     * an inner class that extends the JPanel class and implements the MouseListener
     * interface.
     */
    private class BigTwoPanel extends JPanel implements MouseListener {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
        }

        public void mouseClicked(MouseEvent e) {

        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }
    }

    /**
     * an inner class that extends the JPanel class and implements the MouseListener
     * interface on the bigTwopanel.
     */
    class CardClickListener extends JPanel implements MouseListener {
        @Override
        public void mouseReleased(MouseEvent event) {
            JLabel target = ((JLabel) (event.getComponent()));
            int rank = -1;
            int suit = -1;
            if (target.getIcon().toString() == "src/b.gif") {
                return;
            }
            char cRank = target.getIcon().toString().charAt(4);
            char cSuit = target.getIcon().toString().charAt(5);
            for (int i = 0; i < 13; i++) {
                if (rankArray[i] == cRank) {
                    rank = i;
                }
            }
            for (int i = 0; i < 4; i++) {
                if (suitArray[i] == cSuit) {
                    suit = i;
                }
            }
            boolean lifted = (target.getBorder().getBorderInsets(target).bottom == 15);
            boolean rightMost = (target.getBorder().getBorderInsets(target).right == 0);

            if (!lifted && !rightMost) {
                target.setBorder(new EmptyBorder(0, 0, 15, -40));
                for (int i = 0; i < playerList.get(activePlayer).getNumOfCards(); i++) {
                    if (playerList.get(activePlayer).getCardsInHand().getCard(i).compareTo(new Card(suit, rank)) == 0) {
                        selected[i] = true;
                    }
                }
            } else if (!lifted && rightMost) {
                target.setBorder(new EmptyBorder(0, 0, 15, 0));
                for (int i = 0; i < playerList.get(activePlayer).getNumOfCards(); i++) {
                    if (playerList.get(activePlayer).getCardsInHand().getCard(i).compareTo(new Card(suit, rank)) == 0) {
                        selected[i] = true;
                    }
                }
            } else if (lifted && !rightMost) {
                target.setBorder(new EmptyBorder(0, 0, 0, -40));
                for (int i = 0; i < playerList.get(activePlayer).getNumOfCards(); i++) {
                    if (playerList.get(activePlayer).getCardsInHand().getCard(i).compareTo(new Card(suit, rank)) == 0) {
                        selected[i] = false;
                    }
                }

            } else if (lifted && rightMost) {
                target.setBorder(new EmptyBorder(0, 0, 0, 0));
                for (int i = 0; i < playerList.get(activePlayer).getNumOfCards(); i++) {
                    if (playerList.get(activePlayer).getCardsInHand().getCard(i).compareTo(new Card(suit, rank)) == 0) {
                        selected[i] = false;
                    }
                }
            }
        }

        public void mousePressed(MouseEvent event) {
        }

        public void mouseEntered(MouseEvent event) {
        }

        public void mouseExited(MouseEvent event) {
        }

        public void mouseClicked(MouseEvent event) {
        }
    }

    /**
     * an inner class that implements the ActionListener interface of the play
     * buttom.
     * Add a actionPerformed() method from the ActionListener interface to handle
     * button-click events
     */
    private class PlayButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (getSelectedTrueLength() == 0) {
                printMsg("Please select cards to play or pass your term.");
            } else if (getSelectedTrueLength() > 5) {
                printMsg("Please do not select more than 5 cards in each term.");
            } else {
                // disable();
                game.makeMove(getActivePlayer(), getcardIdx());
            }
            repaint();
        }
    }

    /**
     * an inner class that implements the ActionListener interface of the pass
     * buttom.
     * Add a actionPerformed() method from the ActionListener interface to handle
     * button-click events
     */
    private class PassButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // disable();
            game.makeMove(getActivePlayer(), getcardIdx());
            repaint();
        }
    }

    /**
     * an inner class that implements the ActionListener interface of the restart
     * buttom in the menu.
     * Add a actionPerformed() method from the ActionListener interface to handle
     * button-click events
     */
    private class RestartMenuItemListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // (i) create a new BigTwoDeck object and call its shuffle() method
            BigTwoDeck newBigTwoDeck = new BigTwoDeck();
            newBigTwoDeck.initialize();
            newBigTwoDeck.shuffle();
            // (ii) call the start() method of your BigTwo object with the BigTwoDeck object
            // as an argument.
            reset();
            game.start(newBigTwoDeck);
        }
    }

    /**
     * an inner class that implements the ActionListener interface of the quit
     * buttom in the menu.
     * Add a actionPerformed() method from the ActionListener interface to handle
     * button-click events
     */
    private class QuitMenuItemListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            printMsg("You have quit the game.");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            printMsg("You have quit the game.");

            System.exit(0);
        }
    }

    /**
     * an inner class that implements the ActionListener interface of the clear Text
     * Box
     * buttom in the menu.
     * Add a actionPerformed() method from the ActionListener interface to handle
     * button-click events
     */
    private class TextClearListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            clearMsgArea();
        }
    }

    /**
     * an inner class that implements the ActionListener interface of the clear chat
     * Box
     * buttom in the menu.
     * Add a actionPerformed() method from the ActionListener interface to handle
     * button-click events
     */
    private class ChatClearListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            clearchatArea();
        }
    }

    /**
     * an inner class that implements the ActionListener interface of the message
     * text input box
     * buttom in the menu.
     * Add a actionPerformed() method from the ActionListener interface to handle
     * button-click events
     */
    class chatAreaText extends JTextField implements ActionListener {

        private static final long serialVersionUID = 1L;

        public chatAreaText(int i) {
            super(i);
            addActionListener(this);
        }

        public void actionPerformed(ActionEvent e) {
            String chatMsg = getText();
            printchatArea("Player " + getActivePlayer() + ": " + chatMsg);
            this.setText("");
        }
    }

}