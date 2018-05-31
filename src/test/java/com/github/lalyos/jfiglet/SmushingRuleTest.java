package com.github.lalyos.jfiglet;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class SmushingRuleTest {

  /*
   * Rule 1: EQUAL CHARACTER SMUSHING (code value 1)
   *
   * Two sub-characters are smushed into a single sub-character
   * if they are the same.  This rule does not smushHorizontal
   * hardblanks.  (See "Hardblanks" below.)
   */
  @Test
  public void testEqualCharacterSmushingSmush() throws Exception {
    assertEquals('a', SmushingRule.HORIZONTAL_EQUAL_CHARACTER_SMUSHING.smush( 'a', 'a', '$').charValue());
    assertNull(SmushingRule.HORIZONTAL_EQUAL_CHARACTER_SMUSHING.smush( 'a', 'b', '$'));
    assertNull(SmushingRule.HORIZONTAL_EQUAL_CHARACTER_SMUSHING.smush( '$', 'b', '$'));
    assertNull(SmushingRule.HORIZONTAL_EQUAL_CHARACTER_SMUSHING.smush( '$', '$', '$'));
  }

  @Test
  public void testEqualCharacterSmushingValue() throws Exception {
    assertEquals(SmushingRule.HORIZONTAL_EQUAL_CHARACTER_SMUSHING, SmushingRule.getByCodeValue(1));
    assertEquals(SmushingRule.HORIZONTAL_EQUAL_CHARACTER_SMUSHING, SmushingRule.valueOf("HORIZONTAL_EQUAL_CHARACTER_SMUSHING"));
  }

  /*
   * Rule 2: UNDERSCORE SMUSHING (code value 2)
   *
   * An underscore ("_") will be replaced by any of: "|", "/",
   * "\", "[", "]", "{", "}", "(", ")", "<" or ">".
   */
  @Test
  public void testUnderscoreSmushingSmush() throws Exception {
    char[] values = {'|', '/', '\\', '[', ']', '{', '}', '(', ')', '<', '>'};
    for(char s : values) {
      assertEquals(s, SmushingRule.HORIZONTAL_UNDERSCORE_SMUSHING.smush('_', s, '$').charValue());
      assertEquals(s, SmushingRule.HORIZONTAL_UNDERSCORE_SMUSHING.smush(s, '_', '$').charValue());
      assertNull(SmushingRule.HORIZONTAL_UNDERSCORE_SMUSHING.smush('a', s, '$'));
      assertNull(SmushingRule.HORIZONTAL_UNDERSCORE_SMUSHING.smush(s, 'a', '$'));
    }
    assertNull(SmushingRule.HORIZONTAL_UNDERSCORE_SMUSHING.smush( 'a', '_', '$'));
    assertNull(SmushingRule.HORIZONTAL_UNDERSCORE_SMUSHING.smush( '_', 'a', '$'));
    assertNull(SmushingRule.HORIZONTAL_UNDERSCORE_SMUSHING.smush( 'a', 'a', '$'));
    assertNull(SmushingRule.HORIZONTAL_UNDERSCORE_SMUSHING.smush( 'a', 'b', '$'));
    assertNull(SmushingRule.HORIZONTAL_UNDERSCORE_SMUSHING.smush( '$', 'b', '$'));
    assertNull(SmushingRule.HORIZONTAL_UNDERSCORE_SMUSHING.smush( 'b', '$', '$'));
    assertNull(SmushingRule.HORIZONTAL_UNDERSCORE_SMUSHING.smush( '$', '$', '$'));
  }

  @Test
  public void testUnderscoreSmushingValue() throws Exception {
    assertEquals(SmushingRule.HORIZONTAL_UNDERSCORE_SMUSHING, SmushingRule.getByCodeValue(2));
    assertEquals(SmushingRule.HORIZONTAL_UNDERSCORE_SMUSHING, SmushingRule.valueOf("HORIZONTAL_UNDERSCORE_SMUSHING"));
  }


  /*
   * Rule 3: HIERARCHY SMUSHING (code value 4)
   *
   * A hierarchy of six classes is used: "|", "/\", "[]", "{}",
   * "()", and "<>".  When two smushing sub-characters are
   * from different classes, the one from the latter class
   * will be used.
   */
  @Test
  public void testHierarchySmushingSmush() throws Exception {
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '|', '|', '$'));
    assertEquals('/', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '|', '/', '$').charValue());
    assertEquals('\\', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '|', '\\', '$').charValue());
    assertEquals('[', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '|', '[', '$').charValue());
    assertEquals(']', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '|', ']', '$').charValue());
    assertEquals('{', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '|', '{', '$').charValue());
    assertEquals('}', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '|', '}', '$').charValue());
    assertEquals('(', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '|', '(', '$').charValue());
    assertEquals(')', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '|', ')', '$').charValue());
    assertEquals('<', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '|', '<', '$').charValue());
    assertEquals('>', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '|', '>', '$').charValue());

    assertEquals('/', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '/', '|', '$').charValue());
    assertEquals('\\', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '\\', '|', '$').charValue());
    assertEquals('[', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '[', '|', '$').charValue());
    assertEquals(']', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ']', '|', '$').charValue());
    assertEquals('{', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '{', '|', '$').charValue());
    assertEquals('}', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '}', '|', '$').charValue());
    assertEquals('(', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '(', '|', '$').charValue());
    assertEquals(')', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ')', '|', '$').charValue());
    assertEquals('<', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '<', '|', '$').charValue());
    assertEquals('>', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '>', '|', '$').charValue());

    assertEquals('/', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '/', '|', '$').charValue());
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '/', '/', '$'));
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '/', '\\', '$'));
    assertEquals('[', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '/', '[', '$').charValue());
    assertEquals(']', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '/', ']', '$').charValue());
    assertEquals('{', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '/', '{', '$').charValue());
    assertEquals('}', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '/', '}', '$').charValue());
    assertEquals('(', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '/', '(', '$').charValue());
    assertEquals(')', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '/', ')', '$').charValue());
    assertEquals('<', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '/', '<', '$').charValue());
    assertEquals('>', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '/', '>', '$').charValue());

    assertEquals('/', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '|', '/', '$').charValue());
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '/', '/', '$'));
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '/', '\\', '$'));
    assertEquals('[', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '[', '/', '$').charValue());
    assertEquals(']', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ']', '/', '$').charValue());
    assertEquals('{', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '{', '/', '$').charValue());
    assertEquals('}', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '}', '/', '$').charValue());
    assertEquals('(', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '(', '/', '$').charValue());
    assertEquals(')', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ')', '/', '$').charValue());
    assertEquals('<', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '<', '/', '$').charValue());
    assertEquals('>', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '>', '/', '$').charValue());

    assertEquals('\\', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '\\', '|', '$').charValue());
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '\\', '/', '$'));
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '\\', '\\', '$'));
    assertEquals('[', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '\\', '[', '$').charValue());
    assertEquals(']', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '\\', ']', '$').charValue());
    assertEquals('{', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '\\', '{', '$').charValue());
    assertEquals('}', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '\\', '}', '$').charValue());
    assertEquals('(', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '\\', '(', '$').charValue());
    assertEquals(')', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '\\', ')', '$').charValue());
    assertEquals('<', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '\\', '<', '$').charValue());
    assertEquals('>', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '\\', '>', '$').charValue());

    assertEquals('\\', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '|', '\\', '$').charValue());
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '\\', '/', '$'));
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '\\', '\\', '$'));
    assertEquals('[', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '[', '\\', '$').charValue());
    assertEquals(']', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ']', '\\', '$').charValue());
    assertEquals('{', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '{', '\\', '$').charValue());
    assertEquals('}', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '}', '\\', '$').charValue());
    assertEquals('(', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '(', '\\', '$').charValue());
    assertEquals(')', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ')', '\\', '$').charValue());
    assertEquals('<', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '<', '\\', '$').charValue());
    assertEquals('>', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '>', '\\', '$').charValue());

    assertEquals('[', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '[', '|', '$').charValue());
    assertEquals('[', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '[', '/', '$').charValue());
    assertEquals('[', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '[', '\\', '$').charValue());
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '[', '[', '$'));
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '[', ']', '$'));
    assertEquals('{', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '[', '{', '$').charValue());
    assertEquals('}', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '[', '}', '$').charValue());
    assertEquals('(', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '[', '(', '$').charValue());
    assertEquals(')', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '[', ')', '$').charValue());
    assertEquals('<', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '[', '<', '$').charValue());
    assertEquals('>', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '[', '>', '$').charValue());

    assertEquals('[', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '|', '[', '$').charValue());
    assertEquals('[', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '/', '[', '$').charValue());
    assertEquals('[', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '\\', '[', '$').charValue());
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '[', '[', '$'));
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '[', ']', '$'));
    assertEquals('{', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '{', '[', '$').charValue());
    assertEquals('}', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '}', '[', '$').charValue());
    assertEquals('(', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '(', '[', '$').charValue());
    assertEquals(')', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ')', '[', '$').charValue());
    assertEquals('<', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '<', '[', '$').charValue());
    assertEquals('>', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '>', '[', '$').charValue());

    assertEquals(']', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ']', '|', '$').charValue());
    assertEquals(']', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ']', '/', '$').charValue());
    assertEquals(']', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ']', '\\', '$').charValue());
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ']', '[', '$'));
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ']', ']', '$'));
    assertEquals('{', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ']', '{', '$').charValue());
    assertEquals('}', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ']', '}', '$').charValue());
    assertEquals('(', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ']', '(', '$').charValue());
    assertEquals(')', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ']', ')', '$').charValue());
    assertEquals('<', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ']', '<', '$').charValue());
    assertEquals('>', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ']', '>', '$').charValue());

    assertEquals(']', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '|', ']', '$').charValue());
    assertEquals(']', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '/', ']', '$').charValue());
    assertEquals(']', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '\\', ']', '$').charValue());
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ']', '[', '$'));
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ']', ']', '$'));
    assertEquals('{', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '{', ']', '$').charValue());
    assertEquals('}', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '}', ']', '$').charValue());
    assertEquals('(', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '(', ']', '$').charValue());
    assertEquals(')', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ')', ']', '$').charValue());
    assertEquals('<', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '<', ']', '$').charValue());
    assertEquals('>', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '>', ']', '$').charValue());

    assertEquals('{', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '{', '|', '$').charValue());
    assertEquals('{', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '{', '/', '$').charValue());
    assertEquals('{', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '{', '\\', '$').charValue());
    assertEquals('{', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '{', '[', '$').charValue());
    assertEquals('{', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '{', ']', '$').charValue());
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '{', '{', '$'));
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '{', '}', '$'));
    assertEquals('(', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '{', '(', '$').charValue());
    assertEquals(')', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '{', ')', '$').charValue());
    assertEquals('<', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '{', '<', '$').charValue());
    assertEquals('>', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '{', '>', '$').charValue());

    assertEquals('{', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '|', '{', '$').charValue());
    assertEquals('{', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '/', '{', '$').charValue());
    assertEquals('{', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '\\', '{', '$').charValue());
    assertEquals('{', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '[', '{', '$').charValue());
    assertEquals('{', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ']', '{', '$').charValue());
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '{', '{', '$'));
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '{', '}', '$'));
    assertEquals('(', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '(', '{', '$').charValue());
    assertEquals(')', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ')', '{', '$').charValue());
    assertEquals('<', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '<', '{', '$').charValue());
    assertEquals('>', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '>', '{', '$').charValue());

    assertEquals('}', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '}', '|', '$').charValue());
    assertEquals('}', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '}', '/', '$').charValue());
    assertEquals('}', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '}', '\\', '$').charValue());
    assertEquals('}', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '}', '[', '$').charValue());
    assertEquals('}', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '}', ']', '$').charValue());
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '}', '{', '$'));
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '}', '}', '$'));
    assertEquals('(', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '}', '(', '$').charValue());
    assertEquals(')', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '}', ')', '$').charValue());
    assertEquals('<', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '}', '<', '$').charValue());
    assertEquals('>', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '}', '>', '$').charValue());

    assertEquals('}', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '|', '}', '$').charValue());
    assertEquals('}', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '/', '}', '$').charValue());
    assertEquals('}', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '\\', '}', '$').charValue());
    assertEquals('}', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '[', '}', '$').charValue());
    assertEquals('}', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ']', '}', '$').charValue());
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '}', '{', '$'));
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '}', '}', '$'));
    assertEquals('(', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '(', '}', '$').charValue());
    assertEquals(')', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ')', '}', '$').charValue());
    assertEquals('<', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '<', '}', '$').charValue());
    assertEquals('>', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '>', '}', '$').charValue());

    assertEquals('(', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '(', '|', '$').charValue());
    assertEquals('(', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '(', '/', '$').charValue());
    assertEquals('(', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '(', '\\', '$').charValue());
    assertEquals('(', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '(', '[', '$').charValue());
    assertEquals('(', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '(', ']', '$').charValue());
    assertEquals('(', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '(', '{', '$').charValue());
    assertEquals('(', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '(', '}', '$').charValue());
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '(', '(', '$'));
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '(', ')', '$'));
    assertEquals('<', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '(', '<', '$').charValue());
    assertEquals('>', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '(', '>', '$').charValue());

    assertEquals('(', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '|', '(', '$').charValue());
    assertEquals('(', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '/', '(', '$').charValue());
    assertEquals('(', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '\\', '(', '$').charValue());
    assertEquals('(', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '[', '(', '$').charValue());
    assertEquals('(', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ']', '(', '$').charValue());
    assertEquals('(', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '{', '(', '$').charValue());
    assertEquals('(', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '}', '(', '$').charValue());
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '(', '(', '$'));
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '(', ')', '$'));
    assertEquals('<', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '<', '(', '$').charValue());
    assertEquals('>', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '>', '(', '$').charValue());

    assertEquals(')', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ')', '|', '$').charValue());
    assertEquals(')', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ')', '/', '$').charValue());
    assertEquals(')', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ')', '\\', '$').charValue());
    assertEquals(')', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ')', '[', '$').charValue());
    assertEquals(')', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ')', ']', '$').charValue());
    assertEquals(')', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ')', '{', '$').charValue());
    assertEquals(')', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ')', '}', '$').charValue());
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ')', '(', '$'));
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ')', ')', '$'));
    assertEquals('<', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ')', '<', '$').charValue());
    assertEquals('>', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ')', '>', '$').charValue());

    assertEquals(')', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '|', ')', '$').charValue());
    assertEquals(')', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '/', ')', '$').charValue());
    assertEquals(')', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '\\', ')', '$').charValue());
    assertEquals(')', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '[', ')', '$').charValue());
    assertEquals(')', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ']', ')', '$').charValue());
    assertEquals(')', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '{', ')', '$').charValue());
    assertEquals(')', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '}', ')', '$').charValue());
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ')', '(', '$'));
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ')', ')', '$'));
    assertEquals('<', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '<', ')', '$').charValue());
    assertEquals('>', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '>', ')', '$').charValue());

    assertEquals('<', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '<', '|', '$').charValue());
    assertEquals('<', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '<', '/', '$').charValue());
    assertEquals('<', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '<', '\\', '$').charValue());
    assertEquals('<', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '<', '[', '$').charValue());
    assertEquals('<', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '<', ']', '$').charValue());
    assertEquals('<', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '<', '{', '$').charValue());
    assertEquals('<', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '<', '}', '$').charValue());
    assertEquals('<', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '<', '(', '$').charValue());
    assertEquals('<', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '<', ')', '$').charValue());
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '<', '<', '$'));
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '<', '>', '$'));

    assertEquals('<', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '|', '<', '$').charValue());
    assertEquals('<', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '/', '<', '$').charValue());
    assertEquals('<', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '\\', '<', '$').charValue());
    assertEquals('<', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '[', '<', '$').charValue());
    assertEquals('<', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ']', '<', '$').charValue());
    assertEquals('<', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '{', '<', '$').charValue());
    assertEquals('<', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '}', '<', '$').charValue());
    assertEquals('<', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '(', '<', '$').charValue());
    assertEquals('<', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ')', '<', '$').charValue());
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '<', '<', '$'));
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '<', '>', '$'));

    assertEquals('>', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '>', '|', '$').charValue());
    assertEquals('>', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '>', '/', '$').charValue());
    assertEquals('>', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '>', '\\', '$').charValue());
    assertEquals('>', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '>', '[', '$').charValue());
    assertEquals('>', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '>', ']', '$').charValue());
    assertEquals('>', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '>', '{', '$').charValue());
    assertEquals('>', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '>', '}', '$').charValue());
    assertEquals('>', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '>', '(', '$').charValue());
    assertEquals('>', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '>', ')', '$').charValue());
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '>', '<', '$'));
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '>', '>', '$'));

    assertEquals('>', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '|', '>', '$').charValue());
    assertEquals('>', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '/', '>', '$').charValue());
    assertEquals('>', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '\\', '>', '$').charValue());
    assertEquals('>', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '[', '>', '$').charValue());
    assertEquals('>', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ']', '>', '$').charValue());
    assertEquals('>', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '{', '>', '$').charValue());
    assertEquals('>', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '}', '>', '$').charValue());
    assertEquals('>', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '(', '>', '$').charValue());
    assertEquals('>', SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( ')', '>', '$').charValue());
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '>', '<', '$'));
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '>', '>', '$'));

    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '|', 'a', '$'));
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( 'a', '|', '$'));
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( 'a', 'a', '$'));
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( 'a', 'b', '$'));
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '$', 'b', '$'));
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( 'b', '$', '$'));
    assertNull(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING.smush( '$', '$', '$'));
  }

  @Test
  public void testHierarchySmushingValue() throws Exception {
    assertEquals(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING, SmushingRule.getByCodeValue(4));
    assertEquals(SmushingRule.HORIZONTAL_HIERARCHY_SMUSHING, SmushingRule.valueOf("HORIZONTAL_HIERARCHY_SMUSHING"));
  }

  /*
   * Rule 4: OPPOSITE PAIR SMUSHING (code value 8)
   * Smushes opposing brackets ("[]" or "]["), braces ("{}" or
   * "}{") and parentheses ("()" or ")(") together, replacing
   * any such pair with a vertical bar ("|").
   */
  @Test
  public void testOppositePairSmushingSmush() throws Exception {
    assertEquals('|', SmushingRule.HORIZONTAL_OPPOSITE_PAIR_SMUSHING.smush( '[', ']', '$').charValue());
    assertEquals('|', SmushingRule.HORIZONTAL_OPPOSITE_PAIR_SMUSHING.smush( ']', '[', '$').charValue());
    assertEquals('|', SmushingRule.HORIZONTAL_OPPOSITE_PAIR_SMUSHING.smush( '{', '}', '$').charValue());
    assertEquals('|', SmushingRule.HORIZONTAL_OPPOSITE_PAIR_SMUSHING.smush( '}', '{', '$').charValue());
    assertEquals('|', SmushingRule.HORIZONTAL_OPPOSITE_PAIR_SMUSHING.smush( '(', ')', '$').charValue());
    assertEquals('|', SmushingRule.HORIZONTAL_OPPOSITE_PAIR_SMUSHING.smush( '(', ')', '$').charValue());
    assertNull(SmushingRule.HORIZONTAL_OPPOSITE_PAIR_SMUSHING.smush( '[', '[', '$'));
    assertNull(SmushingRule.HORIZONTAL_OPPOSITE_PAIR_SMUSHING.smush( '[', ')', '$'));
    assertNull(SmushingRule.HORIZONTAL_OPPOSITE_PAIR_SMUSHING.smush( '[', 'a', '$'));
    assertNull(SmushingRule.HORIZONTAL_OPPOSITE_PAIR_SMUSHING.smush( 'a', ']', '$'));
    assertNull(SmushingRule.HORIZONTAL_OPPOSITE_PAIR_SMUSHING.smush( 'a', 'a', '$'));
    assertNull(SmushingRule.HORIZONTAL_OPPOSITE_PAIR_SMUSHING.smush( 'a', 'b', '$'));
    assertNull(SmushingRule.HORIZONTAL_OPPOSITE_PAIR_SMUSHING.smush( '$', 'b', '$'));
    assertNull(SmushingRule.HORIZONTAL_OPPOSITE_PAIR_SMUSHING.smush( 'b', '$', '$'));
    assertNull(SmushingRule.HORIZONTAL_OPPOSITE_PAIR_SMUSHING.smush( '$', '$', '$'));
  }

  @Test
  public void testOppositePairSmushingValue() throws Exception {
    assertEquals(SmushingRule.HORIZONTAL_OPPOSITE_PAIR_SMUSHING, SmushingRule.getByCodeValue(8));
    assertEquals(SmushingRule.HORIZONTAL_OPPOSITE_PAIR_SMUSHING, SmushingRule.valueOf("HORIZONTAL_OPPOSITE_PAIR_SMUSHING"));
  }

  /*
   * Rule 5: BIG X SMUSHING (code value 16)
   *
   * Smushes "/\" into "|", "\/" into "Y", and "><" into "X".
   * Note that "<>" is not smushed in any way by this rule.
   * The name "BIG X" is historical; originally all three pairs
   * were smushed into "X".
   */
  @Test
  public void testBigXSmushingSmush() throws Exception {
    assertEquals('|', SmushingRule.HORIZONTAL_BIG_X_SMUSHING.smush( '/', '\\', '$').charValue());
    assertEquals('Y', SmushingRule.HORIZONTAL_BIG_X_SMUSHING.smush( '\\', '/', '$').charValue());
    assertEquals('X', SmushingRule.HORIZONTAL_BIG_X_SMUSHING.smush( '>', '<', '$').charValue());
    assertNull(SmushingRule.HORIZONTAL_BIG_X_SMUSHING.smush( '/', 'a', '$'));
    assertNull(SmushingRule.HORIZONTAL_BIG_X_SMUSHING.smush( 'a', '\\', '$'));
    assertNull(SmushingRule.HORIZONTAL_BIG_X_SMUSHING.smush( '\\', 'a', '$'));
    assertNull(SmushingRule.HORIZONTAL_BIG_X_SMUSHING.smush( 'a', '/', '$'));
    assertNull(SmushingRule.HORIZONTAL_BIG_X_SMUSHING.smush( '>', 'a', '$'));
    assertNull(SmushingRule.HORIZONTAL_BIG_X_SMUSHING.smush( 'a', '<', '$'));
    assertNull(SmushingRule.HORIZONTAL_BIG_X_SMUSHING.smush( '<', '>', '$'));
    assertNull(SmushingRule.HORIZONTAL_BIG_X_SMUSHING.smush( 'a', 'a', '$'));
    assertNull(SmushingRule.HORIZONTAL_BIG_X_SMUSHING.smush( 'a', 'b', '$'));
    assertNull(SmushingRule.HORIZONTAL_BIG_X_SMUSHING.smush( '$', 'b', '$'));
    assertNull(SmushingRule.HORIZONTAL_BIG_X_SMUSHING.smush( 'b', '$', '$'));
    assertNull(SmushingRule.HORIZONTAL_BIG_X_SMUSHING.smush( '$', '$', '$'));
  }

  @Test
  public void testBigXSmushingValue() throws Exception {
    assertEquals(SmushingRule.HORIZONTAL_BIG_X_SMUSHING, SmushingRule.getByCodeValue(16));
    assertEquals(SmushingRule.HORIZONTAL_BIG_X_SMUSHING, SmushingRule.valueOf("HORIZONTAL_BIG_X_SMUSHING"));
  }

  /*
   * Rule 6: HARDBLANK SMUSHING (code value 32)
   *
   * Smushes two hardblanks together, replacing them with a
   * single hardblank.  (See "Hardblanks" below.)
   */
  @Test
  public void testHardblankSmushingSmush() throws Exception {
    assertEquals('$', SmushingRule.HORIZONTAL_HARDBLANK_SMUSHING.smush(  '$', '$', '$').charValue());
    assertNull(SmushingRule.HORIZONTAL_HARDBLANK_SMUSHING.smush( 'a', 'a', '$'));
    assertNull(SmushingRule.HORIZONTAL_HARDBLANK_SMUSHING.smush( 'a', 'b', '$'));
    assertNull(SmushingRule.HORIZONTAL_HARDBLANK_SMUSHING.smush( '$', 'b', '$'));
    assertNull(SmushingRule.HORIZONTAL_HARDBLANK_SMUSHING.smush( 'b', '$', '$'));
  }

  @Test
  public void testHardblankSmushingValue() throws Exception {
    assertEquals(SmushingRule.HORIZONTAL_HARDBLANK_SMUSHING, SmushingRule.getByCodeValue(32));
    assertEquals(SmushingRule.HORIZONTAL_HARDBLANK_SMUSHING, SmushingRule.valueOf("HORIZONTAL_HARDBLANK_SMUSHING"));
  }

  /*
   * HORIZONTAL FITTING (code value 64)
   *
   * Moves FIGcharacters closer together until they touch.
   * Typographers use the term "kerning" for this phenomenon
   * when applied to the horizontal axis, but fitting also
   * includes this as a vertical behavior, for which there is
   * apparently no established typographical term.
   */
  @Test
  public void testHorizontalFittingSmush() {
    assertEquals(' ', SmushingRule.HORIZONTAL_FITTING.smush(' ', ' ', '$').charValue());
    assertNull(SmushingRule.HORIZONTAL_FITTING.smush( 'a', 'a', '$'));
    assertNull(SmushingRule.HORIZONTAL_FITTING.smush( 'a', ' ', '$'));
    assertNull(SmushingRule.HORIZONTAL_FITTING.smush( ' ', 'a', '$'));
    assertNull(SmushingRule.HORIZONTAL_FITTING.smush( '$', ' ', '$'));
    assertNull(SmushingRule.HORIZONTAL_FITTING.smush( ' ', '$', '$'));
  }

  @Test
  public void testHorizontalFittingValue() {
    assertEquals(SmushingRule.HORIZONTAL_FITTING, SmushingRule.getByCodeValue(64));
    assertEquals(SmushingRule.HORIZONTAL_FITTING, SmushingRule.valueOf("HORIZONTAL_FITTING"));
  }

  /*
   * HORIZONTAL SMUSHING (code value 128)
   *
   * Universal smushing simply overrides the sub-character from the
   * earlier FIGcharacter with the sub-character from the later
   * FIGcharacter.  This produces an "overlapping" effect with some
   * FIGfonts, wherin the latter FIGcharacter may appear to be "in
   * front".
   */
  @Test
  public void testHorizontalSmushingSmush() {
    assertEquals(' ', SmushingRule.HORIZONTAL_SMUSHING.smush(' ', ' ', '$').charValue());
    assertEquals('a', SmushingRule.HORIZONTAL_SMUSHING.smush(' ', 'a', '$').charValue());
    assertEquals(' ', SmushingRule.HORIZONTAL_SMUSHING.smush('b', ' ', '$').charValue());
    assertNull(SmushingRule.HORIZONTAL_SMUSHING.smush( 'a', '$', '$'));
    assertNull(SmushingRule.HORIZONTAL_SMUSHING.smush( '$', 'a', '$'));
  }

  @Test
  public void testHorizontalSmushingValue() {
    assertEquals(SmushingRule.HORIZONTAL_SMUSHING, SmushingRule.getByCodeValue(128));
    assertEquals(SmushingRule.HORIZONTAL_SMUSHING, SmushingRule.valueOf("HORIZONTAL_SMUSHING"));
  }

  /*
   * Rule 1: EQUAL CHARACTER SMUSHING (code value 256)
   * Same as horizontal smushing rule 1.
   */
  @Test
  public void testVerticalEqualCharacterSmushingSmush() {
    //Not implemented
    assertNull(SmushingRule.VERTICAL_EQUAL_CHARACTER_SMUSHING.smush( 'a', 'a', '$'));
  }

  @Test
  public void testVerticalEqualCharacterSmushingValue(){
    assertEquals(SmushingRule.VERTICAL_EQUAL_CHARACTER_SMUSHING, SmushingRule.getByCodeValue(256));
    assertEquals(SmushingRule.VERTICAL_EQUAL_CHARACTER_SMUSHING, SmushingRule.valueOf("VERTICAL_EQUAL_CHARACTER_SMUSHING"));
  }

  /*
   * Rule 2: UNDERSCORE SMUSHING (code value 512)
   * Same as horizontal smushing rule 2.
   */
  @Test
  public void testVerticalUnderscoreSmushingSmush() {
    //Not implemented
    assertNull(SmushingRule.VERTICAL_UNDERSCORE_SMUSHING.smush( 'a', 'a', '$'));
  }

  @Test
  public void testVerticalUnderscoreSmushingValue(){
    assertEquals(SmushingRule.VERTICAL_UNDERSCORE_SMUSHING, SmushingRule.getByCodeValue(512));
    assertEquals(SmushingRule.VERTICAL_UNDERSCORE_SMUSHING, SmushingRule.valueOf("VERTICAL_UNDERSCORE_SMUSHING"));
  }

  /*
   * Rule 3: HIERARCHY SMUSHING (code value 1024)
   * Same as horizontal smushing rule 3.
   */
  @Test
  public void testVerticalHierarchySmushingSmush() {
    //Not implemented
    assertNull(SmushingRule.VERTICAL_HIERARCHY_SMUSHING.smush( 'a', 'a', '$'));
  }

  @Test
  public void testVerticalHierarchySmushingValue(){
    assertEquals(SmushingRule.VERTICAL_HIERARCHY_SMUSHING, SmushingRule.getByCodeValue(1024));
    assertEquals(SmushingRule.VERTICAL_HIERARCHY_SMUSHING, SmushingRule.valueOf("VERTICAL_HIERARCHY_SMUSHING"));
  }

  /*
   * Rule 4: HORIZONTAL LINE SMUSHING (code value 2048)
   * Smushes stacked pairs of "-" and "_", replacing them with
   * a single "=" sub-character.  It does not matter which is
   * found above the other.  Note that vertical smushing rule 1
   * will smushHorizontal IDENTICAL pairs of horizontal lines, while this
   * rule smushes horizontal lines consisting of DIFFERENT
   * sub-characters.
   */
  @Test
  public void testVerticalHorizontalLineSmushingSmush() {
    //Not implemented
    assertNull(SmushingRule.VERTICAL_HORIZONTAL_LINE_SMUSHING.smush( 'a', 'a', '$'));
  }

  @Test
  public void testVerticalHorizontalLineSmushingValue(){
    assertEquals(SmushingRule.VERTICAL_HORIZONTAL_LINE_SMUSHING, SmushingRule.getByCodeValue(2048));
    assertEquals(SmushingRule.VERTICAL_HORIZONTAL_LINE_SMUSHING, SmushingRule.valueOf("VERTICAL_HORIZONTAL_LINE_SMUSHING"));
  }

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
  @Test
  public void testVerticalVerticalLineSmushingSmush() {
    //Not implemented
    assertNull(SmushingRule.VERTICAL_VERTICAL_LINE_SMUSHING.smush( 'a', 'a', '$'));
  }

  @Test
  public void testVerticalVerticalLineSmushingValue(){
    assertEquals(SmushingRule.VERTICAL_VERTICAL_LINE_SMUSHING, SmushingRule.getByCodeValue(4096));
    assertEquals(SmushingRule.VERTICAL_VERTICAL_LINE_SMUSHING, SmushingRule.valueOf("VERTICAL_VERTICAL_LINE_SMUSHING"));
  }

  /*
   * VERTICAL FITTING (code value 8192)
   */
  @Test
  public void testVerticalFittingSmush() {
    //Not implemented
    assertNull(SmushingRule.VERTICAL_FITTING.smush( 'a', 'a', '$'));
  }

  @Test
  public void testVerticalFittingValue(){
    assertEquals(SmushingRule.VERTICAL_FITTING, SmushingRule.getByCodeValue(8192));
    assertEquals(SmushingRule.VERTICAL_FITTING, SmushingRule.valueOf("VERTICAL_FITTING"));
  }

  /*
   * VERTICAL SMUSHING (code value 16384)
   */
  @Test
  public void testVerticalSmushingSmush() {
    //Not implemented
    assertNull(SmushingRule.VERTICAL_SMUSHING.smush( 'a', 'a', '$'));
  }
  @Test
  public void testVerticalSmushingValue(){
    assertEquals(SmushingRule.VERTICAL_SMUSHING, SmushingRule.getByCodeValue(16384));
    assertEquals(SmushingRule.VERTICAL_SMUSHING, SmushingRule.valueOf("VERTICAL_SMUSHING"));
  }

}