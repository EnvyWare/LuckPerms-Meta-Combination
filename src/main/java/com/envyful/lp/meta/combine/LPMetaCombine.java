package com.envyful.lp.meta.combine;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
        modid = "lpmetacombine",
        name = "LPMetaCombine",
        version = "0.0.1",
        acceptableRemoteVersions = "*",
        dependencies = "required-after:luckperms;"
)
public class LPMetaCombine {

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {

    }
}
