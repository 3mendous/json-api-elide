package ru.sbt.qa;

import com.yahoo.elide.Elide;
import com.yahoo.elide.ElideSettingsBuilder;
import com.yahoo.elide.audit.Slf4jLogger;
import com.yahoo.elide.datastores.hibernate5.AbstractHibernateStore;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.util.Map;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(value = "/test", produces = APPLICATION_JSON_VALUE)
public class Controller {

    private MultivaluedMap<String, String> fromMap(final Map<String, String> input) {
        return new MultivaluedHashMap<String, String>(input);
    }

    @RequestMapping(method = GET)
    public String jsonApiGetAll(final HttpServletRequest request) {
        return elideRunner(
                request,
                (elide, path) -> elide.get(path, new MultivaluedHashMap<>(), new Object()).getBody());
    }

    @RequestMapping(value = "/{id}", method = GET)
    public String jsonApiGetOne(final HttpServletRequest request) {
        return elideRunner(
                request,
                (elide, path) -> elide.get(path, new MultivaluedHashMap<>(), new Object()).getBody());
    }

    @RequestMapping(method = POST)
    public String jsonApiPost(@RequestBody String json, final HttpServletRequest request) {
        return elideRunner(
                request,
                (elide, path) -> elide.post(path, json, new Object()).getBody());
    }

    @RequestMapping(value = "/{id}", method = PATCH)
    public String jsonApiPatch(@RequestBody String json, final HttpServletRequest request) {
        return elideRunner(
                request,
                (elide, path) -> elide.patch("test", null, path, json, new Object()).getBody());
    }

    @RequestMapping(value = "/{id}", method = DELETE)
    public String jsonApiDelete(final HttpServletRequest request) {
        return elideRunner(
                request,
                (elide, path) -> elide.delete(path, "", new Object()).getBody());
    }

    private String elideRunner(final HttpServletRequest request, final ElideCallable elideCallable) {
        //This gives the full path that was used to call this endpoint.
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);

        //Elide works with the Hibernate SessionFactory, not the JPA EntityManagerFactory.
        final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

        //Create the Elide object
        Elide elide = new Elide(new ElideSettingsBuilder(new AbstractHibernateStore.Builder(sessionFactory).build())
                .withAuditLogger(new Slf4jLogger())
                .build());
//
//        //We pass in the path, the params, and a place holder security object
//        //(which we won't be using here in any meaningful way, but can be used by Elide
//        //to authorize certain actions)
//        final ElideResponse response = elide.get(path, new MultivaluedHashMap<>(), new Object());

        //Return the JSON response to the client
        return elideCallable.call(elide, path);
    }
}
