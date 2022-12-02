package me.btelnyy.eventutils.listener;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import me.btelnyy.eventutils.EventUtils;
import me.btelnyy.eventutils.constants.ConfigData;
import me.btelnyy.eventutils.service.file_manager.Configuration;
import me.btelnyy.eventutils.service.file_manager.FileID;

public class EventListener implements Listener {
    private static final Configuration language = EventUtils.getInstance().getFileManager().getFile(FileID.LANGUAGE).getConfiguration();
    
    @EventHandler
    public void OnPlayerDeath(PlayerDeathEvent event){
        if(ConfigData.getInstance().PerWorldDeathMsg){
            PlayerDeathMessageModifier(event);
        }
    }

    @EventHandler
    public void OnPlayerChangeWorld(PlayerChangedWorldEvent event){
        if(ConfigData.getInstance().PerWorldTablist){
            HidePlayerFromNonWorlders(event.getPlayer());
        }
    }


    private static void PlayerDeathMessageModifier(PlayerDeathEvent event){
        World world = event.getEntity().getLocation().getWorld();
        String deathmsg = event.getDeathMessage();
        event.setDeathMessage(null);
        for(Player p : Bukkit.getOnlinePlayers()){
            if(p.getLocation().getWorld() == world){
                p.sendMessage(deathmsg);
            }
        }
    }

    private static void HidePlayerFromNonWorlders(Player p){
        for(Player onlinep : Bukkit.getOnlinePlayers()){

        }
    }
}