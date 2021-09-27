package com.example.digitalrefrige.model.dataSource;


import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.digitalrefrige.model.dataHolder.Label;

import java.util.List;

public interface LabelDAO {
    @Query("select * from label_table")
    LiveData<List<Label>> getAllLabels();

    @Insert
    void insertLabel(Label label);

    @Update
    void updateLabel(Label label);

    @Delete
    void deleteLabel(Label label);

    @Query("select * from label_table where id=:id")
    Label findLabelById(int id);
}
