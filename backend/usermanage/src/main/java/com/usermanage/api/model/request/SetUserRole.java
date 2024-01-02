package com.usermanage.api.model.request;

import com.usermanage.api.model.pojo.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SetUserRole {
	private String username;
	private Role role;
}
