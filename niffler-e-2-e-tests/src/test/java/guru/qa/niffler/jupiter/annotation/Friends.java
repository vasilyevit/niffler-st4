package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.model.FriendState;

public @interface Friends {

    boolean pending() default false;

    FriendState friendState() default FriendState.INVITE_SENT;
}
