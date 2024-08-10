package com.kratapps.pmd.rules;

import com.kratapps.pmd.util.AstUtil;
import net.sourceforge.pmd.lang.apex.ast.ASTMethod;

public class AuraEnabledMethodShouldPublishLogsInFinallyBlock extends MethodShouldPublishLogsInFinallyBlock {

    @Override
    public boolean shouldBeChecked(ASTMethod method) {
        return AstUtil.isAuraEnabled(method);
    }
}
