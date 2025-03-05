package com.olive.pribee.module.auth.domain.entity;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.olive.pribee.global.common.BaseTime;
import com.olive.pribee.global.enums.MemberRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Member extends BaseTime {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(unique = true)
	private String facebookId;

	@NotNull
	private String name;

	private String email;

	@NotNull
	private String profilePictureUrl;

	@NotNull
	@Enumerated(EnumType.STRING)
	private MemberRole role;

	public static Member of(@NotNull String facebookId,@NotNull String name, String email, @NotNull String profilePictureUrl) {
		return Member.builder()
			.facebookId(facebookId)
			.name(name)
			.email(email)
			.profilePictureUrl(profilePictureUrl)
			.role(MemberRole.USER)
			.build();
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(new SimpleGrantedAuthority(this.role.getCode()));
	}
}
