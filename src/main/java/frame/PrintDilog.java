package frame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created on 10.05.2016
 */
public class PrintDilog extends JDialog implements ActionListener {
    private static final int D_HEIGHT = 180;   // высота окна
    private final static int D_WIDTH = 300;   // ширина окна
    private final static int L_X = 10;      // левая граница метки для поля
    private final static int L_W = 100;     // ширина метки для поля
    private final static int C_W = 150;     // ширина поля

    private JButton btnOk = new JButton("OK");
    private JButton btnCancel = new JButton("Cancel");
    private JTextField item = new JTextField();

    private boolean result = false;
    public PrintDilog(){

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
