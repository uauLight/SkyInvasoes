package br.com.skyplugins.skyinvasoes;

import br.com.skyplugins.skyinvasoes.commands.MenuC;
import br.com.skyplugins.skyinvasoes.listeners.MenuL;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public final class SkyPlugins extends JavaPlugin {

    public static SkyPlugins plugin;

    @Override
    public void onEnable() {
        send(" ");
        send("&a[SkyInvasões]: &aIniciando SkyInvasões...");
        send("&a  ___________          .___                                               ");
        send("&a /   _____|  | _____.__|   | _______  ______    __________   ____   ______");
        send("&a \\_____  \\|  |/ <   |  |   |/    \\  \\/ \\__  \\  /  ___/  _ \\_/ __ \\ /  ___/");
        send("&a /        |    < \\___  |   |   |  \\   / / __ \\_\\___ (  <_> \\  ___/ \\___ \\ ");
        send("&a/_______  |__|_ \\/ ____|___|___|  /\\_/ (____  /____  \\____/ \\___  /____  >");
        send("&a        \\/     \\/\\/             \\/          \\/     \\/           \\/     \\/ ");
        send("&a Copyright SkyPlugins - www.skyplugins.com.br (Todos os direitos reservados)");
        send(" ");
        plugin = this;

        MenuC menuC = new MenuC();

        Bukkit.getPluginManager().registerEvents(new MenuL(), this);
        getCommand("invasoes").setExecutor(menuC);
        menuC.startUpdateTask();

    }

    public void send(String msg) {
        Bukkit.getConsoleSender().sendMessage(msg.replace("&", "§"));
    }

    @Override
    public void onDisable() {
        send(" ");
        send("&c[SkyInvasões]: Plugin desabilitado com sucesso.");
        send("&c  ___________          .___                                               ");
        send("&c /   _____|  | _____.__|   | _______  ______    __________   ____   ______");
        send("&c \\_____  \\|  |/ <   |  |   |/    \\  \\/ \\__  \\  /  ___/  _ \\_/ __ \\ /  ___/");
        send("&c /        |    < \\___  |   |   |  \\   / / __ \\_\\___ (  <_> \\  ___/ \\___ \\ ");
        send("&c/_______  |__|_ \\/ ____|___|___|  /\\_/ (____  /____  \\____/ \\___  /____  >");
        send("&c        \\/     \\/\\/             \\/          \\/     \\/           \\/     \\/ ");
        send("&c Copyright SkyPlugins - www.skyplugins.com.br (Todos os direitos reservados)");
        send(" ");
    }
}
