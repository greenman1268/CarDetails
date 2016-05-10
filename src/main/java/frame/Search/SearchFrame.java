package frame.Search;

import frame.Main.GroupDialog;
import frame.Main.ItemDialog;
import frame.Main.MainFrame;
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
 * Created on 29.04.2016
 */
public class SearchFrame extends JFrame implements ActionListener, ListSelectionListener, ChangeListener  {

    // Введем сразу имена для кнопок - потом будем их использовать в обработчиках
    private static final String MOVE_GR = "moveGroup";
    private static final String UPDATE_IT = "updateItem";
    private static final String DELETE_IT = "deleteItem";
    private static final String PRINT = "print";

    private MainFrame mainFrame;
    private ManagementSystem ms = null;
    private JTable itemList;
   // private JList grpList;
    private SearchDilog searchDilog;
    private boolean boolName;
    private boolean boolCount;
    private boolean boolDate;
    private boolean boolAll;

    private ArrayList<Item> vector;
    private Vector<Item> selected = new Vector<>();

    public SearchFrame(boolean name, boolean count, boolean date, boolean all, MainFrame mf, SearchDilog sd) throws Exception{

        // Получаем коннект к базе и создаем объект ManagementSystem
        ms = ManagementSystem.getInstance();
        this.mainFrame = mf;
        this.searchDilog = sd;
        this.boolName = name;
        this.boolCount = count;
        this.boolDate = date;
        this.boolAll = all;

        // Создаем верхнюю панель
        JPanel top = new JPanel();
        // Устанавливаем для нее layout
        top.setLayout(null);
        top.setPreferredSize(new Dimension(500,30));

        //кнопка печать
        JButton btnPrint = new JButton("Печать");
        btnPrint.setName(PRINT);
        btnPrint.setBounds(5,5,90,20);
        btnPrint.addActionListener(this);
        top.add(btnPrint);

        //указать курс валют
        JButton lb = new JButton("Курс");
        lb.setBounds(100,5,70,20);
        top.add(lb);

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
        // Создаем таблицу и вставляем ее в скроллируемую
        // панель, которую в свою очередь уже кладем на панель right
        // Наша таблица пока ничего не умеет - просто положим ее как заготовку
        // Сделаем в ней 4 колонки - Номер, Дата последнего изменения, Количество в наличии, Количество проданых

        itemList = new JTable(1, 6);
        right.add(new JScrollPane(itemList), BorderLayout.CENTER);

        // Создаем кнопки для деталей
        JButton btnUpdSt = new JButton("Исправить");
        btnUpdSt.setName(UPDATE_IT);
        btnUpdSt.addActionListener(this);
        JButton btnDelSt = new JButton("Удалить");
        btnDelSt.setName(DELETE_IT);
        btnDelSt.addActionListener(this);
        JButton btnMovGr = new JButton("Переместить");
        btnMovGr.setName(MOVE_GR);
        btnMovGr.addActionListener(this);
        // Создаем панель, на которую положим наши кнопки и кладем ее вниз
        JPanel pnlBtnSt = new JPanel();
        pnlBtnSt.setLayout(new GridLayout(1, 3));
        pnlBtnSt.add(btnUpdSt);
        pnlBtnSt.add(btnDelSt);
        pnlBtnSt.add(btnMovGr);
        right.add(pnlBtnSt, BorderLayout.SOUTH);

        // Вставляем панели со списками групп и деталей в нижнюю панель
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
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof Component) {
            Component c = (Component) e.getSource();
            if (c.getName().equals(MOVE_GR)) {
                moveTOGroup();
            }
            if (c.getName().equals(UPDATE_IT)) {
                updateItem();
            }
            if (c.getName().equals(DELETE_IT)) {
                deleteItem();
            }
        }
    }

    public void stateChanged(ChangeEvent e) {
        reloadItems();
    }

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

                        Collection<Item> s = ms.getAllItems();//ms.getItemsFromGroup(g);

                        if(boolName){
                         s = ms.searchItemsByName(searchDilog.getName());
                        }
                        else if (boolCount){
                         s = ms.searchItemsByCount(searchDilog.getFrom(),searchDilog.getTO());
                        }
                        else if (boolDate){
                         s = ms.searchItemsByDate(searchDilog.getDateFrom(),searchDilog.getDateTO());
                        }
                        else if (boolAll){
                         s = ms.getAllItems();
                        }

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
                        itemList.setModel(new ItemTableSearchModel(new Vector<Item>(s)));
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
                        JOptionPane.showMessageDialog(SearchFrame.this, e.getMessage());
                    }}//}}
            }

            // Окончание нашего метода run
        };
        // Окончание определения анонимного класса
        // И теперь мы запускаем наш поток
         t.start();
    }

    private void moveTOGroup(){
        Thread t = new Thread() {

            public void run() {
                if(itemList == null)return;
                // Если группа не выделена - выходим. Хотя это крайне маловероятно
                if (itemList.getSelectedRows().length == 0) {
                    JOptionPane.showMessageDialog(SearchFrame.this,
                            "Необходимо выделить деталь в списке");
                    return;
                }
                if(itemList.getSelectedRows().length > 0){
                    GroupDialog gd = null;
                    try {

                        gd = new GroupDialog(ms.getGroups());
                        gd.setAlwaysOnTop(true);
                        // Задаем ему режим модальности - нельзя ничего кроме него выделить
                        gd.setModal(true);
                        // Показываем диалог
                        gd.setVisible(true);

                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(SearchFrame.this, e.getMessage());
                        e.printStackTrace();
                    }
                    if (gd.getResult()) {

                        ItemTableSearchModel itm = (ItemTableSearchModel) itemList.getModel();
                        Item item;

                        for (int i = 0; i < itemList.getSelectedRows().length; i++) {
                            try {
                                item = itm.getItem(itemList.getSelectedRows()[i]);
                                ms.moveItemsToGroup(item, gd.getGroup());
                            } catch (SQLException e) {
                                JOptionPane.showMessageDialog(SearchFrame.this, e.getMessage());
                                e.printStackTrace();
                            }
                        }}
                    reloadItems();
                    mainFrame.reloadItems();
                }
            }

        };
        t.start();
    }

    private void updateItem(){
        Thread t = new Thread() {

            public void run() {
                if (itemList != null) {
                    ItemTableSearchModel stm = (ItemTableSearchModel) itemList.getModel();
                    // Проверяем - выделен ли хоть какая-нибудь деталь
                    if (itemList.getSelectedRow() >= 0) {
                        // Вот где нам пригодился метод getItem(int rowIndex)
                        Item s = stm.getItem(itemList.getSelectedRow());
                        try {
                            ItemDialog sd = new ItemDialog(ms.getGroups());
                            sd.setAlwaysOnTop(true);
                            sd.setItem(s);
                            sd.setModal(true);
                            sd.setVisible(true);
                            if (sd.getResult()) {
                                //searchDilog.setName(sd.getItemName());
                                Item us = sd.getItem();
                                ms.updateItem(us);
                                reloadItems();
                                mainFrame.reloadItems();
                            }
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(SearchFrame.this, e.getMessage());
                            e.printStackTrace();
                        }
                    } // Если деталь не выделена - сообщаем пользователю, что это необходимо
                    else {
                        JOptionPane.showMessageDialog(SearchFrame.this,
                                "Необходимо выделить деталь в списке");
                    }
                }
            }
        };
        t.start();
    }

    private void deleteItem(){
        Thread t = new Thread() {

            public void run() {
                if (itemList != null) {
                    ItemTableSearchModel itmstm = (ItemTableSearchModel) itemList.getModel();
                    // Проверяем - выделена ли хоть какая-нибудь деталь
                    if (itemList.getSelectedRows().length > 0) {
                        if (JOptionPane.showConfirmDialog(SearchFrame.this,
                                "Вы хотите удалить деталь?", "Удаление детали",
                                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                            // Вот где нам пригодился метод getItem(int rowIndex)
                            Item s;
                            for (int i = 0; i < itemList.getSelectedRows().length; i++) {
                                s = itmstm.getItem(itemList.getSelectedRows()[i]);
                                try {
                                    ms.deleteItem(s);

                                } catch (SQLException e) {
                                    JOptionPane.showMessageDialog(SearchFrame.this, e.getMessage());
                                    e.printStackTrace();
                                }
                            }
                            reloadItems();
                            mainFrame.reloadItems();
                        }
                    } // Если деталь не выделена - сообщаем пользователю, что это необходимо
                    else {
                        JOptionPane.showMessageDialog(SearchFrame.this, "Необходимо выделить деталь в списке");
                    }
                }
            }
        };
        t.start();
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
