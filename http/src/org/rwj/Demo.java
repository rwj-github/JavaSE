package org.rwj;

import com.google.gson.Gson;
import org.rwj.dao.StudentDao;
import org.rwj.pojo.Student;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.ArrayList;

/**
 * @author rwj
 * @create 2022-06-30 17:35
 */
public class Demo {
    private static Gson gson = new Gson();
    public static void main(String[] args) throws Exception{
        ServerSocket serverSocket = new ServerSocket(9000);
        System.out.println("服务器已启动");
        StudentDao.slist = load(ArrayList.class);
        if(StudentDao.slist == null){
            StudentDao.slist = new ArrayList<>();
        }
        while (true){
            Socket socket = serverSocket.accept();
            BufferedReader reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String str1 = reader.readLine();
            str1 = URLDecoder.decode(str1);
            String urlPath = str1.split(" ")[1];
            urlPath = urlPath.substring(0,urlPath.indexOf("?")==-1?urlPath.length():urlPath.indexOf("?"));
            switch (urlPath){
                case "/select":
                    select(socket);
                    break;
                case "/insert":
                    insert(socket,str1);
                    saveObject(StudentDao.slist);
                    break;
                case "/delete":
                    delete(socket,str1);
                    saveObject(StudentDao.slist);
                    break;
                case "/update":
                    update(socket,str1);
                    saveObject(StudentDao.slist);
                    break;
                default:

            }
        }
    }

    private static void update(Socket socket, String str1) throws Exception{
        System.out.println("修改数据");
        Student student = new Student();
        student.id = Integer.parseInt(getValueByKey(str1,"id"));
        student.name=getValueByKey(str1,"name");
        student.pwd=getValueByKey(str1,"pwd");
        student.sex=getValueByKey(str1,"sex");
        new StudentDao().update(student);
        OutputStream outputStream = socket.getOutputStream();
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(outputStream));
        printWriter.println("HTTP/1.1 200 OK");
        printWriter.println("Content-Type: text/html; charset=UTF-8");
        printWriter.println("Access-Control-Allow-Origin:*");
        printWriter.println("Access-Control-Allow-Credentials:true");
        printWriter.println("Access-Control-Allow-Headers:*");
        printWriter.println("Access-Control-Allow-Methods:PUT,POST,GET,DELETE,OPTIONS");
        printWriter.println();
        printWriter.println("<h2>修改成功</h2>");
        printWriter.flush();
        socket.shutdownOutput();
    }

    private static void delete(Socket socket, String str1) throws Exception{
        System.out.println("删除数据");
        int id = Integer.parseInt(getValueByKey(str1,"id"));
        new StudentDao().delete(id);
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        printWriter.println("HTTP/1.1 200 OK");
        printWriter.println("Content-Type: text/html; charset=UTF-8");
        printWriter.println("Access-Control-Allow-Origin:*");
        printWriter.println("Access-Control-Allow-Credentials:true");
        printWriter.println("Access-Control-Allow-Headers:*");
        printWriter.println("Access-Control-Allow-Methods:PUT,POST,GET,DELETE,OPTIONS");
        printWriter.println();
        printWriter.println("<h2>删除成功</h2>");
        printWriter.flush();
        socket.shutdownOutput();
    }

    private static void insert(Socket socket, String str1) throws Exception{
        System.out.println("添加数据");
        Student student = new Student();
        student.id = Integer.parseInt(getValueByKey(str1,"id"));
        student.name = getValueByKey(str1,"name");
        student.pwd = getValueByKey(str1,"pwd");
        student.sex = getValueByKey(str1,"sex");
        StudentDao.slist.add(student);
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        printWriter.println("HTTP/1.1 200 OK");
        printWriter.println("Content-Type: text/html; charset=UTF-8");
        printWriter.println("Access-Control-Allow-Origin:*");
        printWriter.println("Access-Control-Allow-Credentials:true");
        printWriter.println("Access-Control-Allow-Headers:*");
        printWriter.println("Access-Control-Allow-Methods:PUT,POST,GET,DELETE,OPTIONS");
        printWriter.println();
        printWriter.println("<h2>添加成功</h2>");
        printWriter.flush();
        socket.shutdownOutput();
    }

    private static void select(Socket socket) throws Exception{
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        System.out.println("开始查询数据");
        printWriter.println("HTTP/1.1 200 OK");
        printWriter.println("Content-Type:text/html;charset=UTF-8");
        printWriter.println("Access-Control-Allow-Origin:*");
        printWriter.println("Access-Control-Allow-Credentials:true");
        printWriter.println("Access-Control-Allow-Headers:*");
        printWriter.println("Access-Control-Allow-Methods:PUT,POST,GET,DELETE,OPTIONS");
        printWriter.println();
        gson.toJson(StudentDao.slist);
        printWriter.println(gson.toJson(StudentDao.slist));
        printWriter.flush();
        socket.shutdownOutput();
    }
    public static String getValueByKey(String url,String key){
        int begin = url.indexOf(key+"=");
        int end = url.indexOf("&",begin);
        if(end < 0){
            end=url.indexOf(" ",begin);
        }
        return url.substring(begin+key.length()+1,end);
    }
    public static void saveObject(Object obj){
        try {
            OutputStream outputStream = new FileOutputStream(Demo.class.getResource("/slist.bin").getPath());
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.writeObject(obj);
            System.out.println("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static <T> T load(Class<T> clazz){
        try {
            ObjectInputStream ois = new ObjectInputStream(Demo.class.getClassLoader().getResourceAsStream("slist.bin"));
            System.out.println("加载成功");
            return (T) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
