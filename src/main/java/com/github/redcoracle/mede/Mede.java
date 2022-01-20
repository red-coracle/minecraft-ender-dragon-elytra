package com.github.redcoracle.mede;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


public class Mede extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll((Plugin) this);
    }

	@EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof EnderDragon) {
            Player slaying_player = event.getEntity().getKiller();
            ItemStack elytra = new ItemStack(Material.ELYTRA);
            World world = event.getEntity().getWorld();

            if (slaying_player.getInventory().firstEmpty() >= 0) {
                slaying_player.getInventory().addItem(elytra);
            } else {
                world.dropItem(slaying_player.getLocation(), elytra);
            }
        }
    }
}
