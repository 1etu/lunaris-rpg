package lunaris.core.command;

import lunaris.core.rank.Rank;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandInfo {
    String name();
    String[] aliases() default {};
    String description() default "";
    String usage() default "";
    Rank[] allowedRanks() default {};
    CommandCategory category() default CommandCategory.GENERAL;
} 