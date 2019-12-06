package net.crytec.RegionGUI.chateditor;

import net.md_5.bungee.api.ChatColor;
import net.crytec.phoenix.api.implementation.AnvilGUI;
import net.crytec.phoenix.api.utils.UtilPlayer;
import org.bukkit.Sound;
import java.util.stream.Collectors;
import java.util.ArrayList;
import net.crytec.phoenix.api.chat.program.CanvasLine;
import net.crytec.phoenix.api.chat.program.CanvasLineComponent;
import net.crytec.shaded.org.apache.lang3.StringUtils;
import java.util.List;
import java.util.function.Supplier;
import net.crytec.phoenix.api.chat.program.ChatCanvas;


public class ListCanvas extends ChatCanvas
{
    private int index;
    private Supplier<Integer> maxIndex;
    private final ListEditor parent;
    
    public ListCanvas(final int index, final List<String> description, final ListEditor parent) {
        super((ChatCanvas.CanvasHeader)new ChatCanvas.CanvasHeader(new CanvasLineComponent("\u25a3" + StringUtils.center("§6Editor§f", 48, "-") + "\u25a3")).addComponent(new CanvasLineComponent("§a [\u2714]", player -> parent.close()).setHover("§2Save changes")), (ChatCanvas.CanvasFooter)new ListFooterCanvas(index, () -> parent.getPages() - 1, parent));
        this.parent = parent;
        this.index = index;
        this.maxIndex = (() -> this.parent.getPages() - 1);
        this.init();
    }
    
    public void init() {
        int n = 2;
        for (int i = 1; i < 6; ++i) {
            super.setLine(i, new CanvasLine(i));
        }
        if (!this.parent.getDescription().isEmpty()) {
            for (final String s : this.getPageItems()) {
                final CanvasLine canvasLine = new CanvasLine(n);
                final int n2 = n - 3;
                canvasLine.getLine().add(new CanvasLineComponent("§2[+]§r", player -> {
                    UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK, 0.6f, 1.35f);
                    new AnvilGUI(player, "Enter a new line", (p2, ss) -> {
                        this.parent.getDescription().add(this.index * ListEditor.getLinesPerPage() + n2, ChatColor.translateAlternateColorCodes('&', ss));
                        this.parent.init(this.index);
                        UtilPlayer.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 0.6f, 1.15f);
                        return null;
                    });
                }).setHover("Insert line here"));
                canvasLine.getLine().add(new CanvasLineComponent("§c[-]§r", player -> {
                    UtilPlayer.playSound(player, Sound.ENTITY_ARMOR_STAND_BREAK, 0.5f, 1.35f);
                    this.parent.getDescription().remove(this.index * ListEditor.getLinesPerPage() + n2);
                    this.parent.init(this.index);
                }).setHover("Remove line"));
                canvasLine.getLine().add(new CanvasLineComponent("§e[\u2710]", player -> {
                    UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK, 0.6f, 1.35f);
                    new AnvilGUI(player, s, (p2, s2) -> {
                        this.parent.getDescription().set(this.index * ListEditor.getLinesPerPage() + n2, ChatColor.translateAlternateColorCodes('&', s2));
                        this.parent.init(this.index);
                        UtilPlayer.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 0.6f, 1.15f);
                        return null;
                    });
                }).setHover("Edit this line"));
                canvasLine.getLine().add(new CanvasLineComponent("  §r"));
                canvasLine.getLine().add(new CanvasLineComponent(s));
                super.setLine(n, canvasLine);
                ++n;
            }
        }
        if (this.index == this.maxIndex.get()) {
            final CanvasLine canvasLine2 = new CanvasLine(8);
            canvasLine2.getLine().add(new CanvasLineComponent(StringUtils.center("[Append]", 66, " "), player -> {
                UtilPlayer.playSound(this.parent.getPlayer(), Sound.UI_BUTTON_CLICK, 0.6f, 1.35f);
                this.parent.getDescription().add(this.parent.getDescription().size(), "");
                this.parent.init(this.index);
            }).setHover("Adds empty line"));
            super.setLine(9, canvasLine2);
        }
    }
    
   // private ArrayList<String> getPageItems() {
  //      return this.parent.getDescription().stream().skip(this.index * ListEditor.getLinesPerPage()).limit(ListEditor.getLinesPerPage()).collect((Collector<? super Object, ?, ArrayList<String>>)Collectors.toCollection((Supplier<R>)ArrayList::new));
   // }
    
       private ArrayList<String> getPageItems() {
      return (ArrayList)this.parent.getDescription().stream().skip((long)(this.index * ListEditor.getLinesPerPage())).limit((long)ListEditor.getLinesPerPage()).collect(Collectors.toCollection(ArrayList::new));
   }
       
       
}
