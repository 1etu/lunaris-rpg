package lunaris.core.account.events;

import lunaris.core.account.Account;
import org.bukkit.event.Cancellable;

public class AccountXPUpdateEvent extends AccountEvent implements Cancellable {
    private boolean cancelled;
    private final int oldXP;
    private int newXP;

    public AccountXPUpdateEvent(Account account, int oldXP, int newXP) {
        super(account);
        this.oldXP = oldXP;
        this.newXP = newXP;
    }

    public int getOldXP() {
        return oldXP;
    }

    public int getNewXP() {
        return newXP;
    }

    public void setNewXP(int newXP) {
        this.newXP = newXP;
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