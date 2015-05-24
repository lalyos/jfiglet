package com.github.lalyos.jfiglet;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class FittingRules {

    public enum OVERRIDE_LAYOUT{
        FITTING,
        CONTROLLED_SMUSHING,
        SMUSHING,
        FULL_WIDTH
    }

    private static String KEY_HORIZONTAL_FULL_WIDTH = "horizontalFullWidth";
    private static String KEY_HORIZONTAL_RULE1 = "horizontalRule1";
    private static String KEY_HORIZONTAL_RULE2 = "horizontalRule2";
    private static String KEY_HORIZONTAL_RULE3 = "horizontalRule3";
    private static String KEY_HORIZONTAL_RULE4 = "horizontalRule4";
    private static String KEY_HORIZONTAL_RULE5 = "horizontalRule5";
    private static String KEY_HORIZONTAL_RULE6 = "horizontalRule6";
    private static String KEY_HORIZONTAL_FITTING = "horizontalFitting";
    private static String KEY_HORIZONTAL_SMUSHING = "horizontalSmushing";
    private static String KEY_VERTICAL_RULE1 = "verticalRule1";
    private static String KEY_VERTICAL_RULE2 = "verticalRule2";
    private static String KEY_VERTICAL_RULE3 = "verticalRule3";
    private static String KEY_VERTICAL_RULE4 = "verticalRule4";
    private static String KEY_VERTICAL_RULE5 = "verticalRule5";
    private static String KEY_VERTICAL_FITTING = "verticalFitting";
    private static String KEY_VERTICAL_SMUSHING = "verticalSmushing";
    private static String KEY_VERTICAL_FULL_WIDTH = "verticalFullWidth";

    private final Map<String, Boolean> properties = new HashMap<String, Boolean>();

    public FittingRules(Integer oldLayout, Integer newLayout) {
        final Map<Integer, String> codes = new LinkedHashMap<Integer, String>();
        codes.put(16384, KEY_VERTICAL_SMUSHING);
        codes.put( 8192, KEY_VERTICAL_FITTING);
        codes.put( 4096, KEY_VERTICAL_RULE5);
        codes.put( 2048, KEY_VERTICAL_RULE4);
        codes.put( 1024, KEY_VERTICAL_RULE3);
        codes.put(  512, KEY_VERTICAL_RULE2);
        codes.put(  256, KEY_VERTICAL_RULE1);
        codes.put(  128, KEY_HORIZONTAL_SMUSHING);
        codes.put(   64, KEY_HORIZONTAL_FITTING);
        codes.put(   32, KEY_HORIZONTAL_RULE6);
        codes.put(   16, KEY_HORIZONTAL_RULE5);
        codes.put(    8, KEY_HORIZONTAL_RULE4);
        codes.put(    4, KEY_HORIZONTAL_RULE3);
        codes.put(    2, KEY_HORIZONTAL_RULE2);
        codes.put(    1, KEY_HORIZONTAL_RULE1);

        int val = (newLayout != null) ? newLayout : oldLayout;
        int index = 0;
        int len = codes.size();
        while ( index < len ) {
            int code = (Integer)codes.keySet().toArray()[index];
            if (val >= code) {

                String key = codes.get(code);
                if ((key.equals(KEY_VERTICAL_FITTING) && isVerticalSmushingEnabled()) || (key.equals(KEY_HORIZONTAL_FITTING) && isHorizontalSmushingEnabled())) {
                    properties.put(key, false);
                }else{
                    properties.put(key, true);
                    val = val - code;
                }
            }
            index++;
        }

        if(! isHorizontalFittingEnabled() && ! isHorizontalSmushingEnabled()) {
            if (oldLayout == 0) {
                properties.put(KEY_HORIZONTAL_FITTING, true);
            } else if (oldLayout == -1){
                properties.put(KEY_HORIZONTAL_FULL_WIDTH, true);
            } else {
                if (isHorizontalRule1Enabled() || isHorizontalRule2Enabled() || isHorizontalRule3Enabled() || isHorizontalRule4Enabled() || isHorizontalRule5Enabled() || isHorizontalRule6Enabled()){
                    properties.put(KEY_HORIZONTAL_SMUSHING, false);
                } else {
                    properties.put(KEY_HORIZONTAL_SMUSHING, true);
                }
                
            }
        } else if (isHorizontalSmushingEnabled() && (isHorizontalRule1Enabled() || isHorizontalRule2Enabled() || isHorizontalRule3Enabled() || isHorizontalRule4Enabled() || isHorizontalRule5Enabled() || isHorizontalRule6Enabled())) {
            properties.put(KEY_HORIZONTAL_SMUSHING, false);
        }


        if(! isVerticalSmushingEnabled() && ! isVerticalFittingEnabled() && !isVerticalRule1Enabled() && !isVerticalRule2Enabled() && !isVerticalRule3Enabled() && !isVerticalRule4Enabled() && !isVerticalRule5Enabled()) {
            properties.put(KEY_VERTICAL_FULL_WIDTH, true);
        } else if (isVerticalSmushingEnabled() && (isVerticalRule1Enabled() || isVerticalRule2Enabled() || isVerticalRule3Enabled() || isVerticalRule4Enabled() || isVerticalRule5Enabled())){
            properties.put(KEY_VERTICAL_SMUSHING, false);
        }

    }

    public void overrideHorizontalLayout(OVERRIDE_LAYOUT layout){
        properties.put(KEY_HORIZONTAL_FULL_WIDTH, false);
        properties.put(KEY_HORIZONTAL_FITTING, false);
        properties.put(KEY_HORIZONTAL_SMUSHING, false);
        properties.put(KEY_HORIZONTAL_RULE1, false);
        properties.put(KEY_HORIZONTAL_RULE2, false);
        properties.put(KEY_HORIZONTAL_RULE3, false);
        properties.put(KEY_HORIZONTAL_RULE4, false);
        properties.put(KEY_HORIZONTAL_RULE5, false);
        properties.put(KEY_HORIZONTAL_RULE6, false);
        switch (layout){
            case FITTING:
                properties.put(KEY_HORIZONTAL_FITTING, true);
                break;
            case CONTROLLED_SMUSHING:
                properties.put(KEY_HORIZONTAL_RULE1, true);
                properties.put(KEY_HORIZONTAL_RULE2, true);
                properties.put(KEY_HORIZONTAL_RULE3, true);
                properties.put(KEY_HORIZONTAL_RULE4, true);
                properties.put(KEY_HORIZONTAL_RULE5, true);
                properties.put(KEY_HORIZONTAL_RULE6, true);
                break;
            case SMUSHING:
                properties.put(KEY_HORIZONTAL_SMUSHING, true);
                break;
            case FULL_WIDTH:
                properties.put(KEY_HORIZONTAL_FULL_WIDTH, true);
                break;
        }
    }

    public void overrideVerticalLayout(OVERRIDE_LAYOUT layout){
        properties.put(KEY_VERTICAL_FULL_WIDTH, false);
        properties.put(KEY_VERTICAL_FITTING, false);
        properties.put(KEY_VERTICAL_SMUSHING, false);
        properties.put(KEY_VERTICAL_RULE1, false);
        properties.put(KEY_VERTICAL_RULE2, false);
        properties.put(KEY_VERTICAL_RULE3, false);
        properties.put(KEY_VERTICAL_RULE4, false);
        properties.put(KEY_VERTICAL_RULE5, false);
        switch (layout){
            case FITTING:
                properties.put(KEY_VERTICAL_FITTING, true);
                break;
            case CONTROLLED_SMUSHING:
                properties.put(KEY_VERTICAL_RULE1, true);
                properties.put(KEY_VERTICAL_RULE2, true);
                properties.put(KEY_VERTICAL_RULE3, true);
                properties.put(KEY_VERTICAL_RULE4, true);
                properties.put(KEY_VERTICAL_RULE5, true);
                break;
            case SMUSHING:
                properties.put(KEY_VERTICAL_SMUSHING, true);
                break;
            case FULL_WIDTH:
                properties.put(KEY_VERTICAL_FULL_WIDTH, true);
                break;
        }    }

    public boolean isHorizontalFullWidthEnabled() {
        return (properties.containsKey(KEY_HORIZONTAL_FULL_WIDTH)) ? properties.get(KEY_HORIZONTAL_FULL_WIDTH) : false;
    }

    public boolean isHorizontalSmushingEnabled() {
        return (properties.containsKey(KEY_HORIZONTAL_SMUSHING)) ? properties.get(KEY_HORIZONTAL_SMUSHING) : false;
    }

    public boolean isHorizontalFittingEnabled() {
        return (properties.containsKey(KEY_HORIZONTAL_FITTING)) ? properties.get(KEY_HORIZONTAL_FITTING) : false;
    }

    
    public boolean isHorizontalRule1Enabled() {
        return (properties.containsKey(KEY_HORIZONTAL_RULE1)) ? properties.get(KEY_HORIZONTAL_RULE1) : false;
    }

    public boolean isHorizontalRule2Enabled() {
        return (properties.containsKey(KEY_HORIZONTAL_RULE2)) ? properties.get(KEY_HORIZONTAL_RULE2) : false;
    }

    public boolean isHorizontalRule3Enabled() {
        return (properties.containsKey(KEY_HORIZONTAL_RULE3)) ? properties.get(KEY_HORIZONTAL_RULE3) : false;
    }

    public boolean isHorizontalRule4Enabled() {
        return (properties.containsKey(KEY_HORIZONTAL_RULE4)) ? properties.get(KEY_HORIZONTAL_RULE4) : false;
    }

    public boolean isHorizontalRule5Enabled() {
        return (properties.containsKey(KEY_HORIZONTAL_RULE5)) ? properties.get(KEY_HORIZONTAL_RULE5) : false;
    }

    public boolean isHorizontalRule6Enabled() {
        return (properties.containsKey(KEY_HORIZONTAL_RULE6)) ? properties.get(KEY_HORIZONTAL_RULE6) : false;
    }

    public boolean isVerticalFullWidthEnabled() {
        return (properties.containsKey(KEY_VERTICAL_FULL_WIDTH)) ? properties.get(KEY_VERTICAL_FULL_WIDTH) : false;
    }

    public boolean isVerticalSmushingEnabled() {
        return (properties.containsKey(KEY_VERTICAL_SMUSHING)) ? properties.get(KEY_VERTICAL_SMUSHING) : false;
    }

    public boolean isVerticalFittingEnabled() {
        return (properties.containsKey(KEY_VERTICAL_FITTING)) ? properties.get(KEY_VERTICAL_FITTING) : false;
    }
    
    public boolean isVerticalRule1Enabled() {
        return (properties.containsKey(KEY_VERTICAL_RULE1)) ? properties.get(KEY_VERTICAL_RULE1) : false;
    }

    public boolean isVerticalRule2Enabled() {
        return (properties.containsKey(KEY_VERTICAL_RULE2)) ? properties.get(KEY_VERTICAL_RULE2) : false;
    }

    public boolean isVerticalRule3Enabled() {
        return (properties.containsKey(KEY_VERTICAL_RULE3)) ? properties.get(KEY_VERTICAL_RULE3) : false;
    }

    public boolean isVerticalRule4Enabled() {
        return (properties.containsKey(KEY_VERTICAL_RULE4)) ? properties.get(KEY_VERTICAL_RULE4) : false;
    }

    public boolean isVerticalRule5Enabled() {
        return (properties.containsKey(KEY_VERTICAL_RULE5)) ? properties.get(KEY_VERTICAL_RULE5) : false;
    }

}