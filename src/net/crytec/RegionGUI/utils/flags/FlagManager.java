package net.crytec.RegionGUI.utils.flags;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.LocationFlag;

import net.crytec.RegionGUI.RegionGUI;


public class FlagManager {
    
    
    private final List<FlagSetting> settings = new ArrayList<>();
    private final List<FlagSetting> flags = new ArrayList<>();;
    //private final LinkedList<FlagSetting> settings;
    //private final LinkedList<FlagSetting> flags;
    private final YamlConfiguration flagConfig;
    private final Set<String> forbiddenFlags;
    
    
    
    public FlagManager(final RegionGUI plugin) {
        //settings = Lists.newLinkedList();//(LinkedList<FlagSetting>)Lists.newLinkedList();
        //flags = Lists.newLinkedList();//(LinkedList<FlagSetting>)Lists.newLinkedList();
        
        forbiddenFlags = new HashSet<>();//(ArrayList<String>)Lists.newArrayList()).add("receive-chat");
        forbiddenFlags.add("allowed-cmds");
        forbiddenFlags.add("blocked-cmds");
        forbiddenFlags.add("send-chat");
        forbiddenFlags.add("invincible");
        forbiddenFlags.add("command-on-entry");
        forbiddenFlags.add("command-on-exit");
        forbiddenFlags.add("console-command-on-entry");
        forbiddenFlags.add("console-command-on-exit");
        forbiddenFlags.add("godmode");
        forbiddenFlags.add("worldedit");
        forbiddenFlags.add("chunk-unload");
        forbiddenFlags.add("passthrough");
        forbiddenFlags.add("price");
        forbiddenFlags.add("receive-chat");
        
        forbiddenFlags.add("greeting"); //настраивается в гл.меню
        forbiddenFlags.add("farewell"); //настраивается в гл.меню
        forbiddenFlags.add("greeting-title"); //настраивается в гл.меню
        forbiddenFlags.add("farewell-title"); //настраивается в гл.меню
        
        flagConfig = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "flags.yml"));
        
        boolean newFlag = false;
        Material icon;
        
        
        for (final Flag<?> flag : WorldGuard.getInstance().getFlagRegistry().getAll()) {
            if (forbiddenFlags.contains(flag.getName())) continue;
            if (flag instanceof LocationFlag) continue;

            final String flagPath = "flags." + flag.getName() + ".";
            
            if (flagConfig.isSet("flags." + flag.getName() + ".name")) {
                
                if (!flagConfig.getBoolean(flagPath + "enabled")) continue;
                icon = Material.matchMaterial( flagConfig.getString(flagPath + "icon"));
                if (icon==null) icon = Material.LIGHT_GRAY_DYE;
                addFlags(flag.getName(), flag, icon, ChatColor.translateAlternateColorCodes('&', flagConfig.getString(flagPath + "name")));
                
            } else {
                
                flagConfig.set(flagPath + "name", ("&7" + flag.getName()));
                flagConfig.set(flagPath + "enabled", true);
                flagConfig.set(flagPath + "icon", Material.LIGHT_GRAY_DYE.toString());
                newFlag = true;
                addFlags(flag.getName(), flag, Material.LIGHT_GRAY_DYE, "§7" + flag.getName());
               
            }
            
        }
        
        
        if (newFlag) {
            RegionGUI.log_ok("§eВ конфигурацию флагов добавлены новые записи!");
           try {
              flagConfig.save(new File(plugin.getDataFolder(), "flags.yml"));
           } catch (IOException ex) {
              ex.printStackTrace();
           }
        }
        
        settings.sort(Comparator.comparing(FlagSetting::getId));
        
        //final Permission perm = new Permission("region.flagmenu.all", "Allow the useage of all flags", PermissionDefault.FALSE);
        //flags.forEach( (fs) -> {
        //    perm.getChildren().put(fs.getPermission().getName(), fs.getPermission().getDefault().getValue(false));
        //});
 
    
    
    }
    
    public void addFlags(final String idenfifier, final Flag<?> flag, final Material icon, final String displayname) {
        flags.add(new FlagSetting(idenfifier, flag, icon, displayname));
    }
    
    public List<FlagSetting> getFlagMap() {
        return flags;
    }
}
