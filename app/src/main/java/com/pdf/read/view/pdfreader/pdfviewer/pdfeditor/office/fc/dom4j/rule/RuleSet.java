package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.rule;

import java.util.ArrayList;
import java.util.Collections;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.Node;

public class RuleSet {
    private final ArrayList rules = new ArrayList();

    private Rule[] ruleArray;

    public RuleSet() {
    }

    public String toString() {
        return super.toString() + " [RuleSet: " + rules + " ]";
    }

    public Rule getMatchingRule(Node node) {
        Rule[] matches = getRuleArray();

        for (int i = matches.length - 1; i >= 0; i--) {
            Rule rule = matches[i];

            if (rule.matches(node)) {
                return rule;
            }
        }

        return null;
    }

    public void addRule(Rule rule) {
        rules.add(rule);
        ruleArray = null;
    }

    public void removeRule(Rule rule) {
        rules.remove(rule);
        ruleArray = null;
    }

    public void addAll(RuleSet that) {
        rules.addAll(that.rules);
        ruleArray = null;
    }

    protected Rule[] getRuleArray() {
        if (ruleArray == null) {
            Collections.sort(rules);

            int size = rules.size();
            ruleArray = new Rule[size];
            rules.toArray(ruleArray);
        }

        return ruleArray;
    }
}