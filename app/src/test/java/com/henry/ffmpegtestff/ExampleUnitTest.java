package com.henry.ffmpegtestff;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {

        String a = "https://www?a=3b=4";
        String[] split = a.split("\\?");
        String url=a.substring(0,a.lastIndexOf("?"));
        System.out.println(url);
//        assertEquals(4, 2 + 2);
    }
}