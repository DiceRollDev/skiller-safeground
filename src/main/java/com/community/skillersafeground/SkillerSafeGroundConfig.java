package com.community.skillersafeground;

import java.awt.Color;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("skillersafeground")
public interface SkillerSafeGroundConfig extends Config
{
    @ConfigSection(
        name = "Aggressive NPC Firewall",
        description = "Settings tracking hostile entities and threat perimeters.",
        position = 1
    )
    String npcSection = "npcSection";

    @ConfigItem(
        keyName = "showFirewallTiles",
        name = "Enable Danger Radius",
        description = "Draws red danger grid layouts inside threat bounds.",
        position = 1,
        section = npcSection
    )
    default boolean showFirewallTiles() { return true; }

    @Alpha
    @ConfigItem(
        keyName = "firewallColor",
        name = "Danger Aura Color",
        description = "The color transparency for shaded threat zone tiles.",
        position = 2,
        section = npcSection
    )
    default Color firewallColor() { return new Color(255, 0, 0, 35); }

    @ConfigItem(
        keyName = "trackWizards",
        name = "Track Dark Wizards & Cultists",
        description = "Maps perimeter bounds for Varrock south gate casters and Zamorak monks.",
        position = 3,
        section = npcSection
    )
    default boolean trackWizards() { return true; }

    @ConfigItem(
        keyName = "trackGuards",
        name = "Track Jail Guards & Highwaymen",
        description = "Maps perimeter bounds for Draynor village area hostiles.",
        position = 4,
        section = npcSection
    )
    default boolean trackGuards() { return true; }

    @ConfigItem(
        keyName = "trackScorpions",
        name = "Track All Scorpions",
        description = "Maps perimeter bounds for Al Kharid, Wilderness, and Mine scorpions.",
        position = 5,
        section = npcSection
    )
    default boolean trackScorpions() { return true; }

    @ConfigItem(
        keyName = "trackIceDungeon",
        name = "Track Ice Giants & Warriors",
        description = "Maps lethal bounds inside the Asgarnian Ice Dungeon.",
        position = 6,
        section = npcSection
    )
    default boolean trackIceDungeon() { return true; }

    @ConfigItem(
        keyName = "trackGiantsSpiders",
        name = "Track Giants & Spiders",
        description = "Maps Hill/Moss Giants and Deadly Red Spiders in Sewers and Dungeons.",
        position = 7,
        section = npcSection
    )
    default boolean trackGiantsSpiders() { return true; }

    @ConfigItem(
        keyName = "trackStronghold",
        name = "Track Stronghold of Security",
        description = "Maps Minotaurs, Flesh Crawlers, Catablepons, and Ankous for safe 10k GP runs.",
        position = 8,
        section = npcSection
    )
    default boolean trackStronghold() { return true; }

    @ConfigItem(
        keyName = "trackDeepWildy",
        name = "Track Deep Wilderness Beasts",
        description = "Maps Lesser Demons, Bandits, Chaos Dwarves, Grizzly Bears, and Ghosts.",
        position = 9,
        section = npcSection
    )
    default boolean trackDeepWildy() { return true; }

    @ConfigSection(
        name = "Universal Attack Metronome",
        description = "Visual tick-countdowns synchronized to entity animation speeds.",
        position = 2
    )
    String metronomeSection = "metronomeSection";

    @ConfigItem(
        keyName = "enableGlobalMetronome",
        name = "Enable Overhead Ticks",
        description = "Renders text countdowns above active targets indicating when their next strike will fire.",
        position = 1,
        section = metronomeSection
    )
    default boolean enableGlobalMetronome() { return true; }

    @ConfigItem(
        keyName = "showDistanceHUD",
        name = "Show Distance In Metronome",
        description = "Appends your exact tile distance directly into the overhead text tracker.",
        position = 2,
        section = metronomeSection
    )
    default boolean showDistanceHUD() { return true; }

    @ConfigSection(
        name = "Quest & Skilling Utilities",
        description = "Markers highlighting non-aggressive milestone quest figures.",
        position = 3
    )
    String questSection = "questSection";

    @ConfigItem(
        keyName = "trackQuestNPCs",
        name = "Highlight Quest Targets",
        description = "Highlights vital F2P quest targets (Willow, Checkal, Marley, Ramarno, Aubury).",
        position = 1,
        section = questSection
    )
    default boolean trackQuestNPCs() { return true; }

    @Alpha
    @ConfigItem(
        keyName = "questNpcColor",
        name = "Quest Target Aura",
        description = "The color highlight used to anchor friendly quest lines.",
        position = 2,
        section = questSection
    )
    default Color questNpcColor() { return new Color(0, 191, 255, 45); }

    @ConfigSection(
        name = "Logout Security Dashboard",
        description = "Diagnostic text overlays and customized true-tile colors.",
        position = 4
    )
    String logoutSection = "logoutSection";

    @ConfigItem(
        keyName = "showLogoutPanel",
        name = "Enable Logout Panel",
        description = "Displays the diagnostic top-left HUD state monitor box.",
        position = 1,
        section = logoutSection
    )
    default boolean showLogoutPanel() { return true; }

    @ConfigItem(
        keyName = "showTrueTile",
        name = "Highlight True-Tile",
        description = "Renders your character real server-side ground point.",
        position = 2,
        section = logoutSection
    )
    default boolean showTrueTile() { return true; }

    @Alpha
    @ConfigItem(
        keyName = "safeTileColor",
        name = "Safe State Color",
        description = "The color of your true tile when outside aggro parameters.",
        position = 3,
        section = logoutSection
    )
    default Color safeTileColor() { return new Color(0, 255, 0, 75); }

    @Alpha
    @ConfigItem(
        keyName = "unsafeTileColor",
        name = "Unsafe State Color",
        description = "The color of your true tile when overlapping an enemy boundary grid.",
        position = 4,
        section = logoutSection
    )
    default Color unsafeTileColor() { return new Color(255, 0, 0, 115); }

    @ConfigItem(
        keyName = "panelFontSize",
        name = "Text Size Scale (%)",
        description = "Scales font metrics rendering inside the UI dashboard monitor.",
        position = 5,
        section = logoutSection
    )
    default int panelFontSize() { return 100; }
}
