package com.hinaplugin.tagGame.common;

import com.hinaplugin.tagGame.TagGame;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

public class EndGame {

    public void end(BossBar bossBar){
        bossBar.setVisible(false);
        bossBar.removeAll();
        for (final Player player : TagGame.teamList.getEscapeList()){
            new BukkitRunnable() {
                @Override
                public void run() {
                    final int x = TagGame.config.getInt("end-game.x", 0);
                    final int y = TagGame.config.getInt("end-game.y", 0);
                    final int z = TagGame.config.getInt("end-game.z", 0);
                    final float yaw = (float) TagGame.config.getDouble("end-game.yaw", 0.0);
                    final float pitch = (float) TagGame.config.getDouble("end-game.pitch", 0.0);
                    player.teleport(new Location(player.getWorld(), x, y, z, yaw, pitch));
                    player.sendMessage(Component.text("鬼ごっこが終了しました！\nあなたは逃走成功です！").color(TextColor.color(75, 255, 75)));
                    for (final Player target : TagGame.teamList.getHunterList()){
                        player.sendMessage(Component.text("鬼は" + target.getName() + "でした．").color(TextColor.color(255, 75, 75)));
                    }
                }
            }.runTask(TagGame.plugin);
        }

        for (final Player player : TagGame.teamList.getHunterList()){
            new BukkitRunnable() {
                @Override
                public void run() {
                    final int x = TagGame.config.getInt("end-game.x", 0);
                    final int y = TagGame.config.getInt("end-game.y", 0);
                    final int z = TagGame.config.getInt("end-game.z", 0);
                    final float yaw = (float) TagGame.config.getDouble("end-game.yaw", 0.0);
                    final float pitch = (float) TagGame.config.getDouble("end-game.pitch", 0.0);
                    player.teleport(new Location(player.getWorld(), x, y, z, yaw, pitch));
                    player.sendMessage(Component.text("鬼ごっこが終了しました！\nあなたは鬼でした！").color(TextColor.color(75, 255, 75)));
                }
            }.runTask(TagGame.plugin);
        }

        TagGame.team.unregister();
        TagGame.team = TagGame.plugin.getServer().getScoreboardManager().getMainScoreboard().registerNewTeam("taggame");
        TagGame.team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);

        TagGame.teamList.getEntryList().addAll(TagGame.teamList.getEscapeList());
        TagGame.teamList.getEntryList().addAll(TagGame.teamList.getHunterList());
        TagGame.teamList.getEscapeList().clear();
        TagGame.teamList.getHunterList().clear();
        TagGame.teamList.getDisableList().clear();

        TagGame.isStart = false;
        TagGame.timer = null;
    }

    public void stop(){
        TagGame.timer.getBossBar().setVisible(false);
        TagGame.timer.getBossBar().removeAll();
        TagGame.timer.cancel();
        for (final Player player : TagGame.teamList.getEscapeList()){
            new BukkitRunnable() {
                @Override
                public void run() {
                    final int x = TagGame.config.getInt("end-game.x", 0);
                    final int y = TagGame.config.getInt("end-game.y", 0);
                    final int z = TagGame.config.getInt("end-game.z", 0);
                    final float yaw = (float) TagGame.config.getDouble("end-game.yaw", 0.0);
                    final float pitch = (float) TagGame.config.getDouble("end-game.pitch", 0.0);
                    player.teleport(new Location(player.getWorld(), x, y, z, yaw, pitch));
                    player.sendMessage(Component.text("鬼ごっこが強制終了しました．").color(TextColor.color(255, 75, 75)));
                }
            }.runTask(TagGame.plugin);
        }

        for (final Player player : TagGame.teamList.getHunterList()){
            new BukkitRunnable() {
                @Override
                public void run() {
                    final int x = TagGame.config.getInt("end-game.x", 0);
                    final int y = TagGame.config.getInt("end-game.y", 0);
                    final int z = TagGame.config.getInt("end-game.z", 0);
                    final float yaw = (float) TagGame.config.getDouble("end-game.yaw", 0.0);
                    final float pitch = (float) TagGame.config.getDouble("end-game.pitch", 0.0);
                    player.teleport(new Location(player.getWorld(), x, y, z, yaw, pitch));
                    player.sendMessage(Component.text("鬼ごっこが強制終了しました．").color(TextColor.color(255, 75, 75)));
                }
            }.runTask(TagGame.plugin);
        }

        TagGame.team.unregister();
        TagGame.team = TagGame.plugin.getServer().getScoreboardManager().getMainScoreboard().registerNewTeam("taggame");
        TagGame.team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);

        TagGame.teamList.getEntryList().addAll(TagGame.teamList.getEscapeList());
        TagGame.teamList.getEntryList().addAll(TagGame.teamList.getHunterList());
        TagGame.teamList.getEscapeList().clear();
        TagGame.teamList.getHunterList().clear();
        TagGame.teamList.getDisableList().clear();

        TagGame.isStart = false;
        TagGame.timer = null;
    }
}
