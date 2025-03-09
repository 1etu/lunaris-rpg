package lunaris.core.account.events;

import lunaris.core.account.Account;
import org.bukkit.event.Cancellable;

public class AccountCreateEvent extends AccountEvent implements Cancellable {
    private boolean cancelled;

    public AccountCreateEvent(Account account) {
        super(account);
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
} 