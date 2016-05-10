package logic;

/**
 * Created on 27.04.2016
 */

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ManagementSystem {

    private static Connection con;
    private static ManagementSystem instance;
    private static DataSource dataSource;

    private ManagementSystem() throws Exception { }

    public static synchronized ManagementSystem getInstance() throws Exception {
        if(instance == null){
            instance = new ManagementSystem();
            MysqlDataSource mysqlDS = null;
            try {

                mysqlDS = new MysqlDataSource();
                mysqlDS.setUser("root");
                mysqlDS.setPassword("126874539");
                instance.dataSource = mysqlDS;
                con =  dataSource.getConnection();

                if(checkDB().equals("NULL")){
                    makeDB();
                    mysqlDS.setURL("jdbc:mysql://localhost:3306/details");
                    instance.dataSource = mysqlDS;
                    con =  dataSource.getConnection();
                }
                else {
                    mysqlDS.setURL("jdbc:mysql://localhost:3306/details");
                    instance.dataSource = mysqlDS;
                    con =  dataSource.getConnection();
                }

            }catch (SQLException e) {
                e.printStackTrace();
            }}
        return instance;
    }

    public static String checkDB() throws SQLException {
        String result = "NULL";

        PreparedStatement stmt = con.prepareStatement("SHOW DATABASES LIKE ? ");
        stmt.setString(1, "details");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
           result = rs.getString(1);
        }
        rs.close();
        stmt.close();
        return result;
    }

    public List<Group> getGroups() throws SQLException {
        List<Group> groups = new ArrayList<Group>();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT group_id, groupName FROM groups");
            while (rs.next()) {
                Group gr = new Group();
                gr.setGroup_id(rs.getInt(1));
                gr.setGroupName(rs.getString(2));
                groups.add(gr);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
        return groups;
    }

    public Group getGroupById(int groupId)throws SQLException{
        Group group = null;
        PreparedStatement stmt = con.prepareStatement("SELECT group_id, groupName FROM groups WHERE group_id = ? ");
        stmt.setInt(1, groupId);

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            group = new Group(rs);
        }
        rs.close();
        stmt.close();
        return group;
    }

    public void deleteGroup(int groupId)throws SQLException{
        PreparedStatement stmt = null;
        ResultSet rs = null;

        stmt = con.prepareStatement("DELETE FROM groups WHERE group_id = ?");
        stmt.setInt(1, groupId);
        stmt.execute();
    }

    public void insertGroup(Group group)throws SQLException{
        PreparedStatement stmt = null;
        ResultSet rs = null;

        stmt = con.prepareStatement("INSERT INTO groups (groupName) VALUES (?)");
        stmt.setString(1, group.getGroupName());
        stmt.execute();
    }

    public void updateGroup(Group group)throws SQLException{
        PreparedStatement stmt = null;
        ResultSet rs = null;

        stmt = con.prepareStatement("UPDATE groups SET groupName = ? WHERE group_id = ?");
        stmt.setString(1, group.getGroupName());
        stmt.setInt(2, group.getGroup_id());
        stmt.execute();
    }

    public Collection<Item> getAllItems() throws SQLException {
        Collection<Item> items = new ArrayList<Item>();

        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT item_id, itemName, changeDate, group_id, in_stock, sold FROM items " +
                            "ORDER BY item_id");
            while (rs.next()) {
                Item item = new Item(rs);
                items.add(item);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
        return items;
    }

    public Collection<Item> getItemsFromGroup(Group group) throws SQLException {
        Collection<Item> items = new ArrayList<Item>();

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement("SELECT item_id, itemName, changeDate, group_id, in_stock, sold FROM items " +
                            "WHERE group_id= ? " +
                            "ORDER BY item_id");
            stmt.setInt(1, group.getGroup_id());

            rs = stmt.executeQuery();
            while (rs.next()) {
                Item item = new Item(rs);
                items.add(item);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }

        return items;
    }

    public Item getItemByName(String itemName) throws SQLException {
        Item item = null;
        PreparedStatement stmt = con.prepareStatement("SELECT item_id, itemName, changeDate, group_id, in_stock, sold FROM items WHERE itemName = ?");
        stmt.setString(1, itemName);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            item = new Item(rs);
        }
        rs.close();
        stmt.close();
        return item;
    }

    public void moveItemsToGroup(Item item, Group newGroup) throws SQLException {
        PreparedStatement stmt = con.prepareStatement("UPDATE items SET group_id = ? "
                + "WHERE item_id = ? ");
        stmt.setInt(1, newGroup.getGroup_id());
        stmt.setInt(2, item.getItemId());
        stmt.execute();
    }

    public void removeItemsFromGroup(Group group) throws SQLException {
        PreparedStatement stmt = con.prepareStatement("DELETE FROM items WHERE group_id = ?");
        stmt.setInt(1, group.getGroup_id());
        stmt.execute();
    }

    public void insertItem(Item item) throws SQLException {
        PreparedStatement stmt = con.prepareStatement("INSERT INTO items "
                + "(itemName, changeDate, group_id, in_stock, sold)"
                + "VALUES(?, ?, ?, ?, ?)");
        stmt.setString(1, item.getItemName());
        stmt.setDate(2, new Date(item.getChangeDate().getTime()));
        stmt.setInt(3, item.getGroupId());
        stmt.setInt(4, item.getIn_stock());
        stmt.setInt(5, item.getSold());
       // stmt.setBigDecimal(6, item.getPrice());
        stmt.execute();
    }

    public void updateItem(Item item) throws SQLException {
        PreparedStatement stmt = con.prepareStatement("UPDATE items "
                + "SET itemName=?, changeDate=?, group_id=? , in_stock=?, sold=? WHERE item_id=?");

        stmt.setString(1, item.getItemName());
        stmt.setDate(2, new Date(item.getChangeDate().getTime()));
        stmt.setInt(3, item.getGroupId());
        stmt.setInt(4, item.getIn_stock());
        stmt.setInt(5, item.getSold());
      //  stmt.setBigDecimal(6, item.getPrice());
        stmt.setInt(6, item.getItemId());
        stmt.execute();
    }

    public void deleteItem(Item item) throws SQLException {
        PreparedStatement stmt = con.prepareStatement("DELETE FROM items WHERE item_id = ?");
        stmt.setInt(1, item.getItemId());
        stmt.execute();
    }

    public Collection<Item> searchItemsByName (String itemName)throws SQLException{
        Collection<Item> items = new ArrayList<Item>();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.prepareStatement("SELECT item_id, itemName, changeDate, group_id, in_stock, sold FROM items " +
                    "WHERE itemName= ? " +
                    "ORDER BY item_id");
            stmt.setString(1, itemName);

            rs = stmt.executeQuery();
            while (rs.next()) {
                Item item = new Item(rs);
                items.add(item);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }

        return items;
    }

    public Collection<Item> searchItemsByCount (int from, int to)throws SQLException{
        Collection<Item> items = new ArrayList<Item>();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.prepareStatement("SELECT DISTINCT item_id, itemName, changeDate, group_id, in_stock, sold FROM items " +
                    "WHERE in_stock BETWEEN ? AND ? ");
            stmt.setInt(1, from);
            stmt.setInt(2, to);

            rs = stmt.executeQuery();
            while (rs.next()) {
                Item item = new Item(rs);
                items.add(item);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }

        return items;
    }

    public Collection<Item> searchItemsByDate (java.util.Date from, java.util.Date to)throws SQLException{
        Collection<Item> items = new ArrayList<Item>();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.prepareStatement("SELECT DISTINCT item_id, itemName, changeDate, group_id, in_stock, sold FROM items " +
                    "WHERE changeDate BETWEEN ? AND ? ");
            stmt.setDate(1, new java.sql.Date(from.getTime()));
            stmt.setDate(2, new java.sql.Date(to.getTime()));

            rs = stmt.executeQuery();
            while (rs.next()) {
                Item item = new Item(rs);
                items.add(item);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }

        return items;
    }

    public BigDecimal getRateValByName(String name)throws SQLException{
        ResultSet rs = null;
        PreparedStatement stmt = null;
        BigDecimal value = new BigDecimal(0);
        value.setScale(2,BigDecimal.ROUND_HALF_UP);

        stmt = con.prepareStatement("SELECT currency_val FROM rates WHERE currency_name=? ");
        stmt.setString(1, name);
        rs = stmt.executeQuery();
        while (rs.next()){
            value = new BigDecimal(rs.getBigDecimal(1).toString());
        }
        return value;
    }

    public void setRateVal(String name,BigDecimal value)throws SQLException{
        PreparedStatement stmt = null;

        stmt = con.prepareStatement("UPDATE rates SET currency_val=? WHERE currency_name=?");
        stmt.setBigDecimal(1,value);
        stmt.setString(2,name);
        stmt.execute();
    }


    public static void makeDB() throws SQLException{
        String query;

        Statement stmt = con.createStatement();

        query = "CREATE database details DEFAULT CHARACTER SET utf8 ";
        stmt.executeUpdate(query);
        query = "use details";
        stmt.executeUpdate(query);
        query = "CREATE TABLE groups( " +
                "group_id int UNSIGNED not null AUTO_INCREMENT, " +
                "groupName varchar(255) not null, " +
                "primary key (group_id)" +
                ") engine=InnoDB";
        stmt.executeUpdate(query);
        query = "CREATE TABLE items " +
                "(" +
                "item_id int unsigned not null auto_increment, " +
                "itemName varchar(255) not null, " +
                "changeDate date not null, " +
                "group_id int not null, " +
                "in_stock int not null, " +
                "sold int not null, " +
               // "price decimal(7,2), " +
                "primary key (item_id)" +
                ") engine=InnoDB";
        stmt.executeUpdate(query);
        query = "CREATE TABLE rates " +
                "(" +
                "currency_id int unsigned not null auto_increment, " +
                "currency_name varchar(255) not null, " +
                "currency_val decimal(7,2), " +
                "PRIMARY KEY (currency_id)" +
                ") engine=InnoDB";
        stmt.execute(query);
        query = "set names utf8";
        stmt.execute(query);
        query = "insert into groups (groupName) values ('Первая')";
        stmt.executeUpdate(query);
        query = "insert into groups (groupName) values ('Вторая')";
        stmt.executeUpdate(query);
        query = "insert into items (itemName, changeDate, group_id, in_stock, sold) " +
                "values ('Деталь 1', '1990-01-01', 1, 10, 10)";
        stmt.executeUpdate(query);
        query = "insert into items (itemName, changeDate, group_id, in_stock, sold) " +
                "values ('Деталь 2', '1990-01-01', 1, 10, 10)";
        stmt.executeUpdate(query);
        query = "insert into rates (currency_name, currency_val) " +
                "values ('dollar', 25.38)";
        stmt.executeUpdate(query);
        query = "insert into rates (currency_name, currency_val) " +
                "values ('euro', 28.98)";
        stmt.executeUpdate(query);

    }

/*   public static void main(String[] args) throws Exception {
        ManagementSystem ms = ManagementSystem.getInstance();
        ms.getRateValByName("dollar");
    }*/

}


