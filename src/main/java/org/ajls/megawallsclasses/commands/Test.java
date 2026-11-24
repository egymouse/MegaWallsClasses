package org.ajls.megawallsclasses.commands;

import net.kyori.adventure.util.TriState;
import org.ajls.lib.utils.ItemStackU;
import org.ajls.megawallsclasses.MegaWallsClasses;
import org.ajls.megawallsclasses.NameSpacedKeys;
import org.ajls.megawallsclasses.PassiveSkills;
import org.ajls.megawallsclasses.custommusic.ClaudeMusic;
import org.ajls.megawallsclasses.custommusic.MidiMusicPlayer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import javax.sound.midi.InvalidMidiDataException;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.ajls.megawallsclasses.MegaWallsClasses.plugin;

public class Test implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            

        }

        return true;
    }

    private TextDisplay spawnPixel(Color color, Location location, double pixelSize) {  //todo 用textdisplay制作血液效果
        TextDisplay textDisplay = location.getWorld().spawn(location, TextDisplay.class);
        textDisplay.setBackgroundColor(color);
        textDisplay.setText(" ");
        textDisplay.setBrightness(new Display.Brightness(15, 15));

        Transformation transformation = textDisplay.getTransformation();
        transformation.getScale().set(pixelSize, pixelSize / 2, pixelSize);  //todo 为什么这里有一个除以2
        textDisplay.setTransformation(transformation);
        return textDisplay;
    }


//    public static void IronSwordStruck(Player player, DamageType type){
//        World world = player.getWorld();
//        Vector look = player.getEyeLocation().getDirection().setY(0);
//        if(look.lengthSquared() < 1.0E-4){
//            look = player.getLocation().getDirection().setY(0);
//        }
//        final Vector dir = look.normalize();
//
//        Location spawn = player.getEyeLocation().clone()
//                .add(dir.clone().multiply(1.2))
//                .add(0, 0.5, 0);
//        ItemDisplay display = world.spawn(spawn, ItemDisplay.class, entity -> {
//            entity.setItemStack(new ItemStack(Material.IRON_SWORD));
//            entity.setBillboard(Display.Billboard.FIXED);
//            entity.setGravity(false);
//            entity.setInvulnerable(true);
//            entity.setPersistent(false);
//            entity.setRotation(0f, 0f);
//        });
//
//        Transformation transformation = display.getTransformation();
//        transformation.getScale().set(4.0f);
//        transformation.getTranslation().set(new Vector3f(0f, 2.0f, 0f));
//        transformation.getRightRotation().set(new AxisAngle4f((float)Math.toRadians(135),0f,0f,1f));
//        display.setTransformation(transformation);
//
//        final int slashTicks = 10;
//        final int holdTicks = 10;
//
//        new BukkitRunnable(){
//            private int ticks = 0;
//            private boolean damaged = false;
//
//            @Override
//            public void run() {
//                if(!display.isValid() || !player.isOnline()){
//                    display.remove();
//                    cancel();
//                    return;
//                }
//
//                if(ticks <= slashTicks){
//                    float progress = ticks / (float)slashTicks;
//                    // 绕“玩家朝向的右方向”旋转：剑刃从上往下，朝玩家正前方斩。
//                    // 把手保持在玩家附近，因此每 tick 根据旋转补偿 translation。
//                    float angle = (float)Math.toRadians(145f * progress);
//                    Vector3f axis = new Vector3f((float)dir.getZ(), 0f, (float)-dir.getX());
//                    Transformation current = display.getTransformation();
//                    current.getLeftRotation().set(new AxisAngle4f(angle, axis.x, axis.y, axis.z));
//
//                    Vector3f handleOffset = new Vector3f(0f, -2.0f, 0f);
//                    current.getLeftRotation().transform(handleOffset);
//                    current.getTranslation().set(-handleOffset.x, -handleOffset.y, -handleOffset.z);
//                    display.setTransformation(current);
//                }
//
//                if(ticks >= slashTicks && !damaged){
//                    damaged = true;
//
//                    Location center = player.getEyeLocation().clone()
//                            .add(dir.clone().multiply(3.0))
//                            .add(0, -1.5, 0);
//                    Collection<Entity> entities = world.getNearbyEntities(center, 2.5, 2.5, 2.5);
//                    for(Entity entity : entities){
//                        if(entity instanceof LivingEntity target
//                                && !(target instanceof Player)
//                                && target.isValid()
//                                && !target.isDead()
//                                && target.getLocation().clone().subtract(player.getLocation()).toVector().dot(dir) > 0
//                                && target.getPersistentDataContainer().has(fromNamespaceKey("entity"))
//                                && !target.getPersistentDataContainer().has(fromNamespaceKey("friend"))){
//                            Damage(target, player, 500, type);
//                            int t = BleedingService.bleeding_ticks.get(target.getUniqueId()) != null ?  BleedingService.bleeding_ticks.get(target.getUniqueId())  : 0;
//                            BleedingService.bleeding_ticks.put(target.getUniqueId(),Math.max(t,200));
//
//
//                            world.spawnParticle(Particle.BLOCK,
//                                    target.getLocation().add(0,1,0), 25,
//                                    Bukkit.createBlockData(Material.REDSTONE_BLOCK));
//                            world.spawnParticle(Particle.DAMAGE_INDICATOR,
//                                    target.getLocation().add(0,1,0), 8);
//                        }
//                    }
//                    world.playSound(center, Sound.BLOCK_ANVIL_LAND, 3f, 0.8f);
//                    world.spawnParticle(Particle.SWEEP_ATTACK, center, 1);
//                }
//
//                if(ticks >= slashTicks + holdTicks){
//                    display.remove();
//                    cancel();
//                    return;
//                }
//                ticks++;
//            }
//        }.runTaskTimer(PLUGIN, 0L, 1L);
//    }
}
