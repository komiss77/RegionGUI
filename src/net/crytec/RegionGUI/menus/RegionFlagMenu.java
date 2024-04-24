package net.crytec.RegionGUI.menus;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.LinkedList;
import net.crytec.RegionGUI.RegionGUI;
import net.crytec.RegionGUI.utils.flags.FlagManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.komiss77.utils.ItemBuilder;
import ru.komiss77.utils.ItemUtils;
import ru.komiss77.utils.inventory.ClickableItem;
import ru.komiss77.utils.inventory.InventoryContent;
import ru.komiss77.utils.inventory.InventoryProvider;
import ru.komiss77.utils.inventory.Pagination;
import ru.komiss77.utils.inventory.SlotIterator;
import ru.komiss77.utils.inventory.SlotPos;



public class RegionFlagMenu implements InventoryProvider {
    
    private static final ItemStack fill = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name(" ").build();;
    private final FlagManager flagManager;
    private final ProtectedRegion region;
    
    
    public RegionFlagMenu(final ProtectedRegion region) {
        this.region = region;
        this.flagManager = RegionGUI.getInstance().getFlagManager();
    }
        
    
    @Override
       public void init(Player player, InventoryContent inventoryContents) {
           
        //inventoryContents.fillRow(0, ClickableItem.empty(fill));
        inventoryContents.fillRow(4, ClickableItem.empty(fill));
        player.playSound(player.getLocation(), Sound.BLOCK_COMPARATOR_CLICK, 5, 5);
        
        
        //ProtectedRegion protectedRegion = this.claim.getProtectedRegion().get();
        
        LinkedList<ClickableItem> menuEntryList = new LinkedList();
        
        flagManager.getFlagMap().forEach( flagSetting -> {
                        //if (player.hasPermission(flagSetting.getPermission()) || player.hasPermission("region.flagmenu.all")) {
            menuEntryList.add(flagSetting.getButton(player, region, inventoryContents));
            //}
            
        });
        
        
        
        
        
        ClickableItem[] arrclickableItem = new ClickableItem[menuEntryList.size()];
        arrclickableItem = menuEntryList.toArray(arrclickableItem);
        
        SlotIterator slotIterator = inventoryContents.newIterator(SlotIterator.Type.HORIZONTAL, SlotPos.of(0,0));
        slotIterator = slotIterator.allowOverride(false);
        
        Pagination pagination = inventoryContents.pagination();
        pagination.setItems(arrclickableItem);
        pagination.setItemsPerPage(36);
        pagination.addToIterator(slotIterator);
        //pagination.addToIterator(slotIterator);
        
        
        
        
        
        inventoryContents.set(  4, 4, ClickableItem.of(  new ItemBuilder(Material.OAK_DOOR).name("гл.меню").build(), 
                e -> MenuOpener.openMain(player, region) ));
        
        
        if (!pagination.isLast()) {
            inventoryContents.set( 4, 6, ClickableItem.of( ItemUtils.nextPage, 
                    e -> inventoryContents.getHost().open( player, pagination.next().getPage()) )
            );
        }
        
        
        if (!pagination.isFirst()) {
            inventoryContents.set( 4, 2, ClickableItem.of( ItemUtils.previosPage, 
                    e -> inventoryContents.getHost().open( player, pagination.previous().getPage()) )
            );
        }
        
        
        
    }
       

    
   // @Override
   // public void update(final Player player, final InventoryContent contents) {
   // }
}
