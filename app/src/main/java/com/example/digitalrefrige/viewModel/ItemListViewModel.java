package com.example.digitalrefrige.viewModel;

import android.util.Log;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.digitalrefrige.model.ItemLabelCrossRefRepository;
import com.example.digitalrefrige.model.ItemRepository;
import com.example.digitalrefrige.model.LabelRepository;
import com.example.digitalrefrige.model.dataHolder.Item;
import com.example.digitalrefrige.model.dataHolder.Label;
import com.example.digitalrefrige.model.dataQuery.ItemWithLabels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ItemListViewModel extends ViewModel implements Filterable {

    private ItemRepository itemRepository;
    private LabelRepository labelRepository;
    private ItemLabelCrossRefRepository itemLabelCrossRefRepository;

    private LiveData<List<Item>> allItems;
    private LiveData<List<Label>> allLabels;
    private LiveData<List<ItemWithLabels>> allItemsWithLabels;

    private List<Label> curSelectedLabel;
    private MutableLiveData<List<Item>> filteredItems;

    private Filter itemFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Item> filteredList = new ArrayList<>();
            try {
                Log.d("MyLog", "\nStart filtering with config:\npattern:" + charSequence + "\n" + "labels:" + curSelectedLabel.toString());
                if (allItems.getValue() == null) return null;
                // first filter item name
                if (charSequence == null || charSequence.length() == 0) {
                    // match all items
                    filteredList.addAll(allItems.getValue());
                } else {
                    // filter with given pattern
                    String filterPattern = charSequence.toString().toLowerCase().trim();
                    for (Item item : allItems.getValue()) {
                        if (item.getName().toLowerCase().contains(filterPattern)
                                || item.getDescription().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                }
                // then filter label
                List<Item> needRemove = new ArrayList<>();
                List<ItemWithLabels> itemWithLabels = allItemsWithLabels.getValue();
                Map<Item, List<Label>> tempMap = new HashMap<>();
                for (ItemWithLabels item : itemWithLabels) {
                    tempMap.put(item.item, item.labels);
                }
                outer:
                for (Item item : filteredList) {
                    List<Label> labelsOfCurItem = tempMap.get(item);
                    for (Label label : labelsOfCurItem) {
                        if (curSelectedLabel.contains(label)) {
                            continue outer;
                        }
                    }
                    needRemove.add(item);
                }
                filteredList.removeAll(needRemove);
            } catch (NullPointerException e) {
                Log.d("MyLog", "filtering failed");
            }


            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            if (filterResults == null) return;
            updateFilteredItemList((List) filterResults.values);
        }
    };

    @Inject
    public ItemListViewModel(ItemRepository itemRepo, LabelRepository labelRepo, ItemLabelCrossRefRepository itemLabelCrossRefRepo) {
        itemRepository = itemRepo;
        labelRepository = labelRepo;
        itemLabelCrossRefRepository = itemLabelCrossRefRepo;
        allItems = itemRepository.getAllItems();
        allItemsWithLabels = itemLabelCrossRefRepo.getAllItemList();
        allLabels = labelRepo.getAllLabels();
        curSelectedLabel = new ArrayList<>();
        filteredItems = new MutableLiveData<>(new ArrayList<>());
    }

    public void updateFilteredItemList(List<Item> list) {
        if (list == null) return;
        Log.d("MyLog", "filteredItems change to size " + list.size());
        List<Item> newFilteredItems = new ArrayList<>(list);
        filteredItems.setValue(newFilteredItems);
    }


    public LiveData<List<Item>> getFilteredItems() {
        return filteredItems;
    }


    public LiveData<List<Item>> getAllItems() {
        return allItems;
    }

    public LiveData<List<Label>> getAllLabels() {
        return allLabels;
    }


    public LiveData<List<ItemWithLabels>> getAllItemsWithLabels() {
        return allItemsWithLabels;
    }

    public void setCurSelectedLabel(List<Label> curSelectedLabel) {
        this.curSelectedLabel = curSelectedLabel;
    }

    public List<Label> getCurSelectedLabel() {
        return curSelectedLabel;
    }

    public long insertItem(Item item) {
        return itemRepository.insertItem(item);
    }

    public void updateItem(Item item) {
        itemRepository.updateItem(item);
    }

    public void deleteItem(Item item) {
        itemRepository.deleteItem(item);
    }

    @Override
    public Filter getFilter() {
        return itemFilter;
    }
}
