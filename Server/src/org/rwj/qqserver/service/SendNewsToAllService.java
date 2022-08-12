package org.rwj.qqserver.service;

import org.rwj.qqcommon.Message;
import org.rwj.qqcommon.MessageType;
import org.rwj.qqserver.utils.Utility;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author rwj
 * @create 2022-06-28 10:37
 */
public class SendNewsToAllService implements Runnable {
    @Override
    public void run() {
        while (true){
            System.out.println("请输入服务器要推送的消息:");
            String news = Utility.readString(100);
            if("exit".equals(news)){
                break;
            }
            Message message = new Message();
            message.setMsgType(MessageType.MESSAGE_TO_ALL_MES);
            message.setSender("服务器");
            message.setContent(news);
            message.setSendTime(new Date().toString());
            System.out.println("服务器推送消息给所有人说:" + news);
            HashMap<String, ServerConnectClientThread> map = ManageServerConnectClientThread.getMap();
            Set<Map.Entry<String, ServerConnectClientThread>> entries = map.entrySet();
            for (Map.Entry<String, ServerConnectClientThread> entry : entries) {
                String onlineUserId = entry.getKey();
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(entry.getValue().getSocket().getOutputStream());
                    oos.writeObject(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
