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
    private JTextField in_stock = new JTextField();
    private JTextField sold = new JTextField();
    private JSpinner changeDate = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH));
    private JComboBox groupList;

    // Второй параметр содержит знак - добавление детали или исправление
    public ItemDialog(List<Group> groups, boolean newItem) {
        // После вставки детали без закрытия окна нам потребуется перегрузка списка
        // А для этого нам надо иметь доступ к этому методу в главной форме
       // this.owner = owner;
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

        l = new JLabel("В наличии:", JLabel.RIGHT);
        l.setBounds(L_X, 60, L_W, 20);
        getContentPane().add(l);
        in_stock.setBounds(L_X + L_W + 10, 60, C_W, 20);
        getContentPane().add(in_stock);

        l = new JLabel("Продано:", JLabel.RIGHT);
        l.setBounds(L_X, 90, L_W, 20);
        getContentPane().add(l);
        sold.setBounds(L_X + L_W + 10, 90, C_W, 20);
        getContentPane().add(sold);

        l = new JLabel("Дата изменения:", JLabel.RIGHT);
        l.setBounds(L_X, 120, L_W, 20);
        getContentPane().add(l);
        changeDate.setBounds(L_X + L_W + 10, 120, C_W, 20);
        getContentPane().add(changeDate);

        JButton btnOk = new JButton("ОК");
        btnOk.setName("OK");
        btnOk.addActionListener(this);
        btnOk.setBounds(L_X + L_W + C_W + 10 + 50, 10, 100, 25);
        getContentPane().add(btnOk);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setName("Cancel");
        btnCancel.addActionListener(this);
        btnCancel.setBounds(L_X + L_W + C_W + 10 + 50, 40, 100, 25);
        getContentPane().add(btnCancel);

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
        in_stock.setText(Integer.toString(item.getIn_stock()));
        sold.setText(Integer.toString(item.getSold()));

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
        item.setChangeDate(d);

        if(in_stock.getText().equals("")){ item.setIn_stock(0); }
        else {item.setIn_stock(Integer.parseInt(in_stock.getText())); }

        if(sold.getText().equals("")){ item.setSold(0); }
        else { item.setSold(Integer.parseInt(sold.getText())); }

        item.setGroupId(((Group) groupList.getSelectedItem()).getGroup_id());

        return item;
    }

    // Получить результат, true - кнопка ОК, false - кнопка Cancel
    public boolean getResult() {
        return result;
    }

    public void actionPerformed(ActionEvent e) {
        JButton src = (JButton) e.getSource();
        // Добавляем деталь, но не закрываем окно
        // Здесь мы не будем вызывать в отдельном потоке сохранение.
        // Оно не занимает много времени и лишние действия здесь не оправданы
       /* if (src.getName().equals("New")) {
            result = true;
            try {
                ManagementSystem.getInstance().insertItem(getItem());
                owner.reloadItems();
                itemName.setText("");
            } catch (Exception sql_e) {
                JOptionPane.showMessageDialog(this, sql_e.getMessage());
            }
            return;
        }*/
        if (src.getName().equals("OK")) {
            if(itemName.getText().equals("")){
                JOptionPane.showMessageDialog(ItemDialog.this,
                        "Введите имя");return;
            }
            result = true;
        }
        if (src.getName().equals("Cancel")) {
            result = false;
        }
        setVisible(false);
    }
    public String getItemName (){
        return itemName.getText();
    }

}
