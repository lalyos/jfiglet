package com.github.lalyos.jfiglet;

import java.util.*;

enum SmushingRule implements Comparable<SmushingRule> {

  /*
   * Rule 1: EQUAL CHARACTER SMUSHING (code value 1)
   *
   * Two sub-characters are smushed into a single sub-character
   * if they are the same.  This rule does not smushHorizontal
   * hardblanks.  (See "Hardblanks" below.)
   */
  HORIZONTAL_EQUAL_CHARACTER_SMUSHING(Type.HORIZONTAL, Layout.CONTROLLED_SMUSHING, 1){
    @Override
    Character smush(char char1, char char2, char hardblank) {
      return char1 != hardblank && char1 == char2 ? char1 : null;
    }
  },


  /*
   * Rule 2: UNDERSCORE SMUSHING (code value 2)
   *
   * An underscore ("_") will be replaced by any of: "|", "/",
   * "\", "[", "]", "{", "}", "(", ")", "<" or ">".
   */
  HORIZONTAL_UNDERSCORE_SMUSHING(Type.HORIZONTAL, Layout.CONTROLLED_SMUSHING, 2) {
    @Override
    Character smush(char char1, char char2, char hardblank) {
      final String chars = "|/\\[]{}()<>";
      if (char1 == '_' && chars.indexOf(char2) != -1) {
        return char2;
      } else if (char2 == '_' && chars.indexOf(char1) != -1) {
        return char1;
      } else {
        return null;
      }
    }
  },

  /*
   * Rule 3: HIERARCHY SMUSHING (code value 4)
   *
   * A hierarchy of six classes is used: "|", "/\", "[]", "{}",
   * "()", and "<>".  When two smushing sub-characters are
   * from different classes, the one from the latter class
   * will be used.
   */
  HORIZONTAL_HIERARCHY_SMUSHING(Type.HORIZONTAL, Layout.CONTROLLED_SMUSHING, 4) {
    @Override
    Character smush(char char1, char char2, char hardblank) {
      String classes = "| /\\ [] {} () <>";
      int pos1 = classes.indexOf(char1);
      int pos2 = classes.indexOf(char2);
      if (pos1 != -1 && pos2 != -1) {
        if (pos1 != pos2 && Math.abs(pos1-pos2) != 1) {
          return classes.substring(Math.max(pos1,pos2), Math.max(pos1,pos2) + 1).charAt(0);
        }
      }
      return null;
    }
  },

  /*
   * Rule 4: OPPOSITE PAIR SMUSHING (code value 8)
   * Smushes opposing brackets ("[]" or "]["), braces ("{}" or
   * "}{") and parentheses ("()" or ")(") together, replacing
   * any such pair with a vertical bar ("|").
   */
  HORIZONTAL_OPPOSITE_PAIR_SMUSHING(Type.HORIZONTAL, Layout.CONTROLLED_SMUSHING, 8) {
    @Override
    Character smush(char char1, char char2, char hardblank) {
      String opposingBrackets = "[] {} ()";
      int pos1 = opposingBrackets.indexOf(char1);
      int pos2 = opposingBrackets.indexOf(char2);
      if (pos1 != -1 && pos2 != -1) {
        if (pos1 != pos2 && Math.abs(pos1-pos2) == 1) {
          return '|';
        }
      }
      return null;
    }
  },

  /*
   * Rule 5: BIG X SMUSHING (code value 16)
   *
   * Smushes "/\" into "|", "\/" into "Y", and "><" into "X".
   * Note that "<>" is not smushed in any way by this rule.
   * The name "BIG X" is historical; originally all three pairs
   * were smushed into "X".
   */
  HORIZONTAL_BIG_X_SMUSHING(Type.HORIZONTAL, Layout.CONTROLLED_SMUSHING, 16) {
    @Override
    Character smush(char char1, char char2, char hardblank) {
      if(char1 == '/' && char2 == "\\".charAt(0)){
        return '|';
      } else if (char1 == "\\".charAt(0) && char2 == '/') {
        return 'Y';
      } else if (char1 == '>' && char2 == '<') {
        return 'X';
      } else {
        return null;
      }
    }
  },

  /*
   * Rule 6: HARDBLANK SMUSHING (code value 32)
   *
   * Smushes two hardblanks together, replacing them with a
   * single hardblank.  (See "Hardblanks" below.)
   */
  HORIZONTAL_HARDBLANK_SMUSHING(Type.HORIZONTAL, Layout.CONTROLLED_SMUSHING, 32) {
    @Override
    Character smush(char char1, char char2, char hardblank) {
       return char1 == hardblank && char2 == hardblank ? hardblank : null;
    }
  },

  /*
   * HORIZONTAL FITTING (code value 64)
   *
   * Moves FIGcharacters closer together until they touch.
   * Typographers use the term "kerning" for this phenomenon
   * when applied to the horizontal axis, but fitting also
   * includes this as a vertical behavior, for which there is
   * apparently no established typographical term.
   */
  HORIZONTAL_FITTING(Type.HORIZONTAL, Layout.FITTING, 64) {
    @Override
    Character smush(char char1, char char2, char hardblank) {
      return char1 == ' ' && char2 == ' ' ? ' ' : null;
    }
  },

  /*
   * HORIZONTAL SMUSHING (code value 128)
   *
   * Universal smushing simply overrides the sub-character from the
   * earlier FIGcharacter with the sub-character from the later
   * FIGcharacter.  This produces an "overlapping" effect with some
   * FIGfonts, wherin the latter FIGcharacter may appear to be "in
   * front".
   */
  HORIZONTAL_SMUSHING(Type.HORIZONTAL, Layout.SMUSHING, 128) {
    @Override
    Character smush(char char1, char char2, char hardblank) {
      return char1 != hardblank && char2 != hardblank ? char2 : null;
    }
  },

  /*
   * Rule 1: EQUAL CHARACTER SMUSHING (code value 256)
   * Same as horizontal smushing rule 1.
   */
  VERTICAL_EQUAL_CHARACTER_SMUSHING(Type.VERTICAL, Layout.CONTROLLED_SMUSHING, 256),

  /*
   * Rule 2: UNDERSCORE SMUSHING (code value 512)
   * Same as horizontal smushing rule 2.
  */
  VERTICAL_UNDERSCORE_SMUSHING(Type.VERTICAL, Layout.CONTROLLED_SMUSHING, 512),

  /*
   * Rule 3: HIERARCHY SMUSHING (code value 1024)
   * Same as horizontal smushing rule 3.
   */
  VERTICAL_HIERARCHY_SMUSHING(Type.VERTICAL, Layout.CONTROLLED_SMUSHING, 1024),

  /*
   * Rule 4: HORIZONTAL LINE SMUSHING (code value 2048)
   * Smushes stacked pairs of "-" and "_", replacing them with
   * a single "=" sub-character.  It does not matter which is
   * found above the other.  Note that vertical smushing rule 1
   * will smushHorizontal IDENTICAL pairs of horizontal lines, while this
   * rule smushes horizontal lines consisting of DIFFERENT
   * sub-characters.
   */
  VERTICAL_HORIZONTAL_LINE_SMUSHING(Type.VERTICAL, Layout.CONTROLLED_SMUSHING, 2048),

  /*
   * Rule 5: VERTICAL LINE SUPERSMUSHING (code value 4096)
   * This one rule is different from all others, in that it
   * "supersmushes" vertical lines consisting of several
   * vertical bars ("|").  This creates the illusion that
   * FIGcharacters have slid vertically against each other.
   * Supersmushing continues until any sub-characters other
   * than "|" would have to be smushed.  Supersmushing can
   * produce impressive results, but it is seldom possible,
   * since other sub-characters would usually have to be
   * considered for smushing as soon as any such stacked
   * vertical lines are encountered.
   */
  VERTICAL_VERTICAL_LINE_SMUSHING(Type.VERTICAL, Layout.CONTROLLED_SMUSHING, 4096),

  /*
   * VERTICAL FITTING (code value 8192)
   */
  VERTICAL_FITTING(Type.VERTICAL, Layout.FITTING, 8192),

  /*
   * VERTICAL SMUSHING (code value 16384)
   */
  VERTICAL_SMUSHING(Type.VERTICAL, Layout.SMUSHING, 16384)
  ;

  private final Type type;
  private final Layout layout;
  private final int codeValue;


  enum Type {HORIZONTAL, VERTICAL}

  enum Layout{FULL_WIDTH, FITTING, SMUSHING, CONTROLLED_SMUSHING}

  private static final Map<Integer, SmushingRule> codeValueToRule;

  static {
    Map<Integer, SmushingRule> results = new HashMap<Integer, SmushingRule>();
    for (SmushingRule rule : SmushingRule.values()){
      results.put(rule.getCodeValue(), rule);
    }
    codeValueToRule = Collections.unmodifiableMap(results);
  }


  SmushingRule(Type type, Layout layout, int codeValue){
    this.type = type;
    this.layout = layout;
    this.codeValue = codeValue;
  }

  Character smush(char char1, char char2, char hardblank) {
    return null;
  }

  boolean smushes(char char1, char char2, char hardblank) {
    return smush(char1, char2, hardblank) != null;
  }

  int getCodeValue() {
    return codeValue;
  }

  Type getType() {
    return type;
  }

  Layout getLayout() {
    return layout;
  }

  static SmushingRule getByCodeValue(int codeValue){
    return codeValueToRule.get(codeValue);
  }

  static List<Integer> getAvailableCodeValues(){
    List<Integer> list = new ArrayList<Integer>(codeValueToRule.keySet());
    Collections.sort(list);
    Collections.reverse(list);
    return Collections.unmodifiableList(list);
  }
}

