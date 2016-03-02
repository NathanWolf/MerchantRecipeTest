package com.elmakers.mine.bukkit.test;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BlockIterator;

import java.util.ArrayList;
import java.util.List;

public class MerchantRecipeTest extends JavaPlugin 
{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (commandLabel.equalsIgnoreCase("merchant")) {
            if (!(sender instanceof Player))
            {
                sender.sendMessage(ChatColor.RED + "This command may only be used in-game");
                return true;
            }
            
            Player player = (Player)sender;
            if (!player.hasPermission("merchant")) return true;

            Location location = player.getEyeLocation();
            BlockIterator iterator = new BlockIterator(location.getWorld(), location.toVector(), location.getDirection(), 0, 64);
            Block block = location.getBlock();
            while (block.getType() == Material.AIR && iterator.hasNext()) {
                block = iterator.next();
            }
            block = block.getRelative(BlockFace.UP);
            Location targetLocation = block.getLocation();
            Entity merchant = targetLocation.getWorld().spawnEntity(targetLocation, EntityType.VILLAGER);
            if (merchant == null || !(merchant instanceof Villager)) {
                sender.sendMessage(ChatColor.RED + "Something went wrong, failed to spawn villager");
                return true;
            }
            
            Villager villager = (Villager)merchant;
            List<MerchantRecipe> recipes = new ArrayList<MerchantRecipe>();
            MerchantRecipe recipe = new MerchantRecipe(new ItemStack(Material.DIAMOND, 2), 20);
            recipe.setExperienceReward(true);
            recipe.addIngredient(new ItemStack(Material.EMERALD));
            recipes.add(recipe);
            villager.setRecipes(recipes);
            
            sender.sendMessage(ChatColor.GREEN + "Spawned a merchant with an emerald -> diamondx2 trade");
        }
        
        return false;
    }
}