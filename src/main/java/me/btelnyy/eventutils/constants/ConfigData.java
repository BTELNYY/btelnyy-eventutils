package me.btelnyy.eventutils.constants;

import me.btelnyy.eventutils.service.file_manager.Configuration;

public class ConfigData {
    private static ConfigData instance;

    public boolean PerWorldDeathMsg;
    public boolean PerWorldTablist;
    public boolean PerWorldChat;

    public String HubWorldName;

    public void load(Configuration config) {
        instance = this;
        PerWorldDeathMsg = config.getBoolean("use_per_world_death_msg");
        PerWorldTablist = config.getBoolean("use_per_world_tablist");
        PerWorldChat = config.getBoolean("use_per_world_chat");
        HubWorldName = config.getString("hub_world_name");

    }
    public static ConfigData getInstance(){
        return instance;
    }
}
