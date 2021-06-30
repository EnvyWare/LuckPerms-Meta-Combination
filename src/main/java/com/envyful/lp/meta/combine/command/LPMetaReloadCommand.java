package com.envyful.lp.meta.combine.command;

import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.concurrency.UtilConcurrency;
import com.envyful.lp.meta.combine.LPMetaCombine;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.MetaNode;
import net.luckperms.api.query.QueryOptions;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.UUID;

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
            this.handlePlayerMeta(player.getUniqueID());
        }

        sender.sendMessage(new TextComponentString("Online player meta update finished."));
    }

    private void handlePlayerMeta(UUID uuid) {
        UtilConcurrency.runAsync(() -> {
            User user = this.luckPerms.getUserManager().getUser(uuid);

            for (String meta : this.mod.getConfig().getMetas()) {
                int total = user.resolveInheritedNodes(NodeType.META, QueryOptions.nonContextual()).stream()
                        .filter(node -> meta.equals(node.getMetaKey()))
                        .mapToInt(metaNode -> Integer.parseInt(metaNode.getMetaValue())).sum();
                MetaNode node = MetaNode.builder(meta, Integer.toString(total)).build();

                user.data().clear(NodeType.META.predicate(metaNode -> metaNode.getMetaKey().equals(meta)));
                user.data().add(node);
                this.luckPerms.getUserManager().saveUser(user);
            }
        });
    }
}
