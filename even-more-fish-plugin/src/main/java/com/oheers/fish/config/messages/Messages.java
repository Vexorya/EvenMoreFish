package com.oheers.fish.config.messages;

import com.oheers.fish.EvenMoreFish;
import com.oheers.fish.config.ConfigBase;
import com.oheers.fish.config.MainConfig;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;

public class Messages extends ConfigBase {

    private static Messages instance = null;
    private ConfigBase defaultEn;

    public Messages() {
        super("messages.yml", "locales/" + "messages_" + MainConfig.getInstance().getLocale() + ".yml", EvenMoreFish.getInstance(), true);
        defaultEn = new ConfigBase("default/messages_en.yml", "locales/messages_en.yml", EvenMoreFish.getInstance(), false);
        defaultEn.getFile().delete();
        defaultEn.reload();
        instance = this;
    }

    public static Messages getInstance() {
        return instance;
    }

    public String getSTDPrefix() {
        return getConfig().getString("prefix-regular") + getConfig().getString("prefix") + "&r";
    }

    public ConfigBase getDefaultEn() {
        return defaultEn;
    }

    @Override
    public UpdaterSettings getUpdaterSettings() {
        return UpdaterSettings.builder()
                .setVersioning(new BasicVersioning("config-version"))
                // Bossbar config relocations - config version 2
                .addCustomLogic("2", yamlDocument -> {
                    String hourColor = yamlDocument.getString("bossbar.hour-color");
                    String hour = yamlDocument.getString("bossbar.hour");
                    yamlDocument.set("bossbar.hour", hourColor + "{hour}" + hour);
                    yamlDocument.remove("bossbar.hour-color");

                    String minuteColor = yamlDocument.getString("bossbar.minute-color");
                    String minute = yamlDocument.getString("bossbar.minute");
                    yamlDocument.set("bossbar.minute", minuteColor + "{minute}" + minute);
                    yamlDocument.remove("bossbar.minute-color");

                    String secondColor = yamlDocument.getString("bossbar.second-color");
                    String second = yamlDocument.getString("bossbar.second");
                    yamlDocument.set("bossbar.second", secondColor + "{second}" + second);
                    yamlDocument.remove("bossbar.second-color");
                })
                // Prefix config relocations - config version 3
                .addCustomLogic("3", yamlDocument -> {
                    String prefix = yamlDocument.getString("prefix");

                    String oldRegular = yamlDocument.getString("prefix-regular");
                    yamlDocument.set("prefix-regular", oldRegular + prefix);

                    String oldAdmin = yamlDocument.getString("prefix-admin");
                    yamlDocument.set("prefix-admin", oldAdmin + prefix);

                    String oldError = yamlDocument.getString("prefix-error");
                    yamlDocument.set("prefix-error", oldError + prefix);

                    yamlDocument.remove("prefix");
                })
                .build();
    }

}
