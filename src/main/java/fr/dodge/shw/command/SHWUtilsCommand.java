package fr.dodge.shw.command;

import fr.dodge.shw.Reference;
import fr.dodge.shw.config.ICommandConfiguration;
import fr.dodge.shw.config.SHWConfiguration;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;

import java.util.Arrays;

public class SHWUtilsCommand {

    protected static final int MAX_DIGITS = 6;

    protected static final String cooldown = "cooldown";
    protected static final String limit = "limit";
    protected static final String travelThroughDimension = "travelThroughDimension";
    protected static final String travelThroughDimensionLC = "travelthroughdimension";

    protected static final String[] commandArgs = {cooldown, limit, travelThroughDimension};

    protected static void teleportPlayer(MinecraftServer server, EntityPlayerMP player, String position, boolean travelThroughDimension, String command) throws CommandException {
        String[] data = position.replace(',', '.').split(";");

        WorldServer worldDestination = server.getWorld(Integer.parseInt(data[0]));
        WorldServer actualDimension = server.getWorld(player.getEntityWorld().provider.getDimension());

        if (actualDimension == worldDestination || travelThroughDimension) {
            if (worldDestination != actualDimension) {
                player.setPortal(player.getPosition());
                player.changeDimension(Integer.parseInt(data[0]));
            }
            player.connection.setPlayerLocation(Double.parseDouble(data[1]), Double.parseDouble(data[2]),
                    Double.parseDouble(data[3]), Float.parseFloat(data[4]), Float.parseFloat(data[5]));
        } else {
            throw new CommandException("commands.shw.error.travelThroughDimension", command);
        }
    }

    protected static String getPositionPlayer(EntityPlayer player) {
        return String.format("%d;%d.5;%d.2;%d.5;%d;1.5",
                player.dimension,
                (int) player.posX,
                (int) player.posY,
                (int) player.posZ,
                (int) player.rotationYaw
        );
    }

    protected static void manageConfiguration(MinecraftServer server, ICommandSender sender, String[] args, String command, ICommandConfiguration configuration) throws CommandException {
        if (args.length >= 1) {
            switch (args[0].toLowerCase()) {
                case cooldown:
                    cooldown(server, args, command, configuration);
                    break;
                case travelThroughDimensionLC:
                    travelThroughDimension(server, args, command, configuration);
                    break;
                case limit:
                    if (!(configuration instanceof SHWConfiguration.HomeConfiguration))
                        limit(server, args, command, configuration);
                    else
                        sender.sendMessage(SHWUtilsTextComponent.textComponentSuccessServer("commands.shw.server.error.limit"));
                    break;
                default:
                    help(sender, command, configuration);
                    break;
            }
        } else {
            help(sender, command, configuration);
        }
    }

    protected static void info(ICommandSender sender, String[] args, String command, ICommandConfiguration configuration) {
        switch (args[0].toLowerCase()) {
            case cooldown:
                cooldownInfo(sender, command, configuration.getCooldown());
                break;
            case travelThroughDimensionLC:
                travelThroughDimensionInfo(sender, command, configuration.isTravelThroughDimension());
                break;
            case limit:
                if (!(configuration instanceof SHWConfiguration.HomeConfiguration))
                    limitInfo(sender, command, configuration.getMaxWaypoints());
                else
                    sender.sendMessage(SHWUtilsTextComponent.textComponentSuccessServer("commands.shw.server.error.limit"));
                break;
            default:
                help(sender, command, configuration);
                break;
        }
        if (sender instanceof EntityPlayer && args.length > 1) {
            ITextComponent informationTC = new TextComponentTranslation("commands.shw.info.configuration")
                    .setStyle(new Style()
                            .setColor(TextFormatting.GRAY)
                            .setItalic(true));
            sender.sendMessage(informationTC);
        }
    }

    private static void cooldownInfo(ICommandSender sender, String command, int cooldownValue) {
        sender.sendMessage(SHWUtilsTextComponent.textComponentSuccessServer("commands.shw.server.info.cooldown",
                command, cooldownValue));
    }

    private static void limitInfo(ICommandSender sender, String command, int limitValue) {
        if (sender instanceof MinecraftServer)
            sender.sendMessage(SHWUtilsTextComponent.textComponentSuccessServer("commands.shw.server.info.limit",
                    command, limitValue));
        else
            sender.sendMessage(SHWUtilsTextComponent.textComponentSuccessServer("commands.shw.wp.limit",
                    limitValue));
    }

    private static void travelThroughDimensionInfo(ICommandSender sender, String command, boolean travelThroughDimensionValue) {
        sender.sendMessage(SHWUtilsTextComponent.textComponentSuccessServer("commands.shw.server.info.travelThroughDimension",
                command, travelThroughDimensionValue));
    }

    private static void cooldown(MinecraftServer server, String[] args, String command, ICommandConfiguration configuration) throws WrongUsageException {
        if (args.length == 1) {
            cooldownInfo(server, command, configuration.getCooldown());
        } else {
            String number = args[1];
            if (number.length() > SHWUtilsCommand.MAX_DIGITS)
                throw new WrongUsageException("commands.shw.server.error.length", SHWUtilsCommand.cooldown, SHWUtilsCommand.MAX_DIGITS);
            if (number.startsWith("-"))
                throw new WrongUsageException("commands.shw.server.error.negative", SHWUtilsCommand.cooldown);
            if (number.matches(String.format("^[0-9]{1,%d}$", SHWUtilsCommand.MAX_DIGITS))) {
                int cooldown = Integer.parseInt(number);
                configuration.setCooldown(cooldown);
                ConfigManager.sync(Reference.MODID, Config.Type.INSTANCE);
                server.getPlayerList().sendMessage(SHWUtilsTextComponent.textComponentSuccessServer("commands.shw.server.success.cooldown", command, cooldown));
            } else {
                throw new WrongUsageException("commands.shw.server.error.type", SHWUtilsCommand.cooldown,
                        new TextComponentTranslation("commands.shw.number"));
            }
        }
    }

    private static void limit(MinecraftServer server, String[] args, String command, ICommandConfiguration configuration) throws WrongUsageException {
        if (args.length == 1) {
            limitInfo(server, command, configuration.getMaxWaypoints());
        } else {
            String number = args[1];
            if (number.length() > SHWUtilsCommand.MAX_DIGITS)
                throw new WrongUsageException("commands.shw.server.error.length", SHWUtilsCommand.limit, SHWUtilsCommand.MAX_DIGITS);
            if (number.startsWith("-"))
                throw new WrongUsageException("commands.shw.server.error.negative", SHWUtilsCommand.limit);
            if (number.matches(String.format("^[0-9]{1,%d}$", SHWUtilsCommand.MAX_DIGITS))) {
                int limit = Integer.parseInt(number);
                configuration.setMaxWaypoints(limit);
                ConfigManager.sync(Reference.MODID, Config.Type.INSTANCE);
                server.getPlayerList().sendMessage(SHWUtilsTextComponent.textComponentSuccessServer("commands.shw.server.success.limit", command, limit));
            } else {
                throw new WrongUsageException("commands.shw.server.error.type", SHWUtilsCommand.limit,
                        new TextComponentTranslation("commands.shw.number"));
            }
        }
    }

    private static void travelThroughDimension(MinecraftServer server, String[] args, String command, ICommandConfiguration configuration) throws WrongUsageException {
        if (args.length == 1) {
            travelThroughDimensionInfo(server, command, configuration.isTravelThroughDimension());
        } else {
            String bool = args[1];
            if (Arrays.asList("true", "false").contains(bool.toLowerCase())) {
                boolean tTD = Boolean.parseBoolean(bool);
                configuration.setTravelThroughDimension(tTD);
                ConfigManager.sync(Reference.MODID, Config.Type.INSTANCE);
                server.getPlayerList().sendMessage(SHWUtilsTextComponent.textComponentSuccessServer("commands.shw.server.success.travelThroughDimension", command, tTD));
            } else {
                throw new WrongUsageException("commands.shw.server.error.type", SHWUtilsCommand.travelThroughDimension,
                        new TextComponentTranslation("commands.shw.boolean"));
            }
        }
    }

    private static void help(ICommandSender sender, String command, ICommandConfiguration configuration) {
        if (sender instanceof MinecraftServer) {
            String borderName = String.format(" | Configuration of %s", command.toUpperCase());
            ITextComponent cooldown = new TextComponentTranslation("commands.shw.server.usage",
                    command, SHWUtilsCommand.cooldown, new TextComponentTranslation("commands.shw.number"));

            ITextComponent travelThroughDimension = new TextComponentTranslation("commands.shw.server.usage",
                    command, SHWUtilsCommand.travelThroughDimension, new TextComponentTranslation("commands.shw.boolean"));

            sender.sendMessage(SHWUtilsTextComponent.getBorder(false, borderName));
            sender.sendMessage(cooldown);
            if (!(configuration instanceof SHWConfiguration.HomeConfiguration)) {
                ITextComponent limit = new TextComponentTranslation("commands.shw.server.usage",
                        command, SHWUtilsCommand.limit, new TextComponentTranslation("commands.shw.number"));
                sender.sendMessage(limit);
            }
            sender.sendMessage(travelThroughDimension);
            sender.sendMessage(SHWUtilsTextComponent.getBorder(false, borderName));
        } else if (sender instanceof EntityPlayer) {
            ITextComponent listCommandTC = new TextComponentTranslation("commands.shw.usage",
                    command, SHWUtilsTextComponent.stringsToTextComponent(", ", "[ ", " ]", TextFormatting.GOLD, commandArgs));
            sender.sendMessage(listCommandTC);
        }
    }
}
