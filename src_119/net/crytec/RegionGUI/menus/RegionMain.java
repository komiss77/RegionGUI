package net.crytec.RegionGUI.menus;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.crytec.RegionGUI.Language;
import net.crytec.RegionGUI.data.RegionUtils;
import net.crytec.RegionGUI.data.Template;
import net.crytec.RegionGUI.manager.TemplateManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ru.komiss77.ApiOstrov;
import ru.komiss77.Ostrov;
import ru.komiss77.utils.ItemBuilder;
import ru.komiss77.utils.inventory.ClickableItem;
import ru.komiss77.utils.inventory.InventoryContent;
import ru.komiss77.utils.inventory.InventoryProvider;
import ru.komiss77.utils.inventory.SlotPos;
import ru.komiss77.utils.inventory.SmartInventory;



public class RegionMain
implements InventoryProvider {
    public static final ItemStack fill = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name(" ").build();
    private final ProtectedRegion region;

    public RegionMain(final ProtectedRegion region) {
        this.region = region;
    }

    

    @Override
    public void init(Player player, InventoryContent inventoryContents) {
        player.playSound(player.getLocation(), Sound.BLOCK_COMPARATOR_CLICK, 5, 5);
        
        inventoryContents.fillBorders(ClickableItem.empty(fill));
        
        final Template template = TemplateManager.getByName( RegionUtils.getTemplateName(region) );
        final String createTime = RegionUtils.getCreateTime(region);
        
        
        
        
        
        
        //инфо о регионе
        inventoryContents.set(0, 4, ClickableItem.empty(new ItemBuilder(Material.FLOWER_BANNER_PATTERN)
                .name("§6>> §7Информация §6<<")
                .lore(template==null ? "":"§7Тип :§6 "+template.getDisplayname())
                .lore(template==null ? "§8Для региона не найдено":"§7Основание :§6 "+template.getSize()+"x"+template.getSize())
                .lore(template==null ? "§8соответствующей заготовки.":"§7Высота :§6 "+template.getHeight())
                .lore(template==null ? "§8Регион не удалится,":"§7Подземная часть :§6 "+template.getDepth())
                .lore(template==null ? "§8но невозможно получить":"§7Стоимость :§6 "+template.getPrice())
                .lore(template==null ? "§8подробные данные о нём.":"§7Возврат после удаления :§6 "+template.getRefund())
                .lore("§7Создан: §6"+(createTime.isEmpty()?"§8нет данных":createTime))
                //.unsaveEnchantment(Enchantment.ARROW_INFINITE, 1)
                //.setItemFlag(ItemFlag.HIDE_ENCHANTS)
                .build()));

        
        
        //кнопка удаления
        inventoryContents.set(0, 8, ClickableItem.of( 
                new ItemBuilder(Material.TNT)
                        .name(Language.INTERFACE_MANAGE_BUTTON_DELETEREGION.toString())
                        .lore(Language.INTERFACE_MANAGE_BUTTON_DELETEREGION_DESCRIPTION.getDescriptionArray())
                        .lore( ApiOstrov.getWorldEditor().hasJob(player) ? "§cДождитесь окончания операции!" : "§4Шифт+ПКМ §f- удалить")
                        .build(), e -> {
                            if ( e.getClick()==ClickType.SHIFT_RIGHT && !ApiOstrov.getWorldEditor().hasJob(player) ) {
                                player.playSound(player.getLocation(), Sound.BLOCK_COMPARATOR_CLICK, 5, 5);
                                Inventory inventory = SmartInventory.builder().provider(new RegionDeleteConfirm(this.region)).title(Language.INTERFACE_DELETE_TITLE.toString()).size(1).build().open(player);
                            }
                        }
        ));
        
        
        
        
        
        //кнопка юзеры
        inventoryContents.set(1, 1, ClickableItem.of( new ItemBuilder(Material.PLAYER_HEAD)
                .name("§6>> Управление пользователями §6<<")
                .lore ("§7Пользователей"+(region.getMembers().getPlayerDomain().size()==0 ? " нет" : ": "+region.getMembers().getPlayerDomain().size()))
                .lore("")
                .lore("§6ЛКМ §f- просмотр / удаление")
                .lore("§6ПКМ §f- добавление")
                .lore("§7Для добавления через меню,")
                .lore("§7кандидат должен находиться")
                .lore("§7в вашем регионе, и быть не далее")
                .lore("§75 блоков от вас.")
                .build(), inventoryClickEvent 
                -> {
                    if (inventoryClickEvent.getClick() == ClickType.LEFT) {

                        SmartInventory.builder().id("regiongui.editmembers").provider(new RegionEditMembers(this.region)).size(5).title("§2Пользователи").build().open(player);

                    }  else if (inventoryClickEvent.getClick() == ClickType.RIGHT) {

                        SmartInventory.builder().id("regiongui.addmembers").provider(new RegionAddMembers(this.region)).size(5).title("§2Найдены рядом").build().open(player);

                    }
                }
        ));


        
        
        
        inventoryContents.set(SlotPos.of(1, 3), ClickableItem.of( new ItemBuilder(Material.OAK_SIGN).name("§2Сообщения при входе/выходе").build(), p2 -> {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 2,2);
            SmartInventory.builder().provider(new RegionMessages(region)).size(3).title("§2Сообщения").build().open(player);
        }));
        

        
        
        
        //меню флагов
        //if (player.hasPermission("region.manage.flagmenu")) {
            player.playSound(player.getLocation(), Sound.BLOCK_COMPARATOR_CLICK, 5, 5);
            inventoryContents.set(1, 5, ClickableItem.of( new ItemBuilder(Material.LIGHT_GRAY_BANNER).name(Language.INTERFACE_MANAGE_BUTTON_FLAG.toString()).build(), inventoryClickEvent 
                    -> {
                        player.playSound(player.getLocation(), Sound.BLOCK_COMPARATOR_CLICK, 5, 5);
                        SmartInventory.builder().id("regiongui.flagMenu").provider((InventoryProvider)new RegionFlagMenu(this.region)).size(5, 9).title(Language.FLAG_TITLE.toString()).build().open(player);
                    }
            ));
        //}

            
            
            
        //показ границ
        inventoryContents.set(1, 7, ClickableItem.of( new ItemBuilder(Material.EXPERIENCE_BOTTLE).name(Language.INTERFACE_MANAGE_BUTTON_SHOWBORDER.toString()).lore(Language.INTERFACE_MANAGE_BUTTON_SHOWBORDER_DESCRIPTION.getDescriptionArray()).build(), inventoryClickEvent 
                -> {
            player.playSound(player.getLocation(), Sound.BLOCK_COMPARATOR_CLICK, 5, 5);
            
            Ostrov.VM.getNmsServer().BorderDisplay(
                player, 
                BukkitAdapter.adapt(player.getWorld(), region.getMinimumPoint()), 
                BukkitAdapter.adapt(player.getWorld(), region.getMaximumPoint()),
                true);
            
            //BorderDisplay borderDisplay = new BorderDisplay(
            //    player, 
            //    BukkitAdapter.adapt(player.getWorld(), region.getMinimumPoint()), 
            //    BukkitAdapter.adapt(player.getWorld(), region.getMaximumPoint()),
            //    true
            //);
        }));

    
    
    
    
    
    
    
    
        //установка точки ТП
        inventoryContents.set( 2, 6, ClickableItem.of( new ItemBuilder(Material.ENDER_PEARL).name(Language.INTERFACE_HOME_BUTTON.toString()).lore(Language.INTERFACE_HOME_BUTTON_DESC.getDescriptionArray()).build(), inventoryClickEvent 
                -> {
            player.playSound(player.getLocation(), Sound.BLOCK_COMPARATOR_CLICK, 5, 5);
            this.region.setFlag((Flag)Flags.TELE_LOC, BukkitAdapter.adapt(player.getLocation()));
            this.region.setDirty(true);
            player.sendMessage(Language.INTERFACE_HOME_BUTTON_SUCCESS.toChatString());
        }));
        
    
    
    

        
        
        
        
        
        
        
        
        
    
    
    }
    


    
    
    
    
    
    
    
    
}


