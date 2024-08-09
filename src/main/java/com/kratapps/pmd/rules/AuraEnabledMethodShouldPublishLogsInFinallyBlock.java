package com.kratapps.pmd.rules;

import net.sourceforge.pmd.lang.apex.ast.ASTMethod;

public class AuraEnabledMethodShouldPublishLogsInFinallyBlock extends MethodShouldPublishLogsInFinallyBlock {

    @Override
    public boolean shouldBeChecked(ASTMethod method) {
        return isAuraEnabled(method);
    }

    private boolean isAuraEnabled(ASTMethod method) {
        return hasAnnotation(method, "AuraEnabled");
    }
}
