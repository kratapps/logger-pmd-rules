public class AuraEnabledMissingTryCatch {
    private static ok.Logger logger = ok.Logger.getLogger(AuraEnabledMissingTryCatch.Class);
    
    @AuraEnabled
    public static void doSomething(Id recordId) {
        doSomethingElse();
    }
}