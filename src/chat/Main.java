package chat;

import java.awt.*;
import java.io.IOException;

class Main {
    public static void main(String[] args) throws IOException, InterruptedException{
        EventQueue.invokeLater(() -> new ClientFrame().createGUI());
    }
}
