package org.ajls.megawallsclasses;

import org.ajls.cactusgenerator.cosmetics.Bloody;
import org.ajls.lib.api.LIbAPI;
import org.ajls.lib.utils.ScoreboardU;
import org.ajls.megawallsclasses.commands.*;
import org.ajls.megawallsclasses.classsystem.ClassNamesConfig;
import org.ajls.megawallsclasses.custommusic.MidiMusicPlayer;
import org.ajls.megawallsclasses.listeners.spawnprotection.SpawnProtectionListener;
import org.ajls.megawallsclasses.packetlisteners.PacketListener;
import org.ajls.megawallsclasses.playerdata.PlayerData;
import org.ajls.megawallsclasses.playerdata.PlayerDataManager;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.*;

import static org.ajls.megawallsclasses.ActiveSkills.block_moleBlockState;
//import static org.ajls.megawallsclasses.PassiveSkills.nullPassiveSkillDisable;
import static org.ajls.megawallsclasses.GameManager.wither_team;
import static org.ajls.megawallsclasses.ScoreboardsAndTeams.*;
import static org.ajls.tractorcompass.MyListener.setTracked_teams;

public final class MegaWallsClasses extends JavaPlugin {
    public static MegaWallsClasses plugin;

    public static MidiMusicPlayer getMidiMusicPlayer() {
        return midiMusicPlayer;
    }

    public static MidiMusicPlayer midiMusicPlayer;
    ArrayList<String> tracked_teams = new ArrayList<>();
//    public static TractorCompass tractorCompass = new TractorCompass();

    public static MegaWallsClasses getPlugin() {
        return plugin;
    }

//    public static int time = 0;
    /*
    取消玩家自己射自己加能量
    黑君隐身被攻击显示提示//message and color 冷却变金色
    黑军隐身换职业没了
    力量双倍伤害包括了力量原本的伤害加成
     */

    //TODO white team can shoot himself to get energy, spider fall damage will kill himself, spider explosion will kill the other, 303 bow hit will kill the other, class description add missing, class name to normal
    //todo 服务器清爽化结构化 插件烦人的都重写，cacutils也写好来 比如scp330 railgun water attribute中depend的麻烦 还有Protocolib的区块数据
    //todo 配置文件先玩家再数据，这样一读就读玩家所有数据，直接加载到内存
    //todo 加入dummy player防止玩家退出游戏像ec一样暴毙，就和afk的玩家一样
    //todo 加入格挡，副手变成盾
    //todo 玩家只有未在无敌帧时被打加能量（比如僵尸被打加能量只能加一次，其他职业打别人只有第一个打的才能加）


    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        midiMusicPlayer = new MidiMusicPlayer(MegaWallsClasses.getPlugin());
        getConfig().options().copyDefaults(true);  //这行代码设置了一个“开关// ”（copyDefaults），告诉Bukkit的配置API：当从getConfig()读取一个不存在的配置项时，自动用插件JAR里config.yml的预设值去填充。
        //todo 我不知道，反正新增职业名称这段代码时实测根本没有copyDefaults，这怎么办（）
        saveDefaultConfig();  //保存到磁盘
        tracked_teams.add("blue_team");
//        org.ajls.
        tracked_teams.add("red_team");
        setTracked_teams(tracked_teams);
        Bloody.registerPlayerDeathItems(CustomEventsOld.getPlayer_deathItems());
        ClassNamesConfig.getInstance(this);  //其实也不用写，反正调用时会自动传入instance
        getServer().getPluginManager().registerEvents(new MyListener(), this);
        LIbAPI.registerListener(new MyListener());
        new SpawnProtectionListener();
        getCommand("class").setExecutor(new ChangeClass());
        getCommand("blue").setExecutor(new Blue());
        getCommand("red").setExecutor(new Red());
        getCommand("start").setExecutor(new Start());
        getCommand("order").setExecutor(new Order());
        getCommand("backdoor").setExecutor(new Backdoor());
        getCommand("test").setExecutor(new Test());
        getCommand("l").setExecutor(new L());
        getCommand("energetic").setExecutor(new Energetic());

        PacketListener.registerPacketListener();

        World world = getServer().getWorld("world");
        Configuration configuration = getConfig();
        Location mapMinLoc = new Location(world, configuration.getInt("locations.loc_map_min.x"), configuration.getInt("locations.loc_map_min.y"), configuration.getInt("locations.loc_map_min.z"));
        Location mapMaxLoc = new Location(world, configuration.getInt("locations.loc_map_max.x"), configuration.getInt("locations.loc_map_max.y"), configuration.getInt("locations.loc_map_max.z"));
//        forceloadMap(mapMinLoc, mapMaxLoc);
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        world.setGameRule(GameRule.DO_MOB_LOOT, false);
//        world.setGameRule(GameRule.KEEP_INVENTORY, true);
        world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        world.setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);

        //newgamerule below
//        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setDifficulty(Difficulty.EASY);
        registerTeam("blue_team");
        registerTeam("red_team");
        setTeamRule("blue_team", Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        setTeamRule("red_team", Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        setFriendlyFire("blue_team", false);
        setFriendlyFire("red_team", false);
        setTeamColor("blue_team", ChatColor.BLUE);
        setTeamColor("red_team", ChatColor.RED);
        if (getScoreboard("class") == null) {
            createScoreboard("class");
        }
        if (getScoreboard("energy") == null) {
            createScoreboard("energy");
        }
        ScoreboardU.createScoreboard("tf"); //transformation master  //todo 搞好tf自定义物品的替换，别吞其他物品，或者依靠物品metadata区分，导致物品无法合并 (直接别补发补给性物品，比如方块和食物和弓箭）
//        PassiveSkills.elaina_icicleSpinTask();

//        BukkitScheduler scheduler = plugin.getServer().getScheduler();
//        scheduler.scheduleSyncRepeatingTask(plugin, () -> {
//            // Do something
//        }, 0L, 1L);
//        Cooldown.playerCooldowns.add(Cooldown.player_passiveSkill1Cooldown);
//        Cooldown.playerCooldowns.add(Cooldown.player_passiveSkill2Cooldown);
        Cooldown.registerCooldowns();
//        HashSet<HashMap<UUID, Integer>> playerCooldownsClone = (HashSet<HashMap<UUID, Integer>>) Cooldown.playerCooldowns.clone();
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
//        Cooldown.registerCooldowns();
        scheduler.runTaskTimer(plugin, () -> {
//            time++;
            PassiveSkills.baseDegree++;
            if (PassiveSkills.baseDegree >= 360) {
                PassiveSkills.baseDegree = 0;
            }
            for (int i = 0; i < Cooldown.playerCooldowns.size(); i++) {
                HashMap<UUID, Integer> cooldown = Cooldown.playerCooldowns.get(i);
                Cooldown.removeCooldown(cooldown, 1, i);
            }
            PlayerDataManager.tick();
//            for (HashMap<UUID, Integer> cooldown : Cooldown.playerCooldowns) {
//                Cooldown.removeCooldown(cooldown, 1);
//            }
//            Cooldown.removeCooldown(Cooldown.player_passiveSkill1Cooldown, 1);
//            Cooldown.removeCooldown(Cooldown.player_passiveSkill2Cooldown, 1);
        }, 1, 1);
//        BlocksModify.fill(configuration.getInt("locations.loc_map_min.x"), configuration.getInt("locations.loc_map_min.y"), configuration.getInt("locations.loc_map_min.z"), configuration.getInt("locations.loc_map_max.x"), configuration.getInt("locations.loc_map_max.y"), configuration.getInt("locations.loc_map_max.z"));configuration.getInt("locations.loc_map_min.x"), configuration.getInt("locations.loc_map_min.y"), configuration.getInt("locations.loc_map_min.z"), configuration.getInt("locations.loc_map_max.x"), configuration.getInt("locations.loc_map_max.y"), configuration.getInt("locations.loc_map_max.z")
        BlocksModify.setBiome(configuration.getInt("locations.loc_map_min.x"), configuration.getInt("locations.loc_map_min.y"), configuration.getInt("locations.loc_map_min.z"), configuration.getInt("locations.loc_map_max.x"), configuration.getInt("locations.loc_map_max.y"), configuration.getInt("locations.loc_map_max.z"), Biome.SNOWY_PLAINS);
    }


    public static HashMap<Location, org.bukkit.block.BlockState> blockRegenWhenDisabled = new HashMap<>();
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for (UUID uuid : wither_team.keySet()) {
            Wither wither = (Wither) Bukkit.getEntity(uuid);
            wither.remove();
        }

        for (org.bukkit.block.BlockState blockState : block_moleBlockState.values()) {  //todo emm 如果被技能破坏了然后放了新方块以哪个为准？新方块又被破坏了呢？
            blockState.update(true);
        }
        for (org.bukkit.block.BlockState blockState : blockRegenWhenDisabled.values()) {
            blockState.update(true);
        }
    }

    public static void createScoreboard(String name) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getMainScoreboard();
        Objective objective = board.registerNewObjective(name, "dummy");
    }

    public static Objective getScoreboard(String name){
        //get the scoreboard of this name
        Objective objective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective(name);
        return objective;
    }

    public static void addScore(Player player, String name, int score) {
        setScore(player, name, getScore(player, name) + score);
    }





    public static ItemStack setColor(ItemStack stack, Color color) {
        LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
        meta.setColor(color);
        stack.setItemMeta(meta);
        return stack;
    }

    public static List<String> getLore(ItemStack itemStack){
        ItemMeta meta = itemStack.getItemMeta();
        List<String> loresList = new ArrayList<String>();
        loresList = meta.getLore();
        return loresList;
    }

    public static ItemStack setEffect(ItemStack itemstack, PotionEffectType effectType, int duration, int amplifier, boolean ambient, boolean particles) {
        PotionMeta meta = (PotionMeta) itemstack.getItemMeta();
//        meta.getPersistentDataContainer().set();
        meta.addCustomEffect(new PotionEffect(effectType, duration, amplifier, ambient, particles), true);
        itemstack.setItemMeta(meta);
        return itemstack;
    }

    public static ItemStack setEffect(ItemStack itemstack, PotionEffectType effectType, int duration, int amplifier) {
        return  setEffect(itemstack, effectType, duration, amplifier, false, true);
    }

}
