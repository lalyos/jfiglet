package com.github.lalyos.jfiglet;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SmushingRulesTest {

    @Test
    public void hRule1_SmushTest() {
        final Character expectedValid = 'a';
        assertEquals(expectedValid, SmushingRules.hRule1_Smush('a','a' ,'@'));
        assertNull(SmushingRules.hRule1_Smush('a','b' ,'@'));
        assertNull(SmushingRules.hRule1_Smush('@', '@', '@'));
    }

    @Test
    public void hRule2_SmushTest() {
        char[] validChars = "|/\\[]{}()<>".toCharArray();
        for (final Character c : validChars){
            final Character actual = SmushingRules.hRule2_Smush('_', c);
            assertEquals(c, actual);
        }
        for (final Character c : validChars){
            final Character actual = SmushingRules.hRule2_Smush(c,'_');
            assertEquals(c, actual);
        }

        assertNull(SmushingRules.hRule2_Smush('a','_'));
        assertNull(SmushingRules.hRule2_Smush('_','a'));
        assertNull(SmushingRules.hRule2_Smush('a','a'));

    }

    @Test
    public void hRule3_SmushTest() {
        char[] pairs = "||/\\[]{}()<>".toCharArray();
        //Check values in same class return null
        for (int i=0; i < pairs.length; i=i+2){
            assertNull(SmushingRules.hRule3_Smush(pairs[i], pairs[i+1]));
        }

        for (int i=3; i < pairs.length; i++){
            final Character expected = pairs[i];
            assertEquals(expected, SmushingRules.hRule3_Smush(pairs[i], pairs[i-3]));
        }
        for (int i=3; i < pairs.length; i++){
            final Character expected = pairs[i];
            assertEquals(expected, SmushingRules.hRule3_Smush(pairs[i], pairs[i-2]));
        }
        assertNull(SmushingRules.hRule3_Smush('a','|'));
        assertNull(SmushingRules.hRule3_Smush('|','a'));
    }

    @Test
    public void hRule4_SmushTest() {
        final Character expected = '|';
        assertEquals(expected,SmushingRules.hRule4_Smush('(',')'));
        assertEquals(expected,SmushingRules.hRule4_Smush(')','('));
        assertEquals(expected,SmushingRules.hRule4_Smush('{','}'));
        assertEquals(expected,SmushingRules.hRule4_Smush('}','{'));
        assertEquals(expected,SmushingRules.hRule4_Smush('[',']'));
        assertEquals(expected,SmushingRules.hRule4_Smush(']','['));

        assertNull(SmushingRules.hRule4_Smush('{','['));
        assertNull(SmushingRules.hRule4_Smush('{','{'));
        assertNull(SmushingRules.hRule4_Smush('{', 'a'));
        assertNull(SmushingRules.hRule4_Smush('a', 'a'));
    }

    @Test
    public void hRule5_SmushTest() {
        final Character actualPipe = SmushingRules.hRule5_Smush('/','\\');
        final Character expectedPipe = '|';
        assertEquals(expectedPipe,actualPipe);
        final Character actualY = SmushingRules.hRule5_Smush('\\','/');
        final Character expectedY = 'Y';
        assertEquals(expectedY,actualY);
        final Character actualX = SmushingRules.hRule5_Smush('>','<');
        final Character expectedX = 'X';
        assertEquals(expectedX,actualX);
        final Character actualNull = SmushingRules.hRule5_Smush('/','<');
        assertNull(actualNull);
        final Character actualNull2 = SmushingRules.hRule5_Smush('<','>');
        assertNull(actualNull2);
    }

    @Test
    public void hRule6_SmushTest() {
        final Character hardBlank = '@';
        final Character actual = SmushingRules.hRule6_Smush(hardBlank,hardBlank,hardBlank);
        assertEquals(hardBlank, actual);
        final Character actualNull1 = SmushingRules.hRule6_Smush(hardBlank,'a',hardBlank);
        assertNull(actualNull1);
        final Character actualNull2 = SmushingRules.hRule6_Smush(hardBlank,hardBlank,'a');
        assertNull(actualNull2);
        final Character actualNull3 = SmushingRules.hRule6_Smush('a',hardBlank,hardBlank);
        assertNull(actualNull3);

    }

    @Test
    public void vRule1_SmushTest() {
        final Character actualValid = SmushingRules.vRule1_Smush('a', 'a');
        final Character expectedValid = 'a';
        assertEquals(expectedValid, actualValid);
        final Character actualNull = SmushingRules.vRule1_Smush('a', 'b');
        assertNull(actualNull);
    }

    @Test
    public void vRule2_SmushTest() {
        char[] validChars = "|/\\[]{}()<>".toCharArray();
        for (final Character c : validChars){
            final Character actual = SmushingRules.vRule2_Smush('_', c);
            assertEquals(c, actual);
        }
        for (final Character c : validChars){
            final Character actual = SmushingRules.vRule2_Smush(c, '_');
            assertEquals(c, actual);
        }

        assertNull(SmushingRules.vRule2_Smush('a', '_'));
        assertNull(SmushingRules.vRule2_Smush('_', 'a'));

    }

    @Test
    public void vRule3_SmushTest() {
        char[] pairs = "||/\\[]{}()<>".toCharArray();
        //Check values in same class return null
        for (int i=0; i < pairs.length; i=i+2){
            assertNull(SmushingRules.vRule3_Smush(pairs[i], pairs[i + 1]));
        }

        for (int i=3; i < pairs.length; i++){
            final Character expected = pairs[i];
            assertEquals(expected, SmushingRules.vRule3_Smush(pairs[i], pairs[i - 3]));
        }
        for (int i=3; i < pairs.length; i++){
            final Character expected = pairs[i];
            assertEquals(expected, SmushingRules.vRule3_Smush(pairs[i], pairs[i - 2]));
        }
        assertNull(SmushingRules.vRule3_Smush('a', '|'));
        assertNull(SmushingRules.vRule3_Smush('|', 'a'));
    }
    @Test
    public void vRule4_SmushTest(){
        final Character expected = '=';
        assertEquals(expected,SmushingRules.vRule4_Smush('-', '_'));
        assertEquals(expected,SmushingRules.vRule4_Smush('_', '-'));
        assertNull(SmushingRules.vRule4_Smush('a', '_'));
        assertNull(SmushingRules.vRule4_Smush('a', '-'));
        assertNull(SmushingRules.vRule4_Smush('-', 'a'));
        assertNull(SmushingRules.vRule4_Smush('_', 'a'));
    }
    @Test
    public void vRule5_SmushTest(){
        final Character expected = '|';
        assertEquals(expected,SmushingRules.vRule5_Smush('|', '|'));
        assertNull(SmushingRules.vRule5_Smush('|', 'a'));
        assertNull(SmushingRules.vRule5_Smush('a', '|'));
        assertNull(SmushingRules.vRule5_Smush('a', 'a'));
    }

    @Test
    public void uni_smushTest(){
        final Character expected = 'a';
        assertEquals(expected,SmushingRules.uni_Smush('a', ' ', '@'));
        assertEquals(expected,SmushingRules.uni_Smush('a', '@', '@'));
        assertEquals(expected,SmushingRules.uni_Smush(' ', '@', '@'));
        assertEquals(expected,SmushingRules.uni_Smush('b', 'a', '@'));
    }
}

