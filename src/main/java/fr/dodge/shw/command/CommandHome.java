package fr.dodge.shw.command;

import fr.dodge.shw.config.SHWConfiguration;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CommandHome extends CommandBase {

    @Override
    public String getName() {
        return "home";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.shw.home.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (!(sender instanceof EntityPlayer)) throw new CommandException("commands.shw.error.sender");
        if (args.length > 0) throw new WrongUsageException("commands.shw.home.usage");

        String homePosition = SHWWorldSavedData.getString((EntityPlayer) sender, server, CommandSetHome.prefix + "home");

        if (!homePosition.isEmpty()) {
            long date = SHWWorldSavedData.getLong((EntityPlayer) sender, server, CommandSetHome.prefixDate);
            long cooldownRemaining = new Date().getTime() - date - TimeUnit.SECONDS.toMillis(SHWConfiguration.HOME.COOLDOWN);

            if (cooldownRemaining < 0) {
                throw new CommandException("commands.shw.error.cooldown",
                        TextComponentCustom.textComponentCooldown(Math.addExact(1, Math.abs(TimeUnit.MILLISECONDS.toSeconds(cooldownRemaining))), SHWConfiguration.HOME.COOLDOWN), "/home");
            } else {
                SHWWorldSavedData.setLong((EntityPlayer) sender, server, CommandSetHome.prefixDate, new Date().getTime());
                SHWUtilsCommand.teleportPlayer(server, (EntityPlayerMP) sender, homePosition);
                sender.sendMessage(new TextComponentTranslation("commands.shw.home.success"));
            }
        } else {
            throw new CommandException("commands.shw.home.error");
        }
    }

    /**
     * Check if the given ICommandSender has permission to execute this command
     */
    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

}
