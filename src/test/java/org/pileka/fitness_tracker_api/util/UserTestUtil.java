package org.pileka.fitness_tracker_api.util;

import lombok.experimental.UtilityClass;
import org.pileka.fitness_tracker_api.domain.User;

@UtilityClass
public class UserTestUtil {
    public final String USERNAME = "username";
    public final String ANOTHER_USERNAME = "another username";
    public final String PASSWORD = "password";

    public final User testUser = new User(USERNAME, "e@mail.com", PASSWORD, null, null);
    public final User anotherTestUser = new User(ANOTHER_USERNAME, "another@email.com", "securepassword", null, null);
}
