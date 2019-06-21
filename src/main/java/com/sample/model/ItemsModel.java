package com.sample.model;

import java.util.List;

import javax.swing.table.AbstractTableModel;

public class ItemsModel extends AbstractTableModel {

    private static final String[] columnNames = {"Name", "Gender", "Price"};
    private List<Item> items;

    public ItemsModel(List<Item> items) {
        this.items = items;
    }

    public void add(Item item) {
        items.add(item);
        this.fireTableRowsInserted(items.size() - 1, items.size() - 1);
    }

    public void remove(Item item) {
        int index = items.indexOf(item);
        items.remove(index);
        this.fireTableRowsDeleted(index, index);
    }

    public void addAll(List<Item> items) {
        this.items.addAll(items);
        this.fireTableDataChanged();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return items.size();
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return items.get(rowIndex).getName();
            case 1:
                return items.get(rowIndex).getGender().toString();
            case 2:
                return items.get(rowIndex).getPrice() + "$";
            case -1:
                return items.get(rowIndex);
        }
        return null;
    }
}