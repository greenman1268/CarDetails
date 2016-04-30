package logic;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created on 27.04.2016
 */
public class Group {

    private int group_id;
    private String groupName;

    public Group(){}

    public Group(ResultSet rs) throws SQLException{
        setGroup_id(rs.getInt(1));
        setGroupName(rs.getString(2));
    }

    public int getGroup_id() {
        return group_id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public String toString() {
        return groupName;
    }
}
