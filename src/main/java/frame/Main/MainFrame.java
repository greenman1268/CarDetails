package frame.Main;

/**
 * Created on 27.04.2016
 */

import frame.Print.PrintDilog;
import frame.Search.SearchDilog;
import frame.Search.SearchFrame;
import logic.Group;
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

public class MainFrame extends JFrame implements ActionListener, ListSelectionListener, ChangeListener {

    // Введем сразу имена для кнопок - потом будем их использовать в обработчиках
    private static final String MOVE_GR = "moveGroup";
    private static final String CLEAR_GR = "clearGroup";
    private static final String INSERT_IT = "insertItem";
    private static final String UPDATE_IT = "updateItem";
    private static final String DELETE_IT = "deleteItem";
    private static final String ALL_ITEMS = "allItems";
    private static final String SEARCH_IT = "searchItem";
    private static final String INSERT_GR = "insertGroup";
    private static final String CHANGE_GR = "changeGroup";
    private static final String DELETE_GR = "deleteGroup";
    private static final String PRINT = "print";
    private static final String RATES = "rates";

    private ManagementSystem ms = null;
    private JList grpList;
    private JTable itemList;
    private TableSearchRenderer tsr;
    private ArrayList<Item> vector;
    private Vector<Item> selected = new Vector<>();

    public MainFrame() throws Exception{

        // Создаем верхнюю панель
        JPanel top = new JPanel();
        // Устанавливаем для нее layout
        top.setLayout(null);
        top.setPreferredSize(new Dimension(500,30));
        // Устанавливаем поиск
        //кнопка поиска
        JButton btnSrch = new JButton("Поиск");
        btnSrch.setName(SEARCH_IT);
        btnSrch.setBounds(5,5,70,20);
        btnSrch.addActionListener(this);
        top.add(btnSrch);

        //кнопка показать все
        JButton btnAll = new JButton("Все детали");
        btnAll.setName(ALL_ITEMS);
        btnAll.setBounds(80, 5, 110, 20);
        btnAll.addActionListener(this);
        top.add(btnAll);

        //кнопка печать
        JButton btnPrint = new JButton("Печать");
        btnPrint.setName(PRINT);
        btnPrint.setBounds(195,5,90,20);
        btnPrint.addActionListener(this);
        top.add(btnPrint);

        //указать курс валют
        JButton btnRates = new JButton("Курс");
        btnRates.setName(RATES);
        btnRates.setBounds(290, 5, 70, 20);
        btnRates.addActionListener(this);
        top.add(btnRates);

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

        JButton btnClGr = new JButton("оч.");
        btnClGr.setName(CLEAR_GR);
        JButton btnAdGr = new JButton("доб.");
        btnAdGr.setName(INSERT_GR);
        JButton btnChGr = new JButton("ред.");
        btnChGr.setName(CHANGE_GR);
        JButton btnDlGr = new JButton("уд.");
        btnDlGr.setName(DELETE_GR);
        // Добавляем листенер

        btnClGr.addActionListener(this);
        btnAdGr.addActionListener(this);
        btnChGr.addActionListener(this);
        btnDlGr.addActionListener(this);
        // Создаем панель, на которую положим наши кнопки и кладем ее вниз
        JPanel pnlBtnGr = new JPanel();
        pnlBtnGr.setLayout(new GridLayout(1, 5));


        pnlBtnGr.add(btnClGr);
        pnlBtnGr.add(btnAdGr);
        pnlBtnGr.add(btnChGr);
        pnlBtnGr.add(btnDlGr);
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

        itemList = new JTable(1, 5);
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
        JButton btnMvGr = new JButton("Переместить");
        btnMvGr.setName(MOVE_GR);
        btnMvGr.addActionListener(this);
        // Создаем панель, на которую положим наши кнопки и кладем ее вниз
        JPanel pnlBtnSt = new JPanel();
        pnlBtnSt.setLayout(new GridLayout(1, 3));
        pnlBtnSt.add(btnAddSt);
        pnlBtnSt.add(btnUpdSt);
        pnlBtnSt.add(btnDelSt);
        pnlBtnSt.add(btnMvGr);
        right.add(pnlBtnSt, BorderLayout.SOUTH);

        // Вставляем панели со списками групп и деталей в нижнюю панель
        bot.add(left, BorderLayout.WEST);
        bot.add(right, BorderLayout.CENTER);

        // Вставляем верхнюю и нижнюю панели в форму
        getContentPane().add(top, BorderLayout.NORTH);
        getContentPane().add(bot, BorderLayout.CENTER);

        Group g = (Group) grpList.getSelectedValue();
        vector =(ArrayList<Item>) ms.getItemsFromGroup(g);
        tsr = new TableSearchRenderer();
        itemList.setDefaultRenderer(Object.class, tsr);
        JTableHeader header = itemList.getTableHeader();
        header.setDefaultRenderer(new MyTableHeaderRenderer(header.getDefaultRenderer()));



        // Задаем границы формы
        setBounds(100, 100, 1000, 500);
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
            if (c.getName().equals(INSERT_GR)) {
                insertGroup();
            }
            if (c.getName().equals(CHANGE_GR)) {
                updateGroup();
            }
            if (c.getName().equals(DELETE_GR)) {
                deleteGroup();
            }
            if (c.getName().equals(PRINT)) {
                printReport();
            }
            if (c.getName().equals(RATES)){
                updateRates();
            }
}}

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

    // метод для обновления списка груп
    public void reloadGroups(){
        Thread t = new Thread() { //SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                if (grpList != null) {
                    try {
                        Collection<Group> gl = ms.getGroups();
                        grpList.setModel(new GroupListModel(new Vector<Group>(gl)));
                        grpList.setSelectedIndex(0);

                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(MainFrame.this, e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        };//});
        t.start();
    }

    // метод для обновления списка деталей для определенной группы
    public void reloadItems() {
        // Создаем анонимный класс для потока
         SwingUtilities.invokeLater(new Runnable() {// Thread t = new Thread() {
            // Переопределяем в нем метод run

            public void run() {
                if (itemList != null) {
                    // Получаем выделенную группу
                    Group g = (Group) grpList.getSelectedValue();

                    try {
                        // Получаем список деталей
                        Collection<Item> s = ms.getItemsFromGroup(g);
                        // И устанавливаем модель для таблицы с новыми данными
                       final Vector<Item> v = new Vector(s);
                        if(selected!=null){
                            for (int i = 0; i < v.size(); i++) {
                                for (int j = 0; j < selected.size(); j++) {
                                    if(v.get(i).getItemName().equals(selected.get(j).getItemName())&& v.get(i).getGroupId()==selected.get(j).getGroupId())
                                        v.get(i).setPrint(selected.get(j).getPrint());
                                }
                            }
                        }

                        itemList.setModel(new ItemTableModel(v));
                        itemList.getModel().addTableModelListener(new TableModelListener() {
                            @Override
                            public void tableChanged(TableModelEvent tableModelEvent) {
                                int row = tableModelEvent.getFirstRow();
                                int column = tableModelEvent.getColumn();
                                if(column == 4){
                                    TableModel model = (TableModel)tableModelEvent.getSource();
                                    Boolean checked = (Boolean)model.getValueAt(row,column);
                                    Item item = v.get(row);
                                    item.setPrint(checked);
                                    selected.add(item);
                                }
                            }
                        });

                        RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(itemList.getModel());
                        itemList.setRowSorter(sorter);

                        vector =(ArrayList<Item>) s;

                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(MainFrame.this, e.getMessage());
                    }
                }
            }
            // Окончание нашего метода run
        });  //};
        // Окончание определения анонимного класса
        // И теперь мы запускаем наш поток
       // t.start();
    }

    //метод для удаления группы
    private void deleteGroup(){
        Thread t = new Thread(){

            public void run(){
                if(grpList != null){
                    try {
                        if(JOptionPane.showConfirmDialog(MainFrame.this,
                                "Вы хотите удалить эту группу?", "Удаление группы",
                                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                            Group g = (Group)grpList.getSelectedValue();
                            ms.deleteGroup(g.getGroup_id());
                            ms.removeItemsFromGroup(g);
                           // reloadItems();
                            reloadGroups();
                        }

                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(MainFrame.this, e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
    }

    // метод - создать новую группу
    private void insertGroup(){
        Thread t = new Thread(){

            public void run(){
                GroupInsertDilog gid = new GroupInsertDilog();
                // Задаем ему режим модальности - нельзя ничего кроме него выделить
                gid.setModal(true);
                // Показываем диалог
                gid.setVisible(true);
                if(gid.getResult()){
                //reloadItems();
                reloadGroups();}
            }
        };
        t.start();
    }

    // метод - внести изменения в группу
    private void updateGroup(){
        Thread t = new Thread(){

            public void run(){
                if(grpList != null){
                    if(grpList.getSelectedValue() != null){
                        ChangeGroupDilog cgd = new ChangeGroupDilog((Group)grpList.getSelectedValue());
                        cgd.setModal(true);
                        cgd.setVisible(true);
                        if(cgd.getResult()){
                            reloadGroups();
                        }
                    }
                }
            }
        };
        t.start();
    }

    // метод для переноса деталей в иную группу
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
                        JOptionPane.showMessageDialog(MainFrame.this, e.getMessage());
                        e.printStackTrace();
                    }
                    if (gd.getResult()) {

                    ItemTableModel itm = (ItemTableModel) itemList.getModel();
                    Item item;

                    for (int i = 0; i < itemList.getSelectedRows().length; i++) {
                        try {
                             item = itm.getItem(itemList.getSelectedRows()[i]);
                             ms.moveItemsToGroup(item, gd.getGroup());
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(MainFrame.this, e.getMessage());
                            e.printStackTrace();
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
                            e.printStackTrace();
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
                if(grpList != null){
                try {
                    // Добавляем новую деталь - поэтому true
                    // Также заметим, что необходимо указать не просто this, а MainFrame.this
                    // Иначе класс не будет воспринят - он же другой - анонимный

                    Group g = (Group) grpList.getSelectedValue();
                    ItemDialog sd = new ItemDialog(ms.getGroups());
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
                    e.printStackTrace();
                } }
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
                            ItemDialog sd = new ItemDialog(ms.getGroups());
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
                                    e.printStackTrace();
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
        Thread t = new Thread(){

            public void run(){
                if(itemList != null){

                        try {
                            SearchFrame sf = new SearchFrame(false,false,false,true,MainFrame.this,null);
                            sf.setDefaultCloseOperation(HIDE_ON_CLOSE);
                            sf.setVisible(true);
                            sf.setAlwaysOnTop(true);
                            sf.reloadItems();
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(MainFrame.this, e.getMessage());
                            e.printStackTrace();
                        }
                }
                else return;
                reloadItems();
                }
        };
        t.start();

    }

    private void searchItem(){
        Thread t = new Thread(){

            public void run(){
                if(itemList != null){

                        try {
                            // Исправляем данные на деталь - поэтому false
                            // Также заметим, что необходимо указать не просто this, а MainFrame.this
                            // Иначе класс не будет воспринят - он же другой - анонимный
                            SearchDilog sd = new SearchDilog(MainFrame.this);
                            SearchFrame sf;
                            sd.setModal(true);
                            sd.setVisible(true);
                            if (sd.getResult()) {
                                //sf = new SearchFrame();
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

    private void printReport() {
        Thread t = new Thread(){

            public void run() {
                if (itemList != null){
                    if(selected.size()>0){
                        ArrayList<Item> list = new ArrayList<>();
                        for (int i = 0; i < selected.size(); i++) {
                            if(selected.get(i).getPrint())list.add(selected.get(i));
                        }
                        /*ExcelGenerateReport ExGR = new ExcelGenerateReport(new ArrayList<>(selected));
                        ExGR.main();
                        */
                        PrintDilog pd = new PrintDilog(selected);
                        pd.setModal(true);
                        pd.setVisible(true);

                        if(pd.getResult())
                        {
                        selected.clear();
                        reloadItems();
                        }
                        else if(!pd.getResult()){
                            reloadItems();
                        }
                    }
                    else JOptionPane.showMessageDialog(MainFrame.this,
                            "Необходимо отметить деталь в списке");
                    return;
            }
            }
        };
        t.start();
    }

    private void updateRates(){
        Thread t = new Thread(){

            public void run(){
                RatesDilog rd = new RatesDilog();
                rd.setModal(true);
                rd.setVisible(true);
                if (rd.getResult()) {
                    reloadItems();
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
        }
    }

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


    public static void main(String args[]) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                    // Мы сразу отменим продолжение работы, если не сможем получить
                    // коннект к базе данных
                    LoginDilog ld = new LoginDilog();
                    ld.setVisible(true);
                }
        });
    }

    // Наш внутренний класс - переопределенная панель.
    class GroupPanel extends JPanel {
        public Dimension getPreferredSize() {
            return new Dimension(300, 0);
        }
}}