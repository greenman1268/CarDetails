package frame.Print.report;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created on 14.05.2016
 */
public class test {
    public static void main(String[] args) {
        BigDecimal bd = new BigDecimal(0);
        bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        bd = new BigDecimal("1037.6980");
        BigInteger decimal = bd.remainder(BigDecimal.ONE).movePointRight(bd.scale()).abs().toBigInteger();
        String s = "долар долари доларiв цент центи центiв";
        s = bd.toString();
        int ss = s.indexOf(".");

        System.out.println(s.substring(ss+1,s.length()));
    }
}
