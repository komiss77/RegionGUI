package net.crytec.RegionGUI.menus;

import net.crytec.RegionGUI.RegionGUI;
import net.crytec.RegionGUI.data.Template;
import net.crytec.RegionGUI.manager.TemplateManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import ru.komiss77.ApiOstrov;
import ru.komiss77.utils.ItemBuilder;
import ru.komiss77.utils.inventory.ClickableItem;
import ru.komiss77.utils.inventory.ConfirmationGUI;
import ru.komiss77.utils.inventory.InputButton;
import ru.komiss77.utils.inventory.InputButton.InputType;
import ru.komiss77.utils.inventory.InventoryContent;
import ru.komiss77.utils.inventory.InventoryProvider;
import ru.komiss77.utils.inventory.SlotPos;
import ru.komiss77.utils.inventory.SmartInventory;
import ru.komiss77.version.AnvilGUI;



public class TemplateEditor implements InventoryProvider
{
    private static final ItemStack filler;
    private final Template template;
    
    static {
        filler = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name(" ").build();
    }
    
    public TemplateEditor(final Template claim) {
        this.template = claim;
    }
    
    
    
    @Override
    public void init(final Player player, final InventoryContent contents) {
        contents.fillRow(0, ClickableItem.empty(TemplateEditor.filler));
        contents.fillRow(4, ClickableItem.empty(TemplateEditor.filler));
        
        
        contents.set(SlotPos.of(0, 4), ClickableItem.of(new ItemBuilder(this.template.getIcon())
                .name("§7Установить иконку")
                .lore("§7Ткните сюда предметом из инвентаря")
                .lore("§7для смены иконки")
                .build(), inventoryClickEvent -> {
            if (inventoryClickEvent.getClick() == ClickType.LEFT && inventoryClickEvent.getCursor() != null && inventoryClickEvent.getCursor().getType() != Material.AIR) {
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1);
                this.template.setIcon(inventoryClickEvent.getCursor().getType());
                inventoryClickEvent.getView().getBottomInventory().addItem(new ItemStack[] { inventoryClickEvent.getCursor() });
                inventoryClickEvent.getView().setCursor(new ItemStack(Material.AIR));
                this.reopen(player, contents);
            }
            //return;
        }));
        
        
        contents.set(SlotPos.of(1, 0), new InputButton(InputType.ANVILL, new ItemBuilder(Material.NAME_TAG)
            .name("§7Отображаемое название")
            .lore("§7Текущее: §6" + this.template.getDisplayname())
            .build(), template.getDisplayname(), dn -> {
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1);
                this.template.setDisplayname(dn);
                SmartInventory.builder().provider(new AdminTemplateList()).size(6)
                .title("§8Редактор заготовок [" + player.getWorld().getName() + "]").build().open(player);
               // return;
            }));
        
        
        
        
        
        
        
        
        
        //Описание
        contents.set(SlotPos.of(2, 0), ClickableItem.of(new ItemBuilder(Material.BOOK)
                .name("§7Описание")
                .lore("§7Текущее:")
                .lore( this.template.getDescription() )
                .lore("")
                .lore("§aЛКМ §7добавить строку")
                .lore("§aПКМ §7удалить последнюю строку.")
                .build(), inventoryClickEvent2 -> {

                        if ( inventoryClickEvent2.getClick() == ClickType.RIGHT) {
                        if (!template.getDescription().isEmpty()) {
                            template.getDescription().remove(template.getDescription().size() - 1);
                            reopen(player, contents);
                        }

                    } else {
                            
                       AnvilGUI agui = new AnvilGUI(RegionGUI.getInstance(), player, "строка..", (player2, value) -> {
                            template.getDescription().add(ChatColor.translateAlternateColorCodes('&', value));
                                //Bukkit.getScheduler().runTaskLater(RegionGUI.getInstance(), () -> contents.inventory().getProvider().reOpen(player, contents), 1L);
                            reopen(player, contents);
                            return null;
                        });

                    }
                }
        ));
        
 
        
  
        
        
        
        
        
        
        
        
        
        //права

        //Сообщение, когда нет права
        contents.set(SlotPos.of(1, 1), ClickableItem.of(new ItemBuilder(Material.BLAZE_POWDER)
                .name("§7Право для покупки")
                .lore( template.getPermission().isEmpty() ? "§8Не требуется" : "§f" + template.getPermission())
                .lore("")
                .lore("§aЛКМ §7установить право")
                .lore("§aПКМ §7удалить право")
                .build(), clickEvent -> {
                    if (clickEvent.getClick() == ClickType.RIGHT) {
                        template.setPermission("");
                        reopen(player, contents);
                    } else {
                        AnvilGUI agui = new AnvilGUI(RegionGUI.getInstance(), player, "region.template.xxx", (player2, value) -> {
                            template.setPermission(ChatColor.stripColor(value));
                                //Bukkit.getScheduler().runTaskLater(RegionGUI.getInstance(), () -> contents.inventory().getProvider().reOpen(player, contents), 1L);
                            reopen(player, contents);
                            return null;
                        });

                    }
                }));
        
        

        
        
        
        
        
        
        //Сообщение, когда нет права
        contents.set(SlotPos.of(2, 1), ClickableItem.of(new ItemBuilder(Material.REDSTONE_TORCH).
                name("§7Сообщение, когда нет права")
                .lore("§eЕсли нет права, указанного выше,")
                .lore("§eна иконке выбора заготовки")
                .lore("§eбудет эта надпись.")
                .lore("")
                .lore("§7Текущее:")
                .lore(template.getNoPermDescription())
                .lore("")
                .lore("§aЛКМ §7добавить строку")
                .lore("§aПКМ §7удалить последнюю строку.")
                .build(), inventoryClickEvent3 -> {
                    if (inventoryClickEvent3.getClick() == ClickType.RIGHT) {
                        if (!template.getNoPermDescription().isEmpty()) {
                            template.getNoPermDescription().remove(template.getNoPermDescription().size() - 1);
                            reopen(player, contents);
                        }
                    } else {
                        AnvilGUI agui = new AnvilGUI(RegionGUI.getInstance(), player, "строка..", (player2, value) -> {
                            template.getNoPermDescription().add(ChatColor.translateAlternateColorCodes('&', value));
                                //Bukkit.getScheduler().runTaskLater(RegionGUI.getInstance(), () -> contents.inventory().getProvider().reOpen(player, contents), 1L);
                            reopen(player, contents);
                            return null;
                        });


                    }
                }));
        
        
        
        
        //Цена
        contents.set(SlotPos.of(1, 2), new InputButton(new ItemBuilder(Material.GOLD_NUGGET)
                .name("§7Цена")
                .lore("§7Сейчас: §6" + this.template.getPrice()+" §7лони")
                .build(), String.valueOf(this.template.getPrice()), s4 -> {
                    if (!ApiOstrov.isInteger(s4)) {
                        player.sendMessage("§cВведите целое число!");
                        this.reopen(player, contents);
                        //return;
                    }
                    else {
                        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1);
                        this.template.setPrice(Integer.parseInt(s4));
                        this.reopen(player, contents);
                        //return;
                    }
                }
        ));
        
        
        
        //Возврат денег
        contents.set(SlotPos.of(2, 2), new InputButton(new ItemBuilder(Material.GOLD_NUGGET)
                .name("§7Возврат денег")
                .lore("§7Данная сумма будет §aполучена")
                .lore("§7игроком после удаления региона.")
                .lore("§7Сейчас: §6" + this.template.getRefund())
                .build(), String.valueOf(this.template.getRefund()), s5 -> {
                    if (!ApiOstrov.isInteger(s5)) {
                        player.sendMessage("§cВведите целое число!");
                        this.reopen(player, contents);
                       // return;
                    } else {
                        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1);
                        template.setRefund(Integer.parseInt(s5));
                        this.reopen(player, contents);
                        //return;
                    }
                }
        ));
        
        
        
        //Размер
        contents.set(SlotPos.of(1, 3), new InputButton(new ItemBuilder(Material.BEACON)
                .name("§7Размер")
                .lore("§7Сейчас: §6" + this.template.getSize())
                .lore("§7Длинна каждой стороны")
                .lore("§7квадратного основания.")
                .build(), String.valueOf(this.template.getSize()), s6 -> {
                    if (!ApiOstrov.isInteger(s6)) {
                        player.sendMessage("§cВведите целое число!");
                        this.reopen(player, contents);
                       // return;
                    }
                    else {
                        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1);
                        this.template.setSize(Integer.parseInt(s6));
                        this.reopen(player, contents);
                        //return;
                    }
                }
        ));
        
        
        
        //Команды
        contents.set(SlotPos.of(1, 5), ClickableItem.of(new ItemBuilder(Material.COMMAND_BLOCK)
                .name("§7Команды")
                .lore("§7Команды, выполняемые после покупки региона.")
                .lore("§7Для выполнения от консоли добавьте <server> в начале.")
                .lore("§7Заполнители:")
                .lore("§e%player% §7- ник покупателя")
                .lore("§e%region% §7- название WG региона")
                .lore("§e%world% §7- название мира")
                .lore("")
                .lore( template.getRunCommands().isEmpty() ? "§8Сейчас команд нет.":"§7Текущие команды:")
                .lore(template.getRunCommands())
                .lore("")
                .lore("§aЛКМ §7добавить строку")
                .lore("§aПКМ §7удалить последнюю строку.")
                .build(), inventoryClickEvent4 -> {
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1);
                    if (inventoryClickEvent4.isRightClick()) {
                        if (!template.getRunCommands().isEmpty()) {
                            this.template.getRunCommands().remove(this.template.getRunCommands().size() - 1);
                            this.reopen(player, contents);
                        }
                    } else {
                        AnvilGUI agui = new AnvilGUI(RegionGUI.getInstance(), player, "строка..", (player2, value) -> {
                            template.getRunCommands().add(ChatColor.stripColor(value));
                                //Bukkit.getScheduler().runTaskLater(RegionGUI.getInstance(), () -> contents.inventory().getProvider().reOpen(player, contents), 1L);
                            reopen(player, contents);
                            return null;
                        });
                       /* player.closeInventory();
                        player.sendMessage("§7Введите строку команды (§cбез§7 (/)): ");
                        PhoenixAPI.get().getPlayerChatInput(player, s7 -> {
                            this.claim.getRunCommands().add(s7);
                            this.reopen(player, contents);
                        });*/
                        //return;
                    }
                }
        ));
        
        
        //Автопостройка ограждения
        contents.set(SlotPos.of(1, 4), ClickableItem.of(new ItemBuilder(Material.OAK_FENCE)
                .name("§7Автопостройка ограждения")
                .lore("§7Сейчас: " + (template.isGenerateBorder() ? "да":"нет")   )
                .build(), p2 -> {
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1);
                    this.template.setGenerateBorder(!this.template.isGenerateBorder());
                    this.reopen(player, contents);
                    //return;
                }
        ));
        
        
        
        //Материал ограждения
        contents.set(SlotPos.of(2, 4), ClickableItem.of(new ItemBuilder(this.template.getBorderMaterial())
                .name("§7Материал ограждения")
                .lore("§7Ткните сюда предметом из инвентаря")
                .lore("§7для установки материала.")
                .build(), inventoryClickEvent5 -> {
                    if (inventoryClickEvent5.getClick() == ClickType.LEFT && inventoryClickEvent5.getCursor() != null && inventoryClickEvent5.getCursor().getType() != Material.AIR) {
                        if (!inventoryClickEvent5.getCursor().getType().isBlock()) {
                            player.sendMessage("§cЭтот материал не может быть блоком!");
                            this.reopen(player, contents);
                        } else {
                            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1);
                            this.template.setBorderMaterial(inventoryClickEvent5.getCursor().getType());
                            inventoryClickEvent5.getView().getBottomInventory().addItem(new ItemStack[] { inventoryClickEvent5.getCursor() });
                            inventoryClickEvent5.getView().setCursor(new ItemStack(Material.AIR));
                            this.reopen(player, contents);
                        }
                    }
                   // return;
                }
        ));
        
        
        
        //удалить заготовку
        contents.set(SlotPos.of(4, 8), ClickableItem.of(new ItemBuilder(Material.TNT)
                .name("§4Удалить заготовку")
                .lore("§7После удаления заготовку не будет")
                .lore("§7доступна для покупки игроками.")
                .build(), p2 -> ConfirmationGUI.open( player, "§4Подтверждение", result -> {
                    if (result) {
                        player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.5f, 1.15f);
                        SmartInventory.builder().provider((InventoryProvider)new AdminTemplateList()).size(6).title("§8Редактор заготовок [" + player.getWorld().getName() + "]").build().open(player);
                        TemplateManager.deleteTemplate(this.template);
                    }
                    else {
                        this.reopen(player, contents);
                        player.playSound(player.getLocation(), Sound.ENTITY_LEASH_KNOT_PLACE, 0.5f, 0.85f);
                    }
                }
                )));

        
        
        
        
        
        //сохранить
        contents.set(SlotPos.of(4, 4), ClickableItem.of(new ItemBuilder(Material.EMERALD).name("§2Сохранить заготовку").build(), p1 -> {
            TemplateManager.save(template);
            SmartInventory.builder().provider((InventoryProvider)new AdminTemplateList()).size(6).title("§8Редактор заготовок [" + player.getWorld().getName() + "]").build().open(player);
        }));
        
        
        
        
        
        
    }
    
    
}



/*
Decompiled with CFR 0.145.

package net.crytec.RegionGUI.menus.admin;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.crytec.RegionGUI.RegionGUI;
import net.crytec.RegionGUI.data.RegionClaim;
import net.crytec.RegionGUI.manager.ClaimManager;
import net.crytec.RegionGUI.menus.admin.AdminTemplateList;
import net.crytec.phoenix.api.PhoenixAPI;
import net.crytec.phoenix.api.inventory.ClickableItem;
import net.crytec.phoenix.api.inventory.ConfirmationGUI;
import net.crytec.phoenix.api.inventory.SmartInventory;
import net.crytec.phoenix.api.inventory.buttons.InputButton;
import net.crytec.phoenix.api.inventory.content.InventoryContents;
import net.crytec.phoenix.api.inventory.content.InventoryProvider;
import net.crytec.phoenix.api.inventory.content.SlotPos;
import net.crytec.phoenix.api.item.ItemBuilder;
import net.crytec.phoenix.api.item.ItemFactory;
import net.crytec.phoenix.api.utils.F;
import net.crytec.phoenix.api.utils.UtilMath;
import net.crytec.phoenix.api.utils.UtilPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class TemplateEditor
implements InventoryProvider {
    private static final ItemStack filler = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name(" ").build();
    private final RegionClaim claim;

    public TemplateEditor(RegionClaim regionClaim) {
        this.claim = regionClaim;
    }

    public void initCFR(Player player, InventoryContents inventoryContents) {
        inventoryContents.fillRow(0, ClickableItem.empty((ItemStack)filler));
        inventoryContents.fillRow(4, ClickableItem.empty((ItemStack)filler));
        
        
        inventoryContents.set(SlotPos.of((int)0, (int)4), ClickableItem.of((ItemStack)new ItemBuilder(this.claim.getIcon()).name("\u00a77Set list icon").lore("\u00a77Click with an Item on your").lore("\u00a77curser to set the list icon").build(), inventoryClickEvent -> {
            if (inventoryClickEvent.getClick() == ClickType.LEFT && inventoryClickEvent.getCursor() != null && inventoryClickEvent.getCursor().getType() != Material.AIR) {
                UtilPlayer.playSound((Player)player, (Sound)Sound.UI_BUTTON_CLICK, (float)0.5f, (float)1.0f);
                this.claim.setIcon(inventoryClickEvent.getCursor().getType());
                inventoryClickEvent.getView().getBottomInventory().addItem(new ItemStack[]{inventoryClickEvent.getCursor()});
                inventoryClickEvent.getView().setCursor(new ItemStack(Material.AIR));
                this.reopen(player, inventoryContents);
            }
        }));
        
        
        inventoryContents.set(SlotPos.of((int)1, (int)0), (ClickableItem)new InputButton(new ItemBuilder(Material.NAME_TAG).name("\u00a77Template").lore("\u00a77Current name: \u00a76" + this.claim.getDisplayname()).build(), "Name..", string -> {
            UtilPlayer.playSound((Player)player, (Sound)Sound.UI_BUTTON_CLICK, (float)0.5f, (float)1.0f);
            this.claim.setDisplayname((String)string);
            SmartInventory.builder().provider((InventoryProvider)new AdminTemplateList()).size(6).title("\u00a78Template Editor [" + player.getWorld().getName() + "]").build().open(player);
        }));
        
        
        inventoryContents.set(SlotPos.of((int)2, (int)0), ClickableItem.of((ItemStack)new ItemBuilder(Material.BOOK).name("\u00a77Description").lore("\u00a77Current description:").lore(this.claim.getDescription().stream().map(string -> ChatColor.translateAlternateColorCodes((char)'&', (String)string)).collect(Collectors.toList())).lore("").lore("\u00a7aЛКМ \u00a77to add a new line").lore("\u00a7aПКМ \u00a77to delete the last line.").build(), inventoryClickEvent -> {
            if (inventoryClickEvent.getClick() == ClickType.RIGHT) {
                if (this.claim.getDescription().size() <= 0) {
                    return;
                }
                List<String> list = this.claim.getDescription();
                list.remove(list.size() - 1);
                this.claim.setDescription(list);
                this.reopen(player, inventoryContents);
                return;
            }
            player.closeInventory();
            player.sendMessage("\u00a77Enter a new line. Type \u00a7eexit\u00a77 to abort");
            PhoenixAPI.get().getPlayerChatInput(player, string -> {
                if (string.equals("exit")) {
                    Bukkit.getScheduler().runTask((Plugin)RegionGUI.getInstance(), () -> this.reopen(player, inventoryContents));
                    return;
                }
                ArrayList arrayList = this.claim.getDescription() == null ? Lists.newArrayList() : this.claim.getDescription();
                arrayList.add(string);
                this.claim.setDescription(arrayList);
                Bukkit.getScheduler().runTask((Plugin)RegionGUI.getInstance(), () -> this.reopen(player, inventoryContents));
            });
        }));
        
        
        
        inventoryContents.set(SlotPos.of((int)1, (int)1), (ClickableItem)new InputButton(new ItemBuilder(Material.BLAZE_POWDER).name("\u00a77Set Permission").lore("\u00a77Current Permission: \u00a76" + this.claim.getPermission()).build(), "permission..", string -> {
            UtilPlayer.playSound((Player)player, (Sound)Sound.UI_BUTTON_CLICK, (float)0.5f, (float)1.0f);
            this.claim.setPermission((String)string);
            this.reopen(player, inventoryContents);
        }));
        inventoryContents.set(SlotPos.of((int)2, (int)1), ClickableItem.of((ItemStack)new ItemBuilder(Material.REDSTONE_TORCH).name("\u00a77Set 'No Permission' description").lore("\u00a7eThis lines will be added below").lore("\u00a7ethe claim description in /land").lore("").lore("\u00a77Current:").lore(this.claim.getNoPermDescription()).lore("\u00a7aЛКМ \u00a77to add a new line").lore("\u00a7aПКМ \u00a77to delete the last line.").build(), inventoryClickEvent -> {
            if (inventoryClickEvent.getClick() == ClickType.RIGHT) {
                if (this.claim.getDescription().size() <= 0) {
                    return;
                }
                this.claim.getNoPermDescription().remove(this.claim.getNoPermDescription().size() - 1);
                this.reopen(player, inventoryContents);
                return;
            }
            player.closeInventory();
            player.sendMessage("\u00a77Enter a new line. Type \u00a7eexit\u00a77 to abort");
            PhoenixAPI.get().getPlayerChatInput(player, string -> {
                if (string.equals("exit")) {
                    Bukkit.getScheduler().runTask((Plugin)RegionGUI.getInstance(), () -> this.reopen(player, inventoryContents));
                    return;
                }
                this.claim.getNoPermDescription().add(ChatColor.translateAlternateColorCodes((char)'&', (String)string));
                Bukkit.getScheduler().runTask((Plugin)RegionGUI.getInstance(), () -> this.reopen(player, inventoryContents));
            });
        }));
        
        
        
        inventoryContents.set(SlotPos.of((int)1, (int)2), (ClickableItem)new InputButton(new ItemBuilder(Material.GOLD_NUGGET).name("\u00a77Price").lore("\u00a77Current Price: \u00a76" + this.claim.getPrice()).build(), String.valueOf(this.claim.getPrice()), string -> {
            if (!UtilMath.isInt((String)string)) {
                player.sendMessage("\u00a7cError: \u00a77The given input is not a valid integer!");
                this.reopen(player, inventoryContents);
                return;
            }
            UtilPlayer.playSound((Player)player, (Sound)Sound.UI_BUTTON_CLICK, (float)0.5f, (float)1.0f);
            this.claim.setPrice(Integer.parseInt(string));
            this.reopen(player, inventoryContents);
        }));
        inventoryContents.set(SlotPos.of((int)2, (int)2), (ClickableItem)new InputButton(new ItemBuilder(Material.GOLD_NUGGET).name("\u00a77Refund").lore("\u00a77This amount will be \u00a7aadded\u00a77 to").lore("\u00a77the players balance on deletion").lore("\u00a77Current Refund: \u00a76" + this.claim.getRefund()).build(), String.valueOf(this.claim.getRefund()), string -> {
            if (!UtilMath.isInt((String)string)) {
                player.sendMessage("\u00a7cError: \u00a77The given input is not a valid integer!");
                this.reopen(player, inventoryContents);
                return;
            }
            UtilPlayer.playSound((Player)player, (Sound)Sound.UI_BUTTON_CLICK, (float)0.5f, (float)1.0f);
            this.claim.setRefund(Integer.parseInt(string));
            this.reopen(player, inventoryContents);
        }));
        inventoryContents.set(SlotPos.of((int)1, (int)3), (ClickableItem)new InputButton(new ItemBuilder(Material.BEACON).name("\u00a77Set Size").lore("\u00a77Current Size: \u00a76" + this.claim.getSize()).lore("\u00a77Increasing the size \u00a7c\u00a7lwill not\u00a77 update").lore("\u00a77already claimed/existing regions.").lore("\u00a77This does only affect new regions").build(), String.valueOf(this.claim.getSize()), string -> {
            if (!UtilMath.isInt((String)string)) {
                player.sendMessage("\u00a7cError: \u00a77The given input is not a valid integer!");
                this.reopen(player, inventoryContents);
                return;
            }
            UtilPlayer.playSound((Player)player, (Sound)Sound.UI_BUTTON_CLICK, (float)0.5f, (float)1.0f);
            this.claim.setSize(Integer.parseInt(string));
            this.reopen(player, inventoryContents);
        }));
        inventoryContents.set(SlotPos.of((int)1, (int)5), new ClickableItem(new ItemBuilder(Material.COMMAND_BLOCK).name("\u00a77Commands").lore("\u00a77This is a set of commands that will be").lore("\u00a77executed by the \u00a7a\u00a7lplayer\u00a77 after").lore("\u00a77a successfull purchase.").lore("\u00a77You may use this to set default flags or").lore("\u00a77whatever you want.").lore("\u00a77Valid placeholders:").lore("\u00a7e%player% \u00a77- The players name").lore("\u00a7e%region% \u00a77- The purchased region").lore("\u00a7e%world% \u00a77- The worldname").lore("").lore("\u00a77To run a command from the console,").lore("\u00a77simply put \u00a7e<server>\u00a77 in front").lore("").lore("\u00a77ПКМ to \u00a7cdelete\u00a77 the last command in the list.").lore("\u00a77Current Commands:").lore(this.claim.getRunCommands()).build(), inventoryClickEvent -> {
            UtilPlayer.playSound((Player)player, (Sound)Sound.UI_BUTTON_CLICK, (float)0.5f, (float)1.0f);
            if (inventoryClickEvent.isRightClick()) {
                if (this.claim.getRunCommands().size() == 0) {
                    return;
                }
                this.claim.getRunCommands().remove(this.claim.getRunCommands().size() - 1);
                this.reopen(player, inventoryContents);
                return;
            }
            player.closeInventory();
            player.sendMessage("\u00a77Please enter the command you want to add (\u00a7cwithout\u00a77 the first slash (/)): ");
            PhoenixAPI.get().getPlayerChatInput(player, string -> {
                this.claim.getRunCommands().add((String)string);
                this.reopen(player, inventoryContents);
            });
        }));
        inventoryContents.set(SlotPos.of((int)1, (int)4), ClickableItem.of((ItemStack)new ItemBuilder(Material.OAK_FENCE).name("\u00a77Enable Border").lore("\u00a77Currently enabled: " + F.tf((boolean)this.claim.isGenerateBorder())).build(), inventoryClickEvent -> {
            UtilPlayer.playSound((Player)player, (Sound)Sound.UI_BUTTON_CLICK, (float)0.5f, (float)1.0f);
            this.claim.setGenerateBorder(!this.claim.isGenerateBorder());
            this.reopen(player, inventoryContents);
        }));
        inventoryContents.set(SlotPos.of((int)2, (int)4), ClickableItem.of((ItemStack)new ItemBuilder(this.claim.getBorderMaterial()).name("\u00a77Set Border Material").lore("\u00a77Click with an Item on your").lore("\u00a77curser to set the border material").build(), inventoryClickEvent -> {
            if (inventoryClickEvent.getClick() == ClickType.LEFT && inventoryClickEvent.getCursor() != null && inventoryClickEvent.getCursor().getType() != Material.AIR) {
                if (!inventoryClickEvent.getCursor().getType().isBlock()) {
                    player.sendMessage("\u00a7cERROR: \u00a77The given material is not a placeable block.");
                    this.reopen(player, inventoryContents);
                    return;
                }
                UtilPlayer.playSound((Player)player, (Sound)Sound.UI_BUTTON_CLICK, (float)0.5f, (float)1.0f);
                this.claim.setBorderMaterial(inventoryClickEvent.getCursor().getType());
                inventoryClickEvent.getView().getBottomInventory().addItem(new ItemStack[]{inventoryClickEvent.getCursor()});
                inventoryClickEvent.getView().setCursor(new ItemStack(Material.AIR));
                this.reopen(player, inventoryContents);
            }
        }));
        inventoryContents.set(SlotPos.of((int)4, (int)8), ClickableItem.of((ItemStack)new ItemBuilder(Material.TNT).name("\u00a74Delete template").lore("\u00a77Deleting this template will remove it").lore("\u00a77from all players that have already").lore("\u00a77purchased it.").build(), inventoryClickEvent -> ConfirmationGUI.open((Player)player, (String)"\u00a74Confirm to delete template", bl -> {
            if (bl.booleanValue()) {
                UtilPlayer.playSound((Player)player, (Sound)Sound.ENTITY_GENERIC_EXPLODE, (float)0.5f, (float)1.15f);
                SmartInventory.builder().provider((InventoryProvider)new AdminTemplateList()).size(6).title("\u00a78Template Editor [" + player.getWorld().getName() + "]").build().open(player);
                RegionGUI.getInstance().getClaimManager().deleteTemplate(this.claim);
            } else {
                this.reopen(player, inventoryContents);
                UtilPlayer.playSound((Player)player, (Sound)Sound.ENTITY_LEASH_KNOT_PLACE, (float)1.0f, (float)0.85f);
            }
        })));
        inventoryContents.set(SlotPos.of((int)4, (int)4), ClickableItem.of((ItemStack)new ItemBuilder(Material.EMERALD).name("\u00a72Save template").build(), inventoryClickEvent -> {
            RegionGUI.getInstance().getClaimManager().save();
            SmartInventory.builder().provider((InventoryProvider)new AdminTemplateList()).size(6).title("\u00a78Template Editor [" + player.getWorld().getName() + "]").build().open(player);
        }));
    }
    
}

}

*/