package skywolf46.nicknameapi.util;

import org.bukkit.Bukkit;

public final class VersionUtil {
    private VersionUtil() {
        // Prevent initialization
    }

    public static String extractVersion() {
        String version = Bukkit.getVersion();
        int versionStartIndex = version.indexOf("MC:") + 4;
        return version.substring(versionStartIndex, version.indexOf(')', versionStartIndex))
                .replace(" ", "");
    }
}
