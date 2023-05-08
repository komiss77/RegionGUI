package net.crytec.RegionGUI.manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import net.crytec.RegionGUI.RegionGUI;
import net.crytec.RegionGUI.data.Template;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;



public class TemplateManager implements Listener
{
   // private static RegionGUI plugin;
    private static LinkedHashMap<String, Template> templates;
    private static File templateFolder;
    
    
    public TemplateManager(final RegionGUI plugin) {
        templates = new LinkedHashMap<>();//LinkedHashMap<UUID, RegionClaim>)Maps.newLinkedHashMap();
       // ClaimManager.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(TemplateManager.this, plugin);
        templateFolder = new File(plugin.getDataFolder(), "templates");
        if (!templateFolder.exists()) {
            templateFolder.mkdir();
        }
        loadTemplates();
    }
    
    public static void registerTemplate(final Template template) {
        templates.put(template.getName(), template);
        save(template);
    }
    

   public static List<Template> getTemplates(final World world) {
       List <Template> result = new ArrayList<>();
       for (Template regionClaim : templates.values()) {
           if (regionClaim.getWorld().isPresent()) {
               if (regionClaim.getWorld().get().getName().equals(world.getName())) {
                   result.add(regionClaim);
               }
           }
       }
       result.sort(Comparator.comparing(Template::getSize));
       return result;
   }
    
    public static Template getByName(final String name) {
        if (name.isEmpty() || !templates.containsKey(name.toLowerCase())) return null;
        return templates.get(name.toLowerCase());
    }
    
    public static void deleteTemplate(final Template template) {
        templates.remove(template.getName());
        final File file = new File(templateFolder, String.valueOf(template.getName()) + ".template");
        if (file.exists()) {
            file.delete();
        }
    }
    
    public static void loadTemplates() {
    	for (final File tp : templateFolder.listFiles()) {
    		final String fnm = tp.getName();
    		if (!fnm.substring(fnm.lastIndexOf('.') + 1).equals("template")) continue;
            try {
                final Template template = YamlConfiguration.loadConfiguration(tp).getSerializable("data", Template.class);
                templates.put(template.getName().toLowerCase(), template);
            } catch (Exception ex) {
                RegionGUI.log_err("Не удалось загрузить заготовку из файла " + fnm + " : " + ex.getMessage());
                //ex.printStackTrace();
            }
    	}
    }
    
    public static void save(final Template template) {
        try {
            final File file = new File(templateFolder, String.valueOf(template.getName()) + ".template");
            if (!file.exists()) {
                file.createNewFile();
            }
            final YamlConfiguration loadConfiguration = YamlConfiguration.loadConfiguration(file);
            loadConfiguration.set("data", (Object)template);
            loadConfiguration.save(file);
        } catch (IOException ex) {
            RegionGUI.log_err("Не удалось сохранить заготовку "+template.getName()+" : "+ex.getMessage());
        }
    }
    
    
    
}
