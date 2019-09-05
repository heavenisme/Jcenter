package com.heaven.base.ui.view.calendar;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * FileName: com.heaven.base.ui.view.calendar.FestivalDay.java
 * author: Heaven
 * email: heavenisme@aliyun.com
 * date: 2019-05-09 12:36
 *
 * @author heaven
 * @version V1.0 TODO <描述当前版本功能>
 */
@Entity(tableName = "festival",primaryKeys = {"year", "month","date"})
public class FestivalDay implements Serializable {

    private static final long serialVersionUID = -6104912748313205415L;
    /**
     * date : 2018-12-25
     * festival : 圣诞
     * holiday :
     */
    public int year;
    public int month;
    @NonNull
    public String date;
    public String festival;
    public String holiday;
}
