package com.example.oldfashioned.form;

import com.example.oldfashioned.entity.User;

import lombok.Data;

@Data
public class FollowForm {
	private User userId;
	
	private User followId;
}
