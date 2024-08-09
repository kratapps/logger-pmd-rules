public class AuraEnabledMissingFinally {
    private static ok.Logger logger = ok.Logger.getLogger(AuraEnabledMissingFinally.Class);
    
    @AuraEnabled
    public static void doSomething(Id recordId) {
        try {
            doSomethingElse();
        } catch (Exception e) {
            logger.error().addException(e).log('Something went wrong.');
        }
    }
}