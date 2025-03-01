package com.hinaplugin.tagGame.common;

import com.hinaplugin.tagGame.TagGame;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Timer extends BukkitRunnable {
    private final int allTime;
    private int nowTime;
    private final BossBar bossBar;
    private final TimeReplace timeReplace = new TimeReplace();

    public Timer(int time, BossBar bossBar){
        this.allTime = time;
        this.nowTime = time;
        this.bossBar = bossBar;
    }

    @Override
    public void run() {
        if (nowTime <= -1){
            new EndGame().end(this.bossBar);
            this.cancel();
            return;
        }
        bossBar.setTitle("鬼ごっこ 残り " + timeReplace.replace(nowTime));
        switch (timeReplace.color(allTime, nowTime)){
            case 0 -> bossBar.setColor(BarColor.GREEN);
            case 1 -> bossBar.setColor(BarColor.YELLOW);
            case 2 -> bossBar.setColor(BarColor.RED);
        }

        if (nowTime <= 60){
            for (final Player player : TagGame.teamList.getEscapeList()){
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 40, 255, false, false));
                    }
                }.runTask(TagGame.plugin);
            }
        }
        bossBar.setProgress((double) nowTime / allTime);
        nowTime--;
    }

    public BossBar getBossBar(){
        return bossBar;
    }
}
