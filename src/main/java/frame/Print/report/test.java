package frame.Print.report;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created on 14.05.2016
 */
public class test {
    public static void main(String[] args) throws FileNotFoundException {
        BigDecimal bd = new BigDecimal(0);
        bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        bd = new BigDecimal("105346.12");
        BigInteger decimal = bd.remainder(BigDecimal.ONE).movePointRight(bd.scale()).abs().toBigInteger();
        StringBuilder sb = new StringBuilder("одна гривня тисяча сто сiмдесят п'ять гривень");
        StringBuilder sb1 = new StringBuilder("сiмдесят чотири гривнi тисячi");
        String s = sb.toString();
        sb = new StringBuilder(s.replaceAll("\\bгрив.*?\\b","").trim());
        InputStream my_banner_image = test.class.getClass().getResourceAsStream("/resources/1.jpg");
        System.out.println();

    }

}
