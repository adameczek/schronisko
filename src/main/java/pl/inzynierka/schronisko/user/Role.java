package pl.inzynierka.schronisko.user;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER(names.USER), ADMIN(names.ADMIN), MODERATOR(names.MODERATOR), SHELTER_OWNER(names.SHELTER_OWNER);
    
    Role(String name) {
    }
    
    @Override
    public String getAuthority() {
        return this.name();
    }
    
    public enum names {
        ;
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String MODERATOR = "ROLE_MODERATOR";
        public static final String SHELTER_OWNER = "SHELTER_OWNER";
    }
}
