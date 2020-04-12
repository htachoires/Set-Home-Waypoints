package fr.dodge.shw.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandSetHome extends CommandBase {

    public static final String prefix = "h-";
    public static final String prefixDate = "date-h";

    @Override
    public String getName() {
        return "sethome";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.shw.sethome.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (sender instanceof MinecraftServer) {
            throw new CommandException("commands.shw.sethome.errorServer");
        }
        if (sender instanceof EntityPlayer) {
            if (args.length > 0) throw new WrongUsageException("commands.shw.sethome.usage");

            SHWWorldSavedData.setString((EntityPlayer) sender, server, prefix + "home", SHWUtilsCommand.getPositionPlayer((EntityPlayer) sender));
            sender.sendMessage(SHWUtilsTextComponent.textComponentSuccess("commands.shw.sethome.success"));
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
