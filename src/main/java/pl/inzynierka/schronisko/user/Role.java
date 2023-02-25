package pl.inzynierka.schronisko.user;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
	USER(names.USER),
	ADMIN(names.ADMIN),
	MODERATOR(names.MODERATOR);

	Role(final String name) {
		this.name = name;
	}

	private final String name;

	@Override
	public String getAuthority() {
		return name();
	}

	public enum names {
		;
		public static final String USER = "ROLE_USER";
		public static final String ADMIN = "ROLE_ADMIN";
		public static final String MODERATOR = "ROLE_MODERATOR";
	}
}
