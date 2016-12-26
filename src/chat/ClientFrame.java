package chat;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

class ClientFrame extends JFrame {
    private JTextArea chatArea;
    private JTextArea messageArea;
    private Client client;
    private JButton sendButton;
    private JButton disconnectButton;
    private JButton reconnectButton;
    private DataDialog dataDialog;

    ClientFrame(){
        chatArea = new JTextArea();
        messageArea = new JTextArea();
        sendButton = new JButton("Send");
        disconnectButton = new JButton("Disconnect");
        reconnectButton = new JButton("Reconnect");
        dataDialog = new DataDialog(this);
        dataDialog.showDialog();
        client = new Client(dataDialog.getNickname(), dataDialog.getIp());
        setTitle("Client: " + dataDialog.getNickname());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    void createGUI(){
        final int AREA_SIZE = 300;
        chatArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setEditable(false);
        chatArea.setPreferredSize(new Dimension(AREA_SIZE, AREA_SIZE));
        messageArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setPreferredSize(new Dimension(AREA_SIZE, AREA_SIZE / 3));

        sendButton.addActionListener(e -> sendMessage());
        disconnectButton.addActionListener(e -> disconnect());
        reconnectButton.addActionListener(e -> reconnect());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(reconnectButton);
        buttonPanel.add(disconnectButton);
        buttonPanel.add(sendButton);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(new JScrollPane(chatArea));
        add(messageArea);
        add(buttonPanel);
        setResizable(false);
        pack();
        connect();
    }

    private void sendMessage(){
        try {
            client.sendMessage(messageArea.getText());
        } catch (IOException ex){
            new ErrorDialog(this, ex.toString());
        }
    }
    private void disconnect(){
        try{
            client.disconnect();
            disconnectButton.setEnabled(false);
            sendButton.setEnabled(false);
        } catch (IOException ex){
            new ErrorDialog(this, ex.toString());
        }
    }
    private void connect(){
        try {
            if(!client.connect()){
                new ErrorDialog(this, "Username is already exists");
                disconnectButton.setEnabled(false);
                sendButton.setEnabled(false);
            } else {
                new Thread(this::receivingMessages).start();
            }
        } catch (IOException e){
            new ErrorDialog(null, e.toString());
            disconnectButton.setEnabled(false);
            sendButton.setEnabled(false);
        }
    }
    private void reconnect(){
        if (client.isWork())
            disconnect();
        dataDialog.showDialog();
        client = new Client(dataDialog.getNickname(), dataDialog.getIp());
        connect();
    }
    private void receivingMessages(){
        while(client.isWork()){
            try{
                StringBuilder messageBuilder = new StringBuilder();
                if (client.readMessage(messageBuilder)) {
                    String message = messageBuilder.toString();
                    if (message.equalsIgnoreCase("buy")) {
                        client.close();
                        chatArea.append("\nYou have been disconnected from the server\n");
                        disconnectButton.setEnabled(false);
                        sendButton.setEnabled(false);
                    } else
                        chatArea.append(message);
                }
            }catch (IOException e){
                new ErrorDialog(null, e.toString());
            }
        }
    }
}
