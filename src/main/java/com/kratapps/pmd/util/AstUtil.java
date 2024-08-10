package com.kratapps.pmd.util;

import net.sourceforge.pmd.lang.apex.ast.ASTAnnotation;
import net.sourceforge.pmd.lang.apex.ast.ASTBlockStatement;
import net.sourceforge.pmd.lang.apex.ast.ASTMethod;

public class AstUtil {

    public static boolean isAuraEnabled(ASTMethod method) {
        return hasAnnotation(method, "AuraEnabled");
    }

    public static boolean hasAnnotation(ASTMethod method, String annotation) {
        return method.findDescendantsOfType(ASTAnnotation.class).stream().anyMatch(it -> isAnnotation(it, annotation));
    }

    public static boolean isAnnotation(ASTAnnotation annotation, String annotationName) {
        return annotation.getImage().equalsIgnoreCase(annotationName);
    }

    public static boolean hasStatement(ASTBlockStatement block, String statement) {
        return block.getNode().getStatements().stream().anyMatch(it -> it.toString().trim().equalsIgnoreCase(statement));
    }
}
