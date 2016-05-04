package frame;

import logic.Item;
import logic.ManagementSystem;

import javax.swing.table.AbstractTableModel;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Vector;

/**
 * Created on 30.04.2016
 */
public class ItemTableSearchModel extends AbstractTableModel {

    private Vector items;
    private ManagementSystem ms;

    public ItemTableSearchModel(Vector itemsFromSF){
        this.items = itemsFromSF;
        try {
            ms = ManagementSystem.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Количество строк равно числу записей
    public int getRowCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    public int getColumnCount() {
        return 5;
    }

    // Вернем наименование колонки
    public String getColumnName(int column) {
        String[] colNames = {"Номер", "Группа", "Дата последнего изменения", "В наличии", "Продано"};
        return colNames[column];
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (items != null) {
            // Получаем из вектора детали
            Item item = (Item) items.get(rowIndex);
            // В зависимости от колонки возвращаем имя, дата и т.д.

            switch (columnIndex) {
                case 0:
                    return item.getItemName();
                case 1:
                    try {
                        return ms.getGroupById(item.getGroupId()).getGroupName();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                case 2:
                    return DateFormat.getDateInstance(DateFormat.SHORT).format(
                            item.getChangeDate());
                case 3:
                    return item.getIn_stock();
                case 4:
                    return item.getSold();
            }
        }
        return null;
    }

    // Добавим метод, который возвращает деталь по номеру строки
    // Это нам пригодится чуть позже
    public Item getItem(int rowIndex) {
        if (items != null) {
            if (rowIndex < items.size() && rowIndex >= 0) {
                return (Item) items.get(rowIndex);
            }
        }
        return null;
    }
}
