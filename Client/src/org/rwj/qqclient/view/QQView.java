package org.rwj.qqclient.view;

import org.rwj.qqclient.service.FileClientService;
import org.rwj.qqclient.service.MessageClientService;
import org.rwj.qqclient.service.UserClientService;
import org.rwj.qqclient.utils.Utility;

import javax.sound.midi.Soundbank;

/**
 * @author rwj
 * @create 2022-06-28 8:46
 */
public class QQView {
    private boolean loop = true;
    private String key = "";
    private UserClientService userClientService = new UserClientService();
    private MessageClientService messageClientService = new MessageClientService();
    private FileClientService fileClientService = new FileClientService();
    public static void main(String[] args) {
        new QQView().mainMenu();
    }
    private void mainMenu(){
        while (loop){
            System.out.println("===========欢迎登录网络通信系统===========");
            System.out.println("\t\t 1 登录系统");
            System.out.println("\t\t 2 退出系统");
            System.out.print("请输入你的选择: ");
            switch (Utility.readString(1)){
                case "1":
                    System.out.println("请输入用户号");
                    String userId = Utility.readString(50);
                    System.out.println("请输入密码");
                    String pwd = Utility.readString(50);
                    if(userClientService.checkUser(userId,pwd)){
                        System.out.println("===========欢迎 (用户 " + userId + " 登录成功) ===========");
                        while (loop){
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.out.println("\n=========网络通信系统二级菜单(用户 " + userId + " )=======");
                            System.out.println("\t\t 1 显示在线用户列表");
                            System.out.println("\t\t 2 群发消息");
                            System.out.println("\t\t 3 私聊消息");
                            System.out.println("\t\t 4 发送文件");
                            System.out.println("\t\t 5 退出系统");
                            System.out.print("请输入你的选择: ");
                            switch (Utility.readString(1)){
                                case "1":
                                    userClientService.onlineFriendList();
                                    break;
                                case "2":
                                    System.out.println("请输入想对大家说的话:");
                                    String s = Utility.readString(100);
                                    messageClientService.sendMessageToAll(userId,s);
                                    break;
                                case "3":
                                    System.out.println("请输入想聊天的用户(在线):");
                                    String getterId = Utility.readString(50);
                                    System.out.println("请输入想说的话:");
                                    String content = Utility.readString(50);
                                    messageClientService.sendMessageToOne(userId,getterId,content);
                                    break;
                                case "4":
                                    System.out.println("请输入你想把文件发送给的用户(在线用户):");
                                    getterId = Utility.readString(50);
                                    System.out.println("请输入发送文件的路径(形式 d:\\xx.jpg)");
                                    String src = Utility.readString(100);
                                    System.out.print("请输入把文件发送到对应的路径(形式 d:\\yy.jpg)");
                                    String dest = Utility.readString(100);
                                    fileClientService.sendFileToOne(src,dest,userId,getterId);
                                    break;
                                case "5":
                                    loop = false;
                                    break;
                                default:
                                    System.out.println("请重新输入");
                            }
                        }
                    }
                    break;
                case "2":
                    loop = false;
                    break;
                default:
                    System.out.println("请重新输入");
            }
        }
    }
}
