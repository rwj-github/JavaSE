package org.rwj.pojo;

import java.io.Serializable;

/**
 * @author rwj
 * @create 2022-06-30 11:19
 */
public class Student implements Serializable {
    public Integer id;
    public String name;
    public String pwd;
    public String sex;

    public Student() {
        System.out.println("student被创建了");
    }

    public Student(Integer id, String name, String pwd, String sex) {
        this.id = id;
        this.name = name;
        this.pwd = pwd;
        this.sex = sex;

    }
    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }
}
