package com.envyful.lp.meta.combine.config;

import com.envyful.api.config.data.ConfigPath;
import com.envyful.api.config.yaml.AbstractYamlConfig;
import com.google.common.collect.Lists;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

/**
 *
 * MetaCombine config that stores the list of metas that need to be combined for each user
 *
 */
@ConfigPath("config/lpmetacombine/config.yml")
@ConfigSerializable
public class MetaCombineConfig extends AbstractYamlConfig {

    private List<String> metas = Lists.newArrayList();

    public MetaCombineConfig() {
        super();
    }

    public List<String> getMetas() {
        return this.metas;
    }
}
