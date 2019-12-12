package frame;

import other.Path;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartFrame extends JFrame {

    private int w = 500, h = 500;
    private JLabel tip;
    private JButton createRoom, findRoom;

    public StartFrame(){
        createRoom = new JButton("创建房间");
        createRoom.setFocusPainted(false);
        createRoom.setBackground(Color.white);
        createRoom.setFont(new Font("黑体", Font.PLAIN, 21));
        createRoom.setBounds(300, 260, 120, 50);
        createRoom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MyRoomFrame();
                dispose();
            }
        });

        findRoom = new JButton("查找房间");
        findRoom.setFocusPainted(false);
        findRoom.setBackground(Color.white);
        findRoom.setFont(new Font("黑体", Font.PLAIN, 21));
        findRoom.setBounds(300, 340, 120, 50);
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
