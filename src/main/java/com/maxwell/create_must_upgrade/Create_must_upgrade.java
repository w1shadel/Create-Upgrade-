package com.maxwell.create_must_upgrade;

import com.maxwell.create_must_upgrade.util.ReflectionHelper;
import com.maxwell.create_must_upgrade.Register.ModItems;
import com.maxwell.create_must_upgrade.Register.ModRegistries;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@SuppressWarnings("removal")
@Mod(Create_must_upgrade.MODID)
public class Create_must_upgrade
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "create_must_upgrade";
    public Create_must_upgrade(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();
        ModItems.register(modEventBus);
        ModRegistries.register(modEventBus);
        modEventBus.addListener(this::onCommonSetup);
    }
    private void onCommonSetup(final FMLCommonSetupEvent event) {
        ReflectionHelper.init();
        ModItems.initializeUpgradeItemMap();
    }
}
