package fr.swansky.discordCommandIOC.Commands;

import fr.swansky.discordCommandIOC.Commands.annotations.Command;
import fr.swansky.discordCommandIOC.Commands.annotations.CommandsContainer;
import fr.swansky.discordCommandIOC.Commands.defaultCommands.HelpCommand;
import fr.swansky.discordCommandIOC.DiscordCommandIOC;
import fr.swansky.discordCommandIOC.config.DiscordCommandIOCConfig;
import fr.swansky.swansAPI.exception.InstanceCreationException;
import fr.swansky.swansAPI.models.ScannedClassDetails;
import net.dv8tion.jda.api.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class CommandManager {
    private final DiscordCommandIOC main;
    private final Map<String, SimpleCommand> commands = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(CommandManager.class);

    public CommandManager(DiscordCommandIOC main) throws InstanceCreationException {
        this.main = main;
        registerCommands();
        registerCommand(new HelpCommand());
    }

    private void registerCommands() throws InstanceCreationException {
        Set<ScannedClassDetails> commandClassScanned = this.main.getIoc().getClassScanning().scanClassesByAnnotation(this.main.getIoc().getAllScanClass(), CommandsContainer.class);
        List<ScannedClassDetails> scannedClassDetails = main.getIoc().getInstantiationService().instantiateServices(commandClassScanned);
        for (ScannedClassDetails scannedClassDetail : scannedClassDetails) {
            registerCommand(scannedClassDetail.getInstance());
        }
    }

    private void registerCommand(Object object) {
        for (Method method : object.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Command.class)) {
                Command command = method.getAnnotation(Command.class);
                method.setAccessible(true);
                SimpleCommand simpleCommand = new SimpleCommand(command.name(), command.description(), command.role(), object, method);
                commands.put(command.name(), simpleCommand);
            }
        }
    }

    public Collection<SimpleCommand> getCommands() {
        return commands.values();
    }

    public boolean commandUser(User user, String command, Message message) {
        Object[] object = getCommand(command);
        if (object[0] == null) return false;
        try {
            EventHandler preCommandEvent = this.main.getDiscordCommandIOCConfig().getPreCommandEvent();
            if (preCommandEvent != null && !preCommandEvent.execute(user, command, message)) {
                    return false;
            }

            execute(((SimpleCommand) object[0]), command, (String[]) object[1], message);

            EventHandler postCommandEvent = this.main.getDiscordCommandIOCConfig().getPostCommandEvent();
            if (postCommandEvent != null) {
                postCommandEvent.execute(user, command, message);
            }
        } catch (Exception exception) {
            logger.error("La methode " + ((SimpleCommand) object[0]).getMethod().getName() + " n'est pas correctement initialisé.");
            exception.printStackTrace();
        }
        return true;
    }

    private Object[] getCommand(String command) {
        String[] commandSplit = command.split(" ");
        String[] args = new String[commandSplit.length - 1];
        System.arraycopy(commandSplit, 1, args, 0, commandSplit.length - 1);
        SimpleCommand simpleCommand = null;
        for (SimpleCommand commandSearch : commands.values()) {
            if (commandSearch.getName().equalsIgnoreCase(commandSplit[0])) {
                simpleCommand = commandSearch;
                break;
            }
        }
        return new Object[]{simpleCommand, args};
    }

    private void execute(SimpleCommand simpleCommand, String command, String[] args, Message message) throws Exception {
        Parameter[] parameters = simpleCommand.getMethod().getParameters();
        Object[] objects = new Object[parameters.length];
        DiscordCommandIOCConfig discordCommandIOCConfig = main.getDiscordCommandIOCConfig();
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].getType() == String[].class) objects[i] = args;
            else if (parameters[i].getType() == User.class) objects[i] = message == null ? null : message.getAuthor();
            else if (parameters[i].getType() == TextChannel.class)
                objects[i] = message == null ? null : message.getTextChannel();
            else if (parameters[i].getType() == PrivateChannel.class)
                objects[i] = message == null ? null : message.getPrivateChannel();
            else if (parameters[i].getType() == Guild.class) objects[i] = message == null ? null : message.getGuild();
            else if (parameters[i].getType() == String.class) objects[i] = command;
            else if (parameters[i].getType() == Message.class) objects[i] = message;
            else if (parameters[i].getType() == MessageChannel.class)
                objects[i] = message == null ? null : message.getChannel();
            Object objectFromClass = discordCommandIOCConfig.getObjectFromClass(parameters[i].getType());
            if (objectFromClass != null) {
                objects[i] = objectFromClass;
            }
        }

        simpleCommand.getMethod().invoke(simpleCommand.getObject(), objects);
    }
}
