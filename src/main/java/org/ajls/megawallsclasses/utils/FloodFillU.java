package org.ajls.megawallsclasses.utils;

import org.ajls.lib.advanced.HaxhMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.HashSet;

public class FloodFillU {
    public static void floodFill(Location location, int distance) {

    }


    //todo 研究这个算法经历了什么历史更迭 比如birth direction算法 还有学习网上洪水填充算法
    public static HashSet<Location> floodFillRespawn(Location location, int distance) {
        HashSet<Vector> directions = new HashSet<>();
        directions.add(new Vector(-1 ,0, 0));
        directions.add(new Vector(1, 0, 0));
        directions.add(new Vector(0, -1, 0));
        directions.add(new Vector(0 ,1, 0));
        directions.add(new Vector(0, 0, -1));
        directions.add(new Vector(0, 0, 1));
        HashSet<Location> currents = new HashSet<>();  //当前查找的边界
        HashSet<Location> nexts = new HashSet<>();  //当前查找的边界的延伸
        HashSet<Location> returnSet = new HashSet<>();
        HashSet<Location> checkedSet = new HashSet<>();
        currents.add(location);
        checkedSet.add(location);
        returnSet.add(location);  //todo::检查一开始的Locations是否安全
        for (int i = 0; i < distance; i++) {
            for (Location currentLocation: currents) {
                for (Vector vector: directions) {
                    Location next = currentLocation.clone().add(vector.multiply(-1));  //multiply -1 to make the goal vector , or just subtract vector
                    if (!checkedSet.contains(next) && !currents.contains(next)) {  //!BirthDirections.contains(vector)  !checkedSet
                        //可以试试用birth direction思想，当然也别忘记检测边界是否含有邻居，有可能环形路两边对碰了

                        if (isFloodFillable(next)) {
                            nexts.add(next);
                        }
                    }
                }

            }
//            if (di) <= 16
            for (Location nextLocation: nexts) {
                checkedSet.add(nextLocation);
                //if standable add to return set
                if (isRespawnableAfterFillable(nextLocation)){  //.getType() != Material.AIR                    //TODO: make it standable instead of air, so a button will trick the plugin.(Actually get highest block also tricks it)
                    returnSet.add(nextLocation);
                }
            }
            //        temp = currents;
            currents = nexts;
            nexts = new HashSet<>();
        }
        return returnSet;
    }

    private static boolean isFloodFillable(Location location) {
        return isFloodFillPassable(location) && isFloodFillPassable(location.clone().add(0, 1, 0));
    }

    private static boolean isFloodFillPassable(Location location) {
        Block block = location.getBlock();
        if (block.isPassable() && !block.isLiquid()) {
            return true;
        }
        return false;
    }

    private static boolean isRespawnableAfterFillable(Location location) {
        return location.clone().add(0, -1, 0).getBlock().isSolid();
    }

//    public class FloodFillDirections {
//        FloodFillDirections()
//    }

    public enum FloodFillDirection {
        X_P,
        X_N,
        Z_P,
        Z_N,
    }
}
