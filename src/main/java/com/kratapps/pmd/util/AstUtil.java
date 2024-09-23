package com.kratapps.pmd.util;

import net.sourceforge.pmd.lang.apex.ast.ASTAnnotation;
import net.sourceforge.pmd.lang.apex.ast.ASTBlockStatement;
import net.sourceforge.pmd.lang.apex.ast.ASTMethod;
import net.sourceforge.pmd.lang.apex.ast.ASTMethodCallExpression;

public class AstUtil {

    public static boolean isAuraEnabled(ASTMethod method) {
        return hasAnnotation(method, "AuraEnabled");
    }

    public static boolean hasAnnotation(ASTMethod method, String annotation) {
        return method.descendants(ASTAnnotation.class).toStream().anyMatch(it -> isAnnotation(it, annotation));
    }

    public static boolean isAnnotation(ASTAnnotation annotation, String annotationName) {
        return annotation.getImage().equalsIgnoreCase(annotationName);
    }

    public static boolean hasMethodCallExpression(ASTBlockStatement block, String callExpression) {
        return block
            .descendants(ASTMethodCallExpression.class)
            .toStream()
            .map(ASTMethodCallExpression::getFullMethodName)
            .anyMatch(it -> it.trim().equalsIgnoreCase(callExpression));
    }
}
