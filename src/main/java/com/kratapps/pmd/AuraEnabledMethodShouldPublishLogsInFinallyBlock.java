package com.kratapps.pmd;

import net.sourceforge.pmd.lang.apex.ast.*;
import net.sourceforge.pmd.lang.apex.rule.AbstractApexRule;

public class AuraEnabledMethodShouldPublishLogsInFinallyBlock extends AbstractApexRule {

    @Override
    public Object visit(ASTMethod node, Object data) {
        if (isAuraEnabled(node)) {
            ensureLogsPublishedInFinallyBlock(node, data);
        }
        return super.visit(node, data);
    }

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

    private boolean isAuraEnabled(ASTMethod node) {
        return node.findDescendantsOfType(ASTAnnotation.class).stream().anyMatch(this::isAuraEnabled);
    }

    private boolean isAuraEnabled(ASTAnnotation node) {
        return node.getImage().equalsIgnoreCase("AuraEnabled");
    }

    private boolean hasLoggerPublishStatement(ASTBlockStatement block) {
        return block.getNode().getStatements().stream().anyMatch(statement -> statement.toString().trim()
                .equalsIgnoreCase("ok.Logger.publish();"));
    }
}



