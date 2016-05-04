package frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created on 04.05.2016
 */
public class LoginDilog extends JDialog implements ActionListener {

    private static final int D_HEIGHT = 180;   // высота окна
    private final static int D_WIDTH = 300;   // ширина окна
    private final static int L_X = 10;      // левая граница метки для поля
    private final static int L_W = 100;     // ширина метки для поля
    private final static int C_W = 150;     // ширина поля

    private JButton btnOk = new JButton("OK");
    private JButton btnCancel = new JButton("Cancel");
    private JTextField login = new JTextField();
    private JTextField password = new JTextField();
    private boolean result = false;


    public LoginDilog(){

        // Установить заголовок
        setTitle("ВХОД");
        getContentPane().setLayout(null);

        JLabel l = new JLabel("Логин:", JLabel.RIGHT);
        l.setBounds(L_X, 30, L_W, 20);
        getContentPane().add(l);
        login.setBounds(L_X + L_W + 10, 30, C_W, 20);
        getContentPane().add(login);
        l = new JLabel("Пароль:", JLabel.RIGHT);
        l.setBounds(L_X, 60, L_W, 20);
        getContentPane().add(l);
        password.setBounds(L_X + L_W + 10, 60, C_W, 20);
        getContentPane().add(password);

        btnOk = new JButton("Login");
        btnOk.setName("OK");
        btnOk.addActionListener(this);
        btnOk.setBounds(60, 100, 100, 25);
        getContentPane().add(btnOk);

        btnCancel = new JButton("Cancel");
        btnCancel.setName("Cancel");
        btnCancel.addActionListener(this);
        btnCancel.setBounds(170, 100, 100, 25);
        getContentPane().add(btnCancel);

        // Устанавливаем поведение формы при закрытии - не закрывать совсем.
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        // Получаем размеры экрана
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        // А теперь просто помещаем его по центру, вычисляя координаты на основе полученной информации
        setBounds(((int) d.getWidth() - LoginDilog.D_WIDTH) / 2, ((int) d.getHeight() - LoginDilog.D_HEIGHT) / 2,
                LoginDilog.D_WIDTH, LoginDilog.D_HEIGHT);

    }

    public void actionPerformed(ActionEvent e) {
        JButton src = (JButton) e.getSource();

        if(src.getName().equals("OK")){
            if(login.getText().equals("admin") && password.getText().equals("1234")){
                try{
                MainFrame sf = new MainFrame();
                sf.setDefaultCloseOperation(EXIT_ON_CLOSE);
                sf.setVisible(true);
                sf.reloadItems();}
            catch (Exception ex){
            ex.printStackTrace();
            }
        }
            else {JOptionPane.showMessageDialog(LoginDilog.this,
                    "Данные введено неверно");return;}
        }

        if (src.getName().equals("Cancel")) {
            result = false;
        }
        setVisible(false);
    }
}
