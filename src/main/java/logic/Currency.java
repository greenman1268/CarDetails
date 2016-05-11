package logic;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created on 11.05.2016
 */
public class Currency {

    private String name;
    private BigDecimal value;

    public Currency(ResultSet rs) throws SQLException {
        setName(rs.getString(1));
        setValue(rs.getBigDecimal(2));
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }


    public String getName() {
        return name;
    }

    public BigDecimal getValue() {
        return value;
    }
}
