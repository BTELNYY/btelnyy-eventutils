package me.btelnyy.eventutils.constants;

import me.btelnyy.eventutils.service.file_manager.Configuration;

public class ConfigData {
    private static ConfigData instance;
    public boolean PerWorldDeathMsg;
    public boolean PerWorldTablist;

    public void load(Configuration config) {
        instance = this;
        PerWorldDeathMsg = config.getBoolean("use_per_world_death_msg");
        PerWorldTablist = config.getBoolean("use_per_world_tablist");
    }
    public static ConfigData getInstance(){
        return instance;
    }
}
