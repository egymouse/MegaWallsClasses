package org.ajls.megawallsclasses.packetlisteners;

import com.comphenix.protocol.events.PacketContainer;

public interface PacketHandler {
    void handle(PacketContainer packetContainer);
}
