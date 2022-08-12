package org.rwj.dao;

import org.rwj.pojo.Student;

import java.util.ArrayList;

/**
 * @author rwj
 * @create 2022-06-30 17:27
 */
public class StudentDao {
    public static ArrayList<Student> slist=new ArrayList<>();
    public ArrayList<Student> select(){
        return slist;
    }
    public void insert(Student info){
        slist.add(info);
    }
    public void update(Student info){
        for(int i =0;i < slist.size();i++){
            if(slist.get(i).id.equals(info.id)){
                slist.set(i,info);
            }
        }
    }
    public void delete(int id){
        for(int i=0;i < slist.size();i++){
            if(slist.get(i).id.equals(id)){
                slist.remove(i);
            }
        }
    }
}
