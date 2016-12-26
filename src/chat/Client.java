package chat;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

class Client implements Closeable{
    private static final int PORT = 4242;
    private final String ip;
    private final String nickname;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    Client(String nickname, String ip){
        this.nickname = nickname;
        this.ip = ip;
    }
    boolean connect() throws IOException{
        socket = new Socket(ip, PORT);
        out = new DataOutputStream(socket.getOutputStream());
        out.writeUTF("nickname: " + nickname);
        in = new DataInputStream(socket.getInputStream());
        return !in.readUTF().equalsIgnoreCase("error");
    }

    void sendMessage(String message) throws IOException{
        out.writeUTF(message);
    }
    boolean readMessage(StringBuilder sb){
        try{
            sb.delete(0, sb.length()).append(in.readUTF());
        } catch (IOException e){
            return false;
        }
        return true;
    }
    boolean isWork(){
        return socket != null && !socket.isClosed() && socket.isConnected();
    }
    void disconnect() throws IOException {
        out.writeUTF("buy");
        close();
    }
    @Override
    public void close() throws IOException {
        out.close();
        in.close();
        socket.close();
    }
}
