package com.hinaplugin.tagGame;

import com.hinaplugin.tagGame.common.TeamList;
import com.hinaplugin.tagGame.common.Timer;
import com.hinaplugin.tagGame.listener.PlayerDamageByBlockListener;
import com.hinaplugin.tagGame.listener.PlayerDamageByEntityListener;
import com.hinaplugin.tagGame.listener.PlayerDamageListener;
import com.hinaplugin.tagGame.listener.PlayerTouchListener;
import org.bukkit.boss.BossBar;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

public final class TagGame extends JavaPlugin {
    public static TagGame plugin;
    public static FileConfiguration config;
    public static TeamList teamList;
    public static boolean isStart = false;
    public static BossBar bossBar;
    public static Timer timer;
    public static Team team;

    @Override
    public void onEnable() {
        // Plugin startup logic
        try {
            plugin = this;

            final File configFile = new File(this.getDataFolder(), "config.yml");
            if (!configFile.exists()){
                this.saveDefaultConfig();
            }

            config = this.getConfig();

            teamList = new TeamList();

            final PluginCommand command = this.getCommand("taggame");
            if (command != null){
                command.setExecutor(new Commands());
            }

            this.getServer().getPluginManager().registerEvents(new PlayerTouchListener(), this);
            this.getServer().getPluginManager().registerEvents(new PlayerDamageListener(), this);
            this.getServer().getPluginManager().registerEvents(new PlayerDamageByBlockListener(), this);
            this.getServer().getPluginManager().registerEvents(new PlayerDamageByEntityListener(), this);

            final Scoreboard scoreboard = this.getServer().getScoreboardManager().getMainScoreboard();
            team = scoreboard.getTeam("taggame");
            if (team != null){
                team.unregister();
            }
            team = scoreboard.registerNewTeam("taggame");
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        }catch (Exception exception){
            exception.printStackTrace(new PrintWriter(new StringWriter()));
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        try {
            HandlerList.unregisterAll(this);
            if (timer != null){
                timer.getBossBar().setVisible(false);
                timer.getBossBar().removeAll();
                timer.cancel();
            }
        }catch (Exception exception){
            exception.printStackTrace(new PrintWriter(new StringWriter()));
        }
    }

    public void saveReloadConfig(){
        this.saveConfig();
        this.reloadConfig();
        config = this.getConfig();
    }

    public void noSaveReloadConfig(){
        this.reloadConfig();
        config = this.getConfig();
    }
}
