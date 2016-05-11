package frame.Print.report;

import logic.Item;
import logic.ManagementSystem;

import javax.swing.table.AbstractTableModel;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Vector;

/**
 * Created on 11.05.2016
 */
public class PrintTableModel extends AbstractTableModel {

    private Vector items;
    private ManagementSystem ms;

    public PrintTableModel (Vector items){
        this.items = items;
        try {
            ms = ManagementSystem.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getRowCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    public boolean isCellEditable(int row, int col){
        return true;
    }


    @Override
    public int getColumnCount() {
        return 6;
    }

    public void setValueAt(Object value, int row, int col) {
        if(items != null){
            Item item = (Item) items.get(row);

            switch (col){
                case 0:item.setItemName((String)value);break;
                case 1:item.setGroupId((Integer)value);break;
                case 2:item.setChangeDate((java.util.Date)value);break;
                //case 3:item.setPrice((BigDecimal)value);break;
                case 3:item.setIn_stock((Integer)value);break;
                case 4:item.setSold((Integer)value);break;
                case 5:item.setPrint((Boolean)value);break;
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
                return String.class;
            case 2:
                return DateFormat.class;
            /*case 3:
                return BigDecimal.class;*/
            case 3:
                return Integer.class;
            case 4:
                return Integer.class;
            case 5:
                return Boolean.class;
            default:
                return null;
        }
    }

    // Вернем наименование колонки
    public String getColumnName(int column) {
        String[] colNames = {"Номер", "Группа", "Дата последнего изменения", "В наличии", "Продано", "Печать"};
        return colNames[column];
    }

    @Override
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
                /*case 3:
                    return item.getPrice();*/
                case 3:
                    return item.getIn_stock();
                case 4:
                    return item.getSold();
                case 5:
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
}
