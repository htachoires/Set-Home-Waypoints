package fr.dodge.shw.command;

import fr.dodge.shw.config.SHWConfiguration;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CommandHome extends CommandBase {

    @Override
    public String getName() {
        return "home";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return sender instanceof MinecraftServer ? "commands.shw.home.server.usage" : "commands.shw.home.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (sender instanceof MinecraftServer) {
            SHWUtilsCommand.manageConfiguration(server, sender, args, this.getName(), SHWConfiguration.HOME);
        } else if (sender instanceof EntityPlayer) {
            if (args.length >= 1) {
                SHWUtilsCommand.info(sender, args, this.getName(), SHWConfiguration.HOME);
            } else {
                String homePosition = SHWWorldSavedData.getString((EntityPlayer) sender, server, CommandSetHome.prefix + "home");

                if (!homePosition.isEmpty()) {
                    long date = SHWWorldSavedData.getLong((EntityPlayer) sender, server, CommandSetHome.prefixDate);
                    long cooldownRemaining = new Date().getTime() - date - TimeUnit.SECONDS.toMillis(SHWConfiguration.HOME.cooldown);

                    if (cooldownRemaining >= 0) {
                        SHWUtilsCommand.teleportPlayer(server, (EntityPlayerMP) sender, homePosition, SHWConfiguration.HOME.travelThroughDimension, this.getName());

                        SHWWorldSavedData.setLong((EntityPlayer) sender, server, CommandSetHome.prefixDate, new Date().getTime());
                        sender.sendMessage(new TextComponentTranslation("commands.shw.home.success"));
                    } else {
                        throw new CommandException("commands.shw.error.cooldown",
                                SHWUtilsTextComponent.textComponentCooldown(Math.addExact(1, Math.abs(TimeUnit.MILLISECONDS.toSeconds(cooldownRemaining))), SHWConfiguration.HOME.cooldown), "/home");
                    }
                } else {
                    throw new CommandException("commands.shw.home.error");
                }
            }
        }
    }

    /**
     * Check if the given ICommandSender has permission to execute this command
     */
    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, SHWUtilsCommand.commandArgs)
                : args.length == 2 && SHWUtilsCommand.travelThroughDimensionLC.equals(args[0].toLowerCase()) && sender instanceof MinecraftServer ? Arrays.asList("true", "false") :
                Collections.emptyList();
    }
}
