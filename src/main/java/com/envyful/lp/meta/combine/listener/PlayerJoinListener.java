package com.envyful.lp.meta.combine.listener;

import com.envyful.api.concurrency.UtilConcurrency;
import com.envyful.api.forge.listener.LazyListener;
import com.envyful.lp.meta.combine.config.MetaCombineConfig;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.MetaNode;
import net.luckperms.api.query.QueryOptions;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 *
 * Listener class that updates the player's meta once they join using the list of metas in the config.
 *
 */
public class PlayerJoinListener extends LazyListener {

    private final LuckPerms luckPerms;
    private final MetaCombineConfig config;

    public PlayerJoinListener(MetaCombineConfig config) {
        this.config = config;
        this.luckPerms = LuckPermsProvider.get();
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        UtilConcurrency.runAsync(() -> {
            User user = this.luckPerms.getUserManager().getUser(event.player.getUniqueID());
            for (String meta : this.config.getMetas()) {
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
