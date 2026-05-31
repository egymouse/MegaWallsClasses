package org.ajls.megawallsclasses.utils;

import org.ajls.lib.advanced.HaxhMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.HashSet;

public class FloodFillU {
    public static void floodFill(Location location, int distance) {

    }

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
        returnSet.add(location);
        for (int i = 0; i < distance; i++) {
            for (Location currentLocation: currents) {
                for (Vector vector: directions) {
                    Location next = currentLocation.clone().add(vector.multiply(-1));  //multiply -1 to make the goal vector , or just subtract vector
                    if (!checkedSet.contains(next) && !currents.contains(next)) {  //!BirthDirections.contains(vector)  !checkedSet
                        //可以试试用birth direction思想，当然也别忘记检测边界是否含有邻居，有可能环形路两边对碰了
                        //todo hashset里面locations真的写了hash方法吗？别到时候get的内存其实不同就爆炸了

                        if (next.getBlock().isPassable() && !next.getBlock().isLiquid()  ) {  //.getType() == Material.AIR //todo make it check passable and also it would be good to avoid 1 block gap so only flood when there a 2 block gap so that steve can cross
                            //todo check if the original code makes people respawn on grass when using             int height = world.getHighestBlockYAt(teleportLocation);
                            Location nextUp = next.clone().add(0, 1, 0);
                            if (nextUp.clone().add(0, 1, 0).getBlock().isPassable() && !nextUp.getBlock().isLiquid()) {  //.getType() == Material.AIR
                                nexts.add(next);
                            }
                        }
                    }
                }

            }
//            if (di) <= 16
            for (Location nextLocation: nexts) {
                checkedSet.add(nextLocation);
                //if standable add to return set
                if (nextLocation.clone().add(0, -1, 0).getBlock().isSolid()){  //.getType() != Material.AIR                    //TODO: make it standable instead of air, so a button will trick the plugin.(Actually get highest block also tricks it)
                    returnSet.add(nextLocation);
                }
            }
            //        temp = currents;
            currents = nexts;
            nexts = new HashSet<>();
        }
        return returnSet;
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
