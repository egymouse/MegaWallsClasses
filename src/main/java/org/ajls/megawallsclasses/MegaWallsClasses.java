package org.ajls.megawallsclasses;

import org.ajls.cactusgenerator.cosmetics.Bloody;
import org.ajls.lib.utils.ScoreboardU;
import org.ajls.megawallsclasses.classsystem.ClassConfigManager;
import org.ajls.megawallsclasses.commands.*;
import org.ajls.megawallsclasses.custommusic.MidiMusicPlayer;
import org.ajls.megawallsclasses.listeners.InventoryListener;
import org.ajls.megawallsclasses.listeners.spawnprotection.SpawnProtectionListener;
import org.ajls.megawallsclasses.packetlisteners.PacketListener;
import org.ajls.megawallsclasses.playerdata.PlayerDataManager;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.event.player.PlayerJoinEvent;
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

import javax.sound.midi.InvalidMidiDataException;
import java.io.IOException;
import java.util.*;

import static org.ajls.megawallsclasses.ActiveSkills.block_moleBlockState;
//import static org.ajls.megawallsclasses.PassiveSkills.nullPassiveSkillDisable;
import static org.ajls.megawallsclasses.GameManager.wither_team;
import static org.ajls.megawallsclasses.ScoreboardsAndTeams.*;
import static org.ajls.trackercompass.MyListener.setTracked_teams;

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
    //todo 箭矢打到队友可以穿过去
    //todo purpur搞一个尝试attack的事件，这样可以监听尝试攻击来修改伤害，而不用被原版的this.isInvulnerableTo(damagesource)什么的束缚
    //todo 同一队伍强dis friendly  fire
    //todo tf修复 d服没有盔甲韧性  职业重写数值 复活到树上 骷髅白队射箭打自己 骷髅爆炸箭打别人給生命回复 复活到树叶里面
    //todo 取消掉前冲攻击的音效
    //todo playerdata npr解决
    //todo d服龙
    //todo 更改头顶显示名称 ClientboundPlayerInfoUpdatePacket ClientboundSetPlayerTeamPacket
    //todo 修复幻变大师切换职业物品 背包不够的bug
    // midi支持are you lost这种更多音色 映射到我的世界原版音色


    //todo 增加传奇11连杀 增加tab显示血量 ***
    //todo 复活的时候慢慢显示整个人 让敌人有预备
    //todo itemkind implement .. 用对应代码 https://chat.deepseek.com/a/chat/s/0c233cbe-2773-4952-93b4-fba03d532cf4 加入布吉岛对应职业
    // 关闭物品栏自动保存 增加一个取消保存按钮
    // 龙伤害范围 解决了
    // 斧头bug修复 *** 自定义物品栏默认就是64axe 解决了
    // 龙剑研究    解决了
    // 龙攻击获取能量 解决了
    // 方块太少了 ***
    // 雪人数量限制
    // 水果三叉戟 解决了
    // 加入洞穴蜘蛛
    // 职业物品不能扔出 解决了
    // EXFL0SIONDisable Attack Cooldoun发现不是ENTITT_ATTACK
    //todo 切换职业时造成的冷却和职业清单暂存，比如warden靠冷却回复sensor 但是如果换了其他职业冷却好了不会恢复sensor，而且我插件是给予调用来互相递进的，一个断全断
    // 萨满虚弱0.5hp 学习基岩版java版低版本高版本区别 然后加入不同职业适配不同版本机制
    // 龙地狱火导致自动能量累计出bug 也不知道如何稳定复现
    // java.lang.NullPointerException: null
    // 老鼠挖砂土
    // override enable/disable class
    //todo 除了神秘的踩人减少毒液之击（明明有小于0.1伤害 return)和毒液之击中增伤外其他都做好了
    // 还有洞穴蜘蛛水平跳更远，竖直跳比较低手感才好
    // 自定义物品栏顺序增加选择d服普通/传奇职业选项
    //todo 其他插件整合一下，对原版逻辑的修改直接改服务端更好
    //todo 研究bukkitTask底层和时序 自己实现会不会更方便 如果创建多个bukkitTask会不会内存爆炸 bukkit是否维护一个优先队列管理最先要执行的task
    // 指南针因为setClassItem用不了了
    // myListener次序问题 蜘蛛踩人也算能量累积
    // purpur击退直接改，伤害计算直接改 要不然*1.2就不行
    // geyser解决
    // blocksmanager学习思路 看能增加什么好用的东西
    // todo 猪排减少秒切会出bug，血药替换一样(因为1tick延迟的原因)
    // todo 搞定geyser
    //      试试packetevents和packetWrapper
    // todo 增加基于插件的队伍系统 更改玩家显示名称增加传奇标和职业  至少玩家不能打自己（比如射箭）（监听damage然后取消射箭的)，把职业Listener逻辑挪到对应的职业类里面



    //todo 玩家interact block回导致主动触发两次 那么雪人，或者后面天赋无限火力就不行

    //protocollib的todo 设置糖果世界 石头是粉色混凝土，铁是白 煤炭是褐 幻变大师正常隐藏手中物品
    //todo 重置物品栏也会导致物品栏被保存 通过调换重置和打开物品栏的顺序看能不能解决


    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        midiMusicPlayer = new MidiMusicPlayer(MegaWallsClasses.getPlugin());
        saveDefaultConfig();  //如果没有config.yml，把默认的拉过来
        getConfig().options().copyDefaults(true);
        //这行代码设置了一个“开关// ”（copyDefaults），告诉Bukkit的配置API：当从getConfig()读取一个不存在的配置项时，自动用插件JAR里config.yml的预设值去填充。
        //todo 我不知道，反正新增职业名称这段代码时实测根本没有copyDefaults，这怎么办（）
        saveConfig(); //保存到磁盘

//        getConfig().set("test", new ItemCount(ItemKind.ARROW, 12));
//        saveConfig();
//        ItemCount itemCount = (ItemCount) getConfig().get("test", ItemCount.class);
//        Bukkit.broadcastMessage(itemCount.toString());


        tracked_teams.add("blue_team");
//        org.ajls.
        tracked_teams.add("red_team");
        setTracked_teams(tracked_teams);
        Bloody.registerPlayerDeathItems(CustomEventsOld.getPlayer_deathItems());
        ClassConfigManager.getInstance(this);  //其实也不用写，反正调用时会自动传入instance
        MyListener myListener = new  MyListener();
        getServer().getPluginManager().registerEvents(myListener, this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new SpawnProtectionListener(), this);
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
        getCommand("ec").setExecutor(new ECCommand());

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
        world.setGameRule(GameRule.NATURAL_REGENERATION, true);
        world.setGameRule(GameRule.RANDOM_TICK_SPEED, 0);
        world.setGameRule(GameRule.MOB_GRIEFING, true); //保证爆炸能破坏方块
        world.setGameRule(GameRule.FIRE_DAMAGE, true);  //防止神秘地图取消这俩
        world.setGameRule(GameRule.FALL_DAMAGE, true);
        world.setGameRule(GameRule.DROWNING_DAMAGE, true);
        world.setGameRule(GameRule.SPAWN_CHUNK_RADIUS,  0);


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


        for (ClassEnum ce : ClassEnum.values()) {
            ce.customClass.initializeCommonItems();  //等enum全部初始化后再执行
        }



        for (Player player : getServer().getOnlinePlayers()) {
            try {
                myListener.onPlayerJoin(new PlayerJoinEvent(player, "插件重载的伪加入游戏"));
            } catch (InvalidMidiDataException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static HashMap<Location, org.bukkit.block.BlockState> blockRegenWhenDisabled = new HashMap<>();
    @Override
    public void onDisable() {  //todo 搞config mark dirty 然后最后再保存
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
