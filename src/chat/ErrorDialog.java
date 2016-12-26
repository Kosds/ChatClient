package chat;

import javax.swing.*;
import java.awt.*;

class ErrorDialog extends JDialog{
    ErrorDialog(JFrame parent, String message){
        super(parent, "Error", true);
        setLayout(new BorderLayout(10, 10));
        add(new Label(message), BorderLayout.CENTER);
        setResizable(false);
        pack();
        setVisible(true);
    }
}
