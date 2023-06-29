package net.crytec.RegionGUI.utils.flags;

import java.util.Set;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
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
import ru.komiss77.ApiOstrov;
import ru.komiss77.Ostrov;
import ru.komiss77.utils.ColorUtils;
import ru.komiss77.utils.ItemBuilder;
import ru.komiss77.utils.PlayerInput;
import ru.komiss77.utils.inventory.ClickableItem;
import ru.komiss77.utils.inventory.InputButton;
import ru.komiss77.utils.inventory.InputButton.InputType;
import ru.komiss77.utils.inventory.InventoryContent;



public class FlagSetting implements Comparable<FlagSetting> {
    
    private final Flag<?> flag;
    private final String id;
    private Material icon;
    private final FlagInputType inputType;
    //private final Permission permission;
    private final String displayname;
    
    public FlagSetting(final String id, final Flag<?> flag, final Material icon, final String displayname) {
        this.icon = Material.LIGHT_GRAY_BANNER;
        this.flag = flag;
        this.id = id;
        this.displayname = displayname;
        this.icon = icon;
        //permission = new Permission("region.flagmenu." + this.id, "Enables the use of the " + id + " flag.", PermissionDefault.TRUE);
        //try {
        //    Bukkit.getPluginManager().addPermission(this.permission);
        //} catch (IllegalArgumentException ex) {}
        
        
        if (flag instanceof StringFlag) {
            inputType = FlagInputType.STRING;
        } else if (flag instanceof StateFlag) {
            inputType = FlagInputType.STATE;
        } else if (flag instanceof SetFlag) { // HashSet
            inputType = FlagInputType.SET;
        } else if (flag instanceof IntegerFlag ) {
            inputType = FlagInputType.INTEGER;
        } else if (flag instanceof DoubleFlag ) {
            inputType = FlagInputType.DOUBLE;
        } else if (flag instanceof BooleanFlag) {
            inputType = FlagInputType.BOOLEAN;
        } else {
            inputType = FlagInputType.OTHER;
        }
        //RegistryFlag LocationFlag NumberFlag
    }
    
    
    
    
    
    public ClickableItem getButton (final Player player, final ProtectedRegion region, final InventoryContent contents) {
        
        //String menuEntryname = "";
        final boolean hasPerm = (player.hasPermission("regiongui.flag."+id) || player.hasPermission("regiongui.flag.all")) && !player.hasPermission("-regiongui.flag."+id);
        
        ItemStack menuEntry = new ItemBuilder(icon)
            .name("§7" + displayname)
            .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
            .setItemFlag(ItemFlag.HIDE_ENCHANTS)
            .addLore("")
            .addLore(getCurrentValue(region))
            .addLore("")
            .addLore(hasPerm ? "§6ЛКМ§7-изменить состояние" : "§cнет права менять")
            .addLore(hasPerm && region.getFlags().containsKey(flag) ? "§6ПКМ§7-сброс (сделать по умолчанию)": "")
            .build();
        
        if (region.getFlags().containsKey(flag)) {
            //if (null == inputType) {
          //      if (ApiOstrov.canChangeColor(menuEntry.getType())) menuEntry = ColorUtils.changeColor(menuEntry, DyeColor.MAGENTA);
         //   } else //name.enchantment(Enchantment.ARROW_INFINITE);
            switch (inputType) {
                
                case STATE:
                    if ( region.getFlag(flag) == StateFlag.State.DENY) {
                        if (ApiOstrov.canChangeColor(menuEntry.getType())) menuEntry = ColorUtils.changeColor(menuEntry, DyeColor.PINK);
                    } else {
                        if (ApiOstrov.canChangeColor(menuEntry.getType())) menuEntry = ColorUtils.changeColor(menuEntry, DyeColor.LIME);
                    }
                    break;
                    
                case BOOLEAN:
                    if ( (boolean)region.getFlag(flag) ) {
                        if (ApiOstrov.canChangeColor(menuEntry.getType())) menuEntry = ColorUtils.changeColor(menuEntry, DyeColor.PINK);
                    } else {
                        if (ApiOstrov.canChangeColor(menuEntry.getType())) menuEntry = ColorUtils.changeColor(menuEntry, DyeColor.LIME);
                    }
                    break;
                    
                default:
                    if (ApiOstrov.canChangeColor(menuEntry.getType())) menuEntry = ColorUtils.changeColor(menuEntry, DyeColor.BLUE);
                    break;
            }
            
        } else {
            
            if (ApiOstrov.canChangeColor(menuEntry.getType())) menuEntry = ColorUtils.changeColor(menuEntry, DyeColor.GRAY);
            
        }
        
 
        
        
        if (hasPerm) {
            
            return ClickableItem.of(menuEntry, e -> {

                if (e.getClick() == ClickType.RIGHT && region.getFlags().containsKey(flag)) {

                    region.setFlag(flag, null);
                    contents.getHost().getProvider().reopen(player, contents);

                } else {
                    switch (inputType) {
                        case DOUBLE, INTEGER, STRING:
                            player.closeInventory();
                        	new InputButton(InputType.ANVILL, new ItemStack(Material.STONE), "Flag", f -> {
                                setFlag(player, region, this.flag, f);
                                Bukkit.getScheduler().runTaskLater(RegionGUI.getInstance(), () -> contents.getHost().getProvider().reopen(player, contents), 1L);
                                //Ostrov.sync(() -> contents.getHost().getProvider().reopen(player, contents), 1);
                        	//}).run(new InventoryClickEvent(player.getOpenInventory(), SlotType.CONTAINER, 0, ClickType.LEFT, InventoryAction.PICKUP_ALL)
                                });
                            break;

                        case SET:
                            player.sendMessage("§fНаберите в чате новое значение для флага и нажмите Enter");
                            PlayerInput.get(InputButton.InputType.CHAT, player, value -> {
                                setFlag(player, region, this.flag, (String)value);
                                Bukkit.getScheduler().runTaskLater((Plugin)RegionGUI.getInstance(), () -> contents.getHost().getProvider().reopen(player, contents), 1L);
                            }, "");
                            break;
					case BOOLEAN, STATE:
	                    switchState(region);
	                    contents.getHost().getProvider().reopen(player, contents);
						break;
					default:
						break;

                    }
                }
            });
        
        } else {
            
            return ClickableItem.empty(menuEntry);
            
        }
    }
    
    
    
    
    
    
    
    
    private void switchState(final ProtectedRegion region) {
        if (inputType == FlagInputType.STATE) {
            
            final StateFlag stateFlag = (StateFlag) flag;
            if (region.getFlags().containsKey(flag)) {
                if (region.getFlag(stateFlag) == StateFlag.State.DENY) {
                    region.setFlag(stateFlag, StateFlag.State.ALLOW);
                    //player.sendMessage(Language.FLAG_ALLOWED.toString().replace("%flag%", this.getName()));
                } else {
                    region.setFlag(stateFlag, StateFlag.State.DENY);
                    //player.sendMessage(Language.FLAG_DENIED.toString().replace("%flag%", this.getName()));
                }
            } else {
                region.setFlag(stateFlag, StateFlag.State.ALLOW);
                //player.sendMessage(Language.FLAG_ALLOWED.toString().replace("%flag%", this.getName()));
            }
            
        } else {
            final BooleanFlag booleanFlag = (BooleanFlag) flag;
            if (region.getFlags().containsKey(flag)) {
                if (!(boolean)region.getFlag(booleanFlag)) {
                    region.setFlag(booleanFlag, true);
                    //player.sendMessage(Language.FLAG_ALLOWED.toString().replace("%flag%", this.getName()));
                } else {
                    region.setFlag(booleanFlag, false);
                    //player.sendMessage(Language.FLAG_DENIED.toString().replace("%flag%", this.getName()));
                }
            } else {
                region.setFlag(booleanFlag, true);
                //player.sendMessage(Language.FLAG_ALLOWED.toString().replace("%flag%", this.getName()));
            }
        }
    }

    
    
    
    
    
    
    
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public String getCurrentValue(final ProtectedRegion region) {
        if (!region.getFlags().containsKey(flag)) {
            return "§8Неактивен";
        }
//System.out.println( "getCurrentValue flag="+getFlag().getName()+" inputType="+inputType  );
        switch (inputType) {
            case BOOLEAN: 
                return (boolean)region.getFlag(flag) ? "§2Да":"§4Нет";

            case STATE: 
                return region.getFlag(flag) == StateFlag.State.ALLOW ? "§2Да":"§4Нет";
            
            case DOUBLE: 
                //return F.name(new StringBuilder().append((double)region.getFlag((Flag)flag)).toString());
                return "" + (double)region.getFlag((DoubleFlag)flag);
            
            case INTEGER: 
                //return F.name(new StringBuilder().append((int)region.getFlag((Flag)flag)).toString());
                return "" + (int)region.getFlag((IntegerFlag)flag);

            case STRING: 
                // return F.name(((String)region.getFlag((Flag)flag)).toString());
                return (String)region.getFlag(flag);
            
            case SET: 
                //return F.format((Iterable)region.getFlag(flag), ",", "none");
                SetFlag<?> var2 = (SetFlag<?>)flag;
                return (String) ((Set) region.getFlag(var2)).stream().collect(Collectors.joining(",", "[", "]"));
            
            case OTHER:
            default:
                //return "§8Неопределён";
                return region.getFlag(flag).toString();
            
        }
    }
    
    
    
    public String suggestValue(final ProtectedRegion region) {
        
        final boolean isPresent = region.getFlags().containsKey(flag);
        //if (!region.getFlags().containsKey(flag)) {
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
    
    //protected static <V> void setFlag(final ProtectedRegion region, final Flag<V> flag, final Actor sender, final String value) throws InvalidFlagFormat {
    protected static <V> void setFlag(final Player player, final ProtectedRegion region, final Flag<V> flag, final String value) {
        try {
            region.setFlag(flag, flag.parseInput(FlagContext.create().setSender(BukkitAdapter.adapt(player)).setInput(value).setObject("region", region).build()));
        } catch (InvalidFlagFormat ex) {
            player.sendMessage("§cНедопустимое значение : "+ex.getMessage());
        }
    }
    
    public String getName() {
        return this.displayname;
    }
    
    @Override
    public int compareTo(final FlagSetting other) {
        return this.getId().compareTo(other.getId());
    }
    
    //public Permission getPermission() {
    //    return this.permission;
    //}


}
