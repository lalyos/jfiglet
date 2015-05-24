package com.github.lalyos.jfiglet;


import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FittingsRulesTest {

    @Test
    public void defaults(){
        //Deliberately invalid value.
        final FittingRules fittingRules = new FittingRules(-2, null);

        assertTrue(fittingRules.isHorizontalSmushingEnabled());
        assertTrue(fittingRules.isVerticalFullWidthEnabled());

        assertFalse(fittingRules.isHorizontalFittingEnabled());
        assertFalse(fittingRules.isHorizontalFullWidthEnabled());
        assertFalse(fittingRules.isHorizontalRule1Enabled());
        assertFalse(fittingRules.isHorizontalRule2Enabled());
        assertFalse(fittingRules.isHorizontalRule3Enabled());
        assertFalse(fittingRules.isHorizontalRule4Enabled());
        assertFalse(fittingRules.isHorizontalRule5Enabled());
        assertFalse(fittingRules.isHorizontalRule6Enabled());
        assertFalse(fittingRules.isVerticalFittingEnabled());
        assertFalse(fittingRules.isVerticalSmushingEnabled());
        assertFalse(fittingRules.isVerticalRule1Enabled());
        assertFalse(fittingRules.isVerticalRule2Enabled());
        assertFalse(fittingRules.isVerticalRule3Enabled());
        assertFalse(fittingRules.isVerticalRule4Enabled());
        assertFalse(fittingRules.isVerticalRule5Enabled());

    }

    @Test
    public void horizontalRule1(){
        assertFalse(new FittingRules(-2, null).isHorizontalRule1Enabled());
        assertTrue(new FittingRules(1, null).isHorizontalRule1Enabled());
        assertTrue(new FittingRules(1, 128 + 1).isHorizontalRule1Enabled());
    }

    @Test
    public void horizontalRule2(){
        assertFalse(new FittingRules(-2, null).isHorizontalRule2Enabled());
        assertTrue(new FittingRules(2, null).isHorizontalRule2Enabled());
        assertTrue(new FittingRules(2, 128 + 2).isHorizontalRule2Enabled());
    }

    @Test
    public void horizontalRule3(){
        assertFalse(new FittingRules(-2, null).isHorizontalRule3Enabled());
        assertTrue(new FittingRules(4, null).isHorizontalRule3Enabled());
        assertTrue(new FittingRules(4, 128 + 4).isHorizontalRule3Enabled());
    }

    @Test
    public void horizontalRule4(){
        assertFalse(new FittingRules(-2, null).isHorizontalRule4Enabled());
        assertTrue(new FittingRules(8, null).isHorizontalRule4Enabled());
        assertTrue(new FittingRules(8, 128 + 8).isHorizontalRule4Enabled());
    }

    @Test
    public void horizontalRule5(){
        assertFalse(new FittingRules(-2, null).isHorizontalRule5Enabled());
        assertTrue(new FittingRules(16, null).isHorizontalRule5Enabled());
        assertTrue(new FittingRules(16, 128 + 16).isHorizontalRule5Enabled());
    }

    @Test
    public void horizontalRule6(){
        assertFalse(new FittingRules(-2, null).isHorizontalRule6Enabled());
        assertTrue(new FittingRules(32, null).isHorizontalRule6Enabled());
        assertTrue(new FittingRules(32, 128 + 32).isHorizontalRule6Enabled());
    }

    @Test
    public void horizontalFitting(){
        assertFalse(new FittingRules(-2, null).isHorizontalFittingEnabled());
        assertTrue(new FittingRules(64, null).isHorizontalFittingEnabled());
        assertTrue(new FittingRules(64, 64).isHorizontalFittingEnabled());
    }

    @Test
    public void horizontalSmushing(){
        assertFalse(new FittingRules(0, null).isHorizontalSmushingEnabled());
        assertTrue(new FittingRules(128, null).isHorizontalSmushingEnabled());
        assertTrue(new FittingRules(128, 128).isHorizontalSmushingEnabled());
    }

    @Test
    public void horizontalFullWidth(){
        assertFalse(new FittingRules(-2, null).isHorizontalFullWidthEnabled());
        assertTrue(new FittingRules(-1, null).isHorizontalFullWidthEnabled());
        assertTrue(new FittingRules(-1, 256).isHorizontalFullWidthEnabled());
    }

    @Test
    public void verticalRule1(){
        assertFalse(new FittingRules(1, 1).isVerticalRule1Enabled());
        assertTrue(new FittingRules(1, 16384 + 256).isVerticalRule1Enabled());
        assertTrue(new FittingRules(1, 256).isVerticalRule1Enabled());
    }

    @Test
    public void verticalRule2(){
        assertFalse(new FittingRules(1, 1).isVerticalRule2Enabled());
        assertTrue(new FittingRules(1, 16384 + 512).isVerticalRule2Enabled());
        assertTrue(new FittingRules(1, 512).isVerticalRule2Enabled());
    }

    @Test
    public void verticalRule3(){
        assertFalse(new FittingRules(1, 1).isVerticalRule3Enabled());
        assertTrue(new FittingRules(1, 16384 + 1024).isVerticalRule3Enabled());
        assertTrue(new FittingRules(1, 1024).isVerticalRule3Enabled());
    }

    @Test
    public void verticalRule4(){
        assertFalse(new FittingRules(1, 1).isVerticalRule4Enabled());
        assertTrue(new FittingRules(1, 16384 + 2048).isVerticalRule4Enabled());
        assertTrue(new FittingRules(1, 2048).isVerticalRule4Enabled());
    }

    @Test
    public void verticalRule5(){
        assertFalse(new FittingRules(1, 1).isVerticalRule5Enabled());
        assertTrue(new FittingRules(1, 16384 + 4096).isVerticalRule5Enabled());
        assertTrue(new FittingRules(1, 4096).isVerticalRule5Enabled());
    }

    @Test
    public void verticalFitting(){
        assertFalse(new FittingRules(1, 1).isVerticalFittingEnabled());
        assertTrue(new FittingRules(1, 8192).isVerticalFittingEnabled());
    }

    @Test
    public void verticalSmushing(){
        assertFalse(new FittingRules(1, 1).isVerticalSmushingEnabled());
        assertTrue(new FittingRules(1, 16384).isVerticalSmushingEnabled());
    }

    @Test
    public void oldRulesFittingEnabled(){
        FittingRules fittingRules = new FittingRules(0,null);
        assertTrue(fittingRules.isHorizontalFittingEnabled());
        assertFalse(fittingRules.isHorizontalFullWidthEnabled());
        assertFalse(fittingRules.isHorizontalSmushingEnabled());
        assertFalse(fittingRules.isHorizontalRule1Enabled());
        assertFalse(fittingRules.isHorizontalRule2Enabled());
        assertFalse(fittingRules.isHorizontalRule3Enabled());
        assertFalse(fittingRules.isHorizontalRule4Enabled());
        assertFalse(fittingRules.isHorizontalRule5Enabled());
        assertFalse(fittingRules.isHorizontalRule6Enabled());
    }

    @Test
    public void oldRulesFullWidthEnabled(){
        FittingRules fittingRules = new FittingRules(-1,null);
        assertTrue(fittingRules.isHorizontalFullWidthEnabled());
        assertFalse(fittingRules.isHorizontalFittingEnabled());
        assertFalse(fittingRules.isHorizontalSmushingEnabled());
        assertFalse(fittingRules.isHorizontalRule1Enabled());
        assertFalse(fittingRules.isHorizontalRule2Enabled());
        assertFalse(fittingRules.isHorizontalRule3Enabled());
        assertFalse(fittingRules.isHorizontalRule4Enabled());
        assertFalse(fittingRules.isHorizontalRule5Enabled());
        assertFalse(fittingRules.isHorizontalRule6Enabled());
    }

    @Test
    public void overrideHorizontalLayoutTest() {
        FittingRules fittingRules = new FittingRules(0,24511);
        assertTrue(fittingRules.isHorizontalRule1Enabled());
        assertTrue(fittingRules.isHorizontalRule2Enabled());
        assertTrue(fittingRules.isHorizontalRule3Enabled());
        assertTrue(fittingRules.isHorizontalRule4Enabled());
        assertTrue(fittingRules.isHorizontalRule5Enabled());
        assertTrue(fittingRules.isHorizontalRule6Enabled());
        assertTrue(fittingRules.isVerticalRule1Enabled());
        assertTrue(fittingRules.isVerticalRule2Enabled());
        assertTrue(fittingRules.isVerticalRule3Enabled());
        assertTrue(fittingRules.isVerticalRule4Enabled());
        assertTrue(fittingRules.isVerticalRule5Enabled());
        assertFalse(fittingRules.isHorizontalFullWidthEnabled());
        assertFalse(fittingRules.isVerticalFullWidthEnabled());
        assertFalse(fittingRules.isHorizontalSmushingEnabled());
        assertFalse(fittingRules.isVerticalSmushingEnabled());
        assertFalse(fittingRules.isHorizontalFittingEnabled());
        assertFalse(fittingRules.isVerticalFittingEnabled());
        fittingRules.overrideHorizontalLayout(FittingRules.OVERRIDE_LAYOUT.FULL_WIDTH);
        //Should now be enabled
        assertTrue(fittingRules.isHorizontalFullWidthEnabled());
        //Now should be disabled
        assertFalse(fittingRules.isHorizontalRule1Enabled());
        assertFalse(fittingRules.isHorizontalRule2Enabled());
        assertFalse(fittingRules.isHorizontalRule3Enabled());
        assertFalse(fittingRules.isHorizontalRule4Enabled());
        assertFalse(fittingRules.isHorizontalRule5Enabled());
        assertFalse(fittingRules.isHorizontalRule6Enabled());
        //Should still be the same
        assertTrue(fittingRules.isVerticalRule1Enabled());
        assertTrue(fittingRules.isVerticalRule2Enabled());
        assertTrue(fittingRules.isVerticalRule3Enabled());
        assertTrue(fittingRules.isVerticalRule4Enabled());
        assertTrue(fittingRules.isVerticalRule5Enabled());
        assertFalse(fittingRules.isVerticalFullWidthEnabled());
        assertFalse(fittingRules.isHorizontalSmushingEnabled());
        assertFalse(fittingRules.isVerticalSmushingEnabled());
        assertFalse(fittingRules.isHorizontalFittingEnabled());
        assertFalse(fittingRules.isVerticalFittingEnabled());    
    }

    @Test
    public void overrideHorizontalLayoutFittingTest() {
        FittingRules fittingRules = new FittingRules(0,24511);
        assertTrue(fittingRules.isHorizontalRule1Enabled());
        assertTrue(fittingRules.isHorizontalRule2Enabled());
        assertTrue(fittingRules.isHorizontalRule3Enabled());
        assertTrue(fittingRules.isHorizontalRule4Enabled());
        assertTrue(fittingRules.isHorizontalRule5Enabled());
        assertTrue(fittingRules.isHorizontalRule6Enabled());
        assertTrue(fittingRules.isVerticalRule1Enabled());
        assertTrue(fittingRules.isVerticalRule2Enabled());
        assertTrue(fittingRules.isVerticalRule3Enabled());
        assertTrue(fittingRules.isVerticalRule4Enabled());
        assertTrue(fittingRules.isVerticalRule5Enabled());
        assertFalse(fittingRules.isHorizontalFullWidthEnabled());
        assertFalse(fittingRules.isVerticalFullWidthEnabled());
        assertFalse(fittingRules.isHorizontalSmushingEnabled());
        assertFalse(fittingRules.isVerticalSmushingEnabled());
        assertFalse(fittingRules.isHorizontalFittingEnabled());
        assertFalse(fittingRules.isVerticalFittingEnabled());
        fittingRules.overrideHorizontalLayout(FittingRules.OVERRIDE_LAYOUT.FITTING);
        //Should now be enabled
        assertTrue(fittingRules.isHorizontalFittingEnabled());
        //Now should be disabled
        assertFalse(fittingRules.isHorizontalRule1Enabled());
        assertFalse(fittingRules.isHorizontalRule2Enabled());
        assertFalse(fittingRules.isHorizontalRule3Enabled());
        assertFalse(fittingRules.isHorizontalRule4Enabled());
        assertFalse(fittingRules.isHorizontalRule5Enabled());
        assertFalse(fittingRules.isHorizontalRule6Enabled());
        //Should still be the same
        assertTrue(fittingRules.isVerticalRule1Enabled());
        assertTrue(fittingRules.isVerticalRule2Enabled());
        assertTrue(fittingRules.isVerticalRule3Enabled());
        assertTrue(fittingRules.isVerticalRule4Enabled());
        assertTrue(fittingRules.isVerticalRule5Enabled());
        assertFalse(fittingRules.isVerticalFullWidthEnabled());
        assertFalse(fittingRules.isHorizontalSmushingEnabled());
        assertFalse(fittingRules.isVerticalSmushingEnabled());
        assertFalse(fittingRules.isHorizontalFullWidthEnabled());
        assertFalse(fittingRules.isVerticalFittingEnabled());
    }
    @Test
    public void overrideHorizontalLayoutSmushingTest() {
        FittingRules fittingRules = new FittingRules(0,24511);
        assertTrue(fittingRules.isHorizontalRule1Enabled());
        assertTrue(fittingRules.isHorizontalRule2Enabled());
        assertTrue(fittingRules.isHorizontalRule3Enabled());
        assertTrue(fittingRules.isHorizontalRule4Enabled());
        assertTrue(fittingRules.isHorizontalRule5Enabled());
        assertTrue(fittingRules.isHorizontalRule6Enabled());
        assertTrue(fittingRules.isVerticalRule1Enabled());
        assertTrue(fittingRules.isVerticalRule2Enabled());
        assertTrue(fittingRules.isVerticalRule3Enabled());
        assertTrue(fittingRules.isVerticalRule4Enabled());
        assertTrue(fittingRules.isVerticalRule5Enabled());
        assertFalse(fittingRules.isHorizontalFullWidthEnabled());
        assertFalse(fittingRules.isVerticalFullWidthEnabled());
        assertFalse(fittingRules.isHorizontalSmushingEnabled());
        assertFalse(fittingRules.isVerticalSmushingEnabled());
        assertFalse(fittingRules.isHorizontalFittingEnabled());
        assertFalse(fittingRules.isVerticalFittingEnabled());
        fittingRules.overrideHorizontalLayout(FittingRules.OVERRIDE_LAYOUT.SMUSHING);
        //Should now be enabled
        assertTrue(fittingRules.isHorizontalSmushingEnabled());
        //Now should be disabled
        assertFalse(fittingRules.isHorizontalRule1Enabled());
        assertFalse(fittingRules.isHorizontalRule2Enabled());
        assertFalse(fittingRules.isHorizontalRule3Enabled());
        assertFalse(fittingRules.isHorizontalRule4Enabled());
        assertFalse(fittingRules.isHorizontalRule5Enabled());
        assertFalse(fittingRules.isHorizontalRule6Enabled());
        //Should still be the same
        assertTrue(fittingRules.isVerticalRule1Enabled());
        assertTrue(fittingRules.isVerticalRule2Enabled());
        assertTrue(fittingRules.isVerticalRule3Enabled());
        assertTrue(fittingRules.isVerticalRule4Enabled());
        assertTrue(fittingRules.isVerticalRule5Enabled());
        assertFalse(fittingRules.isVerticalFullWidthEnabled());
        assertFalse(fittingRules.isHorizontalFittingEnabled());
        assertFalse(fittingRules.isVerticalSmushingEnabled());
        assertFalse(fittingRules.isHorizontalFullWidthEnabled());
        assertFalse(fittingRules.isVerticalFittingEnabled());
    }

    @Test
    public void overrideHorizontalLayoutControlledSmushingTest() {
        FittingRules fittingRules = new FittingRules(-1,24320);
        assertFalse(fittingRules.isHorizontalRule1Enabled());
        assertFalse(fittingRules.isHorizontalRule2Enabled());
        assertFalse(fittingRules.isHorizontalRule3Enabled());
        assertFalse(fittingRules.isHorizontalRule4Enabled());
        assertFalse(fittingRules.isHorizontalRule5Enabled());
        assertFalse(fittingRules.isHorizontalRule6Enabled());
        assertTrue(fittingRules.isVerticalRule1Enabled());
        assertTrue(fittingRules.isVerticalRule2Enabled());
        assertTrue(fittingRules.isVerticalRule3Enabled());
        assertTrue(fittingRules.isVerticalRule4Enabled());
        assertTrue(fittingRules.isVerticalRule5Enabled());
        assertTrue(fittingRules.isHorizontalFullWidthEnabled());
        assertFalse(fittingRules.isVerticalFullWidthEnabled());
        assertFalse(fittingRules.isHorizontalSmushingEnabled());
        assertFalse(fittingRules.isVerticalSmushingEnabled());
        assertFalse(fittingRules.isHorizontalFittingEnabled());
        assertFalse(fittingRules.isVerticalFittingEnabled());
        fittingRules.overrideHorizontalLayout(FittingRules.OVERRIDE_LAYOUT.CONTROLLED_SMUSHING);
        //Should now be enabled
        assertTrue(fittingRules.isHorizontalRule1Enabled());
        assertTrue(fittingRules.isHorizontalRule2Enabled());
        assertTrue(fittingRules.isHorizontalRule3Enabled());
        assertTrue(fittingRules.isHorizontalRule4Enabled());
        assertTrue(fittingRules.isHorizontalRule5Enabled());
        assertTrue(fittingRules.isHorizontalRule6Enabled());
        //Now should be disabled
        assertFalse(fittingRules.isHorizontalFullWidthEnabled());
        //Should still be the same
        assertTrue(fittingRules.isVerticalRule1Enabled());
        assertTrue(fittingRules.isVerticalRule2Enabled());
        assertTrue(fittingRules.isVerticalRule3Enabled());
        assertTrue(fittingRules.isVerticalRule4Enabled());
        assertTrue(fittingRules.isVerticalRule5Enabled());
        assertFalse(fittingRules.isHorizontalSmushingEnabled());
        assertFalse(fittingRules.isVerticalFullWidthEnabled());
        assertFalse(fittingRules.isHorizontalFittingEnabled());
        assertFalse(fittingRules.isVerticalSmushingEnabled());
        assertFalse(fittingRules.isVerticalFittingEnabled());
    }


    @Test
    public void overrideVerticalLayoutTest() {
        FittingRules fittingRules = new FittingRules(0,24511);
        assertTrue(fittingRules.isHorizontalRule1Enabled());
        assertTrue(fittingRules.isHorizontalRule2Enabled());
        assertTrue(fittingRules.isHorizontalRule3Enabled());
        assertTrue(fittingRules.isHorizontalRule4Enabled());
        assertTrue(fittingRules.isHorizontalRule5Enabled());
        assertTrue(fittingRules.isHorizontalRule6Enabled());
        assertTrue(fittingRules.isVerticalRule1Enabled());
        assertTrue(fittingRules.isVerticalRule2Enabled());
        assertTrue(fittingRules.isVerticalRule3Enabled());
        assertTrue(fittingRules.isVerticalRule4Enabled());
        assertTrue(fittingRules.isVerticalRule5Enabled());
        assertFalse(fittingRules.isHorizontalFullWidthEnabled());
        assertFalse(fittingRules.isVerticalFullWidthEnabled());
        assertFalse(fittingRules.isHorizontalSmushingEnabled());
        assertFalse(fittingRules.isVerticalSmushingEnabled());
        assertFalse(fittingRules.isHorizontalFittingEnabled());
        assertFalse(fittingRules.isVerticalFittingEnabled());
        fittingRules.overrideVerticalLayout(FittingRules.OVERRIDE_LAYOUT.FULL_WIDTH);
        //Should now be enabled
        assertTrue(fittingRules.isVerticalFullWidthEnabled());
        //Now should be disabled
        assertFalse(fittingRules.isVerticalRule1Enabled());
        assertFalse(fittingRules.isVerticalRule2Enabled());
        assertFalse(fittingRules.isVerticalRule3Enabled());
        assertFalse(fittingRules.isVerticalRule4Enabled());
        assertFalse(fittingRules.isVerticalRule5Enabled());
        //Should still be the same
        assertTrue(fittingRules.isHorizontalRule1Enabled());
        assertTrue(fittingRules.isHorizontalRule2Enabled());
        assertTrue(fittingRules.isHorizontalRule3Enabled());
        assertTrue(fittingRules.isHorizontalRule4Enabled());
        assertTrue(fittingRules.isHorizontalRule5Enabled());
        assertTrue(fittingRules.isHorizontalRule6Enabled());
        assertFalse(fittingRules.isHorizontalFullWidthEnabled());
        assertFalse(fittingRules.isHorizontalSmushingEnabled());
        assertFalse(fittingRules.isVerticalSmushingEnabled());
        assertFalse(fittingRules.isHorizontalFittingEnabled());
        assertFalse(fittingRules.isVerticalFittingEnabled());    
    }
    @Test
    public void overrideVerticalLayoutFittingTest() {
        FittingRules fittingRules = new FittingRules(0,24511);
        assertTrue(fittingRules.isHorizontalRule1Enabled());
        assertTrue(fittingRules.isHorizontalRule2Enabled());
        assertTrue(fittingRules.isHorizontalRule3Enabled());
        assertTrue(fittingRules.isHorizontalRule4Enabled());
        assertTrue(fittingRules.isHorizontalRule5Enabled());
        assertTrue(fittingRules.isHorizontalRule6Enabled());
        assertTrue(fittingRules.isVerticalRule1Enabled());
        assertTrue(fittingRules.isVerticalRule2Enabled());
        assertTrue(fittingRules.isVerticalRule3Enabled());
        assertTrue(fittingRules.isVerticalRule4Enabled());
        assertTrue(fittingRules.isVerticalRule5Enabled());
        assertFalse(fittingRules.isHorizontalFullWidthEnabled());
        assertFalse(fittingRules.isVerticalFullWidthEnabled());
        assertFalse(fittingRules.isHorizontalSmushingEnabled());
        assertFalse(fittingRules.isVerticalSmushingEnabled());
        assertFalse(fittingRules.isHorizontalFittingEnabled());
        assertFalse(fittingRules.isVerticalFittingEnabled());
        fittingRules.overrideVerticalLayout(FittingRules.OVERRIDE_LAYOUT.FITTING);
        //Should now be enabled
        assertTrue(fittingRules.isVerticalFittingEnabled());
        //Now should be disabled
        assertFalse(fittingRules.isVerticalRule1Enabled());
        assertFalse(fittingRules.isVerticalRule2Enabled());
        assertFalse(fittingRules.isVerticalRule3Enabled());
        assertFalse(fittingRules.isVerticalRule4Enabled());
        assertFalse(fittingRules.isVerticalRule5Enabled());
        //Should still be the same
        assertTrue(fittingRules.isHorizontalRule1Enabled());
        assertTrue(fittingRules.isHorizontalRule2Enabled());
        assertTrue(fittingRules.isHorizontalRule3Enabled());
        assertTrue(fittingRules.isHorizontalRule4Enabled());
        assertTrue(fittingRules.isHorizontalRule5Enabled());
        assertTrue(fittingRules.isHorizontalRule6Enabled());
        assertFalse(fittingRules.isHorizontalFullWidthEnabled());
        assertFalse(fittingRules.isHorizontalSmushingEnabled());
        assertFalse(fittingRules.isVerticalSmushingEnabled());
        assertFalse(fittingRules.isHorizontalFullWidthEnabled());
    }

    @Test
    public void overrideVerticalLayoutSmushingTest() {
        FittingRules fittingRules = new FittingRules(0,24511);
        assertTrue(fittingRules.isHorizontalRule1Enabled());
        assertTrue(fittingRules.isHorizontalRule2Enabled());
        assertTrue(fittingRules.isHorizontalRule3Enabled());
        assertTrue(fittingRules.isHorizontalRule4Enabled());
        assertTrue(fittingRules.isHorizontalRule5Enabled());
        assertTrue(fittingRules.isHorizontalRule6Enabled());
        assertTrue(fittingRules.isVerticalRule1Enabled());
        assertTrue(fittingRules.isVerticalRule2Enabled());
        assertTrue(fittingRules.isVerticalRule3Enabled());
        assertTrue(fittingRules.isVerticalRule4Enabled());
        assertTrue(fittingRules.isVerticalRule5Enabled());
        assertFalse(fittingRules.isHorizontalFullWidthEnabled());
        assertFalse(fittingRules.isVerticalFullWidthEnabled());
        assertFalse(fittingRules.isHorizontalSmushingEnabled());
        assertFalse(fittingRules.isVerticalSmushingEnabled());
        assertFalse(fittingRules.isHorizontalFittingEnabled());
        assertFalse(fittingRules.isVerticalFittingEnabled());
        fittingRules.overrideVerticalLayout(FittingRules.OVERRIDE_LAYOUT.SMUSHING);
        //Should now be enabled
        assertTrue(fittingRules.isVerticalSmushingEnabled());
        //Now should be disabled
        assertFalse(fittingRules.isVerticalRule1Enabled());
        assertFalse(fittingRules.isVerticalRule2Enabled());
        assertFalse(fittingRules.isVerticalRule3Enabled());
        assertFalse(fittingRules.isVerticalRule4Enabled());
        assertFalse(fittingRules.isVerticalRule5Enabled());
        //Should still be the same
        assertTrue(fittingRules.isHorizontalRule1Enabled());
        assertTrue(fittingRules.isHorizontalRule2Enabled());
        assertTrue(fittingRules.isHorizontalRule3Enabled());
        assertTrue(fittingRules.isHorizontalRule4Enabled());
        assertTrue(fittingRules.isHorizontalRule5Enabled());
        assertTrue(fittingRules.isHorizontalRule6Enabled());
        assertFalse(fittingRules.isHorizontalFullWidthEnabled());
        assertFalse(fittingRules.isHorizontalSmushingEnabled());
        assertFalse(fittingRules.isVerticalFittingEnabled());
        assertFalse(fittingRules.isHorizontalFullWidthEnabled());
    }

    @Test
    public void overrideVerticalLayoutControlledSmushingTest() {
        FittingRules fittingRules = new FittingRules(0,191);
        assertTrue(fittingRules.isHorizontalRule1Enabled());
        assertTrue(fittingRules.isHorizontalRule2Enabled());
        assertTrue(fittingRules.isHorizontalRule3Enabled());
        assertTrue(fittingRules.isHorizontalRule4Enabled());
        assertTrue(fittingRules.isHorizontalRule5Enabled());
        assertTrue(fittingRules.isHorizontalRule6Enabled());
        assertFalse(fittingRules.isVerticalRule1Enabled());
        assertFalse(fittingRules.isVerticalRule2Enabled());
        assertFalse(fittingRules.isVerticalRule3Enabled());
        assertFalse(fittingRules.isVerticalRule4Enabled());
        assertFalse(fittingRules.isVerticalRule5Enabled());
        assertFalse(fittingRules.isHorizontalFullWidthEnabled());
        assertTrue(fittingRules.isVerticalFullWidthEnabled());
        assertFalse(fittingRules.isHorizontalSmushingEnabled());
        assertFalse(fittingRules.isVerticalSmushingEnabled());
        assertFalse(fittingRules.isHorizontalFittingEnabled());
        assertFalse(fittingRules.isVerticalFittingEnabled());
        fittingRules.overrideVerticalLayout(FittingRules.OVERRIDE_LAYOUT.CONTROLLED_SMUSHING);
        //Should now be enabled
        assertTrue(fittingRules.isVerticalRule1Enabled());
        assertTrue(fittingRules.isVerticalRule2Enabled());
        assertTrue(fittingRules.isVerticalRule3Enabled());
        assertTrue(fittingRules.isVerticalRule4Enabled());
        assertTrue(fittingRules.isVerticalRule5Enabled());
        //Now should be disabled
        assertFalse(fittingRules.isHorizontalFullWidthEnabled());
        //Should still be the same
        assertFalse(fittingRules.isVerticalSmushingEnabled());
        assertTrue(fittingRules.isHorizontalRule1Enabled());
        assertTrue(fittingRules.isHorizontalRule2Enabled());
        assertTrue(fittingRules.isHorizontalRule3Enabled());
        assertTrue(fittingRules.isHorizontalRule4Enabled());
        assertTrue(fittingRules.isHorizontalRule5Enabled());
        assertTrue(fittingRules.isHorizontalRule6Enabled());
        assertFalse(fittingRules.isHorizontalFullWidthEnabled());
        assertFalse(fittingRules.isHorizontalSmushingEnabled());
        assertFalse(fittingRules.isVerticalFittingEnabled());
    }

}
