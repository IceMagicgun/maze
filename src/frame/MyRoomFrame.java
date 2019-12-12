package frame;

import main.HomeownerLine;
import network.Link;
import network.Server;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

public class MyRoomFrame extends JFrame {

    private int w = 800, h = 800;
    private Server server;
    private HomeownerLine homeownerLine;
    private JTextField roomname;
    private JPanel top, center, bottom, chatpanel;
    private List<Link> playerlist;
    private JScrollPane playerscroll;
    private JButton refresh, startgame;
    private JTextArea chatarea;
    private JTextField input, anothergamer, role;
    private StringBuffer messages;
    private Thread chatthread;
    private GamePanel gamePanel;

    public MyRoomFrame() {
        server = new Server();
        server.start();

        messages = new StringBuffer();
        homeownerLine = new HomeownerLine();
        chatthread = new Thread(new Chat(homeownerLine.getChat()));
        chatthread.start();

        setLayout(new BorderLayout());

        initTop();
        initCenter();
        initBottom();

        setSize(w, h);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(3);
        setVisible(true);
    }

    private void initTop() {
        top = new JPanel(new BorderLayout());

        roomname = new JTextField(Server.getName());
        roomname.setFont(new Font("黑体", Font.BOLD, 25));
        roomname.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                Server.changeName(roomname.getText());
            }
        });
        top.add(roomname);
        add(top, BorderLayout.NORTH);
    }

    private void initCenter() {
        center = new JPanel(new BorderLayout());

        JLabel title = new JLabel("PlayerList: ");
        title.setFont(new Font("黑体", Font.PLAIN, 20));

        refresh = new JButton("刷新");
        refresh.setFont(new Font("黑体", Font.PLAIN, 15));
        refresh.setFocusPainted(false);
        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RefreshPlayerList();
            }
        });

        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.add(title);
        p.add(refresh);
        center.add(p, BorderLayout.NORTH);

        playerscroll = new JScrollPane();
        RefreshPlayerList();

        add(center, BorderLayout.CENTER);
    }

    private void initBottom(){
        bottom = new JPanel(new BorderLayout());

        chatpanel = new JPanel(new BorderLayout());
        chatpanel.setPreferredSize(new Dimension(500, 300));
        chatpanel.setBorder(new MatteBorder(2, 2, 2, 2, Color.black));

        chatarea = new JTextArea();
        chatarea.setLineWrap(true);
        chatarea.setEditable(false);
        chatarea.setFont(new Font("黑体", Font.PLAIN, 20));

        JScrollPane chatscroll = new JScrollPane();
        chatscroll.setViewportView(chatarea);

        input = new JTextField();
        input.setFont(new Font("黑体", Font.PLAIN, 20));
        input.setPreferredSize(new Dimension(500, 40));
        input.setBorder(new MatteBorder(1, 1, 1, 1,Color.green));
        input.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    homeownerLine.chat(input.getText());
                    input.setText("");
                }
            }
        });

        chatpanel.add(chatscroll, BorderLayout.CENTER);
        chatpanel.add(input, BorderLayout.SOUTH);

        JPanel gamebeginpanel = new JPanel(new GridLayout(3, 1));
        gamebeginpanel.setPreferredSize(new Dimension(300, 300));

        JLabel p1 = new JLabel("输入另一名玩家的序号:");
        p1.setFont(new Font("黑体", Font.PLAIN, 20));
        JLabel p2 = new JLabel("选择角色序号:");
        p2.setFont(new Font("黑体", Font.PLAIN, 20));

        anothergamer = new JTextField();
        anothergamer.setFont(new Font("黑体", Font.PLAIN, 20));
        anothergamer.setPreferredSize(new Dimension(80, 40));
        role = new JTextField();
        role.setFont(new Font("黑体", Font.PLAIN, 20));
        role.setPreferredSize(new Dimension(80, 40));
        role.setToolTipText("1：引路人，2：逃生者");

        startgame = new JButton("开始游戏");
        startgame.setFont(new Font("黑体", Font.PLAIN, 20));
        startgame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                homeownerLine.game(playerlist.get(Integer.parseInt(anothergamer.getText())), Integer.parseInt(role.getText()));
                setVisible(false);
                getContentPane().removeAll();
                w = 1430; h = 1000;
                setSize(w, h);
                setLocationRelativeTo(null);
                add(chatpanel, BorderLayout.WEST);
                gamePanel =  new GamePanel(homeownerLine.getGameLine());
                add(gamePanel, BorderLayout.CENTER);
                setVisible(true);
            }
        });

        JPanel pp1 = new JPanel();
        pp1.add(p1); pp1.add(anothergamer);
        JPanel pp2 = new JPanel();
        pp2.add(p2); pp2.add(role);
        JPanel pp3 = new JPanel();
        pp3.add(startgame);

        gamebeginpanel.add(pp1); gamebeginpanel.add(pp2); gamebeginpanel.add(pp3);

        bottom.add(chatpanel, BorderLayout.CENTER);
        bottom.add(gamebeginpanel, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);
    }

    private void RefreshPlayerList() {
        playerlist = Server.getLinkList();
        Box playerbox = Box.createVerticalBox();
        JLabel[] players = new JLabel[playerlist.size()];
        for (int u = 0; u < playerlist.size(); ++u) {
            players[u] = new JLabel("Player" + (u + 1) + "." + playerlist.get(u).getName() + "       " + playerlist.get(u).getPing() + "ms");
            players[u].setFont(new Font("黑体", Font.PLAIN, 18));
            playerbox.add(players[u]);
        }
        playerscroll.setViewportView(playerbox);
        center.add(playerscroll, BorderLayout.CENTER);
    }

    public class Chat implements Runnable{
        private ArrayBlockingQueue<Map<String,String>> chatQueue;
 
        public Chat(ArrayBlockingQueue<Map<String,String>> chatQueue){
            this.chatQueue=chatQueue;
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
