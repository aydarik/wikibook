package ru.gumerbaev;

import io.micronaut.core.util.CollectionUtils;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.views.View;
import jakarta.inject.Inject;
import ru.gumerbaev.model.WikiProcessor;

import java.io.IOException;

@Controller("/berlin")
public class MapController {
    @Inject
    WikiProcessor processor;

    @View("map")
    @Get("/")
    public HttpResponse index() throws IOException {
        return HttpResponse.ok(CollectionUtils.mapOf("boxes", processor.parse()));
    }
}
