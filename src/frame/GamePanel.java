package frame;

import entry.Cell;
import main.GameLine;
import main.HomeownerLine;
import main.RoomLine;
import main.Stage;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.ImageObserver;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

public class GamePanel extends JPanel {

    private int w = 930, h = 1000;
    private Cell[][] stage;
    private JPanel stagepanel, infopanel, chatpanel;
    private JButton[][] stagecell;
    private JButton endgame;
    private JTextArea chatarea;
    private JTextField input;
    private StringBuffer messages;
    private HomeownerLine homeownerLine;
    private RoomLine roomLine;
    private GameLine gameLine;

    public GamePanel(GameLine gameLine){

        this.homeownerLine = homeownerLine;
        this.roomLine = null;
        this.gameLine = gameLine;

        stage = gameLine.getStage();

        setLayout(new BorderLayout());
        initStagePanel();

        setPreferredSize(new Dimension(w, h));
        setVisible(true);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_UP){
                    gameLine.up();
                    System.out.println("up");
                }else if(e.getKeyCode() == KeyEvent.VK_DOWN){
                    gameLine.down();
                    System.out.println("down");
                }else if(e.getKeyCode() == KeyEvent.VK_LEFT){
                    gameLine.left();
                    System.out.println("left");
                }else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
                    gameLine.right();
                    System.out.println("right");
                }
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                requestFocus();
            }
        });
        requestFocus();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {

                    stagepanel.repaint();
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void initStagePanel(){

        stagepanel = new StagePanel(gameLine);

        stagepanel.setBackground(Color.white);
        stagepanel.setPreferredSize(new Dimension(930, 930));
        stagepanel.setBorder(new MatteBorder(1, 1, 1, 1, Color.black));

        initInfoPanel();

        JPanel p = new JPanel(new BorderLayout());
        p.setPreferredSize(new Dimension(930, 1000));
        p.add(stagepanel, BorderLayout.CENTER);
        p.add(infopanel, BorderLayout.SOUTH);

        add(p, BorderLayout.CENTER);

    }

    private void initInfoPanel(){

        infopanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infopanel.setPreferredSize(new Dimension(930, 70));

        JLabel info = new JLabel("迷宫大小：" + stage.length + "x" + stage.length);
        info.setFont(new Font("黑体", Font.PLAIN, 20));
        infopanel.add(info);

        endgame = new JButton("退出");
        endgame.setFont(new Font("黑体", Font.PLAIN, 20));
        endgame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        infopanel.add(endgame);
    }

    private class isGameOver implements Runnable{

        @Override
        public void run() {
            while(gameLine.getStatus() == 0){

            }
            stagepanel.setEnabled(false);
            if(gameLine.getStatus() == 1) System.out.println("Victory!");
            else System.out.println("Defeat!");
        }
    }

}
