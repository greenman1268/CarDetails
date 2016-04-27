package frame;

/**
 * Created on 27.04.2016
 */

import logic.Item;

import javax.swing.table.AbstractTableModel;
import java.text.DateFormat;
import java.util.Vector;

public class ItemTableModel extends AbstractTableModel {
    // Сделаем хранилище для нашего списка деталей

    private Vector items;

    // Модель при создании получает список деталей
    public ItemTableModel(Vector items) {
        this.items = items;
    }

    // Количество строк равно числу записей
    public int getRowCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    // Количество столбцов - 4. Номер, Дата последнего изменения, Количество в наличии, Количество проданых
    public int getColumnCount() {
        return 4;
    }

    // Вернем наименование колонки
    public String getColumnName(int column) {
        String[] colNames = {"Номер", "Дата последнего изменения", "Количество в наличии", "Количество проданых"};
        return colNames[column];
    }

    // Возвращаем данные для определенной строки и столбца
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (items != null) {
            // Получаем из вектора детали
            Item item = (Item) items.get(rowIndex);
            // В зависимости от колонки возвращаем имя, дата и т.д.
            switch (columnIndex) {
                case 0:
                    return item.getItemName();
                case 1:return DateFormat.getDateInstance(DateFormat.SHORT).format(
                            item.getChangeDate());
                case 2:
                    return 0;
                case 3:
                    return 0;
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
