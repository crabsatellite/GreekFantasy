package greekfantasy.client.screen;

import greekfantasy.GFRegistry;
import greekfantasy.GreekFantasy;
import greekfantasy.item.QuestItem;
import greekfantasy.util.Quest;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class QuestScreen extends Screen {

    private static final ResourceLocation BACKGROUND = new ResourceLocation(GreekFantasy.MODID,
            "textures/gui/quest.png");

    private static final int IMAGE_WIDTH = 195;
    private static final int IMAGE_HEIGHT = 146;
    private static final int MARGIN_Y = 14;
    private static final int MARGIN_X = 12;

    private final int itemSlot;
    private final ItemStack itemStack;
    private final ResourceLocation questId;
    private final Quest quest;

    /** The x position of the upper-left corner **/
    private int x;
    /** The y position of the upper-left corner **/
    private int y;

    private MutableComponent title;
    private List<MutableComponent> components;

    protected QuestScreen(int itemSlot, ItemStack itemStack) {
        super(Component.empty());
        this.itemSlot = itemSlot;
        this.itemStack = itemStack;
        if (itemStack.is(GFRegistry.ItemReg.QUEST.get()) && itemStack.hasTag()
                && itemStack.getTag().contains(QuestItem.KEY_QUEST)) {
            this.questId = ResourceLocation.tryParse(itemStack.getTag().getString(QuestItem.KEY_QUEST));
            this.quest = GreekFantasy.QUEST_MAP.getOrDefault(questId, Quest.EMPTY);
        } else {
            this.questId = new ResourceLocation("empty");
            this.quest = Quest.EMPTY;
        }
    }

    @Override
    protected void init() {
        super.init();
        this.x = (this.width - IMAGE_WIDTH) / 2;
        this.y = (this.height - IMAGE_HEIGHT) / 2;
        this.title = this.quest.getDescription().withStyle(ChatFormatting.UNDERLINE);
        this.components = this.quest.getComponents();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // draw background
        guiGraphics.blit(BACKGROUND, this.x, this.y, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        // draw title
        int maxWidth = IMAGE_WIDTH - MARGIN_X * 2;
        int startX = (this.width - this.font.width(title)) / 2;
        int startY = this.y + MARGIN_Y;
        guiGraphics.drawString(this.font, title, startX, startY, 0, false);
        // draw components
        startX = this.x + MARGIN_X;
        startY += this.font.lineHeight + 4;
        for (MutableComponent text : components) {
            guiGraphics.drawWordWrap(this.font, text, startX, startY, maxWidth, 0);
            startY += this.font.wordWrapHeight(text.getString(), maxWidth) + 2;
        }
        // draw other widgets
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
