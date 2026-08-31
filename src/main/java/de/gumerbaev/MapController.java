package de.gumerbaev;

import io.micronaut.core.util.CollectionUtils;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.views.View;
import jakarta.inject.Inject;
import de.gumerbaev.model.WikiProcessor;

import java.io.IOException;
import java.net.URI;

@Controller
public class MapController {
    @Inject
    WikiProcessor processor;

    @View("map")
    @Get("/berlin")
    public HttpResponse<?> berlin() throws IOException {
        return HttpResponse.ok(CollectionUtils.mapOf("boxes", processor.parse()));
    }

    @Get("/")
    public HttpResponse<?> root() {
        return HttpResponse.redirect(URI.create("/berlin"));
    }
}
