package com.kratapps.pmd.rules;

import java.util.List;
import java.util.stream.Collectors;
import net.sourceforge.pmd.lang.apex.ast.*;
import net.sourceforge.pmd.lang.apex.rule.AbstractApexRule;

public class LoggerInitClassMatchesClassName extends AbstractApexRule {

    @Override
    public Object visit(ASTUserClass node, Object data) {
        // Find `ok.Logger.getLogger(...)` method calls.
        getLoggerInitCalls(node)
            .stream()
            // Check the input param of `ok.Logger.getLogger(...)` matches the class name.
            .filter(it -> !initClassMatchesClassName(it, node))
            .forEach(it -> asCtx(data).addViolation(it));
        return super.visit(node, data);
    }

    private List<ASTMethodCallExpression> getLoggerInitCalls(ASTUserClass node) {
        return node.descendants(ASTMethodCallExpression.class).filter(this::isLoggerGetLoggerCall).collect(Collectors.toList());
    }

    private boolean isLoggerGetLoggerCall(ASTMethodCallExpression methodCall) {
        return methodCall.getFullMethodName().equalsIgnoreCase("ok.Logger.getLogger") && methodCall.getInputParametersSize() == 1;
    }

    private boolean initClassMatchesClassName(ASTMethodCallExpression exp, ASTUserClass cls) {
        int start = exp.getTextRegion().getStartOffset();
        int end = exp.getTextRegion().getEndOffset();
        // todo there should be a better way than using getTextDocument(), performance impact?
        String actualMethodCall = exp.getTextDocument().getText().substring(start, end);
        String expectedMethodCall = "ok.Logger.getLogger(" + cls.getImage() + ".class)";
        return expectedMethodCall.equalsIgnoreCase(actualMethodCall);
    }
}
