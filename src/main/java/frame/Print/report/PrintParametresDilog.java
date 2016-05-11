package frame.Print.report;

import logic.Currency;
import logic.Group;
import logic.Item;
import logic.ManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

/**
 * Created on 11.05.2016
 */
public class PrintParametresDilog extends JDialog implements ActionListener {
    private static final int D_HEIGHT = 240;   // высота окна
    private final static int D_WIDTH = 1000;   // ширина окна

    // Владелец нашего окна - вводим для вызова нужного нам метода
    // Результат нажатия кнопок
    private boolean result = false;
    // Параметры детали
    private JTextField itemName = new JTextField();
    private JTextField count = new JTextField();
    private JTextField price = new JTextField();
    private JComboBox currency;
    private JLabel name;
    private ManagementSystem ms;
    private Item item;
    private ArrayList<Currency> list;

    public PrintParametresDilog(Item item){
        this.item = item;
        try {
            this.ms = ManagementSystem.getInstance();
            this.list =(ArrayList) ms.getRateList();

        } catch (Exception e) {
            e.printStackTrace();
        }
        // Установить заголовок
        setTitle("Ввод данных на печать");
        getContentPane().setLayout(null);

        name = new JLabel(item.getItemName(), JLabel.RIGHT);
        name.setBounds(10, 30, 200, 20);
        getContentPane().add(l);

        JLabel l = new JLabel("Количество: ", JLabel.RIGHT);
        l.setBounds(220,30,150,20);
        getContentPane().add(l);

        count.setBounds(380,30,100,20);
        getContentPane().add(count);

        l = new JLabel("Цена:", JLabel.RIGHT);
        l.setBounds(490,30,100,20);
        getContentPane().add(l);

        price.setBounds(600,30,150,20);
        getContentPane().add(price);


        currency = new JComboBox(new Vector(list));
        currency.setBounds(760,30,100,20);
        getContentPane().add(currency);

        JButton btnOk = new JButton("ОК");
        btnOk.setName("OK");
        btnOk.addActionListener(this);
        btnOk.setBounds(10, 60, 100, 25);
        getContentPane().add(btnOk);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setName("Cancel");
        btnCancel.addActionListener(this);
        btnCancel.setBounds(120, 60, 100, 25);
        getContentPane().add(btnCancel);

        // Устанавливаем поведение формы при закрытии - не закрывать совсем.
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        // Получаем размеры экрана
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        // А теперь просто помещаем его по центру, вычисляя координаты на основе полученной информации
        setBounds(((int) d.getWidth() - PrintParametresDilog.D_WIDTH) / 2, ((int) d.getHeight() - PrintParametresDilog.D_HEIGHT) / 2,
                PrintParametresDilog.D_WIDTH, PrintParametresDilog.D_HEIGHT);
    }

    public boolean getResult() {
        return result;
    }

    // Установить поля соответственно переданным данным о детале
    public void setItem(Item item) {

        count.setText(Integer.toString(item.getIn_stock()));
        price.setText("0.0");
        currency.setSelectedIndex(2);
      }


    // Вернуть данные в виде новой детали с соотвтествующими полями
    public Item getItem() {
        Item item = new Item();
        item.setItemName(name.getText());

       /* if(price.getText().equals("")){item.setPrice(new BigDecimal(0));}
        else {
            BigDecimal bg = new BigDecimal(0);
            bg.setScale(2, RoundingMode.HALF_UP);
            bg = new BigDecimal(price.getText());
            item.setPrice(bg); }*/

        if(in_stock.getText().equals("")){ item.setIn_stock(0); }
        else { item.setIn_stock(Integer.parseInt(in_stock.getText())); }

        if(sold.getText().equals("")){ item.setSold(0); }
        else { item.setSold(Integer.parseInt(sold.getText())); }

        item.setGroupId(((Group) groupList.getSelectedItem()).getGroup_id());

        return item;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton src = (JButton) e.getSource();
        // Добавляем деталь, но не закрываем окно
        // Здесь мы не будем вызывать в отдельном потоке сохранение.
        // Оно не занимает много времени и лишние действия здесь не оправданы

        if (src.getName().equals("OK")) {

            result = true;
        }
        if (src.getName().equals("Cancel")) {
            result = false;
        }
        setVisible(false);
    }
}
