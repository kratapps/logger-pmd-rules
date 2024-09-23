package com.kratapps.pmd.rules;

import com.kratapps.pmd.util.AstUtil;
import net.sourceforge.pmd.lang.apex.ast.ASTBlockStatement;
import net.sourceforge.pmd.lang.apex.ast.ASTMethod;
import net.sourceforge.pmd.lang.apex.ast.ASTTryCatchFinallyBlockStatement;
import net.sourceforge.pmd.lang.apex.rule.AbstractApexRule;

public abstract class MethodShouldPublishLogsInFinallyBlock extends AbstractApexRule {

    @Override
    public Object visit(ASTMethod method, Object data) {
        if (shouldBeChecked(method)) {
            ensureLogsPublishedInFinallyBlock(method, data);
        }
        return super.visit(method, data);
    }

    public abstract boolean shouldBeChecked(ASTMethod method);

    private void ensureLogsPublishedInFinallyBlock(ASTMethod method, Object data) {
        ASTTryCatchFinallyBlockStatement tryCatchBlock = method.descendants(ASTTryCatchFinallyBlockStatement.class).first();
        if (tryCatchBlock == null) {
            // Missing try-catch block.
            asCtx(data).addViolation(method);
        } else {
            ensureLogsPublishedInFinallyBlock(tryCatchBlock, data);
        }
    }

    private void ensureLogsPublishedInFinallyBlock(ASTTryCatchFinallyBlockStatement block, Object data) {
        ASTBlockStatement finallyBlock = block.getFinallyBlock();
        if (finallyBlock == null) {
            // Missing finally block.
            asCtx(data).addViolation(block);
        } else if (!AstUtil.hasMethodCallExpression(finallyBlock, "ok.Logger.publish")) {
            // Missing `ok.Logger.publish();` in finally block.
            asCtx(data).addViolation(finallyBlock);
        }
    }
}
