package devjluvisi.mlb.helper;

import devjluvisi.mlb.MoreLuckyBlocks;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberRange;
import org.apache.commons.lang.math.NumberUtils;

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
            plugin.getLogger().warning("Could not grab latest version for MoreLuckyBlocks. MalformedUrlException");
        } catch (IOException e) {
            plugin.getLogger().warning("Could not grab latest version for MoreLuckyBlocks. IOException");
        }
            return result.toString();

    }

    private static final String SEARCH_STRING = "id=\"ver\"";

    public static String getLatestVersion(MoreLuckyBlocks plugin) {
        String input = getHTMLAsString(plugin);
        int index = StringUtils.indexOf(input, SEARCH_STRING);
        index += SEARCH_STRING.length() + 1; // +1 for ending '>'
        StringBuilder version = new StringBuilder();
        while(input.charAt(index) != '<') {
            version.append(input.charAt(index));
            index++;
        }
        return version.toString().trim();
    }

    public static int getLatestVersionAsBuildInteger(MoreLuckyBlocks plugin) {
        String input = getLatestVersion(plugin);
        input = StringUtils.substring(input, 0, input.indexOf("-"));
        input = StringUtils.replace(input, ".", StringUtils.EMPTY);
        return NumberUtils.toInt(input, -1);
    }


}
