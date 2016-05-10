package frame.Print;

import logic.Item;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
/**
 * Created on 10.05.2016
 */
public class PrintDilog extends JDialog implements ActionListener {
    private static final int D_HEIGHT = 500;   // высота окна
    private final static int D_WIDTH = 950;   // ширина окна

    private JButton btnOk = new JButton("OK");
    private JButton btnCancel = new JButton("Cancel");
    private JTextField count = new JTextField();
    private JTextField price = new JTextField();
    private Vector<Item> selected = new Vector<>();
    private boolean result = false;

    public PrintDilog(Vector<Item> selected){

        for (int i = 0; i < selected.size(); i++) {
            if(selected.get(i).getPrint()==true)this.selected.add(selected.get(i));
        }

        // Установить заголовок
        setTitle("Добавить в накладную");
        getContentPane().setLayout(null);



        btnOk = new JButton("Печать");
        btnOk.setName("OK");
        btnOk.addActionListener(this);
        btnOk.setBounds(20, 400, 100, 25);
        getContentPane().add(btnOk);

        btnCancel = new JButton("Cancel");
        btnCancel.setName("Cancel");
        btnCancel.addActionListener(this);
        btnCancel.setBounds(140, 400, 100, 25);
        getContentPane().add(btnCancel);

        // Устанавливаем поведение формы при закрытии - не закрывать совсем.
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        // Получаем размеры экрана
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        // А теперь просто помещаем его по центру, вычисляя координаты на основе полученной информации
        setBounds(((int) d.getWidth() - PrintDilog.D_WIDTH) / 2, ((int) d.getHeight() - PrintDilog.D_HEIGHT) / 2,
                PrintDilog.D_WIDTH, PrintDilog.D_HEIGHT);
    }

    public boolean getResult (){return result;}
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton src = (JButton) e.getSource();

        if (src.getName().equals("OK")) {
            result = true;
        }
        if (src.getName().equals("Cancel")) {
            result = false;
        }
        setVisible(false);
    }
}
