package lunaris.core.utils;

import lunaris.core.command.CommandInfo;
import lunaris.core.command.SubCommandInfo;

public class UsageBuilder {
    private static final String REQUIRED_ARG = "<%s>";
    private static final String OPTIONAL_ARG = "[%s]";
    
    public static String buildUsage(String commandName, String... args) {
        StringBuilder usage = new StringBuilder("§7/" + commandName);
        
        for (String arg : args) {
            boolean optional = arg.startsWith("?");
            String argName = optional ? arg.substring(1) : arg;
            String format = optional ? OPTIONAL_ARG : REQUIRED_ARG;
            
            usage.append(" ").append(String.format(format, argName));
        }
        
        return usage.toString();
    }
    
    public static String buildCommandUsage(CommandInfo info) {
        if (!info.usage().isEmpty()) {
            return info.usage();
        }
        return buildUsage(info.name());
    }
    
    public static String buildSubCommandUsage(String parentCommand, SubCommandInfo info) {
        if (!info.usage().isEmpty()) {
            return info.usage();
        }
        return buildUsage(parentCommand + " " + info.name());
    }
} 