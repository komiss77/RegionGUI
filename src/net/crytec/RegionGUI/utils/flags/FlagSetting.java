package net.crytec.RegionGUI.utils.flags;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldguard.protection.flags.BooleanFlag;
import com.sk89q.worldguard.protection.flags.DoubleFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.FlagContext;
import com.sk89q.worldguard.protection.flags.IntegerFlag;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;
import com.sk89q.worldguard.protection.flags.SetFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StringFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.crytec.RegionGUI.RegionGUI;
import net.crytec.phoenix.api.PhoenixAPI;
import net.crytec.phoenix.api.implementation.AnvilGUI;
import net.crytec.phoenix.api.inventory.ClickableItem;
import net.crytec.phoenix.api.inventory.content.InventoryContents;
import net.crytec.phoenix.api.item.ItemBuilder;
import net.crytec.phoenix.api.utils.F;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import ru.komiss77.ApiOstrov;
import ru.komiss77.utils.ColorUtils;



public class FlagSetting implements Comparable<FlagSetting>
{
    private final Flag<?> flag;
    private final String id;
    private Material icon;
    private final FlagInputType inputType;
    private final Permission permission;
    private final String displayname;
    
    public FlagSetting(final String id, final Flag<?> flag, final Material icon, final String displayname) {
        this.icon = Material.LIGHT_GRAY_BANNER;
        this.flag = flag;
        this.id = id;
        this.displayname = displayname;
        this.icon = icon;
        this.permission = new Permission("region.flagmenu." + this.id, "Enables the use of the " + id + " flag.", PermissionDefault.TRUE);
        try {
            Bukkit.getPluginManager().addPermission(this.permission);
        } catch (IllegalArgumentException ex) {}
        
        
        if (flag instanceof StringFlag) {
            this.inputType = FlagInputType.STRING;
        }
        else if (flag instanceof StateFlag) {
            this.inputType = FlagInputType.STATE;
        }
        else if (flag instanceof SetFlag) { // HashSet
            this.inputType = FlagInputType.SET;
        }
        else if (flag instanceof IntegerFlag ) {
            this.inputType = FlagInputType.INTEGER;
        }
        else if (flag instanceof DoubleFlag ) {
            this.inputType = FlagInputType.DOUBLE;
        }
        else if (flag instanceof BooleanFlag) {
            this.inputType = FlagInputType.BOOLEAN;
        }
        else {
            this.inputType = FlagInputType.OTHER;
        }
        //RegistryFlag LocationFlag NumberFlag
    }
    
    
    
    
    
    public ClickableItem getButton(final Player player, final ProtectedRegion region, final InventoryContents contents) {
        
        //String menuEntryname = "";
        
            ItemStack menuEntry = new ItemBuilder(this.icon)
                .name("§7" + this.getName())
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .setItemFlag(ItemFlag.HIDE_ENCHANTS)
                .lore("")
                .lore(this.getCurrentValue(region))
                .lore("")
                .lore("§6ЛКМ§7-изменить состояние")
                .lore("§6ПКМ§7-сброс (сделать по умолчанию)")
                .build();
        
        if (region.getFlags().containsKey(this.getFlag())) {
            //name.enchantment(Enchantment.ARROW_INFINITE);
            if (this.inputType == FlagInputType.STATE) {
                
                if ( region.getFlag(this.getFlag()) == StateFlag.State.DENY) {
                    if (ApiOstrov.canChangeColor(menuEntry.getType())) menuEntry = ColorUtils.changeColor(menuEntry, DyeColor.RED);
                } else {
                    if (ApiOstrov.canChangeColor(menuEntry.getType())) menuEntry = ColorUtils.changeColor(menuEntry, DyeColor.GREEN);
                }
                
                //name.name((region.getFlag(this.getFlag()) == StateFlag.State.DENY) ? ("§c" + this.getName()) : ("§a" + this.getName()));
            } else if (this.inputType == FlagInputType.BOOLEAN) {
                
                if ( region.getFlag((Flag)this.getFlag()) ) {
                    if (ApiOstrov.canChangeColor(menuEntry.getType())) menuEntry = ColorUtils.changeColor(menuEntry, DyeColor.RED);
                } else {
                    if (ApiOstrov.canChangeColor(menuEntry.getType())) menuEntry = ColorUtils.changeColor(menuEntry, DyeColor.GREEN);
                }
                
                //name.name((region.getFlag(this.getFlag()) == StateFlag.State.DENY) ? ("§c" + this.getName()) : ("§a" + this.getName()));
            } else {
                
                if (ApiOstrov.canChangeColor(menuEntry.getType())) menuEntry = ColorUtils.changeColor(menuEntry, DyeColor.MAGENTA);
                
            }
        }
        
 
        
        
        
        return new ClickableItem(menuEntry, inventoryClickEvent -> {
            
            if (inventoryClickEvent.getClick() == ClickType.RIGHT && region.getFlags().containsKey(this.getFlag())) {
                
                region.setFlag((Flag)this.getFlag(), (Object)null);
                contents.inventory().getProvider().reOpen(player, contents);
                
            } else if (this.inputType == FlagInputType.STATE || this.inputType == FlagInputType.BOOLEAN) {
                
                this.switchState(region);
                contents.inventory().getProvider().reOpen(player, contents);
                
            } else {
                
                player.closeInventory();
                
                switch (inputType) {
                    
                    case DOUBLE:
                    case INTEGER:
                    case STRING:
                        AnvilGUI agui = new AnvilGUI( player, this.suggestValue(region), (player2, value) -> {

                            if(!value.isEmpty() ) {
                                try {
                                    setFlag(region, this.flag, (Actor)BukkitAdapter.adapt(player), value);
                                    Bukkit.getScheduler().runTaskLater((Plugin)RegionGUI.getInstance(), () -> contents.inventory().getProvider().reOpen(player, contents), 1L);
                                } catch (InvalidFlagFormat ex) {
                                    player.sendMessage("§cНедопустимое значение : "+ex.getMessage());
                                }
                            }
                            return null;

                        });
                        break;
                        
                    case SET:
                        player.sendMessage("§fНаберите в чате новое значение для флага и нажмите Enter");
                        PhoenixAPI.get().getPlayerChatInput(player, value -> {
                            try {
                                setFlag(region, this.flag, (Actor)BukkitAdapter.adapt(player), value);
                                Bukkit.getScheduler().runTaskLater((Plugin)RegionGUI.getInstance(), () -> contents.inventory().getProvider().reOpen(player, contents), 1L);
                            }
                            catch (InvalidFlagFormat invalidFlagFormat) {
                                player.sendMessage(invalidFlagFormat.getMessage());
                            }
                        });
                        break;

                }

                
                
                
                //player.sendMessage(Language.FLAG_INPUT_CHAT.toChatString().replace("%flag%", this.getName()));
                /*PhoenixAPI.get().getPlayerChatInput(player, value -> {
                    try {
                        setFlag(region, this.flag, (Actor)BukkitAdapter.adapt(player), value);
                        Bukkit.getScheduler().runTaskLater((Plugin)RegionGUI.getInstance(), () -> contents.inventory().getProvider().reOpen(player, contents), 1L);
                    }
                    catch (InvalidFlagFormat invalidFlagFormat) {
                        player.sendMessage(invalidFlagFormat.getMessage());
                    }
                });*/
                
            }
        });
    }
    
    
    
    
    
    
    
    
    private void switchState(final ProtectedRegion region) {
        if (this.inputType == FlagInputType.STATE) {
            final StateFlag stateFlag = (StateFlag)this.getFlag();
            if (region.getFlags().containsKey(this.getFlag())) {
                if (region.getFlag((Flag)stateFlag) == StateFlag.State.DENY) {
                    region.setFlag((Flag)stateFlag, (Object)StateFlag.State.ALLOW);
                    //player.sendMessage(Language.FLAG_ALLOWED.toString().replace("%flag%", this.getName()));
                }
                else {
                    region.setFlag((Flag)stateFlag, (Object)StateFlag.State.DENY);
                    //player.sendMessage(Language.FLAG_DENIED.toString().replace("%flag%", this.getName()));
                }
            }
            else {
                region.setFlag((Flag)stateFlag, (Object)StateFlag.State.ALLOW);
                //player.sendMessage(Language.FLAG_ALLOWED.toString().replace("%flag%", this.getName()));
            }
        }
        else {
            final BooleanFlag booleanFlag = (BooleanFlag)this.getFlag();
            if (region.getFlags().containsKey(this.getFlag())) {
                if (!(boolean)region.getFlag((Flag)booleanFlag)) {
                    region.setFlag((Flag)booleanFlag, (Object)true);
                    //player.sendMessage(Language.FLAG_ALLOWED.toString().replace("%flag%", this.getName()));
                }
                else {
                    region.setFlag((Flag)booleanFlag, (Object)false);
                    //player.sendMessage(Language.FLAG_DENIED.toString().replace("%flag%", this.getName()));
                }
            }
            else {
                region.setFlag((Flag)booleanFlag, (Object)true);
                //player.sendMessage(Language.FLAG_ALLOWED.toString().replace("%flag%", this.getName()));
            }
        }
    }

    
    
    
    
    
    
    
    
    
    public String getCurrentValue(final ProtectedRegion region) {
        if (!region.getFlags().containsKey(this.getFlag())) {
            return "§8Неактивен";
        }
        
//System.out.println( "getCurrentValue flag="+getFlag().getName()+" inputType="+inputType  );

        switch (inputType) {
            case BOOLEAN: 
                return region.getFlag((Flag)this.getFlag()) ? "§2Да":"§4Нет";

            case STATE: 
                return region.getFlag((Flag)this.getFlag()) == StateFlag.State.ALLOW ? "§2Да":"§4Нет";
            
            case DOUBLE: 
                return F.name(new StringBuilder().append((double)region.getFlag((Flag)this.getFlag())).toString());
            
            case INTEGER: 
                return F.name(new StringBuilder().append((int)region.getFlag((Flag)this.getFlag())).toString());

            case STRING: 
                // return F.name(((String)region.getFlag((Flag)this.getFlag())).toString());
                return (String)region.getFlag(this.getFlag());
            
            case SET: 
                return F.format((Iterable)region.getFlag(this.getFlag()), ",", "none");
            
            case OTHER:
            default:
                //return "§8Неопределён";
                return region.getFlag(this.getFlag()).toString();
            
        }
    }
    
    public String suggestValue(final ProtectedRegion region) {
        
        final boolean isPresent = region.getFlags().containsKey(this.getFlag());
        //if (!region.getFlags().containsKey(this.getFlag())) {
        //    return "§8Неактивен";
        //}
        
//System.out.println( "getCurrentValue flag="+getFlag().getName()+" inputType="+inputType  );

        switch (inputType) {
            
            case DOUBLE:
            case INTEGER: 
                if (isPresent) return getCurrentValue(region);
                else return "0";

            case STRING: 
                if (isPresent) return getCurrentValue(region);
                else return "";
            
            case OTHER:
            default:
                return "новое значение";
            
        }
    }
    

    
    
    
    
    
    
    
    public void setIcon(final Material mat) {
        this.icon = mat;
    }
    
    public Material getIcon() {
        return this.icon;
    }
    
    public String getId() {
        return this.id;
    }
    
    public Flag<?> getFlag() {
        return this.flag;
    }
    
    protected static <V> void setFlag(final ProtectedRegion region, final Flag<V> flag, final Actor sender, final String value) throws InvalidFlagFormat {
        region.setFlag((Flag)flag, flag.parseInput(FlagContext.create().setSender(sender).setInput(value).setObject("region", (Object)region).build()));
    }
    
    public String getName() {
        return this.displayname;
    }
    
    @Override
    public int compareTo(final FlagSetting other) {
        return this.getId().compareTo(other.getId());
    }
    
    public Permission getPermission() {
        return this.permission;
    }
}
