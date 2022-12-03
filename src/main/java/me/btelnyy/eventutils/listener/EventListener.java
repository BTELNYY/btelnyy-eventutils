package me.btelnyy.eventutils.listener;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.injector.PacketConstructor;

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
        World world = event.getPlayer().getLocation().getWorld();
        if(ConfigData.getInstance().PerWorldTablist){
            List<Player> diffworldlist = new ArrayList<Player>();
            List<Player> sameworldlist = new ArrayList<Player>();
            for(Player p : Bukkit.getOnlinePlayers()){
                if(p.getWorld() != world){
                    diffworldlist.add(p);
                }else{
                    sameworldlist.add(p);
                }
            }
            HidePlayerFromTargets(event.getPlayer(), diffworldlist);
            ShowPlayerForTargets(event.getPlayer(), sameworldlist);
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

    private static void HidePlayerFromTargets(Player p, List<Player> targets){
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        //Create new packet
        PacketContainer container = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
        //Tell the client to remove the player from tablist
        container.getSpecificModifier(int.class).write(0, 4);
        //set size of players
        container.getSpecificModifier(int.class).write(1, Bukkit.getOnlinePlayers().size() - 1);
        //tell the player to remove
        Object[] packet = { p.getUniqueId(), 4 };
        container.getSpecificModifier(Object[].class).write(2, packet);
        for(Player onlinep : targets){
            try {
                manager.sendServerPacket(onlinep, container);
            } catch (InvocationTargetException e) {
                EventUtils.getInstance().log(Level.SEVERE, "Failed to send message to client! " +  e.toString());
            }
        }
    }

    private static void ShowPlayerForTargets(Player p, List<Player> targets){
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        //Create new packet
        PacketContainer container = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
        container.getSpecificModifier(int.class).write(0, 0);
        container.getSpecificModifier(int.class).write(1, Bukkit.getOnlinePlayers().size());
        Object[] propertiesarr = { p.getName(), "", false};
        Object[] array = { p.getUniqueId(), p.getName(), 3, propertiesarr, p.getGameMode().ordinal(), p.getPing(), true, false};
        container.getSpecificModifier(Object[].class).write(2, array);
        for(Player onlinep : targets){
            try {
                manager.sendServerPacket(onlinep, container);
            } catch (InvocationTargetException e) {
                EventUtils.getInstance().log(Level.SEVERE, "Failed to send message to client! " +  e.toString());
            }
        }
    }
}