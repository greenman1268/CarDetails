package frame.Print.report;

import logic.Currency;
import logic.Item;
import logic.ManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created on 11.05.2016
 */
public class PrintParametresDilog extends JDialog implements ActionListener {
    private static final int D_HEIGHT = 200;   // высота окна
    private final static int D_WIDTH = 350;   // ширина окна

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
    private int in_stock;
    private int sold;
    private PrintFrame pf;

    public PrintParametresDilog(Item item, PrintFrame pf){
        this.item = item;
        try {
            this.ms = ManagementSystem.getInstance();
            this.list =(ArrayList) ms.getRateList();

        } catch (Exception e) {
            e.printStackTrace();
        }
        this.in_stock = item.getIn_stock();
        this.sold = item.getSold();
        this.pf = pf;

        // Установить заголовок
        setTitle("Ввод данных на печать");
        getContentPane().setLayout(null);

        name = new JLabel(item.getItemName(), JLabel.RIGHT);
        name.setBounds(40, 30, 200, 20);
        getContentPane().add(name);

        JLabel l = new JLabel("Количество: ", JLabel.RIGHT);
        l.setBounds(10,60,100,20);
        getContentPane().add(l);

        count.setBounds(120,60,100,20);
        getContentPane().add(count);

        l = new JLabel("Цена:", JLabel.RIGHT);
        l.setBounds(10,90,100,20);
        getContentPane().add(l);

        price.setBounds(120,90,150,20);
        getContentPane().add(price);


        currency = new JComboBox(new Vector<Currency>(list));
        currency.setBounds(275,90,50,20);
        getContentPane().add(currency);

        JButton btnOk = new JButton("ОК");
        btnOk.setName("OK");
        btnOk.addActionListener(this);
        btnOk.setBounds(40, 120, 100, 25);
        getContentPane().add(btnOk);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setName("Cancel");
        btnCancel.addActionListener(this);
        btnCancel.setBounds(145, 120, 100, 25);
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
        price.setText(item.getPrice().toString());

    }


    // Вернуть данные в виде новой детали с соотвтествующими полями
    public Item updateItem(Vector<Item> selected) {
        item.setItemId(this.item.getItemId());
        item.setItemName(name.getText());
        item.setIn_stock(in_stock-Integer.parseInt(count.getText()));
        item.setSold(sold+Integer.parseInt(count.getText()));
        item.setGroupId(this.item.getGroupId());

        BigDecimal pricen = new BigDecimal(0);
        pricen.setScale(2,BigDecimal.ROUND_HALF_UP);
        pricen = new BigDecimal(price.getText());

        item.setPrice(pricen.multiply(((Currency)currency.getModel().getSelectedItem()).getValue()));
        for (int i = 0; i < selected.size(); i++) {
            if(selected.get(i).getItemId()==item.getItemId()){
                selected.get(i).setIn_stock(in_stock-Integer.parseInt(count.getText()));
                selected.get(i).setSold(sold + Integer.parseInt(count.getText()));
                selected.get(i).setPrice(pricen.multiply(((Currency)currency.getModel().getSelectedItem()).getValue()));
                selected.get(i).setCount(Integer.parseInt(count.getText()));
                selected.get(i).setCurrency(((Currency) currency.getModel().getSelectedItem()).getName());
            }
        }

        return item;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton src = (JButton) e.getSource();
        // Добавляем деталь, но не закрываем окно
        // Здесь мы не будем вызывать в отдельном потоке сохранение.
        // Оно не занимает много времени и лишние действия здесь не оправданы

        if (src.getName().equals("OK")) {

            try{
                Integer.parseInt(count.getText());
            }catch (Exception ex){
                JOptionPane.showMessageDialog(PrintParametresDilog.this,
                        "Неверное значение \"Количество\"");
                ex.printStackTrace();return;
            }
            if(Integer.parseInt(count.getText())>in_stock){
                JOptionPane.showMessageDialog(PrintParametresDilog.this,
                    "Значение \"Количество\" слишком большое");return;}
            try{
                new BigDecimal(price.getText());
            }catch (Exception ex){
                JOptionPane.showMessageDialog(PrintParametresDilog.this,
                        "Неверное значение \"Цена\"");
                ex.printStackTrace();return;
            }
            result = true;
        }
        if (src.getName().equals("Cancel")) {
            result = false;
        }
        setVisible(false);
    }
}
