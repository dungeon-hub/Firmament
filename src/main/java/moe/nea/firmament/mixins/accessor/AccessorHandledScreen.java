package moe.nea.firmament.mixins.accessor;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractContainerScreen.class)
public interface AccessorHandledScreen {
    @Accessor("hoveredSlot")
    @Nullable
	Slot getFocusedSlot_Firmament();

    @Accessor("leftPos")
    int getX_Firmament();

    @Accessor("leftPos")
    void setX_Firmament(int newX);

    @Accessor("topPos")
    int getY_Firmament();

    @Accessor("topPos")
    void setY_Firmament(int newY);

}
