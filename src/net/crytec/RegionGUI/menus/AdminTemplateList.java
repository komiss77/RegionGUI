package net.crytec.RegionGUI.menus;


import java.util.ArrayList;
import java.util.Collections;
import ru.komiss77.utils.inventory.ClickableItem;
import ru.komiss77.utils.inventory.InventoryContent;
import ru.komiss77.utils.inventory.InventoryProvider;
import ru.komiss77.utils.inventory.SlotPos;
import net.crytec.RegionGUI.RegionGUI;
import net.crytec.RegionGUI.data.Template;
import net.crytec.RegionGUI.manager.TemplateManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import ru.komiss77.utils.ItemBuilder;
import ru.komiss77.utils.inventory.SmartInventory;
import ru.komiss77.version.AnvilGUI;



public class AdminTemplateList implements InventoryProvider
{
    
    public AdminTemplateList() {
    }
    
    @Override
    public void init(final Player player, final InventoryContent contents) {
        
        final ArrayList<Template> list = new ArrayList<>(TemplateManager.getTemplates(player.getWorld()));
        Collections.sort(list);
        
        for (final Template template : list) {
            final ItemBuilder itemBuilder = new ItemBuilder((template.getIcon() == null) ? Material.OAK_FENCE : template.getIcon().getType());
            itemBuilder.name(ChatColor.translateAlternateColorCodes('&', template.getName()));
            //final ArrayList <String> list2 = new ArrayList(template.getDescription());
            //list2.replaceAll(s -> ChatColor.translateAlternateColorCodes('&', s));
            itemBuilder.lore(template.getDescription());
            //final RegionClaim claim;
            contents.add(ClickableItem.of(itemBuilder.build(), p2 -> {
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.0f);
                SmartInventory.builder().provider((InventoryProvider)new TemplateEditor(template)).size(5).title("Редактирование " + template.getName()).build().open(player);
                //return;
            }));
        }
        
        contents.set(SlotPos.of(5, 4), new ClickableItem(new ItemBuilder(Material.EMERALD).name("§2Создание заготовки").build(), p2 -> {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.0f);
            
            new AnvilGUI(RegionGUI.getInstance(), player, "template", (player2, templateName) -> {
                
                if(templateName.length()>16 || !RegionGUI.checkString(templateName) ) {
                    player.sendMessage("§cНедопустимое имя!");
                    return null;
                } else if ( TemplateManager.getByName(templateName)!=null ) {
                    player.sendMessage("§cТакая заготовка уже есть");
                    return null;
                } else {
                    TemplateManager.registerTemplate(new Template(player2.getWorld(), templateName) );
                    TemplateManager.saveAll();
                    this.reopen(player, contents);
                    return null;
                }
                
            });
        }));
        
        
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
