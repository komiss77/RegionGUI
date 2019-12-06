package net.crytec.RegionGUI.menus;

import net.crytec.phoenix.api.inventory.content.Pagination;
import net.crytec.RegionGUI.Language;
import net.crytec.phoenix.api.inventory.content.SlotIterator;
import java.util.LinkedList;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.crytec.phoenix.api.inventory.ClickableItem;
import net.crytec.phoenix.api.inventory.content.InventoryContents;
import org.bukkit.entity.Player;
import net.crytec.RegionGUI.RegionGUI;
import net.crytec.phoenix.api.item.ItemBuilder;
import org.bukkit.Material;
import net.crytec.RegionGUI.utils.flags.FlagManager;
import org.bukkit.inventory.ItemStack;
import net.crytec.phoenix.api.inventory.content.InventoryProvider;
import org.bukkit.Sound;



public class RegionFlagMenu implements InventoryProvider
{
    private static final ItemStack fill = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name(" ").build();;
    private final FlagManager flagManager;
    private final ProtectedRegion region;
    

    
    public RegionFlagMenu(final ProtectedRegion region) {
        this.region = region;
        this.flagManager = RegionGUI.getInstance().getFlagManager();
    }
    

    
    
    @Override
       public void init(Player player, InventoryContents inventoryContents) {
           
        inventoryContents.fillRow(0, ClickableItem.empty(fill));
        inventoryContents.fillRow(4, ClickableItem.empty(fill));

        
        player.playSound(player.getLocation(), Sound.BLOCK_COMPARATOR_CLICK, 5, 5);
        
        
        //ProtectedRegion protectedRegion = this.claim.getProtectedRegion().get();
        
        LinkedList<ClickableItem> menuEntryList = new LinkedList();
        
        flagManager.getFlagMap().forEach(flagSetting -> {
            
            if (player.hasPermission(flagSetting.getPermission()) || player.hasPermission("region.flagmenu.all")) {
                menuEntryList.add(flagSetting.getButton(player, region, inventoryContents));
            }
            
        });
        
        
        
        
        
        ClickableItem[] arrclickableItem = new ClickableItem[menuEntryList.size()];
        arrclickableItem = menuEntryList.toArray(arrclickableItem);
        
        SlotIterator slotIterator = inventoryContents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 0);
        slotIterator = slotIterator.allowOverride(false);
        
        Pagination pagination = inventoryContents.pagination();
        pagination.setItems(arrclickableItem);
        pagination.setItemsPerPage(27);
        pagination.addToIterator(slotIterator);
        
        
        inventoryContents.set(  4, 4, ClickableItem.of( new ItemBuilder(Material.OAK_DOOR).name(Language.INTERFACE_BACK.toString()).build(), 
                inventoryClickEvent -> MenuOpener.openMain(player, region) ));
        
        
        if (!pagination.isLast()) {
            inventoryContents.set( 4, 6, ClickableItem.of( new ItemBuilder(Material.MAP).name(Language.INTERFACE_NEXT_PAGE.toString()).build(),
                    inventoryClickEvent -> inventoryContents.inventory().open( player, pagination.next().getPage(), new String[]{"region"}, new Object[]{this.region})));
        }
        
        
        if (!pagination.isFirst()) {
            inventoryContents.set( 4, 2, ClickableItem.of( new ItemBuilder(Material.MAP).name(Language.INTERFACE_PREVIOUS_PAGE.toString()).build(),
                    inventoryClickEvent -> inventoryContents.inventory().open( player, pagination.previous().getPage(), new String[]{"region"}, new Object[]{this.region})));
        }
        
        
        pagination.addToIterator(inventoryContents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 0));
        
    }
       

    
    @Override
    public void update(final Player player, final InventoryContents contents) {
    }
}
