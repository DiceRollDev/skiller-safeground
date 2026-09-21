package com.community.skillersafeground;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;

public class SkillerSafeGroundOverlay extends Overlay
{
    private final Client client;
    private final SkillerSafeGroundPlugin plugin;
    private final SkillerSafeGroundConfig config;
    private final PanelComponent panelComponent = new PanelComponent();

    @Inject
    public SkillerSafeGroundOverlay(Client client, SkillerSafeGroundPlugin plugin, SkillerSafeGroundConfig config)
    {
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        setPosition(OverlayPosition.TOP_LEFT);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        panelComponent.getChildren().clear();
        if (client.getLocalPlayer() == null) return null;

        WorldPoint playerLoc = client.getLocalPlayer().getWorldLocation();
        boolean isCurrentlySafe = true;

        // LAYER 1: Threat Firewalls
        for (NPC npc : plugin.getDangerousNpcs())
        {
            WorldPoint npcLoc = npc.getWorldLocation();
            if (npcLoc == null) continue;

            int distance = playerLoc.distanceTo(npcLoc);
            if (distance <= 5)
            {
                isCurrentlySafe = false;
            }

            if (config.showFirewallTiles())
            {
                int radius = 5;
                for (int dx = -radius; dx <= radius; dx++)
                {
                    for (int dy = -radius; dy <= radius; dy++)
                    {
                        WorldPoint targetTile = new WorldPoint(npcLoc.getX() + dx, npcLoc.getY() + dy, npcLoc.getPlane());
                        LocalPoint lp = LocalPoint.fromWorld(client, targetTile);
                        if (lp != null)
                        {
                            java.awt.Polygon poly = Perspective.getCanvasTilePoly(client, lp);
                            if (poly != null)
                            {
                                graphics.setColor(config.firewallColor());
                                graphics.fillPolygon(poly);
                            }
                        }
                    }
                }
            }

            // Universal Overhead Metronome Calculation Engine
            if (config.enableGlobalMetronome())
            {
                int currentTicks = plugin.getNpcMetronomeTimers().getOrDefault(npc, 4);
                String displayString = "ATTACK IN: " + currentTicks + " Ticks";
                if (config.showDistanceHUD())
                {
                    displayString += " | Dist: " + distance;
                }

                Point textPoint = npc.getCanvasTextLocation(graphics, displayString, npc.getLogicalHeight() + 30);
                if (textPoint != null)
                {
                    graphics.setFont(new Font("Arial", Font.BOLD, 12));
                    graphics.setColor(currentTicks <= 1 ? Color.RED : Color.YELLOW);
                    graphics.drawString(displayString, textPoint.getX(), textPoint.getY());
                }
            }
        }

        // LAYER 2: Milestone Quest Target Outlines
        if (config.trackQuestNPCs())
        {
            for (NPC npc : plugin.getQuestNpcs())
            {
                WorldPoint npcLoc = npc.getWorldLocation();
                if (npcLoc == null) continue;

                LocalPoint lp = LocalPoint.fromWorld(client, npcLoc);
                if (lp != null)
                {
                    java.awt.Polygon poly = Perspective.getCanvasTilePoly(client, lp);
                    if (poly != null)
                    {
                        graphics.setColor(config.questNpcColor());
                        graphics.fillPolygon(poly);
                    }
                }
            }
        }

        // LAYER 3: Translucent True-Tile Layer Rendering
        if (config.showTrueTile())
        {
            LocalPoint playerLocal = LocalPoint.fromWorld(client, playerLoc);
            if (playerLocal != null)
            {
                java.awt.Polygon playerPoly = Perspective.getCanvasTilePoly(client, playerLocal);
                if (playerPoly != null)
                {
                    graphics.setColor(isCurrentlySafe ? config.safeTileColor() : config.unsafeTileColor());
                    graphics.fillPolygon(playerPoly);
                    graphics.drawPolygon(playerPoly);
                }
            }
        }

        // LAYER 4: Dashboard Output Panels
        if (config.showLogoutPanel())
        {
            Font originalFont = graphics.getFont();
            float scalingFactor = config.panelFontSize() / 100f;
            Font scaledFont = originalFont.deriveFont(originalFont.getSize() * scalingFactor);
            graphics.setFont(scaledFont);

            panelComponent.getChildren().add(LineComponent.builder()
                .left("Security Profile:")
                .right(isCurrentlySafe ? "SAFE TO LOGOUT" : "IN THREAT BOUNDARY")
                .rightColor(isCurrentlySafe ? Color.GREEN : Color.RED)
                .build());

            Dimension dimensions = panelComponent.render(graphics);
            graphics.setFont(originalFont);
            return dimensions;
        }

        return null;
    }
}
