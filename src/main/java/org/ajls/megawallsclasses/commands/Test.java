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

import javax.sound.midi.InvalidMidiDataException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.ajls.megawallsclasses.MegaWallsClasses.plugin;

public class Test implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.getWorld().createExplosion(player.getLocation(), 3f, false, true, player);
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
}
