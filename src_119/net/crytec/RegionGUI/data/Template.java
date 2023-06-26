package net.crytec.RegionGUI.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import net.crytec.RegionGUI.RegionGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;
import ru.komiss77.utils.ItemBuilder;



@SerializableAs("Template")
public class Template implements ConfigurationSerializable, Comparable<Template> {
    
    private final String name;
    private String world;
    private String displayname;
    private Material icon;
    private List<String> description;
    private int size;
    private int height;
    private int depth;
    private int price;
    private int refund;
    private Material borderMaterial;
    private boolean generateBorder;
    private List<String> runCommands;
    private String permission;
    private List<String> noPermDescription;
    
    public Template(final World world, final String name) {
//System.out.print("new Template name="+name);
        this.world = world.getName();
        this.name = name;
        this.displayname = "Обычный регион";
        this.icon = Material.OAK_SLAB;
        this.description = Arrays.asList("§7Небольшой регион");
        this.size = 15;
        this.height = 319;//256;
        this.depth = 10;
        this.price = 0;
        this.refund = 0;
        this.borderMaterial = Material.OAK_FENCE;
        this.generateBorder = true;
        this.runCommands = new ArrayList<>();
        this.permission = "";
        this.noPermDescription = Arrays.asList("Доступно всем.");
    }
  
    private Template(final String name) {
        this.name = name;
        this.displayname = "Обычный регион";
        this.icon = Material.OAK_SLAB;
        this.description = Arrays.asList("§7Небольшой регион");
        this.size = 15;
        this.height = 319;//256;
        this.depth = 10;
        this.price = 0;
        this.refund = 0;
        this.borderMaterial = Material.OAK_FENCE;
        this.generateBorder = true;
        this.runCommands = new ArrayList<>();
        this.permission = "";
        this.noPermDescription = Arrays.asList("Доступно всем.");

    }
    
    
    
    public ItemStack getIcon() {
        return new ItemBuilder(this.icon).name(this.displayname).build();
    }
    
   // public void setNoPermDescription(final List<String> list) {
   //     this.noPermDescription = list.stream().map(s -> ChatColor.translateAlternateColorCodes('&', s)).collect((Collector<? super Object, ?, List<String>>)Collectors.toList());
  //  }
   public void setNoPermDescription(List <String> list) {
        this.noPermDescription.clear();
        for (String desc : list) {
            this.description.add(ChatColor.translateAlternateColorCodes('&', desc));
        }
      //this.noPermDescription = (List)list.stream().map((str) -> {
     //    return ChatColor.translateAlternateColorCodes('&', str);
     // }).collect(Collectors.toList());
   }  
   
    public boolean hasRefund() {
        return this.refund > 0;
    }
    
    public Optional<World> getWorld() {
        return Optional.ofNullable(Bukkit.getWorld(this.world));
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }
    
    @Override
    public boolean equals(final Object obj) {
        return this == obj || (obj != null && obj instanceof Template && Objects.equals(this.name, ((Template)obj).name));
    }
    
    @Override
    public Map<String, Object> serialize() {
        final HashMap<String, Object> hashMap = new HashMap<>();//Maps.newHashMap();
        hashMap.put("name", this.name);
        hashMap.put("gui.displayname", this.displayname);
        hashMap.put("gui.icon", this.icon.toString());
        hashMap.put("gui.description", this.description);
        hashMap.put("gui.noPermDescription", this.noPermDescription);
        hashMap.put("data.size", this.size);
        hashMap.put("data.heigth", height==256 ? 319 : height);
        hashMap.put("data.depth", this.depth);
        hashMap.put("data.price", this.price);
        hashMap.put("data.refund", this.refund);
        hashMap.put("data.world", this.world);
        hashMap.put("data.permission", this.permission);
        hashMap.put("border.material", this.borderMaterial.toString());
        hashMap.put("border.enabled", this.generateBorder);
        hashMap.put("generation.runCommands", this.runCommands);
        return (Map<String, Object>)hashMap;
    }
    /*
            return "Заготовка(name=" + this.getName()+
                ", отображаемоеИмя=" + this.getDisplayname() + 
                ", иконка=" + this.getIcon() + 
                ", описание=" + this.getDescription() + 
                ", размер=" + this.getSize() + 
                ", мир=" + this.getWorld() + 
                ", высота=" + this.getHeight() + 
                ", клабина=" + this.getDepth() + 
                ", цена=" + this.getPrice() + 
                ", манибэк=" + this.getRefund() + 
                ", материалЗабора=" + this.getBorderMaterial() + 
                ", делатьЗабор=" + this.isGenerateBorder() + 
                ", команды=" + this.getRunCommands() + 
                ", права=" + this.getPermission() + 
                ", сообщениеЕслиНетПрав=" + this.getNoPermDescription() + ")";
    */
    public static Template deserialize(final Map<String, Object> map) {
        final Template template = new Template((String) map.get("name"));
        template.setDisplayname((String) map.get("gui.displayname"));
        template.setIcon(Material.valueOf((String)map.get("gui.icon")));
        template.description = ((List<String>)map.get("gui.description"));
        template.noPermDescription = ((List<String>)map.get("gui.noPermDescription"));
        template.setSize((int)map.get("data.size"));
        final int h = (int)map.get("data.heigth");
        template.setHeight(h==256 ? 319 : h);
        template.setDepth((int)map.get("data.depth"));
        template.setPrice((int)map.get("data.price"));
        template.setRefund((int)map.get("data.refund"));
        template.setWorld((String) map.get("data.world"));
        template.setPermission((String) map.getOrDefault("data.permission", ""));
        template.setBorderMaterial(Material.valueOf((String)map.get("border.material")));
        template.setGenerateBorder((boolean)map.get("border.enabled"));
        template.runCommands = ((List<String>)map.get("generation.runCommands"));
        RegionGUI.log_ok("Загружена заготовка " + template.getName()+ " - " + template.getDisplayname());
        return template;
    }
    
    @Override
    public int compareTo(final Template o) {
        return (this.size > o.getSize()) ? 1 : ((this.size < o.getSize()) ? -1 : 0);
    }
    
    public String getDisplayname() {
        return this.displayname;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setDisplayname(final String displayname) {
        this.displayname = displayname;
    }
    
    public void setIcon(final Material icon) {
        this.icon = icon;
    }
    
    public List<String> getDescription() {
        return this.description;
    }
    
    public void setDescription(final List<String> newDescription) {
        description.clear();
        for (String desc:newDescription) {
            description.add(ChatColor.translateAlternateColorCodes('&', desc));
        }
      //this.description = (List)list.stream().map((str) -> {
      //   return ChatColor.translateAlternateColorCodes('&', str);
      //}).collect(Collectors.toList());
    }
    
    public int getSize() {
        return this.size;
    }
    
    public void setSize(final int size) {
        this.size = size;
    }
    
    public void setWorld(final String world) {
        this.world = world;
    }
    
    public int getHeight() {
        return height<319 ? height : 319;
    }
    
    public void setHeight(final int height) {
        this.height = height>319 ? 319 : height;
    }
    
    public int getDepth() {
        return this.depth;
    }
    
    public void setDepth(final int depth) {
        this.depth = depth;
    }
    
    public int getPrice() {
        return this.price;
    }
    
    public void setPrice(final int price) {
        this.price = price;
    }
    
    public int getRefund() {
        return this.refund;
    }
    
    public void setRefund(final int refund) {
        this.refund = refund;
    }
    
    public Material getBorderMaterial() {
        return this.borderMaterial;
    }
    
    public void setBorderMaterial(final Material borderMaterial) {
        this.borderMaterial = borderMaterial;
    }
    
    public boolean isGenerateBorder() {
        return this.generateBorder;
    }
    
    public void setGenerateBorder(final boolean generateBorder) {
        this.generateBorder = generateBorder;
    }
    
    public List<String> getRunCommands() {
        return this.runCommands;
    }
    
    public void setRunCommands(final List<String> runCommands) {
        this.runCommands = runCommands;
    }
    
    public String getPermission() {
        return this.permission;
    }
    
    public void setPermission(final String permission) {
        this.permission = permission;
    }
    
    public List<String> getNoPermDescription() {
        return this.noPermDescription;
    }
    
    @Override
    public String toString() {
        return "Заготовка(name=" + this.getName()+
                ", отображаемоеИмя=" + this.getDisplayname() + 
                ", иконка=" + this.getIcon() + 
                ", описание=" + this.getDescription() + 
                ", размер=" + this.getSize() + 
                ", мир=" + this.getWorld() + 
                ", высота=" + this.getHeight() + 
                ", клабина=" + this.getDepth() + 
                ", цена=" + this.getPrice() + 
                ", манибэк=" + this.getRefund() + 
                ", материалЗабора=" + this.getBorderMaterial() + 
                ", делатьЗабор=" + this.isGenerateBorder() + 
                ", команды=" + this.getRunCommands() + 
                ", права=" + this.getPermission() + 
                ", сообщениеЕслиНетПрав=" + this.getNoPermDescription() + ")";
    }


    public Location getMinimumPoint(final Location loc) {
        final int halfSize = (int)Math.round(size / 2.0);
        int blockY = loc.getBlockY();
        if (blockY>30) blockY = 30;
        blockY-=depth;
        if (blockY<0) blockY=0;
        return new Location(loc.getWorld(), loc.getBlockX() - halfSize, blockY, loc.getBlockZ() - halfSize);
    }
    
    public Location getMaximumPoint(final Location loc) {
        final Location high =  getMinimumPoint(loc).add(size, 0, size);
        int blockY = loc.getBlockY();
        blockY+=height;
        if (blockY>319) blockY = 319;
        high.setY(blockY);
        return high;
    }
}
