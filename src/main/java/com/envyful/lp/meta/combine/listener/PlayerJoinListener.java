package com.envyful.lp.meta.combine.listener;

import com.envyful.api.concurrency.UtilConcurrency;
import com.envyful.api.forge.listener.LazyListener;
import com.envyful.lp.meta.combine.LPMetaCombine;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.MetaNode;
import net.luckperms.api.query.QueryOptions;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.UUID;

/**
 *
 * Listener class that updates the player's meta once they join using the list of metas in the config.
 *
 */
public class PlayerJoinListener extends LazyListener {

    private final LPMetaCombine mod;
    private final LuckPerms luckPerms;

    public PlayerJoinListener(LPMetaCombine mod) {
        this.mod = mod;
        this.luckPerms = LuckPermsProvider.get();
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        this.handlePlayerMeta(event.player.getUniqueID());
    }

    private void handlePlayerMeta(UUID uuid) {
        UtilConcurrency.runAsync(() -> {
            User user = this.luckPerms.getUserManager().getUser(uuid);

            for (String meta : this.mod.getConfig().getMetas()) {
                user.data().clear(NodeType.META.predicate(metaNode -> metaNode.getMetaKey().equals(meta)));

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
