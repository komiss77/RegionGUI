package net.crytec.RegionGUI.chateditor;

import net.crytec.phoenix.api.utils.UtilPlayer;
import org.bukkit.Sound;
import net.crytec.phoenix.api.inventory.content.InventoryProvider;
import net.crytec.RegionGUI.menus.TemplateEditor;
import net.crytec.phoenix.api.inventory.SmartInventory;
import net.crytec.phoenix.api.PhoenixAPI;
import net.crytec.phoenix.api.chat.program.ChatCanvas;
import org.bukkit.entity.Player;
import net.crytec.RegionGUI.data.Template;
import java.util.List;
import java.util.function.Consumer;
import net.crytec.phoenix.api.chat.program.ChatProgramm;




public class ListEditor extends ChatProgramm
{
    private static final int linesPerPage = 6;
    private final Consumer<List<String>> result;
    private final Template claim;
    private int pages;
    private final List<String> description;
    
    public ListEditor(final Player player, final List<String> current, final Template claim, final Consumer<List<String>> result) {
        super(player);
        this.result = result;
        this.claim = claim;
        this.description = current;
        this.init(0);
    }
    
    public void init(int sendIndex) {
        this.pages = this.description.size() / 6;
        if (this.description.size() % 6 >= 0) {
            ++this.pages;
        }
        super.getCanvasList().clear();
        super.registerChatCanvas((ChatCanvas)new ListCanvas(0, this.description, this));
        for (int i = 1; i < this.pages; ++i) {
            super.registerChatCanvas((ChatCanvas)new ListCanvas(i, this.description, this));
        }
        if (sendIndex >= this.pages && sendIndex != 0) {
            sendIndex = this.pages - 1;
        }
        else if (sendIndex <= 0) {
            sendIndex = 0;
        }
        super.changeView(sendIndex);
    }
    
    public void onClose() {
        PhoenixAPI.get().getChatQueue().unregisterPlayer(super.getPlayer());
        this.result.accept(this.description);
        this.claim.setDescription(this.description);
        SmartInventory.builder().provider((InventoryProvider)new TemplateEditor(this.claim)).size(5).title("Editing " + this.claim.getDisplayname()).build().open(this.getPlayer());
        UtilPlayer.playSound(super.getPlayer(), Sound.ENTITY_ITEM_PICKUP);
    }
    
    public void onOpen() {
        PhoenixAPI.get().getChatQueue().registerPlayer(super.getPlayer());
    }
    
    public static int getLinesPerPage() {
        return 6;
    }
    
    public int getPages() {
        return this.pages;
    }
    
    public List<String> getDescription() {
        return this.description;
    }
}
