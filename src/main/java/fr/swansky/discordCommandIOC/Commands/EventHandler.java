package fr.swansky.discordCommandIOC.Commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

public interface EventHandler {
    boolean execute(User user, String command, Message message);
}
