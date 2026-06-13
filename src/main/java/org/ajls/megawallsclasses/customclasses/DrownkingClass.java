package org.ajls.megawallsclasses.customclasses;

import org.ajls.lib.utils.ItemStackU;
import org.ajls.megawallsclasses.*;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.UUID;

import static org.ajls.megawallsclasses.ActiveSkills.activeTridents;
import static org.ajls.megawallsclasses.EnergyAccumulate.addEnergy;
import static org.ajls.megawallsclasses.MegaWallsClasses.getPlugin;
import static org.bukkit.Bukkit.getServer;

public class DrownkingClass extends AbstractCustomClass {





    @Override
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        Trident trident = (Trident) event.getEntity();
        UUID tridentUUID = trident.getUniqueId();
        Player player = (Player) trident.getShooter();
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
    public void onProjectileHit(ProjectileHitEvent event, Player player) {
        Trident trident = (Trident) event.getEntity();
        UUID tridentUUID = trident.getUniqueId();


        //                    if (drownking_tridentThrownSlot.containsKey(playerUUID)) {
//                        int slot = drownking_tridentThrownSlot.get(playerUUID);
//                        if (slot != -1) {
//                            ItemStack itemStack = player.getInventory().getItem(slot);
//                            if (itemStack != null && itemStack.getType() == Material.TRIDENT) {
//                                drownking_tridentThrownSlot.remove(playerUUID);
//                                player.getInventory().setItem(slot, new ItemStack(Material.AIR));
//                            }
//                        }
//                    }

        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.runTaskLater(MegaWallsClasses.getPlugin(), () -> {
            trident.remove();
            boolean found = false;
            for (int i = 0; i < player.getInventory().getSize(); i++) {
                ItemStack playerItem = player.getInventory().getItem(i);
                if (playerItem != null) {
                    String itemType = ItemStackU.getStringPersistentData(playerItem, NameSpacedKeys.ITEM_TYPE);
                    if (itemType != null){
                        if (itemType.equals("drownking_trident")) {
                            if (!found) {
                                found = true;
//                                                        playerItem.setType(Material.SCULK_SENSOR);
//                                                        playerItem.setAmount(MyListener.warden_sensorAmount.get(uuid));
                                ItemStack item = InventoryManager.drownking_trident();  //todo 要么这个drownking_trident method缓存到内存，别每次都新建，包括菜单物品栏也缓存到内存,reorder也酌情根据玩家职业选择缓存（改职业就改缓存），要么就直接trident.getItemStack()
                                player.getInventory().setItem(i, item);
//                                            break;

                            }
                            else {
                                playerItem.setAmount(0);// = new ItemStack(Material.AIR);
                            }
                        }
                    }

                }
            }
            if (!found) {
                ItemStack item = InventoryManager.drownking_trident();
                player.getInventory().addItem(item);
            }

        }, 2L);  // try 0l  //try 10L as 后摇 backswing

        if (activeTridents.contains(tridentUUID)) {  //no that should be drownking_activeSkillTimes ，后面备注，这句话意思是，扎到地板就要清理，对于老版本防止6s内一直刺，对于新版本，就是Cooldown.player_activeSkillCooldown.remove(damagerUUID);，实现扎到人时判定是不是在cd内同时移除cd防止一直叉，当然老版本清理那个三叉戟——射出者映射是正确的，版本更新时居然忘记要清理了，导致内存泄露
            //后面继续备注，其实还要补上探测能量是不是100，如果是100要直接激活主动技能，也就是半新版本的 射出不会自动激活主动，而是叉中激活主动（对应叉中结算），当然前面有个判断isEnergyFull来判断是否叉中（如果叉中自然会交给damage代码处理，移除Energy同时伤害），如果扔出去没叉到人掉地上一样能量会被清除，一样也是对应叉中结算。新版本只需要在Launch执行逻辑就行，这个叉中结算版本不仅需要在damage处理逻辑，还需要在hit但是没damage这里处理逻辑来防止没叉中人，但是还有cd或者还有能量
            //当然现在是修bug加上去的，之前更新了居然忘记了不移除会导致trident内存泄露，现在记得了
            scheduler = getServer().getScheduler();
            scheduler.scheduleSyncDelayedTask(getPlugin(), () ->{
                activeTridents.remove(tridentUUID);




            }, 2L);  // try 0l //后面备注：这个event和damage event到底谁先被执行呢？
        }
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
        }
    }



    //    @Override
//    public void onTridentDamage(EntityDamageByEntityEvent event) {
//
//    }
}
