package ru.gumerbaev.model;

import jakarta.inject.Singleton;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import ru.gumerbaev.data.BookBox;
import ru.gumerbaev.data.Coordinate;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Singleton
public class WikiProcessor {

    private static final Logger LOG = Logger.getLogger(WikiProcessor.class.getName());

    public List<BookBox> parse() throws IOException {
        var url = "https://de.m.wikipedia.org/wiki/Liste_öffentlicher_Bücherschränke_in_Berlin";
        var doc = Jsoup.connect(url).get();
        var list = doc.getElementsByTag("table").first().firstChild().childNodes();

        return list.stream().map(it -> {
            var addrColumn = it.lastChild().outerHtml();
            var addrString = Jsoup.parse(addrColumn).body().text().replace("⊙", "").trim();
            var coordPattern = Pattern.compile("(\\d{2}\\.\\d{4}).+(\\d{2}\\.\\d{4})");
            var coords = coordPattern.matcher(addrColumn);
            if (coords.find()) {
                Element img = Jsoup.parse(it.firstChild().outerHtml()).getElementsByTag("img").first();
                String type = null;
                String comment = null;
                if (it.childNodes().size() == 11) {
                    type = Jsoup.parse(it.childNode(2).outerHtml()).body().text().replaceAll("\\[\\d+\\]", "");
                    comment = Jsoup.parse(it.childNode(8).outerHtml()).body().text().replaceAll("\\[\\d+\\]", "");
                }
                return new BookBox(addrString,
                        img != null ? img.attr("src") : null,
                        new Coordinate(Double.parseDouble(coords.group(1)), Double.parseDouble(coords.group(2))),
                        type == null || type.isBlank() ? null : type,
                        comment == null || comment.isBlank() ? null : comment);
            }

            LOG.info(addrString);
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }
}
