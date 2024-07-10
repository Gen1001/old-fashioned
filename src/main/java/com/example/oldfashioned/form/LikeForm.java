package com.example.oldfashioned.form;

import com.example.oldfashioned.entity.Post;
import com.example.oldfashioned.entity.User;

import lombok.Data;

@Data
public class LikeForm {
	private Post postId;
	
	private User userId;
}
