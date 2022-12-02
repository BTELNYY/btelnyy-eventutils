package me.btelnyy.mcexampleplugin.listener;

import org.bukkit.event.Listener;

import me.btelnyy.mcexampleplugin.McExmaplePlugin;
import me.btelnyy.mcexampleplugin.service.file_manager.Configuration;
import me.btelnyy.mcexampleplugin.service.file_manager.FileID;

public class EventListener implements Listener {
    private static final Configuration language = McExmaplePlugin.getInstance().getFileManager().getFile(FileID.LANGUAGE).getConfiguration();
    
}
