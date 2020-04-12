package fr.dodge.shw.command;

import fr.dodge.shw.Reference;
import fr.dodge.shw.config.SHWConfiguration;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.HoverEvent;

public class SHWUtilsTextComponent {

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

    protected static ITextComponent getBorder(boolean returnLine, String name) {
        String limitText = "====";
        String border = String.format("%s %s %s", limitText, Reference.NAME + name, limitText);
        return new TextComponentString(border).appendText(returnLine ? "\n" : "")
                .setStyle(new Style().setColor(TextFormatting.GREEN));
    }

    public static ITextComponent textComponentNumberWaypoint(int number) {
        ITextComponent numberOfWaypoints = new TextComponentString(String.format(" (%d/%d)", number, SHWConfiguration.WAYPOINTS.maxWaypoints));
        double color = (double) number / SHWConfiguration.WAYPOINTS.maxWaypoints;
        double ratio = 0.5;
        numberOfWaypoints.setStyle(new Style().setColor(
                color < ratio - ratio / 6 ? TextFormatting.DARK_GREEN :
                        color > ratio + ratio / 2 ? TextFormatting.RED :
                                TextFormatting.GOLD
        ));
        return numberOfWaypoints;
    }
}
