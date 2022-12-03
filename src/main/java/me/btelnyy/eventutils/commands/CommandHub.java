package me.btelnyy.eventutils.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.btelnyy.eventutils.EventUtils;
import me.btelnyy.eventutils.constants.ConfigData;
import me.btelnyy.eventutils.service.Utils;
import me.btelnyy.eventutils.service.file_manager.*;

public class CommandHub implements CommandExecutor{
    private static final Configuration language = EventUtils.getInstance().getFileManager().getFile(FileID.LANGUAGE).getConfiguration();

    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        if(!(sender instanceof Player player)){
            sender.sendMessage(Utils.colored(language.getString("must_be_player")));
            return true;
        }
        if(Bukkit.getWorld(ConfigData.getInstance().HubWorldName) == null){
            sender.sendMessage(Utils.colored(language.getString("invalid_world")));
            return true;
        }
        World world = Bukkit.getWorld(ConfigData.getInstance().HubWorldName);
        player.getLocation().setWorld(world);
        sender.sendMessage(Utils.colored(language.getString("sent_to_hub")));
        return true;
    }
    
}
