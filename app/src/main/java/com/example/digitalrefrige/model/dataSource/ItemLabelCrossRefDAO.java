package com.example.digitalrefrige.model.dataSource;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.digitalrefrige.model.dataHolder.Item;
import com.example.digitalrefrige.model.dataHolder.ItemLabelCrossRef;
import com.example.digitalrefrige.model.dataHolder.Label;
import com.example.digitalrefrige.model.dataQuery.ItemWithLabels;
import com.example.digitalrefrige.model.dataQuery.LabelWithItems;

import java.util.List;

@Dao
public interface ItemLabelCrossRefDAO {

    @Transaction
    @Query("SELECT * FROM label_table")
    LiveData<List<LabelWithItems>> getLabelOfItems();

    @Transaction
    @Query("SELECT * FROM item_table")
    LiveData<List<ItemWithLabels>> getItemOfLabels();

    @Query("SELECT * FROM label_table WHERE label_table.labelId IN (SELECT itemlabelcrossref.labelId FROM itemlabelcrossref WHERE itemlabelcrossref.itemId=:itemId)")
    LiveData<List<Label>> getLabelsByItem(long itemId);

    @Insert
    long insertItemLabelCrossRef(ItemLabelCrossRef itemLabelCrossRef);

    @Update
    void updateItemLabelCrossRef(ItemLabelCrossRef itemLabelCrossRef);

    @Delete
    void deleteItemLabelCrossRef(ItemLabelCrossRef itemLabelCrossRef);
}
