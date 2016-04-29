package frame;

import logic.Group;
import logic.ManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created on 29.04.2016
 */
public class GroupInsertDilog  extends JDialog implements ActionListener {

    private static final int D_HEIGHT = 200;   // высота окна
    private final static int D_WIDTH = 450;   // ширина окна
    private final static int L_X = 10;      // левая граница метки для поля
    private final static int L_W = 100;     // ширина метки для поля
    private final static int C_W = 150;     // ширина поля
    private MainFrame owner;
    private ManagementSystem ms;
    private boolean result = false;
    private JTextField GroupName = new JTextField();
    private JButton btnOk = new JButton("OK");
    private JButton btnCancel = new JButton("Cancel");

    public GroupInsertDilog(){

        // Установить заголовок
        setTitle("Создать новую группу");

        // Не будем использовать layout совсем
        getContentPane().setLayout(null);

        // Разместим компоненты по абсолютным координатам
        // Имя
        JLabel l = new JLabel("Имя:", JLabel.RIGHT);
        l.setBounds(L_X, 30, L_W, 20);
        getContentPane().add(l);
        GroupName.setBounds(L_X + L_W + 10, 30, C_W, 20);
        getContentPane().add(GroupName);

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

        // Устанавливаем поведение формы при закрытии - не закрывать совсем.
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        // Получаем размеры экрана
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        // А теперь просто помещаем его по центру, вычисляя координаты на основе полученной информации
        setBounds(((int) d.getWidth() - GroupInsertDilog.D_WIDTH) / 2, ((int) d.getHeight() - GroupInsertDilog.D_HEIGHT) / 2,
                GroupInsertDilog.D_WIDTH, GroupInsertDilog.D_HEIGHT);
    }

    // Получить результат, true - кнопка ОК, false - кнопка Cancel
    public boolean getResult() {
        return result;
    }

    public void actionPerformed(ActionEvent e) {
        JButton src = (JButton) e.getSource();

        if (src.getName().equals("OK")) {
            try {
                Group group = new Group();
                group.setGroupName(GroupName.getText());
                ms = ManagementSystem.getInstance();
                ms.insertGroup(group);
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(GroupInsertDilog.this, e1.getMessage());
                e1.printStackTrace();
            }
            result = true;
        }
        if (src.getName().equals("Cancel")) {
            result = false;
        }
        setVisible(false);
    }
}
