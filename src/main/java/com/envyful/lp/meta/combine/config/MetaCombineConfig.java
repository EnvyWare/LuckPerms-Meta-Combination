package com.envyful.lp.meta.combine.config;

import com.envyful.api.config.data.ConfigPath;
import com.envyful.api.config.yaml.AbstractYamlConfig;
import com.envyful.api.config.yaml.data.YamlConfigStyle;
import com.google.common.collect.Lists;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.yaml.NodeStyle;

import java.util.List;

/**
 *
 * MetaCombine config that stores the list of metas that need to be combined for each user
 *
 */
@ConfigPath("config/lpmetacombine/config.yml")
@YamlConfigStyle(NodeStyle.BLOCK)
@ConfigSerializable
public class MetaCombineConfig extends AbstractYamlConfig {

    @Comment("The list of LuckPerm metas that will be added together when the player logs into the server")
    private List<String> metas = Lists.newArrayList();

    public MetaCombineConfig() {
        super();
    }

    public List<String> getMetas() {
        return this.metas;
    }
}
