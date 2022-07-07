package me.faln.projects.warzonechests.factory;

import me.faln.projects.warzonechests.WarzoneChests;
import me.faln.projects.warzonechests.objects.YMLConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.logging.Logger;

public class YMLConfigFactory {

    private final Logger logger;
    private final WarzoneChests main;

    public YMLConfigFactory(WarzoneChests main) {
        this.main = main;
        logger = main.getLogger();
    }

    public YMLConfig createConfig(final File folder, final String name) {
        if (!folder.exists()) {
            if (folder.mkdirs()) {
                logger.info("Created folder for " + name + ".yml");
            }
        }

        File ymlFile = new File(folder, name + ".yml");

        if (!ymlFile.exists()) {
            try {
                main.saveResource(ymlFile.getName(), false);
                if (ymlFile.createNewFile()) {
                    logger.info("Created file: " + ymlFile.getName());
                }
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(ymlFile);

        return new YMLConfig(ymlFile, config);
    }
}
