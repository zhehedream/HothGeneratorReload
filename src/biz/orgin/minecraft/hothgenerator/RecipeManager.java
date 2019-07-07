/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biz.orgin.minecraft.hothgenerator;

import java.util.ArrayList;
import java.util.List;
import static org.bukkit.Bukkit.getServer;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

/**
 *
 * @author zhiqiang.hao
 */
public class RecipeManager {
    public static final String WarmSuitTag = "§7Warm Suit";
    public static final String RepellentSuitTag = "§2Repellent Suit";
    public static final String CoolingSuitTag = "§3Cooling Suit";
    public static final String GlassSuitTag = "§eGlass Suit";
    
    public static void registerRecipe(HothGeneratorPlugin plugin) {
        registerWarmArmor(plugin);
        registerRepellentArmor(plugin);
        registerCoolingArmor(plugin);
        registerGlassArmor(plugin);
    }
    
    public static void registerWarmArmor(HothGeneratorPlugin plugin) {
        doRegisterWarmArmor(plugin);
    }
    private static void doRegisterWarmArmor(HothGeneratorPlugin plugin) {
        ShapedRecipe recipe;
        
        ItemStack helmet = subRegisterWarmArmor(plugin, new ItemStack(Material.LEATHER_HELMET));
        recipe = new ShapedRecipe( new NamespacedKey(plugin, "warm_suit_helmet"), helmet );
        recipe.shape( "XXX", "XSX", "XXX" );
        recipe.setIngredient( 'X', Material.WHITE_WOOL );
        recipe.setIngredient( 'S', Material.LEATHER_HELMET );
        getServer().addRecipe( recipe );
        
        ItemStack chestplate = subRegisterWarmArmor(plugin, new ItemStack(Material.LEATHER_CHESTPLATE));
        recipe = new ShapedRecipe( new NamespacedKey(plugin, "warm_suit_chestplate"), chestplate );
        recipe.shape( "XXX", "XSX", "XXX" );
        recipe.setIngredient( 'X', Material.WHITE_WOOL );
        recipe.setIngredient( 'S', Material.LEATHER_CHESTPLATE );
        getServer().addRecipe( recipe );
        
        ItemStack leggings = subRegisterWarmArmor(plugin, new ItemStack(Material.LEATHER_LEGGINGS));
        recipe = new ShapedRecipe( new NamespacedKey(plugin, "warm_suit_leggings"), leggings );
        recipe.shape( "XXX", "XSX", "XXX" );
        recipe.setIngredient( 'X', Material.WHITE_WOOL );
        recipe.setIngredient( 'S', Material.LEATHER_LEGGINGS );
        getServer().addRecipe( recipe );
        
        ItemStack boots = subRegisterWarmArmor(plugin, new ItemStack(Material.LEATHER_BOOTS));
        recipe = new ShapedRecipe( new NamespacedKey(plugin, "warm_suit_boots"), boots );
        recipe.shape( "XXX", "XSX", "XXX" );
        recipe.setIngredient( 'X', Material.WHITE_WOOL );
        recipe.setIngredient( 'S', Material.LEATHER_BOOTS );
        getServer().addRecipe( recipe );
    }
    private static ItemStack subRegisterWarmArmor(HothGeneratorPlugin plugin, ItemStack is) {
        LeatherArmorMeta white_meta = (LeatherArmorMeta) is.getItemMeta();
        white_meta.setColor(Color.WHITE);
        is.setItemMeta(white_meta);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ConfigManager.getWarmSuitName(plugin));
        List<String> sub = new ArrayList<>();
        sub.add(RecipeManager.WarmSuitTag);
        sub.add(ConfigManager.getWarmSuitDescription(plugin));
        im.setLore(sub);
        is.setItemMeta(im);
        return is;
    }
    
    public static void registerRepellentArmor(HothGeneratorPlugin plugin) {
        doRegisterRepellentArmor(plugin);
    }
    private static void doRegisterRepellentArmor(HothGeneratorPlugin plugin) {
        ShapedRecipe recipe;
        
        ItemStack helmet = subRegisterRepellentArmor(plugin, new ItemStack(Material.LEATHER_HELMET));
        recipe = new ShapedRecipe( new NamespacedKey(plugin, "repellent_suit_helmet"), helmet );
        recipe.shape( "XXX", "XSX", "XXX" );
        recipe.setIngredient( 'X', Material.FERN );
        recipe.setIngredient( 'S', Material.LEATHER_HELMET );
        getServer().addRecipe( recipe );
        
        ItemStack chestplate = subRegisterRepellentArmor(plugin, new ItemStack(Material.LEATHER_CHESTPLATE));
        recipe = new ShapedRecipe( new NamespacedKey(plugin, "repellent_suit_chestplate"), chestplate );
        recipe.shape( "XXX", "XSX", "XXX" );
        recipe.setIngredient( 'X', Material.FERN );
        recipe.setIngredient( 'S', Material.LEATHER_CHESTPLATE );
        getServer().addRecipe( recipe );
        
        ItemStack leggings = subRegisterRepellentArmor(plugin, new ItemStack(Material.LEATHER_LEGGINGS));
        recipe = new ShapedRecipe( new NamespacedKey(plugin, "repellent_suit_leggings"), leggings );
        recipe.shape( "XXX", "XSX", "XXX" );
        recipe.setIngredient( 'X', Material.FERN );
        recipe.setIngredient( 'S', Material.LEATHER_LEGGINGS );
        getServer().addRecipe( recipe );
        
        ItemStack boots = subRegisterRepellentArmor(plugin, new ItemStack(Material.LEATHER_BOOTS));
        recipe = new ShapedRecipe( new NamespacedKey(plugin, "repellent_suit_boots"), boots );
        recipe.shape( "XXX", "XSX", "XXX" );
        recipe.setIngredient( 'X', Material.FERN );
        recipe.setIngredient( 'S', Material.LEATHER_BOOTS );
        getServer().addRecipe( recipe );
    }
    private static ItemStack subRegisterRepellentArmor(HothGeneratorPlugin plugin, ItemStack is) {
        LeatherArmorMeta green_meta = (LeatherArmorMeta) is.getItemMeta();
        green_meta.setColor(Color.GREEN);
        is.setItemMeta(green_meta);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ConfigManager.getRepellentSuitName(plugin));
        List<String> sub = new ArrayList<>();
        sub.add(RecipeManager.RepellentSuitTag);
        sub.add(ConfigManager.getRepellentSuitDescription(plugin));
        im.setLore(sub);
        is.setItemMeta(im);
        return is;
    }
    
    public static void registerCoolingArmor(HothGeneratorPlugin plugin) {
        doRegisterCoolingArmor(plugin);
    }
    private static void doRegisterCoolingArmor(HothGeneratorPlugin plugin) {
        ShapedRecipe recipe;
        
        ItemStack helmet = subRegisterCoolingArmor(plugin, new ItemStack(Material.LEATHER_HELMET));
        recipe = new ShapedRecipe( new NamespacedKey(plugin, "cooling_suit_helmet"), helmet );
        recipe.shape( "XXX", "XSX", "XXX" );
        recipe.setIngredient( 'X', Material.ICE );
        recipe.setIngredient( 'S', Material.LEATHER_HELMET );
        getServer().addRecipe( recipe );
        
        ItemStack chestplate = subRegisterCoolingArmor(plugin, new ItemStack(Material.LEATHER_CHESTPLATE));
        recipe = new ShapedRecipe( new NamespacedKey(plugin, "cooling_suit_chestplate"), chestplate );
        recipe.shape( "XXX", "XSX", "XXX" );
        recipe.setIngredient( 'X', Material.ICE );
        recipe.setIngredient( 'S', Material.LEATHER_CHESTPLATE );
        getServer().addRecipe( recipe );
        
        ItemStack leggings = subRegisterCoolingArmor(plugin, new ItemStack(Material.LEATHER_LEGGINGS));
        recipe = new ShapedRecipe( new NamespacedKey(plugin, "cooling_suit_leggings"), leggings );
        recipe.shape( "XXX", "XSX", "XXX" );
        recipe.setIngredient( 'X', Material.ICE );
        recipe.setIngredient( 'S', Material.LEATHER_LEGGINGS );
        getServer().addRecipe( recipe );
        
        ItemStack boots = subRegisterCoolingArmor(plugin, new ItemStack(Material.LEATHER_BOOTS));
        recipe = new ShapedRecipe( new NamespacedKey(plugin, "cooling_suit_boots"), boots );
        recipe.shape( "XXX", "XSX", "XXX" );
        recipe.setIngredient( 'X', Material.ICE );
        recipe.setIngredient( 'S', Material.LEATHER_BOOTS );
        getServer().addRecipe( recipe );
    }
    private static ItemStack subRegisterCoolingArmor(HothGeneratorPlugin plugin, ItemStack is) {
        LeatherArmorMeta green_meta = (LeatherArmorMeta) is.getItemMeta();
        green_meta.setColor(Color.AQUA);
        is.setItemMeta(green_meta);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ConfigManager.getCoolingSuitName(plugin));
        List<String> sub = new ArrayList<>();
        sub.add(RecipeManager.CoolingSuitTag);
        sub.add(ConfigManager.getCoolingSuitDescription(plugin));
        im.setLore(sub);
        is.setItemMeta(im);
        return is;
    }
    public static void registerGlassArmor(HothGeneratorPlugin plugin) {
        doRegisterGlassArmor(plugin);
    }
    private static void doRegisterGlassArmor(HothGeneratorPlugin plugin) {
        ShapedRecipe recipe;
        
        ItemStack helmet = subRegisterGlassArmor(plugin, new ItemStack(Material.LEATHER_HELMET));
        recipe = new ShapedRecipe( new NamespacedKey(plugin, "Glass_suit_helmet"), helmet );
        recipe.shape( "XXX", "XSX", "XXX" );
        recipe.setIngredient( 'X', Material.GLASS );
        recipe.setIngredient( 'S', Material.LEATHER_HELMET );
        getServer().addRecipe( recipe );
        
        ItemStack chestplate = subRegisterGlassArmor(plugin, new ItemStack(Material.LEATHER_CHESTPLATE));
        recipe = new ShapedRecipe( new NamespacedKey(plugin, "Glass_suit_chestplate"), chestplate );
        recipe.shape( "XXX", "XSX", "XXX" );
        recipe.setIngredient( 'X', Material.GLASS );
        recipe.setIngredient( 'S', Material.LEATHER_CHESTPLATE );
        getServer().addRecipe( recipe );
        
        ItemStack leggings = subRegisterGlassArmor(plugin, new ItemStack(Material.LEATHER_LEGGINGS));
        recipe = new ShapedRecipe( new NamespacedKey(plugin, "Glass_suit_leggings"), leggings );
        recipe.shape( "XXX", "XSX", "XXX" );
        recipe.setIngredient( 'X', Material.GLASS );
        recipe.setIngredient( 'S', Material.LEATHER_LEGGINGS );
        getServer().addRecipe( recipe );
        
        ItemStack boots = subRegisterGlassArmor(plugin, new ItemStack(Material.LEATHER_BOOTS));
        recipe = new ShapedRecipe( new NamespacedKey(plugin, "Glass_suit_boots"), boots );
        recipe.shape( "XXX", "XSX", "XXX" );
        recipe.setIngredient( 'X', Material.GLASS );
        recipe.setIngredient( 'S', Material.LEATHER_BOOTS );
        getServer().addRecipe( recipe );
    }
    private static ItemStack subRegisterGlassArmor(HothGeneratorPlugin plugin, ItemStack is) {
        LeatherArmorMeta green_meta = (LeatherArmorMeta) is.getItemMeta();
        green_meta.setColor(Color.YELLOW);
        is.setItemMeta(green_meta);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ConfigManager.getGlassSuitName(plugin));
        List<String> sub = new ArrayList<>();
        sub.add(RecipeManager.GlassSuitTag);
        sub.add(ConfigManager.getGlassSuitDescription(plugin));
        im.setLore(sub);
        is.setItemMeta(im);
        return is;
    }
}
