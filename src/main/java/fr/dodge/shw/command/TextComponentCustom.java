package fr.dodge.shw.command;

import net.minecraft.util.text.*;
import net.minecraft.util.text.event.HoverEvent;

public class TextComponentCustom {

    protected static ITextComponent textComponentWaypoint(String name) {
        return new TextComponentString(String.format("%s", name)).setStyle(new Style()
                .setItalic(true)
                .setColor(TextFormatting.LIGHT_PURPLE));
    }

    protected static ITextComponent textComponentSuccess(String translationKey, Object... args) {
        return new TextComponentTranslation(translationKey, args).setStyle(new Style()
                .setColor(TextFormatting.DARK_GREEN));
    }

    protected static ITextComponent textComponentSuccessServer(String translationKey, Object... args) {
        return new TextComponentTranslation(translationKey, args).setStyle(new Style()
                .setItalic(true)
                .setColor(TextFormatting.YELLOW));
    }

    protected static ITextComponent textComponentCooldown(long cooldown, long totalCooldown) {
        return new TextComponentString(Long.toString(cooldown)).setStyle(new Style()
                .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        (new TextComponentTranslation("commands.shw.totalCooldown", totalCooldown)).setStyle(new Style()
                                .setColor(TextFormatting.GREEN)))));
    }

    protected static ITextComponent stringsToTextComponent(String separator, String prefix, String suffix, TextFormatting color, String... args) {
        ITextComponent result = new TextComponentString(prefix);
        Style style = new Style().setColor(color);

        for (int i = 0; i < args.length; i++) {
            result.appendSibling(new TextComponentString(args[i]).setStyle(style));
            if (i < args.length - 1) result.appendText(separator);
        }
        result.appendText(suffix);
        return result;
    }

}
