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

public class Books implements Serializable {
    private int id;
    private String bookname;
    private String booktime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getBooktime() {
        return booktime;
    }

    public void setBooktime(String booktime) {
        this.booktime = booktime;
    }

    @Override
    public String toString() {
        return "Books{" +
                "id=" + id +
                ", bookname='" + bookname + '\'' +
                ", booktime='" + booktime + '\'' +
                '}';
    }
}

