package logic;

/**
 * Created on 27.04.2016
 */

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
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
                mysqlDS.setURL("jdbc:mysql://localhost:3306/details");
                mysqlDS.setUser("root");
                mysqlDS.setPassword("126874539");
                instance.dataSource = mysqlDS;
                con =  dataSource.getConnection();

            }catch (SQLException e) {
                e.printStackTrace();
            }}
        return instance;
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

    public Collection<Item> getAllItems() throws SQLException {
        Collection<Item> items = new ArrayList<Item>();

        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT item_id, itemName, changeDate, group_id FROM items " +
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
            stmt = con.prepareStatement("SELECT item_id, itemName, changeDate, group_id FROM items " +
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

    public Item getItemById(int itemId) throws SQLException {
        Item item = null;
        PreparedStatement stmt = con.prepareStatement("SELECT item_id, itemName, changeDate, group_id FROM items WHERE item_id = ?");
        stmt.setInt(1, itemId);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            item = new Item(rs);
            //System.out.println(person);
        }
        rs.close();
        stmt.close();
        return item;
    }

    public void moveItemsToGroup(Group oldGroup, Group newGroup) throws SQLException {
        PreparedStatement stmt = con.prepareStatement("UPDATE items SET group_id = ? "
                + "WHERE group_id = ? ");
        stmt.setInt(1, newGroup.getGroup_id());
        stmt.setInt(2, oldGroup.getGroup_id());
        stmt.execute();
    }

    public void removeItemsFromGroup(Group group, String year) throws SQLException {
        PreparedStatement stmt = con.prepareStatement("DELETE FROM items WHERE group_id = ? AND (YEAR(changeDate)) = ?");
        stmt.setInt(1, group.getGroup_id());
        stmt.setString(2, year);
        stmt.execute();
    }

    public void insertItem(Item item) throws SQLException {
        PreparedStatement stmt = con.prepareStatement("INSERT INTO items "
                + "(item_id, itemName, changeDate, group_id)"
                + "VALUES( ?, ?, ?, ?)");
        stmt.setInt(1, item.getItemId());
        stmt.setString(2, item.getItemName());
        stmt.setDate(3, new Date(item.getChangeDate().getTime()));
        stmt.setInt(4, item.getGroupId());
        stmt.execute();
    }

    public void updateItem(Item item) throws SQLException {
        PreparedStatement stmt = con.prepareStatement("UPDATE items "
                + "SET itemName=?, changeDate=?, group_id=? WHERE item_id=?");

        stmt.setString(1, item.getItemName());
        stmt.setDate(2, new Date(item.getChangeDate().getTime()));
        stmt.setInt(3, item.getGroupId());
        stmt.execute();
    }

    public void deleteItem(Item item) throws SQLException {
        PreparedStatement stmt = con.prepareStatement("DELETE FROM items WHERE item_id = ?");
        stmt.setInt(1, item.getItemId());
        stmt.execute();
    }

}
