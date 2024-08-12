package net.crytec.RegionGUI;

import java.util.stream.Collectors;
import org.bukkit.ChatColor;
import java.util.Arrays;
import org.bukkit.configuration.file.YamlConfiguration;
import java.util.List;

public enum Language
{
    TITLE("title-name", "&2[&fRegionGUI&2]"), 
    ERROR_WORLD_DISABLED("error.WorldDisabled", "&cSorry, but you can't claim a region in this world."), 
    ERROR_NO_PERMISSION("error.noPermission", "&cSorry, but you don't have the required permission to do this."), 
    ERROR_ALREADYMEMBER("error.alreadyMember", "&6%name% &7is already a member of your region."), 
    ERROR_NOT_MEMBER("error.notMember", "&6%name% &7is not a member of this region. &cCould not remove!"), 
    ERROR_UUID_RESOLVE("error.uuidResolve", "&cThe given nickname could not be resolved to a UUID. Did you enter the nickname correctly?"), 
    ERROR_INVALID_OFFLINEPLAYER("error.offlineplayer", "&cThe entered player has never played on this server before."), 
    ERROR_CANCELED("error.cancel", "&cAction cancelled!"), 
    ERROR_NO_REGION_FOUND("error.noRegionFound", "&cSorry, but there is no region on your current position."), 
    ERROR_NOT_OWNER("error.notOwner", "&cSorry, but you do not own this region."), 
    ERROR_NO_MONEY("error.notEnoughMoney", "&cYou don't have enough money to purchase this."), 
    ERROR_OVERLAP("error.overlap", "&cYou would overlap one or more regions you don't own!"), 
    ERROR_MAX_REGIONS("error.max_regions", "&cYou have reached the maximum amount of regions for this world."), 
    ERROR_BORDERLIMIT("error.borderLimit", "&cSorry, only one region border can be displayed at the same time!"), 
    ERROR_VERSION("error.invalidVersion", "&cThe plugin does not support this server version! Cannot display particles!"), 
    ERROR_NO_REGIONS("error.noRegions", "&cYou do not own any regions in this world."), 
    ERROR_NO_REGIONS_OTHER("error.noRegions", "&cThis player does not own any regions in the current world."), 
    ERROR_INVALID_NAME("error.invalidName", "&cThe entered region name contains invalid characters!"), 
    ERROR_REGIONNAME_ALREADY_EXISTS("error.alreadyexists", "&cThere is already a region with this name."), 
    ERROR_NO_HOME_SET("error.no-home", "&cNo teleport location found for the selected region. Please set a home position in the manage interface."), 
    CHAT_ENTER_REGIONNAME("chat.enterRegionName", "&7Please enter a name of your region"), 
    COMMAND_LIST_OWN("command.list.own", "&7Regions in world &6%world%&7:"), 
    COMMAND_LIST_OTHERS("command.list.others", "&7Regions of player &6%player%&7 in world &6%world%&7:"), 
    COMMAND_LIST_ENTRY("command.list.entry", "&7ID:&6 %region% &7 - Template: &6 %template%"), 
    MANAGE_MEMBERADDED("manage.memberAdded", "&6%name%&7 is now a member of your region."), 
    MANAGE_MEMBERREMOVED("manage.memberRemoved", "&6%name%&7 has been removed from your region."), 
    INTERFACE_NEXT_PAGE("interface.general.nextpage", "&fNext Page"), 
    INTERFACE_PREVIOUS_PAGE("interface.general.previous", "&fPrevious Page"), 
    INTERFACE_BACK("interface.general.back", "&fBack"), 
    INTERFACE_SELECT_TITLE("interface.select.title", "Select your Region"), 
    INTERFACE_SELECT_DESCRIPTION("interface.select.description", "&7Left click to edit region &a%region%"), 
    INTERFACE_MANAGE_TITLE("interface.manage.title", "&lManage your region"), 
    INTERFACE_MANAGE_MEMBERS("interface.manage.members.name", "&6>> &7Current Members:"), 
    INTERFACE_MANAGE_BUTTON_ADDMEMBER("interface.manage.button_addMember.name", "&7Click to &aadd&7 a player."), 
    INTERFACE_MANAGE_BUTTON_INFO("interface.manage.button_info", "&6>> &7Info"), 
    INTERFACE_MANAGE_BUTTON_INFO_DESCRIPTION("interface.manage.info.description", Arrays.asList("&6>>&7This is the default configuration", "&6>>&7 You can define a custom text which", "&6>>&7is shown in this menu :)")), 
    INTERFACE_MANAGE_BUTTON_MANAGE_MEMBERS("interface.manage.button.managemembers", "§7Click to manage region members."), 
    INTERFACE_MANAGE_BUTTON_DELETEREGION("interface.manage.button_deleteRegion.name", "&7Delete your region"), 
    INTERFACE_MANAGE_BUTTON_DELETEREGION_DESCRIPTION("interface.manage.button_deleteRegion.description", Arrays.asList("&7This will delete your region", "&7there are &cno&7 refunds.")), 
    INTERFACE_MANAGE_BUTTON_SHOWBORDER("interface.manage.button_showBorder.name", "&7Click to show the border."), 
    INTERFACE_MANAGE_BUTTON_SHOWBORDER_DESCRIPTION("interface.manage.button_showBorder.description", Arrays.asList("&7This enables a particle border", "&7for a limited time.")), 
    INTERFACE_MANAGE_BUTTON_FLAG("interface.manage.button_flag.name", "&7Click to open the Flag menu"), 
    INTERFACE_MANAGE_BUTTON_WALK_FLAGS("interface.manage.walk-flags.name", "&7Click to change the greeting/farewell message."), 
    INTERFACE_MANAGE_BUTTON_WALK_FLAGS_DESCRIPTION("interface.manage.walk-flags.description", Arrays.asList("&aLeft click &7to change the welcome message", "&aRight-click &7to change the farewell message.")), 
    INTERFACE_REMOVE_TITLE("interface.remove.title", "Current Members"), 
    INTERFACE_REMOVE_DESC("interface.remove.desc", "&7Click to remove %name%"), 
    INTERFACE_REMOVE_SUCESSFULL("interface.remove.successfull", "&6%name% &7has been removed from your region."), 
    INTERFACE_WELCOME_TITLE("interface.welcome.title", "Change welcome message"), 
    INTERFACE_WELCOME_CHAT("interface.welcome.chat", "&7Change the &echat message&7 upon entering the region."), 
    INTERFACE_WELCOME_TITLEMESSAGE("interface.welcome.titlemessage", "&7Change the &etitle message&7 upon entering the region."), 
    INTERFACE_WELCOME_ACTIONBAR("interface.welcome.actionbar", "&7Change the &eaction bar message&7 upon entering the region."), 
    INTERFACE_WELCOME_BUTTON_DESCRIPTION("interface.welcome.button.description", Arrays.asList("&aLeft click &7to change the message", "&aRight-click &7to delete the current message.")), 
    INTERFACE_FAREWELL_TITLE("interface.farewell.title", "Change farewell message"), 
    INTERFACE_FAREWELL_CHAT("interface.farewell.chat", "&7Change the &echat message&7 upon leaving the region."), 
    INTERFACE_FAREWELL_TITLEMESSAGE("interface.farewell.titlemessage", "&7Change the &etitle message&7 upon leaving the region."), 
    INTERFACE_FAREWELL_ACTIONBAR("interface.farewell.actionbar", "&7Change the &eaction bar message&7 upon leaving the region."), 
    INTERFACE_FAREWELL_BUTTON_DESCRIPTION("interface.farewell.button.description", Arrays.asList("&aLeft click &7to change the message", "&aRight-click &7to delete the current message.")), 
    INTERFACE_DELETE_TITLE("interface.delete.title", "&c&lConfirm Deletion"), 
    INTERFACE_DELETE_CONFIRM_BUTTON("interface.delete.buttonConfirm", "&aConfirm"), 
    INTERFACE_DELETE_ABORT_BUTTON("interface.delete.buttonAbort", "&cAbort"), 
    INTERFACE_BUY_TITLE("interface.buy.title", "&f&lClaim a new region:"), 
    INTERFACE_HOME_TITLE("interface.home.title", "&9Claimed Land"), 
    INTERFACE_HOME_ENTRYBUTTON_DESCRIPTION("interface.home.entrybutton-description", Arrays.asList("&7World: %world%", "&aLeft click &7to teleport")), 
    INTERFACE_HOME_BUTTON("interface.home.button", "&7Set home point"), 
    INTERFACE_HOME_BUTTON_DESC("interface.home.buttondescription", Arrays.asList("&7Click to set the home teleport", "&7point at your current location")), 
    INTERFACE_HOME_BUTTON_SUCCESS("interface.home.success", "&2The home teleport location has been set at your current position."), 
    REGION_DELETE_ABORT("region.messageDeleteAbort", "§cRegion Deletion aborted!"), 
    REGION_MESSAGE_CHATADDMEMBER("region.messageChatAddMember", "&7Please type the username you wish to add:"), 
    REGION_REMOVED("region.removed", "&cYour region has been removed."), 
    REGION_REMOVED_REFUNDED("region.refunded", "&7You have been refunded &a%refund%"), 
    REGION_CREATE_SUCCESS("region.create", "&aA new Region has been created for you."), 
    REGION_CREATE_MONEY("region.createMoney", "&aYou've paid %money% %currencyname%."), 
    REGION_ADMIN_RELOAD("region.reload", "&aConfiguration has been reloaded."), 
    REGION_DISPLAYSTART("displaytask.start", "&7Showing border for &6%cooldown%&7 seconds."), 
    REGION_DISPLAYSTOP("displaytask.stop", "&7The region border is no longer visible."), 
    REGION_MESSAGE_INPUT("region.messageInput", "&7Please type the new message you wish to set into the chat:"), 
    REGION_WELCOME_REMOVED("region.welcomeRemoved", "&cWelcome message has been disabled."), 
    REGION_WELCOME_CHANGED("region.welcomeMessageChanged", "&7Welcome message has been changed to:\n%message%"), 
    REGION_FAREWELL_REMOVED("region.farewellRemoved", "&cFarewell message has been disabled."), 
    REGION_FAREWELL_CHANGED("region.farewellMessageChanged", "&7Farewell message has been changed to:\n%message%"), 
    REGION_LIST("region.list", "&6Your Regions in this world:"), 
    REGION_LIST_OTHERS("region.list.others", "&6%target%'s regions in this world:"), 
    PREVIEW_CANCEL("region.preview.cancel", "&2Sneak to cancel border display"), 
    FLAG_TITLE("flag.menu_title", "§lFlag Menu"), 
    FLAG_EMPTY_MESSAGE("flag.message.empty", "§cSorry, but you currently cannot set any flags!"), 
    FLAG_CLEARED("flag.menu.cleared", "§7Flag (&6%flag%&7) has been cleared."), 
    FLAG_ALLOWED("flag.menu.allowed", "§7Flag (&6%flag%&7) is now &2allowed!"), 
    FLAG_DENIED("flag.menu.denied", "§7Flag (&6%flag%&7) is now &4forbidden"), 
    FLAG_INPUT_CHAT("flag.message.chatinput", "&7Please enter the new value for flag %flag%:");
    
    private String path;
    private String def;
    private boolean isArray;
    private List<String> defArray;
    private static YamlConfiguration LANG;
    
    private Language(final String path, final String start) {
        this.isArray = false;
        this.path = path;
        this.def = start;
    }
    
    private Language(final String path, final List<String> start) {
        this.isArray = false;
        this.path = path;
        this.defArray = start;
        this.isArray = true;
    }
    
    public static void setFile(final YamlConfiguration config) {
        Language.LANG = config;
    }
    
    public static YamlConfiguration getFile() {
        return Language.LANG;
    }
    
    @Override
    public String toString() {
        if (this == Language.TITLE) {
            return String.valueOf(TCUtil.translateAlternateColorCodes('&', Language.LANG.getString(this.path, this.def))) + " ";
        }
        return TCUtil.translateAlternateColorCodes('&', Language.LANG.getString(this.path, this.def));
    }
    
    public String toChatString() {
        return String.valueOf(Language.TITLE.toString()) + TCUtil.translateAlternateColorCodes('&', Language.LANG.getString(this.path, this.def));
    }
    
    public List<String> getDescriptionArray() {
        return (List<String>)Language.LANG.getStringList(this.path).stream().map(s -> TCUtil.translateAlternateColorCodes('&', s)).collect(Collectors.toList());
    }
    
    public boolean isArray() {
        return this.isArray;
    }
    
    public List<String> getDefArray() {
        return this.defArray;
    }
    
    public String getDefault() {
        return this.def;
    }
    
    public String getPath() {
        return this.path;
    }
}
