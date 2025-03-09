package lunaris.rpg.profiles.guis;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import lunaris.core.Lunaris;
import lunaris.core.account.Account;
import lunaris.core.account.AccountModule;
import lunaris.rpg.classes.PlayerClass;
import lunaris.rpg.profiles.Profile;
import lunaris.rpg.profiles.ProfileType;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffectType;
import lunaris.rpg.profiles.events.PlayerProfileCreateEvent;

public class ProfileCreationGUI implements InventoryProvider {
    private static final Map<UUID, ProfileCreationGUI> instances = new HashMap<>();
    private final Lunaris plugin;
    private String profileName;
    private PlayerClass selectedClass = null;
    private ProfileType profileType = ProfileType.TEKIL;

    public static SmartInventory getInventory(Lunaris plugin, Player player) {
        ProfileCreationGUI gui = instances.computeIfAbsent(player.getUniqueId(), k -> new ProfileCreationGUI(plugin, player.getName()));
        return SmartInventory.builder()
                .id("profile-creation")
                .provider(gui)
                .size(1, 9)
                .title("§8Profil Oluştur")
                .closeable(true)
                .manager(plugin.getInventoryManager())
                .build();
    }

    public ProfileCreationGUI(Lunaris plugin, String playerName) {
        this.plugin = plugin;
        this.profileName = playerName; 
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        ItemStack glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);
        
        contents.set(0, 0, ClickableItem.empty(glass));
        contents.set(0, 1, ClickableItem.empty(glass));

        updateNameButton(contents);
        updateClassButton(contents);
        updateTypeButton(contents);

        contents.set(0, 5, ClickableItem.empty(glass));

        updateStatusButton(contents, player);

        contents.set(0, 7, ClickableItem.empty(glass));
        contents.set(0, 8, ClickableItem.empty(glass));
    }

    private void updateNameButton(InventoryContents contents) {
        ItemStack nameItem = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta nameMeta = nameItem.getItemMeta();
        nameMeta.setDisplayName("§aProfil İsmi");
        
        List<String> nameLore = new ArrayList<>();
        nameLore.add("");
        nameLore.add("§7Seçilen: §f" + profileName);
        nameLore.add("");
        nameLore.add("§7Profil ismi oyuncu isminizle aynı olacaktır");
        
        nameMeta.setLore(nameLore);
        nameItem.setItemMeta(nameMeta);

        contents.set(0, 2, ClickableItem.empty(nameItem));
    }

    private void updateClassButton(InventoryContents contents) {
        ItemStack classItem = new ItemStack(selectedClass == null ? Material.REDSTONE_BLOCK : Material.EMERALD_BLOCK);
        ItemMeta classMeta = classItem.getItemMeta();
        classMeta.setDisplayName(selectedClass == null ? "§cUzmanlık" : "§aUzmanlık");
        
        List<String> classLore = new ArrayList<>();
        classLore.add("");
        classLore.add(selectedClass == null ? "§7Uzmanlık seçilmedi!" : "§7Seçilen: " + selectedClass.getFormattedName());
        classLore.add("");
        for (PlayerClass playerClass : PlayerClass.values()) {
            if (playerClass != PlayerClass.NONE) {
                classLore.add((selectedClass == playerClass ? "§b➥ " : "§7") + playerClass.getFormattedName());
            }
        }
        classLore.add("");
        classLore.add("§e→ Uzmanlık seçmek için tıkla!");
        
        classMeta.setLore(classLore);
        classItem.setItemMeta(classMeta);

        contents.set(0, 3, ClickableItem.of(classItem, e -> {
            cycleClass();
            init((Player) e.getWhoClicked(), contents);
        }));
    }

    private void updateTypeButton(InventoryContents contents) {
        ItemStack typeItem = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta typeMeta = typeItem.getItemMeta();
        typeMeta.setDisplayName("§aProfil Tipi");
        
        List<String> typeLore = new ArrayList<>();
        typeLore.add("");
        for (ProfileType type : ProfileType.values()) {
            typeLore.add((profileType == type ? "§b➥ " : "§7") + type.getDisplayName());
        }
        typeLore.add("");
        typeLore.add("§e→ Tip değiştirmek için tıkla!");
        
        typeMeta.setLore(typeLore);
        typeItem.setItemMeta(typeMeta);

        contents.set(0, 4, ClickableItem.of(typeItem, e -> {
            cycleProfileType();
            init((Player) e.getWhoClicked(), contents);
        }));
    }

    private void updateStatusButton(InventoryContents contents, Player player) {
        boolean canCreate = selectedClass != null;
        
        ItemStack statusItem = new ItemStack(canCreate ? Material.LIME_DYE : Material.RED_DYE);
        ItemMeta statusMeta = statusItem.getItemMeta();
        statusMeta.setDisplayName(canCreate ? "§aHazır!" : "§cHazır Değil!");
        
        List<String> statusLore = new ArrayList<>();
        statusLore.add("");
        if (!canCreate) {
            if (selectedClass == null) statusLore.add("§7• §cUzmanlık seçilmedi!");
        } else {
            statusLore.add("§7Profil oluşturmak için tıkla!");
        }
        
        statusMeta.setLore(statusLore);
        statusItem.setItemMeta(statusMeta);

        contents.set(0, 6, ClickableItem.of(statusItem, e -> {
            if (canCreate) {
                createProfile(player);
            }
        }));
    }

    private void cycleClass() {
        PlayerClass[] classes = PlayerClass.values();
        if (selectedClass == null) {
            selectedClass = classes[1]; // idk why we did it but it works
            return;
        }
        
        int currentIndex = Arrays.asList(classes).indexOf(selectedClass);
        currentIndex = (currentIndex + 1) % classes.length;
        if (currentIndex == 0) currentIndex = 1;
        selectedClass = classes[currentIndex];
    }

    private void cycleProfileType() {
        ProfileType[] types = ProfileType.values();
        int currentIndex = Arrays.asList(types).indexOf(profileType);
        profileType = types[(currentIndex + 1) % types.length];
    }

    private void createProfile(Player player) {
        try {
            var account = plugin.getModuleManager()
                .getModule(AccountModule.class)
                .map(module -> module.getAccountService().getOrCreateAccount(player))
                .orElseThrow(() -> new IllegalStateException("Account not found"));
                
            plugin.getLogger().info("[dbg->CREATE_PROFILE] FOR: " + player.getName());
            plugin.getLogger().info("[dbg->CREATE_PROFILE] SELECTED CLASS: " + selectedClass);
            plugin.getLogger().info("[dbg->CREATE_PROFILE] CURRENT ACCOUNT CLASS: " + account.getPlayerClass().name());
            
            if (selectedClass == null) {
                plugin.getLogger().warning("[dbg->CREATE_PROFILE] SELECTED CLASS IS NULL!");
                player.sendMessage("§c§lHata! §cLütfen bir sınıf seçin!");
                return;
            }

            Profile profile = Profile.createNew(account.getAccountId(), player.getName(), profileName);
            profile.setProfileType(profileType);
            plugin.getProfileRepository().save(profile);
            plugin.getLogger().info("[dbg->CREATE_PROFILE] PROFILE SAVED SUCCESSFULLY");

            plugin.getLogger().info("[dbg->CREATE_PROFILE] SETTING CLASS TO: " + selectedClass.name());
            account.setPlayerClass(selectedClass);
            
            plugin.getModuleManager().getModule(AccountModule.class)
                .ifPresent(module -> {
                    try {
                        plugin.getLogger().info("[dbg->CREATE_PROFILE] ABOUT TO SAVE ACCOUNT WITH CLASS: " + account.getPlayerClass().name());
                        module.getAccountService().saveAccount(account);
                        plugin.getLogger().info("[dbg->CREATE_PROFILE] ACCOUNT SAVED SUCCESSFULLY");
                        plugin.getLogger().info("[dbg->CREATE_PROFILE] VERIFYING CLASS AFTER SAVE: " + account.getPlayerClass().name());
                        
                        Account savedAccount = module.getAccountService().getOrCreateAccount(player);
                        plugin.getLogger().info("[dbg->CREATE_PROFILE] CLASS IN DATABASE: " + savedAccount.getPlayerClass().name());
                        
                        plugin.getServer().getPluginManager().callEvent(
                            new PlayerProfileCreateEvent(player, account));
                        plugin.getLogger().info("[dbg->CREATE_PROFILE] PROFILECREATEEVENT FIRED");
                        
                        instances.remove(player.getUniqueId());
                        
                        plugin.getServer().getScheduler().runTask(plugin, () -> {
                            player.closeInventory();
                            
                            player.removePotionEffect(PotionEffectType.BLINDNESS);
                            player.removePotionEffect(PotionEffectType.SLOW);
                            
                            for (int i = 0; i < 100; i++) {
                              player.sendMessage("");
                            }
                            player.sendMessage("§e§lMaceran başlıyor...");
                            
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                            player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
                        });
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                        player.sendMessage(ChatColor.RED + "Sınıf kaydedilirken bir hata oluştu!");
                    }
                });
            
        } catch (SQLException e) {
            e.printStackTrace();
            player.sendMessage(ChatColor.RED + "Profil oluşturulurken bir hata oluştu!");
        }
    }

    @Override
    public void update(Player player, InventoryContents contents) {
    }
} 