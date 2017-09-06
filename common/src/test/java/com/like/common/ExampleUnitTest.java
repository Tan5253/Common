package com.like.common;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
        System.out.println(verifyPhoneNumber("17311114444"));
    }

    public static boolean verifyPhoneNumber(String phoneNumber) {
        boolean result = false;
        if (phoneNumber == null || phoneNumber.length() <=0) {
            return false;
        }
        String reg = "(^1[3,4,5,7,8]\\d{9}$)";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(phoneNumber);
        result = m.matches();
        return result;
    }
}