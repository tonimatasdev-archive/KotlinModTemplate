package example.example.example

import example.example.example.block.ModBlocks
import net.minecraft.client.Minecraft
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent
import org.apache.logging.log4j.LogManager
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.runForDist

@Mod(ExampleMod.ID)
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
object ExampleMod {
    const val ID = "example"
    
    val LOGGER = LogManager.getLogger(ID)!!
    
    init {
        LOGGER.info("Initializing test mod...")
        
        ModBlocks.REGISTRY.register(MOD_BUS)
        
        val obj = runForDist(
            clientTarget = {
                MOD_BUS.addListener(ExampleMod::onClientSetup)
                Minecraft.getInstance()
            },
            serverTarget = {
                MOD_BUS.addListener(ExampleMod::onServerSetup)
                "test"
            }
        )
        
        MOD_BUS.addListener(::onCommonSetup)
        
        println(obj)
    }

    private fun onClientSetup(event: FMLClientSetupEvent) {
        LOGGER.info("Initializing client...")
    }

    /**
     * Fired on the global Forge bus.
     */
    private fun onServerSetup(event: FMLDedicatedServerSetupEvent) {
        LOGGER.info("Server starting...")
    }

    @SubscribeEvent
    fun onCommonSetup(event: FMLCommonSetupEvent) {
        LOGGER.info("Hello! This is working!")
    }
}