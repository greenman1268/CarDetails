package frame;

/**
 * Created on 27.04.2016
 */

import logic.Group;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

public class GroupDialog extends JDialog implements ActionListener {

    private static final int D_HEIGHT = 150;   // ������
    private final static int D_WIDTH = 200;   // ������
    private JSpinner spYear;
    private JComboBox groupList;
    private JButton btnOk = new JButton("OK");
    private JButton btnCancel = new JButton("Cancel");
    private boolean result = false;

    public GroupDialog(int year, List<Group> groups) {
        // ���������� ���������
        setTitle("������� ������");

        // ������� ������� layout ��� ������ ����
        GridBagLayout gbl = new GridBagLayout();
        setLayout(gbl);
        // ������� ���������� ��� ��������� ������ ����������
        GridBagConstraints c = new GridBagConstraints();
        // ����� ������ ������ �� ������ ��� ������� ��������
        c.insets = new Insets(5, 5, 5, 5);

        // ������ ������� - ��������� ��� ���� ������ �����
        JLabel l = new JLabel("����� ������:");
        // ����� ���� ����� ����� ��� �������� ����������
        c.gridwidth = GridBagConstraints.RELATIVE;
        // �� ��������� ��� ������������, ���������� ����������
        c.fill = GridBagConstraints.NONE;
        // "�����������" ��������� � ������� ����
        c.anchor = GridBagConstraints.EAST;
        // ������������� ��� ������� ��� ������ ���������
        gbl.setConstraints(l, c);
        // ��������� ���������
        getContentPane().add(l);

        // ������ ������� - ������ �����
        groupList = new JComboBox(new Vector<Group>(groups));
        // ������� �������� ��� ���������� ������
        c.gridwidth = GridBagConstraints.REMAINDER;
        // ����������� ��������� �� ����� ������������ ��� ����
        c.fill = GridBagConstraints.BOTH;
        // "�����������" ��� � ����� �����
        c.anchor = GridBagConstraints.WEST;
        // ������������� ��� ������� ��� ������ ���������
        gbl.setConstraints(groupList, c);
        // ��������� ���������
        getContentPane().add(groupList);

        // ������ ������� - ��������� ��� ���� ������ ����
        l = new JLabel("����� ���:");
        // ����� ���� ����� ����� ��� �������� ����������
        c.gridwidth = GridBagConstraints.RELATIVE;
        // �� ��������� ��� ������������, ���������� ����������
        c.fill = GridBagConstraints.NONE;
        // "�����������" ��������� � ������� ����
        c.anchor = GridBagConstraints.EAST;
        // ������������� ��� ������� ��� ������ ���������
        gbl.setConstraints(l, c);
        // ��������� ���������
        getContentPane().add(l);

        // ����� ����������� ������ �� ���� ��� - ��� ��������
        spYear = new JSpinner(new SpinnerNumberModel(year + 1, 1900, 2100, 1));
        // ������� �������� ��� ���������� ������
        c.gridwidth = GridBagConstraints.REMAINDER;
        // ����������� ��������� �� ����� ������������ ��� ����
        c.fill = GridBagConstraints.BOTH;
        // "�����������" ��� � ����� �����
        c.anchor = GridBagConstraints.WEST;
        // ������������� ��� ������� ��� ������ ���������
        gbl.setConstraints(spYear, c);
        // ��������� ���������
        getContentPane().add(spYear);

        c.gridwidth = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.BOTH;
        btnOk.setName("OK");
        // ��������� �������� ��� ������
        btnOk.addActionListener(this);
        // ������������� ��� ������� ��� ������ ���������
        gbl.setConstraints(btnOk, c);
        // ��������� ���������
        getContentPane().add(btnOk);

        btnCancel.setName("Cancel");
        // ��������� �������� ��� ������
        btnCancel.addActionListener(this);
        // ������������� ��� ������� ��� ������ ���������
        gbl.setConstraints(btnCancel, c);
        // ��������� ���������
        getContentPane().add(btnCancel);

        // ������������� ��������� ����� ��� �������� - �� ��������� ������.
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        // �������� ������� ������
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        // � ������ ������ �������� ��� �� ������, �������� ���������� �� ������ ���������� ����������
        setBounds(((int) d.getWidth() - GroupDialog.D_WIDTH) / 2, ((int) d.getHeight() - GroupDialog.D_HEIGHT) / 2,
                GroupDialog.D_WIDTH, GroupDialog.D_HEIGHT);
    }

    // ������� ����, ������� ���������� �� �����
    public int getYear() {
        return ((SpinnerNumberModel) spYear.getModel()).getNumber().intValue();
    }

    // ������� ������, ������� ����������� �� �����
    public Group getGroup() {
        if (groupList.getModel().getSize() > 0) {
            return (Group) groupList.getSelectedItem();
        }
        return null;
    }

    // �������� ���������, true - ������ ��, false - ������ Cancel
    public boolean getResult() {
        return result;
    }

    // ��������� ������ ������
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
