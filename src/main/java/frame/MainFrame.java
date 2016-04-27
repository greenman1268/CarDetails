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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
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
    private ManagementSystem ms = null;
    private JList grpList;
    private JTable stdList;
    private JSpinner spYear;

    public MainFrame() throws Exception{

        // Создаем строку меню
        JMenuBar menuBar = new JMenuBar();
        // Создаем выпадающее меню
        JMenu menu = new JMenu("Отчеты");
        // Создаем пункт в выпадающем меню
        JMenuItem menuItem = new JMenuItem("Все детали");
        menuItem.setName(ALL_ITEMS);
        // Добавляем листенер
        menuItem.addActionListener(this);
        // Вставляем пункт меню в выпадающее меню
        menu.add(menuItem);
        // Вставляем выпадающее меню в строку меню
        menuBar.add(menu);
        // Устанавливаем меню для формы
        setJMenuBar(menuBar);

        // Создаем верхнюю панель, где будет поле дл/* я ввода года
        JPanel top = new JPanel();
        // Устанавливаем для нее layout
        top.setLayout(new FlowLayout(FlowLayout.LEFT));

        // Вставляем пояснительную надпись
        top.add(new JLabel("Год обучения:"));
        // Делаем спин-поле
        // 1. Задаем модель поведения - только цифры
        // 2. Вставляем в панель */
        SpinnerModel sm = new SpinnerNumberModel(2006, 1900, 2100, 1);
        spYear = new JSpinner(sm);
        // Добавляем листенер
        spYear.addChangeListener(this);
        top.add(spYear);

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
        stdList = new JTable(1, 4);
        right.add(new JScrollPane(stdList), BorderLayout.CENTER);
        // Создаем кнопки для студентов
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

        // Задаем границы формы
        setBounds(100, 100, 700, 500);
    }
    // Метод для обеспечения интерфейса ActionListener
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof Component) {
            Component c = (Component) e.getSource();
            if (c.getName().equals(MOVE_GR)) {
                moveGroup();
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
        Thread t = new Thread() {
            // Переопределяем в нем метод run

            public void run() {
                if (stdList != null) {
                    // Получаем выделенную группу
                    Group g = (Group) grpList.getSelectedValue();
                    // Получаем число из спинера
                   // int y = ((SpinnerNumberModel) spYear.getModel()).getNumber().intValue();
                    try {
                        // Получаем список деталей
                        Collection<Item> s = ms.getItemsFromGroup(g);
                        // И устанавливаем модель для таблицы с новыми данными
                        stdList.setModel(new ItemTableModel(new Vector<Item>(s)));
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(MainFrame.this, e.getMessage());
                    }
                }
            }
            // Окончание нашего метода run
        };
        // Окончание определения анонимного класса

        // И теперь мы запускаем наш поток
        t.start();
    }

    // метод для переноса группы
    private void moveGroup() {
        Thread t = new Thread() {

            public void run() {
                // Если группа не выделена - выходим. Хотя это крайне маловероятно
                if (grpList.getSelectedValue() == null) {
                    return;
                }
                try {
                    // Получаем выделенную группу
                    Group g = (Group) grpList.getSelectedValue();
                    // Получаем число из спинера
                    int y = ((SpinnerNumberModel) spYear.getModel()).getNumber().intValue();
                    // Создаем наш диалог
                    GroupDialog gd = new GroupDialog(y, ms.getGroups());
                    // Задаем ему режим модальности - нельзя ничего кроме него выделить
                    gd.setModal(true);
                    // Показываем диалог
                    gd.setVisible(true);
                    // Если нажали кнопку OK - перемещаем в новую группу с новым годом
                    // и перегружаем список студентов
                    if (gd.getResult()) {
                        ms.moveItemsToGroup(g, gd.getGroup());
                        reloadItems();
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(MainFrame.this, e.getMessage());
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
                            "Вы хотите удалить студентов из группы?", "Удаление студентов",
                            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        // Получаем выделенную группу
                        Group g = (Group) grpList.getSelectedValue();
                        // Получаем число из спинера
                        int y = ((SpinnerNumberModel) spYear.getModel()).getNumber().intValue();
                        try {
                            // Удалили студентов из группы
                            ms.removeItemsFromGroup(g, String.valueOf(y));
                            // перегрузили список студентов
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

    // метод для добавления студента
    private void insertItem() {
        Thread t = new Thread() {

            public void run() {
                try {
                    // Добавляем нового студента - поэтому true
                    // Также заметим, что необходимо указать не просто this, а StudentsFrame.this
                    // Иначе класс не будет воспринят - он же другой - анонимный
                    ItemDialog sd = new ItemDialog(ms.getGroups(), true, MainFrame.this);
                    sd.setModal(true);
                    sd.setVisible(true);
                    if (sd.getResult()) {
                        Item s = sd.getItem();
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

    // метод для редактирования студента
    private void updateItem() {
        Thread t = new Thread() {

            public void run() {
                if (stdList != null) {
                    ItemTableModel stm = (ItemTableModel) stdList.getModel();
                    // Проверяем - выделен ли хоть какой-нибудь студент
                    if (stdList.getSelectedRow() >= 0) {
                        // Вот где нам пригодился метод getStudent(int rowIndex)
                        Item s = stm.getItem(stdList.getSelectedRow());
                        try {
                            // Исправляем данные на студента - поэтому false
                            // Также заметим, что необходимо указать не просто this, а StudentsFrame.this
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
                        }
                    } // Если студент не выделен - сообщаем пользователю, что это необходимо
                    else {
                        JOptionPane.showMessageDialog(MainFrame.this,
                                "Необходимо выделить студента в списке");
                    }
                }
            }
        };
        t.start();
    }

    // метод для удаления студента
    private void deleteItem() {
        Thread t = new Thread() {

            public void run() {
                if (stdList != null) {
                    ItemTableModel stm = (ItemTableModel) stdList.getModel();
                    // Проверяем - выделен ли хоть какой-нибудь студент
                    if (stdList.getSelectedRow() >= 0) {
                        if (JOptionPane.showConfirmDialog(MainFrame.this,
                                "Вы хотите удалить студента?", "Удаление студента",
                                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                            // Вот где нам пригодился метод getStudent(int rowIndex)
                            Item s = stm.getItem(stdList.getSelectedRow());
                            try {
                                ms.deleteItem(s);
                                reloadItems();
                            } catch (SQLException e) {
                                JOptionPane.showMessageDialog(MainFrame.this, e.getMessage());
                            }
                        }
                    } // Если студент не выделен - сообщаем пользователю, что это необходимо
                    else {
                        JOptionPane.showMessageDialog(MainFrame.this, "Необходимо выделить деталь в списке");
                    }
                }
            }
        };
        t.start();
    }

    // метод для показа всех студентов
    private void showAllItems() {
        JOptionPane.showMessageDialog(this, "showAllStudents");
    }

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
            return new Dimension(250, 0);
        }
}}
