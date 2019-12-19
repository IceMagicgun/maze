package frame;

import main.RoomLine;
import network.Client;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

public class RoomFrame extends JFrame {

    private int w = 500, h = 800;
    private JLabel title;
    private RoomLine roomLine;
    private Client client;
    private StringBuffer messages;
    private JTextArea chatarea;
    private JPanel center;
    private JTextField name, input;
    private Thread chatthread;
    private GamePanel gamePanel;

    public RoomFrame(String roomownername, Client client){

        this.client = client;

        title = new JLabel("房主: " + roomownername);
        title.setFont(new Font("黑体", Font.BOLD, 20));

        messages = new StringBuffer("等待房主开始游戏.....\n");
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        roomLine = new RoomLine(client);
        chatthread = new Thread(new Chat(roomLine.getChat()));
        chatthread.start();
        new Thread(new isGameStart()).start();

        initCenter();

        setLayout(new BorderLayout());
        add(title, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        setSize(w, h);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(3);
        setVisible(true);
    }

    private void initCenter(){
        center = new JPanel(new BorderLayout());

        name = new JTextField(client.getName());
        name.setFont(new Font("黑体", Font.PLAIN, 20));
        name.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                roomLine.changeName(name.getText());
            }
        });

        JPanel chatpanel = new JPanel(new BorderLayout());
        chatpanel.setBorder(new MatteBorder(2, 2, 2, 2, Color.black));

        chatarea = new JTextArea(messages.toString());
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
                    if(!input.getText().equals("")) {
                        roomLine.chat(input.getText());
                        input.setText("");
                    }
                }
            }
        });

        chatpanel.add(chatscroll, BorderLayout.CENTER);
        chatpanel.add(input, BorderLayout.SOUTH);

        center.add(name, BorderLayout.NORTH);
        center.add(chatpanel, BorderLayout.CENTER);
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

    private class isGameStart implements Runnable{
        @Override
        public void run() {
            while(true){
                try {
                    if(roomLine.getGameLine() != null){
                        messages.append("房主已开始游戏......\n");
                        chatarea.setText(messages.toString());
                        while(roomLine.getGameLine().getStage() == null){

                        }
                        gamePanel = new GamePanel(roomLine.getGameLine());
                        setVisible(false);
                        Thread.sleep(1000);
                        getContentPane().removeAll();
                        w = 1440; h = 1000;
                        setSize(w, h);
                        setLocationRelativeTo(null);
                        add(center, BorderLayout.WEST);
                        add(gamePanel, BorderLayout.CENTER);
                        setVisible(true);
                        break;
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
