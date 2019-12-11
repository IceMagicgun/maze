package frame;

import entry.IP;
import network.Client;
import network.FindServer;
import network.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ServerListFrame extends JFrame {

    private int w = 500, h = 800;
    private FindServer findServer;
    private JLabel title;
    private List<IP> serverlist;
    private JScrollPane roomscroll;
    private JButton[] rooms;
    private JButton refresh;
    private JPanel top, center;

    public ServerListFrame() {

        findServer = new FindServer();

        title = new JLabel("房间列表:");
        title.setFont(new Font("黑体", Font.PLAIN, 20));

        roomscroll = new JScrollPane();

        top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(title);
        refresh = new JButton("刷新");
        refresh.setFont(new Font("黑体", Font.PLAIN, 15));
        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Refresh();
            }
        });
        top.add(refresh);

        center = new JPanel(new BorderLayout());
        Refresh();

        setLayout(new BorderLayout());
        add(top, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        setSize(w, h);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(3);
        setVisible(true);
    }


    private void Refresh(){
        serverlist = findServer.getSeverIpList();
        Box roombox = Box.createVerticalBox();
        rooms = new JButton[serverlist.size()];
        for (int u = 0; u < serverlist.size(); ++u) {
            rooms[u] = new JButton("Room" + (u + 1) + ": " + serverlist.get(u).name);
            rooms[u].setFont(new Font("黑体", Font.PLAIN, 18));
            rooms[u].addActionListener(new JoinRoom());
            roombox.add(rooms[u]);
        }
        roomscroll.setViewportView(roombox);
        center.add(roomscroll, BorderLayout.CENTER);
    }

    class JoinRoom implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            for(int u = 0; u < serverlist.size(); ++u){
                if(e.getSource() == rooms[u]){
                    Client client = new Client(serverlist.get(u), JOptionPane.showInputDialog("请输入昵称"));
                    new RoomFrame(serverlist.get(u).name, client);
                    dispose();
                }
            }
        }
    }

}
