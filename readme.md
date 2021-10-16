# Discord Command IOC 


Learning project!

### [Project example](https://github.com/Swansky/ExampleCommandManagerIOCExtension)




Simple commands manager for discord 

Plugin for [swans IOC Container](https://github.com/Swansky/SimpleIOCContainer)


use:

add [swans IOC Container](https://github.com/Swansky/SimpleIOCContainer) and this project

add @CommandsContainer annotation for class witch will contain commands

add @Command to your command method



```java
@CommandsContainer
public class DefaultCommands {
    public DefaultCommands() {
        System.out.println("test default commands containers");
    }

    @Command(name = "test", description = "")
    public void test(Test test,User user, Message message, TextChannel textchannel) {
        test.toTest();
    }
}
```

if user make command call this method: 

```java
DiscordCommandIOC.getCommandManager().commandUser(user,"test",message);
```

all parameters of command method will be auto-inject

by default, all these types are supported:
- String[] = args command
- User = user realizes the command
- TextChannel = channel were the command is made
- Guild = discord server
- String = all command string
- Message = JDA obj message
- MessageChannel = JDA obj


if you want to add more auto-inject object, add DiscordCommandIOCConfig like it: 

````java
public class SwansPlugin {

    public static void main(String[] args) {
        try {
            DiscordCommandIOCConfig discordCommandIOCConfig = new DiscordCommandIOCConfig();
            discordCommandIOCConfig.addObjectToAutoInjects(new Test());
            
            SwansIOC swansIOC = SwansIOC.CreateIOC(SwansPlugin.class);
            swansIOC.getConfigExtensionManager().addConfigExtension(discordCommandIOCConfig);
            swansIOC.CreateIOC();
            
        } catch (InstanceCreationException e) {
            e.printStackTrace();
        }
    }
}
````



