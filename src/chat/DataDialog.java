package chat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class DataDialog extends JDialog {
    private String ip;
    private String nickname;
    private JTextField ipField;
    private JTextField nicknameField;
    private boolean isCreated;

    DataDialog(JFrame parent){
        super(parent, "Connect Data", true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        ipField = new JTextField();
        nicknameField = new JTextField();
        isCreated = false;
    }

    void showDialog(){
        if(!isCreated) {
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosed(e);
                    System.exit(0);
                }
            });

            Box ipBox = Box.createHorizontalBox();
            ipBox.add(new JLabel("Server IP"));
            ipBox.add(Box.createGlue());
            ipBox.add(formatField(ipField));

            Box nicknameBox = Box.createHorizontalBox();
            nicknameBox.add(new JLabel("Nickname"));
            nicknameBox.add(Box.createGlue());
            nicknameBox.add(formatField(nicknameField));

            JButton okButton = new JButton("OK");
            okButton.addActionListener(e -> {
                ip = ipField.getText();
                nickname = nicknameField.getText();
                if (!ip.isEmpty() && !nickname.isEmpty())
                    dispose();
                else
                    new ErrorDialog(null, "Поля на заполнены");
            });
            okButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);

            setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
            add(ipBox);
            add(nicknameBox);
            add(okButton);
            setResizable(false);
            setSize(400, 150);
            isCreated = true;
        }
        pack();
        setVisible(true);
    }
    String getIp() {
        return ip;
    }
    String getNickname() {
        return nickname;
    }
    private JTextField formatField(JTextField field){
        Dimension fixDimension = new Dimension(200, 30);
        field.setMaximumSize(fixDimension);
        field.setPreferredSize(fixDimension);
        field.setMinimumSize(fixDimension);
        field.setFont(field.getFont().deriveFont(16.0f));
        return field;
    }
}
