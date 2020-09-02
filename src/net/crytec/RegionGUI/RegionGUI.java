package net.crytec.RegionGUI;

import java.io.File;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import net.crytec.RegionGUI.commands.LandAdmin;
import net.crytec.RegionGUI.commands.LandCommand;
import net.crytec.RegionGUI.data.Template;
import net.crytec.RegionGUI.commands.CommadClaimListener;
import net.crytec.RegionGUI.manager.PreviewBlockManager;
import net.crytec.RegionGUI.manager.TemplateManager;
import net.crytec.RegionGUI.utils.flags.FlagManager;




public final class RegionGUI extends JavaPlugin
{
    private static RegionGUI instance;
    private FlagManager flagmanager;
    
    static {
        ConfigurationSerialization.registerClass(Template.class, "Template");
    }
    
     
    @Override
    public void onLoad() {
        
        RegionGUI.instance = this;
        
        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdir();
        }
        
        final File file = new File(this.getDataFolder(), "config.yml");
        try {
            if (!file.exists()) {
                file.createNewFile();
                this.saveResource("config.yml", true);
                //this.log("§2Setup - New default configuration has been written.");
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    @Override
    public void onEnable() {
        
        loadLanguage();
        
        flagmanager = new FlagManager(this);
        
        TemplateManager templateManager = new TemplateManager(this);
        PreviewBlockManager pbm = new PreviewBlockManager();
        
        instance.getCommand("land").setExecutor(new LandCommand());
        instance.getCommand("landadmin").setExecutor(new LandAdmin());

        Bukkit.getPluginManager().registerEvents(new CommadClaimListener(), this);
        

    }

 

    
    @Override
    public void onDisable() {

        TemplateManager.saveAll();

    }
    
    
    public static RegionGUI getInstance() {
        return RegionGUI.instance;
    }
    
    
    @Override
    public void reloadConfig() {
        super.reloadConfig();
        this.loadLanguage();
    }
    
    public FlagManager getFlagManager() {
        return this.flagmanager;
    }
    
    public void loadLanguage() {
        final File file = new File(getInstance().getDataFolder(), "lang.yml");
        if (!file.exists()) {
            try {
                getInstance().getDataFolder().mkdir();
                file.createNewFile();
                if (file != null) {
                    final YamlConfiguration loadConfiguration = YamlConfiguration.loadConfiguration(file);
                    loadConfiguration.save(file);
                    Language.setFile(loadConfiguration);
                }
            }
            catch (IOException ex2) {
                log_err("Could not create language file!");
                Bukkit.getPluginManager().disablePlugin((Plugin)getInstance());
            }
        }
        final YamlConfiguration loadConfiguration2 = YamlConfiguration.loadConfiguration(file);
        Language[] values;
        for (int length = (values = Language.values()).length, i = 0; i < length; ++i) {
            final Language language = values[i];
            if (loadConfiguration2.getString(language.getPath()) == null) {
                if (language.isArray()) {
                    loadConfiguration2.set(language.getPath(), (Object)language.getDefArray());
                }
                else {
                    loadConfiguration2.set(language.getPath(), (Object)language.getDefault());
                }
            }
        }
        Language.setFile(loadConfiguration2);
        try {
            loadConfiguration2.save(file);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    

    public static boolean checkString (final String message) {
      final String allowed = "_0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя";
      for(int i = 0; i < message.length(); ++i) {
         if(!allowed.contains(String.valueOf(message.charAt(i)))) {
            return false;
         }
      }
      return true;
   }    

    public static void log_ok(String s) {   Bukkit.getConsoleSender().sendMessage("§2[§fРегионы§2] §7:§2 "+ s); }
    public static void log_warn(String s) {   Bukkit.getConsoleSender().sendMessage("§2[§fРегионы§2] §7:§6 "+ s); }
    public static void log_err(String s) {   Bukkit.getConsoleSender().sendMessage("§2[§fРегионы§2] §7:§c "+ s); }
    
}
