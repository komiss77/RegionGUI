package net.crytec.RegionGUI.manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import net.crytec.RegionGUI.RegionGUI;
import net.crytec.RegionGUI.data.Template;
import net.crytec.shaded.org.apache.commons.io.FileUtils;
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
       //plots.sort(Comparator.comparing(Plot::getVotePoints).reversed());
   //   return (Set)this.templates.values().stream().filter((var0) -> {
   //      return var0.getWorld().isPresent();
  //   }).filter((var1x) -> {
   //      return ((World)var1x.getWorld().get()).equals(var1);
   //   }).collect(Collectors.toSet());
   }
   
  // public Set<RegionClaim> getTemplates(final World world) {
    //    return this.templates.values().stream().filter(regionClaim -> regionClaim.getWorld().isPresent()).filter(regionClaim2 -> regionClaim2.getWorld().get().equals(world)).collect((Collector<? super RegionClaim, ?, Set<RegionClaim>>)Collectors.toSet());
    //}
    
    //public static Optional<Template> getByName(final World world, final String name) {
    //    return templates.values().stream().filter(regionClaim -> regionClaim.getWorld().isPresent()).filter(regionClaim2 -> regionClaim2.getWorld().get().equals(world) && regionClaim2.getDisplayname().equals(name)).findFirst();
    //}
    
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
        //RegionGUI.getInstance().getPlayerManager().getPlayerdata().values().forEach(set -> set.removeIf(claimEntry -> claimEntry.getTemplate().equals(claim)));
    }
    
    public static void loadTemplates() {
        final Iterator iterateFiles = FileUtils.iterateFiles(templateFolder, new String[] { "template" }, false);
        while (iterateFiles.hasNext()) {
            final File file = (File) iterateFiles.next();
            try {
                final Template template = YamlConfiguration.loadConfiguration(file).getSerializable("data", Template.class);
                templates.put(template.getName().toLowerCase(), template);
            } catch (Exception ex) {
                RegionGUI.log_err("Не удалось загрузить заготовку из файла " + file.getName()+" : "+ex.getMessage());
                //ex.printStackTrace();
            }
        }
    }
    
    public static void saveAll() {
        for (final Template template : templates.values()) {
            try {
                final File file = new File(templateFolder, String.valueOf(template.getName()) + ".template");
                if (!file.exists()) {
                    file.createNewFile();
                }
                final YamlConfiguration loadConfiguration = YamlConfiguration.loadConfiguration(file);
                loadConfiguration.set("data", (Object)template);
                loadConfiguration.save(file);
            }
            catch (IOException ex) {
                RegionGUI.log_err("Не удалось сохранить заготовку "+template.getName()+" : "+ex.getMessage());
            }
        }
    }
}
