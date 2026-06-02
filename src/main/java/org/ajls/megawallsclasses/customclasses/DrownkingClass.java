package org.ajls.megawallsclasses.customclasses;

import org.ajls.lib.utils.ItemStackU;
import org.ajls.megawallsclasses.*;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

import java.util.UUID;

import static org.ajls.megawallsclasses.EnergyAccumulate.addEnergy;
import static org.ajls.megawallsclasses.EnergyAccumulate.shootEnergyAccumulate;
import static org.ajls.megawallsclasses.KillsManager.registerPlayerHit;

public class DrownkingClass implements CustomClassI{
    @Override
    public void onTridentLaunch(ProjectileLaunchEvent event, Player player) {
        Trident trident = (Trident) event.getEntity();
        UUID tridentUUID = trident.getUniqueId();
        UUID playerUUID = player.getUniqueId();

        // 原代码：冷却 & 能量判断
        if (Cooldown.player_activeSkillCooldown.containsKey(playerUUID)) {
            Cooldown.player_activeSkillCooldown.remove(playerUUID);
            ActiveSkills.activeTridents.add(tridentUUID);
        } else if (EnergyAccumulate.isFullEnergy(player)) {
            MyListener.activeSkill(player); // 调用你的主动技能，等价于你先开了主动再扔的三叉戟
            Cooldown.player_activeSkillCooldown.remove(playerUUID);
            ActiveSkills.activeTridents.add(tridentUUID);
        }

        // 原代码：占位符物品
        ItemStack placeholderItem = new ItemStack(Material.COMMAND_BLOCK);  //todo 设计这些占位符以及职业物品无法丢弃
        ItemStackU.setAttributePlayerBlockRange(placeholderItem, -114514);
        ItemStackU.setStringPersistentData(placeholderItem, NameSpacedKeys.ITEM_TYPE, "drownking_trident");

        // 原代码：替换主手/副手三叉戟
        if (player.getInventory().getItemInMainHand().getType() == Material.TRIDENT) {
            player.getEquipment().setItemInMainHand(placeholderItem);
        } else if (player.getInventory().getItemInOffHand().getType() == Material.TRIDENT) {
            player.getEquipment().setItemInOffHand(placeholderItem);
        }
        //                if (player_offHandRightClickItem.get(playerUUID) != null) {  //ec的解决方法 直接存储是哪个快捷栏扔的三叉戟
//                    if (player_offHandRightClickItem.get(playerUUID).getType() == Material.TRIDENT) {
//                        drownking_tridentThrownSlot.put(player.getUniqueId(), -1);
//                    }
//                }
//                if (player_mainHandRightClickItem.get(playerUUID) != null) {
//                    if (player_mainHandRightClickItem.get(playerUUID).getType() == Material.TRIDENT) {
//                        drownking_tridentThrownSlot.put(player.getUniqueId(), player.getInventory().getHeldItemSlot());
//                    }
//                }

        //以下为修正后能用的MyListener逻辑
        /*
        UUID playerUUID = player.getUniqueId();
        //                    trident_drownking.put(tridentUUID, player.getUniqueId());
        if (Cooldown.player_activeSkillCooldown.containsKey(playerUUID)) {
            Cooldown.player_activeSkillCooldown.remove(playerUUID);
            ActiveSkills.activeTridents.add(tridentUUID);  // add the trident to active tridents
        }
        else if (EnergyAccumulate.isFullEnergy(player)) {
            MyListener.activeSkill(player);  // activate the skill if player has full energy
            Cooldown.player_activeSkillCooldown.remove(playerUUID);
            ActiveSkills.activeTridents.add(tridentUUID);  // add the trident to active tridents
        }
        //or use boolean to reduce repeat code, and remember only active skill when the player is not in activeSkillCooldown.

        ItemStack placeholderItem = new ItemStack(Material.COMMAND_BLOCK);;
        ItemStackU.setAttributePlayerBlockRange(placeholderItem, -114514);
        ItemStackU.setStringPersistentData(placeholderItem, NameSpacedKeys.ITEM_TYPE, "drownking_trident");


        if (player.getInventory().getItemInMainHand().getType() == Material.TRIDENT) {
//                        drownking_tridentThrownSlot.put(player.getUniqueId(), player.getInventory().getHeldItemSlot());
            player.getEquipment().setItemInMainHand(placeholderItem);
        }
        else if (player.getInventory().getItemInOffHand().getType() == Material.TRIDENT) {
//                        drownking_tridentThrownSlot.put(player.getUniqueId(), -1);
            player.getEquipment().setItemInOffHand(placeholderItem);
        }


//                if (player_offHandRightClickItem.get(playerUUID) != null) {
//                    if (player_offHandRightClickItem.get(playerUUID).getType() == Material.TRIDENT) {
//                        drownking_tridentThrownSlot.put(player.getUniqueId(), -1);
//                    }
//                }
//                if (player_mainHandRightClickItem.get(playerUUID) != null) {
//                    if (player_mainHandRightClickItem.get(playerUUID).getType() == Material.TRIDENT) {
//                        drownking_tridentThrownSlot.put(player.getUniqueId(), player.getInventory().getHeldItemSlot());
//                    }
//                }
         */


        //以下为MyListener的原始代码
        /*
        UUID playerUUID = player.getUniqueId();
                if (getScore(player, "class") == 10) {
//                    trident_drownking.put(tridentUUID, player.getUniqueId());
                    if (Cooldown.player_activeSkillCooldown.containsKey(playerUUID)) {
                        Cooldown.player_activeSkillCooldown.remove(playerUUID);
                        activeTridents.add(tridentUUID);  // add the trident to active tridents
                    }
                    else if (EnergyAccumulate.isFullEnergy(player)) {
                        activeSkill(player);  // activate the skill if player has full energy
                        Cooldown.player_activeSkillCooldown.remove(playerUUID);
                        activeTridents.add(tridentUUID);  // add the trident to active tridents
                    }
                    //or use boolean to reduce repeat code, and remember only active skill when the player is not in activeSkillCooldown.

                    ItemStack placeholderItem = new ItemStack(Material.COMMAND_BLOCK);;
                    ItemStackU.setAttributePlayerBlockRange(placeholderItem, -114514);
                    ItemStackU.setStringPersistentData(placeholderItem, NameSpacedKeys.ITEM_TYPE, "drownking_trident");



                    if (player.getInventory().getItemInMainHand().getType() == Material.TRIDENT) {
//                        drownking_tridentThrownSlot.put(player.getUniqueId(), player.getInventory().getHeldItemSlot());
                        player.getEquipment().setItemInMainHand(placeholderItem);
                    }
                    else if (player.getInventory().getItemInOffHand().getType() == Material.TRIDENT) {
//                        drownking_tridentThrownSlot.put(player.getUniqueId(), -1);
                        player.getEquipment().setItemInOffHand(placeholderItem);
                    }
                }


//                if (player_offHandRightClickItem.get(playerUUID) != null) {
//                    if (player_offHandRightClickItem.get(playerUUID).getType() == Material.TRIDENT) {
//                        drownking_tridentThrownSlot.put(player.getUniqueId(), -1);
//                    }
//                }
//                if (player_mainHandRightClickItem.get(playerUUID) != null) {
//                    if (player_mainHandRightClickItem.get(playerUUID).getType() == Material.TRIDENT) {
//                        drownking_tridentThrownSlot.put(player.getUniqueId(), player.getInventory().getHeldItemSlot());
//                    }
//                }
         */
    }

    @Override
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Player player = (Player) entity;
        Entity damagerEntity = event.getDamager();
        Trident trident = (Trident) damagerEntity;
        UUID tridentUUID = trident.getUniqueId();
        ProjectileSource projectileSource = trident.getShooter();
        Player damager = (Player) projectileSource;
        UUID damagerUUID = damager.getUniqueId();

        if (projectileSource instanceof Player) {
            //Player damager = (Player) projectileSource;  //todo 在event用预处理返回context来解决这些冗余判断，到时候会有一个每个职业都有的handle(context) interface，方便的直接把这个方法也集成到里面，不方便的也可以用handle里面再call这个就行了
            //UUID damagerUUID = damager.getUniqueId();

            registerPlayerHit(player, damager);
            boolean isActive = ActiveSkills.activeTridents.remove(tridentUUID);//drownking_activeSkillTimes.containsKey(damagerUUID);
//                    boolean isFullEnergy = EnergyAccumulate.isFullEnergy(player);
//                    if (isFullEnergy && !isActive) {
//                        activeSkill(player);
//                    }
            if (isActive) { // || isFullEnergy) {
                double playerHealth = player.getHealth();
                playerHealth = playerHealth - event.getFinalDamage();  // use EventU to get final health
                MyListener.addHealth(player, (player.getMaxHealth() - playerHealth) * -0.38);
                World world = player.getWorld();
                world.strikeLightningEffect(player.getLocation());
                MyListener.drownking_activeSkillTimes.remove(damagerUUID);
                addEnergy(damager, 30);
            }
            shootEnergyAccumulate(damager); //todo a 30 energy bonus if hit a player and with the 20 by energy accumulate, the total is 50，但是我没在event看到什么attackEnergyAccumulate的代码 ，好吧发现了就在这里这一行
        }
    }


    @Override
    public void onTridentHit(EntityDamageByEntityEvent event) {

    }
}
