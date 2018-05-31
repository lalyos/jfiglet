package com.github.lalyos.jfiglet;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class SmushingRulesToApply {

  private final SmushingRule.Layout horizontalLayout;
  private final SmushingRule.Layout verticalLayout;
  private final List<SmushingRule> horizontalSmushingRules;
  private final List<SmushingRule> verticalSmushingRules;

  SmushingRulesToApply(SmushingRule.Layout horizontalLayout, SmushingRule.Layout verticalLayout, List<SmushingRule> horizontalSmushingRules, List<SmushingRule> verticalSmushingRules){
    this.horizontalLayout = horizontalLayout;
    this.verticalLayout = verticalLayout;
    Collections.sort(horizontalSmushingRules, new SmushingRuleCodeValueComparator());
    this.horizontalSmushingRules = Collections.unmodifiableList(horizontalSmushingRules);
    Collections.sort(verticalSmushingRules, new SmushingRuleCodeValueComparator());
    this.verticalSmushingRules =  Collections.unmodifiableList(verticalSmushingRules);
  }

  SmushingRule.Layout getHorizontalLayout() {
    return horizontalLayout;
  }

  SmushingRule.Layout getVerticalLayout() {
    return verticalLayout;
  }

  List<SmushingRule> getHorizontalSmushingRules() {
    return horizontalSmushingRules;
  }

  List<SmushingRule> getVerticalSmushingRules() {
    return verticalSmushingRules;
  }

  boolean smushesHorizontal(char char1, char char2, char hardblank){
    for(SmushingRule smushingRule : getHorizontalSmushingRules()){
      if(smushingRule.smushes(char1, char2, hardblank)){
        return true;
      }
    }
    return false;
  }

  Character smushHorizontal(char char1, char char2, char hardblank) {
    if(char1 == ' '){
      return char2;
    }
    if(char2 == ' '){
      return char1;
    }
    if(getHorizontalLayout() == SmushingRule.Layout.SMUSHING){
      return SmushingRule.HORIZONTAL_SMUSHING.smush(char1, char2, hardblank);
    }
    for(SmushingRule smushingRule : getHorizontalSmushingRules()){
      if(smushingRule.smushes(char1, char2, hardblank)){
        return smushingRule.smush(char1, char2, hardblank);
      }
    }
    throw new ImproperUseException("Method should only be called when smush is possible");
  }

  private class SmushingRuleCodeValueComparator implements  Comparator<SmushingRule> {
    public int compare(SmushingRule o1, SmushingRule o2) {
      return Integer.valueOf(o1.getCodeValue()).compareTo(o2.getCodeValue());
    }
  }

  class ImproperUseException extends RuntimeException {
    ImproperUseException(String message){
      super(message);
    }
  }
}
