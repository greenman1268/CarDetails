package frame.Sold;

import frame.Search.ItemTableSearchModel;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

/**
 * Created on 19.05.2016
 */

public class SoldFrame extends JFrame implements ActionListener, ListSelectionListener, ChangeListener {
    private ManagementSystem ms = null;
    private JTable itemList;

    private ArrayList<Item> vector;
    private Vector<Item> selected = new Vector<>();

    public SoldFrame()throws Exception{
        ms = ManagementSystem.getInstance();

        // ������� ������� ������
        JPanel top = new JPanel();
        // ������������� ��� ��� layout
        top.setLayout(null);
        top.setPreferredSize(new Dimension(500,30));

        // ������� ������ ������ � ������ �� layout
        JPanel bot = new JPanel();
        bot.setLayout(new BorderLayout());

        // ������� ������ ������ ��� ������ ������ �������
        JPanel right = new JPanel();
        // ������ layout � ������ "������" ������ ������
        right.setLayout(new BorderLayout());
        right.setBorder(new BevelBorder(BevelBorder.LOWERED));
        // ������� �������
        right.add(new JLabel("������:"), BorderLayout.NORTH);

        itemList = new JTable(1, 6);
        right.add(new JScrollPane(itemList), BorderLayout.CENTER);
        bot.add(right, BorderLayout.CENTER);
        bot.add(top, BorderLayout.NORTH);
        // ���������  ������ ������ � �����
        getContentPane().add(bot, BorderLayout.CENTER);
        vector =(ArrayList<Item>) ms.getAllItems();
        TableSearchRenderer tsr = new TableSearchRenderer();
        itemList.setDefaultRenderer(Object.class, tsr);

        JTableHeader header = itemList.getTableHeader();
        header.setDefaultRenderer(new MyTableHeaderRenderer(header.getDefaultRenderer()));

        // ������ ������� �����
        setBounds(200, 100, 1000, 500);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

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

    // ����� ��� ���������� ������ ������� ��� ������������ ������
    public void reloadItems() {
        // ������� ��������� ����� ��� ������
        Thread t = new Thread(){// SwingUtilities.invokeLater(new Runnable() {// Thread t = new Thread() {
            // �������������� � ��� ����� run

            public void run() {
                if (itemList != null) {
                    // �������� ���������� ������

                    try {
                        // �������� ������ �������

                        Collection<Item> s = ms.getSold();//ms.getItemsFromGroup(g);

                        final Vector<Item> v = new Vector(s);
                        if(selected!=null){
                            for (int i = 0; i < v.size(); i++) {
                                for (int j = 0; j < selected.size(); j++) {
                                    if(v.get(i).getItemName().equals(selected.get(j).getItemName())&& v.get(i).getGroupId()==selected.get(j).getGroupId())
                                        v.get(i).setPrint(selected.get(j).getPrint());
                                }
                            }
                        }
                        // � ������������� ������ ��� ������� � ������ �������
                        itemList.setModel(new ItemTableSearchModel(v));
                        itemList.getModel().addTableModelListener(new TableModelListener() {
                            @Override
                            public void tableChanged(TableModelEvent tableModelEvent) {
                                int row = tableModelEvent.getFirstRow();
                                int column = tableModelEvent.getColumn();
                                if(column == 5){
                                    TableModel model = (TableModel)tableModelEvent.getSource();
                                    Boolean checked = (Boolean)model.getValueAt(row,column);
                                    Item item = v.get(row);
                                    item.setPrint(checked);
                                    selected.add(item);
                                }
                            }
                        });
                        vector = (ArrayList<Item>) s;
                        RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(itemList.getModel());
                        itemList.setRowSorter(sorter);

                    } catch (SQLException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(SoldFrame.this, e.getMessage());
                    }}//}}
            }

            // ��������� ������ ������ run
        };
        // ��������� ����������� ���������� ������
        // � ������ �� ��������� ��� �����
        t.start();
    }

    private class TableSearchRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setBackground(null);
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if(vector.get(row).getIn_stock() == 0)component.setBackground(Color.RED);
            else if(vector.get(row).getIn_stock() > 0 && vector.get(row).getIn_stock() < 3)component.setBackground(Color.YELLOW);
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
