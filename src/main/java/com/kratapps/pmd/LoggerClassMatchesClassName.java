package com.kratapps.pmd;

import net.sourceforge.pmd.lang.apex.ast.ASTClassRefExpression;
import net.sourceforge.pmd.lang.apex.ast.ASTMethodCallExpression;
import net.sourceforge.pmd.lang.apex.ast.ASTUserClass;
import net.sourceforge.pmd.lang.apex.rule.AbstractApexRule;

public class LoggerClassMatchesClassName extends AbstractApexRule {

    @Override
    public Object visit(ASTUserClass node, Object data) {
        String className = node.getImage();
        // Find "ok.Logger.getLogger(...)" method calls.
        node.findDescendantsOfType(ASTMethodCallExpression.class).stream().filter(this::isLoggerGetLoggerCall).forEach(methodCall -> {
            // Find first class ref expressions.
            methodCall.findDescendantsOfType(ASTClassRefExpression.class).stream().findFirst().ifPresent(exp -> {
                // Get class ref name, e.g. "MyClass" for "ok.Logger.getLogger(MyClass.class)".
                String classRefName = exp.getNode().toString().toLowerCase().replace(".class", "");
                // Ignore namespace if present as we don't have namespace in the "className".
                classRefName = classRefName.contains(".") ? classRefName.substring(classRefName.indexOf('.') + 1) : classRefName;
                // Check the ref name matches the class name.
                if (!classRefName.equalsIgnoreCase(className)) {
                    asCtx(data).addViolation(exp);
                }
            });
        });
        return super.visit(node, data);
    }

    private boolean isLoggerGetLoggerCall(ASTMethodCallExpression methodCall) {
        return methodCall.getFullMethodName().equalsIgnoreCase("ok.Logger.getLogger") && methodCall.getInputParametersSize() == 1;
    }
}



