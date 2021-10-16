package fr.swansky.discordCommandIOC.Commands.defaultCommands;

import fr.swansky.discordCommandIOC.Commands.annotations.Command;
import fr.swansky.discordCommandIOC.Commands.annotations.CommandsContainer;

@CommandsContainer
public class HelpCommand {

    @Command(name = "help",description = "help commands")
    private void help() {

    }
}
