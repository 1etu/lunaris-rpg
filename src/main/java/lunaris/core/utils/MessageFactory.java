package lunaris.core.utils;

public class MessageFactory {
    private static final String ERROR_PREFIX = "§cHata: §7";
    private static final String SUCCESS_PREFIX = "§aBaşarılı: §7";
    private static final String INFO_PREFIX = "§eUyarı: §7";
    private static final String ADMIN_PREFIX = "§c§lYÖNETİM §7";
    private static final String RANKS_PREFIX = "§b§lRÜTBE §7";
    
    private static final String HEADER_FORMAT = "§6§l=== %s §6§l===";
    
    public static String error(String message) {
        return ERROR_PREFIX + message;
    }
    
    public static String success(String message) {
        return SUCCESS_PREFIX + message;
    }
    
    public static String info(String message) {
        return INFO_PREFIX + message;
    }
    
    public static String admin(String message) {
        return ADMIN_PREFIX + message;
    }
    
    public static String ranks(String message) {
        return RANKS_PREFIX + message;
    }
    
    public static String header(String title) {
        return String.format(HEADER_FORMAT, title);
    }
    
    public static String coloredHeader(String title, String color) {
        return String.format(HEADER_FORMAT, color + title);
    }
} 