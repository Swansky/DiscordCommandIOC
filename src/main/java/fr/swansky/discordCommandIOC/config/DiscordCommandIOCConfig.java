package fr.swansky.discordCommandIOC.config;

import fr.swansky.swansAPI.config.ConfigExtension;

import java.util.ArrayList;
import java.util.List;

public class DiscordCommandIOCConfig implements ConfigExtension {
    // List of object for to inject to command in CommandManager.java
    private final List<Object> objectsToAutoInjects = new ArrayList<>();

    @Override
    public Class<? extends ConfigExtension> getConfigClass() {
        return this.getClass();
    }

    public void addObjectToAutoInjects(Object object) {
        objectsToAutoInjects.add(object);
    }

    public List<Object> getObjectsToAutoInjects() {
        return objectsToAutoInjects;
    }

    public <T> T getObjectFromClass(Class<T> clazz) {
        for (Object objectsToAutoInject : objectsToAutoInjects) {
            if (objectsToAutoInject != null) {
                if (clazz.isInstance(objectsToAutoInject)) {
                    return clazz.cast(objectsToAutoInject);
                }
            }
        }
        return null;
    }
}
