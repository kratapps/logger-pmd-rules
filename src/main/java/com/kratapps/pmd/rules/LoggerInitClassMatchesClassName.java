package com.kratapps.pmd.rules;

import java.util.List;
import java.util.stream.Collectors;
import net.sourceforge.pmd.lang.apex.ast.ASTClassRefExpression;
import net.sourceforge.pmd.lang.apex.ast.ASTMethodCallExpression;
import net.sourceforge.pmd.lang.apex.ast.ASTUserClass;
import net.sourceforge.pmd.lang.apex.rule.AbstractApexRule;

public class LoggerInitClassMatchesClassName extends AbstractApexRule {

    @Override
    public Object visit(ASTUserClass node, Object data) {
        // Find `ok.Logger.getLogger(...)` method calls.
        getLoggerInitCalls(node).forEach(initCall -> {
            // Find first class ref expressions.
            initCall
                .findDescendantsOfType(ASTClassRefExpression.class)
                .stream()
                // Check the input param of `ok.Logger.getLogger(...)` matches the class name.
                .filter(it -> !initClassMatchesClassName(it, node))
                .forEach(it -> asCtx(data).addViolation(it));
        });
        return super.visit(node, data);
    }

    private List<ASTMethodCallExpression> getLoggerInitCalls(ASTUserClass node) {
        return node
            .findDescendantsOfType(ASTMethodCallExpression.class)
            .stream()
            .filter(this::isLoggerGetLoggerCall)
            .collect(Collectors.toList());
    }

    private boolean isLoggerGetLoggerCall(ASTMethodCallExpression methodCall) {
        return methodCall.getFullMethodName().equalsIgnoreCase("ok.Logger.getLogger") && methodCall.getInputParametersSize() == 1;
    }

    private boolean initClassMatchesClassName(ASTClassRefExpression exp, ASTUserClass cls) {
        String classRefName = exp.getNode().toString().toLowerCase().replace(".class", "");
        // Ignore namespace if present as we don't have namespace in the "className".
        classRefName = classRefName.contains(".") ? classRefName.substring(classRefName.indexOf('.') + 1) : classRefName;
        return classRefName.equalsIgnoreCase(cls.getImage());
    }
}
