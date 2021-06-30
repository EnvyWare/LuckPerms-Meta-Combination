package com.envyful.lp.meta.combine;

import com.envyful.api.concurrency.UtilConcurrency;
import com.envyful.lp.meta.combine.config.MetaCombineConfig;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.MetaNode;
import net.luckperms.api.query.QueryOptions;

import java.util.UUID;

public class LuckPermsMetaFactory {
    public static void handlePlayerMeta(UUID uuid, MetaCombineConfig config, LuckPerms luckPerms) {
        UtilConcurrency.runAsync(() -> {
            User user = luckPerms.getUserManager().getUser(uuid);

            for (String meta : config.getMetas()) {
                user.data().clear(NodeType.META.predicate(metaNode -> metaNode.getMetaKey().equals(meta)));
                int total = user.resolveInheritedNodes(NodeType.META, QueryOptions.nonContextual()).stream()
                        .filter(node -> meta.equals(node.getMetaKey()))
                        .mapToInt(metaNode -> {
                            try {
                                return Integer.parseInt(metaNode.getMetaValue());
                            } catch (NumberFormatException e) {
                                return 0;
                            }
                        }).sum();
                MetaNode node = MetaNode.builder(meta, Integer.toString(total)).build();

                user.data().clear(NodeType.META.predicate(metaNode -> metaNode.getMetaKey().equals(meta)));
                user.data().add(node);
                luckPerms.getUserManager().saveUser(user);
            }
        });
    }
}
