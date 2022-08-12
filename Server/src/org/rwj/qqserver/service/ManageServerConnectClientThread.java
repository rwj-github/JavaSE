package org.rwj.qqserver.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author rwj
 * @create 2022-06-28 9:19
 */
public class ManageServerConnectClientThread {
    private static HashMap<String,ServerConnectClientThread> map = new HashMap<>();
    public static void addServerConnectClientThread(String userId,ServerConnectClientThread serverConnectClientThread){
        map.put(userId,serverConnectClientThread);
    }
    public static ServerConnectClientThread getServerConnectClientThread(String userId){
        return map.get(userId);
    }
    public static String getOnlineUser(){
        Set<Map.Entry<String, ServerConnectClientThread>> entries = map.entrySet();
        String onlineUserList = "";
        for (Map.Entry<String, ServerConnectClientThread> entry : entries) {
            onlineUserList += entry.getKey() + " ";
        }
        return onlineUserList;
    }

    public static HashMap<String, ServerConnectClientThread> getMap() {
        return map;
    }
}
