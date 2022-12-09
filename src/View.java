import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class View implements MouseListener, KeyListener, WindowListener {
    private int points = 0;
    private int resets = 0;
    private final Random random;
    private boolean nullsExists;
    private int clicked = 0;
    private boolean move = false;
    private final int[] position = new int[4];
    private final JLabel pointsLabel1;
    private final JLabel resettext;
    private final JLabel resettext1;
    private final JLabel resettext2;
    private final String[] icons = new String[2];
    private final JLabel[][] cursor = new JLabel[8][8];
    private final Gem[][] foreground = new Gem[8][8];
    private String formattedDate;
    private String username = "";
    private FileWriter writer;
    private static final String gameVersion = "MAk3 v1.03";
    private final String[] path = new String[]{
            "res/amber.png",
            "res/diamond.png",
            "res/emerald.png",
            "res/lapisLazuli.png",
            "res/pearl.png",
            "res/roseQuartz.png",
            "res/ruby.png"
    };
    Multiplier multiplier = new Multiplier();

    public View() {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        JLabel board = new JLabel();
        JLabel pointsLabel = new JLabel();
        pointsLabel1 = new JLabel();
        resettext = new JLabel();
        resettext1 = new JLabel();
        resettext2 = new JLabel();
        random = new Random();
        multiplier.start();
        LocalDateTime datetime = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
        formattedDate = datetime.format(format);

        File file = new File("txt/results.txt");
        while(username == null || username.length() == 0){
            username = JOptionPane.showInputDialog("Insert username:");
            if(username == null){
                int test = JOptionPane.showConfirmDialog(null, "Do you want to exit?", "Do you want to exit?", JOptionPane.YES_OPTION);
                if(test == 0){
                    System.exit(0);
                }
            }
        }

        try {
            writer = new FileWriter("txt/results.txt", true);
            writer.write(gameVersion + "\n");
            writer.write("Player: " + username + "\n");
            writer.write("Game started: " + formattedDate + "\n");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        try {
            file.createNewFile();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                cursor[i][j] = new JLabel();
                cursor[i][j].setBounds(i * 64, j * 64, 64, 64);
                Image image2 = new ImageIcon("res/cursor.png").getImage();
                cursor[i][j].setIcon(new ImageIcon(image2));
                cursor[i][j].setVisible(false);
                cursor[i][j].setLayout(null);
                board.add(cursor[i][j]);

                foreground[i][j] = new Gem(i * 64, j * 64, path[random.nextInt(7)]);
                foreground[i][j].getLabel().addMouseListener(this);
                board.add(foreground[i][j].getLabel());

                JLabel[][] background = new JLabel[8][8];
                background[i][j] = new JLabel();
                background[i][j].setBounds(i * 64, j * 64, 64, 64);
                Image image1 = new ImageIcon("res/tile.png").getImage();
                background[i][j].setIcon(new ImageIcon(image1));
                background[i][j].setVisible(true);
                background[i][j].setLayout(null);
                board.add(background[i][j]);
            }
        }

        panel.setBackground(Color.darkGray);
        panel.setOpaque(true);
        panel.setLayout(null);

        pointsLabel.setBounds(562, 125, 100, 25);
        pointsLabel.setOpaque(true);
        pointsLabel.setVisible(true);
        pointsLabel.setLayout(null);
        pointsLabel.setBackground(Color.darkGray);
        pointsLabel.setText("Points:");
        pointsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pointsLabel.setFont(new Font("Serif", Font.BOLD, 25));
        pointsLabel.setForeground(Color.white);
        panel.add(pointsLabel);

        pointsLabel1.setBounds(562, 175, 100, 25);
        pointsLabel1.setOpaque(true);
        pointsLabel1.setVisible(true);
        pointsLabel1.setLayout(null);
        pointsLabel1.setBackground(Color.darkGray);
        pointsLabel1.setText("0");
        pointsLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        pointsLabel1.setFont(new Font("Serif", Font.BOLD, 25));
        pointsLabel1.setForeground(Color.white);
        panel.add(pointsLabel1);

        resettext.setBounds(575, 350, 100, 25);
        resettext.setOpaque(true);
        resettext.setVisible(true);
        resettext.setLayout(null);
        resettext.setBackground(Color.darkGray);
        resettext.setText("Press R");
        resettext.setFont(new Font("Serif", Font.BOLD, 20));
        resettext.setForeground(Color.white);
        panel.add(resettext);
        resettext1.setBounds(575, 375, 100, 25);
        resettext1.setOpaque(true);
        resettext1.setVisible(true);
        resettext1.setLayout(null);
        resettext1.setBackground(Color.darkGray);
        resettext1.setText("to reset");
        resettext1.setFont(new Font("Serif", Font.BOLD, 20));
        resettext1.setForeground(Color.white);
        panel.add(resettext1);
        resettext2.setBounds(575, 400, 100, 25);
        resettext2.setOpaque(true);
        resettext2.setVisible(true);
        resettext2.setLayout(null);
        resettext2.setBackground(Color.darkGray);
        resettext2.setText("the board");
        resettext2.setFont(new Font("Serif", Font.BOLD, 20));
        resettext2.setForeground(Color.white);
        panel.add(resettext2);

        board.setBounds(25, 25, 512, 512);
        board.setOpaque(true);
        board.setVisible(true);
        board.setLayout(null);
        panel.add(board);

        Image image = new ImageIcon("res/icon.png").getImage();
        frame.setIconImage(image);
        frame.setSize(700, 600);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.addKeyListener(this);
        frame.addWindowListener(this);
        frame.setTitle("Mak3");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(panel);
        checkBoard();
    }

    private void checkBoard() {
        do {
            checkMap();
            if (nullsExists) {
                for (int i = 0; i < 8; i++) {
                    dropRowDown(i);
                }
            }
        } while (nullsExists);
    }

    private void resetBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                foreground[i][j].setIcon(path[random.nextInt(7)]);
                cursor[i][j].setVisible(false);
            }
        }
        for (int i = 0; i < 4; i++) {
            position[i] = 0;
            if (i <= 1) {
                icons[i] = null;
            }
        }
        move = false;
        clicked = 0;
        checkBoard();
        //debug(position, icons);
    }

    private void checkBorderToRight(int[] position) {
        //System.out.println("checkbordertoright");
        move = Objects.equals(foreground[position[0]][position[1]].getPath(), foreground[position[2] + 1][position[3]].getPath()) &&
                Objects.equals(foreground[position[0]][position[1]].getPath(), foreground[position[2] + 2][position[3]].getPath());
        if (!move) {
            move = Objects.equals(foreground[position[2]][position[3]].getPath(), foreground[position[0] + 1][position[1]].getPath()) &&
                    Objects.equals(foreground[position[2]][position[3]].getPath(), foreground[position[0] + 2][position[1]].getPath());
        }
    }

    private void checkBorderToLeft(int[] position) {
        //System.out.println("checkbordertoleft");
        move = Objects.equals(foreground[position[0]][position[1]].getPath(), foreground[position[2] - 1][position[3]].getPath()) &&
                Objects.equals(foreground[position[0]][position[1]].getPath(), foreground[position[2] - 2][position[3]].getPath());
        if (!move) {
            move = Objects.equals(foreground[position[2]][position[3]].getPath(), foreground[position[0] - 1][position[1]].getPath()) &&
                    Objects.equals(foreground[position[2]][position[3]].getPath(), foreground[position[0] - 2][position[1]].getPath());
        }
    }

    private void checkCenters(int[] position) {
        //System.out.println("checkcenters");
        move = Objects.equals(foreground[position[0]][position[1]].getPath(), foreground[position[2] - 1][position[3]].getPath()) &&
                Objects.equals(foreground[position[0]][position[1]].getPath(), foreground[position[2] + 1][position[3]].getPath());
        if (!move) {
            move = Objects.equals(foreground[position[2]][position[3]].getPath(), foreground[position[0] - 1][position[1]].getPath()) &&
                    Objects.equals(foreground[position[2]][position[3]].getPath(), foreground[position[0] + 1][position[1]].getPath());
        }
    }

    private void checkAnotherCenters(int[] position) {
       // System.out.println("checkanothercenters");
        move = Objects.equals(foreground[position[0]][position[1]].getPath(), foreground[position[2]][position[3] - 1].getPath()) &&
                Objects.equals(foreground[position[0]][position[1]].getPath(), foreground[position[2]][position[3] + 1].getPath());
        if (!move) {
            move = Objects.equals(foreground[position[2]][position[3]].getPath(), foreground[position[0]][position[1] - 1].getPath()) &&
                    Objects.equals(foreground[position[2]][position[3]].getPath(), foreground[position[0]][position[1] + 1].getPath());
        }
    }

    private void checkBorderVerticallyUp(int[] position) {
        //System.out.println("checkverticallyup");
            move = Objects.equals(foreground[position[0]][position[1]].getPath(), foreground[position[2]][position[3] - 1].getPath()) &&
                    Objects.equals(foreground[position[0]][position[1]].getPath(), foreground[position[2]][position[3] - 2].getPath());
            if(!move){
                move = Objects.equals(foreground[position[0]][position[1]].getPath(), foreground[position[2]][position[3] + 1].getPath()) &&
                        Objects.equals(foreground[position[0]][position[1]].getPath(), foreground[position[2]][position[3] + 2].getPath());
            }
        }

    private void checkBorderVerticallyDown(int[] position) {
        //System.out.println("checkverticalldown");
        move = Objects.equals(foreground[position[0]][position[1]].getPath(), foreground[position[2]][position[3] + 1].getPath()) &&
                Objects.equals(foreground[position[0]][position[1]].getPath(), foreground[position[2]][position[3] + 2].getPath());
        if(!move){
            move = Objects.equals(foreground[position[2]][position[3]].getPath(), foreground[position[0]][position[1] + 1].getPath()) &&
                    Objects.equals(foreground[position[2]][position[3]].getPath(), foreground[position[0]][position[1] + 2].getPath());
        }
    }

    private void checkVertically(int[] position) {
        //System.out.println("checkvertically");
        if (position[1] <= 1) {
            checkBorderVerticallyDown(position);
        }
        if(position[1] == 2 && position[3] > 2){
            checkBorderVerticallyUp(position);
            if(!move){
                checkBorderVerticallyDown(position);
            }
        }else if(position[1] == 2 && position[3] < 2){
            checkBorderVerticallyDown(position);
        }
        if(position[1] == 5 && position[3] > 5){
            checkBorderVerticallyUp(position);
        }else if(position[1] == 5 && position[3] < 5){
            checkBorderVerticallyDown(position);
            if(!move){
                checkBorderVerticallyUp(position);
            }
        }
        if (position[1] >= 6) {
            checkBorderVerticallyUp(position);
        }
        if (position[1] > 2 && position[1] < 5) {
            checkBorderVerticallyUp(position);
            if (!move) {
                checkBorderVerticallyDown(position);
            }
        }
    }

    private void checkDown(int[] position) {
        //System.out.println("checkdown");
        move = Objects.equals(foreground[position[0]][position[1]].getPath(), foreground[position[2]][position[3] + 1].getPath()) &&
                Objects.equals(foreground[position[0]][position[1]].getPath(), foreground[position[2]][position[3] + 2].getPath());
        if (!move) {
            move = Objects.equals(foreground[position[2]][position[3]].getPath(), foreground[position[0]][position[1] + 1].getPath()) &&
                    Objects.equals(foreground[position[2]][position[3]].getPath(), foreground[position[0]][position[1] + 2].getPath());
        }
    }

    private void checkUp(int[] position) {
       // System.out.println("checkup");
        move = Objects.equals(foreground[position[0]][position[1]].getPath(), foreground[position[2]][position[3] - 1].getPath()) &&
                Objects.equals(foreground[position[0]][position[1]].getPath(), foreground[position[2]][position[3] - 2].getPath());
        if (!move) {
            move = Objects.equals(foreground[position[2]][position[3]].getPath(), foreground[position[0]][position[1] - 1].getPath()) &&
                    Objects.equals(foreground[position[2]][position[3]].getPath(), foreground[position[0]][position[1] - 2].getPath());
        }
    }

        private void checkHorizontallyRight ( int[] position){
           // System.out.println("checkhorizontallyright");
            move = Objects.equals(foreground[position[0]][position[1]].getPath(), foreground[position[2] + 1][position[3]].getPath()) &&
                    Objects.equals(foreground[position[0]][position[1]].getPath(), foreground[position[2] + 2][position[3]].getPath());
            if(!move){
                move = Objects.equals(foreground[position[0]][position[1]].getPath(), foreground[position[2] - 1][position[3]].getPath()) &&
                        Objects.equals(foreground[position[0]][position[1]].getPath(), foreground[position[2] - 2][position[3]].getPath());
            }
        }

        private void checkHorizontallyLeft ( int[] position){
           // System.out.println("checkhorizontallyleft");
            move = Objects.equals(foreground[position[0]][position[1]].getPath(), foreground[position[2] - 1][position[3]].getPath()) &&
                    Objects.equals(foreground[position[0]][position[1]].getPath(), foreground[position[2] - 2][position[3]].getPath());
            if(!move){
                move = Objects.equals(foreground[position[2]][position[3]].getPath(), foreground[position[0] - 1][position[1]].getPath()) &&
                        Objects.equals(foreground[position[2]][position[3]].getPath(), foreground[position[0] - 2][position[1]].getPath());
            }
        }

        private void checkHorizontally ( int[] position){
            //System.out.println("checkhorizontally");
            if (position[0] <= 1) {
                checkHorizontallyRight(position);
            }
            if(position[0] == 2 && position[2] > 2){
                checkHorizontallyRight(position);
                if(!move){
                    checkHorizontallyLeft(position);
                }
            }else if(position[0] == 2 && position[2] < 2){
                checkHorizontallyRight(position);
            }
            if(position[0] == 5 && position[2] > 5){
                checkHorizontallyLeft(position);
            }else if(position[0] == 5 && position[2] < 5){
                checkHorizontallyRight(position);
                if(!move){
                    checkHorizontallyLeft(position);
                }
            }
            if (position[0] >= 6) {
                checkHorizontallyLeft(position);
            }
            if (position[0] > 2 && position[0] < 5) {
                checkHorizontallyRight(position);
                if (!move) {
                    checkHorizontallyLeft(position);
                }
            }
        }

        private void checkMove (int[] position){
            if (position[0] == position[2]) {
                if (position[0] <= 1) {
                    checkBorderToRight(position);
                    if (!move) {
                        checkVertically(position);
                    }
                    if(!move){
                        checkCenters(position);
                    }
                }
                if (position[0] == 2) {
                    checkCenters(position);
                    if (!move) {
                        checkBorderToRight(position);
                    }
                    if(!move){
                        checkBorderToLeft(position);
                    }
                    if (!move) {
                        checkVertically(position);
                    }
                }
                if (position[0] == 5) {
                    checkCenters(position);
                    if (!move) {
                        checkBorderToLeft(position);
                    }
                    if(!move){
                        checkBorderToRight(position);
                    }
                    if (!move) {
                        checkVertically(position);
                    }
                }
                if (position[0] >= 6) {
                    checkBorderToLeft(position);
                    if (!move) {
                        checkVertically(position);
                    }
                    if(!move){
                        checkCenters(position);
                    }
                }
                if (position[0] > 2 && position[0] < 5) {
                    checkBorderToRight(position);
                    if (!move) {
                        checkBorderToLeft(position);
                    }
                    if (!move) {
                        checkVertically(position);
                    }
                    if (!move) {
                        checkCenters(position);
                    }
                }
            }
            if (position[1] == position[3]) {
                if (position[1] <= 1) {
                    checkDown(position);
                    if (!move) {
                        checkHorizontally(position);
                    }
                    if(!move){
                        checkAnotherCenters(position);
                    }
                }
                if (position[1] == 2) {
                    checkAnotherCenters(position);
                    if (!move) {
                        checkDown(position);
                    }
                    if(!move){
                        checkUp(position);
                    }
                    if (!move) {
                        checkHorizontally(position);
                    }
                }
                if (position[1] == 5) {
                    checkAnotherCenters(position);
                    if (!move) {
                        checkUp(position);
                    }
                    if(!move){
                        checkDown(position);
                    }
                    if (!move) {
                        checkHorizontally(position);
                    }
                }
                if (position[1] >= 6) {
                    checkUp(position);
                    if (!move) {
                        checkHorizontally(position);
                    }
                    if(!move){
                        checkAnotherCenters(position);
                    }
                }
                if (position[1] > 2 && position[1] < 5) {
                    checkDown(position);
                    if (!move) {
                        checkUp(position);
                    }
                    if (!move) {
                        checkHorizontally(position);
                    }
                    if (!move) {
                        checkAnotherCenters(position);
                    }
                }
            }
        }

        private void checkMap () {
            List<Integer> tabY = new ArrayList<>();
            List<Integer> tabX = new ArrayList<>();
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 6; j++) {
                    if (Objects.equals(foreground[i][j].getPath(), foreground[i][j + 1].getPath()) &&
                            Objects.equals(foreground[i][j + 1].getPath(), foreground[i][j + 2].getPath())) {
                        tabY.add(i);
                        tabY.add(j);
                        tabY.add(i);
                        tabY.add(j + 1);
                        tabY.add(i);
                        tabY.add(j + 2);
                    }
                }
            }
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 8; j++) {
                    if (Objects.equals(foreground[i][j].getPath(), foreground[i + 1][j].getPath()) &&
                            Objects.equals(foreground[i + 1][j].getPath(), foreground[i + 2][j].getPath())) {
                        tabX.add(i);
                        tabX.add(j);
                        tabX.add(i + 1);
                        tabX.add(j);
                        tabX.add(i + 2);
                        tabX.add(j);

                    }
                }
            }
            nullsExists = false;
            for (int i = 0; i < tabX.size(); i += 2) {
                foreground[tabX.get(i)][tabX.get(i + 1)].setIcon(null);
                nullsExists = true;
            }
            for (int i = 0; i < tabY.size(); i += 2) {
                foreground[tabY.get(i)][tabY.get(i + 1)].setIcon(null);
                nullsExists = true;
            }
        }

        private void dropDown ( int x){
            for (int i = 7; i > 0; i--) {
                if (foreground[x][i].getPath() == null) {
                    foreground[x][i].setIcon(foreground[x][i - 1].getPath());
                    foreground[x][i - 1].setIcon(null);
                }
            }
            if (foreground[x][0].getPath() == null) {
                int n = random.nextInt(7);
                foreground[x][0].setIcon(path[n]);
            }
        }

        private void dropRowDown ( int x){
            int nulls = 0;
            for (int i = 0; i < 8; i++) {
                if (foreground[x][i].getPath() == null) {
                    nulls++;
                }
            }
            for (int i = 0; i < nulls; i++) {
                dropDown(x);
            }
        }

        public void debug ( int[] position, String[] icons){
            System.out.println(position[0] + "," + position[1] + "," + position[2] + "," + position[3]);
            System.out.println(icons[0]);
            System.out.println(icons[1]);
            System.out.println("Move = " + move);
            System.out.println("Clicked = " + clicked);
        }

        @Override
        public void mouseClicked (MouseEvent e){
            String text = e.getSource().toString().substring(20, 27).replace(",", " ");
            String[] tab = text.split(" ");
            if (tab.length == 3) {
                Arrays.copyOf(tab, tab.length - 1);
            }
            int x = Integer.parseInt(tab[0]) / 64;
            int y = Integer.parseInt(tab[1]) / 64;
            if (clicked == 0) {
                icons[0] = foreground[x][y].getPath();
                cursor[x][y].setVisible(true);
                position[0] = x;
                position[1] = y;
                clicked = 1;
            } else {
                //debug(position, icons);
                icons[1] = foreground[x][y].getPath();

                position[2] = x;
                position[3] = y;
                clicked = 0;

                checkMove(position);
                if (move) {
                    //debug(position, icons);
                    foreground[position[0]][position[1]].setIcon(icons[1]);
                    foreground[position[2]][position[3]].setIcon(icons[0]);
                    points += 100 * multiplier.getMulti();
                    pointsLabel1.setText(String.valueOf(points));
                    multiplier.setI(100);
                    move = false;
                    checkBoard();
                }
                cursor[position[0]][position[1]].setVisible(false);
                move = false;
            }
        }

        @Override
        public void mousePressed (MouseEvent e){

        }

        @Override
        public void mouseReleased (MouseEvent e){

        }

        @Override
        public void mouseEntered (MouseEvent e){

        }

        @Override
        public void mouseExited (MouseEvent e){

        }

        @Override
        public void keyTyped (KeyEvent e){
            if (e.getKeyChar() == 'R' || e.getKeyChar() == 'r') {
                resets++;
                resetBoard();
            }
        }
    @Override
    public void keyPressed (KeyEvent e){

    }

    @Override
    public void keyReleased (KeyEvent e){

    }

    @Override
    public void windowOpened (WindowEvent e){

    }


        @Override
        public void windowClosing (WindowEvent e){
            LocalDateTime datetime = LocalDateTime.now();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
            formattedDate = datetime.format(format);
            try {
                writer.write("Game finished: " + formattedDate + "\n");
                writer.write("Points: " + points + "\n");
                writer.write("Times reseted: " + resets + "\n" + "\n");
                writer.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        @Override
        public void windowClosed (WindowEvent e){

        }

        @Override
        public void windowIconified (WindowEvent e){

        }

        @Override
        public void windowDeiconified (WindowEvent e){

        }

        @Override
        public void windowActivated (WindowEvent e){

        }

        @Override
        public void windowDeactivated (WindowEvent e){

        }
    }
