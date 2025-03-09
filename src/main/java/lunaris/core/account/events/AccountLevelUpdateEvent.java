package lunaris.core.account.events;

import lunaris.core.account.Account;
import org.bukkit.event.Cancellable;

public class AccountLevelUpdateEvent extends AccountEvent implements Cancellable {
    private boolean cancelled;
    private final int oldLevel;
    private int newLevel;

    public AccountLevelUpdateEvent(Account account, int oldLevel, int newLevel) {
        super(account);
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
    }

    public int getOldLevel() {
        return oldLevel;
    }

    public int getNewLevel() {
        return newLevel;
    }

    public void setNewLevel(int newLevel) {
        this.newLevel = newLevel;
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