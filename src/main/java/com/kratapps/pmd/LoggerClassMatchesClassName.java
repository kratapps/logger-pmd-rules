package com.kratapps.pmd;

import net.sourceforge.pmd.lang.apex.ast.ASTClassRefExpression;
import net.sourceforge.pmd.lang.apex.ast.ASTMethodCallExpression;
import net.sourceforge.pmd.lang.apex.ast.ASTUserClass;
import net.sourceforge.pmd.lang.apex.rule.AbstractApexRule;

import java.util.List;

public class LoggerClassMatchesClassName extends AbstractApexRule {

    @Override
    public Object visit(ASTUserClass node, Object data) {
        String className = node.getImage();
        node.findDescendantsOfType(ASTMethodCallExpression.class)
                .stream()
                .filter(this::isLoggerGetLoggerCall)
                .forEach(methodCall -> {
                    List<ASTClassRefExpression> expressions = methodCall.findDescendantsOfType(ASTClassRefExpression.class);
                    if (expressions.size() == 1) {
                        ASTClassRefExpression exp = expressions.get(0);
                        String inputParamName = exp.getNode().toString();
                        if (!inputParamName.endsWith(className + ".class")) {
                            asCtx(data).addViolation(exp);
                        }
                    }
                });
        return super.visit(node, data);
    }

    private boolean isLoggerGetLoggerCall(ASTMethodCallExpression methodCall) {
        return methodCall.getFullMethodName().equals("ok.Logger.getLogger") && methodCall.getInputParametersSize() == 1;
    }
}



