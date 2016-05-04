package frame;

import logic.ManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

/**
 * Created on 28.04.2016
 */
public class SearchDilog extends JDialog implements ActionListener {

    private static final int D_HEIGHT = 200;   // высота окна
    private final static int D_WIDTH = 550;   // ширина окна
    private final static int L_X = 10;      // левая граница метки для поля
    private final static int L_W = 100;     // ширина метки для поля
    private final static int C_W = 150;     // ширина поля

    private JButton btnOk = new JButton("OK");
    private JButton btnCancel = new JButton("Cancel");
    private JTextField itemName = new JTextField();
    private JTextField in_stock = new JTextField();
    private JTextField in_stock2 = new JTextField();
    private JSpinner changeDate = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH));
    private JSpinner changeDate2 = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH));
    private JCheckBox name = new JCheckBox();
    private JCheckBox count = new JCheckBox();
    private JCheckBox date = new JCheckBox();
    private SearchFrame SF;
    private MainFrame owner;
    private boolean result = false;
    private ManagementSystem ms;

    public SearchDilog(MainFrame mf){

        this.owner = mf;
        try {
            ms = ManagementSystem.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Установить заголовок
        setTitle("Критерии поиска");
        getContentPane().setLayout(null);

        JLabel l = new JLabel("По имени:", JLabel.RIGHT);
        l.setBounds(L_X, 30, L_W, 20);
        getContentPane().add(l);
        itemName.setBounds(L_X + L_W + 10, 30, C_W, 20);
        getContentPane().add(itemName);
        name.setBounds(L_X + L_W + 10 + C_W, 30, 20, 20);
        getContentPane().add(name);

        l = new JLabel("По количеству:", JLabel.RIGHT);
        l.setBounds(L_X, 60, L_W, 20);
        getContentPane().add(l);
        in_stock.setBounds(L_X + L_W + 10, 60, C_W, 20);
        getContentPane().add(in_stock);
        l = new JLabel(":", JLabel.RIGHT);
        l.setBounds(L_X + L_W + 10 + C_W, 60, 10, 20);
        getContentPane().add(l);
        in_stock2.setBounds(L_X + L_W + 30 + C_W, 60, C_W, 20);
        getContentPane().add(in_stock2);
        count.setBounds(L_X + L_W + 30 + C_W + C_W, 60, 20, 20);
        getContentPane().add(count);

        l = new JLabel("По дате:", JLabel.RIGHT);
        l.setBounds(L_X, 90, L_W, 20);
        getContentPane().add(l);
        changeDate.setBounds(L_X + L_W + 10, 90, C_W, 20);
        getContentPane().add(changeDate);
        l = new JLabel(":", JLabel.RIGHT);
        l.setBounds(L_X + L_W + 10 + C_W, 90, 10, 20);
        getContentPane().add(l);
        changeDate2.setBounds(L_X + L_W + 30 + C_W, 90, C_W, 20);
        getContentPane().add(changeDate2);
        date.setBounds(L_X + L_W + 30 + C_W + C_W, 90, 20, 20);
        getContentPane().add(date);

        JButton btnOk = new JButton("Поиск");
        btnOk.setName("OK");
        btnOk.addActionListener(this);
        btnOk.setBounds(L_X + L_W + 10, 120, 100, 25);
        getContentPane().add(btnOk);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setName("Cancel");
        btnCancel.addActionListener(this);
        btnCancel.setBounds(L_X + L_W + 120, 120, 100, 25);
        getContentPane().add(btnCancel);

        // Устанавливаем поведение формы при закрытии - не закрывать совсем.
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        // Получаем размеры экрана
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        // А теперь просто помещаем его по центру, вычисляя координаты на основе полученной информации
        setBounds(((int) d.getWidth() - SearchDilog.D_WIDTH) / 2, ((int) d.getHeight() - SearchDilog.D_HEIGHT) / 2,
                SearchDilog.D_WIDTH, SearchDilog.D_HEIGHT);
    }
    public boolean getResult() {
        return result;
    }

    public void actionPerformed(ActionEvent e) {
        JButton src = (JButton) e.getSource();

        if (src.getName().equals("OK")) {
             int countSelected = 0;
                if(name.isSelected())countSelected++;
                if(date.isSelected())countSelected++;
                if(count.isSelected())countSelected++;
                if(countSelected == 0){
                    JOptionPane.showMessageDialog(SearchDilog.this,
                            "Выберите критерий поиска");
                    return;
                }
                if (countSelected > 1){
                    JOptionPane.showMessageDialog(SearchDilog.this,
                            "Выберите только один критерий поиска");
                    if(name.isSelected())name.setSelected(false);
                    if(count.isSelected())count.setSelected(false);
                    if(date.isSelected())date.setSelected(false);
                    return;
                }

            try {
                if(!in_stock.getText().equals("")||!in_stock2.getText().equals(""))
                {
                    Integer.parseInt(in_stock.getText());
                    Integer.parseInt(in_stock2.getText());
                }
            }catch (Exception ex){
                JOptionPane.showMessageDialog(SearchDilog.this, "Введите корректные данные 'В наличии'");
                in_stock.setText("");
                return;
            }

                 try {
                     if(name.isSelected()) {
                         if(name.getText().equals("")) {
                             JOptionPane.showMessageDialog(SearchDilog.this, "Введите имя");return;}
                         SF = new SearchFrame(true,false,false,false,owner, SearchDilog.this);
                         SF.setDefaultCloseOperation(HIDE_ON_CLOSE);
                         SF.setVisible(true);
                         SF.setAlwaysOnTop(true);
                         SF.reloadItems();
                     }

                    if(count.isSelected()) {
                        if(in_stock.getText().equals("")||in_stock2.getText().equals("")){
                            JOptionPane.showMessageDialog(SearchDilog.this, "Введите значение");return;}
                        SF = new SearchFrame(false, true, false,false,owner, SearchDilog.this);
                        SF.setDefaultCloseOperation(HIDE_ON_CLOSE);
                        SF.setVisible(true);
                        SF.setAlwaysOnTop(true);
                        SF.reloadItems();
                    }

                    if(date.isSelected()){
                        SF = new SearchFrame(false,false,true,false,owner,SearchDilog.this);
                        SF.setDefaultCloseOperation(HIDE_ON_CLOSE);
                        SF.setVisible(true);
                        SF.setAlwaysOnTop(true);
                        SF.reloadItems();
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            result = true;
        }
        if (src.getName().equals("Cancel")) {
            result = false;
        }
        setVisible(false);
    }

    public String getName(){ return itemName.getText(); }

    public void setName(String newName){ itemName.setText(newName); }

    public int getFrom(){ return Integer.parseInt(in_stock.getText());}

    public int getTO(){ return Integer.parseInt(in_stock2.getText());}

    public Date getDateFrom(){ return ((SpinnerDateModel) changeDate.getModel()).getDate();}

    public Date getDateTO(){ return ((SpinnerDateModel) changeDate2.getModel()).getDate();}
}
