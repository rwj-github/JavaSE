package org.rwj.qqclient.service;

import java.util.HashMap;

/**
 * @author rwj
 * @create 2022-06-28 9:01
 */
public class ManageClientConnectServerThread {
    private static HashMap<String,ClientConnectServerThread> map = new HashMap<>();
    public static void addClientConnectServerThread(String userId,ClientConnectServerThread clientConnectServerThread){
        map.put(userId,clientConnectServerThread);
    }
    public static ClientConnectServerThread getClientConnectServerThread(String userId){
        return map.get(userId);
    }
}
