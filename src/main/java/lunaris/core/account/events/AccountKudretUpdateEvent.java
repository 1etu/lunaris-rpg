package lunaris.core.account.events;

import lunaris.core.account.Account;
import org.bukkit.event.Cancellable;

public class AccountKudretUpdateEvent extends AccountEvent implements Cancellable {
    private boolean cancelled;
    private final int oldKudret;
    private int newKudret;

    public AccountKudretUpdateEvent(Account account, int oldKudret, int newKudret) {
        super(account);
        this.oldKudret = oldKudret;
        this.newKudret = newKudret;
    }

    public int getOldKudret() {
        return oldKudret;
    }

    public int getNewKudret() {
        return newKudret;
    }

    public void setNewKudret(int newKudret) {
        this.newKudret = newKudret;
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