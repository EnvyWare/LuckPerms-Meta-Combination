package com.envyful.lp.meta.combine;

import com.envyful.api.config.yaml.YamlConfigFactory;
import com.envyful.lp.meta.combine.config.MetaCombineConfig;
import com.envyful.lp.meta.combine.listener.PlayerJoinListener;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.io.IOException;

@Mod(
        modid = "lpmetacombine",
        name = "LPMetaCombine",
        version = "0.0.1",
        acceptableRemoteVersions = "*"
)
public class LPMetaCombine {

    private MetaCombineConfig config;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        this.loadConfig();
    }

    @Mod.EventHandler
    public void onServerStart(FMLServerStartingEvent event) {
        new PlayerJoinListener(this.config);
    }

    private void loadConfig() {
        try {
            this.config = YamlConfigFactory.getInstance(MetaCombineConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
