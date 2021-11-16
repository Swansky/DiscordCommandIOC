package fr.swansky.discordCommandIOC;

import fr.swansky.discordCommandIOC.Commands.CommandManager;
import fr.swansky.discordCommandIOC.config.DiscordCommandIOCConfig;
import fr.swansky.swansAPI.exception.InstanceCreationException;
import fr.swansky.swansAPI.extensions.FrameworkExtension;

import java.util.Optional;

public class DiscordCommandIOC extends FrameworkExtension {
    private static DiscordCommandIOC INSTANCE;
    private static CommandManager COMMAND_MANAGER;
    private DiscordCommandIOCConfig discordCommandIOCConfig;

    public static DiscordCommandIOC getInstance() {
        return INSTANCE;
    }

    public static CommandManager getCommandManager() {
        return COMMAND_MANAGER;
    }

    @Override
    public void load() {
        INSTANCE = this;
        this.discordCommandIOCConfig = this.getIoc().getConfigExtensionManager().findConfigByClass(DiscordCommandIOCConfig.class);
        try {
            COMMAND_MANAGER = new CommandManager(this);
        } catch (InstanceCreationException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void unLoad() {

    }

    public Optional<DiscordCommandIOCConfig> getDiscordCommandIOCConfig() {
        return Optional.ofNullable(discordCommandIOCConfig);
    }
}
