package com.fireflyest.storage;

import com.fireflyest.essential.api.Data;
import com.fireflyest.essential.api.Storage;
import com.fireflyest.essential.bean.PlayerData;
import com.fireflyest.essential.data.Config;
import com.fireflyest.storage.sql.SqlData;
import com.fireflyest.storage.sql.SqlStorage;
import com.fireflyest.storage.sqll.SqLiteData;
import com.fireflyest.storage.sqll.SqLiteStorage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Fireflyest
 * 2022/1/1 11:05
 */
public class EssentialStorage extends JavaPlugin {

    public static JavaPlugin getInstance() { return plugin; }

    private static JavaPlugin plugin;

    @Override
    public void onEnable() {
        plugin = this;

        // 初始化数据库
        Storage storage;
        Data data;
        if(Config.SQL){
            if(Config.DEBUG) Bukkit.getLogger().info("使用数据库存储");
            // 数据库访问对象
            storage = new SqlStorage(Config.URL, Config.USER, Config.PASSWORD);
            data = new SqlData(storage);
        }else{
            if(Config.DEBUG) Bukkit.getLogger().info("使用本地存储");
            // 本地数据库访问对象
            String url = "jdbc:sqlite:" + getDataFolder().getParent() + "/Essential/storage.db";
            storage = new SqLiteStorage(url);
            data = new SqLiteData(storage);
        }

        // 注册服务
        this.getServer().getServicesManager().register(Storage.class, storage, plugin, ServicePriority.Normal);
        this.getServer().getServicesManager().register(Data.class, data, plugin, ServicePriority.Normal);

        // 初始化数据
        data.createTable(PlayerData.class);

    }

    @Override
    public void onDisable() {
    }

}
