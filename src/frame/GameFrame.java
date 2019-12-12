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

public class GameFrame extends JFrame {

    private int w = 1600, h = 1000;
    private Cell[][] stage;
    private JPanel stagepanel, infopanel, chatpanel;
    private JButton[][] stagecell;
    private JButton endgame;
    private JTextArea chatarea;
    private JTextField input;
    private StringBuffer messages;
    private HomeownerLine homeownerLine;
    private RoomLine roomLine;

    public GameFrame(HomeownerLine homeownerLine, GameLine gameLine){

        this.homeownerLine = homeownerLine;
        this.roomLine = null;

        stage = gameLine.getStage();

        setLayout(new BorderLayout());
        initStagePanel();
        initChatPanel();

        setUndecorated(true);
        setSize(w, h);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(3);
        setVisible(true);

//        new Thread(new Chat(homeownerLine.getChat())).start();
    }

    public GameFrame(RoomLine roomLine, GameLine gameLine){

        this.roomLine = roomLine;
        this.homeownerLine = null;

        stage = gameLine.getStage();

        setLayout(new BorderLayout());
        initStagePanel();
        initChatPanel();

        setUndecorated(true);
        setSize(w, h);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(3);
        setVisible(true);

//        new Thread(new Chat(roomLine.getChat())).start();
    }

    private void initStagePanel(){

        stagepanel = new StagePanel(stage);

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

    private void initChatPanel(){

        chatpanel = new JPanel(new BorderLayout());
        chatpanel.setPreferredSize(new Dimension(670, 1000));
        chatpanel.setBorder(new MatteBorder(2, 2, 2, 2, Color.black));

        chatarea = new JTextArea();
        chatarea.setLineWrap(true);
        chatarea.setEditable(false);
        chatarea.setFont(new Font("黑体", Font.PLAIN, 20));
        JScrollPane chatscroll = new JScrollPane();
        chatscroll.setViewportView(chatarea);

        input = new JTextField();
        input.setFont(new Font("黑体", Font.PLAIN, 20));
        input.setPreferredSize(new Dimension(670, 40));
        input.setBorder(new MatteBorder(1, 1, 1, 1,Color.green));
        input.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
//                    if(homeownerLine != null) {
//                        System.out.println(1);
//                        homeownerLine.chat(input.getText());
//                    }
//                    else {
//                        System.out.println(2);
//                        roomLine.chat(input.getText());
//                    }
                    input.setText("");
                }
            }
        });

        chatpanel.add(chatscroll, BorderLayout.CENTER);
        chatpanel.add(input, BorderLayout.SOUTH);

        add(chatpanel, BorderLayout.EAST);
    }

    private class Chat implements Runnable{
        private ArrayBlockingQueue<Map<String,String>> chatQueue;

        public Chat(ArrayBlockingQueue<Map<String,String>> chatQueue){
            this.chatQueue = chatQueue;
        }
        @Override
        public void run() {
            while(true) {
                try {
                    Map<String,String> map = chatQueue.take();
                    messages.append(map.get("talker") + ":" + map.get("message") + "\n");
                    chatarea.setText(messages.toString());
                    chatarea.setCaretPosition(chatarea.getDocument().getLength());
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
