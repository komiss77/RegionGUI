package net.crytec.RegionGUI.menus;

import com.sk89q.worldguard.domains.PlayerDomain;
import com.sk89q.worldguard.domains.DefaultDomain;
import net.crytec.phoenix.api.inventory.content.Pagination;
import net.crytec.phoenix.api.inventory.content.SlotIterator;
import net.crytec.phoenix.api.PhoenixAPI;
import net.crytec.phoenix.api.inventory.content.SlotPos;
import net.crytec.phoenix.api.utils.UtilPlayer;
import org.bukkit.Sound;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import net.crytec.RegionGUI.Language;
import java.util.ArrayList;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.crytec.phoenix.api.inventory.ClickableItem;
import net.crytec.phoenix.api.inventory.content.InventoryContents;
import org.bukkit.entity.Player;
import net.crytec.phoenix.api.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import net.crytec.phoenix.api.inventory.content.InventoryProvider;



public class RegionAddMembers implements InventoryProvider
{
    private static final ItemStack fill;
    private final ProtectedRegion region;
    
    static {
        fill = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name(" ").build();
    }
    
    public RegionAddMembers(final ProtectedRegion region) {
        this.region = region;
    }
    
    @Override
    public void init(final Player player, final InventoryContents contents) {
        player.playSound(player.getLocation(), Sound.BLOCK_COMPARATOR_CLICK, 5, 5);
        contents.fillBorders(ClickableItem.empty(RegionAddMembers.fill));
        final Pagination pagination = contents.pagination();
        
        
        
        
        final ArrayList<ClickableItem> menuEntry = new ArrayList<>();
        
        
        
        
            DefaultDomain domain = region.getMembers();
            PlayerDomain playerDomain = domain.getPlayerDomain();
            
            for (Player v : player.getWorld().getPlayers()) {
                
                if ( !player.getName().equals(v.getName()) && !playerDomain.contains(v.getName()) && v.getLocation().distance( player.getLocation() ) < 10) {
                    
                    final ItemStack head = new ItemBuilder(Material.PLAYER_HEAD)
                            .name("§f" + v.getName())
                            .lore("§7ЛКМ - добавить пользователем")
                            .build();
                    final SkullMeta skullmeta = (SkullMeta)head.getItemMeta();
                    skullmeta.hasOwner();
                    skullmeta.setOwner(v.getName());
                    head.setItemMeta((ItemMeta)skullmeta);

                    menuEntry.add(ClickableItem.of(head, p6 -> {

                        playerDomain.addPlayer(v.getName());
                        domain.setPlayerDomain(playerDomain);
                        region.setMembers(domain);
                        region.setDirty(true);
                        
                        contents.inventory().open(player, pagination.getPage(), new String[] { "region" }, new Object[] { this.region });
                        UtilPlayer.playSound(player, Sound.BLOCK_LEVER_CLICK);
                        //player.sendMessage(Language.INTERFACE_REMOVE_SUCESSFULL.toChatString().replaceAll("%name%", name));

                    }));            
                    
                }

            }


        
        
        
        pagination.setItems((ClickableItem[])menuEntry.toArray(new ClickableItem[menuEntry.size()]));
        pagination.setItemsPerPage(18);
        
        
        
        
        

        
        
        

        //добавление через чат
        contents.set(SlotPos.of(0, 4), ClickableItem.of(new ItemBuilder(Material.FEATHER).name("§7Добавить через чат").build(), p3 -> {
            player.playSound(player.getLocation(), Sound.BLOCK_COMPARATOR_CLICK, 5, 5);
            
            player.closeInventory();
            player.sendMessage(Language.REGION_MESSAGE_CHATADDMEMBER.toChatString());
            PhoenixAPI.get().getPlayerChatInput(player, addName -> {
                
              //  final OfflinePlayer offlinePlayer2 = Bukkit.getOfflinePlayer(s);
              //  if (!offlinePlayer2.hasPlayedBefore()) {
               //     player.sendMessage(Language.ERROR_INVALID_OFFLINEPLAYER.toChatString());
               // }
               // else {
                   // RegionAddMemberEvent regionAddMemberEvent = new RegionAddMemberEvent(player, this.claim.getTemplate(), offlinePlayer2.getUniqueId());
                   // Bukkit.getPluginManager().callEvent((Event)regionAddMemberEvent);
                   // if (regionAddMemberEvent.isCancelled()) {
                      //  this.reOpen(player, contents);
                   // }
                    //else {
                        
                        //DefaultDomain defaultDomain = this.region.getMembers();
                        //PlayerDomain playerDomain = defaultDomain.getPlayerDomain();
                        playerDomain.addPlayer(addName);
                        domain.setPlayerDomain(playerDomain);
                        region.setMembers(domain);
                        region.setDirty(true);
                        //Profile profile = new Profile(offlinePlayer2.getUniqueId(), offlinePlayer2.getName());
                       // WorldGuard.getInstance().getProfileCache().put(profile);
                        //defaultDomain.toUserFriendlyComponent(WorldGuard.getInstance().getProfileCache());
                        this.reOpen(player, contents);
                        
                        
                        
                        /*
                        //protectedRegion.getMembers();
                        protectedRegion.getMembers().getPlayerDomain().addPlayer(offlinePlayer2.getUniqueId());
                        protectedRegion.getMembers().setPlayerDomain(playerDomain);
                        protectedRegion.setMembers(members);
                        protectedRegion.setDirty(true);
                        WorldGuard.getInstance().getProfileCache().put(new Profile(offlinePlayer2.getUniqueId(), offlinePlayer2.getName()));
                        members.toUserFriendlyComponent(WorldGuard.getInstance().getProfileCache());
                        this.reOpen(player, contents);*/
                   // }
               // }
            });
            //return;
        }));
        
        
        
        
        
        contents.set( 4, 4, ClickableItem.of( new ItemBuilder(Material.OAK_DOOR).name(Language.INTERFACE_BACK.toString()).build(), p1 
                -> MenuOpener.openMain(player, region) ));
        
        contents.set( 4, 6, ClickableItem.of( new ItemBuilder(Material.MAP).name(Language.INTERFACE_NEXT_PAGE.toString()).build(), p4 
                -> contents.inventory().open(player, pagination.next().getPage(), new String[] { "region" }, new Object[] { this.region })));
        
        contents.set( 4, 2, ClickableItem.of( new ItemBuilder(Material.MAP).name(Language.INTERFACE_PREVIOUS_PAGE.toString()).build(), p4 
                -> contents.inventory().open(player, pagination.previous().getPage(), new String[] { "region" }, new Object[] { this.region })));
        
        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1).allowOverride(false));
        
     /*   
        final Iterator iterator = this.region.getMembers().getUniqueIds().iterator();
        
        
        while (iterator.hasNext()) {
            final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer((UUID)iterator.next());
            final String s2 = offlinePlayer.hasPlayedBefore() ? offlinePlayer.getName() : "Unknown Name";
            final ItemStack build = new ItemBuilder(Material.PLAYER_HEAD).name("§f" + s2).lore(Language.INTERFACE_REMOVE_DESC.toString().replaceAll("%name%", s2)).build();
            
            if (offlinePlayer.hasPlayedBefore()) {
                final ItemStack itemStack = build;
                final SkullMeta itemMeta = (SkullMeta)itemStack.getItemMeta();
                itemMeta.hasOwner();
                itemMeta.setOwningPlayer(offlinePlayer);
                itemStack.setItemMeta((ItemMeta)itemMeta);
            }
            //final UUID target;
            //final RegionRemoveMemberEvent regionRemoveMemberEvent;
            //final ProtectedRegion protectedRegion2;
            //final Pagination pagination2;
            //final String replacement;
            
            list.add(ClickableItem.of(build, p6 -> {
              //  RegionRemoveMemberEvent regionRemoveMemberEvent = new RegionRemoveMemberEvent(player, this.claim.getTemplate(), offlinePlayer.getUniqueId());
              //  Bukkit.getPluginManager().callEvent((Event)regionRemoveMemberEvent);
                
               // if (regionRemoveMemberEvent.isCancelled()) {
                //    this.reOpen(player, contents);
                    //contents.inventory().open(player, new String[] { "region" }, new Object[] { protectedRegion2 });
              //      return;
              //  }
               // else {
                    
                    protectedRegion.getMembers().removePlayer(offlinePlayer.getName());
                    contents.inventory().open(player, pagination.getPage(), new String[] { "region" }, new Object[] { protectedRegion });
                    UtilPlayer.playSound(player, Sound.BLOCK_LEVER_CLICK);
                    player.sendMessage(Language.INTERFACE_REMOVE_SUCESSFULL.toChatString().replaceAll("%name%", offlinePlayer.getName()));
                    return;
              //  }
            }));
        }*/
        
        

    }
    
    
    
    
    
    
    
    
    
    
}
