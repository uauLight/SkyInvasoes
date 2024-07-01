package br.com.skyplugins.skyinvasoes.commands;

import br.com.skyplugins.skyinvasoes.SkyPlugins;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.util.Banner;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class MenuC implements CommandExecutor {

    public static Inventory inv;

    private static final int MAX_FACTIONS_PER_PAGE = 28;
    private int currentPage = 0;

    private List<Faction> factionsUnderAttack = new ArrayList<>();
    private int countdownValue = 30;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage("Comando apenas disponível para jogadores.");
            return true;
        }

        Player p = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("invasoes")) {


            int totalPages = (int) Math.ceil((double) factionsUnderAttack.size() / MAX_FACTIONS_PER_PAGE);

            if (factionsUnderAttack.isEmpty()) {
                // Handle the case when there are no factions under attack
                inv = Bukkit.createInventory(null, 54, "Facções em ataque");
                ItemStack cobweb = new ItemStack(Material.WEB);
                ItemMeta cobwebMeta = cobweb.getItemMeta();
                cobwebMeta.setDisplayName(ChatColor.YELLOW + "Nenhuma facção está em ataque.");
                cobweb.setItemMeta(cobwebMeta);
                inv.setItem(22, cobweb);
                inv.setItem(49, clock());
                p.openInventory(inv);
                return true;
            }

            inv.setItem(22, null);
            inv.setItem(49, clock());

            // Ensure the current page is within bounds
            if (currentPage < 0) {
                currentPage = 0;
            } else if (currentPage >= totalPages) {
                currentPage = totalPages - 1;
            }

            // Calculate the starting and ending index for the current page
            int startIndex = currentPage * MAX_FACTIONS_PER_PAGE;
            int endIndex = Math.min(startIndex + MAX_FACTIONS_PER_PAGE, factionsUnderAttack.size());


            // Start placing banners from slot 10
            int slot = 10;
            for (int i = startIndex; i < endIndex; i++) {
                Faction faction = factionsUnderAttack.get(i);
                ItemStack banner = new ItemStack(Banner.getWhiteBanner(faction.getTag()));
                ItemMeta meta = banner.getItemMeta();
                meta.removeItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                meta.setDisplayName(ChatColor.YELLOW + "[" + faction.getTag() + "]" + " " + faction.getName());

                // Add lore with remaining time in attack and members online
                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.GRAY + "Poder: " + faction.getPowerRounded() + "/" + faction.getPowerMaxRounded()); // Replace with your time calculation method
                lore.add(ChatColor.GRAY + "Membros Online: " + faction.getMembersCount() + "/" + faction.getMembersLimit()); // Replace with your method to get online members
                meta.setLore(lore);

                banner.setItemMeta(meta);
                inv.setItem(slot, banner);
                slot++;
            }

            // Add navigation buttons (next page and previous page) if there is more than one page
            if (totalPages > 1) {
                ItemStack nextPageButton = new ItemStack(Material.ARROW);
                ItemMeta nextPageMeta = nextPageButton.getItemMeta();
                nextPageMeta.setDisplayName(ChatColor.GREEN + "Próxima Página");
                nextPageButton.setItemMeta(nextPageMeta);
                ItemStack prevPageButton = new ItemStack(Material.ARROW);
                ItemMeta prevPageMeta = prevPageButton.getItemMeta();
                prevPageMeta.setDisplayName(ChatColor.RED + "Página Anterior");
                prevPageButton.setItemMeta(prevPageMeta);

                // Place navigation buttons in end slots
                inv.setItem(45, prevPageButton);
                inv.setItem(53, nextPageButton);
            }

            p.openInventory(inv);
        }

        return true;
    }


    // Method to update the factionsUnderAttack list
    private void updateFactionsUnderAttack() {
        factionsUnderAttack.clear(); // Clear the list
        countdownValue = 30;

        // Iterate through factions and add those under attack
        for (Faction faction : FactionColl.get().getAll()) {
            if (faction.isInAttack()) {
                factionsUnderAttack.add(faction);
            }
        }
            // Schedule a task to decrement the countdown value every second
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (countdownValue > 0) {
                        countdownValue--;
                    } else {
                        countdownValue = 30; // Reset the countdown when it reaches 0
                    }
                }
            }.runTaskTimer(SkyPlugins.plugin, 20L, 20L); // 20L represents 1 second in ticks
    }

    // BukkitRunnable to periodically update the factionsUnderAttack list
    public void startUpdateTask() {
        // Schedule the task to run every 30 seconds (600 ticks)
        new BukkitRunnable() {
            @Override
            public void run() {
                updateFactionsUnderAttack();
            }
        }.runTaskTimer(SkyPlugins.plugin, 0, 600L);
    }

    public ItemStack clock() {
        ItemStack customHead = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) customHead.getItemMeta();
        skullMeta.setDisplayName(ChatColor.YELLOW + "Atualização");
        List<String> lore2 = new ArrayList<>();
        lore2.add(ChatColor.GRAY + "Tempo Restante: " + countdownValue); // Replace with your time calculation method
        skullMeta.setLore(lore2);

        try {
            // Create a new GameProfile with a random UUID
            GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);

            // Set the custom texture property
            gameProfile.getProperties().put("textures", new Property("textures", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2NhMWE0OGQyZDIzMWZhNzFiYTVmN2M0MGZkYzEwZDNmMmU5OGM1YTYzYzAxNzMyMWU2NzgxMzA4YjhhNTc5MyJ9fX0=\\"));

            // Use reflection to set the GameProfile on the SkullMeta
            Field profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, gameProfile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        customHead.setItemMeta(skullMeta);
        return customHead;
    }
}
