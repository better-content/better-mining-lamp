package com.bettercontent.bettermininglamp;

import java.util.function.Predicate;
import java.util.stream.Stream;

/** Optional client integration policy, independent of the optional provider's classes. */
public final class MiningLampLightPolicy {
    private MiningLampLightPolicy() {}

    public static boolean registerIfAvailable(Predicate<String> loaded, Runnable registration) {
        if (!loaded.test("sodiumdynamiclights")) return false;
        registration.run();
        return true;
    }

    public static boolean isWorn(boolean armorHead, Stream<String> lampCurioSlots) {
        return armorHead || lampCurioSlots.anyMatch("head"::equals);
    }

    public static int luminance(boolean worn) { return worn ? 15 : 0; }
}
