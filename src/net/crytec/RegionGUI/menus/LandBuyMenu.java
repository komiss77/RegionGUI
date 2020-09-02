package net.crytec.RegionGUI.menus;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.ArrayList;
import java.util.List;
import net.crytec.RegionGUI.Language;
import net.crytec.RegionGUI.RegionGUI;
import net.crytec.RegionGUI.data.PlotBuilder;
import net.crytec.RegionGUI.data.RegionUtils;
import net.crytec.RegionGUI.data.Template;
import net.crytec.RegionGUI.manager.PreviewBlockManager;
import net.crytec.RegionGUI.manager.TemplateManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import ru.komiss77.ApiOstrov;
import ru.komiss77.Managers.PM;
import ru.komiss77.Managers.WE;
import ru.komiss77.Objects.Oplayer;
import ru.komiss77.utils.ItemBuilder;
import ru.komiss77.utils.inventory.ClickableItem;
import ru.komiss77.utils.inventory.InventoryContent;
import ru.komiss77.utils.inventory.InventoryProvider;
import ru.komiss77.utils.inventory.SmartInventory;



public class LandBuyMenu implements InventoryProvider {
    //private final ClaimManager manager;
   // private final RegionGUI plugin;
    private static final ItemStack fill = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name(" ").build();;
    
    public LandBuyMenu(final RegionGUI plugin) {
        //this.plugin = plugin;
        //this.manager = plugin.getClaimManager();
    }
    
   
    
    
    
    
    @Override
    public void init(final Player player, final InventoryContent inventoryContents) {
        
        inventoryContents.fillRow(3, ClickableItem.empty(fill));
        
        final Oplayer op = PM.getOplayer(player);
        if (op==null) {
            RegionGUI.log_err("§c[ERROR] нет экземпляра Oplayer для "+player.getName());
            return;
        }
        
        
        final List <ProtectedRegion> playerRegions = RegionUtils.getPlayerOwnedRegions(player);
        
        int totalRegion = playerRegions.size();
        
        int totatRegionLimit = op.getBigestPermValue("region.limit.total");
        if (totatRegionLimit<1) totatRegionLimit = 1; //один приват всегда можно, раз уж есть плагин на сервере!
        


        if (!playerRegions.isEmpty()) {
            inventoryContents.set(4, 2, ClickableItem.of( new ItemBuilder(Material.GRAY_BED).name("§aТП в регион").build(), e -> {
                SmartInventory.builder().id("home-" + player.getName()).provider(new LandHomeMenu()).size(5, 9).title(Language.INTERFACE_HOME_TITLE.toString()).build().open(player);
                player.playSound(player.getLocation(), Sound.BLOCK_COMPARATOR_CLICK, 5, 5);
            }));

            inventoryContents.set(4, 6, ClickableItem.of( new ItemBuilder(Material.PAPER).name("§bСписок регионов в этом мире").build(), e -> {
                player.closeInventory();
                player.playSound(player.getLocation(), Sound.BLOCK_COMPARATOR_CLICK, 5, 5);
                player.performCommand("land list");
            }));
        }
                
                
                
                
                
        //получаем список заготовок для данного мира
        final List <Template> templateList =TemplateManager.getTemplates(player.getWorld());
        if (templateList == null || templateList.isEmpty()) {
            inventoryContents.add( ClickableItem.empty( new ItemBuilder(Material.BARRIER).name("§4Не вариантов покупки").lore("§cДля этого мира нет заготовок региона для покупки!","§c").build() ) );
            return;
        }

        if (totalRegion >= totatRegionLimit) {
            
            inventoryContents.add( ClickableItem.empty( new ItemBuilder(Material.BARRIER)
                    .name("§4Создание новых недоступно")
                    .lore("§cВаш глобальный лимит: "+totatRegionLimit,"§c")
                    .build() )
            );
            
        } else {
            
            for (final Template template : templateList) { //перебираем все заготовки

                int currentTemplateLimit = op.getBigestPermValue("region.limit."+template.getName());
                if (currentTemplateLimit<1) currentTemplateLimit = 1;

                //подсчёт приватов такого типа
                int currentTemplateCount = 0;
                for (ProtectedRegion rg:playerRegions) {
                    if (template.getName().equalsIgnoreCase(RegionUtils.getTemplateName(rg))) {
                        currentTemplateCount=++currentTemplateCount;
                    }
                }
                
                
                final ItemBuilder itemBuilder = new ItemBuilder(template.getIcon());
                itemBuilder.name(ChatColor.translateAlternateColorCodes('&', template.getDisplayname()));
                itemBuilder.setItemFlag(ItemFlag.HIDE_ATTRIBUTES);

                //final ArrayList <String> lore = new ArrayList();
                itemBuilder.addLore(template.getDescription());
                itemBuilder.addLore("");
                itemBuilder.addLore("§7Ваши регионы: §3"+(totalRegion==0?"не найдено":totalRegion)+(" §7(лимит: §5"+totatRegionLimit+"§7)"));
                itemBuilder.addLore("§7Регионы данного типа: §3"+(currentTemplateCount==0?"не найдено":currentTemplateCount)+(" §7(лимит: §5"+currentTemplateLimit+"§7)"));
                itemBuilder.addLore("§7Размеры: §e"+template.getSize()+"x"+template.getSize()+"§7, вниз §e"+template.getDepth()+"§7, вверх §e"+template.getHeight());
                itemBuilder.addLore("§7Цена: §b"+(template.getPrice()==0?"бесплатно":template.getPrice()+" §7лони."));
                itemBuilder.addLore("");
                
                //itemBuilder.lore(lore);

                //если нет права на эту заготовку, не кликабельное и добавляем сообщение 
                if ( !template.getPermission().isEmpty() && !player.hasPermission(template.getPermission())) {

                    //itemBuilder.lore("");
                    itemBuilder.addLore(template.getNoPermDescription());
                    inventoryContents.add(ClickableItem.empty(itemBuilder.build()));

                } else if (currentTemplateCount>=currentTemplateLimit) {
                    
                    itemBuilder.addLore("§cЛимит регионов данного типа!");
                    inventoryContents.add(ClickableItem.empty(itemBuilder.build()));
                    
                } else {
                    
                    itemBuilder.addLore("§6ЛКМ §f- предпросмотр на местности");
                    itemBuilder.addLore( WE.hasJob(player) ? "§cДождитесь окончания операции!" : "§6ПКМ §f- создать регион");
                    
                    inventoryContents.add(ClickableItem.of(itemBuilder.build(), e -> {
                        
                        if (e.getClick() == ClickType.RIGHT) { //пкм - покупка
                            
                            player.closeInventory();
                            if (WE.hasJob(player))  {
                                player.sendMessage("§cДождитесь окончания операции!");
                                return;
                            }
                            player.playSound(player.getLocation(), Sound.BLOCK_COMPARATOR_CLICK, 5, 5);

                                //создание привата
                            new PlotBuilder(player, template).build();
                            player.resetTitle();


                        } else if (e.getClick() == ClickType.LEFT) { //лкм - предпросмотр

                            //new RegionPreview(player, template.getSize() + 1);
                            player.closeInventory();
                            PreviewBlockManager.startPreview(player, template);
                            
                            player.playSound(player.getLocation(), Sound.BLOCK_COMPARATOR_CLICK, 5, 5);
                            
                            ApiOstrov.sendTitle(player, "§6Предпросмотр", "§aДля покупки ПКМ в меню");
                            //player.sendMessage("§6Предпросмотр региона");
                            player.sendMessage("§aДля покупки ПРАВЫЙ клик в меню покупки.");
                        }

                    }));
                }

            }
        }
        
        
        
    }
    
    
    
    
    
    
    
    
    
}
