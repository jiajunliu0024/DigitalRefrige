package com.example.digitalrefrige.model.dataHolder;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;
import java.util.Date;

/**
 * Entity class for local database, id is PK and autogenerated by Room
 */
@Entity(tableName = "item_table")
public class Item {
    private int quantity;
    private String name;
    private String description;
    private Date createDate;
    private Date expireDate;
    private String imgUrl;


    @PrimaryKey(autoGenerate = true)
    private long itemId;

    public Item(String name, String description, Date createDate, String imgUrl) {
        this.name = name;
        this.description = description;
        this.createDate = createDate;
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setExpireDate(Date expireDate){ this.expireDate = expireDate; }

    public Date getExpireDate(){return this.expireDate;}

    public void setImgUrl(String imgUrl){ this.imgUrl = imgUrl;}

    public String getImgUrl(){return this.imgUrl; }

}
