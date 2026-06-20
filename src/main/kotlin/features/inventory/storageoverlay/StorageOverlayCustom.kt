package moe.nea.firmament.features.inventory.storageoverlay

import me.shedaniel.math.Point
import me.shedaniel.math.Rectangle
import net.minecraft.client.input.MouseButtonEvent
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.screens.inventory.ContainerScreen
import net.minecraft.client.input.CharacterEvent
import net.minecraft.client.input.KeyEvent
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.Slot
import moe.nea.firmament.util.customgui.CustomGui
import moe.nea.firmament.util.focusedItemStack

class StorageOverlayCustom(
    val handler: StorageBackingHandle,
    val screen: ContainerScreen,
    val overview: StorageOverlayScreen,
) : CustomGui() {
	override fun onVoluntaryExit(): Boolean {
		overview.isExiting = true
		StorageOverlayScreen.resetScroll()
		return super.onVoluntaryExit()
	}

	override fun getBounds(): List<Rectangle> {
		return overview.getBounds()
	}

	override fun onInit() {
		overview.rebuildLayout(screen.width, screen.height)
	}

	override fun isPointOverSlot(slot: Slot, xOffset: Int, yOffset: Int, pointX: Double, pointY: Double): Boolean {
		if (!super.isPointOverSlot(slot, xOffset, yOffset, pointX, pointY))
			return false
		if (slot.container !is Inventory) {
			if (!overview.getScrollPanelInner().contains(pointX, pointY))
				return false
		}
		return true
	}

	override fun shouldDrawForeground(): Boolean {
		return false
	}

	override fun mouseReleased(click: MouseButtonEvent): Boolean {
		return overview.mouseReleased(click)
	}

	override fun mouseDragged(click: MouseButtonEvent, offsetX: Double, offsetY: Double): Boolean {
		return overview.mouseDragged(click, offsetX, offsetY)
	}

	override fun keyReleased(input: KeyEvent): Boolean {
		return overview.keyReleased(input)
	}

	override fun keyPressed(input: KeyEvent): Boolean {
		return overview.keyPressed(input)
	}

	override fun charTyped(input: CharacterEvent): Boolean {
		return overview.charTyped(input)
	}

	override fun mouseClick(click: MouseButtonEvent, doubled: Boolean): Boolean {
		return overview.mouseClicked(click, doubled, (handler as? StorageBackingHandle.Page)?.storagePageSlot)
	}

	override fun render(drawContext: GuiGraphicsExtractor, delta: Float, mouseX: Int, mouseY: Int) {
		val baseX = overview.measurements.x
		val baseY = overview.measurements.y

		overview.drawBackgrounds(drawContext)
		overview.drawPages(
			drawContext,
			mouseX,
			mouseY,
			delta,
			(handler as? StorageBackingHandle.Page)?.storagePageSlot,
			screen.menu.slots.take(screen.menu.rowCount * 9).drop(9),
		Point(baseX, baseY)
		)
		overview.drawScrollBar(drawContext)
		overview.drawControls(drawContext, mouseX, mouseY)
	}

	override fun moveSlot(slot: Slot) {
		val baseX = overview.measurements.x
		val baseY = overview.measurements.y

		val index = slot.containerSlot
		if (index in 0..<36) {
			val (x, y) = overview.getPlayerInventorySlotPosition(index)
			slot.x = x - baseX
			slot.y = y - baseY
		} else {
			slot.x = -100000
			slot.y = -100000
		}
	}

	override fun mouseScrolled(
		mouseX: Double,
		mouseY: Double,
		horizontalAmount: Double,
		verticalAmount: Double
	): Boolean {
		if (screen.focusedItemStack != null && StorageOverlay.TConfig.itemsBlockScrolling)
			return false
		return overview.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)
	}
}
