package net.crytec.RegionGUI.utils.flags;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import java.util.Comparator;
import org.bukkit.ChatColor;
import net.crytec.shaded.org.apache.lang3.EnumUtils;
import org.bukkit.Material;
import com.sk89q.worldguard.protection.flags.LocationFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.WorldGuard;
import com.google.common.collect.Lists;
import net.crytec.RegionGUI.RegionGUI;
import java.util.ArrayList;
import net.crytec.phoenix.api.io.PluginConfig;
import java.util.LinkedList;


public class FlagManager {
    
    
    private LinkedList<FlagSetting> settings;
    private LinkedList<FlagSetting> flags;
    private final PluginConfig flagConfig;
    private ArrayList<String> forbiddenFlags;
    
    
    
    public FlagManager(final RegionGUI plugin) {
        this.settings = Lists.newLinkedList();//(LinkedList<FlagSetting>)Lists.newLinkedList();
        this.flags = Lists.newLinkedList();//(LinkedList<FlagSetting>)Lists.newLinkedList();
        this.forbiddenFlags = new ArrayList<>();//(ArrayList<String>)Lists.newArrayList()).add("receive-chat");
        this.forbiddenFlags.add("allowed-cmds");
        this.forbiddenFlags.add("blocked-cmds");
        this.forbiddenFlags.add("send-chat");
        this.forbiddenFlags.add("invincible");
        this.forbiddenFlags.add("command-on-entry");
        this.forbiddenFlags.add("command-on-exit");
        this.forbiddenFlags.add("console-command-on-entry");
        this.forbiddenFlags.add("console-command-on-exit");
        this.forbiddenFlags.add("godmode");
        this.forbiddenFlags.add("worldedit");
        this.forbiddenFlags.add("chunk-unload");
        this.forbiddenFlags.add("passthrough");
        this.forbiddenFlags.add("price");
        
        this.forbiddenFlags.add("greeting");
        this.forbiddenFlags.add("farewell");
        this.forbiddenFlags.add("greeting-title");
        this.forbiddenFlags.add("farewell-title");
        
        //this.forbiddenFlags.add("price");
        //this.forbiddenFlags.add("price");

        this.flagConfig = new PluginConfig(plugin, plugin.getDataFolder(), "flags.yml");
        
        boolean b = false;
        
        for (final Flag flag : WorldGuard.getInstance().getFlagRegistry().getAll()) {
            if (!this.forbiddenFlags.contains(flag.getName())) {
                if (flag instanceof LocationFlag) {
                    continue;
                }
                final String string = "flags." + flag.getName() + ".";
                if (!this.flagConfig.isSet("flags." + flag.getName() + ".name")) {
                    this.flagConfig.set(String.valueOf(string) + "name", (Object)("&7" + flag.getName()));
                    this.flagConfig.set(String.valueOf(string) + "enabled", (Object)true);
                    this.flagConfig.set(String.valueOf(string) + "icon", (Object)Material.LIGHT_GRAY_DYE.toString());
                    b = true;
                }
                Material icon = Material.LIGHT_GRAY_BANNER;
                if (EnumUtils.isValidEnum((Class)Material.class, this.flagConfig.getString(String.valueOf(string) + "icon"))) {
                    icon = Material.valueOf(this.flagConfig.getString(String.valueOf(string) + "icon"));
                }
                if (!this.flagConfig.getBoolean(String.valueOf(string) + "enabled")) {
                    continue;
                }
                this.addFlags(flag.getName(), (Flag<?>)flag, icon, ChatColor.translateAlternateColorCodes('&', this.flagConfig.getString(String.valueOf(string) + "name")));
            }
        }
        
        if (b) {
            RegionGUI.log_ok("§eFlag settings configuration updated with new entires.");
            this.flagConfig.saveConfig();
        }
        
       //this.settings.sort(Comparator.comparing((Function<? super Object, ? extends Comparable>)FlagSetting::getId));
        this.settings.sort(Comparator.comparing(FlagSetting::getId));
        //proc
       // this.getFlagMap().stream().map((Function<? super Object, ?>)FlagSetting::getPermission).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()).forEach(permission -> new Permission("region.flagmenu.all", "Allow the useage of all flags", PermissionDefault.FALSE).getChildren().put(permission.getName(), permission.getDefault().getValue(false)));
        
    //ff
      Permission var7 = new Permission("region.flagmenu.all", "Allow the useage of all flags", PermissionDefault.FALSE);
      
      for ( FlagSetting fs : getFlagMap()) {
          //List <String> perms = fs.getPermission().co
                  var7.getChildren().put(fs.getPermission().getName(), fs.getPermission().getDefault().getValue(false));
      }
      
      //((List)this.getFlagMap().stream().map(FlagSetting::getPermission).collect(Collectors.toList())).forEach(  (var1x) -> {
      //   var7.getChildren().put(var1x.getName(), var1x.getDefault().getValue(false));
      //}
      
     // );    
    
    
    }
    
    public void addFlags(final String idenfifier, final Flag<?> flag, final Material icon, final String displayname) {
        this.flags.add(new FlagSetting(idenfifier, flag, icon, displayname));
    }
    
    public LinkedList<FlagSetting> getFlagMap() {
        return this.flags;
    }
}
