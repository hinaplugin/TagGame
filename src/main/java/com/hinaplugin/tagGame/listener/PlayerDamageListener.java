package com.hinaplugin.tagGame.listener;

import com.hinaplugin.tagGame.TagGame;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamageListener implements Listener {

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event){
        if (!TagGame.isStart){
            return;
        }

        final Entity entity = event.getEntity();

        if (entity instanceof Player player){
            if (TagGame.teamList.getEscapeList().contains(player) || TagGame.teamList.getHunterList().contains(player)){
                event.setCancelled(true);
            }
        }
    }
}
