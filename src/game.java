
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.awt.geom.Point2D;
import java.awt.Font;
import java.awt.FontFormatException;
import java.util.Random;
import java.util.ArrayList;

public class game extends Board implements WindowListener, KeyListener, MouseListener, MouseMotionListener {

    private JFrame mainWindow;
    private BufferStrategy bufferStrategy;
    private boolean active;
    HashMap<Integer, Boolean> keyPool;
    private int i, j, k, l, a, b, x, y;
    private JPanel mainPanel;
    private int mouseX, mouseY, swap, size;
    private int previousMouseX, previousMouseY, afterMouseX, afterMouseY;
    private boolean pieceSelected, time, stopSelection, needCapture, begin, instruction, capture, analyse, choose, player, twoPlayer, aleatory, mustCapture, remain;
    private int[] pieces;
    private int[] whereCapture, capCOM;
    private int[][] helpBoard, saveXYpositions, positions;
    private String victory;
    private Random random;

    public game() {

        mainWindow = new JFrame();

        mainWindow.addKeyListener(this);
        mainWindow.addMouseListener(this);
        mainWindow.addMouseMotionListener(this);

        mainWindow.setSize(400, 400);
        
        keyPool = new HashMap<Integer, Boolean>();

        pieceSelected = false;
        stopSelection = false;
        needCapture = false;
        whereCapture = new int[4];
        capCOM = new int[5];
        instruction = false;
        capture = false;
        analyse = true;
        choose = false;
        aleatory = false;
        mustCapture = false;
        remain = false;

        helpBoard = new int[8][8];
        saveXYpositions = new int[12][2];
        positions = new int[10][5];

        victory = new String();
        victory = "None";

        active = true;
        twoPlayer = false;

        // true  = White plays
        // false = Black plays
        time = true;
        begin = false;
        random = new Random();

        // Register this class (graphic) as listener of the window's event.
        mainWindow.addWindowListener(this);
    }

    public void welcome() {

        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();

        g.setColor(new Color(100, 100, 100));

        g.fillRect(0, 0, 400, 400);

        g.setColor(new Color(255, 102, 0));

        g.setFont(new Font("TimesRoman", Font.PLAIN, 40));

        g.drawString("Welcome to", 80, 40);
        g.drawString("Checkers!", 100, 90);

        g.setColor(new Color(102, 0, 200));
        g.fillRect(130, 120, 150, 70);
        g.fillRect(130, 220, 150, 70);
        g.fillRect(130, 320, 150, 70);

        g.setColor(new Color(255, 255, 0));
        g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
        g.drawString("P1 vs P2", 140, 163);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 25));
        g.drawString("P1 vs COM", 135, 263);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        g.drawString("Instructions", 147, 362);

        // Release the object graphics.
        g.dispose();

        // Ask for the buffer strategy show what was drawn above.
        bufferStrategy.show();

        if (keyPool.get(MouseEvent.BUTTON1) != null) {
            if(mouseX >= 130 && mouseX <= (130+150) && mouseY >= 120 && mouseY <= (120+70)) {
                begin = true;
/*                previousMouseX = 0;
                previousMouseY = 0;
                afterMouseX = 0;
                afterMouseY = 0;
*/                if(!twoPlayer) begin(true);
                twoPlayer = true;
                time = true;
                choose = false;
                victory = "None";
            }
            else if(mouseX >= 130 && mouseX <= (130+150) && mouseY >= 320 && mouseY <= (320+70)) instruction = true;
            else if(mouseX >= 130 && mouseX <= (130+150) && mouseY >= 220 && mouseY <= (220+70)) {
                if(twoPlayer) begin(true);
                choose = true;
                begin = false;
            }
        }
    }

    void moveHelp(int board[][], int x, int y, int x1, int y1){

        if(board[x1][y1] == 0){

            swap = board[x][y];
            board[x][y] = 0;
            board[x1][y1] = swap;

        }

    }

    void Movehelp(int x, int y, int x1, int y1, int blank){

        if(blank == -1) return;

        if(board[x1][y1].type == 0){

            swap = board[x][y].type;
            board[x][y].type = 0;
            board[x1][y1].type = swap;

        }

        if(blank == 0) board[x+1][y-1].type = 0;
        else if(blank == 1) board[x+1][y+1].type = 0;
        else if(blank == 2) board[x-1][y-1].type = 0;
        else if(blank == 3) board[x-1][y+1].type = 0;

        king();
    }

    private void anotherMove(int board[][], int x, int y, int x1, int y1){

        couldMove = false;

        if(x1 <= 7 && x1 >= 0 && y1 <= 7 && y1 >= 0) {

            if(board[x][y] == 1 || board[x][y] == 2 || board[x][y] == 4){

                if(x >= 0 && x < 7){

                    if(y > 0 && y < 7){

                        if(x + 1 == x1){

                            if((y + 1 == y1) || (y - 1 == y1)){

                                moveHelp(board, x, y, x1, y1);
                                couldMove = true;

                            }
                        }       

                    }else if (y == 0){

                        if(x + 1 == x1){

                            if(y + 1 == y1){


                                moveHelp(board, x, y, x1, y1);
                                couldMove = true;

                            }
                        }

                    }else if(y == 7){

                        if(x + 1 == x1){

                            if(y - 1 == y1){

                                moveHelp(board, x, y, x1, y1);
                                couldMove = true;

                            }

                        }

                    }

                }

            }

            if(board[x][y] == 2 || board[x][y] == 3 || board[x][y] == 4){

                if(x > 0 && x <= 7){

                    if(y > 0 && y < 7){

                        if(x - 1 == x1){

                            if((y + 1 == y1) || (y - 1 == y1)){

                                moveHelp(board, x, y, x1, y1);
                                couldMove = true;
                            }
                        }       

                    }else if (y == 0){

                        if(x - 1 == x1){

                            if(y + 1 == y1){

                                moveHelp(board, x, y, x1, y1);
                                couldMove = true;

                            }
                        }

                    }else if(y == 7){

                        if(x - 1 == x1){

                            if(y - 1 == y1){

                                moveHelp(board, x, y, x1, y1);
                                couldMove = true;

                            }

                        }

                    }

                }

            }
        }
    }

    private boolean anotherAnalysisCapture(int board[][]) {

        for(a = 0; a < 8; a++) {
            for(b = 0; b < 8; b++) {

                whereCapture = ifCapture(a, b, 0);

                for(k = 0; k < 4; k++) {

                    if(whereCapture[k] == 1) {

                        // Men pieces

                        if(board[a][b] == 1 && time) {

                            if(k == 0) return true;
                            if(k == 1) return true;
                        }

                        if(board[a][b] == 3 && !time) {

                            if(k == 2) return true;
                            if(k == 3) return true;
                        }

                        // King pieces

                        if(board[a][b] == 2 && time) {

                            if(k == 0) return true;
                            if(k == 1) return true;
                            if(k == 2) return true;
                            if(k == 3) return true;
                        }

                        if(board[a][b] == 4 && !time) {
                            if(k == 0) return true;
                            if(k == 1) return true;
                            if(k == 2) return true;
                            if(k == 3) return true;

                        }

                    }

                }
            }
        }

        return false;
    }

    private boolean analysisDraw(boolean time) {
        for(i = 0; i < 8; i++)
            for(j = 0; j < 8; j++)
                helpBoard[i][j] = board[i][j].type;

        if(!time) {
            for(i = 0; i < 8; i++) {
                for(j = 0; j < 8; j++) {

                    if(helpBoard[j][i] == 3) {

                        if(j > 0 && i > 0 && helpBoard[j-1][i-1] == 0) return false;
                        if(j > 0 && i < 7 && helpBoard[j-1][i+1] == 0) return false;

                    } else if (helpBoard[j][i] == 4) {

                        if(j < 7 && i > 0 && helpBoard[j+1][i-1] == 0) return false;
                        if(j < 7 && i < 7 && helpBoard[j+1][i+1] == 0) return false;
                        if(j > 0 && i > 0 && helpBoard[j-1][i-1] == 0) return false;
                        if(j > 0 && i < 7 && helpBoard[j-1][i+1] == 0) return false;
                    }
                }
            }
        } else {
            for(i = 0; i < 8; i++) {
                for(j = 0; j < 8; j++) {

                    if(helpBoard[j][i] == 1) {

                        if(j < 7 && i > 0 && helpBoard[j+1][i-1] == 0) return false;
                        if(j < 7 && i < 7 && helpBoard[j+1][i+1] == 0) return false;

                    } else if (helpBoard[j][i] == 2) {

                        if(j < 7 && i > 0 && helpBoard[j+1][i-1] == 0) return false;
                        if(j < 7 && i < 7 && helpBoard[j+1][i+1] == 0) return false;
                        if(j > 0 && i > 0 && helpBoard[j-1][i-1] == 0) return false;
                        if(j > 0 && i < 7 && helpBoard[j-1][i+1] == 0) return false;
                    }
                }
            }
        }

        return true;
    }

    // Black is zero

    private boolean checkVector(int x, int y) {

        for(a = 0; a < 8; a++)
            if(saveXYpositions[a][0] == x && saveXYpositions[a][1] == y) return false;

        return true;
    }

    private boolean isFill(int matriz[][], int tam1, int tam2) {
        for(a = 0; a < tam1; a++)
            for(b = 0; b < tam2; b++)
                if(matriz[a][b] == -1) return false;

        return true;
    }

    private void moveCOMblack() {

        couldMove = false;

        for(a = 0, size = 0; a < 8; a++)
            for(b = 0; b < 8; b++)
                if(board[a][b].type == 3 || board[a][b].type == 4) size++;

        for(i = 0; i < 8; i++)
            for(j = 0; j < 2; j++)
                saveXYpositions[i][j] = -1;

        while(!couldMove) {

            mouseX = random.nextInt(8);
            mouseY = random.nextInt(8);

            k = 0;

            while(board[mouseY][mouseX].type != 3 && board[mouseY][mouseX].type != 4) {

                mouseX = random.nextInt(8);
                mouseY = random.nextInt(8);

                if(isFill(saveXYpositions, size, 2)) {
                    victory = "Draw";
                    return;
                }

                if(board[mouseY][mouseX].type == 3 || board[mouseY][mouseX].type == 4 && checkVector(mouseX, mouseY)) {
                    saveXYpositions[k][0] = mouseX;
                    saveXYpositions[k][1] = mouseY;
                }

                k = (k + 1) % 12;

            }

            for(i = mouseY; i < 8; i++) {

                if(board[i][mouseX].type == 4) {

                    j = random.nextInt(4);

                    if(j == 0) {

                            move(i, mouseX, i + 1, mouseX - 1);

                            if(couldMove) return;

                            move(i, mouseX, i + 1, mouseX + 1);

                            if(couldMove) return;

                            move(i, mouseX, i - 1, mouseX - 1);

                            if(couldMove) return;

                            move(i, mouseX, i - 1, mouseX + 1);

                            if(couldMove) return;

                    } else if(j == 1) {

                            move(i, mouseX, i + 1, mouseX + 1);

                            if(couldMove) return;

                            move(i, mouseX, i + 1, mouseX - 1);

                            if(couldMove) return;

                            move(i, mouseX, i - 1, mouseX - 1);

                            if(couldMove) return;

                            move(i, mouseX, i - 1, mouseX + 1);

                            if(couldMove) return;

                    } else if(j == 2) {

                            move(i, mouseX, i - 1, mouseX - 1);

                            if(couldMove) return;

                            move(i, mouseX, i + 1, mouseX + 1);

                            if(couldMove) return;

                            move(i, mouseX, i + 1, mouseX - 1);

                            if(couldMove) return;

                            move(i, mouseX, i - 1, mouseX + 1);

                            if(couldMove) return;

                    } else if(j == 3) {

                            move(i, mouseX, i - 1, mouseX + 1);

                            if(couldMove) return;

                            move(i, mouseX, i - 1, mouseX - 1);

                            if(couldMove) return;

                            move(i, mouseX, i + 1, mouseX - 1);

                            if(couldMove) return;

                            move(i, mouseX, i + 1, mouseX + 1);

                            if(couldMove) return;

                    }
                } else if(board[i][mouseX].type == 3) {

                    j = random.nextInt(2);

                    if(j == 0) {

                            move(i, mouseX, i - 1, mouseX - 1);

                            if(couldMove) return;

                            move(i, mouseX, i - 1, mouseX + 1);

                            if(couldMove) return;

                    } else if(j == 1) {

                            move(i, mouseX, i - 1, mouseX + 1);

                            if(couldMove) return;

                            move(i, mouseX, i - 1, mouseX - 1);

                            if(couldMove) return;
                        }
                    }
                }
            }

            for(i = mouseY; i > -1; i--) {

                if(board[i][mouseX].type == 4) {

                    j = random.nextInt(4);

                    if(j == 0) {

                            move(i, mouseX, i + 1, mouseX - 1);

                            if(couldMove) return;

                            move(i, mouseX, i + 1, mouseX + 1);

                            if(couldMove) return;

                            move(i, mouseX, i - 1, mouseX - 1);

                            if(couldMove) return;

                            move(i, mouseX, i - 1, mouseX + 1);

                            if(couldMove) return;

                    } else if(j == 1) {

                            move(i, mouseX, i + 1, mouseX + 1);

                            if(couldMove) return;

                            move(i, mouseX, i + 1, mouseX - 1);

                            if(couldMove) return;

                            move(i, mouseX, i - 1, mouseX - 1);

                            if(couldMove) return;

                            move(i, mouseX, i - 1, mouseX + 1);

                            if(couldMove) return;

                    } else if(j == 2) {

                            move(i, mouseX, i - 1, mouseX - 1);

                            if(couldMove) return;

                            move(i, mouseX, i + 1, mouseX + 1);

                            if(couldMove) return;

                            move(i, mouseX, i + 1, mouseX - 1);

                            if(couldMove) return;

                            move(i, mouseX, i - 1, mouseX + 1);

                            if(couldMove) return;

                    } else if(j == 3) {

                            move(i, mouseX, i - 1, mouseX + 1);

                            if(couldMove) return;

                            move(i, mouseX, i - 1, mouseX - 1);

                            if(couldMove) return;

                            move(i, mouseX, i + 1, mouseX - 1);

                            if(couldMove) return;

                            move(i, mouseX, i + 1, mouseX + 1);

                            if(couldMove) return;

                    }
                } else if(board[i][mouseX].type == 3) {

                    j = random.nextInt(2);

                    if(j == 0) {

                        move(i, mouseX, i - 1, mouseX - 1);

                        if(couldMove) return;

                        move(i, mouseX, i - 1, mouseX + 1);

                        if(couldMove) return;

                    } else if(j == 1) {

                        move(i, mouseX, i - 1, mouseX + 1);

                        if(couldMove) return;

                        move(i, mouseX, i - 1, mouseX - 1);

                        if(couldMove) return;

                    }
                }
            }

            // System.out.println("mouseX -> " + mouseX + " & " + "mouseY -> " + mouseY);
    }

   private void moveCOMwhite() {

        couldMove = false;

        for(a = 0, size = 0; a < 8; a++)
            for(b = 0; b < 8; b++)
                if(board[a][b].type == 3 || board[a][b].type == 4) size++;

        for(i = 0; i < 8; i++)
            for(j = 0; j < 2; j++)
                saveXYpositions[i][j] = -1;

        while(!couldMove) {

            mouseX = random.nextInt(8);
            mouseY = random.nextInt(8);

            k = 0;

            while(board[mouseY][mouseX].type != 1 && board[mouseY][mouseX].type != 2) {

                mouseX = random.nextInt(8);
                mouseY = random.nextInt(8);

                if(isFill(saveXYpositions, size, 2)) {
                    victory = "Draw";
                    return;
                }

                if(board[mouseY][mouseX].type == 1 || board[mouseY][mouseX].type == 2 && checkVector(mouseX, mouseY)) {
                    saveXYpositions[k][0] = mouseX;
                    saveXYpositions[k][1] = mouseY;
                }

                k = (k + 1) % 12;

            }

            for(i = mouseY; i < 8; i++) {

                if(board[i][mouseX].type == 2) {

                    j = random.nextInt(4);

                    if(j == 0) {

                            move(i, mouseX, i + 1, mouseX - 1);

                            if(couldMove) return;

                            move(i, mouseX, i + 1, mouseX + 1);

                            if(couldMove) return;

                            move(i, mouseX, i - 1, mouseX - 1);

                            if(couldMove) return;

                            move(i, mouseX, i - 1, mouseX + 1);

                            if(couldMove) return;

                    } else if(j == 1) {

                            move(i, mouseX, i + 1, mouseX + 1);

                            if(couldMove) return;

                            move(i, mouseX, i + 1, mouseX - 1);

                            if(couldMove) return;

                            move(i, mouseX, i - 1, mouseX - 1);

                            if(couldMove) return;

                            move(i, mouseX, i - 1, mouseX + 1);

                            if(couldMove) return;

                    } else if(j == 2) {

                            move(i, mouseX, i - 1, mouseX - 1);

                            if(couldMove) return;

                            move(i, mouseX, i + 1, mouseX + 1);

                            if(couldMove) return;

                            move(i, mouseX, i + 1, mouseX - 1);

                            if(couldMove) return;

                            move(i, mouseX, i - 1, mouseX + 1);

                            if(couldMove) return;

                    } else if(j == 3) {

                            move(i, mouseX, i - 1, mouseX + 1);

                            if(couldMove) return;

                            move(i, mouseX, i - 1, mouseX - 1);

                            if(couldMove) return;

                            move(i, mouseX, i + 1, mouseX - 1);

                            if(couldMove) return;

                            move(i, mouseX, i + 1, mouseX + 1);

                            if(couldMove) return;

                    }
                } else if(board[i][mouseX].type == 1) {

                    j = random.nextInt(2);

                    if(j == 0) {

                            move(i, mouseX, i + 1, mouseX - 1);

                            if(couldMove) return;

                            move(i, mouseX, i + 1, mouseX + 1);

                            if(couldMove) return;

                    } else if(j == 1) {

                            move(i, mouseX, i + 1, mouseX + 1);

                            if(couldMove) return;

                            move(i, mouseX, i + 1, mouseX - 1);

                            if(couldMove) return;
                        }
                    }
                }
            }

            for(i = mouseY; i > -1; i--) {

                if(board[i][mouseX].type == 2) {

                    j = random.nextInt(4);

                    if(j == 0) {

                            move(i, mouseX, i + 1, mouseX - 1);

                            if(couldMove) return;

                            move(i, mouseX, i + 1, mouseX + 1);

                            if(couldMove) return;

                            move(i, mouseX, i - 1, mouseX - 1);

                            if(couldMove) return;

                            move(i, mouseX, i - 1, mouseX + 1);

                            if(couldMove) return;

                    } else if(j == 1) {

                            move(i, mouseX, i + 1, mouseX + 1);

                            if(couldMove) return;

                            move(i, mouseX, i + 1, mouseX - 1);

                            if(couldMove) return;

                            move(i, mouseX, i - 1, mouseX - 1);

                            if(couldMove) return;

                            move(i, mouseX, i - 1, mouseX + 1);

                            if(couldMove) return;

                    } else if(j == 2) {

                            move(i, mouseX, i - 1, mouseX - 1);

                            if(couldMove) return;

                            move(i, mouseX, i + 1, mouseX + 1);

                            if(couldMove) return;

                            move(i, mouseX, i + 1, mouseX - 1);

                            if(couldMove) return;

                            move(i, mouseX, i - 1, mouseX + 1);

                            if(couldMove) return;

                    } else if(j == 3) {

                            move(i, mouseX, i - 1, mouseX + 1);

                            if(couldMove) return;

                            move(i, mouseX, i - 1, mouseX - 1);

                            if(couldMove) return;

                            move(i, mouseX, i + 1, mouseX - 1);

                            if(couldMove) return;

                            move(i, mouseX, i + 1, mouseX + 1);

                            if(couldMove) return;

                    }
                } else if(board[i][mouseX].type == 1) {

                    j = random.nextInt(2);

                    if(j == 0) {

                        move(i, mouseX, i + 1, mouseX - 1);

                        if(couldMove) return;

                        move(i, mouseX, i + 1, mouseX + 1);

                        if(couldMove) return;

                    } else if(j == 1) {

                        move(i, mouseX, i + 1, mouseX + 1);

                        if(couldMove) return;

                        move(i, mouseX, i + 1, mouseX - 1);

                        if(couldMove) return;

                    }
                }
            }

            // System.out.println("mouseX -> " + mouseX + " & " + "mouseY -> " + mouseY);
    }
    
    private int[] bestCap(int x, int y, boolean remain) {

        for(i = 0; i < 10; i++)
            for(j = 0; j < 5; j++)
                positions[i][j] = -1;

        if(!remain) {

            for(i = 0, l = 0; i < 8; i++) {
                for(j = 0; j < 8; j++) {

                    whereCapture = ifCapture(i, j, 0);

                    for(k = 0; k < 4; k++) {

                        if(whereCapture[k] == 1) {

                            // Men pieces

                            if(board[i][j].type == 1 && !player) {

                                if(k == 0) {
                                    positions[l][0] = i;
                                    positions[l][1] = j;
                                    positions[l][2] = i+2;
                                    positions[l][3] = j-2;
                                    positions[l][4] = 0;
                                    l++;
                                }

                                if(k == 1) {
                                    positions[l][0] = i;
                                    positions[l][1] = j;
                                    positions[l][2] = i+2;
                                    positions[l][3] = j+2;
                                    positions[l][4] = 1;
                                    l++;
                                }

                            }

                            if(board[i][j].type == 3 && player) {

                                if(k == 2) {
                                    positions[l][0] = i;
                                    positions[l][1] = j;
                                    positions[l][2] = i-2;
                                    positions[l][3] = j-2;
                                    positions[l][4] = 2;
                                    l++;
                                }

                                if(k == 3) {
                                    positions[l][0] = i;
                                    positions[l][1] = j;
                                    positions[l][2] = i-2;
                                    positions[l][3] = j+2;
                                    positions[l][4] = 3;
                                    l++;
                                }

                            }

                            // King pieces

                            if(board[i][j].type == 2 && !player) {

                                if(k == 0) {
                                    positions[l][0] = i;
                                    positions[l][1] = j;
                                    positions[l][2] = i+2;
                                    positions[l][3] = j-2;
                                    positions[l][4] = 0;
                                    l++;
                                }

                                if(k == 1) {
                                    positions[l][0] = i;
                                    positions[l][1] = j;
                                    positions[l][2] = i+2;
                                    positions[l][3] = j+2;
                                    positions[l][4] = 1;
                                    l++;
                                }

                                if(k == 2) {
                                    positions[l][0] = i;
                                    positions[l][1] = j;
                                    positions[l][2] = i-2;
                                    positions[l][3] = j-2;
                                    positions[l][4] = 2;
                                    l++;
                                }

                                if(k == 3) {
                                    positions[l][0] = i;
                                    positions[l][1] = j;
                                    positions[l][2] = i-2;
                                    positions[l][3] = j+2;
                                    positions[l][4] = 3;
                                    l++;
                                }

                            }

                            if(board[i][j].type == 4 && player) {
                                if(k == 0) {
                                    positions[l][0] = i;
                                    positions[l][1] = j;
                                    positions[l][2] = i+2;
                                    positions[l][3] = j-2;
                                    positions[l][4] = 0;
                                    l++;
                                }

                                if(k == 1) {
                                    positions[l][0] = i;
                                    positions[l][1] = j;
                                    positions[l][2] = i+2;
                                    positions[l][3] = j+2;
                                    positions[l][4] = 1;
                                    l++;
                                }

                                if(k == 2) {
                                    positions[l][0] = i;
                                    positions[l][1] = j;
                                    positions[l][2] = i-2;
                                    positions[l][3] = j-2;
                                    positions[l][4] = 2;
                                    l++;
                                }

                                if(k == 3) {
                                    positions[l][0] = i;
                                    positions[l][1] = j;
                                    positions[l][2] = i-2;
                                    positions[l][3] = j+2;
                                    positions[l][4] = 3;
                                    l++;
                                }
                            }

                        }

                    }
                }
            }
        } else {

            whereCapture = ifCapture(x, y, 0);

            for(k = 0; k < 4; k++) {

                if(whereCapture[k] == 1) {

                    // Men pieces

                    if(board[x][y].type == 1 && !player) {

                        if(k == 0) {
                            positions[l][0] = x;
                            positions[l][1] = y;
                            positions[l][2] = x+2;
                            positions[l][3] = y-2;
                            positions[l][4] = 0;
                            l++;
                        }

                        if(k == 1) {
                            positions[l][0] = x;
                            positions[l][1] = y;
                            positions[l][2] = x+2;
                            positions[l][3] = y+2;
                            positions[l][4] = 1;
                            l++;
                        }

                    }

                    if(board[x][y].type == 3 && player) {

                        if(k == 2) {
                            positions[l][0] = x;
                            positions[l][1] = y;
                            positions[l][2] = x-2;
                            positions[l][3] = y-2;
                            positions[l][4] = 2;
                            l++;
                        }

                        if(k == 3) {
                            positions[l][0] = x;
                            positions[l][1] = y;
                            positions[l][2] = x-2;
                            positions[l][3] = y+2;
                            positions[l][4] = 3;
                            l++;
                        }

                    }

                    // King pieces

                    if(board[x][y].type == 2 && !player) {

                        if(k == 0) {
                            positions[l][0] = x;
                            positions[l][1] = y;
                            positions[l][2] = x+2;
                            positions[l][3] = y-2;
                            positions[l][4] = 0;
                            l++;
                        }

                        if(k == 1) {
                            positions[l][0] = x;
                            positions[l][1] = y;
                            positions[l][2] = x+2;
                            positions[l][3] = y+2;
                            positions[l][4] = 1;
                            l++;
                        }

                        if(k == 2) {
                            positions[l][0] = x;
                            positions[l][1] = y;
                            positions[l][2] = x-2;
                            positions[l][3] = y-2;
                            positions[l][4] = 2;
                            l++;
                        }

                        if(k == 3) {
                            positions[l][0] = x;
                            positions[l][1] = y;
                            positions[l][2] = x-2;
                            positions[l][3] = y+2;
                            positions[l][4] = 3;
                            l++;
                        }

                    }

                    if(board[x][y].type == 4 && player) {
                        if(k == 0) {
                            positions[l][0] = x;
                            positions[l][1] = y;
                            positions[l][2] = x+2;
                            positions[l][3] = y-2;
                            positions[l][4] = 0;
                            l++;
                        }

                        if(k == 1) {
                            positions[l][0] = x;
                            positions[l][1] = y;
                            positions[l][2] = x+2;
                            positions[l][3] = y+2;
                            positions[l][4] = 1;
                            l++;
                        }

                        if(k == 2) {
                            positions[l][0] = x;
                            positions[l][1] = y;
                            positions[l][2] = x-2;
                            positions[l][3] = y-2;
                            positions[l][4] = 2;
                            l++;
                        }

                        if(k == 3) {
                            positions[l][0] = x;
                            positions[l][1] = y;
                            positions[l][2] = x-2;
                            positions[l][3] = y+2;
                            positions[l][4] = 3;
                            l++;
                        }
                    }

                }

            }
        }

        if(l == 0) {
            positions[0][4] = -1;
            return positions[0];
        }

        return positions[random.nextInt(l)];
    }

    private void playAs(Graphics2D g) {

        g.setFont(new Font("TimesRoman", Font.PLAIN, 40));

        g.setColor(new Color(150, 150, 150)); // gray
        g.fillRect(0, 0, 400, 400);

        g.setColor(Color.red);
        g.fillRect(50, 250, 120, 70);
        g.fillRect(250, 250, 120, 70);

        g.setColor(Color.green);
        g.drawString("PLAY AS", 120, 170);
        g.drawString("White", 50, 295);
        g.drawString("Black", 250, 295);

    }

    private void Instructions() {

        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();

        g.setColor(new Color(100, 100, 100));
        g.fillRect(0, 0, 400, 400);

        g.setColor(new Color(255, 102, 0));
        g.setFont(new Font("TimesRoman", Font.PLAIN, 30));

        g.drawString("ESC -> ", 50, 50);
        g.drawString("Exit", 50, 90);

        g.drawString("ENTER -> ", 50, 180);
        g.drawString("Restart or replay", 50, 220);        

        g.drawString("SPACE BAR -> ", 50, 310);
        g.drawString("Back to menu", 50, 350);

        g.dispose();

        bufferStrategy.show();

    }

    public void load() {

        begin(true);

        victory = "None";

        // Take off the decoration (which means, it has no edges).
        mainWindow.setUndecorated(true);

        // Set to ignore the draw's event of the system, because
        // we'll use active rendering, that is, drawing (on our own?).
        mainWindow.setIgnoreRepaint(true);
        // Position the window to x, y pixels from the edge.
        // mainWindow.setLocation(500, 250);
        mainWindow.setLocation(500, 200);
        // Show the window.
        mainWindow.setVisible(true);
        // Create a buffer strategy with 2 buffers (double buffer).
        mainWindow.createBufferStrategy(2);
        // Store a buffer strategy on our variable to be used after.
        bufferStrategy = mainWindow.getBufferStrategy();

    }

    private boolean whiteWins() {
        for(i = 0; i < 8; i++)
            for(j = 0; j < 8; j++) if(board[i][j].type == 3 || board[i][j].type == 4) return false;

        return true;
    }

    private boolean blackWins() {
        for(i = 0; i < 8; i++)
            for(j = 0; j < 8; j++) if(board[i][j].type == 1 || board[i][j].type == 2) return false;

        return true;
    }

    public void unload() {

        // Release the buffer strategy.
        bufferStrategy.dispose();
        // Release the window.
        mainWindow.dispose();

    }

    private void lookForCaptures(){

        int cap[] = new int[4];
        int x, y;

        cap[0] = 0;//left up
        cap[1] = 0;//right up
        cap[2] = 0;//left down
        cap[3] = 0;//right down

        for(x = 0; x < 8; x++)
            for(y = 0; y < 8; y++)
                helpBoard[x][y] = 0;


        for(x = 0; x < 8; x++) {
            for(y = 0; y < 8; y++) {

                if(board[x][y].type == 1 || board[x][y].type == 2){

                    if(x < 6 && x > 1){

                        if(y == 0 || y == 1){

                            if (board[x+1][y+1].type == 3 || board[x+1][y+1].type == 4){

                                if(board[x+2][y+2].type == 0){

                                    cap[1] = 1;

                                }

                            }

                        }else if(y == 6 || y == 7){

                            if (board[x+1][y-1].type == 3 || board[x+1][y-1].type == 4){

                                if(board[x+2][y-2].type == 0){

                                    cap[0] = 1;

                                }


                            }

                        }else if(y > 1 && y < 6){

                            if (board[x+1][y+1].type == 3 || board[x+1][y+1].type == 4){

                                if(board[x+2][y+2].type == 0){

                                    cap[1] = 1;

                                }

                            }
                            if (board[x+1][y-1].type == 3 || board[x+1][y-1].type == 4){

                                if(board[x+2][y-2].type == 0){

                                    cap[0] = 1;
                                    
                                }


                            }

                        }

                        if(board[x][y].type == 2){

                            if(y == 0 || y == 1){

                                if (board[x-1][y+1].type == 3 || board[x-1][y+1].type == 4){

                                    if(board[x-2][y+2].type == 0){

                                        cap[3] = 1;

                                    }

                                }

                            }else if(y == 6 || y == 7){

                                if (board[x-1][y-1].type == 3 || board[x-1][y-1].type == 4){

                                    if(board[x-2][y-2].type == 0){

                                        cap[2] = 1;

                                    }


                                }

                            }else if(y > 1 && y < 6){

                                if (board[x-1][y+1].type == 3 || board[x-1][y+1].type == 4){

                                    if(board[x-2][y+2].type == 0){

                                        cap[3] = 1;

                                    }

                                }
                                if (board[x-1][y-1].type == 3 || board[x-1][y-1].type == 4){

                                    if(board[x-2][y-2].type == 0){

                                        cap[2] = 1;
                                        
                                    }


                                }

                            }

                        }

                    }else if(x == 0 || x == 1){

                        if(y == 0 || y == 1){

                            if (board[x+1][y+1].type == 3 || board[x+1][y+1].type == 4){

                                if(board[x+2][y+2].type == 0){

                                    cap[1] = 1;

                                }

                            }

                        }else if(y == 6 || y == 7){

                            if (board[x+1][y-1].type == 3 || board[x+1][y-1].type == 4){

                                if(board[x+2][y-2].type == 0){

                                    cap[0] = 1;

                                }


                            }

                        }else if(y > 1 && y < 6){

                            if (board[x+1][y+1].type == 3 || board[x+1][y+1].type == 4){

                                if(board[x+2][y+2].type == 0){

                                    cap[1] = 1;


                                }

                            }
                            if (board[x+1][y-1].type == 3 || board[x+1][y-1].type == 4){

                                if(board[x+2][y-2].type == 0){

                                    cap[0] = 1;
                                    
                                }

                            }

                        }

                    }else if((x == 6 || x == 7) && (board[x][y].type == 2)){

                        if(y == 0 || y == 1){

                            if (board[x-1][y+1].type == 3 || board[x-1][y+1].type == 4){

                                if(board[x-2][y+2].type == 0){

                                    cap[3] = 1;

                                }

                            }

                        }else if(y == 6 || y == 7){

                            if (board[x-1][y-1].type == 3 || board[x-1][y-1].type == 4){

                                if(board[x-2][y-2].type == 0){

                                    cap[2] = 1;

                                }


                            }

                        }else if(y > 1 && y < 6){

                            if (board[x-1][y+1].type == 3 || board[x-1][y+1].type == 4){

                                if(board[x-2][y+2].type == 0){

                                    cap[3] = 1;

                                }

                            }
                            if (board[x-1][y-1].type == 3 || board[x-1][y-1].type == 4){

                                if(board[x-2][y-2].type == 0){

                                    cap[2] = 1;
                                    
                                }

                            }

                        }

                    }

                }else if(board[x][y].type == 3 || board[x][y].type == 4){

                    if(x < 6 && x > 1){

                        if(y == 0 || y == 1){

                            if (board[x-1][y+1].type == 1 || board[x-1][y+1].type == 2){

                                if(board[x-2][y+2].type == 0){

                                    cap[3] = 1;

                                }

                            }

                        }else if(y == 6 || y == 7){

                            if (board[x-1][y-1].type == 1 || board[x-1][y-1].type == 2){

                                if(board[x-2][y-2].type == 0){

                                    cap[2] = 1;

                                }


                            }

                        }else if(y > 1 && y < 6){

                            if (board[x-1][y+1].type == 1 || board[x-1][y+1].type == 2){

                                if(board[x-2][y+2].type == 0){

                                    cap[3] = 1;


                                }

                            }
                            if (board[x-1][y-1].type == 1 || board[x-1][y-1].type == 2){

                                if(board[x-2][y-2].type == 0){

                                    cap[2] = 1;
                                    
                                }


                            }

                        }

                        if(board[x][y].type == 4){

                            if(y == 0 || y == 1){

                                if (board[x+1][y+1].type == 1 || board[x+1][y+1].type == 2){

                                    if(board[x+2][y+2].type == 0){

                                        cap[1] = 1;

                                    }

                                }

                            }else if(y == 6 || y == 7){

                                if (board[x+1][y-1].type == 1 || board[x+1][y-1].type == 2){

                                    if(board[x+2][y-2].type == 0){

                                        cap[0] = 1;

                                    }


                                }

                            }else if(y > 1 && y < 6){

                                if (board[x-1][y+1].type == 1 || board[x-1][y+1].type == 2){

                                    if(board[x+2][y+2].type == 0){

                                        cap[1] = 1;


                                    }

                                }
                                if (board[x-1][y-1].type == 1 || board[x-1][y-1].type == 2){

                                    if(board[x+2][y-2].type == 0){

                                        cap[0] = 1;
                                        
                                    }


                                }

                            }

                        }

                    }else if((x == 0 || x == 1) && (board[x][y].type == 4)){

                        if(y == 0 || y == 1){

                            if (board[x+1][y+1].type == 1 || board[x+1][y+1].type == 2){

                                if(board[x+2][y+2].type == 0){

                                    cap[1] = 1;

                                }

                            }

                        }else if(y == 6 || y == 7){

                            if (board[x+1][y-1].type == 1 || board[x+1][y-1].type == 2){

                                if(board[x+2][y-2].type == 0){

                                    cap[0] = 1;

                                }


                            }

                        }else if(y > 1 && y < 6){

                            if (board[x+1][y+1].type == 1 || board[x+1][y+1].type == 2){

                                if(board[x+2][y+2].type == 0){

                                    cap[1] = 1;


                                }

                            }
                            if (board[x+1][y-1].type == 1 || board[x+1][y-1].type == 2){

                                if(board[x+2][y-2].type == 0){

                                    cap[0] = 1;
                                    
                                }

                            }

                        }

                    }else if(x == 6 || x == 7){

                        if(y == 0 || y == 1){

                            if (board[x-1][y+1].type == 1 || board[x-1][y+1].type == 2){

                                if(board[x-2][y+2].type == 0){

                                    cap[3] = 1;

                                }

                            }

                        }else if(y == 6 || y == 7){

                            if (board[x-1][y-1].type == 1 || board[x-1][y-1].type == 2){

                                if(board[x-2][y-2].type == 0){

                                    cap[2] = 1;

                                }


                            }

                        }else if(y > 1 && y < 6){

                            if (board[x-1][y+1].type == 1 || board[x-1][y+1].type == 2){

                                if(board[x-2][y+2].type == 0){

                                    cap[3] = 1;


                                }

                            }
                            if (board[x-1][y-1].type == 1 || board[x-1][y-1].type == 2){

                                if(board[x-2][y-2].type == 0){

                                    cap[2] = 1;
                                    
                                }

                            }

                        }

                    }
                }

                for(i = 0; i < 4; i++) {
                    if(cap[i] == 1) {
                        if(i == 0) helpBoard[x+2][y-2] = i+1;
                        else if(i == 1) helpBoard[x+2][y+2] = i+1;
                        else if(i == 2) helpBoard[x-2][y-2] = i+1;
                        else if(i == 3) helpBoard[x-2][y+2] = i+1;
                    }
                    cap[i] = 0;
                }
            }
        }
    }

    private int[] saveXandYpositions() {

        int[] anotherCap = new int[4];

        for(i = 0; i < 4; i++)
            anotherCap[i] = 0;

        if(helpBoard[afterMouseY][afterMouseX] == 1){
            previousMouseX = afterMouseX + 2;
            previousMouseY = afterMouseY - 2;
            anotherCap[0] = 1;
        } else if(helpBoard[afterMouseY][afterMouseX] == 2) {
            previousMouseX = afterMouseX - 2;
            previousMouseY = afterMouseY - 2;
            anotherCap[1] = 1;
        } else if(helpBoard[afterMouseY][afterMouseX] == 3) {
            previousMouseX = afterMouseX + 2;
            previousMouseY = afterMouseY + 2;
            anotherCap[2] = 1;
        } else if(helpBoard[afterMouseY][afterMouseX] == 4) {
            previousMouseX = afterMouseX - 2;
            previousMouseY = afterMouseY + 2;
            anotherCap[3] = 1;
        }

        return anotherCap;

    }

    private boolean analysisCapture(boolean special, int x, int y) {

        if(board[7][1].type != 0) {
            aleatory = true;
            l = board[7][1].type;
        }

        if(!special) {
            for(i = 0, needCapture = false; i < 8; i++) {
                for(j = 0; j < 8; j++) {

                    whereCapture = ifCapture(i, j, 0);

                    for(k = 0; k < 4; k++) {

                        if(whereCapture[k] == 1) {

                            // Men pieces

                            if(board[i][j].type == 1 && time) {

                                if(k == 0) {
                                    board[i+2][j-2].color = "Green";
                                    needCapture = true;
                                }

                                if(k == 1) {
                                    board[i+2][j+2].color = "Green";
                                    needCapture = true;
                                }

                            }

                            if(board[i][j].type == 3 && !time) {

                                if(k == 2) {
                                    board[i-2][j-2].color = "Green";
                                    needCapture = true;
                                }

                                if(k == 3) {
                                    board[i-2][j+2].color = "Green";
                                    needCapture = true;
                                }

                            }

                            // King pieces

                            if(board[i][j].type == 2 && time) {

                                if(k == 0) {
                                    board[i+2][j-2].color = "Green";
                                    needCapture = true;
                                }

                                if(k == 1) {
                                    board[i+2][j+2].color = "Green";
                                    needCapture = true;
                                }

                                if(k == 2) {
                                    board[i-2][j-2].color = "Green";
                                    needCapture = true;
                                }

                                if(k == 3) {
                                    board[i-2][j+2].color = "Green";
                                    needCapture = true;
                                }

                            }

                            if(board[i][j].type == 4 && !time) {
                                if(k == 0) {
                                    board[i+2][j-2].color = "Green";
                                    needCapture = true;
                                }

                                if(k == 1) {
                                    board[i+2][j+2].color = "Green";
                                    needCapture = true;
                                }

                                if(k == 2) {
                                    board[i-2][j-2].color = "Green";
                                    needCapture = true;
                                }

                                if(k == 3) {
                                    board[i-2][j+2].color = "Green";
                                    needCapture = true;
                                }

                            }

                        }

                    }
                }
            }
        } else {

            whereCapture = ifCapture(y, x, 0);

            for(k = 0; k < 4; k++) {

                if(whereCapture[k] == 1) {

                    // Men pieces

                    if(board[y][x].type == 1 && time) {

                        if(k == 0) {
                            board[y+2][x-2].color = "Green";
                            needCapture = true;
                        }

                        if(k == 1) {
                            board[y+2][x+2].color = "Green";
                            needCapture = true;
                        }

                    }

                    if(board[y][x].type == 3 && !time) {

                        if(k == 2) {
                            board[y-2][x-2].color = "Green";
                            needCapture = true;
                        }

                        if(k == 3) {
                            board[y-2][x+2].color = "Green";
                            needCapture = true;
                        }

                    }

                    // King pieces

                    if(board[y][x].type == 2 && time) {

                        if(k == 0) {
                            board[y+2][x-2].color = "Green";
                            needCapture = true;
                        }

                        if(k == 1) {
                            board[y+2][x+2].color = "Green";
                            needCapture = true;
                        }

                        if(k == 2) {
                            board[y-2][x-2].color = "Green";
                            needCapture = true;
                        }

                        if(k == 3) {
                            board[y-2][x+2].color = "Green";
                            needCapture = true;
                        }

                    }

                    if(board[y][x].type == 4 && !time) {
                        if(k == 0) {
                            board[y+2][x-2].color = "Green";
                            needCapture = true;
                        }

                        if(k == 1) {
                            board[y+2][x+2].color = "Green";
                            needCapture = true;
                        }

                        if(k == 2) {
                            board[y-2][x-2].color = "Green";
                            needCapture = true;
                        }

                        if(k == 3) {
                            board[y-2][x+2].color = "Green";
                            needCapture = true;
                        }

                    }

                }

            }

        }

        // fix one of the easter egg
        if(aleatory) {
            board[7][1].type = l;
            aleatory = false;
        }

        return needCapture;

    }

    private boolean stillCapture(int x, int y){

        if(board[x][y].type == 1 || board[x][y].type == 2){

            if(x < 6 && x > 1){

                if(y == 0 || y == 1){

                    if (board[x+1][y+1].type == 3 || board[x+1][y+1].type == 4){

                        if(board[x+2][y+2].type == 0){

                            return true;

                        }

                    }

                }else if(y == 6 || y == 7){

                    if (board[x+1][y-1].type == 3 || board[x+1][y-1].type == 4){

                        if(board[x+2][y-2].type == 0){

                            return true;

                        }


                    }

                }else if(y > 1 && y < 6){

                    if (board[x+1][y+1].type == 3 || board[x+1][y+1].type == 4){

                        if(board[x+2][y+2].type == 0){

                            return true;

                        }

                    }
                    if (board[x+1][y-1].type == 3 || board[x+1][y-1].type == 4){

                        if(board[x+2][y-2].type == 0){

                            return true;
                            
                        }


                    }

                }

                if(board[x][y].type == 2){

                    if(y == 0 || y == 1){

                        if (board[x-1][y+1].type == 3 || board[x-1][y+1].type == 4){

                            if(board[x-2][y+2].type == 0){

                                return true;

                            }

                        }

                    }else if(y == 6 || y == 7){

                        if (board[x-1][y-1].type == 3 || board[x-1][y-1].type == 4){

                            if(board[x-2][y-2].type == 0){

                                return true;

                            }


                        }

                    }else if(y > 1 && y < 6){

                        if (board[x-1][y+1].type == 3 || board[x-1][y+1].type == 4){

                            if(board[x-2][y+2].type == 0){

                                return true;

                            }

                        }
                        if (board[x-1][y-1].type == 3 || board[x-1][y-1].type == 4){

                            if(board[x-2][y-2].type == 0){

                                return true;
                                
                            }


                        }

                    }

                }

            }else if(x == 0 || x == 1){

                if(y == 0 || y == 1){

                    if (board[x+1][y+1].type == 3 || board[x+1][y+1].type == 4){

                        if(board[x+2][y+2].type == 0){

                            return true;

                        }

                    }

                }else if(y == 6 || y == 7){

                    if (board[x+1][y-1].type == 3 || board[x+1][y-1].type == 4){

                        if(board[x+2][y-2].type == 0){

                            return true;

                        }


                    }

                }else if(y > 1 && y < 6){

                    if (board[x+1][y+1].type == 3 || board[x+1][y+1].type == 4){

                        if(board[x+2][y+2].type == 0){

                            return true;

                        }

                    }
                    if (board[x+1][y-1].type == 3 || board[x+1][y-1].type == 4){

                        if(board[x+2][y-2].type == 0){

                            return true;
                            
                        }

                    }

                }

            }else if((x == 6 || x == 7) && (board[x][y].type == 2)){

                if(y == 0 || y == 1){

                    if (board[x-1][y+1].type == 3 || board[x-1][y+1].type == 4){

                        if(board[x-2][y+2].type == 0){

                            return true;

                        }

                    }

                }else if(y == 6 || y == 7){

                    if (board[x-1][y-1].type == 3 || board[x-1][y-1].type == 4){

                        if(board[x-2][y-2].type == 0){

                            return true;

                        }


                    }

                }else if(y > 1 && y < 6){

                    if (board[x-1][y+1].type == 3 || board[x-1][y+1].type == 4){

                        if(board[x-2][y+2].type == 0){

                            return true;

                        }

                    }
                    if (board[x-1][y-1].type == 3 || board[x-1][y-1].type == 4){

                        if(board[x-2][y-2].type == 0){

                            return true;
                            
                        }

                    }

                }

            }

        }else if(board[x][y].type == 3 || board[x][y].type == 4){

            if(x < 6 && x > 1){

                if(y == 0 || y == 1){

                    if (board[x-1][y+1].type == 1 || board[x-1][y+1].type == 2){

                        if(board[x-2][y+2].type == 0){

                            return true;

                        }

                    }

                }else if(y == 6 || y == 7){

                    if (board[x-1][y-1].type == 1 || board[x-1][y-1].type == 2){

                        if(board[x-2][y-2].type == 0){

                            return true;

                        }


                    }

                }else if(y > 1 && y < 6){

                    if (board[x-1][y+1].type == 1 || board[x-1][y+1].type == 2){

                        if(board[x-2][y+2].type == 0){

                            return true;

                        }

                    }
                    if (board[x-1][y-1].type == 1 || board[x-1][y-1].type == 2){

                        if(board[x-2][y-2].type == 0){

                            return true;
                            
                        }


                    }

                }

                if(board[x][y].type == 4){

                    if(y == 0 || y == 1){

                        if (board[x+1][y+1].type == 1 || board[x+1][y+1].type == 2){

                            if(board[x+2][y+2].type == 0){

                                return true;

                            }

                        }

                    }else if(y == 6 || y == 7){

                        if (board[x+1][y-1].type == 1 || board[x+1][y-1].type == 2){

                            if(board[x+2][y-2].type == 0){

                                return true;

                            }


                        }

                    }else if(y > 1 && y < 6){

                        if (board[x+1][y+1].type == 1 || board[x+1][y+1].type == 2){

                            if(board[x+2][y+2].type == 0){

                                return true;

                            }

                        }

                        if (board[x-1][y+1].type == 1 || board[x-1][y+1].type == 2){

                            if(board[x-2][y+2].type == 0){

                                return true;

                            }

                        }

                        if (board[x-1][y-1].type == 1 || board[x-1][y-1].type == 2){

                            if(board[x-2][y-2].type == 0){

                                return true;
                                
                            }


                        }

                        if (board[x+1][y-1].type == 1 || board[x+1][y-1].type == 2){

                            if(board[x+2][y-2].type == 0){

                                return true;
                                
                            }


                        }


                    }

                }

            }else if((x == 0 || x == 1) && (board[x][y].type == 4)){

                if(y == 0 || y == 1){

                    if (board[x+1][y+1].type == 1 || board[x+1][y+1].type == 2){

                        if(board[x+2][y+2].type == 0){

                            return true;

                        }

                    }

                }else if(y == 6 || y == 7){

                    if (board[x+1][y-1].type == 1 || board[x+1][y-1].type == 2){

                        if(board[x+2][y-2].type == 0){

                            return true;

                        }


                    }

                }else if(y > 1 && y < 6){

                    if (board[x+1][y+1].type == 1 || board[x+1][y+1].type == 2){

                        if(board[x+2][y+2].type == 0){

                            return true;

                        }

                    }
                    if (board[x+1][y-1].type == 1 || board[x+1][y-1].type == 2){

                        if(board[x+2][y-2].type == 0){

                            return true;
                            
                        }

                    }

                }

            }else if(x == 6 || x == 7){

                if(y == 0 || y == 1){

                    if (board[x-1][y+1].type == 1 || board[x-1][y+1].type == 2){

                        if(board[x-2][y+2].type == 0){

                            return true;

                        }

                    }

                }else if(y == 6 || y == 7){

                    if (board[x-1][y-1].type == 1 || board[x-1][y-1].type == 2){

                        if(board[x-2][y-2].type == 0){

                            return true;

                        }


                    }

                }else if(y > 1 && y < 6){

                    if (board[x-1][y+1].type == 1 || board[x-1][y+1].type == 2){

                        if(board[x-2][y+2].type == 0){

                            return true;

                        }

                    }
                    if (board[x-1][y-1].type == 1 || board[x-1][y-1].type == 2){

                        if(board[x-2][y-2].type == 0){

                            return true;
                            
                        }

                    }

                }

            }

        }

        return false;

    }

    private boolean remainCapture(boolean player, int i, int j) {

        if(((board[i][j].type == 1 || board[i][j].type == 2) && !player) ||
            ((board[i][j].type == 3 || board[i][j].type == 4) && player)) {
            whereCapture = ifCapture(i, j, 0);

            for(k = 0; k < 4; k++)
                if(whereCapture[k] == 1) return true;

        }

        return false;
    }

    public void update() {

        if (keyPool.get(KeyEvent.VK_ESCAPE) != null) terminate();
        else if (keyPool.get(KeyEvent.VK_SPACE) != null) {
            begin = false;
            choose = false;
            instruction = false;
        } else if (keyPool.get(KeyEvent.VK_ENTER) != null) {
            time = true;
            begin(false);
            victory = "None";
        } else {

            if(choose && !begin) {
                if (keyPool.get(MouseEvent.BUTTON1) != null) {
                    if(mouseX >= 50 && mouseX <= (50+120) && mouseY >= 250 && mouseY <= (250+70)) {
                        begin = true;
                        if(!player || twoPlayer){
                            begin(true);
                            time = true;
                            victory = "None";
                            twoPlayer = false;
                        }
                        player = true;
                    } else if(mouseX >= 250 && mouseX <= (250+120) && mouseY >= 250 && mouseY <= (250+70)) {
                        begin = true;
                        if(player || twoPlayer) {
                            begin(true);
                            time = true;
                            victory = "None";
                            twoPlayer = false;
                        }
                        player = false;
                    }
                }
            } else {

                if(whiteWins()) victory = "White";
                else if(blackWins()) victory = "Black";
                else {
                    if(instruction) {
                        if (keyPool.get(KeyEvent.VK_SPACE) != null) instruction = false;
                    } else {
                        if((!twoPlayer && needCapture && ((player && time) || (!player && !time))) ||
                            (twoPlayer && needCapture)) {

                            while(!stopSelection) System.out.println();

                            while(keyPool.get(MouseEvent.BUTTON1) == null) System.out.println();

                            previousMouseY = (400 - mouseY) / 50;
                            previousMouseX = (mouseX) / 50;

                            if(previousMouseX >= 0 && previousMouseX <= 7 && previousMouseY >= 0 && previousMouseY <= 7) {

                                if(!board[previousMouseY][previousMouseX].color.equals("Green")) {

                                    while(!stopSelection) System.out.println();

                                    while(keyPool.get(MouseEvent.BUTTON1) == null) System.out.println();

                                    afterMouseY = (400 - mouseY) / 50;
                                    afterMouseX = (mouseX) / 50;

                                    if(afterMouseX >= 0 && afterMouseX <= 7 && afterMouseY >= 0 && afterMouseY <= 7) {

                                        if(board[afterMouseY][afterMouseX].color.equals("Green")) {

                                            needCapture = false;

                                            if(!analyse) {

                                                lookForCaptures();

                                                whereCapture = saveXandYpositions();

                                                analyse = true;
                                            }

                                            capture = capture(previousMouseY, previousMouseX, afterMouseY, afterMouseX);

                                            if(couldCapture) {

                                                for(i = 0; i < 8; i++)
                                                    for(j = 0; j < 8; j++)
                                                        if(board[i][j].color.equals("Green")) board[i][j].color = "Black";

                                                if(stillCapture(afterMouseY, afterMouseX) && !capture) {
                                                    analyse = analysisCapture(true, afterMouseX, afterMouseY);
                                                    needCapture = true;
                                                }

                                                if(!needCapture) {
                                                    if(time) time = false;
                                                    else     time = true;
                                                    mustCapture = false;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else if(mustCapture && !twoPlayer) {

                            if(remain) capCOM = bestCap(x, y, true);
                            else capCOM = bestCap(0, 0, false);

                            if(capCOM[2] != -1 && capCOM[3] != -1) {
                                x = capCOM[2];
                                y = capCOM[3];
                            }

                            mustCapture = false;
                            needCapture = false;

                            if(capCOM[0] != -1) {
                                try {
                                    Thread.sleep(500);
                                } catch(Exception e) {
                                    System.out.println("ERROR!");
                                }

                                Movehelp(capCOM[0], capCOM[1], capCOM[2], capCOM[3], capCOM[4]);

                                if(remainCapture(player, x, y)) {
                                    remain = true;
                                } else {
                                    remain = false;
                                    if(time) time = false;
                                    else     time = true;
                                }

                                for(i = 0; i < 8; i++)
                                    for(j = 0; j < 8; j++)
                                        if(board[i][j].color.equals("Green")) board[i][j].color = "Black";
                            }

                        } else {

                            if(!twoPlayer && !time && begin && player) {

                                mustCapture = analysisCapture(false, 0, 0);
                                if(!mustCapture) {

                                    try {
                                        Thread.sleep(500);
                                    } catch(Exception e) {
                                        System.out.println("ERROR!");
                                    }

                                    moveCOMblack();

                                    if(time) time = false;
                                    else     time = true;
                                }

                            } else if(!twoPlayer && time && !player && begin) {

                                mustCapture = analysisCapture(false, 0, 0);
                                if(!mustCapture) {

                                    moveCOMwhite();

                                    try {
                                        Thread.sleep(500);
                                    } catch(Exception e) {
                                        System.out.println("ERROR!");
                                    }

                                    if(time) time = false;
                                    else     time = true;
                                }

                            } else {

                                if(remain) {
                                    analysisCapture(true, x, y);
                                    mustCapture = true;
                                } else {

                                    if(!analysisDraw(time)) {

                                        mustCapture = false;

                                        // MOVE THE PIECES
                                        if(pieceSelected && keyPool.get(MouseEvent.BUTTON1) != null) {

                                            afterMouseY = (400 - mouseY) / 50;
                                            afterMouseX = (mouseX) / 50;

                                            if(afterMouseX >= 0 && afterMouseX <= 7 && afterMouseY >= 0 && afterMouseY <= 7 &&
                                                previousMouseX >= 0 && previousMouseX <= 7 && previousMouseY >= 0 && previousMouseY <= 7) {

                                                // empty square
                                                if(board[afterMouseY][afterMouseX].type == 0 && board[afterMouseY][afterMouseX].color.equals("Black")) {

                                                    // choose who will play
                                                    if((time && (board[previousMouseY][previousMouseX].type == 1 || board[previousMouseY][previousMouseX].type == 2))) {

                                                        // White

                                                        if((board[previousMouseY][previousMouseX].type == 2 && previousMouseY - 1 == afterMouseY) || previousMouseY + 1 == afterMouseY) {
                                                            move(previousMouseY, previousMouseX, afterMouseY, afterMouseX);

                                                            if(time) time = false;
                                                            else     time = true;

                                                            pieceSelected = false;
                                                        }

                                                    } else if((!time && (board[previousMouseY][previousMouseX].type == 3 || board[previousMouseY][previousMouseX].type == 4))) {

                                                        if((board[previousMouseY][previousMouseX].type == 4 && previousMouseY + 1 == afterMouseY) || previousMouseY - 1 == afterMouseY) {
                                                            move(previousMouseY, previousMouseX, afterMouseY, afterMouseX);

                                                            if(time) time = false;
                                                            else     time = true;

                                                            pieceSelected = false;
                                                        }
                                                    }

                                                }
                                            }
                                        }

                                        // GET POSITION OF THE MOUSE
                                        if (keyPool.get(MouseEvent.BUTTON1) != null && !stopSelection) {
                                            previousMouseY = (400 - mouseY) / 50;
                                            previousMouseX = (mouseX) / 50;

                                            if(previousMouseX >= 0 && previousMouseX <= 7 && previousMouseY >= 0 && previousMouseY <= 7) {
                                                if(board[previousMouseY][previousMouseX].type != 0 && board[previousMouseY][previousMouseX].color.equals("Black") && !stopSelection && !pieceSelected) {
                                                    pieceSelected = true;
                                                }
                                            }
                                        }

                                        analyse = analysisCapture(false, 0, 0);

                                    } else victory = "Draw";
                                }
                            }
                        }
                    }
                }
            }
        }

        // Fix another easter egg
        if(!board[7][1].color.equals("Green")) board[7][1].color = "Black";

        Thread.yield();
    }

    private void drawVictory(Graphics2D g) {

        g.setFont(new Font("TimesRoman", Font.PLAIN, 40));

        if(victory.equals("White")) {
            g.setColor(Color.red);
            g.fillRect(50, 150, 300, 100);
            g.setColor(Color.green);
            g.drawString("WHITE wins", 75, 210);
        }
        else if(victory.equals("Black")) {
            g.setColor(Color.red);
            g.fillRect(50, 150, 300, 100);
            g.setColor(Color.green);
            g.drawString("BLACK wins", 75, 210);
        }
        else if(victory.equals("Draw")) {
            g.setColor(Color.red);
            g.fillRect(50, 150, 300, 100);
            g.setColor(Color.green);
            g.drawString("DRAW", 120, 210);
        }
    }

    private void drawPiece(Graphics2D g, int type, String color, int posX, int posY) {

        // id = 1 => White
        // id = 3 => Black

        if(color.equals("Green")) {
            g.setColor(Color.green);
            g.fillRect(posX, posY, 50, 50);
            return;
        }

        if(color.equals("White") || type == 0) return;

        g.setColor(new Color(0, 0, 0));
        g.fillOval(posX + 3, posY + 3, 44, 44);

        if(type == 3 || type == 4)       g.setColor(new Color(102, 102, 102));
        else if(type == 1 || type == 2)  g.setColor(new Color(200, 200, 200));

        g.fillOval(posX + 5, posY + 5, 40, 40);

        g.setColor(new Color(0, 0, 0));
        g.fillOval(posX + 13, posY + 13, 24, 24);

        if(type == 3)       g.setColor(new Color(153, 153, 153));
        else if(type == 1)  g.setColor(new Color(255, 255, 255));
        else if(type == 2 || type == 4) g.setColor(new Color(255, 51, 51));

        g.fillOval(posX + 15, posY + 15, 20, 20);

    }

    private void drawBackGround(Graphics2D g) {

        g.setColor(new Color(204, 204, 204)); // gray
        g.fillRect(0, 0, 400, 400);

        g.setColor(new Color(102, 52, 0));

        for(i = 0; i < 8; i++) {
            for(j = 0; j < 8; j++) {
                if(board[i][j].color.contains("Edge")) {
                    g.setColor(new Color(0, 102, 0));
                    g.fillRect((((i%2)*50) + (j*100))-50, 50*(i), 50, 50);
                    g.setColor(new Color(102, 52, 0));
                    g.fillRect(((((i%2)*50) + (j*100))-50) + 5, (50*(i)) + 5, 40, 40);
                    continue;
                }
                g.fillRect((((i%2)*50) + (j*100))-50, 50*(i), 50, 50);
            }

        }

        g.setColor(new Color(238, 238, 238)); // white
        g.fillRect(400, 0, 400, 400);

    }

    private void drawPieces(Graphics2D g) {

        for(i = 0; i < 8; i++)
            for(j = 0; j < 8; j++)
                drawPiece(g, board[i][j].type, board[i][j].color, j*50, 50*(7-i));

    }

    public void render() {

        // Every call to render, we get a graphics to draw.
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();

        if(instruction) Instructions();
        else if(!begin && !choose) welcome();
        else if(choose && !begin) playAs(g);
        else {

            // [BOARD]

                drawBackGround(g);

            // [PIECES]

                drawPieces(g);

            // [VICTORY]
                drawVictory(g);

        }

        // Release the object graphics.
        g.dispose();

        // Ask for the buffer strategy show what was drawn above.
        bufferStrategy.show();
    }

    public void terminate() {
    	active = false;
    }

    public void loop() {
    	load();

    	active = true;

    	while(active) {
            update();
    		render();
    		try {
    			Thread.sleep(50);
    		} catch(Exception e) {
    			System.out.println("ERROR!");
    		}
    	}

    	unload();
    }

    public void windowClosing(WindowEvent e) {
        terminate();
    }

    public void windowOpened(WindowEvent e) {
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void keyTyped(KeyEvent e) {
        // Function not used. Key's event typed.
    }

    public void keyPressed(KeyEvent e) {
        // When a key is pressed, adds it to pool
        keyPool.put(e.getKeyCode(), true);
    }

    public void keyReleased(KeyEvent e) {
        // When a key is released, removes it from pool
        keyPool.remove(e.getKeyCode());
    }


    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        // Chamado quando um botão do mouse é pressionado.
        keyPool.put(e.getButton(), true);
        stopSelection = false;
    }

    public void mouseReleased(MouseEvent e) {
        // Chamado quando um botão do mouse é solto.
        keyPool.remove(e.getButton());
        stopSelection = true;
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        // Chamado quando o mouse é movido com o botão pressionado.
        mouseX = e.getX();
        mouseY = e.getY();
    }

    public void mouseMoved(MouseEvent e) {
        // Chamado quando o mouse é movido.
        mouseX = e.getX();
        mouseY = e.getY();
    }

}