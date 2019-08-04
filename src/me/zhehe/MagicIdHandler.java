/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.zhehe;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Zhehe
 */
public class MagicIdHandler {
    private static Map<String, BlockData> map = new HashMap<>();
    private static Map<String, Material> matmap = new HashMap<>();
    
    static class Json {
        Map<String, String> blocks = new HashMap<>();
        Map<String, String> items = new HashMap<>();
    }
    
    public void init(JavaPlugin plugin) {
        try {
            InputStream in = plugin.getResource("legacy.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                sb.append(line);
                line = reader.readLine();
            }
            Json json = (new Gson()).fromJson(sb.toString(), Json.class);
            map.clear();
            matmap.clear();
            for(Map.Entry<String,String> entry : json.blocks.entrySet()) {
                BlockData data;
                try {
                    data = Bukkit.createBlockData(entry.getValue());
                } catch (Exception ex) {
                    data = Bukkit.createBlockData(Material.AIR);
                }
                map.put(entry.getKey(), data);
            }
            for(Map.Entry<String,String> entry : json.items.entrySet()) {
                Material mat;
                try {
                    mat = Material.getMaterial(entry.getValue());
                } catch (Exception ex) {
                    mat = Material.AIR;
                }
                matmap.put(entry.getKey(), mat);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(MagicIdHandler.class.getName()).log(Level.SEVERE, null, ex);
            Bukkit.getLogger().log(Level.SEVERE, ex.toString());
        }
    }
    
    public static BlockData fromId(int id, int data) {
        String index = Integer.toString(id) + ":" + Integer.toString(data);
        return map.get(index);
    }
    
    public static Material fromId(int id) {
        String index = Integer.toString(id) + ":0";
        return matmap.get(index);
    }
    
}
