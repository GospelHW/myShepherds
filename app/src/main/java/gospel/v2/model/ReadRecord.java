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

public class ReadRecord implements Serializable {
    private int id;
    private String bookid;
    private String bookname;
    private String userid;
    private String username;
    private String readtime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookid() {
        return bookid;
    }

    public void setBookid(String bookid) {
        this.bookid = bookid;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getReadtime() {
        return readtime;
    }

    public void setReadtime(String readtime) {
        this.readtime = readtime;
    }

    @Override
    public String toString() {
        return "ReadRecord{" +
                "id=" + id +
                ", bookid='" + bookid + '\'' +
                ", bookname='" + bookname + '\'' +
                ", userid='" + userid + '\'' +
                ", username='" + username + '\'' +
                ", readtime='" + readtime + '\'' +
                '}';
    }
}
