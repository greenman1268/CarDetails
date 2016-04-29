package frame;

import javax.swing.*;
import java.util.Vector;

/**
 * Created on 29.04.2016
 */
public class GroupListModel extends AbstractListModel {
    Vector group;

    public GroupListModel(Vector group){
        this.group = group;
    }

    public int getSize() {
        if(group != null){
            return group.size();}
        return 0;
    }

    public Object getElementAt(int index) {
        if(group != null){
           // Group groupitem = (Group)group.get(index);
            return group.get(index);
        }
        return null;
    }
}
