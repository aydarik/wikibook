package ru.gumerbaev.model;

import io.micronaut.cache.annotation.CacheConfig;
import io.micronaut.cache.annotation.Cacheable;
import jakarta.inject.Singleton;
import org.jsoup.Jsoup;
import ru.gumerbaev.data.BookBox;
import ru.gumerbaev.data.Coordinate;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Singleton
@CacheConfig("boxes")
public class WikiProcessor {

    @Cacheable
    public List<BookBox> parse() throws IOException {
        var url = "https://de.m.wikipedia.org/wiki/Liste_öffentlicher_Bücherschränke_in_Berlin";
        var doc = Jsoup.connect(url).get();
        var list = doc.getElementsByTag("table").first().getElementsByTag("tbody").first().children();

        var coordPattern = Pattern.compile("(\\d{2}\\.\\d{4}).+(\\d{2}\\.\\d{4})");
        return list.parallelStream().map(it -> {
            var columns = it.children();
            var addrColumn = columns.get(5);
            var addrString = addrColumn.text()
                    .replace("⊙", "").replaceAll("\\[\\d+]", "")
                    .replaceAll("\\d+\\.+\\d+\\.+\\d+", "")
                    .replace("/", " / ").replace("  ", " ").trim();
            var coords = coordPattern.matcher(addrColumn.html());
            if (coords.find()) {
                var img = columns.get(0).getElementsByTag("img").first();
                var type = columns.get(1).text()
                        .replaceAll("\\[\\d+]", "")
                        .replace("/", " / ").replace("  ", " ").trim();
                var comment = columns.get(4).text()
                        .replaceAll("\\[\\d+]", "")
                        .replace("/", " / ").replace("  ", " ").trim();
                return new BookBox(addrString,
                        img != null ? img.attr("src") : null,
                        new Coordinate(Double.parseDouble(coords.group(1)), Double.parseDouble(coords.group(2))),
                        type.isBlank() ? null : type,
                        comment.isBlank() ? null : comment);
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }
}
