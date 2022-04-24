package com.adamlewandowski.Discord_Bot.service;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

@Service
public class RankPngGenerator {
    private String path = "src/main/resources";
    private String defaultFile = "/discord_user_rank.png";
    private String editedFile = "/discord_user_rank_edited.png";
    private File f = new File(path);
    private String absolutePath = f.getAbsolutePath();
    private File file = new File(absolutePath + defaultFile);
    private File fileEdited = new File(absolutePath + editedFile);
    private Font fontDetail = new Font("Arial", Font.CENTER_BASELINE, 12);
    private Font fontHead = new Font("Arial", Font.CENTER_BASELINE, 20);

    public File loadImageAndAddText(String name, String avatarUrl, Long rank, Integer points) throws IOException {
        BufferedImage avatarBufferedImage = ImageIO.read(new URL(avatarUrl));
        BufferedImage avatarRounded = makeRoundedAvatar(avatarBufferedImage);
        Image avatar = avatarRounded.getScaledInstance(110, 110, 0);

        BufferedImage bufferedImage = ImageIO.read(file);
        Graphics g = bufferedImage.getGraphics();
        g.setFont(fontHead);
        g.setColor(Color.BLACK);
        g.drawString(String.format("%s", name), 160, 30);
        g.drawImage(avatar, 25, 25, null);

        g.setFont(fontDetail);
        g.setColor(Color.GRAY);
        g.drawString(String.format("Rank: #%s", rank), 160, 60);
        g.drawString(String.format("Total: #%s", points), 160, 90);
        g.dispose();
        ImageIO.write(bufferedImage, "png", fileEdited);
        return fileEdited;
    }

    public static BufferedImage makeRoundedAvatar(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        int cornerRadius = 500;
        BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = output.createGraphics();

        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));

        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(image, 0, 0, null);
        g2.dispose();

        return output;
    }
}