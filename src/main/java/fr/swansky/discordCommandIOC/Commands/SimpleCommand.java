package fr.swansky.discordCommandIOC.Commands;

import java.lang.reflect.Method;

public record SimpleCommand(String name, String description, String role,
                            Object object, Method method) {

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getRole() {
        return role;
    }

    public Object getObject() {
        return object;
    }

    public Method getMethod() {
        return method;
    }


}
