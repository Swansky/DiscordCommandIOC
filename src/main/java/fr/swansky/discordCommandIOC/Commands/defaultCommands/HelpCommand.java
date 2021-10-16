package fr.swansky.discordCommandIOC.Commands.defaultCommands;

import fr.swansky.discordCommandIOC.Commands.SimpleCommand;
import fr.swansky.discordCommandIOC.Commands.annotations.Command;
import fr.swansky.discordCommandIOC.Commands.annotations.CommandsContainer;
import fr.swansky.discordCommandIOC.DiscordCommandIOC;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.internal.entities.UserImpl;

import java.awt.*;
import java.util.Collection;

@CommandsContainer
public class HelpCommand {


    @Command(name = "help")
    private void help(User user, TextChannel channel) {
        if (channel != null) {
            if (!channel.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_EMBED_LINKS)) return;
        }
        Collection<SimpleCommand> commands = DiscordCommandIOC.getCommandManager().getCommands();
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("**Liste des commandes**", null);
        builder.setColor(Color.DARK_GRAY);
        for (SimpleCommand elem : commands) {
            builder.addField("**" + elem.getName() + "**", "" + elem.getDescription() + "", true);
        }
        if (!user.hasPrivateChannel()) {
            user.openPrivateChannel().complete();
            ((UserImpl) user).getPrivateChannel().sendMessageEmbeds(builder.build()).queue();
        }
        channel.sendMessage(user.getAsMention() + " la liste des commandes a été envoyé par messages privé.").queue();

    }
}
