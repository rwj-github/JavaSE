package org.rwj.qqserver.service;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.rwj.qqcommon.Message;
import org.rwj.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author rwj
 * @create 2022-06-28 9:14
 */
public class ServerConnectClientThread extends Thread{
    private String userId;
    private Socket socket;

    public ServerConnectClientThread(String userId, Socket socket) {
        this.userId = userId;
        this.socket = socket;
    }
    public Socket getSocket(){
        return socket;
    }

    @Override
    public void run() {
        while (true){
            System.out.println("服务端和客户端" + userId + " 保持通信，读取数据...");
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();
                if(message.getMsgType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)){
                    System.out.println(message.getSender() + " 要在线用户列表");
                    String onlineUser = ManageServerConnectClientThread.getOnlineUser();
                    Message message1 = new Message();
                    message1.setMsgType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                    message1.setContent(onlineUser);
                    message1.setGetter(message.getSender());
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message1);
                }else if(message.getMsgType().equals(MessageType.MESSAGE_TO_ALL_MES)){
                    HashMap<String, ServerConnectClientThread> map = ManageServerConnectClientThread.getMap();
                    Set<Map.Entry<String, ServerConnectClientThread>> entries = map.entrySet();
                    for (Map.Entry<String, ServerConnectClientThread> entry : entries) {
                        String onlineUserId = entry.getKey();
                        if(!onlineUserId.equals(message.getSender())){
                            ObjectOutputStream oos = new ObjectOutputStream(entry.getValue().getSocket().getOutputStream());
                            oos.writeObject(message);
                        }
                    }
                }else if(message.getMsgType().equals(MessageType.MESSAGE_COMM_MES)){
                    ObjectOutputStream oos = new ObjectOutputStream(ManageServerConnectClientThread.getServerConnectClientThread(message.getGetter()).getSocket().getOutputStream());
                    oos.writeObject(message);
                }else if(message.getMsgType().equals(MessageType.MESSAGE_FILE_MES)){
                    ObjectOutputStream oos = new ObjectOutputStream(ManageServerConnectClientThread.getServerConnectClientThread(message.getGetter()).getSocket().getOutputStream());
                    oos.writeObject(message);
                }else{
                    System.out.println("其他类型的消息，暂时不做处理");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
