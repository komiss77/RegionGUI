package net.crytec.RegionGUI.chateditor;

import net.crytec.phoenix.api.utils.UtilPlayer;
import org.bukkit.Sound;
import net.crytec.phoenix.api.chat.program.CanvasLineComponent;
import net.crytec.shaded.org.apache.lang3.StringUtils;
import org.bukkit.entity.Player;
import java.util.function.Supplier;
import net.crytec.phoenix.api.chat.program.ChatCanvas;




public class ListFooterCanvas extends ChatCanvas.CanvasFooter
{
    private final int siteIndex;
    private final Supplier<Integer> maxIndex;
    private final ListEditor root;
    
    
    public ListFooterCanvas(final int index, final Supplier<Integer> maxIndex, final ListEditor root) {
        this.siteIndex = index;
        this.maxIndex = maxIndex;
        this.root = root;
    }
    
    
    @Override
    public void sendTo(final Player player) {
        super.getLine().clear();
        if (this.siteIndex == 0) {
            final boolean b = this.maxIndex.get() == 0;
            super.getLine().add(new CanvasLineComponent(StringUtils.center(" §6" + this.siteIndex + " §f/ §6" + this.maxIndex.get() + "§f ", b ? 50 : 48, "-")));
            if (!b) {
                super.getLine().add(this.getRightArrow());
            }
        }
        else if (this.siteIndex == this.maxIndex.get()) {
            super.getLine().add(this.getLeftArrow());
            super.getLine().add(new CanvasLineComponent(StringUtils.center(" §6" + this.siteIndex + " §f/ §6" + this.maxIndex.get() + "§f ", 48, "-")));
        }
        else {
            super.getLine().add(this.getLeftArrow());
            super.getLine().add(new CanvasLineComponent(StringUtils.center(" §6" + this.siteIndex + " §f/ §6" + this.maxIndex.get() + "§f ", 46, "-")));
            super.getLine().add(this.getRightArrow());
        }
        super.sendTo(player);
    }
    
    private CanvasLineComponent getLeftArrow() {
        final CanvasLineComponent canvasLineComponent = new CanvasLineComponent("§6\u25c0\u25c0\u25c0§f", player -> {
            this.root.changeView(this.siteIndex - 1);
            UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK, 0.6f, 1.2f);
        });
        canvasLineComponent.getDescriptionLines().add("Page " + (this.siteIndex - 1));
        return canvasLineComponent;
    }
    
    private CanvasLineComponent getRightArrow() {
        final CanvasLineComponent canvasLineComponent = new CanvasLineComponent("§6\u25b6\u25b6\u25b6", player -> {
            this.root.changeView(this.siteIndex + 1);
            UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK, 0.6f, 1.2f);
        });
        canvasLineComponent.getDescriptionLines().add("Page " + (this.siteIndex + 1));
        return canvasLineComponent;
    }
}
