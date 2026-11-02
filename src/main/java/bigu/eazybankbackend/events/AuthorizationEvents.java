package bigu.eazybankbackend.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthorizationEvents {

    @EventListener
    public  void onFailure(AuthorizationDeniedEvent deniedEvent){
        log.error("The Authorization failed for the user : {} , due to : {}",
                deniedEvent.getAuthentication().get().getName(), deniedEvent.getAuthorizationResult());
    }
}
