package org.ajls.megawallsclasses.packetlisteners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import org.ajls.megawallsclasses.MegaWallsClasses;

public class PacketPlayServerSpawnEntityAdapter extends PacketAdapter {
    public PacketPlayServerSpawnEntityAdapter() {
        super(MegaWallsClasses.getPlugin(), PacketType.Play.Server.SPAWN_ENTITY);
    }
}
