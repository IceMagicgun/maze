package frame;

import other.Path;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.*;

public class StartFrame extends JFrame {

    private int w = 500, h = 500;
    private JLabel tip, title, p;
    private RButton createRoom, findRoom;

    public StartFrame(){

        title = new JLabel("双人迷宫游戏");
        title.setFont(new Font("黑体", Font.BOLD, 45));
        title.setBounds(100, 70, 300, 70);

        p = new JLabel("->");
        p.setBounds(165, 220, 50, 50);
        p.setFont(new Font("黑体", Font.BOLD, 20));

        createRoom = new RButton("创建房间");
        createRoom.setBounds(190, 220, 120, 50);
        createRoom.requestFocus();
        createRoom.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    new MyRoomFrame();
                    dispose();
                }else if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN){
                    p.setBounds(165, 320, 120, 50);
                    findRoom.requestFocus();
                }
            }
        });
        createRoom.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                p.setBounds(165, 220, 120, 50);
                createRoom.requestFocus();
            }
        });
        createRoom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MyRoomFrame();
                dispose();
            }
        });

        findRoom = new RButton("查找房间");
        findRoom.setBounds(190, 320, 120, 50);
        findRoom.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    new ServerListFrame();
                    dispose();
                }else if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN){
                    p.setBounds(165, 220, 120, 50);
                    createRoom.requestFocus();
                }
            }
        });
        findRoom.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                p.setBounds(165, 320, 120, 50);
                findRoom.requestFocus();
            }
        });
        findRoom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ServerListFrame();
                dispose();
            }
        });

        tip = new JLabel("Maze Game @Verson " + Path.ver);
        tip.setBounds(0, 448, 200, 30);

        setLayout(null);
        add(p);
        add(title);
        add(createRoom);
        add(findRoom);
        add(tip);
        setSize(w, h);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(3);
        setVisible(true);
    }



    public static void main(String[] args) {
        new StartFrame();
    }

}
