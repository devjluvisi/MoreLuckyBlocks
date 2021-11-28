package devjluvisi.mlb.helper;

import devjluvisi.mlb.MoreLuckyBlocks;
import org.apache.commons.lang.StringUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;


public final class WebUtil {

    public static String getHTMLAsString(MoreLuckyBlocks plugin) {
        StringBuilder result = new StringBuilder("Unknown.");
        final String httpsUrl = "https://devjluvisi.github.io/MoreLuckyBlocks/";
        try {
            final URL url = new URL(httpsUrl);
            final HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

            final BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String input;
            result.setLength(0);
            while ((input = br.readLine()) != null){
                result.append(input);
            }
            br.close();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return result.toString();
    }

    public static String getLatestVersion(MoreLuckyBlocks plugin) {
        String input = getHTMLAsString(plugin);
        int index = StringUtils.indexOf(input, "id=\"ver\"");
        index++;
        StringBuilder version = new StringBuilder();
        while(input.charAt(index) != '<') {
            version.append(input.charAt(index));
            index++;
        }
        return version.toString();
    }

        /*
        Document doc = null;
        try {
            doc = Jsoup.connect("https://devjluvisi.github.io/MoreLuckyBlocks/").get();

        } catch (IOException e) {
            plugin.getLogger().info(e.getMessage());
        }
        if(doc == null) {
            return "Could not parse.";
        }
        return doc.title();

         */


}
