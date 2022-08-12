package org.rwj.qqserver.service;

import org.rwj.qqcommon.Message;
import org.rwj.qqcommon.MessageType;
import org.rwj.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author rwj
 * @create 2022-06-28 8:41
 */
public class QQServer {
    private ServerSocket ss = null;
    private static ConcurrentHashMap<String, User> validUsers = new ConcurrentHashMap<>();
    static { //在静态代码块，初始化 validUsers

        validUsers.put("100", new User("100", "123456"));
        validUsers.put("200", new User("200", "123456"));
        validUsers.put("300", new User("300", "123456"));
        validUsers.put("至尊宝", new User("至尊宝", "123456"));
        validUsers.put("紫霞仙子", new User("紫霞仙子", "123456"));
        validUsers.put("菩提老祖", new User("菩提老祖", "123456"));
    }

    private boolean checkUser(String userId,String pwd){
        User user = validUsers.get(userId);
        if(user == null){
            return false;
        }
        if(!user.getPwd().equals(pwd)){
            return false;
        }
        return true;
    }
    public QQServer(){
        System.out.println("服务器端在9999端口监听。。。");
        try {
            new Thread(new SendNewsToAllService()).start();
            ss = new ServerSocket(9999);
            while (true){
                Socket socket = ss.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                User user = (User) ois.readObject();
                Message message = new Message();
                if(checkUser(user.getUserId(),user.getPwd())){
                    message.setMsgType(MessageType.MESSAGE_LOGIN_SUCCEED);
                    oos.writeObject(message);
                    ServerConnectClientThread serverConnectClientThread = new ServerConnectClientThread(user.getUserId(), socket);
                    serverConnectClientThread.start();
                    ManageServerConnectClientThread.addServerConnectClientThread(user.getUserId(),serverConnectClientThread);
                }else{
                    System.out.println("用户 id=" + user.getUserId() + " pwd=" + user.getPwd() + " 验证失败");
                    message.setMsgType(MessageType.MESSAGE_LOGIN_FAIL);
                    oos.writeObject(message);
                    socket.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                ss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
