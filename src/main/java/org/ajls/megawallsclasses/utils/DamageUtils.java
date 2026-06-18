package org.ajls.megawallsclasses.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import static org.bukkit.Bukkit.getServer;

public class DamageUtils {

    public static void dealTrueDamage(LivingEntity entity, double health) {  //造成真伤的意思
        double absorptionPlayer = entity.getAbsorptionAmount();
        double healthPlayer = entity.getHealth();
        if (health < 0 ) {  //damage
            getServer().getPluginManager().callEvent(new EntityDamageEvent(entity, EntityDamageEvent.DamageCause.CUSTOM, -health));
            if (absorptionPlayer >= -health) { // absorption able to absorb damage
                absorptionPlayer += health;
                health = 0;
            }
            else {  // absorption not able to absorb damage
                health += absorptionPlayer; // damage remove absorption amount
                absorptionPlayer = 0;
//                healthPlayer += health; // damage to player's health
            }
        }
        entity.setAbsorptionAmount(absorptionPlayer);
        healthPlayer += health;  // damage to player's health
//        else { // healing
//            healthPlayer += health;
//        }
//        healthPlayer = healthPlayer + health;
        if (0 < healthPlayer && healthPlayer < entity.getMaxHealth()) {
            entity.setHealth(healthPlayer);
//            EntityRegainHealthEvent entityRegainHealthEvent = new EntityRegainHealthEvent(entity, health, EntityRegainHealthEvent.RegainReason.CUSTOM);
//            Bukkit.getServer().getPluginManager().callEvent(entityRegainHealthEvent);
        }
        else if (healthPlayer <= 0) {
            if (entity instanceof Player) {
                Player player = (Player) entity;
//                playerDeathEvent(player);  //disabled because call event will handle death
            }
            else {
                entity.setHealth(0);
            }
        }
        else {
            entity.setHealth(entity.getMaxHealth());
        }
    }


    /**
     * 治疗实体
     * @param entity 目标实体
     * @param amount 治疗量（必须 > 0）
     */
    public static void heal(LivingEntity entity, double amount) {
        if (entity == null || entity.isDead() || amount <= 0) return;

        // 触发治疗事件（让其他插件能监听）
        EntityRegainHealthEvent event = new EntityRegainHealthEvent(entity, amount, EntityRegainHealthEvent.RegainReason.CUSTOM);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        double actualAmount = event.getAmount(); // 插件可能修改了治疗量
        double newHealth = Math.min(entity.getHealth() + actualAmount, entity.getMaxHealth());
        entity.setHealth(newHealth);
    }


    public static void addHealth(LivingEntity entity, double health) {  //造成真伤的意思
        double absorptionPlayer = entity.getAbsorptionAmount();
        double healthPlayer = entity.getHealth();
        if (health < 0 ) {  //damage
            getServer().getPluginManager().callEvent(new EntityDamageEvent(entity, EntityDamageEvent.DamageCause.CUSTOM, -health));
            if (absorptionPlayer >= -health) { // absorption able to absorb damage
                absorptionPlayer += health;
                health = 0;
            }
            else {  // absorption not able to absorb damage
                health += absorptionPlayer; // damage remove absorption amount
                absorptionPlayer = 0;
//                healthPlayer += health; // damage to player's health
            }
        }
        entity.setAbsorptionAmount(absorptionPlayer);
        healthPlayer += health;  // damage to player's health
//        else { // healing
//            healthPlayer += health;
//        }
//        healthPlayer = healthPlayer + health;
        if (0 < healthPlayer && healthPlayer < entity.getMaxHealth()) {
            entity.setHealth(healthPlayer);
//            EntityRegainHealthEvent entityRegainHealthEvent = new EntityRegainHealthEvent(entity, health, EntityRegainHealthEvent.RegainReason.CUSTOM);
//            Bukkit.getServer().getPluginManager().callEvent(entityRegainHealthEvent);
        }
        else if (healthPlayer <= 0) {
            if (entity instanceof Player) {
                Player player = (Player) entity;
//                playerDeathEvent(player);  //disabled because call event will handle death
            }
            else {
                entity.setHealth(0);
            }
        }
        else {
            entity.setHealth(entity.getMaxHealth());
        }
    }
}
