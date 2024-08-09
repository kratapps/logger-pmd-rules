package com.kratapps.pmd.rules;

import net.sourceforge.pmd.lang.apex.ast.ASTAnnotation;
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
        ASTTryCatchFinallyBlockStatement tryCatchBlock = method.getFirstDescendantOfType(ASTTryCatchFinallyBlockStatement.class);
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
        } else if (!hasLoggerPublishStatement(finallyBlock)) {
            // Missing `ok.Logger.publish();` in finally block.
            asCtx(data).addViolation(finallyBlock);
        }
    }

    protected boolean hasAnnotation(ASTMethod method, String annotation) {
        return method.findDescendantsOfType(ASTAnnotation.class).stream().anyMatch(it -> isAnnotation(it, annotation));
    }

    private boolean isAnnotation(ASTAnnotation annotation, String annotationName) {
        return annotation.getImage().equalsIgnoreCase(annotationName);
    }

    private boolean hasLoggerPublishStatement(ASTBlockStatement block) {
        return block
            .getNode()
            .getStatements()
            .stream()
            .anyMatch(statement -> statement.toString().trim().equalsIgnoreCase("ok.Logger.publish();"));
    }
}
