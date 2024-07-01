package br.com.skyplugins.skyinvasoes.listeners;

import br.com.skyplugins.skyinvasoes.commands.MenuC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MenuL implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e){
        if (MenuC.inv != null && e.getInventory().getName().equals(MenuC.inv.getTitle())) {
            e.setCancelled(true);
        }
    }
}
