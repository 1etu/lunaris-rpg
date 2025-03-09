package lunaris.core.account.events;

import lunaris.core.account.Account;
import lunaris.core.rank.Rank;
import org.bukkit.event.Cancellable;

public class AccountRankUpdateEvent extends AccountEvent implements Cancellable {
    private boolean cancelled;
    private final Rank oldRank;
    private Rank newRank;

    public AccountRankUpdateEvent(Account account, Rank oldRank, Rank newRank) {
        super(account);
        this.oldRank = oldRank;
        this.newRank = newRank;
    }

    public Rank getOldRank() {
        return oldRank;
    }

    public Rank getNewRank() {
        return newRank;
    }

    public void setNewRank(Rank newRank) {
        this.newRank = newRank;
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