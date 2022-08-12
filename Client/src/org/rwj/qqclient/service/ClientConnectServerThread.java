package org.rwj.qqclient.service;

import org.rwj.qqcommon.Message;
import org.rwj.qqcommon.MessageType;

import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * @author rwj
 * @create 2022-06-28 8:57
 */
public class ClientConnectServerThread extends Thread{
    private Socket socket;

    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket(){
        return socket;
    }
    @Override
    public void run() {
        System.out.println("客户端线程，等待读取从服务器端发送的消息");
        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message message = (Message) ois.readObject();
            if(message.getMsgType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)){
                String content = message.getContent();
                String[] s = content.split(" ");
                System.out.println("\n=======当前在线用户列表========");
                for (int i = 0;i < s.length;i++){
                    System.out.println("用户:" + s[i]);
                }
            }else if(message.getMsgType().equals(MessageType.MESSAGE_TO_ALL_MES)){
                System.out.println(message.getSender() + "对大家说:" + message.getContent());
            }else if(message.getMsgType().equals(MessageType.MESSAGE_COMM_MES)){
                System.out.println(message.getSender() + " 对 " + message.getGetter() + " 说 " + message.getContent());
            }else if(message.getMsgType().equals(MessageType.MESSAGE_FILE_MES)){
                System.out.println("\n" + message.getSender() + " 给 " + message.getGetter()
                        + " 发文件: " + message.getSrc() + " 到我的电脑的目录 " + message.getDest());
                FileOutputStream fos = new FileOutputStream(message.getDest(),true);
                fos.write(message.getFileBytes());
                fos.close();
            }else{
                System.out.println("其他类型的消息，暂时不做处理");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
