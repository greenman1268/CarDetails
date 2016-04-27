package frame;

import logic.Group;
import logic.Item;
import logic.ManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Created on 27.04.2016
 */

public class ItemDialog extends JDialog implements ActionListener {

    private static final int D_HEIGHT = 200;   // высота окна
    private final static int D_WIDTH = 450;   // ширина окна
    private final static int L_X = 10;      // левая граница метки для поля
    private final static int L_W = 100;     // ширина метки для поля
    private final static int C_W = 150;     // ширина поля
    // Владелец нашего окна - вводим для вызова нужного нам метода
    private MainFrame owner;
    // Результат нажатия кнопок
    private boolean result = false;
    // Параметры детали
    private int itemId = 0;
    private JTextField itemName = new JTextField();
    private JSpinner changeDate = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH));
    private ButtonGroup sex = new ButtonGroup();
    private JSpinner year = new JSpinner(new SpinnerNumberModel(2006, 1900, 2100, 1));
    private JComboBox groupList;

    // Второй параметр содержит знак - добавление детали или исправление
    public ItemDialog(List<Group> groups, boolean newItem, MainFrame owner) {
        // После вставки детали без закрытия окна нам потребуется перегрузка списка
        // А для этого нам надо иметь доступ к этому методу в главной форме
        this.owner = owner;
        // Установить заголовок
        setTitle("Редактирование данных детали");
        getContentPane().setLayout(new FlowLayout());

        groupList = new JComboBox(new Vector<Group>(groups));

        // Не будем использовать layout совсем
        getContentPane().setLayout(null);

        // Разместим компоненты по абсолютным координатам
        // Имя
        JLabel l = new JLabel("Имя:", JLabel.RIGHT);
        l.setBounds(L_X, 30, L_W, 20);
        getContentPane().add(l);
        itemName.setBounds(L_X + L_W + 10, 30, C_W, 20);
        getContentPane().add(itemName);

        l = new JLabel("Дата изменения:", JLabel.RIGHT);
        l.setBounds(L_X, 90, L_W, 20);
        getContentPane().add(l);
        changeDate.setBounds(L_X + L_W + 10, 90, C_W, 20);
        getContentPane().add(changeDate);

        JButton btnOk = new JButton("OK");
        btnOk.setName("OK");
        btnOk.addActionListener(this);
        btnOk.setBounds(L_X + L_W + C_W + 10 + 50, 10, 100, 25);
        getContentPane().add(btnOk);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setName("Cancel");
        btnCancel.addActionListener(this);
        btnCancel.setBounds(L_X + L_W + C_W + 10 + 50, 40, 100, 25);
        getContentPane().add(btnCancel);

        if (newItem) {
            JButton btnNew = new JButton("New");
            btnNew.setName("New");
            btnNew.addActionListener(this);
            btnNew.setBounds(L_X + L_W + C_W + 10 + 50, 70, 100, 25);
            getContentPane().add(btnNew);
        }

        // Устанавливаем поведение формы при закрытии - не закрывать совсем.
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        // Получаем размеры экрана
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        // А теперь просто помещаем его по центру, вычисляя координаты на основе полученной информации
        setBounds(((int) d.getWidth() - ItemDialog.D_WIDTH) / 2, ((int) d.getHeight() - ItemDialog.D_HEIGHT) / 2,
                ItemDialog.D_WIDTH, ItemDialog.D_HEIGHT);
    }

    // Установить поля соответственно переданным данным о детале
    public void setItem(Item item) {
        itemId = item.getItemId();
        itemName.setText(item.getItemName());
        changeDate.getModel().setValue(item.getChangeDate());
        /*for (Enumeration e = sex.getElements(); e.hasMoreElements();) {
            AbstractButton ab = (AbstractButton) e.nextElement();
            ab.setSelected(ab.getActionCommand().equals(new String("" + st.getSex())));
        }*/
       // year.getModel().setValue(new Integer(item.getChangeDate()));
        for (int i = 0; i < groupList.getModel().getSize(); i++) {
            Group g = (Group) groupList.getModel().getElementAt(i);
            if (g.getGroup_id() == item.getGroupId()) {
                groupList.setSelectedIndex(i);
                break;
            }
        }
    }

    // Вернуть данные в виде новой детали с соотвтествующими полями
    public Item getItem() {
        Item item = new Item();
        item.setItemId(itemId);
        item.setItemName(itemName.getText());
        Date d = ((SpinnerDateModel) changeDate.getModel()).getDate();
        item.setChangeDate2(d);
       /* for (Enumeration e = sex.getElements(); e.hasMoreElements();) {
            AbstractButton ab = (AbstractButton) e.nextElement();
            if (ab.isSelected()) {
                item.setSex(ab.getActionCommand().charAt(0));
            }
        }*/
        int y = ((SpinnerNumberModel) year.getModel()).getNumber().intValue();
       // item.setEducationYear(y);
        item.setGroupId(((Group) groupList.getSelectedItem()).getGroup_id());
        return item;
    }

    // Получить результат, true - кнопка ОК, false - кнопка Cancel
    public boolean getResult() {
        return result;
    }

    public void actionPerformed(ActionEvent e) {
        JButton src = (JButton) e.getSource();
        // Добавляем студента, но не закрываем окно
        // Здесь мы не будем вызывать в отдельном потоке сохранение.
        // Оно не занимаем много времени и лишние действия здесь не оправданы
        if (src.getName().equals("New")) {
            result = true;
            try {
                ManagementSystem.getInstance().insertItem(getItem());
                owner.reloadItems();
                itemName.setText("");
            } catch (Exception sql_e) {
                JOptionPane.showMessageDialog(this, sql_e.getMessage());
            }
            return;
        }
        if (src.getName().equals("OK")) {
            result = true;
        }
        if (src.getName().equals("Cancel")) {
            result = false;
        }
        setVisible(false);
    }

}
