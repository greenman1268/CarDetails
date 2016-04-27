package logic;

/**
 * Created on 27.04.2016
 */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class Item implements Comparable{

    private int itemId;
    private String itemName ;
    private java.util.Calendar changeDate = new GregorianCalendar();
    private int groupId;

    public Item() {
    }

    public Item(ResultSet rs) throws SQLException {
        setItemId(rs.getInt(1));
        setItemName(rs.getString(2));
        setChangeDate(rs.getDate(3));
        setGroupId(rs.getInt(4));
    }

    public int getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public java.util.Calendar getChangeDate() {
        return changeDate;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setChangeDate(java.sql.Date date) {
        this.changeDate.setTime(date);
    }
    public void setChangeDate2(java.util.Date date) {
        this.changeDate.setTime(date);
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String btoS(){
        SimpleDateFormat smt = new SimpleDateFormat("dd.MM.YYYY");
        smt.setCalendar(changeDate);
        return smt.format(changeDate.getTime());}

    public String toString() {
        return  "groupId: " + groupId +
                "\nitemId: " + itemId +
                "\nitemName: " + itemName +
                "\nchangeDate: " + btoS();
    }

    public int compareTo(Object obj) {
        return this.toString().compareTo(obj.toString());
    }
}
