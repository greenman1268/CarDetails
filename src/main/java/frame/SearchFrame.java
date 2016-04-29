package frame;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created on 29.04.2016
 */
public class SearchFrame extends JFrame implements ActionListener, ListSelectionListener, ChangeListener  {

    // ¬ведем сразу имена дл€ кнопок - потом будем их использовать в обработчиках
    private static final String MOVE_GR = "moveGroup";
    private static final String CLEAR_GR = "clearGroup";
    private static final String INSERT_IT = "insertItem";
    private static final String UPDATE_IT = "updateItem";
    private static final String DELETE_IT = "deleteItem";
    private static final String ALL_ITEMS = "allItems";
    private static final String SEARCH_IT = "searchItem";

    public SearchFrame(){

    }
    public void actionPerformed(ActionEvent e) {

    }

    public void stateChanged(ChangeEvent e) {

    }

    public void valueChanged(ListSelectionEvent e) {

    }
}
