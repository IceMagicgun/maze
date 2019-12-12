package frame;

import entry.Cell;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;

public class StagePanel extends JPanel {

    private Cell[][] stage;
    private int flag = 0;

    public StagePanel(Cell[][] stage){ this.stage = stage; }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        new Thread(new DrawStage(this)).start();
    }

    class DrawStage implements Runnable {

        private JPanel p;

        public DrawStage(JPanel p) { this.p = p; }

        @Override
        public void run() {

            int k = 30, len = k * 31;
            Graphics g = p.getGraphics();

            for (int u = 0; u < 32; ++u) {
                g.drawLine(0, u * k, len, u * k);
                if (u == 10) new Thread(new DrawStage1(p)).start();
                if(flag == 0) {
                    try {
                        Thread.sleep(70);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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
                if(flag == 0) {
                    try {
                        Thread.sleep(70);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            flag = 1;

            for(int u = 0; u < stage.length; ++u){
                for(int v = 0; v < stage[u].length; ++v){
                    if(stage[u][v].isLead == true){ g.setColor(Color.red);g.drawRect(v*k, u*k , 30, 30); g.setColor(Color.black);}
                    if(stage[u][v].type != 0 && stage[u][v].type != 3 && stage[u][v].type != 2){
                        g.drawImage(new ImageIcon("src/image/type_" + stage[u][v].type + ".png").getImage(), v * k + 1, u * k + 1, 28, 28, new ImageObserver() {
                            @Override
                            public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                                return false;
                            }
                        });
                    }else if(stage[u][v].type == 3 || stage[u][v].type == 2){
                        g.drawString(String.valueOf(stage[u][v].type), v*k+11, u*k+20);
                    }
                }
            }
        }
    }

}
