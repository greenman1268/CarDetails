package logic;

/**
 * Created on 27.04.2016
 */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Item implements Comparable{

    private int itemId;
    private String itemName ;
    private Date changeDate = new Date();
    private int groupId;
    private int in_stock;
    private int sold;

    public Item() {
    }

    public Item(ResultSet rs) throws SQLException {
        setItemId(rs.getInt(1));
        setItemName(rs.getString(2));
        setChangeDate(rs.getDate(3));
        setGroupId(rs.getInt(4));
        setIn_stock(rs.getInt(5));
        setSold(rs.getInt(6));
    }

    public int getIn_stock() { return in_stock; }

    public int getSold() { return sold; }

    public int getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public Date getChangeDate() {
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

    public void setChangeDate(Date date) {
        this.changeDate = date;
    }

    public void setIn_stock(int in_stock) { this.in_stock = in_stock; }

    public void setSold(int sold) { this.sold = sold; }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String btoS(){
        SimpleDateFormat smt = new SimpleDateFormat("dd.MM.YYYY");
        return smt.format(changeDate.getTime());}

    public String toString() {
        return  "groupId: " + groupId +
                "\nitemId: " + itemId +
                "\nitemName: " + itemName +
                "\nchangeDate: " + btoS() +
                "\nin_stock: " + in_stock +
                "\nsold: " + sold + "\n";
    }

    public int compareTo(Object obj) {
        return this.toString().compareTo(obj.toString());
    }
}
