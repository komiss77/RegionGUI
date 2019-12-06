package net.crytec.RegionGUI.menus;

import net.crytec.phoenix.api.implementation.AnvilGUI;
import net.crytec.phoenix.api.inventory.content.SlotPos;
import net.crytec.phoenix.api.inventory.ClickableItem;
import net.crytec.phoenix.api.inventory.SmartInventory;
import net.crytec.phoenix.api.utils.UtilPlayer;
import org.bukkit.Sound;
import org.bukkit.ChatColor;
import net.crytec.phoenix.api.item.ItemBuilder;
import org.bukkit.Material;
import net.crytec.RegionGUI.data.Template;
import java.util.Collections;
import java.util.ArrayList;
import net.crytec.RegionGUI.RegionGUI;
import net.crytec.phoenix.api.inventory.content.InventoryContents;
import org.bukkit.entity.Player;
import net.crytec.RegionGUI.manager.TemplateManager;
import net.crytec.phoenix.api.inventory.content.InventoryProvider;



public class AdminTemplateList implements InventoryProvider
{
    
    public AdminTemplateList() {
    }
    
    @Override
    public void init(final Player player, final InventoryContents contents) {
        
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
                UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK, 0.5f, 1.0f);
                SmartInventory.builder().provider((InventoryProvider)new TemplateEditor(template)).size(5).title("Редактирование " + template.getName()).build().open(player);
                //return;
            }));
        }
        
        contents.set(SlotPos.of(5, 4), new ClickableItem(new ItemBuilder(Material.EMERALD).name("§2Создание заготовки").build(), p2 -> {
            UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK, 0.5f, 1.0f);
            
            new AnvilGUI( player, "template", (player2, templateName) -> {
                
                if(templateName.length()>16 || !RegionGUI.checkString(templateName) ) {
                    player.sendMessage("§cНедопустимое имя!");
                    return null;
                } else if ( TemplateManager.getByName(templateName)!=null ) {
                    player.sendMessage("§cТакая заготовка уже есть");
                    return null;
                } else {
                    TemplateManager.registerTemplate(new Template(player2.getWorld(), templateName) );
                    TemplateManager.saveAll();
                    this.reOpen(player, contents);
                    return null;
                }
                
            });
        }));
        
        
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
