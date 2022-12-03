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
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.EnumWrappers.NativeGameMode;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerInfoAction;

import me.btelnyy.eventutils.EventUtils;
import me.btelnyy.eventutils.constants.ConfigData;
import me.btelnyy.eventutils.data.WrapperPlayServerPlayerInfo;
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

    public void OnPlayerChat(AsyncPlayerChatEvent event){
        if(ConfigData.getInstance().PerWorldChat){
            Player sender = event.getPlayer();
            World world = sender.getWorld();
            for(Player p : event.getRecipients()){
                if(p.getWorld() != world){
                    event.getRecipients().remove(p);
                }
            }
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

    private static void HidePlayerFromTargets(Player p, List<Player> targets){;
        WrapperPlayServerPlayerInfo packet = new WrapperPlayServerPlayerInfo();
        packet.setAction(PlayerInfoAction.REMOVE_PLAYER);
        WrappedChatComponent chat = WrappedChatComponent.fromText(p.getDisplayName());
        PlayerInfoData data = new PlayerInfoData(WrappedGameProfile.fromPlayer(p), p.getPing(), NativeGameMode.SURVIVAL, chat);
        List<PlayerInfoData> array = new ArrayList<PlayerInfoData>();
        array.add(data);
        packet.setData(array);
        for(Player onlinep : targets){
            try {
                packet.sendPacket(onlinep);
            } catch (Exception e) {
                EventUtils.getInstance().log(Level.SEVERE, "Failed to send message to client! " +  e.toString());
            }
        }
    }

    private static void ShowPlayerForTargets(Player p, List<Player> targets){
        WrapperPlayServerPlayerInfo packet = new WrapperPlayServerPlayerInfo();
        packet.setAction(PlayerInfoAction.ADD_PLAYER);
        WrappedChatComponent chat = WrappedChatComponent.fromText(p.getDisplayName());
        PlayerInfoData data = new PlayerInfoData(WrappedGameProfile.fromPlayer(p), p.getPing(), NativeGameMode.SURVIVAL, chat);
        List<PlayerInfoData> array = new ArrayList<PlayerInfoData>();
        array.add(data);
        packet.setData(array);
        for(Player onlinep : targets){
            try {
                packet.sendPacket(onlinep);
            } catch (Exception e) {
                EventUtils.getInstance().log(Level.SEVERE, "Failed to send message to client! " +  e.toString());
            }
        }
    }
}