package uni.miskolc.spring.websocket.model;

public class GlobalSessionId {
    public static String globalSession;

    public static String getGlobalSession() {
        return globalSession;
    }

    public static void setGlobalSession(String globalSession) {
        GlobalSessionId.globalSession = globalSession;
    }
}
