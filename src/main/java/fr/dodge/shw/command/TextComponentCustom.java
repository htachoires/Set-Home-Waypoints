package fr.dodge.shw.command;

import net.minecraft.util.text.*;
import net.minecraft.util.text.event.HoverEvent;

public class TextComponentCustom {

    public static ITextComponent textComponentWaypoint(String name) {
        return new TextComponentString(String.format("%s", name)).setStyle(new Style()
                .setItalic(true)
                .setColor(TextFormatting.LIGHT_PURPLE));
    }

    public static ITextComponent textComponentSuccess(String translationKey, Object... args) {
        return new TextComponentTranslation(translationKey, args).setStyle(new Style()
                .setColor(TextFormatting.DARK_GREEN));
    }

    public static ITextComponent textComponentCooldown(long cooldown, long totalCooldown) {
        return new TextComponentString(Long.toString(cooldown)).setStyle(new Style()
                .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        (new TextComponentTranslation("commands.shw.totalCooldown", totalCooldown)).setStyle(new Style()
                                .setColor(TextFormatting.GREEN)))));
    }
}
