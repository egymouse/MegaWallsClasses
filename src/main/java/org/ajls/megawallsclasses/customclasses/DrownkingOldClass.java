package org.ajls.megawallsclasses.customclasses;

import org.ajls.lib.utils.ItemStackU;
import org.ajls.megawallsclasses.*;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static org.ajls.megawallsclasses.EnergyAccumulate.addEnergy;
import static org.ajls.megawallsclasses.ScoreboardsAndTeams.getScore;
import static org.bukkit.Bukkit.broadcastMessage;

public class DrownkingOldClass extends AbstractCustomClass {

    @Override
    public ItemStack createClassSword() {
        return InventoryManager.drownking_trident_for_inventory();
    }

    @Override
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        Trident trident = (Trident) event.getEntity();
        UUID tridentUUID = trident.getUniqueId();
        Player player = (Player) trident.getShooter();
        UUID playerUUID = player.getUniqueId();

//        // 原代码：冷却 & 能量判断
//        if (Cooldown.player_activeSkillCooldown.containsKey(playerUUID)) {
//            Cooldown.player_activeSkillCooldown.remove(playerUUID);
//            ActiveSkills.activeTridents.add(tridentUUID);
//        } else if (EnergyAccumulate.isFullEnergy(player)) {
//            MyListener.activeSkill(player); // 调用你的主动技能，等价于你先开了主动再扔的三叉戟
//            Cooldown.player_activeSkillCooldown.remove(playerUUID);
//            ActiveSkills.activeTridents.add(tridentUUID);
//        }

        // 原代码：占位符物品
        ItemStack placeholderItem = new ItemStack(Material.COMMAND_BLOCK);  //todo 设计这些占位符物品能被忠诚识别
        ItemStackU.setAttributePlayerBlockRange(placeholderItem, -114514);
        ItemStackU.setStringPersistentData(placeholderItem, NameSpacedKeys.ITEM_TYPE, "drownking_trident");

        // 原代码：替换主手/副手三叉戟
        if (player.getInventory().getItemInMainHand().getType() == Material.TRIDENT) {
            player.getEquipment().setItemInMainHand(placeholderItem);
        } else if (player.getInventory().getItemInOffHand().getType() == Material.TRIDENT) {
            player.getEquipment().setItemInOffHand(placeholderItem);
        }
    }



    @Override
    public void onPlayerPickupArrow(PlayerPickupArrowEvent event) {      //todo ，或者也要placeholder item但是就算背包满了也能捡起替换placeholder item
        @NotNull AbstractArrow abstractArrow = event.getArrow();
        Player player = event.getPlayer();
        Item item = event.getItem();
        ItemStack itemStack = event.getItem().getItemStack();
        Material material = itemStack.getType();
        UUID playerUUID = player.getUniqueId();


        event.setCancelled(true);
////                item.remove();  //这里改item没有半毛钱用的，存在一个已知问题：在 PlayerPickupArrowEvent 中，通过 getItem() 修改物品（如 event.getItem().setItemStack(...)）不会生效，玩家捡起的依然会是原来的物品。如果需要修改，应该使用 getArrow() 来修改箭头实体本身。
        abstractArrow.remove();

        tridentReturn(player, itemStack);

    }

    static void tridentReturn(Player player, ItemStack itemStack) {
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
                            ItemStack item = itemStack;
                            player.getInventory().setItem(i, item);
//                                            break;  //不break，是想要清理所有多余的Placeholder吧 我不太知道

                        }
                        else {
                            playerItem.setAmount(0);// = new ItemStack(Material.AIR);
                        }
                    }
                }

            }
        }
        if (!found) {
            ItemStack item = itemStack;
            player.getInventory().addItem(item);
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

        if (ActiveSkills.activeTridents.contains(tridentUUID) || true) {

//            boolean isActive = drownking_activeSkillTimes.containsKey(damagerUUID);
//            boolean isFullEnergy = EnergyAccumulate.isFullEnergy(player);
//            if (isActive) {
//                activeSkill(player);
//            }
//            if (isActive || isFullEnergy) {
//
//            }
            //把下面的改成上面注释的这样


            if (Cooldown.player_activeSkillCooldown.containsKey(damagerUUID)) {  //老职业特有的砸到才判断6s过没，而且6s内无论叉多少，叉到人才消失效果
//                        activeSkill(damager);
                double playerHealth = player.getHealth();
                playerHealth = playerHealth - event.getFinalDamage();  // use EventU to get final health
                MyListener.addHealth(player, (player.getMaxHealth() - playerHealth) * -0.38);
                World world = player.getWorld();
                world.strikeLightningEffect(player.getLocation());
//                        HashMapUtils.hashMapIncrease(damagerUUID, drownking_activeSkillTimes); // prevent reset when skill is already used // don't need to because the next time active skill will add times thus remove function won't work
//                drownking_activeSkillTimes.remove(damagerUUID);
                Cooldown.player_activeSkillCooldown.remove(damagerUUID);
                addEnergy(damager, 30);
                broadcastMessage(player.getName() + " was impaled by " + damager.getName() + " damage: " + (player.getMaxHealth() - playerHealth) * -0.38);
            }
            else if(getScore(damager, "energy") >= 100) {  // && ScoreboardsAndTeams.getScore(damager, "class") == 10) useless because checked when trident_dk contains key
                MyListener.activeSkill(damager);
                //                        activeSkill(damager);
                double playerHealth = player.getHealth();
                playerHealth = playerHealth - event.getFinalDamage();
                MyListener.addHealth(player, (player.getMaxHealth() - playerHealth) * -0.38);
                World world = player.getWorld();
                world.strikeLightningEffect(player.getLocation());
//                        HashMapUtils.hashMapIncrease(damagerUUID, drownking_activeSkillTimes); // prevent reset when skill is already used // don't need to because the next time active skill will add times thus remove function won't work
//                drownking_activeSkillTimes.remove(damagerUUID);
                Cooldown.player_activeSkillCooldown.remove(damagerUUID);
                addEnergy(damager, 30);
                broadcastMessage(player.getName() + " was impaled by " + damager.getName() + " damage: " + (player.getMaxHealth() - playerHealth) * -0.38);
            }
        }
    }


}
