package com.github.redcoracle.mcsc;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin implements Listener {
    private static final HashSet<EntityType> explosion_entities = new HashSet<EntityType>() {{
        add(EntityType.CREEPER);
        add(EntityType.FIREBALL);
        add(EntityType.WITHER_SKULL);
    }};

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll((Plugin) this);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void preventBlockExplosion(final EntityExplodeEvent event) {
        if (explosion_entities.contains(event.getEntity().getType())) {
            event.blockList().clear();
        }
    }

    // Drop an Elytra when slaying the Ender Dragon
    @EventHandler
    public void dropElytra(final EntityDeathEvent event) {
        if (event.getEntity() instanceof EnderDragon) {
            Player slaying_player = event.getEntity().getKiller();
            ItemStack elytra = new ItemStack(Material.ELYTRA);
            World world = event.getEntity().getWorld();

            if (slaying_player.getInventory().firstEmpty() >= 0) {
                HashMap added = slaying_player.getInventory().addItem(elytra);
                if (!added.isEmpty()) {
                    world.dropItem(slaying_player.getLocation(), elytra);
                }
            } else {
                world.dropItem(slaying_player.getLocation(), elytra);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void recordDeathCoords(final PlayerDeathEvent event) {
        Player p = event.getEntity();
        this.getLogger().info(String.format(
            "'%s' died at %s, %s, %s",
            p.getName(),
            p.getLocation().getBlockX(),
            p.getLocation().getBlockY(),
            p.getLocation().getBlockZ()
        ));
    }

    @EventHandler
    public void preventCropTrample(final EntityInteractEvent event) {
        if (event.getEntity() instanceof Player) {
            return;
        }

        if (event.getBlock().getType() == Material.FARMLAND) {
            event.setCancelled(true);
        }
    }
}
