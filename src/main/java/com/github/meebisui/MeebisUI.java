package com.github.meebisui;

import com.github.meebisui.inventory.MeebisInventory;
import com.github.meebisui.listener.PlayerInventoryClickListener;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

@Getter
@Accessors(fluent = true)
public class MeebisUI {

    private static MeebisUI INSTANCE;

    private final Map<Player, MeebisInventory> meebisInventoryTrack = Maps.newHashMap();

    public MeebisUI() {
        INSTANCE = new MeebisUI();
    }

    /**
     * This needs to be called at some point
     * in order to initialize the MeebisUI
     * @param plugin
     */
    public void loadPlugin(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new PlayerInventoryClickListener(), plugin);
    }

    public static MeebisUI INSTANCE() {
        if(INSTANCE == null) {
            INSTANCE = new MeebisUI();
        }
        return INSTANCE;
    }
}
