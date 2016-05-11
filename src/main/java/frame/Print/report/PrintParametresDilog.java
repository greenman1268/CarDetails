package frame.Print.report;

import logic.Currency;
import logic.Item;
import logic.ManagementSystem;
import java.util.ArrayList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created on 11.05.2016
 */
public class PrintParametresDilog extends JDialog implements ActionListener {
    private static final int D_HEIGHT = 240;   // высота окна
    private final static int D_WIDTH = 800;   // ширина окна

    // Владелец нашего окна - вводим для вызова нужного нам метода
    // Результат нажатия кнопок
    private boolean result = false;
    // Параметры детали
    private int itemId = 0;
    private JTextField itemName = new JTextField();
    private JTextField in_stock = new JTextField();
    private JTextField count = new JTextField();
    private JTextField price = new JTextField();
    private JComboBox currency;

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

        JLabel l = new JLabel(item.getItemName(), JLabel.RIGHT);
        l.setBounds(10, 30, 200, 20);
        getContentPane().add(l);

        l = new JLabel("Количество: ", JLabel.RIGHT);
        l.setBounds(220,30,150,20);
        getContentPane().add(l);

        count.setBounds(380,30,100,20);
        getContentPane().add(count);

        l = new JLabel("Цена:", JLabel.RIGHT);
        l.setBounds(490,30,100,20);
        getContentPane().add(l);

        price.setBounds(600,30,150,20);
        getContentPane().add(price);


        currency = new JComboBox()

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

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
