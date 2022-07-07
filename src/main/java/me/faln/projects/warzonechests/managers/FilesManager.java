package me.faln.projects.warzonechests.managers;

import me.faln.projects.warzonechests.WarzoneChests;
import me.faln.projects.warzonechests.factory.YMLConfigFactory;
import me.faln.projects.warzonechests.objects.YMLConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilesManager {

    private final Map<String, YMLConfig> filesMap = new HashMap<>();

    private final WarzoneChests main;

    public FilesManager(WarzoneChests main) {
        this.main = main;
        createFiles();
    }

    private void createFiles() {
        YMLConfigFactory configFactory = new YMLConfigFactory(main);
        for (String fileName : new String[] {"config", "chests", "lang", "data"}) {
            YMLConfig ymlConfig = configFactory.createConfig(main.getDataFolder(), fileName);
            this.filesMap.put(fileName, ymlConfig);
        }
    }

    public YMLConfig getFile(final String fileName) {
        return this.filesMap.getOrDefault(fileName, null);
    }

    public void reloadAll(List<String> files) {
        for (String file : files) {
            this.getFile(file).reload();
        }
    }
}
