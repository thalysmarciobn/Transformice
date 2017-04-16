package com.transformice.network.server.messages.outgoing.screen;

import com.transformice.server.Server;
import com.transformice.helpers.network.ByteArray;
import com.transformice.network.server.messages.outgoing.MessageComposer;
import com.transformice.helpers.network.Outgoing;
import com.transformice.server.users.GameClient;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.FontMetrics;
import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;

public class DrawCaptcha extends MessageComposer {

    @Override
    public void compose(Server server, GameClient client, int packetID) {
        Font font = new Font("Consolab", Font.BOLD, 12);
        BufferedImage image = new BufferedImage(36, 17, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setFont(font);
        FontMetrics metrics = g2d.getFontMetrics();
        int charsWidth = metrics.charsWidth(client.currentCaptcha.toCharArray(), 0, 4);
        g2d.dispose();
        image.flush();
        image = new BufferedImage(charsWidth + 4, 17, BufferedImage.TYPE_INT_RGB);
        g2d = image.createGraphics();
        g2d.setFont(font);
        g2d.setBackground(Color.black);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        TextLayout textLayout = new TextLayout(client.currentCaptcha, font, g2d.getFontRenderContext());
        g2d.setPaint(new Color(205, 205, 205));
        textLayout.draw(g2d, 1, 13);
        textLayout.draw(g2d, 2, 13);
        textLayout.draw(g2d, 1, 14);
        textLayout.draw(g2d, 1, 12);
        textLayout.draw(g2d, 2, 14);
        textLayout.draw(g2d, 0, 12);
        textLayout.draw(g2d, 0, 14);
        textLayout.draw(g2d, 2, 12);
        g2d.setPaint(new Color(0, 0, 0));
        textLayout.draw(g2d, 1, 13);
        int width = image.getWidth();
        int height = image.getHeight();
        int pixelsCount = 0;
        ByteArray pixels = new ByteArray();
        ByteArray packet = new ByteArray();
        packet.writeShort(width);
        packet.writeShort(height);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                pixels.writeInt(new Color(image.getRGB(col, row)).getBlue());
                pixelsCount++;
            }
        }
        g2d.dispose();
        image.flush();
        packet.writeShort(pixelsCount);
        packet.writeByte(0);
        packet.writeBytes(pixels.toByteArray());
        client.sendPacket(Outgoing.captcha, packet.toByteArray());
    }
}
