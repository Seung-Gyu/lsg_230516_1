package untitled.infra;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import untitled.domain.*;

@Component
public class RankHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<Rank>> {

    @Override
    public EntityModel<Rank> process(EntityModel<Rank> model) {
        return model;
    }
}
