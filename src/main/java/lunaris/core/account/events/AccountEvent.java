package lunaris.core.account.events;

import lunaris.core.account.Account;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class AccountEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    protected final Account account;

    public AccountEvent(Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
} 