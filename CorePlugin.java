package de.deinserver.coreplugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class CorePlugin extends JavaPlugin {

    private static CorePlugin instance;
    private ClanManager clanManager;
    private CombatManager combatManager;
    private RecordManager recordManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.clanManager = new ClanManager(this);
        this.combatManager = new CombatManager(this);
        this.recordManager = new RecordManager(this);

        // Befehle registrieren
        getCommand("clan").setExecutor(clanManager);
        getCommand("clan").setTabCompleter(clanManager);
        getCommand("clanchat").setExecutor(clanManager);
        getCommand("rekord").setExecutor(recordManager);

        // Events registrieren
        Bukkit.getPluginManager().registerEvents(clanManager, this);
        Bukkit.getPluginManager().registerEvents(combatManager, this);
        Bukkit.getPluginManager().registerEvents(recordManager, this);

        // Beim Reload alle online Spieler aktualisieren
        Bukkit.getOnlinePlayers().forEach(p -> {
            clanManager.updatePlayerNametag(p);
            combatManager.checkPendingReturns(p);
        });
        recordManager.checkRecord();

        getLogger().info("CorePlugin (1.21.11) erfolgreich aktiviert!");
    }

    @Override
    public void onDisable() {
        if (clanManager != null) clanManager.saveClans();
        if (combatManager != null) combatManager.savePendingReturns();
        if (recordManager != null) recordManager.saveRecord();
        getLogger().info("CorePlugin wurde deaktiviert und alle Daten gespeichert.");
    }

    public static CorePlugin getInstance() {
        return instance;
    }

    public ClanManager getClanManager() {
        return clanManager;
    }
}
