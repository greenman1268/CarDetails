package frame;

/**
 * Created on 27.04.2016
 */

import logic.Group;
import logic.Item;
import logic.ManagementSystem;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

public class MainFrame extends JFrame implements ActionListener, ListSelectionListener, ChangeListener {

    // Введем сразу имена для кнопок - потом будем их использовать в обработчиках
    private static final String MOVE_GR = "moveGroup";
    private static final String CLEAR_GR = "clearGroup";
    private static final String INSERT_IT = "insertItem";
    private static final String UPDATE_IT = "updateItem";
    private static final String DELETE_IT = "deleteItem";
    private static final String ALL_ITEMS = "allItems";
    private static final String SEARCH_IT = "searchItem";
    private static final String BY_NAME = "byName";
    private static final String BY_COUNT = "byCount";
    private static final String BY_DATE = "byDate";
    private ManagementSystem ms = null;
    private JList grpList;
    private JTable itemList;
    private JTextField search = new JTextField();
    private JCheckBox name = new JCheckBox("имя");
    private JCheckBox count = new JCheckBox("количество");
    private JCheckBox date = new JCheckBox("дата");
    private ArrayList<Item> vector;

    public MainFrame() throws Exception{

        // Создаем верхнюю панель, где будет поле для ввода года
        JPanel top = new JPanel();
        // Устанавливаем для нее layout
        top.setLayout(null);
        top.setPreferredSize(new Dimension(500,50));
        // Устанавливаем поиск
       // search.setBounds(5,10,100,20);
                //кнопка поиска
        JButton btnSrch = new JButton("Поиск");
        btnSrch.setName(SEARCH_IT);
        btnSrch.setBounds(105,9,70,20);
        btnSrch.addActionListener(this);


      /*  name.setName(BY_NAME);
        name.setBounds(1,30,50,15);
        name.addActionListener(this);

        count.setName(BY_COUNT);
        count.setBounds(50, 30, 75, 15);
        count.addActionListener(this);

        date.setName(BY_DATE);
        date.setBounds(125, 30, 75, 15);
        date.addActionListener(this);

        top.add(name);
        top.add(search);*/
        top.add(btnSrch);
       /* top.add(count);
        top.add(date);*/
      //  top.setPreferredSize(new Dimension(300,30));
        // Создаем нижнюю панель и задаем ей layout
        JPanel bot = new JPanel();
        bot.setLayout(new BorderLayout());

        // Создаем левую панель для вывода списка групп
        // Она у нас
        GroupPanel left = new GroupPanel();
        // Задаем layout и задаем "бордюр" вокруг панели
        left.setLayout(new BorderLayout());
        left.setBorder(new BevelBorder(BevelBorder.LOWERED));

        // Получаем коннект к базе и создаем объект ManagementSystem
        ms = ManagementSystem.getInstance();
        // Получаем список групп
        Vector<Group> gr = new Vector<Group>(ms.getGroups());
        // Создаем надпись
        left.add(new JLabel("Группы:"), BorderLayout.NORTH);
        // Создаем визуальный список и вставляем его в скроллируемую
        // панель, которую в свою очередь уже кладем на панель left
        grpList = new JList(gr);
        // Добавляем листенер
        grpList.addListSelectionListener(this);
        // Сразу выделяем первую группу
        grpList.setSelectedIndex(0);
        left.add(new JScrollPane(grpList), BorderLayout.CENTER);
        // Создаем кнопки для групп
        JButton btnMvGr = new JButton("Переместить");
        btnMvGr.setName(MOVE_GR);
        JButton btnClGr = new JButton("Очистить");
        btnClGr.setName(CLEAR_GR);
        // Добавляем листенер
        btnMvGr.addActionListener(this);
        btnClGr.addActionListener(this);
        // Создаем панель, на которую положим наши кнопки и кладем ее вниз
        JPanel pnlBtnGr = new JPanel();
        pnlBtnGr.setLayout(new GridLayout(1, 2));
        pnlBtnGr.add(btnMvGr);
        pnlBtnGr.add(btnClGr);
        left.add(pnlBtnGr, BorderLayout.SOUTH);
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
        //itemList = new JTable(1, 4).;
        itemList = new JTable(1, 4);

        //JScrollPane scrollPane = new JScrollPane(itemList);

        right.add(new JScrollPane(itemList), BorderLayout.CENTER);
        // Создаем кнопки для деталей
        JButton btnAddSt = new JButton("Добавить");
        btnAddSt.setName(INSERT_IT);
        btnAddSt.addActionListener(this);
        JButton btnUpdSt = new JButton("Исправить");
        btnUpdSt.setName(UPDATE_IT);
        btnUpdSt.addActionListener(this);
        JButton btnDelSt = new JButton("Удалить");
        btnDelSt.setName(DELETE_IT);
        btnDelSt.addActionListener(this);
        // Создаем панель, на которую положим наши кнопки и кладем ее вниз
        JPanel pnlBtnSt = new JPanel();
        pnlBtnSt.setLayout(new GridLayout(1, 3));
        pnlBtnSt.add(btnAddSt);
        pnlBtnSt.add(btnUpdSt);
        pnlBtnSt.add(btnDelSt);
        right.add(pnlBtnSt, BorderLayout.SOUTH);

        // Вставляем панели со списками групп и деталей в нижнюю панель
        bot.add(left, BorderLayout.WEST);
        bot.add(right, BorderLayout.CENTER);

        // Вставляем верхнюю и нижнюю панели в форму
        getContentPane().add(top, BorderLayout.NORTH);
        getContentPane().add(bot, BorderLayout.CENTER);

        TableSearchRenderer tsr = new TableSearchRenderer();
        itemList.setDefaultRenderer(Object.class, tsr);

        // Задаем границы формы
        setBounds(100, 100, 700, 500);
    }
    // Метод для обеспечения интерфейса ActionListener
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof Component) {
            Component c = (Component) e.getSource();
            if (c.getName().equals(MOVE_GR)) {
                moveTOGroup();
            }
            if (c.getName().equals(CLEAR_GR)) {
                clearGroup();
            }
            if (c.getName().equals(ALL_ITEMS)) {
                showAllItems();
            }
            if (c.getName().equals(INSERT_IT)) {
                insertItem();
            }
            if (c.getName().equals(UPDATE_IT)) {
                updateItem();
            }
            if (c.getName().equals(DELETE_IT)) {
                deleteItem();
            }
            if (c.getName().equals(SEARCH_IT)) {
                searchItem();
            }

        }
    }

    // Метод для обеспечения интерфейса ListSelectionListener
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            reloadItems();
        }
    }

    // Метод для обеспечения интерфейса ChangeListener
    public void stateChanged(ChangeEvent e) {
        reloadItems();
    }

    // метод для обновления списка деталей для определенной группы
    public void reloadItems() {
        // Создаем анонимный класс для потока
        SwingUtilities.invokeLater(new Runnable() {
            // Переопределяем в нем метод run

            public void run() {
                if (itemList != null) {
                    // Получаем выделенную группу
                    Group g = (Group) grpList.getSelectedValue();

                    try {
                        // Получаем список деталей
                        Collection<Item> s = ms.getItemsFromGroup(g);
                        // И устанавливаем модель для таблицы с новыми данными
                        itemList.setModel(new ItemTableModel(new Vector<Item>(s)));
                        vector =(ArrayList<Item>) ms.getItemsFromGroup(g);


                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(MainFrame.this, e.getMessage());
                    }
                }
            }
            // Окончание нашего метода run
        });
        // Окончание определения анонимного класса

        // И теперь мы запускаем наш поток
        /*t.start();*/
    }

    // метод для переноса группы
    private void moveTOGroup() {
        Thread t = new Thread() {

            public void run() {
                if(itemList == null)return;
                // Если группа не выделена - выходим. Хотя это крайне маловероятно
                if (itemList.getSelectedRows().length == 0) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Необходимо выделить деталь в списке");
                    return;
                }
                if(itemList.getSelectedRows().length > 0){
                    GroupDialog gd = null;
                    try {
                        gd = new GroupDialog(ms.getGroups());
                        // Задаем ему режим модальности - нельзя ничего кроме него выделить
                        gd.setModal(true);
                        // Показываем диалог
                        gd.setVisible(true);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    if (gd.getResult()) {

                    ItemTableModel itm = (ItemTableModel) itemList.getModel();
                    Item item = null;

                    for (int i = 0; i < itemList.getSelectedRows().length; i++) {
                        try {
                             item = itm.getItem(itemList.getSelectedRows()[i]);
                             ms.moveItemsToGroup(item, gd.getGroup());
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(MainFrame.this, e.getMessage());
                        }
                    }}
                    reloadItems();
            }
            }

        };
        t.start();
    }

    // метод для очистки группы
    private void clearGroup() {
        Thread t = new Thread() {

            public void run() {
                // Проверяем - выделена ли группа
                if (grpList.getSelectedValue() != null) {
                    if (JOptionPane.showConfirmDialog(MainFrame.this,
                            "Вы хотите удалить детали из группы?", "Удаление деталей",
                            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        // Получаем выделенную группу
                        Group g = (Group) grpList.getSelectedValue();
                        try {
                            // Удалили детали из группы
                            ms.removeItemsFromGroup(g);
                            // перегрузили список деталей
                            reloadItems();
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(MainFrame.this, e.getMessage());
                        }
                    }
                }
            }
        };
        t.start();
    }

    // метод для добавления детали
    private void insertItem() {
        Thread t = new Thread() {

            public void run() {
                try {
                    // Добавляем новую деталь - поэтому true
                    // Также заметим, что необходимо указать не просто this, а MainFrame.this
                    // Иначе класс не будет воспринят - он же другой - анонимный
                    Group g = (Group) grpList.getSelectedValue();
                    ItemDialog sd = new ItemDialog(ms.getGroups(), true, MainFrame.this);
                    sd.setModal(true);
                    sd.setVisible(true);
                    if (sd.getResult()) {
                        Item s = sd.getItem();
                        s.setGroupId(g.getGroup_id());
                        ms.insertItem(s);
                        reloadItems();
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(MainFrame.this, e.getMessage());
                }
            }
        };
        t.start();
    }

    // метод для редактирования детали
    private void updateItem() {
        Thread t = new Thread() {

            public void run() {
                if (itemList != null) {
                    ItemTableModel stm = (ItemTableModel) itemList.getModel();
                    // Проверяем - выделен ли хоть какая-нибудь деталь
                    if (itemList.getSelectedRow() >= 0) {
                        // Вот где нам пригодился метод getItem(int rowIndex)
                        Item s = stm.getItem(itemList.getSelectedRow());
                        try {
                            // Исправляем данные на деталь - поэтому false
                            // Также заметим, что необходимо указать не просто this, а MainFrame.this
                            // Иначе класс не будет воспринят - он же другой - анонимный
                            ItemDialog sd = new ItemDialog(ms.getGroups(), false, MainFrame.this);
                            sd.setItem(s);
                            sd.setModal(true);
                            sd.setVisible(true);
                            if (sd.getResult()) {
                                Item us = sd.getItem();
                                ms.updateItem(us);
                                reloadItems();
                            }
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(MainFrame.this, e.getMessage());
                            e.printStackTrace();
                        }
                    } // Если деталь не выделена - сообщаем пользователю, что это необходимо
                    else {
                        JOptionPane.showMessageDialog(MainFrame.this,
                                "Необходимо выделить деталь в списке");
                    }
                }
            }
        };
        t.start();
    }


    // метод для удаления детали
    private void deleteItem() {
        Thread t = new Thread() {

            public void run() {
                if (itemList != null) {
                    ItemTableModel itmstm = (ItemTableModel) itemList.getModel();
                    // Проверяем - выделена ли хоть какая-нибудь деталь
                    if (itemList.getSelectedRows().length > 0) {
                        if (JOptionPane.showConfirmDialog(MainFrame.this,
                                "Вы хотите удалить деталь?", "Удаление детали",
                                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                            // Вот где нам пригодился метод getItem(int rowIndex)
                            Item s;
                            for (int i = 0; i < itemList.getSelectedRows().length; i++) {
                                s = itmstm.getItem(itemList.getSelectedRows()[i]);
                                try {
                                    ms.deleteItem(s);

                                } catch (SQLException e) {
                                    JOptionPane.showMessageDialog(MainFrame.this, e.getMessage());
                                }
                            }
                            reloadItems();
                        }
                    } // Если деталь не выделена - сообщаем пользователю, что это необходимо
                    else {
                        JOptionPane.showMessageDialog(MainFrame.this, "Необходимо выделить деталь в списке");
                    }
                }
            }
        };
        t.start();
    }

    // метод для показа всех деталей
    private void showAllItems() {
        JOptionPane.showMessageDialog(this, "showAllItems");
    }

    private void searchItem(){
        Thread t = new Thread(){

            public void run(){
                if(itemList != null){
                   // if(!search.getText().equals("")){
                        ItemTableModel itemtm = (ItemTableModel) itemList.getModel();
                        Item s = itemtm.getItemByName(search.getText());
                        try {
                            // Исправляем данные на деталь - поэтому false
                            // Также заметим, что необходимо указать не просто this, а MainFrame.this
                            // Иначе класс не будет воспринят - он же другой - анонимный
                            //ItemDialog sd = new ItemDialog(ms.getGroups(), false, MainFrame.this);
                           // sd.setItem(s);
                            SearchDilog sd = new SearchDilog();
                            sd.setModal(true);
                            sd.setVisible(true);
                            if (sd.getResult()) {
                                //Item us = sd.getItem();
                                /*if(nameCheck)ms.updateItem(us);
                                if(countCheck)ms.updateItem(us);
                                if(dateCheck)ms.updateItem(us);*/
                                reloadItems();
                            }
                        } catch (Exception e){//(SQLException e) {
                            JOptionPane.showMessageDialog(MainFrame.this, e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    else return;

                }
           // }
        };
        t.start();
    }

    private class TableSearchRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setBackground(null);
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if(vector.get(row).getIn_stock()==0)component.setBackground(Color.RED);
         /*  if(value instanceof Integer){int i = (Integer)value;

               if ((Integer)value==0 && )component.setBackground(null);if(row>0)component.setBackground(Color.RED);}*/

            /*ItemTableModel itm = (ItemTableModel) table.getModel();
            Item item = itm.getItem(row);
                if(item.getIn_stock()==0) component.setBackground(Color.RED);
                else if(item.getIn_stock() > 0 && item.getIn_stock() < 3) {component.setBackground(Color.YELLOW);setBorder(noFocusBorder);}
                else component.setBackground(table.getBackground());*/

           // table.repaint();
            return component;
        }}

    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    // Мы сразу отменим продолжение работы, если не сможем получить
                    // коннект к базе данных
                    MainFrame sf = new MainFrame();
                    sf.setDefaultCloseOperation(EXIT_ON_CLOSE);
                    sf.setVisible(true);
                    sf.reloadItems();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    // Наш внутренний класс - переопределенная панель.
    class GroupPanel extends JPanel {
        public Dimension getPreferredSize() {
            return new Dimension(225, 0);
        }
}}
