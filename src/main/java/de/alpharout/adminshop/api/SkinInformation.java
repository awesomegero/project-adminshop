package de.alpharout.adminshop.api;

import de.alpharout.adminshop.AdminShop;
import org.bukkit.Bukkit;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class SkinInformation {
    /*
    loadFromURL code from the Citizens2 repo (NPCCommands, skin function)
     */
    public static SkinInformation loadFromURL(String url) {
        DataOutputStream out;
        BufferedReader reader;
        try {
            URL target = new URL("https://api.mineskin.org/generate/url");
            HttpURLConnection con = (HttpURLConnection) target.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setConnectTimeout(1000);
            con.setReadTimeout(30000);
            out = new DataOutputStream(con.getOutputStream());
            out.writeBytes("url=" + URLEncoder.encode(url, StandardCharsets.UTF_8));
            out.close();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            JSONObject output = (JSONObject) new JSONParser().parse(reader);
            JSONObject data = (JSONObject) output.get("data");
            String uuid = (String) data.get("uuid");
            JSONObject texture = (JSONObject) data.get("texture");
            String textureEncoded = (String) texture.get("value");
            String signature = (String) texture.get("signature");
            con.disconnect();

            return new SkinInformation(uuid, signature, textureEncoded);
        } catch (IOException | ParseException e) {
            return null;
        }
    }

    private final String skinName;
    private final String textureSignature;
    private final String textureValue;

    /*
    These three values are necesary to load a skin from mineskin.org.
    Citizens supports them natively.
     */
    public SkinInformation(String skinName, String textureSignature, String textureValue) {
        this.skinName = skinName;
        this.textureSignature = textureSignature;
        this.textureValue = textureValue;
    }

    public String getSkinName() {
        return skinName;
    }

    public String getTextureSignature() {
        return textureSignature;
    }

    public String getTextureValue() {
        return textureValue;
    }
}
