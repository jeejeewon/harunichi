package com.harunichi.follow.service;

import java.util.Map;

public interface FollowService {
	Map<String, Object> getFollowList(String searchKeyword, int page);
}
