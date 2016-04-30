package frame;

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

    private MainFrame mainFrame;
    private ManagementSystem ms = null;
    private JTable itemList;
    private JList grpList;
    private SearchDilog searchDilog;
    private boolean boolName;
    private boolean boolCount;
    private boolean boolDate;

    private ArrayList<Item> vector;

    public SearchFrame(boolean name, boolean count, boolean date, MainFrame mf, SearchDilog sd) throws Exception{

        // Создаем нижнюю панель и задаем ей layout
        JPanel bot = new JPanel();
        bot.setLayout(new BorderLayout());


        // Получаем коннект к базе и создаем объект ManagementSystem
        ms = ManagementSystem.getInstance();
        this.mainFrame = mf;
        this.searchDilog = sd;
        this.boolName = name;
        this.boolCount = count;
        this.boolDate = date;
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
        JButton btnUpdSt = new JButton("Исправить");
        btnUpdSt.setName(UPDATE_IT);
        btnUpdSt.addActionListener(this);
        JButton btnDelSt = new JButton("Удалить");
        btnDelSt.setName(DELETE_IT);
        btnDelSt.addActionListener(this);
        JButton btnMovGr = new JButton("Переместить");
        btnDelSt.setName(MOVE_GR);
        btnDelSt.addActionListener(this);
        // Создаем панель, на которую положим наши кнопки и кладем ее вниз
        JPanel pnlBtnSt = new JPanel();
        pnlBtnSt.setLayout(new GridLayout(1, 3));
        pnlBtnSt.add(btnUpdSt);
        pnlBtnSt.add(btnDelSt);
        pnlBtnSt.add(btnMovGr);
        right.add(pnlBtnSt, BorderLayout.SOUTH);

        // Вставляем панели со списками групп и деталей в нижнюю панель
        bot.add(right, BorderLayout.CENTER);

        // Вставляем  нижнюю панель в форму
        getContentPane().add(bot, BorderLayout.CENTER);

        vector =(ArrayList<Item>) ms.getAllItems();
        TableSearchRenderer tsr = new TableSearchRenderer();
        itemList.setDefaultRenderer(Object.class, tsr);

        // Задаем границы формы
        setBounds(100, 100, 1000, 500);
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
        Thread t = new Thread() {// SwingUtilities.invokeLater(new Runnable() {//
            // Переопределяем в нем метод run

            public void run() {
                if (itemList != null) {
                    // Получаем выделенную группу
                   int countGroups = 0;
                    try {
                        countGroups = ms.getGroups().size();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    if(countGroups > 0){
                    for (int i = 0; i < countGroups; i++) {

                    try {
                        // Получаем список деталей
                        Group g = ms.getGroups().get(i);
                        Collection<Item> s = null;
                        if(boolName){
                         s = ms.searchItemsByName(searchDilog.getName());} // ms.getAllItems();
                        else if (boolCount){}
                        else if (boolDate){}
                        // И устанавливаем модель для таблицы с новыми данными
                        itemList.setModel(new ItemTableSearchModel(new Vector<Item>(s)));
                        vector =(ArrayList<Item>) s;

                    } catch (SQLException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(SearchFrame.this, e.getMessage());
                    }}}}
            }

            // Окончание нашего метода run
        };
        // Окончание определения анонимного класса
        // И теперь мы запускаем наш поток
         t.start();
    }

    private void moveTOGroup(){}

    private void updateItem(){}

    private void deleteItem(){}

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
}
