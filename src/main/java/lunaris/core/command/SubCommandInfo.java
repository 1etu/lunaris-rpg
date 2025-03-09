package lunaris.core.command;

import lunaris.core.rank.Rank;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SubCommandInfo {
    String name();
    String[] aliases() default {};
    String description() default "";
    String usage() default "";
    Rank[] allowedRanks() default {};
} 