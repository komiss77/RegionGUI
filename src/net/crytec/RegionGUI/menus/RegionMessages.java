package net.crytec.RegionGUI.menus;

import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.crytec.RegionGUI.Language;
import net.crytec.phoenix.api.implementation.AnvilGUI;
import net.crytec.phoenix.api.inventory.ClickableItem;
import net.crytec.phoenix.api.inventory.content.InventoryContents;
import net.crytec.phoenix.api.inventory.content.InventoryProvider;
import net.crytec.phoenix.api.inventory.content.SlotPos;
import net.crytec.phoenix.api.item.ItemBuilder;
import net.crytec.phoenix.api.utils.UtilPlayer;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;



public class RegionMessages implements InventoryProvider
{
    private final ProtectedRegion region;
    
    public RegionMessages(final ProtectedRegion region) {
        this.region = region;
    }
    
    
    
    
    
    @Override
    public void init(final Player player, InventoryContents contents) {
        player.playSound(player.getLocation(), Sound.BLOCK_COMPARATOR_CLICK, 5, 5);
        
        contents.fillBorders(ClickableItem.empty(RegionMain.fill));
        
        
        
        
        contents.set(SlotPos.of(1, 2), ClickableItem.of( new ItemBuilder(Material.OAK_SIGN)
                .name("§eПриветствие в чате")
                .lore("§7Сейчас:")
                .lore(region.getFlags().containsKey(Flags.GREET_MESSAGE) ? region.getFlag(Flags.GREET_MESSAGE) : "§8не установлено" )
                .lore("")
                .lore("§6ЛКМ §7- изменить")
                .lore("§6ПКМ §7- удалить")
                .build(), inventoryClickEvent -> {
                    
                    UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK, 0.5f, 1.0f);
                    
                    if (inventoryClickEvent.getClick() == ClickType.LEFT) {

                        new AnvilGUI( player, "Вы вошли в приват "+player.getName(), (player2, welcome) -> {

                            if(welcome.length()>40 ) {
                                player.sendMessage("§cНе больше 40 символов!");
                                return null;
                            } else {
                                region.setFlag(Flags.GREET_MESSAGE, welcome);
                                this.reOpen(player, contents);
                                return null;
                            }

                        });

                    }  else if (inventoryClickEvent.getClick() == ClickType.RIGHT && region.getFlags().containsKey(Flags.GREET_MESSAGE)) {

                        region.setFlag(Flags.GREET_MESSAGE, null);
                        this.reOpen(player, contents);
                    }
        }));
        
        
        //.name("§eТитры при входе")  "Здравствуйте!"
        contents.set(SlotPos.of(1, 3), ClickableItem.of( new ItemBuilder(Material.OAK_SIGN)
                .name("§eТитры при входе")
                .lore("§7Сейчас:")
                .lore(region.getFlags().containsKey(Flags.GREET_TITLE) ? region.getFlag(Flags.GREET_TITLE) : "§8не установлено" )
                .lore("")
                .lore("§6ЛКМ §7- изменить")
                .lore("§6ПКМ §7- удалить")
                .build(), inventoryClickEvent -> {
                    
                    UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK, 0.5f, 1.0f);
                    
                    if (inventoryClickEvent.getClick() == ClickType.LEFT) {

                        new AnvilGUI( player, "Здравствуйте!", (player2, welcome) -> {

                            if(welcome.length()>40 ) {
                                player.sendMessage("§cНе больше 40 символов!");
                                return null;
                            } else {
                                region.setFlag(Flags.GREET_TITLE, welcome);
                                this.reOpen(player, contents);
                                return null;
                            }

                        });

                    }  else if (inventoryClickEvent.getClick() == ClickType.RIGHT && region.getFlags().containsKey(Flags.GREET_TITLE)) {

                        region.setFlag(Flags.GREET_TITLE, null);
                        this.reOpen(player, contents);
                        
                    }                
        }));

        
        

        
        //.name("§eПрощание в чате")    "Вы покинули приват "+player.getName()
        contents.set(SlotPos.of(1, 5), ClickableItem.of( new ItemBuilder(Material.OAK_SIGN)
                .name("§eПрощание в чате")
                .lore("§7Сейчас:")
                .lore(region.getFlags().containsKey(Flags.FAREWELL_MESSAGE) ? region.getFlag(Flags.FAREWELL_MESSAGE) : "§8не установлено" )
                .lore("")
                .lore("§6ЛКМ §7- изменить")
                .lore("§6ПКМ §7- удалить")
                .build(), inventoryClickEvent -> {
                    
                    UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK, 0.5f, 1.0f);
                    
                    if (inventoryClickEvent.getClick() == ClickType.LEFT) {

                        new AnvilGUI( player, "Вы покинули приват "+player.getName(), (player2, welcome) -> {

                            if(welcome.length()>40 ) {
                                player.sendMessage("§cНе больше 40 символов!");
                                return null;
                            } else {
                                region.setFlag(Flags.FAREWELL_MESSAGE, welcome);
                                this.reOpen(player, contents);
                                return null;
                            }

                        });

                    }  else if (inventoryClickEvent.getClick() == ClickType.RIGHT && region.getFlags().containsKey(Flags.FAREWELL_MESSAGE)) {

                        region.setFlag(Flags.FAREWELL_MESSAGE, null);
                        this.reOpen(player, contents);
                        
                    }                
        }));
        
        
        //.name("§eТитры при выходе")   "До свидания!"
        contents.set(SlotPos.of(1, 6), ClickableItem.of( new ItemBuilder(Material.OAK_SIGN)
                .name("§eТитры при выходе")
                .lore("§7Сейчас:")
                .lore(region.getFlags().containsKey(Flags.FAREWELL_TITLE) ? region.getFlag(Flags.FAREWELL_TITLE) : "§8не установлено" )
                .lore("")
                .lore("§6ЛКМ §7- изменить")
                .lore("§6ПКМ §7- удалить")
                .build(), inventoryClickEvent -> {
                    
                    UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK, 0.5f, 1.0f);
                    
                    if (inventoryClickEvent.getClick() == ClickType.LEFT) {

                        new AnvilGUI( player, "До свидания!", (player2, welcome) -> {

                            if(welcome.length()>40 ) {
                                player.sendMessage("§cНе больше 40 символов!");
                                return null;
                            } else {
                                region.setFlag(Flags.FAREWELL_TITLE, welcome);
                                this.reOpen(player, contents);
                                return null;
                            }

                        });

                    }  else if (inventoryClickEvent.getClick() == ClickType.RIGHT && region.getFlags().containsKey(Flags.FAREWELL_TITLE)) {

                        region.setFlag(Flags.FAREWELL_TITLE, null);
                        this.reOpen(player, contents);
                        
                    }                
        }));

        
        
        
        
        
        
        
        contents.set( 2, 4, ClickableItem.of( new ItemBuilder(Material.OAK_DOOR).name(Language.INTERFACE_BACK.toString()).build(), p1 
                -> MenuOpener.openMain(player, region) ));
        
        
        
    
    }

    
    
}

