package frame;

import entry.Cell;
import main.Game;
import main.GameLine;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.File;

public class StagePanel extends JPanel {

    private GameLine gameLine;

    public StagePanel(GameLine gameLine){ this.gameLine = gameLine; }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        int k = 30, len = k * 31;

        for (int u = 0; u < 32; ++u) {
            g.drawLine(0, u * k, len, u * k);
        }

        for (int u = 0; u < 32; ++u) {
            g.drawLine(u * k, 0, u * k, len);
        }

        Cell[][] stage = gameLine.getStage();

        for(int u = 0; u < stage.length; ++u){

            for(int v = 0; v < stage[u].length; ++v){
                if(stage[u][v].isLead == true){ g.setColor(Color.red);g.drawRect(v*k, u*k , 30, 30); g.setColor(Color.black);}
                if(stage[u][v].type != 0 && stage[u][v].type != 2){
                    g.drawImage(new ImageIcon(new File("").getAbsolutePath()+File.separator + "src/image/type_" + stage[u][v].type + ".png").getImage(), v * k + 1, u * k + 1, 28, 28, new ImageObserver() {
                        @Override
                        public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                            return false;
                        }
                    });
                }else if(stage[u][v].type == 2){
                    g.drawString(String.valueOf(stage[u][v].type), v*k+11, u*k+20);
                }
            }

        }
    }


}
