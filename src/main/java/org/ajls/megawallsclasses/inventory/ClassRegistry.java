package org.ajls.megawallsclasses.inventory;

import java.util.HashMap;
import java.util.Map;

public final class ClassRegistry {

    private final Map<Integer, ClassKit> kits = new HashMap<>();

    public void register(ClassKit kit) {
        kits.put(kit.classId(), kit);
    }

    public ClassKit get(int classId) {
        ClassKit kit = kits.get(classId);
        if (kit == null) {
            throw new IllegalArgumentException("Unknown class id: " + classId);
        }
        return kit;
    }
}
