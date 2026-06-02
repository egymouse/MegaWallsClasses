package org.ajls.megawallsclasses.customclasses;

import org.ajls.megawallsclasses.ActiveSkills;
import org.ajls.megawallsclasses.Cooldown;
import org.ajls.megawallsclasses.EnergyAccumulate;
import org.ajls.megawallsclasses.MyListener;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.util.UUID;

import static org.ajls.megawallsclasses.EnergyAccumulate.addEnergy;
import static org.ajls.megawallsclasses.EnergyAccumulate.shootEnergyAccumulate;
import static org.ajls.megawallsclasses.KillsManager.registerPlayerHit;
import static org.ajls.megawallsclasses.ScoreboardsAndTeams.getScore;
import static org.bukkit.Bukkit.broadcastMessage;

public class DrownkingOldClass implements CustomClassI {
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

            registerPlayerHit(player, damager);

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
            shootEnergyAccumulate(damager);
        }
    }

    //todo launch时要引入忠诚设计，不能落地就remove，需要更改hit逻辑，同时也要记录物品栏index，或者也要placeholder item但是就算背包满了也能捡起替换placeholder item 监听onPlayerAttemptPickupItem就行
}
