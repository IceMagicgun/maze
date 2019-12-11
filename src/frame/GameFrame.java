package frame;

import entry.Cell;
import main.GameLine;
import main.HomeownerLine;
import main.RoomLine;
import main.Stage;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    private int w = 1800, h = 1000;
    private Cell[][] stage;
    private JPanel stagepanel;
    private JButton[][] stagecell;

    public GameFrame(HomeownerLine homeownerLine, GameLine gameLine){

        stage = gameLine.getStage();

        setLayout(new BorderLayout());
        initStagePanel();

        setUndecorated(true);
        setSize(w, h);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(3);
        setVisible(true);
    }

    public GameFrame(RoomLine roomLine, GameLine gameLine){

        setUndecorated(true);
        setSize(w, h);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(3);
        setVisible(true);
    }

    private void initStagePanel(){

        int len = stage.length;
        stagepanel = new JPanel(new GridLayout(len, len));
        stagecell = new JButton[len][len];

        for(int u = 0; u < len; ++u){
            for(int v = 0; v < len; ++v){
                stagecell[u][v] = new JButton(String.valueOf(stage[u][v].type));
                stagecell[u][v].setMargin(new Insets(0, 0, 0, 0));
                stagepanel.add(stagecell[u][v]);
            }
        }
        add(stagepanel, BorderLayout.CENTER);
    }

}
