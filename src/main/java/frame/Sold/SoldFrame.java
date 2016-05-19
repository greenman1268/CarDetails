package frame.Sold;

import logic.Item;
import logic.ManagementSystem;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

/**
 * Created on 19.05.2016
 */

public class SoldFrame extends JFrame implements ActionListener, ListSelectionListener, ChangeListener {
    private ManagementSystem ms = null;
    private JTable itemList;
    private static final String DELETE = "delete";
    private ArrayList<Item> vector;
    private Vector<Item> selected = new Vector<>();

    public SoldFrame()throws Exception{
        ms = ManagementSystem.getInstance();

        // Создаем верхнюю панель
        JPanel top = new JPanel();
        // Устанавливаем для нее layout
        top.setLayout(null);
        top.setPreferredSize(new Dimension(500,30));

        //кнопка печать
        JButton btnPrint = new JButton("Удалить");
        btnPrint.setName(DELETE);
        btnPrint.setBounds(5,5,90,20);
        btnPrint.addActionListener(this);
        top.add(btnPrint);
        // Создаем нижнюю панель и задаем ей layout
        JPanel bot = new JPanel();
        bot.setLayout(new BorderLayout());

        // Создаем правую панель для вывода списка деталей
        JPanel right = new JPanel();
        // Задаем layout и задаем "бордюр" вокруг панели
        right.setLayout(new BorderLayout());
        right.setBorder(new BevelBorder(BevelBorder.LOWERED));
        // Создаем надпись
        right.add(new JLabel("Детали:"), BorderLayout.NORTH);

        itemList = new JTable(1, 6);
        right.add(new JScrollPane(itemList), BorderLayout.CENTER);
        bot.add(right, BorderLayout.CENTER);
        bot.add(top, BorderLayout.NORTH);
        // Вставляем  нижнюю панель в форму
        getContentPane().add(bot, BorderLayout.CENTER);
        vector =(ArrayList<Item>) ms.getAllItems();
        TableSearchRenderer tsr = new TableSearchRenderer();
        itemList.setDefaultRenderer(Object.class, tsr);

        JTableHeader header = itemList.getTableHeader();
        header.setDefaultRenderer(new MyTableHeaderRenderer(header.getDefaultRenderer()));

        // Задаем границы формы
        setBounds(200, 100, 1000, 500);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof Component) {
            Component c = (Component) e.getSource();
            if (c.getName().equals(DELETE)) {
                deleteItems();
            }}
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        reloadItems();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            reloadItems();
        }
    }

    // метод для обновления списка деталей для определенной группы
    public void reloadItems() {
        // Создаем анонимный класс для потока
        Thread t = new Thread(){// SwingUtilities.invokeLater(new Runnable() {// Thread t = new Thread() {
            // Переопределяем в нем метод run

            public void run() {
                if (itemList != null) {
                    // Получаем выделенную группу

                    try {
                        // Получаем список деталей
                        Collection<Item> s = ms.getSold();//ms.getItemsFromGroup(g);

                        final Vector<Item> v = new Vector(s);
                        if(selected!=null){
                            for (int i = 0; i < v.size(); i++) {
                                for (int j = 0; j < selected.size(); j++) {
                                    if(v.get(i).getItemName().equals(selected.get(j).getItemName())&& v.get(i).getGroupId()==selected.get(j).getGroupId())
                                        v.get(i).setPrint(selected.get(j).getPrint());
                                }
                            }
                        }
                        // И устанавливаем модель для таблицы с новыми данными
                        itemList.setModel(new ItemTableSoldModel(v));
                        itemList.getModel().addTableModelListener(new TableModelListener() {
                            @Override
                            public void tableChanged(TableModelEvent tableModelEvent) {
                                int row = tableModelEvent.getFirstRow();
                                int column = tableModelEvent.getColumn();
                                if(column == 5){
                                    TableModel model = (TableModel)tableModelEvent.getSource();
                                    Boolean checked = (Boolean)model.getValueAt(row,column);
                                    Item item = v.get(row);
                                    item.setPrint(checked);
                                    selected.add(item);
                                }
                            }
                        });
                        vector = (ArrayList<Item>) s;
                        RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(itemList.getModel());
                        itemList.setRowSorter(sorter);

                    } catch (SQLException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(SoldFrame.this, e.getMessage());
                    }}
            }

            // Окончание нашего метода run
        };
        // Окончание определения анонимного класса
        // И теперь мы запускаем наш поток
        t.start();
    }

    public void deleteItems(){
        Thread t = new Thread(){

            public void run(){
                if(selected.size()!=0){
                for (int i = 0; i < selected.size(); i++) {
                   if(selected.get(i).getPrint()) try {
                       ms.deleteSold(selected.get(i));
                   } catch (SQLException e) {
                       e.printStackTrace();
                   }
                }}
                else {JOptionPane.showMessageDialog(SoldFrame.this,
                        "Необходимо отметить деталь в списке");
                return;}

            }
        };
        t.start();
        reloadItems();
    }

    private class TableSearchRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setBackground(null);
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if(vector.get(row).getIn_stock() == 0)component.setBackground(Color.RED);
            else if(vector.get(row).getIn_stock() > 0 && vector.get(row).getIn_stock() < 3)component.setBackground(Color.YELLOW);
            else component.setBackground(table.getBackground());
            if(table.isCellSelected(row,column)){component.setBackground(Color.GRAY);}

            return component;
        }}

    private static class MyTableHeaderRenderer implements TableCellRenderer {
        private static final Font labelFont = new Font("Arial", Font.BOLD, 11);

        private TableCellRenderer delegate;

        public MyTableHeaderRenderer(TableCellRenderer delegate) {
            this.delegate = delegate;
        }


        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            Component c = delegate.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if(c instanceof JLabel) {
                JLabel label = (JLabel) c;
                label.setFont(labelFont);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setBorder(BorderFactory.createEtchedBorder());

            }
            return c;
        }
    }
}
