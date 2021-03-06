package com.envyful.lp.meta.combine.listener;

import com.envyful.api.forge.listener.LazyListener;
import com.envyful.lp.meta.combine.LPMetaCombine;
import com.envyful.lp.meta.combine.LuckPermsMetaFactory;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

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
        LuckPermsMetaFactory.handlePlayerMeta(event.player.getUniqueID(), this.mod.getConfig(), this.luckPerms);
    }
}
