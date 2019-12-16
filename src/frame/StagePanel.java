package frame;

import entry.Cell;
import main.GameLine;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;

public class StagePanel extends JPanel {

    private GameLine gameLine;
    private int role;

    public StagePanel(GameLine gameLine, int role) {
        this.gameLine = gameLine;
        this.role = role;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Cell[][] stage = gameLine.getStage();

        if(role == 1 || role == 3) {
            int k = 30, len = k * 31;
            for (int u = 0; u < 32; ++u) { g.drawLine(0, u * k, len, u * k); }
            for (int u = 0; u < 32; ++u) { g.drawLine(u * k, 0, u * k, len); }
            for (int u = 0; u < stage.length; ++u) {
                for (int v = 0; v < stage[u].length; ++v) {
                    if ((stage[u][v].type == 2 || stage[u][v].type == 3) && (stage[u][v].status == 1 || stage[u][v].status == 2)) {

                        if (stage[u][v].status == 1) {
                            g.setColor(new Color(255, 0, 0));
                            g.drawRect(v * k, u * k, 30, 30);
                            g.setColor(new Color(0, 0, 0));
                        } else {
                            g.setColor(new Color(0, 255, 0));
                            g.drawRect(v * k, u * k, 30, 30);
                            g.setColor(new Color(0, 0, 0));
                        }
                        g.drawString(String.valueOf(stage[u][v].time), v * k + 10, u * k + 20);
                        System.out.println(String.valueOf(stage[u][v].time));

                    } else if (stage[u][v].type != 0 && stage[u][v].type != 2) {
                        g.drawImage(new ImageIcon(this.getClass().getClassLoader().getResource("Image/type_" + stage[u][v].type + ".png")).getImage(), v * k + 1, u * k + 1, 28, 28, new ImageObserver() {
                            @Override
                            public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                                return false;
                            }
                        });
                    }
                }
            }
        }else{
            int k = 62, len = k*15, x = -1, y = -1;
            for (int u = 0; u < 16; ++u) { g.drawLine(0, u * k, len, u * k); }
            for (int u = 0; u < 16; ++u) { g.drawLine(u * k, 0, u * k, len); }
            //ÕÒµ½Æðµã
            for(int u = 0; u < stage.length; ++u){
                for(int v = 0; v < stage[u].length; ++v){
                    if(stage[u][v].isLead == true){
                        x = u; y = v;
                    }
                }
            }
            x = Math.max(0, x - 7 + Math.min(30-x-7, 0));
            y = Math.max(0, y - 7 + Math.min(30-y-7, 0));
            for(int u = x; u < x+15; ++u){
                for(int v = y; v < y+15; ++v){
                    if(stage[u][v].isLead == true){
                        g.drawImage(new ImageIcon(this.getClass().getClassLoader().getResource("Image/type_man.png")).getImage(), (v-y) * k + 1, (u-x) * k + 1, 60, 60, new ImageObserver() {
                            @Override
                            public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                                return false;
                            }
                        });
                    }
                    if(stage[u][v].type == 5){
                        g.drawImage(new ImageIcon(this.getClass().getClassLoader().getResource("Image/type_5.png")).getImage(), (v-y) * k + 1, (u-x) * k + 1, 60, 60, new ImageObserver() {
                            @Override
                            public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                                return false;
                            }
                        });
                    }
                }
            }
        }
    }


}
