package com.github.lalyos.jfiglet;

import java.util.HashMap;
import java.util.Map;

public class FittingRules {

    protected static enum LAYOUT {
        NOT_SET,
        FULL_WIDTH,
        FITTING,
        SMUSHING,
        CONTROLLED_SMUSHING;
    }

    private static final String KEY_HLAYOUT = "hLayout";
    private static final String KEY_HRULE1 = "hRule1";
    private static final String KEY_HRULE2 = "hRule2";
    private static final String KEY_HRULE3 = "hRule3";
    private static final String KEY_HRULE4 = "hRule4";
    private static final String KEY_HRULE5 = "hRule5";
    private static final String KEY_HRULE6 = "hRule6";
    private static final String KEY_VLAYOUT = "vLayout";
    private static final String KEY_VRULE1 = "vRule1";
    private static final String KEY_VRULE2 = "vRule2";
    private static final String KEY_VRULE3 = "vRule3";
    private static final String KEY_VRULE4 = "vRule4";
    private static final String KEY_VRULE5 = "vRule5";

    private final Map<String, Object> properties = new HashMap<String, Object>();

    public FittingRules(Integer oldLayout, Integer newLayout) {
        Object[][] codes = {{16384,"vLayout",LAYOUT.SMUSHING}, {8192,"vLayout",LAYOUT.FITTING}, {4096, "vRule5", true}, {2048, "vRule4", true},
                {1024, "vRule3", true}, {512, "vRule2", true}, {256, "vRule1", true}, {128, "hLayout", LAYOUT.SMUSHING},
                {64, "hLayout", LAYOUT.FITTING}, {32, "hRule6", true}, {16, "hRule5", true}, {8, "hRule4", true}, {4, "hRule3", true},
                {2, "hRule2", true}, {1, "hRule1", true}};

        int val = (newLayout != null) ? newLayout : oldLayout;
        int index = 0;
        int len = codes.length;
        while ( index < len ) {
            Object[] code = codes[index];
            int id = (Integer) code[0];
            String key = (String) code[1];
            Object value = code[2];
            if (val >= id) {
                val = val - id;
                if(! properties.containsKey(key)){
                    properties.put(key,value);
                }
            } else if (! key.equals(KEY_VLAYOUT) && ! key.equals(KEY_HLAYOUT)){
                properties.put(key,false);
            }
            index++;
        }

        if (gethLayout() == LAYOUT.NOT_SET) {
            if (oldLayout == 0) {
                sethLayout(LAYOUT.FITTING);
            } else if (oldLayout == -1) {
                sethLayout(LAYOUT.FULL_WIDTH);
            } else {
                if (ishRule1() || ishRule2() || ishRule3() || ishRule4() || ishRule5() || ishRule6()) {
                    sethLayout(LAYOUT.CONTROLLED_SMUSHING);
                } else {
                    sethLayout(LAYOUT.SMUSHING);
                }
            }
        } else if (gethLayout() == LAYOUT.SMUSHING) {
            if (ishRule1() || ishRule2() || ishRule3() || ishRule4() || ishRule5() || ishRule6()) {
                sethLayout(LAYOUT.CONTROLLED_SMUSHING);
            }
        }

        if (getvLayout() == LAYOUT.NOT_SET) {
            if (isvRule1() || isvRule2() || isvRule3() || isvRule4() || isvRule5()) {
                setvLayout(LAYOUT.CONTROLLED_SMUSHING);
            } else {
                setvLayout(LAYOUT.FULL_WIDTH);
            }
        } else if (getvLayout() == LAYOUT.SMUSHING) {
            if (isvRule1() || isvRule2() || isvRule3() || isvRule4() || isvRule5()) {
                setvLayout(LAYOUT.CONTROLLED_SMUSHING);
            }
        }

    }

    public void overridehLayout(LAYOUT layout){
        sethLayout(layout);
        if (layout == LAYOUT.CONTROLLED_SMUSHING) {
            properties.put("hRule1", true);
            properties.put("hRule2", true);
            properties.put("hRule3", true);
            properties.put("hRule4", true);
            properties.put("hRule5", true);
            properties.put("hRule6", true);
        } else {
            properties.put("hRule1", false);
            properties.put("hRule2", false);
            properties.put("hRule3", false);
            properties.put("hRule4", false);
            properties.put("hRule5", false);
            properties.put("hRule6", false);
        }
    }

    public void overridevLayout(LAYOUT layout){
        setvLayout(layout);
        if (layout == LAYOUT.CONTROLLED_SMUSHING) {
            properties.put("vRule1", true);
            properties.put("vRule2", true);
            properties.put("vRule3", true);
            properties.put("vRule4", true);
            properties.put("vRule5", true);
        } else {
            properties.put("vRule1", false);
            properties.put("vRule2", false);
            properties.put("vRule3", false);
            properties.put("vRule4", false);
            properties.put("vRule5", false);
        }
    }

    public LAYOUT gethLayout() {
        return (properties.containsKey(KEY_HLAYOUT)) ? (LAYOUT)properties.get(KEY_HLAYOUT) : LAYOUT.NOT_SET;
    }

    public void sethLayout(LAYOUT hLayout) {
        properties.put(KEY_HLAYOUT,hLayout);
    }

    public boolean ishRule1() {
        return (properties.containsKey(KEY_HRULE1)) ? (Boolean)properties.get(KEY_HRULE1) : false;
    }

    public boolean ishRule2() {
        return (properties.containsKey(KEY_HRULE2)) ? (Boolean)properties.get(KEY_HRULE2) : false;
    }

    public boolean ishRule3() {
        return (properties.containsKey(KEY_HRULE3)) ? (Boolean)properties.get(KEY_HRULE3) : false;
    }

    public boolean ishRule4() {
        return (properties.containsKey(KEY_HRULE4)) ? (Boolean)properties.get(KEY_HRULE4) : false;
    }

    public boolean ishRule5() {
        return (properties.containsKey(KEY_HRULE5)) ? (Boolean)properties.get(KEY_HRULE5) : false;
    }

    public boolean ishRule6() {
        return (properties.containsKey(KEY_HRULE6)) ? (Boolean)properties.get(KEY_HRULE6) : false;
    }

    public LAYOUT getvLayout() {
        return (properties.containsKey(KEY_VLAYOUT)) ? (LAYOUT)properties.get(KEY_VLAYOUT) : LAYOUT.NOT_SET;
    }

    public void setvLayout(LAYOUT vLayout) {
        properties.put(KEY_VLAYOUT,vLayout);
    }

    public boolean isvRule1() {
        return (properties.containsKey(KEY_VRULE1)) ? (Boolean)properties.get(KEY_VRULE1) : false;
    }

    public boolean isvRule2() {
        return (properties.containsKey(KEY_VRULE2)) ? (Boolean)properties.get(KEY_VRULE2) : false;
    }

    public boolean isvRule3() {
        return (properties.containsKey(KEY_VRULE3)) ? (Boolean)properties.get(KEY_VRULE3) : false;
    }

    public boolean isvRule4() {
        return (properties.containsKey(KEY_VRULE4)) ? (Boolean)properties.get(KEY_VRULE4) : false;
    }

    public boolean isvRule5() {
        return (properties.containsKey(KEY_VRULE5)) ? (Boolean)properties.get(KEY_VRULE5) : false;
    }

}