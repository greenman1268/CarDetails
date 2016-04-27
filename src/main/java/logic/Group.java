package logic;

/**
 * Created on 27.04.2016
 */
public class Group {

    private int group_id;
    private String groupName;

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
