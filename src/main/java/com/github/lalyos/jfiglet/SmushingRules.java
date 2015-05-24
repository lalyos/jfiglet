package com.github.lalyos.jfiglet;

import java.util.HashMap;
import java.util.Map;

public class SmushingRules {
    
    private  SmushingRules(){
        
    }
    /*
    Rule 1: EQUAL CHARACTER SMUSHING (code value 1)

    Two sub-characters are smushed into a single sub-character
    if they are the same.  This rule does not smush
    hardblanks.  (See rule 6 on hardblanks below)
*/
    protected static  Character hRule1_Smush(char ch1, char ch2, char hardBlank) {
        if (ch1 == ch2 && ch1 != hardBlank) {
            return ch1;
        }
        return null;
    }

    /*
        Rule 2: UNDERSCORE SMUSHING (code value 2)

        An underscore ("_") will be replaced by any of: "|", "/",
        "\", "[", "]", "{", "}", "(", ")", "<" or ">".
    */
    protected static  Character hRule2_Smush(char ch1, char ch2) {
        String rule2Str = "|/\\[]{}()<>";
        if (ch1 == '_') {
            if (rule2Str.contains(String.valueOf(ch2))) {
                return ch2;
            }
        } else if (ch2 == '_') {
            if (rule2Str.contains(String.valueOf(ch1))) {
                return ch1;
            }
        }
        return null;
    }

    /*
        Rule 3: HIERARCHY SMUSHING (code value 4)

        A hierarchy of six classes is used: "|", "/\", "[]", "{}",
        "()", and "<>".  When two smushing sub-characters are
        from different classes, the one from the latter class
        will be used.
    */
    protected static  Character hRule3_Smush(char ch1, char ch2) {
        String rule3Classes = "||/\\[]{}()<>";
        int r3_pos1 = rule3Classes.indexOf(ch1);
        int r3_pos2 = rule3Classes.indexOf(ch2);
        if (r3_pos1 != -1 && r3_pos2 != -1) {
            if (r3_pos1 != r3_pos2 && Math.abs(r3_pos1 - r3_pos2) != 1) {
                return rule3Classes.substring(Math.max(r3_pos1, r3_pos2)).charAt(0);
            }
        }
        return null;
    }

    /*
        Rule 4: OPPOSITE PAIR SMUSHING (code value 8)

        Smushes opposing brackets ("[]" or "]["), braces ("{}" or
        "}{") and parentheses ("()" or ")(") together, replacing
        any such pair with a vertical bar ("|").
    */
    protected static  Character hRule4_Smush(char ch1, char ch2) {
        String rule4Str = "[]{}()";
        int r4_pos1 = rule4Str.indexOf(ch1);
        int r4_pos2 = rule4Str.indexOf(ch2);
        if (r4_pos1 != -1 && r4_pos2 != -1) {
            if (r4_pos1 != r4_pos2 &&Math.abs(r4_pos1 - r4_pos2) <= 1) {
                return '|';
            }
        }
        return null;
    }

    /*
        Rule 5: BIG X SMUSHING (code value 16)

        Smushes "/\" into "|", "\/" into "Y", and "><" into "X".
        Note that "<>" is not smushed in any way by this rule.
        The name "BIG X" is historical; originally all three pairs
        were smushed into "X".
    */
    protected static  Character hRule5_Smush(char ch1, char ch2) {
        String rule5Str = "/\\ \\/ ><";
        Map<Integer, Character> rule5Hash = new HashMap<Integer, Character>();
        rule5Hash.put(0, '|');
        rule5Hash.put(3, 'Y');
        rule5Hash.put(6, 'X');
        int r5_pos = rule5Str.indexOf(String.valueOf(ch1) + String.valueOf(ch2));
        if (r5_pos != -1) {
            return rule5Hash.get(r5_pos);
        }
        return null;
    }

    /*
        Rule 6: HARDBLANK SMUSHING (code value 32)

        Smushes two hardblanks together, replacing them with a
        single hardblank.  (See "Hardblanks" below.)
    */
    protected static  Character hRule6_Smush(char ch1, char ch2, char hardBlank) {
        if (ch1 == hardBlank && ch2 == hardBlank) {
            return hardBlank;
        }
        return null;
    }

    /*
        Rule 1: EQUAL CHARACTER SMUSHING (code value 256)

            Same as horizontal smushing rule 1.
    */
    protected static  Character vRule1_Smush(char ch1, char ch2) {
        if (ch1 == ch2 ) {
            return ch1;
        }
        return null;
    }

    /*
        Rule 2: UNDERSCORE SMUSHING (code value 512)

        Same as horizontal smushing rule 2.
    */
    protected static  Character vRule2_Smush(char ch1, char ch2) {
        return hRule2_Smush(ch1,ch2);
    }

    /*
        Rule 3: HIERARCHY SMUSHING (code value 1024)

            Same as horizontal smushing rule 3.
    */
    protected static  Character vRule3_Smush(char ch1, char ch2) {
        return hRule3_Smush(ch1,ch2);
    }

    /*
        Rule 4: HORIZONTAL LINE SMUSHING (code value 2048)

        Smushes stacked pairs of "-" and "_", replacing them with
        a single "=" sub-character.  It does not matter which is
        found above the other.  Note that vertical smushing rule 1
        will smush IDENTICAL pairs of horizontal lines, while this
        rule smushes horizontal lines consisting of DIFFERENT
        sub-characters.
    */
    protected static  Character vRule4_Smush(char ch1, char ch2) {
        if ((ch1 == '-' && ch2 == '_') || (ch1 == '_' && ch2 == '-')) {
            return '=';
        }
        return null;
    }

    /*
        Rule 5: VERTICAL LINE SUPERSMUSHING (code value 4096)

        This one rule is different from all others, in that it
        "supersmushes" vertical lines consisting of several
        vertical bars ("|").  This creates the illusion that
        FIGcharacters have slid vertically against each other.
        Supersmushing continues until any sub-characters other
        than "|" would have to be smushed.  Supersmushing can
        produce impressive results, but it is seldom possible,
        since other sub-characters would usually have to be
        considered for smushing as soon as any such stacked
        vertical lines are encountered.
    */
    protected static  Character vRule5_Smush(char ch1, char ch2) {
        if (ch1 == '|' && ch2 == '|') {
            return '|';
        }
        return null;
    }

    /*
        Universal smushing simply overrides the sub-character from the
        earlier FIGcharacter with the sub-character from the later
        FIGcharacter.  This produces an "overlapping" effect with some
        FIGfonts, wherin the latter FIGcharacter may appear to be "in
        front".
    */

    protected static  Character uni_Smush(char ch1, char ch2, char hardBlank) {
        if (ch2 == ' ') {
            return ch1;
        } else if (ch2 == hardBlank && ch1 != ' ') {
            return ch1;
        } else {
            return ch2;
        }
    }

}
