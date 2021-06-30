package com.envyful.lp.meta.combine.command;

import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.lp.meta.combine.LPMetaCombine;
import com.envyful.lp.meta.combine.LuckPermsMetaFactory;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;

@Command("lpmreload")
@Permissible("lpmeta.reload")
public class LPMetaReloadCommand {

    private final LPMetaCombine mod;
    private final LuckPerms luckPerms;

    public LPMetaReloadCommand(LPMetaCombine mod) {
        this.mod = mod;
        this.luckPerms = LuckPermsProvider.get();
    }

    @CommandProcessor
    public void run(@Sender ICommandSender sender) {
        this.mod.loadConfig();
        sender.sendMessage(new TextComponentString("Reloaded LPMeta config. Now updating players..."));

        for (EntityPlayerMP player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
            LuckPermsMetaFactory.handlePlayerMeta(player.getUniqueID(), this.mod.getConfig(), this.luckPerms);
        }

        sender.sendMessage(new TextComponentString("Online player meta update finished."));
    }
}
