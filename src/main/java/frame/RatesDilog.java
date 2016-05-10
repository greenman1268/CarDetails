package frame;

import logic.ManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.SQLException;

/**
 * Created on 10.05.2016
 */
public class RatesDilog extends JDialog implements ActionListener {


    private static final int D_HEIGHT = 180;   // высота окна
    private final static int D_WIDTH = 300;   // ширина окна
    private final static int L_X = 10;      // левая граница метки для поля
    private final static int L_W = 100;     // ширина метки для поля
    private final static int C_W = 150;     // ширина поля

    private JButton btnOk = new JButton("OK");
    private JButton btnCancel = new JButton("Cancel");
    private JTextField dollar = new JTextField();
    private JTextField euro = new JTextField();

    private MainFrame owner;
    private boolean result = false;
    private ManagementSystem ms;

    public RatesDilog(){
        try {
            ms = ManagementSystem.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Установить заголовок
        setTitle("Курс Валют");
        getContentPane().setLayout(null);

        JLabel l = new JLabel("Доллар:", JLabel.RIGHT);
        l.setBounds(L_X, 30, L_W, 20);
        getContentPane().add(l);
        dollar.setBounds(L_X + L_W + 10, 30, C_W, 20);
        getContentPane().add(dollar);

        l = new JLabel("Евро:", JLabel.RIGHT);
        l.setBounds(L_X, 60, L_W, 20);
        getContentPane().add(l);
        euro.setBounds(L_X + L_W + 10, 60, C_W, 20);
        getContentPane().add(euro);

        btnOk = new JButton("Применить");
        btnOk.setName("OK");
        btnOk.addActionListener(this);
        btnOk.setBounds(L_X + 50, 100, 100, 25);
        getContentPane().add(btnOk);

        btnCancel = new JButton("Cancel");
        btnCancel.setName("Cancel");
        btnCancel.addActionListener(this);
        btnCancel.setBounds(L_X + 40 + 120, 100, 100, 25);
        getContentPane().add(btnCancel);

        try {
            setCurrency();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Устанавливаем поведение формы при закрытии - не закрывать совсем.
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        // Получаем размеры экрана
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        // А теперь просто помещаем его по центру, вычисляя координаты на основе полученной информации
        setBounds(((int) d.getWidth() - RatesDilog.D_WIDTH) / 2, ((int) d.getHeight() - RatesDilog.D_HEIGHT) / 2,
                RatesDilog.D_WIDTH, RatesDilog.D_HEIGHT);

    }

    // Установить поля соответственно переданным данным о детале
    public void setCurrency() throws SQLException {
        dollar.setText(String.valueOf(ms.getRateValByName("dollar")));
        euro.setText(String.valueOf(ms.getRateValByName("euro")));
    }

    public boolean getResult() {
        return result;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton src = (JButton) e.getSource();

        BigDecimal bgD = new BigDecimal(0);
        BigDecimal bgE = new BigDecimal(0);

        if(src.getName().equals("OK")){
            if(dollar.getText().equals("")){JOptionPane.showMessageDialog(RatesDilog.this,
                    "Введите правильное значение \"Доллар\"");
            return;}
            try{

                bgD.setScale(2, BigDecimal.ROUND_HALF_UP);
                bgD = new BigDecimal(dollar.getText());
            }catch (Exception ex){
                ex.printStackTrace();
                JOptionPane.showMessageDialog(RatesDilog.this,
                        "Введите правильное значение \"Доллар\"");
                return;
            }
            if(euro.getText().equals("")){JOptionPane.showMessageDialog(RatesDilog.this,
                    "Введите правильное значение \"Евро\"");
            return;}
            try{

                bgE.setScale(2, BigDecimal.ROUND_HALF_UP);
                bgE = new BigDecimal(euro.getText());
            }catch (Exception ex){
                ex.printStackTrace();
                JOptionPane.showMessageDialog(RatesDilog.this,
                        "Введите правильное значение \"Евро\"");
                return;
            }
            try {
                ms.setRateVal("dollar",bgD);
                ms.setRateVal("euro",bgE);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

        }
        if (src.getName().equals("Cancel")) {
            result = false;
        }
        setVisible(false);

    }
}
