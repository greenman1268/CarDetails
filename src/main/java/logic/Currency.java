package logic;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created on 11.05.2016
 */
public class Currency {

    private int currency_id;
    private String name;
    private BigDecimal value;

    public Currency(ResultSet rs) throws SQLException {
        setCurrency_id(rs.getInt(1));
        setName(rs.getString(2));
        setValue(rs.getBigDecimal(3));
    }
    public void setCurrency_id(int id){ this.currency_id = id;}

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public int getCurrency_id(){ return currency_id;}

    public String getName() {
        return name;
    }

    public BigDecimal getValue() {
        return value;
    }

    public String toString(){return name;}
}
