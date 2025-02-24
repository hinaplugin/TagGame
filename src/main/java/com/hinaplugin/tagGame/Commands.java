package com.hinaplugin.tagGame;

import com.google.common.collect.Lists;
import com.hinaplugin.tagGame.common.EndGame;
import com.hinaplugin.tagGame.common.Timer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class Commands implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length >= 1){
            if (!strings[0].isEmpty()){
                if (strings[0].equalsIgnoreCase("reload")){
                    if (commandSender.hasPermission("taggame.commands.reload")){
                        TagGame.plugin.noSaveReloadConfig();
                        commandSender.sendMessage(Component.text("config.ymlを再読み込みしました．").color(TextColor.color(75, 255, 75)));
                        return true;
                    }
                }

                if (commandSender instanceof Player player){
                    switch (strings[0]){
                        case "join" -> {
                            if (strings.length == 1){
                                if (TagGame.isStart){
                                    player.sendMessage(Component.text("既に開始されているため実行できません．").color(TextColor.color(255, 75, 75)));
                                    return true;
                                }

                                if (!player.hasPermission("taggame.commands.join")){
                                    player.sendMessage(Component.text("このコマンドを実行するための権限がありません．").color(TextColor.color(255, 75, 75)));
                                    return true;
                                }

                                if (TagGame.teamList.getEntryList().contains(player)){
                                    player.sendMessage(Component.text("既に参加しているため実行できません．").color(TextColor.color(255, 75, 75)));
                                    return true;
                                }

                                TagGame.teamList.getEntryList().add(player);
                                player.sendMessage(Component.text("鬼ごっこの待機グループに参加しました．").color(TextColor.color(75, 255, 75)));
                            }else {
                                if (strings.length == 2){
                                    if (TagGame.isStart){
                                        player.sendMessage(Component.text("既に開始されているため実行できません．").color(TextColor.color(255, 75, 75)));
                                        return true;
                                    }

                                    if (!player.hasPermission("taggame.commands.join.other")){
                                        player.sendMessage(Component.text("このコマンドを実行するための権限がありません．").color(TextColor.color(255, 75, 75)));
                                        return true;
                                    }

                                    final Player target = TagGame.plugin.getServer().getPlayer(strings[1]);
                                    if (target == null){
                                        player.sendMessage(Component.text(strings[1] + "が見つかりませんでした．").color(TextColor.color(255, 75, 75)));
                                        return true;
                                    }

                                    if (TagGame.teamList.getEntryList().contains(target)){
                                        player.sendMessage(Component.text("既に" + strings[1] + "は参加しているため実行できません．").color(TextColor.color(255, 75, 75)));
                                        return true;
                                    }

                                    TagGame.teamList.getEntryList().add(target);
                                    player.sendMessage(Component.text("鬼ごっこの待機グループに" + strings[1] + "を参加させました．").color(TextColor.color(75, 255, 75)));
                                    target.sendMessage(Component.text("鬼ごっこの待機グループに" + player.getName() + "により参加しました．").color(TextColor.color(75, 255, 75)));
                                }
                            }
                        }
                        case "joinall" -> {
                            if (TagGame.isStart){
                                player.sendMessage(Component.text("既に開始されているため実行できません．").color(TextColor.color(255, 75, 75)));
                                return true;
                            }

                            if (!player.hasPermission("taggame.commands.join.all")){
                                player.sendMessage(Component.text("このコマンドを実行するための権限がありません．").color(TextColor.color(255, 75, 75)));
                                return true;
                            }

                            int i = 0;
                            for (final Player target : TagGame.plugin.getServer().getOnlinePlayers()){
                                if (TagGame.teamList.getEntryList().contains(target)){
                                    continue;
                                }

                                TagGame.teamList.getEntryList().add(target);
                                target.sendMessage(Component.text("鬼ごっこの待機グループに" + player.getName() + "により参加しました．").color(TextColor.color(75, 255, 75)));
                                i++;
                            }

                            player.sendMessage(Component.text(i + "名のプレイヤーを参加させました．").color(TextColor.color(75, 255, 75)));
                        }
                        case "leave" -> {
                            if (strings.length == 1){
                                if (TagGame.isStart){
                                    player.sendMessage(Component.text("既に開始されているため実行できません．").color(TextColor.color(255, 75, 75)));
                                    return true;
                                }

                                if (!player.hasPermission("taggame.commands.leave")){
                                    player.sendMessage(Component.text("このコマンドを実行するための権限がありません．").color(TextColor.color(255, 75, 75)));
                                    return true;
                                }

                                if (!TagGame.teamList.getEntryList().contains(player)){
                                    player.sendMessage(Component.text("参加していないため実行できません．").color(TextColor.color(255, 75, 75)));
                                    return true;
                                }

                                TagGame.teamList.getEntryList().remove(player);
                                player.sendMessage(Component.text("鬼ごっこの待機グループから退室しました．").color(TextColor.color(75, 255, 75)));
                            }else {
                                if (strings.length == 2){
                                    if (TagGame.isStart){
                                        player.sendMessage(Component.text("既に開始されているため実行できません．").color(TextColor.color(255, 75, 75)));
                                        return true;
                                    }

                                    if (!player.hasPermission("taggame.commands.leave.other")){
                                        player.sendMessage(Component.text("このコマンドを実行するための権限がありません．").color(TextColor.color(255, 75, 75)));
                                        return true;
                                    }

                                    final Player target = TagGame.plugin.getServer().getPlayer(strings[1]);
                                    if (target == null){
                                        player.sendMessage(Component.text(strings[1] + "が見つかりませんでした．").color(TextColor.color(255, 75, 75)));
                                        return true;
                                    }

                                    if (TagGame.teamList.getEntryList().contains(target)){
                                        player.sendMessage(Component.text(strings[1] + "は参加していないため実行できません．").color(TextColor.color(255, 75, 75)));
                                        return true;
                                    }

                                    TagGame.teamList.getEntryList().remove(target);
                                    player.sendMessage(Component.text("鬼ごっこの待機グループから" + strings[1] + "を退室させました．").color(TextColor.color(75, 255, 75)));
                                    target.sendMessage(Component.text("鬼ごっこの待機グループから" + player.getName() + "により退室しました．").color(TextColor.color(75, 255, 75)));
                                }
                            }
                        }
                        case "start" -> {
                            if (TagGame.isStart){
                                player.sendMessage(Component.text("既に開始されているため実行できません．").color(TextColor.color(255, 75, 75)));
                                return true;
                            }

                            if (!player.hasPermission("taggame.commands.start")){
                                player.sendMessage(Component.text("このコマンドを実行するための権限がありません．").color(TextColor.color(255, 75, 75)));
                                return true;
                            }

                            if (TagGame.teamList.getEntryList().size() < 1 + TagGame.config.getInt("hunter", 2)){
                                player.sendMessage(Component.text("必要な人数に満たないため開始できません．").color(TextColor.color(255, 75, 75)));
                                return true;
                            }

                            TagGame.bossBar = Bukkit.createBossBar("残り時間", BarColor.GREEN, BarStyle.SOLID);
                            TagGame.bossBar.setProgress(1.0);
                            TagGame.bossBar.setVisible(false);

                            final BossBar bossBar = Bukkit.createBossBar("まもなく開始", BarColor.GREEN, BarStyle.SOLID);
                            bossBar.setProgress(1.0);
                            bossBar.setVisible(true);
                            TagGame.timer = new Timer(TagGame.config.getInt("time", 10) * 60, TagGame.bossBar);

                            final Random random = new Random();
                            for (int i = 0; i < TagGame.config.getInt("hunter", 2); i++){
                                final int index = random.nextInt(TagGame.teamList.getEntryList().size());
                                final Player target = TagGame.teamList.getEntryList().get(index);
                                TagGame.teamList.getHunterList().add(target);
                                TagGame.teamList.getEntryList().remove(target);
                            }

                            TagGame.teamList.getEscapeList().addAll(TagGame.teamList.getEntryList());
                            TagGame.teamList.getEntryList().clear();

                            for (final Player target : TagGame.teamList.getEscapeList()){
                                target.sendMessage(Component.text("まもなく鬼ごっこを開始します・・・").color(TextColor.color(75, 255, 75)));
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        final int x = TagGame.config.getInt("escape-start.x", 0);
                                        final int y = TagGame.config.getInt("escape-start.y", 0);
                                        final int z = TagGame.config.getInt("escape-start.z", 0);
                                        final float yaw = (float) TagGame.config.getDouble("escape-start.yaw", 0.0);
                                        final float pitch = (float) TagGame.config.getDouble("escape-start.pitch", 0.0);
                                        target.teleport(new Location(player.getWorld(), x, y, z, yaw, pitch));
                                        TagGame.bossBar.addPlayer(target);
                                        bossBar.addPlayer(target);
                                        target.setGameMode(GameMode.ADVENTURE);
                                        target.sendMessage(Component.text("あなたは逃走者チームになりました！").color(TextColor.color(75, 255, 75)));
                                        target.getInventory().clear();
                                        final ItemStack itemStack = new ItemStack(Material.COOKED_BEEF, 64);
                                        target.getInventory().addItem(itemStack);
                                        TagGame.team.addPlayer(target);
                                    }
                                }.runTask(TagGame.plugin);
                            }

                            for (final Player target : TagGame.teamList.getHunterList()){
                                target.sendMessage(Component.text("まもなく鬼ごっこを開始します・・・").color(TextColor.color(75, 255, 75)));
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        final int x = TagGame.config.getInt("hunter-start.x", 0);
                                        final int y = TagGame.config.getInt("hunter-start.y", 0);
                                        final int z = TagGame.config.getInt("hunter-start.z", 0);
                                        final float yaw = (float) TagGame.config.getDouble("hunter-start.yaw", 0.0);
                                        final float pitch = (float) TagGame.config.getDouble("hunter-start.pitch", 0.0);
                                        target.teleport(new Location(player.getWorld(), x, y, z, yaw, pitch));
                                        TagGame.bossBar.addPlayer(target);
                                        bossBar.addPlayer(target);
                                        target.setGameMode(GameMode.ADVENTURE);
                                        target.sendMessage(Component.text("あなたは鬼チームになりました！").color(TextColor.color(255, 75, 75)));
                                        target.getInventory().clear();
                                        final ItemStack itemStack = new ItemStack(Material.COOKED_BEEF, 64);
                                        target.getInventory().addItem(itemStack);
                                        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 255, false, false));
                                        TagGame.team.addPlayer(target);
                                    }
                                }.runTask(TagGame.plugin);
                            }

                            for (final Player target : TagGame.teamList.getHunterList()){
                                for (final Player targetPlayer : TagGame.teamList.getEscapeList()){
                                    targetPlayer.sendMessage(Component.text("鬼は" + target.getName() + "です．").color(TextColor.color(255, 75, 75)));
                                }
                            }

                            new BukkitRunnable() {
                                int i = 10;

                                @Override
                                public void run() {
                                    if (i <= -1){
                                        bossBar.setVisible(false);
                                        bossBar.removeAll();
                                        TagGame.timer.runTaskTimerAsynchronously(TagGame.plugin, 0L, 20L);
                                        TagGame.bossBar.setVisible(true);
                                        this.cancel();
                                        return;
                                    }
                                    bossBar.setProgress((double) i / 10);
                                    bossBar.setTitle("鬼ごっこ開始まで　残り: " + i + "秒");
                                    i--;
                                }
                            }.runTaskTimerAsynchronously(TagGame.plugin, 0L, 20L);
                            TagGame.isStart = true;
                        }
                        case "set" -> {
                            if (TagGame.isStart){
                                player.sendMessage(Component.text("既に開始されているため実行できません．").color(TextColor.color(255, 75, 75)));
                                return true;
                            }

                            if (!player.hasPermission("taggame.commands.set")){
                                player.sendMessage(Component.text("このコマンドを実行するための権限がありません．").color(TextColor.color(255, 75, 75)));
                                return true;
                            }

                            if (strings.length == 2){
                                if (!strings[1].isEmpty()){
                                    switch (strings[1]){
                                        case "escape" -> {
                                            final Location location = player.getLocation();
                                            final int x = location.getBlockX();
                                            final int y = location.getBlockY();
                                            final int z = location.getBlockZ();
                                            final float yaw = location.getYaw();
                                            final float pitch = location.getPitch();
                                            TagGame.config.set("escape-start.x", x);
                                            TagGame.config.set("escape-start.y", y);
                                            TagGame.config.set("escape-start.z", z);
                                            TagGame.config.set("escape-start.yaw", yaw);
                                            TagGame.config.set("escape-start.pitch", pitch);
                                            TagGame.plugin.saveReloadConfig();
                                            player.sendMessage(Component.text("逃走者チームの開始地点を登録しました．").color(TextColor.color(75, 255, 75)));
                                        }
                                        case "hunter" -> {
                                            final Location location = player.getLocation();
                                            final int x = location.getBlockX();
                                            final int y = location.getBlockY();
                                            final int z = location.getBlockZ();
                                            final float yaw = location.getYaw();
                                            final float pitch = location.getPitch();
                                            TagGame.config.set("hunter-start.x", x);
                                            TagGame.config.set("hunter-start.y", y);
                                            TagGame.config.set("hunter-start.z", z);
                                            TagGame.config.set("hunter-start.yaw", yaw);
                                            TagGame.config.set("hunter-start.pitch", pitch);
                                            TagGame.plugin.saveReloadConfig();
                                            player.sendMessage(Component.text("鬼チームの開始地点を登録しました．").color(TextColor.color(75, 255, 75)));
                                        }
                                        case "end" -> {
                                            final Location location = player.getLocation();
                                            final int x = location.getBlockX();
                                            final int y = location.getBlockY();
                                            final int z = location.getBlockZ();
                                            final float yaw = location.getYaw();
                                            final float pitch = location.getPitch();
                                            TagGame.config.set("end-game.x", x);
                                            TagGame.config.set("end-game.y", y);
                                            TagGame.config.set("end-game.z", z);
                                            TagGame.config.set("end-game.yaw", yaw);
                                            TagGame.config.set("end-game.pitch", pitch);
                                            TagGame.plugin.saveReloadConfig();
                                            player.sendMessage(Component.text("ゲーム終了後の集合地点を登録しました．").color(TextColor.color(75, 255, 75)));
                                        }
                                    }
                                }
                            }else if (strings.length == 3){
                                if (!strings[1].isEmpty()){
                                    switch (strings[1]){
                                        case "size" -> {
                                            if (!strings[2].isEmpty()){
                                                if (strings[2].chars().allMatch(Character::isDigit)){
                                                    final int size = Integer.parseInt(strings[2]);
                                                    TagGame.config.set("hunter", size);
                                                    TagGame.plugin.saveReloadConfig();
                                                    player.sendMessage(Component.text("ハンターの人数を" + size + "人に設定しました．").color(TextColor.color(75, 255, 75)));
                                                }
                                            }
                                        }
                                        case "time" -> {
                                            if (!strings[2].isEmpty()){
                                                if (strings[2].chars().allMatch(Character::isDigit)){
                                                    final int time = Integer.parseInt(strings[2]);
                                                    TagGame.config.set("time", time);
                                                    TagGame.plugin.saveReloadConfig();
                                                    player.sendMessage(Component.text("制限時間を" + time + "分に設定しました．").color(TextColor.color(75, 255, 75)));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        case "list" -> {
                            if (!player.hasPermission("taggame.commands.list")){
                                player.sendMessage(Component.text("このコマンドを実行するための権限がありません．").color(TextColor.color(255, 75, 75)));
                                return true;
                            }

                            if (TagGame.isStart){
                                int i = 1;
                                player.sendMessage(Component.text("----- 参加者一覧(鬼) -----"));
                                for (final Player target : TagGame.teamList.getHunterList()){
                                    player.sendMessage(Component.text(i + ". " + target.getName()).color(TextColor.color(255, 75, 75)));
                                    i++;
                                }

                                i = 1;
                                player.sendMessage(Component.text("----- 参加者一覧(逃走者) -----"));
                                for (final Player target : TagGame.teamList.getEscapeList()){
                                    player.sendMessage(Component.text(i + ". " + target.getName()).color(TextColor.color(75, 255, 255)));
                                    i++;
                                }
                            }else {
                                int i = 1;
                                player.sendMessage(Component.text("----- 参加者一覧 -----"));
                                for (final Player target : TagGame.teamList.getEntryList()){
                                    player.sendMessage(Component.text(i + ". " + target.getName()).color(TextColor.color(75, 255, 75)));
                                    i++;
                                }
                            }
                        }
                        case "stop" -> {
                            if (!TagGame.isStart){
                                player.sendMessage(Component.text("まだ開始されていないため実行できません．").color(TextColor.color(255, 75, 75)));
                                return true;
                            }

                            if (!player.hasPermission("taggame.commands.stop")){
                                player.sendMessage(Component.text("このコマンドを実行するための権限がありません．").color(TextColor.color(255, 75, 75)));
                                return true;
                            }

                            new EndGame().stop();
                            player.sendMessage(Component.text("鬼ごっこをコマンドで強制終了させました．").color(TextColor.color(75, 255, 75)));
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        final List<String> complete = Lists.newArrayList();
        if (commandSender.hasPermission("taggame.commands.reload")){
            if (strings.length == 0){
                complete.add("reload");
            }else if (strings.length == 1){
                if (strings[0].isEmpty()){
                    complete.add("reload");
                }else if ("reload".startsWith(strings[0])){
                    complete.add("reload");
                }
            }
        }

        if (commandSender instanceof Player player){
            if (player.hasPermission("taggame.commands.join") || player.hasPermission("taggame.commands.join.other")){
                if (strings.length == 0){
                    complete.add("join");
                }else if (strings.length == 1){
                    if (strings[0].isEmpty()){
                        complete.add("join");
                    }else if ("join".startsWith(strings[0])){
                        complete.add("join");
                    }
                }else if (strings.length == 2){
                    if (strings[0].equalsIgnoreCase("join")){
                        if (strings[1].isEmpty()){
                            if (player.hasPermission("taggame.commands.join.other")){
                                for (final Player target : TagGame.plugin.getServer().getOnlinePlayers()){
                                    complete.add(target.getName());
                                }
                            }else {
                                complete.add(player.getName());
                            }
                        }else {
                            if (player.hasPermission("taggame.commands.join.other")){
                                for (final Player target : TagGame.plugin.getServer().getOnlinePlayers()){
                                    if (target.getName().startsWith(strings[1])){
                                        complete.add(target.getName());
                                    }
                                }
                            }else {
                                if (player.getName().startsWith(strings[1])){
                                    complete.add(player.getName());
                                }
                            }
                        }
                    }
                }
            }
            if (player.hasPermission("taggame.commands.join.all")){
                if (strings.length == 0){
                    complete.add("joinall");
                }else if (strings.length == 1){
                    if (strings[0].isEmpty()){
                        complete.add("joinall");
                    }else if ("joinall".startsWith(strings[0])){
                        complete.add("joinall");
                    }
                }
            }
            if (player.hasPermission("taggame.commands.leave")){
                if (strings.length == 0){
                    complete.add("leave");
                }else if (strings.length == 1){
                    if (strings[0].isEmpty()){
                        complete.add("leave");
                    }else if ("leave".startsWith(strings[0])){
                        complete.add("leave");
                    }
                }
            }
            if (player.hasPermission("taggame.commands.start")){
                if (strings.length == 0){
                    complete.add("start");
                }else if (strings.length == 1){
                    if (strings[0].isEmpty()){
                        complete.add("start");
                    }else if ("start".startsWith(strings[0])){
                        complete.add("start");
                    }
                }
            }
            if (player.hasPermission("taggame.commands.set")){
                if (strings.length == 0){
                    complete.add("set");
                }else if (strings.length == 1){
                    if (strings[0].isEmpty()){
                        complete.add("set");
                    }else if ("set".startsWith(strings[0])){
                        complete.add("set");
                    }
                }else if (strings.length == 2){
                    if (strings[0].equalsIgnoreCase("set")){
                        if (strings[1].isEmpty()){
                            complete.add("escape");
                            complete.add("hunter");
                            complete.add("end");
                        }else {
                            if ("escape".startsWith(strings[1])){
                                complete.add("escape");
                            }
                            if ("hunter".startsWith(strings[1])){
                                complete.add("hunter");
                            }
                            if ("end".startsWith(strings[1])){
                                complete.add("end");
                            }
                        }
                    }
                }
            }
            if (player.hasPermission("taggame.commands.list")){
                if (strings.length == 0){
                    complete.add("list");
                }else if (strings.length == 1){
                    if (strings[0].isEmpty()){
                        complete.add("list");
                    }else if ("list".startsWith(strings[0])){
                        complete.add("list");
                    }
                }
            }
            if (player.hasPermission("taggame.commands.stop")){
                if (strings.length == 0){
                    complete.add("stop");
                }else if (strings.length == 1){
                    if (strings[0].isEmpty()){
                        complete.add("stop");
                    }else if ("stop".startsWith(strings[0])){
                        complete.add("stop");
                    }
                }
            }
        }
        return complete;
    }
}
