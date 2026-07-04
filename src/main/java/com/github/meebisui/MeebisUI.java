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
public class MeebisUI extends JavaPlugin {

    public static MeebisUI INSTANCE;

    private final Map<Player, MeebisInventory> meebisInventoryTrack = Maps.newHashMap();

    @Override
    public void onEnable() {
        INSTANCE = this;

        Bukkit.getPluginManager().registerEvents(new PlayerInventoryClickListener(), this);
    }

    @Override
    public void onDisable() {
        INSTANCE = null;
    }
}
