package lunaris.rpg.profiles.guis;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import lunaris.core.Lunaris;
import lunaris.core.account.Account;
import lunaris.core.account.AccountModule;
import lunaris.rpg.profiles.Profile;
import lunaris.rpg.profiles.ProfileRepository;
import lunaris.rpg.skills.SkillType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProfileManagementGUI implements InventoryProvider {
    private final Lunaris plugin;
    private final ProfileRepository profileRepository;
    private boolean hasProfile;

    public ProfileManagementGUI(Lunaris plugin) {
        this.plugin = plugin;
        this.profileRepository = new ProfileRepository(plugin);
    }

    public static SmartInventory getInventory(Lunaris plugin) {
        return SmartInventory.builder()
                .id("profile-management")
                .provider(new ProfileManagementGUI(plugin))
                .size(4, 9)
                .title("§8Profil Yönetimi")
                .manager(plugin.getInventoryManager())
                .closeable(false)
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        Account account = plugin.getModuleManager()
                .getModule(AccountModule.class)
                .map(module -> module.getAccountService().getOrCreateAccount(player))
                .orElseThrow(() -> new IllegalStateException("acc_not_found"));

        try {
            List<Profile> profiles = profileRepository.findByAccountId(account.getAccountId());
            this.hasProfile = !profiles.isEmpty();

            ItemStack grayGlass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta glassMeta = grayGlass.getItemMeta();
            glassMeta.setDisplayName(" ");
            grayGlass.setItemMeta(glassMeta);

            ItemStack lockedSlot = new ItemStack(Material.BEDROCK);
            ItemMeta lockedMeta = lockedSlot.getItemMeta();
            lockedMeta.setDisplayName("§cKilitli Profil Kontenjanı");
            lockedMeta.setLore(Arrays.asList("§8Müsait Değil"));
            lockedSlot.setItemMeta(lockedMeta);

            contents.fillRow(0, ClickableItem.empty(grayGlass));
            contents.set(1, 0, ClickableItem.empty(grayGlass));
            contents.set(1, 1, ClickableItem.empty(grayGlass));

            if (!hasProfile) {
                createEmptyProfileSlot(contents, account);
            } else {
                createExistingProfileSlot(contents, account, profiles.get(0));
            }

            for (int i = 3; i <= 6; i++) {
                contents.set(1, i, ClickableItem.empty(lockedSlot));
            }

            contents.set(1, 7, ClickableItem.empty(grayGlass));
            contents.set(1, 8, ClickableItem.empty(grayGlass));
            contents.fillRow(2, ClickableItem.empty(grayGlass));

            contents.fillRow(3, ClickableItem.empty(grayGlass));
            if (hasProfile) {
                ItemStack closeButton = new ItemStack(Material.BARRIER);
                ItemMeta closeMeta = closeButton.getItemMeta();
                closeMeta.setDisplayName("§cKapat");
                closeButton.setItemMeta(closeMeta);
                contents.set(3, 4, ClickableItem.of(closeButton, e -> e.getWhoClicked().closeInventory()));
            } else {
                ItemStack warningItem = new ItemStack(Material.BARRIER);
                ItemMeta warningMeta = warningItem.getItemMeta();
                warningMeta.setDisplayName("§c§lProfil Seçimi Gerekli!");
                warningMeta.setLore(Arrays.asList(
                    "§7Oyuna devam etmek için bir profil",
                    "§7oluşturmanız veya seçmeniz gerekiyor!"
                ));
                warningItem.setItemMeta(warningMeta);
                contents.set(3, 4, ClickableItem.empty(warningItem));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            player.sendMessage(ChatColor.RED + "Profil verisi yüklenirken bir hata oluştu!");
            return;
        }
    }

    private void createEmptyProfileSlot(InventoryContents contents, Account account) {
        ItemStack profileSlot = new ItemStack(Material.PAPER);
        ItemMeta meta = profileSlot.getItemMeta();
        meta.setDisplayName("§eBoş Profil Kontenjanı");
        meta.setLore(Arrays.asList(
            "§8Müsait",
            "",
            "§7Bu kontenjanı kullanarak profil",
            "§7oluşturabilir, yeni bir maceraya",
            "§7atılabilirsin!",
            "",
            "§7Her profilin kendine özgü:",
            "§7• Envanteri",
            "§7• Ender Sandığı",
            "§7• Bankası",
            "§7• Görevleri",
            "§7• Koleksiyonları vardır!",
            "",
            "§4§LUYARI: §cDiğer profillere avantaj sağlamak amacıyla profil oluşturmak kötüye kullanım olarak kabul edilir ve cezalandırılır.",
            "",
            "§e→ Profil oluşturmak için tıkla!"
        ));
        profileSlot.setItemMeta(meta);

        contents.set(1, 2, ClickableItem.of(profileSlot, e -> {
            Player player = (Player) e.getWhoClicked();
            ProfileCreationGUI.getInventory(plugin, player).open(player);
        }));
    }

    private void createExistingProfileSlot(InventoryContents contents, Account account, Profile profile) {
        ItemStack profileBlock = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta meta = profileBlock.getItemMeta();
        meta.setDisplayName("§eProfil");

        List<String> lore = new ArrayList<>();
        lore.add("§8Seçilen Profil");
        lore.add("");
        lore.add("§cBu profil kimseyle paylaşılmıyor!");
        lore.add("§7- Boş");
        lore.add("§7- Boş");
        lore.add("§7- Boş");
        lore.add("");
        lore.add("§7Seviye: §f" + account.getLevel());
        lore.add("§7Sınıf: §f" + account.getPlayerClass().getFormattedName());
        lore.add("§7Savaşçı Seviyesi: §f" + account.getCombatLevel());
        lore.add("");
        
        lore.add("§f" + SkillType.WOODCUTTING.getIcon() + " §7Odunculuk: §f" + account.getWoodcuttingLevel());
        lore.add("§f" + SkillType.MINING.getIcon() + " §7Madencilik: §f" + account.getMiningLevel());
        lore.add("§f" + SkillType.FARMING.getIcon() + " §7Çiftçilik: §f" + account.getFarmingLevel());
        lore.add("§f" + SkillType.FISHING.getIcon() + " §7Balıkçılık: §f" + account.getFishingLevel());

        lore.add("");
        lore.add("§7Yaş: §f" + profile.getFormattedAge());
        lore.add("§7Son Giriş: §f" + profile.getLastLogin().toLocalDate());

        meta.setLore(lore);
        profileBlock.setItemMeta(meta);

        contents.set(1, 2, ClickableItem.of(profileBlock, e -> {
            Player player = (Player) e.getWhoClicked();
            openProfileDetails(player, profile);
        }));
    }

    private void createNewProfile(Player player, Account account) {
        Profile newProfile = Profile.createNew(account.getAccountId(), player.getName(), player.getName());
        try {
            profileRepository.save(newProfile);
            this.hasProfile = true;
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            player.sendMessage(ChatColor.GREEN + "Yeni profil oluşturuldu!");
            getInventory(plugin).open(player);
        } catch (SQLException e) {
            e.printStackTrace();
            player.sendMessage(ChatColor.RED + "Profil oluşturulurken bir hata oluştu!");
        }
    }

    private void openProfileDetails(Player player, Profile profile) {
        player.sendMessage(ChatColor.YELLOW + "Profil detayları yakında!");
    }

    @Override
    public void update(Player player, InventoryContents contents) {
    }
} 