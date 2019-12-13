package frame;

import entry.Cell;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;
import java.io.File;
import java.util.Scanner;

public class testFrame extends JFrame {

    private int w = 945, h = 962;
    private JPanel p;
    private int[][] stage;
    private int indexX = 0, indexY = 0;

    public testFrame(int[][] stage) {

        this.stage = stage;

        p = new JPanel();
        p.setBackground(Color.white);
        p.setBorder(new MatteBorder(1, 1, 1, 1, Color.black));
        p.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println(e.getX() + " " + e.getY());
                new Thread(new Runnable() {
                    private  Graphics g = p.getGraphics();
                    @Override
                    public void run() {
                        int x = e.getX()/30*30+1, y = e.getY()/30*30+1;
                        int time = 5;
                        while(time >= 1) {
                            System.out.println(time);
                            g.setColor(Color.white);
                            g.fillRect(x+1, y+1, 28, 28);
                            g.setColor(Color.black);
                            g.drawString(String.valueOf(time), x+10, y+20);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                            time--;
                        }
                        g.setColor(Color.white);
                        g.fillRect(x+1, y+1, 28, 28);
                    }
                }).start();
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                Graphics g = p.getGraphics();
                g.setColor(Color.black);
                g.drawRect(indexX, indexY, 30, 30);
                if(e.getKeyCode() == KeyEvent.VK_UP){
                    if(indexY >= 30) indexY -= 30;
                }else if(e.getKeyCode() == KeyEvent.VK_DOWN){
                    if(indexY <= 870) indexY += 30;
                }else if(e.getKeyCode() == KeyEvent.VK_LEFT){
                    if(indexX >= 30) indexX -= 30;
                }else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
                    if(indexX <= 870) indexX += 30;
                }
                g.setColor(new Color(255, 0, 0));
                g.drawRect(indexX, indexY, 30, 30);
            }
        });


        setResizable(false);
        setLayout(new BorderLayout());
        add(p, BorderLayout.CENTER);
        setSize(w, h);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(3);
        setVisible(true);
        requestFocus();
        new Thread(new DrawStage(p)).start();
    }

    class DrawStage implements Runnable {

        private JPanel p;

        public DrawStage(JPanel p) {
            this.p = p;
        }

        @Override
        public void run() {

            int k = 30, len = k * 31;
            Graphics g = p.getGraphics();

            for (int u = 0; u < 32; ++u) {
                g.drawLine(0, u * k, len, u * k);
                if (u == 10) new Thread(new DrawStage1(p)).start();
                try {
                    Thread.sleep(70);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class DrawStage1 implements Runnable {

        private JPanel p;

        public DrawStage1(JPanel p) {
            this.p = p;
        }

        @Override
        public void run() {

            int k = 30, len = k * 31;
            Graphics g = p.getGraphics();

            for (int u = 0; u < 32; ++u) {
                g.drawLine(u * k, 0, u * k, len);
                try {
                    Thread.sleep(70);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            for(int u = 0; u < stage.length; ++u){
                for(int v = 0; v < stage[u].length; ++v){
                    if(stage[u][v] != 0){
                        g.drawImage(new ImageIcon(this.getClass().getClassLoader().getResource("Image/type_" + stage[u][v] + ".png")).getImage(), v * k + 1, u * k + 1, 28, 28, new ImageObserver() {
                            @Override
                            public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                                return false;
                            }
                        });
                    }
                }
            }

            g.setColor(Color.red);
            g.drawRect(indexX, indexY, 30, 30);
        }
    }

    public static void main(String[] args) {

//        int[][] s = {{1,1,1,1,1,1,4,4,3,3,4,5}, {1,1,1,1,1,1,4,4,3,3,4,5}, {1,1,1,1,1,1,4,4,3,3,4,5}};
//
//        new testFrame(s);

        Scanner sc = new Scanner(System.in);
        while(sc.hasNext()){
            int x = sc.nextInt();
            int y = sc.nextInt();
            x = Math.max(0, x - 7 + Math.min(30-x-7, 0));
            y = Math.max(0, y - 7 + Math.min(30-y-7, 0));
            System.out.println(x + " " + y);
        }
    }

}
