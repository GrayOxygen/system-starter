package com.shineoxygen.work.admin.model;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.mongodb.morphia.annotations.Entity;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.saysth.commons.utils.SystemConfig;
import com.shineoxygen.work.base.model.User;
import com.shineoxygen.work.base.model.enums.UserStatus;
import com.shineoxygen.work.base.utils.ResourceUtil;

@Document(collection = "AdminUser") // spring data生成collection
@Entity // querydsl根据该注解生成Q对象
public class AdminUser extends User {
	@Indexed
	private String userName;
	private UserStatus userStatus = UserStatus.NORMAL;
	private String phoneNum;
	private String email;
	private boolean buildin = false; // 系统内置用户标识, 如是系统内置用户不能删除
	private String nickName;// 昵称

	private String avatar;// 头像图片url

	public static AdminUser empty() {
		AdminUser user = new AdminUser();
		user.setUserStatus(null);
		user.setCtime(null);
		return user;
	}

	public static AdminUser empty(String userName) {
		new AdminUser();
		AdminUser user = AdminUser.empty();
		user.setUserName(userName);
		return user;
	}

	public static AdminUser emptyWizId(String id) {
		new AdminUser();
		AdminUser user = AdminUser.empty();
		user.setId(id);
		return user;
	}

	public UserStatus getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	@Override
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isBuildin() {
		return buildin;
	}

	public void setBuildin(boolean buildin) {
		this.buildin = buildin;
	}

	public String getAvatar() {
		if (StringUtils.isBlank(avatar)) {
			return "";
		}
		return avatar + "?v=" + new Date().getTime();
	}
	// public String getAvatar() {
	// if (StringUtils.isBlank(avatar)) {
	// return "";
	// }
	//
	// return StringUtils.replace(avatar,
	// SystemConfig.getProperty("static.bucket") + "@",
	// ResourceUtil.getStaticUrl() + ResourceUtil.getStaticImgRootUrl(), 1);
	// }

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

}
