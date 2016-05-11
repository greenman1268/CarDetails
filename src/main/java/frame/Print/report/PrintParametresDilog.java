package frame.Print.report;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created on 11.05.2016
 */
public class PrintParametresDilog extends JDialog implements ActionListener {
    private static final int D_HEIGHT = 240;   // высота окна
    private final static int D_WIDTH = 450;   // ширина окна
    private final static int L_X = 10;      // левая граница метки для поля
    private final static int L_W = 100;     // ширина метки для поля
    private final static int C_W = 150;     // ширина поля
    // Владелец нашего окна - вводим для вызова нужного нам метода
    // Результат нажатия кнопок
    private boolean result = false;
    // Параметры детали
    private int itemId = 0;
    private JTextField itemName = new JTextField();
    private JTextField in_stock = new JTextField();
    private JTextField count = new JTextField();
    private JTextField price = new JTextField();
    private JComboBox groupList;

    public PrintParametresDilog(){
        // Установить заголовок
        setTitle("Редактирование данных детали");
        getContentPane().setLayout(null);

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
