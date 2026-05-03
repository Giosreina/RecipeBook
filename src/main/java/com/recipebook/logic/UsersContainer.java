package com.recipebook.logic;

import java.util.ArrayList;
import java.util.List;

public class UsersContainer {
    private List<User> users;

    public UsersContainer() {
        this.users = new ArrayList<>();
    }

    public UsersContainer(List<User> users) {
        this.users = users;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public User selectUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public User getUserById(int id) {
        for (User user : users) {
            if (user.getId_usuario() == id) {
                return user;
            }
        }
        return null;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public int size() {
        return users.size();
    }

    public boolean isEmpty() {
        return users.isEmpty();
    }
}
