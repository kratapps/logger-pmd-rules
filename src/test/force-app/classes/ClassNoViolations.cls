public class ClassNoViolations {
    private static ok.Logger logger = ok.Logger.getLogger(classNoViolations.Class);
    
    public static ok.Logger getLogger() {
        return null;
    }
    
    @AuraEnabled
    public static void doSomething(Id recordId) {
        try {
            doSomethingElse();
        } catch (Exception e) {
            logger.error().addException(e).log('Something went wrong');
        } finally {
            ok.Logger.publish();
        }
    }

    public static void doSomethingElse(Id recordId2) {
        doSomethingElse();
    }
}