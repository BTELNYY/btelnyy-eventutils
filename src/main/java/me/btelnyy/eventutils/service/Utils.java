package me.btelnyy.eventutils.service;

import java.util.Arrays;

import org.bukkit.ChatColor;

public class Utils {

    private static final int SEGMENT_BITS = 0x7F;
    private static final int CONTINUE_BIT = 0x80;
    
    /*
    Allows you to use colours in messages like
    "&cHello!"
    Which would be red
     */
    public static String coloured(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static String colored(String str) {
        return coloured(str);
    }

    public static String buildMessage(String[] parts, boolean ignorefirst) {
        String message = "";
        if(ignorefirst){
            String[] yourArray = Arrays.copyOfRange(parts, 1, parts.length);
            for(String part : yourArray){
                message += part + " ";
            }
        }else{
            for (String part : parts) {
                message += part + " ";
            }
        }
        return message;
    }

    public static byte writeVarInt(int value) {
        while (true) {
            if ((value & ~SEGMENT_BITS) == 0) {
                return (byte) value;
            }
            //value >>>= 7;
            int result = (value & SEGMENT_BITS) | CONTINUE_BIT;
            return (byte) result;
    
            // Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
            
        }
    }
}
