package com.community.skillersafeground;

import com.google.inject.Provides;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
    name = "Skiller SafeGround",
    description = "A comprehensive visual security grid mapping all F2P hostile firewalls, universal overhead metronomes, and true tiles.",
    tags = {"skiller", "level3", "metronome", "safe", "overlay"},
    enabledByDefault = true
)
public class SkillerSafeGroundPlugin extends Plugin
{
    @Inject
    private Client client;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private SkillerSafeGroundOverlay overlay;

    @Inject
    private SkillerSafeGroundConfig config;

    private final Set<NPC> dangerousNpcs = new HashSet<>();
    private final Set<NPC> questNpcs = new HashSet<>();
    private final Map<NPC, Integer> npcMetronomeTimers = new HashMap<>();

    @Override
    protected void startUp() throws Exception
    {
        overlayManager.add(overlay);
        clearTrackingCache();
    }

    @Override
    protected void shutDown() throws Exception
    {
        overlayManager.remove(overlay);
        clearTrackingCache();
    }

    private void clearTrackingCache()
    {
        dangerousNpcs.clear();
        questNpcs.clear();
        npcMetronomeTimers.clear();
    }

    @Subscribe
    public void onNpcSpawned(NpcSpawned event)
    {
        NPC npc = event.getNpc();
        String name = npc.getName();
        if (name == null) return;

        boolean isThreat = false;

        // Comprehensive F2P Monster Array Sorting
        if ((name.equalsIgnoreCase("Dark wizard") || name.equalsIgnoreCase("Monk of zamorak")) && config.trackWizards()) isThreat = true;
        else if ((name.equalsIgnoreCase("Jail guard") || name.equalsIgnoreCase("Highwayman") || name.equalsIgnoreCase("Mugger")) && config.trackGuards()) isThreat = true;
        else if (name.toLowerCase().contains("scorpion") && config.trackScorpions()) isThreat = true;
        else if ((name.equalsIgnoreCase("Ice warrior") || name.equalsIgnoreCase("Ice giant")) && config.trackIceDungeon()) isThreat = true;
        else if ((name.equalsIgnoreCase("Hill giant") || name.equalsIgnoreCase("Moss giant") || name.equalsIgnoreCase("Deadly red spider")) && config.trackGiantsSpiders()) isThreat = true;
        else if ((name.equalsIgnoreCase("Minotaur") || name.equalsIgnoreCase("Flesh crawler") || name.equalsIgnoreCase("Catablepon") || name.equalsIgnoreCase("Ankou")) && config.trackStronghold()) isThreat = true;
        else if ((name.equalsIgnoreCase("Lesser demon") || name.equalsIgnoreCase("Bandit") || name.equalsIgnoreCase("Chaos dwarf") || name.equalsIgnoreCase("Grizzly bear") || name.equalsIgnoreCase("Ghost") || name.equalsIgnoreCase("Skeleton") || name.equalsIgnoreCase("Zombie") || name.equalsIgnoreCase("Hobgoblin")) && config.trackDeepWildy()) isThreat = true;
        else if (name.equalsIgnoreCase("Ancient guardian")) isThreat = true;

        if (isThreat)
        {
            dangerousNpcs.add(npc);
            npcMetronomeTimers.put(npc, getBaseAttackSpeed(name));
        }
        else if (config.trackQuestNPCs() && 
            (name.equalsIgnoreCase("Willow") || name.equalsIgnoreCase("Ramarno") || 
             name.equalsIgnoreCase("Checkal") || name.equalsIgnoreCase("Marley") || 
             name.equalsIgnoreCase("Aubury")))
        {
            questNpcs.add(npc);
        }
    }

    @Subscribe
    public void onNpcDespawned(NpcDespawned event)
    {
        NPC npc = event.getNpc();
        dangerousNpcs.remove(npc);
        questNpcs.remove(npc);
        npcMetronomeTimers.remove(npc);
    }

    @Subscribe
    public void onGameTick(GameTick event)
    {
        if (!config.enableGlobalMetronome()) return;

        for (NPC npc : dangerousNpcs)
        {
            int currentTicks = npcMetronomeTimers.getOrDefault(npc, 4);
            currentTicks--;
            
            if (currentTicks <= 0)
            {
                currentTicks = getBaseAttackSpeed(npc.getName());
            }
            npcMetronomeTimers.put(npc, currentTicks);
        }
    }

    private int getBaseAttackSpeed(String npcName)
    {
        if (npcName == null) return 4;
        String lower = npcName.toLowerCase();
        
        // 5-Tick heavy weapon speeds
        if (lower.contains("giant") || lower.contains("wizard") || lower.contains("guardian") || lower.contains("guard") || lower.contains("catablepon")) return 5;
        // 4-Tick rapid melee weapon speeds (Scorpions, Spiders, Crawlers, Demons, Ankous)
        return 4;
    }

    public Set<NPC> getDangerousNpcs() { return dangerousNpcs; }
    public Set<NPC> getQuestNpcs() { return questNpcs; }
    public Map<NPC, Integer> getNpcMetronomeTimers() { return npcMetronomeTimers; }

    @Provides
    SkillerSafeGroundConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(SkillerSafeGroundConfig.class);
    }
}
