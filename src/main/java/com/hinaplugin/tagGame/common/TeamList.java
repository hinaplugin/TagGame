package com.hinaplugin.tagGame.common;

import com.google.common.collect.Lists;
import org.bukkit.entity.Player;

import java.util.List;

public class TeamList {

    private final List<Player> entryList = Lists.newArrayList();

    public List<Player> getEntryList(){ return entryList; }

    private final List<Player> escapeList = Lists.newArrayList();

    public List<Player> getEscapeList(){ return escapeList; }

    private final List<Player> hunterList = Lists.newArrayList();

    public List<Player> getHunterList(){ return hunterList; }

    private final List<Player> disableList = Lists.newArrayList();

    public List<Player> getDisableList(){ return disableList; }

}
