package com.hinaplugin.tagGame.listener;

import com.hinaplugin.tagGame.TagGame;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerTouchListener implements Listener {

    @EventHandler
    public void onPlayerTouch(EntityDamageByEntityEvent event){
        if (!TagGame.isStart){
            return;
        }
        final Entity entity = event.getEntity();
        final Entity attack = event.getDamager();
        if (entity instanceof Player target && attack instanceof Player attacker){
            if (TagGame.teamList.getEscapeList().contains(target) && TagGame.teamList.getHunterList().contains(attacker)){
                if (TagGame.teamList.getDisableList().contains(attacker)){
                    return;
                }
                TagGame.teamList.getHunterList().remove(attacker);
                TagGame.teamList.getEscapeList().add(attacker);
                TagGame.teamList.getEscapeList().remove(target);
                TagGame.teamList.getHunterList().add(target);
                target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 255, false, false));
                target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 255, false, false));

                final BossBar bossBar = Bukkit.createBossBar("鬼が復活まで", BarColor.RED, BarStyle.SOLID);
                this.start(bossBar, target);

                for (final Player player : TagGame.teamList.getHunterList()){
                    player.sendMessage(Component.text("鬼が" + attacker.getName() + "から" + target.getName() + "に変わりました．").color(TextColor.color(75, 255, 75)));
                }

                for (final Player player : TagGame.teamList.getEscapeList()){
                    player.sendMessage(Component.text("鬼が" + attacker.getName() + "から" + target.getName() + "に変わりました．").color(TextColor.color(75, 255, 75)));
                }

                TagGame.teamList.getDisableList().add(target);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        TagGame.teamList.getDisableList().remove(target);
                    }
                }.runTaskLaterAsynchronously(TagGame.plugin, 200L);
            }
        }
    }

    private void start(BossBar bossBar, Player player){
        bossBar.setProgress(1.0);
        bossBar.addPlayer(player);
        bossBar.setVisible(true);
        new BukkitRunnable() {
            int i = 10;
            @Override
            public void run() {
                if (i <= -1){
                    bossBar.setVisible(false);
                    bossBar.removeAll();
                    this.cancel();
                    return;
                }
                bossBar.setProgress((double) i / 10);
                bossBar.setTitle("追跡可能まであと" + i + "秒・・・");
                i--;
            }
        }.runTaskTimerAsynchronously(TagGame.plugin, 0L, 20L);
    }
}
