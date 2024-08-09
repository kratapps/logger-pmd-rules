public class AuraEnabledMissingPublishInFinally {
    private static ok.Logger logger = ok.Logger.getLogger(AuraEnabledMissingPublishInFinally.Class);
    
    @AuraEnabled
    public static void doSomething(Id recordId) {
        try {
            doSomethingElse();
        } catch (Exception e) {
            logger.error().addException(e).log('Something went wrong.');
        } finally {
            System.debug('Nothing');
        }
    }
}