package me.btelnyy.mcexampleplugin.constants;

import me.btelnyy.mcexampleplugin.service.file_manager.Configuration;

public class ConfigData {
    private static ConfigData instance;

    public void load(Configuration config) {
        instance = this;
    }
    public static ConfigData getInstance(){
        return instance;
    }
}
