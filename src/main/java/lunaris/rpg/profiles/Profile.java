package lunaris.rpg.profiles;

import java.time.LocalDateTime;
import java.util.UUID;

public class Profile {
    private final int profileId;
    private final int accountId;
    private final UUID uuid;
    private String name;
    private String profileName;
    private final LocalDateTime creationDate;
    private boolean locked;
    private int playtimeMinutes;
    private LocalDateTime lastLogin;
    private ProfileType profileType;

    public Profile(int profileId, int accountId, UUID uuid, String name, String profileName, LocalDateTime creationDate, 
                  boolean locked, int playtimeMinutes, LocalDateTime lastLogin, ProfileType profileType) {
        this.profileId = profileId;
        this.accountId = accountId;
        this.uuid = uuid;
        this.name = name;
        this.profileName = profileName;
        this.creationDate = creationDate;
        this.locked = locked;
        this.playtimeMinutes = playtimeMinutes;
        this.lastLogin = lastLogin;
        this.profileType = profileType;
    }

    public static Profile createNew(int accountId, String playerName, String profileName) {
        return new Profile(
            -1,
            accountId,
            UUID.randomUUID(),
            playerName,
            profileName,
            LocalDateTime.now(),
            false,
            0,
            LocalDateTime.now(),
            ProfileType.TEKIL
        );
    }

    public int getProfileId() {
        return profileId;
    }

    public int getAccountId() {
        return accountId;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public int getPlaytimeMinutes() {
        return playtimeMinutes;
    }

    public void setPlaytimeMinutes(int playtimeMinutes) {
        this.playtimeMinutes = playtimeMinutes;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getFormattedPlaytime() {
        int hours = playtimeMinutes / 60;
        int minutes = playtimeMinutes % 60;
        
        if (hours > 0) {
            return String.format("%d saat %d dakika", hours, minutes);
        } else {
            return String.format("%d dakika", minutes);
        }
    }

    public String getFormattedAge() {
        LocalDateTime now = LocalDateTime.now();
        long years = java.time.temporal.ChronoUnit.YEARS.between(creationDate, now);
        long months = java.time.temporal.ChronoUnit.MONTHS.between(creationDate, now) % 12;
        
        if (years > 0) {
            if (months > 0) {
                return String.format("%d yıl, %d ay", years, months);
            }
            return String.format("%d yıl", years);
        } else if (months > 0) {
            return String.format("%d ay", months);
        } else {
            long days = java.time.temporal.ChronoUnit.DAYS.between(creationDate, now);
            return String.format("%d gün", days);
        }
    }

    public ProfileType getProfileType() {
        return profileType;
    }

    public void setProfileType(ProfileType profileType) {
        this.profileType = profileType;
    }
} 