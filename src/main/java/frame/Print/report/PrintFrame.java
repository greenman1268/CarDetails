package frame.Print.report;

import frame.Main.MainFrame;
import frame.Main.RatesDilog;
import logic.Item;
import logic.ManagementSystem;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Vector;

/**
 * Created on 11.05.2016
 */

public class PrintFrame extends JFrame implements ActionListener, ListSelectionListener, ChangeListener {
    private ManagementSystem ms = null;
    private JTable itemList;
    private Vector<Item> selected;
    private Vector<Item> resetList;
    private Vector<Item> listToPrint = new Vector<>();
    private boolean result;

    public PrintFrame(Vector<Item> selected){

        this.selected = new Vector<>(selected);
        this.resetList = new Vector<>(selected);

        try {
            ms = ManagementSystem.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Создаем верхнюю панель
        JPanel top = new JPanel();
        // Устанавливаем для нее layout
        top.setLayout(null);
        top.setPreferredSize(new Dimension(500,30));

        //указать курс валют
        JButton lb = new JButton("Курс");
        lb.setName("Rates");
        lb.setBounds(100,5,70,20);
        lb.addActionListener(this);
        top.add(lb);

        // Создаем нижнюю панель и задаем ей layout
        JPanel bot = new JPanel();
        bot.setLayout(new BorderLayout());

        // Создаем правую панель для вывода списка деталей
        JPanel right = new JPanel();
        // Задаем layout и задаем "бордюр" вокруг панели
        right.setLayout(new BorderLayout());
        right.setBorder(new BevelBorder(BevelBorder.LOWERED));
        // Создаем надпись
        right.add(new JLabel("Детали:"), BorderLayout.NORTH);

        itemList = new JTable(1, 8);
        right.add(new JScrollPane(itemList), BorderLayout.CENTER);

        JButton btnOk = new JButton("Печать");
        btnOk.setBounds(5,5,100,20);
        btnOk.setName("OK");
        btnOk.addActionListener(this);
        JButton btnParam = new JButton("Параметры");
        btnParam.setBounds(110,5,120,20);
        btnParam.setName("Params");
        btnParam.addActionListener(this);

        // Создаем панель, на которую положим наши кнопки и кладем ее вниз
        JPanel pnlBtnSt = new JPanel();
        pnlBtnSt.setLayout(null);
        pnlBtnSt.setPreferredSize(new Dimension(2000,30));
        pnlBtnSt.add(btnOk);
        pnlBtnSt.add(btnParam);
        right.add(pnlBtnSt, BorderLayout.SOUTH);

        // Вставляем панели со списками групп и деталей в нижнюю панель
        bot.add(right, BorderLayout.CENTER);
        bot.add(top, BorderLayout.NORTH);
        // Вставляем  нижнюю панель в форму
        getContentPane().add(bot, BorderLayout.CENTER);

        TableSearchRenderer tsr = new TableSearchRenderer();
        itemList.setDefaultRenderer(Object.class, tsr);

        JTableHeader header = itemList.getTableHeader();
        header.setDefaultRenderer(new MyTableHeaderRenderer(header.getDefaultRenderer()));
        // Задаем границы формы
        setBounds(200, 100, 1000, 500);

    }
    public boolean getResult(){return result;}
    public Vector<Item> getResetList(){return resetList;}

    public void reloadItems(){
        Thread t = new Thread(){

            public void run(){
                if (itemList != null) {

                    itemList.setModel(new PrintTableModel(selected));
                    RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(itemList.getModel());
                    itemList.setRowSorter(sorter);
                    itemList.getModel().addTableModelListener(new TableModelListener() {
                        @Override
                        public void tableChanged(TableModelEvent tableModelEvent) {
                            int row = tableModelEvent.getFirstRow();
                            int column = tableModelEvent.getColumn();
                            if(column == 7){
                                TableModel model = (TableModel)tableModelEvent.getSource();
                                Boolean checked = (Boolean)model.getValueAt(row,column);
                                if(checked){
                                    Item item = selected.get(row);
                                    listToPrint.add(item);
                                }
                                if(!checked)listToPrint.remove(row);
                            }
                        }
                    });
                }
            }
        };

        t.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof Component) {
            Component c = (Component) e.getSource();
            if (c.getName().equals("OK")) {
                toPrint();
                result = true;
            }
            if (c.getName().equals("Params")){
                createParams();
            }
            if (c.getName().equals("Rates")){
                updateRates();
            }
        }

    }

    @Override
    public void stateChanged(ChangeEvent e) {
        reloadItems();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            reloadItems();
        }
    }

    public void toPrint(){
        /*ExcelGenerateReport ExGR = new ExcelGenerateReport(new ArrayList<>(selected));
                        ExGR.main();
                        */
        //ms.updateItem();
    }

    public void createParams(){
        Thread t = new Thread(){

            public void run() {
                if(itemList!=null){
                    PrintTableModel prtm = (PrintTableModel)itemList.getModel();
                    if(itemList.getSelectedRow() >= 0){
                        Item item = prtm.getItem(itemList.getSelectedRow());
                PrintParametresDilog ppd = new PrintParametresDilog(item, PrintFrame.this);
                        ppd.setItem(item);
                ppd.setModal(true);
                ppd.setVisible(true);
                if (ppd.getResult()) {
                    ppd.updateItem(selected);
                    reloadItems();
                }}
                else JOptionPane.showMessageDialog(PrintFrame.this,
                            "Необходимо отметить деталь в списке");
                    return;}

            }
        };
        t.start();

    }

    public void updateRates(){
        Thread t = new Thread(){

            public void run(){
                RatesDilog rd = new RatesDilog();
                rd.setModal(true);
                rd.setVisible(true);
                if (rd.getResult()) {
                    reloadItems();
                }
            }
        };
        t.start();
    }

    private class TableSearchRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setBackground(null);
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if(selected.get(row).getIn_stock() == 0)component.setBackground(Color.RED);
            else if(selected.get(row).getIn_stock() > 0 && selected.get(row).getIn_stock() < 3)component.setBackground(Color.YELLOW);
            else component.setBackground(table.getBackground());
            if(table.isCellSelected(row,column)){component.setBackground(Color.GRAY);}

            return component;
        }}

    private static class MyTableHeaderRenderer implements TableCellRenderer {
        private static final Font labelFont = new Font("Arial", Font.BOLD, 11);

        private TableCellRenderer delegate;

        public MyTableHeaderRenderer(TableCellRenderer delegate) {
            this.delegate = delegate;
        }


        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            Component c = delegate.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if(c instanceof JLabel) {
                JLabel label = (JLabel) c;
                label.setFont(labelFont);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setBorder(BorderFactory.createEtchedBorder());

            }
            return c;
        }
    }
}
