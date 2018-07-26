/*
 * copyright (c)2018-8-15
 * DXC technology
 */

package gospel.v2.model;

import java.io.Serializable;

/**
 * Created by gospel on 2017/8/18.
 * About User infomation
 */

public class Reader implements Serializable {
    private int id;
    private String username;
    private String gender;
    private String sYear;
    private String phone;
    private String realname;
    private String area;
    private String email;
    private String location;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getsYear() {
        return sYear;
    }

    public void setsYear(String sYear) {
        this.sYear = sYear;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Reader{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", gender='" + gender + '\'' +
                ", sYear='" + sYear + '\'' +
                ", phone='" + phone + '\'' +
                ", realname='" + realname + '\'' +
                ", area='" + area + '\'' +
                '}';
    }
}
