package tool.stock.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tool.stock.constant.TwseDataset;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class TwseDownloadService {

    private static final String BASE_URL = "https://openapi.twse.com.tw/v1";

    @Value("${app.stock.base-path}")
    private String basePath;

    public Path download(TwseDataset dataset) {
        try {
            String today = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);

            Path dir = Paths.get(basePath);
            Files.createDirectories(dir);

            String fileName = today + "_" + dataset.getFilePrefix() + ".json";
            Path filePath = dir.resolve(fileName);

            String url = BASE_URL + dataset.getPath();

            byte[] bytes = readAllBytesWithRedirect(url, 5);
            String content = new String(bytes, StandardCharsets.UTF_8).trim();

            if (content.startsWith("<")) {
                String preview = content.substring(0, Math.min(300, content.length()));
                throw new RuntimeException("下載到的不是 JSON，疑似 HTML 頁面，url=" + url + "，內容開頭=" + preview);
            }

            Files.copy(new ByteArrayInputStream(bytes), filePath, StandardCopyOption.REPLACE_EXISTING);

            return filePath;

        } catch (Exception e) {
            throw new RuntimeException("下載失敗, url=" + BASE_URL + dataset.getPath(), e);
        }
    }

    private byte[] readAllBytesWithRedirect(String urlStr, int maxRedirect) throws Exception {
        if (maxRedirect <= 0) {
            throw new RuntimeException("Redirect 次數過多");
        }

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        conn.setRequestProperty("Referer", "https://openapi.twse.com.tw/");
        conn.setRequestProperty("Accept-Language", "zh-TW,zh;q=0.9,en;q=0.8");

        int status = conn.getResponseCode();

        if (status == HttpURLConnection.HTTP_OK) {
            try (InputStream in = conn.getInputStream();
                 ByteArrayOutputStream out = new ByteArrayOutputStream()) {

                byte[] buffer = new byte[4096];
                int len;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
                return out.toByteArray();
            }
        }

        if (status == HttpURLConnection.HTTP_MOVED_PERM
                || status == HttpURLConnection.HTTP_MOVED_TEMP
                || status == HttpURLConnection.HTTP_SEE_OTHER) {

            String newUrl = conn.getHeaderField("Location");
            if (newUrl == null || newUrl.isBlank()) {
                throw new RuntimeException("收到 redirect 但沒有 Location header, url=" + urlStr);
            }

            return readAllBytesWithRedirect(newUrl, maxRedirect - 1);
        }

        throw new RuntimeException("HTTP Status=" + status + ", url=" + urlStr);
    }
}