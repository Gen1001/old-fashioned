package com.example.oldfashioned.form;

import com.example.oldfashioned.entity.Store;
import com.example.oldfashioned.entity.User;

import lombok.Data;

@Data
public class KeepForm {
	private Store storeId;
	
	private User userId;
}
