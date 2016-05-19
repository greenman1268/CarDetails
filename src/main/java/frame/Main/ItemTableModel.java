package frame.Main;

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
        return 5;
    }

    // Вернем наименование колонки
    public String getColumnName(int column) {
        String[] colNames = {"Название", "Дата последнего изменения", "В наличии", "Продано", "Печать"};
        return colNames[column];
    }

    @Override
    public boolean isCellEditable(int row, int col){
        return true;
    }

    public void setValueAt(Object value, int row, int col) {
        if(items != null){
            Item item = (Item) items.get(row);

            switch (col){
                case 0:item.setItemName((String)value);break;
                case 1:item.setChangeDate((java.util.Date)value);break;
               // case 2:item.setPrice((BigDecimal)value);break;
                case 2:item.setIn_stock((Integer)value);break;
                case 3:item.setSold((Integer)value);break;
                case 4:item.setPrint((Boolean)value);break;
                default:
                    System.out.println("SOMETHING WRONG");
            }
        }
        fireTableCellUpdated(row, col);
    }
    @Override
    public Class getColumnClass(int column){
        switch (column){
            case 0:
                return String.class;
            case 1:
                return DateFormat.class;
            /*case 2:
                return BigDecimal.class;*/
            case 2:
                return Integer.class;
            case 3:
                return Integer.class;
            case 4:
                return Boolean.class;
            default:
                return null;
        }
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
                case 1:
                    return DateFormat.getDateInstance(DateFormat.SHORT).format(
                           item.getChangeDate());
                /*case 2:
                    return item.getPrice();*/

                case 2:
                    return item.getIn_stock();
                case 3:
                    return item.getSold();
                case 4:
                    return item.getPrint();
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

    public Item getItemByName(String nameItem){
        if (items != null){
            Item item;
            for (int i = 0; i < items.size(); i++) {
               item = (Item)items.get(i);
                if(item.getItemName().equals(nameItem)) return item;
            }
        }
        return null;
    }


}
