package net.crytec.RegionGUI;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;
import net.crytec.RegionGUI.commands.LandAdmin;
import net.crytec.RegionGUI.commands.LandCommand;
import net.crytec.RegionGUI.data.Template;
import net.crytec.RegionGUI.manager.PreviewBlockManager;
import net.crytec.RegionGUI.manager.TemplateManager;
import net.crytec.RegionGUI.utils.flags.FlagManager;
import net.crytec.acf.BaseCommand;
import net.crytec.acf.BukkitCommandIssuer;
import net.crytec.acf.BukkitCommandManager;
import net.crytec.acf.InvalidCommandArgument;
import net.crytec.phoenix.api.PhoenixAPI;
import net.crytec.phoenix.api.chat.program.ChatEditorCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;



public final class RegionGUI extends JavaPlugin
{
    private static RegionGUI instance;
    //public final Logger log;
    //public static Economy econ;
    private FlagManager flagmanager;
    //private ClaimManager claimManager;
    //private PlayerManager playerManager;
    //public static String USER;
    
    static {
       // RegionGUI.econ = null;
        //RegionGUI.USER = "38105";
        ConfigurationSerialization.registerClass(Template.class, "Template");
        //ConfigurationSerialization.registerClass((Class)ClaimEntry.class, "PlayerClaim");
    }
    
   // public RegionGUI() {
        //this.log = Bukkit.getLogger();
   // }
    
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
        //loadConfig0();
        /*if (!this.setupEconomy()) {
            this.log("§cNo Vault Compatible Economy Plugin found! Cannot load RegionGUI", true);
            Bukkit.getPluginManager().disablePlugin((Plugin)this);
        }*/
        if (!PhoenixAPI.get().requireAPIVersion((JavaPlugin)this, 108)) {
            Bukkit.getPluginManager().disablePlugin((Plugin)this);
            log_err("Для работы нужен PhoenixAPI!");
            return;
        }
        
        this.loadLanguage();
        
        final BukkitCommandManager bukkitCommandManager = new BukkitCommandManager(this);
        
        
        bukkitCommandManager.getCommandContexts().registerContext(ProtectedRegion.class, bukkitCommandExecutionContext -> {
            final ProtectedRegion region = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(((BukkitCommandIssuer)bukkitCommandExecutionContext.getIssuer()).getPlayer().getWorld())).getRegion(bukkitCommandExecutionContext.popFirstArg());
            if (region == null) {
                throw new InvalidCommandArgument("Could not find an region with that name in your current world.");
            }
            return region;
        });
        
        bukkitCommandManager.getCommandContexts().registerContext(Template.class, bukkitCommandExecutionContext -> {
            final Optional<Template> first = TemplateManager.getTemplates((bukkitCommandExecutionContext.getIssuer()).getPlayer().getWorld()).stream().filter(regionClaim -> ChatColor.stripColor(regionClaim.getDisplayname()).equals(bukkitCommandExecutionContext.popFirstArg())).findFirst();
            if (!first.isPresent()) {
                throw new InvalidCommandArgument("Could not find a template with that name in your current world.");
            }
            return first.get();
        });
        
        this.flagmanager = new FlagManager(this);
        new ChatEditorCore(this);
        //this.claimManager = new ClaimManager(this);
        new TemplateManager(this);
        //this.playerManager = new PlayerManager(this, this.claimManager);
        //Bukkit.getPluginManager().registerEvents((Listener)new RegionPurchaseListener(), (Plugin)this);
        bukkitCommandManager.registerCommand(new LandCommand(this));
        bukkitCommandManager.registerCommand(new LandAdmin(this));
        
        Bukkit.getPluginManager().registerEvents(new PreviewBlockManager(), this);
        
        bukkitCommandManager.enableUnstableAPI("help");

    }
    
    @Override
    public void onDisable() {
       // if (this.getPlayerManager() != null) {
      //      this.getPlayerManager().saveOnDisable();
      //  }
        TemplateManager.saveAll();
      //  if (this.claimManager != null) {
      //      this.claimManager.saveAll();
       // }
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
