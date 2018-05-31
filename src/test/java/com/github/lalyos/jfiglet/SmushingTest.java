package com.github.lalyos.jfiglet;

import org.junit.Test;

import static com.github.lalyos.jfiglet.SmushingRule.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SmushingTest {

  @Test
  public void testGetRulesToApplyFullWidth() {
    //Full width
    SmushingRulesToApply rulesToApply;
    rulesToApply = Smushing.getRulesToApply(-1, 0);
    assertEquals(SmushingRule.Layout.FULL_WIDTH, rulesToApply.getHorizontalLayout());
    assertEquals(SmushingRule.Layout.FULL_WIDTH, rulesToApply.getVerticalLayout());
    assertEquals(0, rulesToApply.getHorizontalSmushingRules().size());
    assertEquals(0, rulesToApply.getVerticalSmushingRules().size());

    rulesToApply = Smushing.getRulesToApply(-1, null);
    assertEquals(SmushingRule.Layout.FULL_WIDTH, rulesToApply.getHorizontalLayout());
    assertEquals(SmushingRule.Layout.FULL_WIDTH, rulesToApply.getVerticalLayout());
    assertEquals(0, rulesToApply.getHorizontalSmushingRules().size());
    assertEquals(0, rulesToApply.getVerticalSmushingRules().size());
  }
  @Test
  public void testGetRulesToApplyKerning(){
    SmushingRulesToApply rulesToApply;
    //Kerning
    rulesToApply =  Smushing.getRulesToApply(0, 64);
    assertEquals(SmushingRule.Layout.FITTING, rulesToApply.getHorizontalLayout());
    assertEquals(SmushingRule.Layout.FULL_WIDTH, rulesToApply.getVerticalLayout());
    assertEquals(1, rulesToApply.getHorizontalSmushingRules().size());
    assertEquals(0, rulesToApply.getVerticalSmushingRules().size());
    assertEquals(SmushingRule.HORIZONTAL_FITTING, rulesToApply.getHorizontalSmushingRules().get(0));

    rulesToApply =  Smushing.getRulesToApply(0, null);
    assertEquals(SmushingRule.Layout.FITTING, rulesToApply.getHorizontalLayout());
    assertEquals(SmushingRule.Layout.FULL_WIDTH, rulesToApply.getVerticalLayout());
    assertEquals(1, rulesToApply.getHorizontalSmushingRules().size());
    assertEquals(0, rulesToApply.getVerticalSmushingRules().size());
    assertEquals(SmushingRule.HORIZONTAL_FITTING, rulesToApply.getHorizontalSmushingRules().get(0));
  }
  @Test
  public void testGetRulesToApplySmushing(){
    SmushingRulesToApply rulesToApply;
    // Smushing No specifics not possible in smushMode only
    rulesToApply =  Smushing.getRulesToApply(0, 128);
    assertEquals(SmushingRule.Layout.SMUSHING, rulesToApply.getHorizontalLayout());
    assertEquals(SmushingRule.Layout.FULL_WIDTH, rulesToApply.getVerticalLayout());
    assertEquals(1, rulesToApply.getHorizontalSmushingRules().size());
    assertEquals(0, rulesToApply.getVerticalSmushingRules().size());
    assertEquals(SmushingRule.HORIZONTAL_SMUSHING, rulesToApply.getHorizontalSmushingRules().get(0));
  }
  @Test
  public void testGetRulesToApplyControlledSmushingSlant(){
    SmushingRulesToApply rulesToApply;
    //Controlled Smushing - slant.flf
    //flf2a$ 6 5 16 15 10 0 18319 96
    rulesToApply =  Smushing.getRulesToApply(15, 18319);
    assertEquals(SmushingRule.Layout.CONTROLLED_SMUSHING, rulesToApply.getHorizontalLayout());
    assertEquals(SmushingRule.Layout.CONTROLLED_SMUSHING, rulesToApply.getVerticalLayout());
    assertEquals(4, rulesToApply.getHorizontalSmushingRules().size());
    assertEquals(3, rulesToApply.getVerticalSmushingRules().size());
    assertTrue(rulesToApply.getVerticalSmushingRules().contains(VERTICAL_HIERARCHY_SMUSHING));
    assertTrue(rulesToApply.getVerticalSmushingRules().contains(VERTICAL_UNDERSCORE_SMUSHING));
    assertTrue(rulesToApply.getVerticalSmushingRules().contains(VERTICAL_EQUAL_CHARACTER_SMUSHING));
    assertTrue(rulesToApply.getHorizontalSmushingRules().contains(HORIZONTAL_OPPOSITE_PAIR_SMUSHING));
    assertTrue(rulesToApply.getHorizontalSmushingRules().contains(HORIZONTAL_HIERARCHY_SMUSHING));
    assertTrue(rulesToApply.getHorizontalSmushingRules().contains(HORIZONTAL_UNDERSCORE_SMUSHING));
    assertTrue(rulesToApply.getHorizontalSmushingRules().contains(HORIZONTAL_EQUAL_CHARACTER_SMUSHING));

    rulesToApply =  Smushing.getRulesToApply(15, null);
    assertEquals(SmushingRule.Layout.CONTROLLED_SMUSHING, rulesToApply.getHorizontalLayout());
    assertEquals(Layout.FULL_WIDTH, rulesToApply.getVerticalLayout());
    assertEquals(4, rulesToApply.getHorizontalSmushingRules().size());
    assertTrue(rulesToApply.getHorizontalSmushingRules().contains(HORIZONTAL_OPPOSITE_PAIR_SMUSHING));
    assertTrue(rulesToApply.getHorizontalSmushingRules().contains(HORIZONTAL_HIERARCHY_SMUSHING));
    assertTrue(rulesToApply.getHorizontalSmushingRules().contains(HORIZONTAL_UNDERSCORE_SMUSHING));
    assertTrue(rulesToApply.getHorizontalSmushingRules().contains(HORIZONTAL_EQUAL_CHARACTER_SMUSHING));
  }
  @Test
  public void testGetRulesToApplyControlledSmushingStandard(){
    SmushingRulesToApply rulesToApply;
    //Controlled Smushing - standard.flf
    //flf2a$ 6 5 16 15 11 0 24463 229
    rulesToApply =  Smushing.getRulesToApply(15, 24463);
    assertEquals(SmushingRule.Layout.CONTROLLED_SMUSHING, rulesToApply.getHorizontalLayout());
    assertEquals(SmushingRule.Layout.CONTROLLED_SMUSHING, rulesToApply.getVerticalLayout());
    assertEquals(4, rulesToApply.getHorizontalSmushingRules().size());
    assertEquals(5, rulesToApply.getVerticalSmushingRules().size());
    assertTrue(rulesToApply.getVerticalSmushingRules().contains(VERTICAL_VERTICAL_LINE_SMUSHING));
    assertTrue(rulesToApply.getVerticalSmushingRules().contains(VERTICAL_HORIZONTAL_LINE_SMUSHING));
    assertTrue(rulesToApply.getVerticalSmushingRules().contains(VERTICAL_HIERARCHY_SMUSHING));
    assertTrue(rulesToApply.getVerticalSmushingRules().contains(VERTICAL_UNDERSCORE_SMUSHING));
    assertTrue(rulesToApply.getVerticalSmushingRules().contains(VERTICAL_EQUAL_CHARACTER_SMUSHING));
    assertTrue(rulesToApply.getHorizontalSmushingRules().contains(HORIZONTAL_OPPOSITE_PAIR_SMUSHING));
    assertTrue(rulesToApply.getHorizontalSmushingRules().contains(HORIZONTAL_HIERARCHY_SMUSHING));
    assertTrue(rulesToApply.getHorizontalSmushingRules().contains(HORIZONTAL_UNDERSCORE_SMUSHING));
    assertTrue(rulesToApply.getHorizontalSmushingRules().contains(HORIZONTAL_EQUAL_CHARACTER_SMUSHING));

    rulesToApply =  Smushing.getRulesToApply(15, null);
    assertEquals(SmushingRule.Layout.CONTROLLED_SMUSHING, rulesToApply.getHorizontalLayout());
    assertEquals(Layout.FULL_WIDTH, rulesToApply.getVerticalLayout());
    assertEquals(4, rulesToApply.getHorizontalSmushingRules().size());
    assertTrue(rulesToApply.getHorizontalSmushingRules().contains(HORIZONTAL_OPPOSITE_PAIR_SMUSHING));
    assertTrue(rulesToApply.getHorizontalSmushingRules().contains(HORIZONTAL_HIERARCHY_SMUSHING));
    assertTrue(rulesToApply.getHorizontalSmushingRules().contains(HORIZONTAL_UNDERSCORE_SMUSHING));
    assertTrue(rulesToApply.getHorizontalSmushingRules().contains(HORIZONTAL_EQUAL_CHARACTER_SMUSHING));
  }

  @Test
  public void testGetRulesToApplyControlledSmushingO8(){
    SmushingRulesToApply rulesToApply;
    //Controlled Smushing - o8.flf
    //flf2a$ 6 5 16 0 9
    rulesToApply =  Smushing.getRulesToApply(0, null);
    assertEquals(SmushingRule.Layout.FITTING, rulesToApply.getHorizontalLayout());
    assertEquals(SmushingRule.Layout.FULL_WIDTH, rulesToApply.getVerticalLayout());
    assertEquals(1, rulesToApply.getHorizontalSmushingRules().size());
    assertEquals(SmushingRule.HORIZONTAL_FITTING, rulesToApply.getHorizontalSmushingRules().get(0));
  }


}
