package frame;

import entry.Cell;
import main.GameLine;
import main.HomeownerLine;
import main.RoomLine;
import main.Stage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameFrame extends JFrame {

    private int w = 1800, h = 1000;
    private Cell[][] stage;
    private JPanel stagepanel, bottom;
    private JButton[][] stagecell;
    private JButton endgame;

    public GameFrame(HomeownerLine homeownerLine, GameLine gameLine){

        stage = gameLine.getStage();

        setLayout(new BorderLayout());
        initStagePanel();
        initBottom();

        setUndecorated(true);
        setSize(w, h);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(3);
        setVisible(true);
    }

    public GameFrame(RoomLine roomLine, GameLine gameLine){

        stage = gameLine.getStage();

        setLayout(new BorderLayout());
        initStagePanel();
        initBottom();

        setUndecorated(true);
        setSize(w, h);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(3);
        setVisible(true);
    }

    private void initStagePanel(){

        stagepanel.setPreferredSize(new Dimension(1000, 1000));
        add(stagepanel, BorderLayout.CENTER);

    }

    private void initBottom(){

        bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel info = new JLabel("迷宫大小：" + stage.length + "x" + stage.length);
        info.setFont(new Font("黑体", Font.PLAIN, 20));
        bottom.add(info);

        endgame = new JButton("退出");
        endgame.setFont(new Font("黑体", Font.PLAIN, 20));
        endgame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        bottom.add(endgame);

        add(bottom, BorderLayout.SOUTH);

    }

}
