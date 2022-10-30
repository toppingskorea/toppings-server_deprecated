package com.toppings.server.domain.user.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toppings.common.constants.ResponseCode;
import com.toppings.common.exception.GeneralException;
import com.toppings.server.domain.user.dto.AdminRegisterRequest;
import com.toppings.server.domain.user.dto.AdminResponse;
import com.toppings.server.domain.user.entity.User;
import com.toppings.server.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	/**
	 * 관리자 등록
	 * @param adminRegisterRequest
	 * @param principalId
	 * @return Boolean
	 */
	@Transactional
	public Boolean insertAdmin(
		AdminRegisterRequest adminRegisterRequest,
		Long principalId
	) {
		User user = getUserByAccount(adminRegisterRequest);
		if (isDuplicatedUser(user))
			throw new GeneralException(ResponseCode.DUPLICATED_USER);

		if (isDeletedUser(user)) {
			setAdminInfo(adminRegisterRequest, principalId, user);
			initAdminInfo(user);
		} else {
			User admin = AdminRegisterRequest.dtoToEntity(adminRegisterRequest);
			setAdminInfo(adminRegisterRequest, principalId, admin);
			userRepository.save(admin);
		}
		return true;
	}

	private void initAdminInfo(User user) {
		user.setDeleteYn("N");
		user.setUpdateUserId(null);
		user.setRefreshToken(null);
	}

	private void setAdminInfo(
		AdminRegisterRequest adminRegisterRequest,
		Long principalId,
		User user
	) {
		user.setName(adminRegisterRequest.getName());
		user.setPassword(passwordEncoder.encode(adminRegisterRequest.getPassword()));
		user.setCreateUserId(principalId);
	}

	private boolean isDeletedUser(User user) {
		return user != null && user.getDeleteYn().equals("Y");
	}

	private boolean isDuplicatedUser(User user) {
		return user != null && user.getDeleteYn().equals("N");
	}

	private User getUserByAccount(AdminRegisterRequest adminRegisterRequest) {
		return userRepository.findUserByUsername(adminRegisterRequest.getUsername()).orElse(null);
	}

	/**
	 * 관리자 삭제
	 * @param id
	 * @param principalId
	 * @return Boolean
	 */
	@Transactional
	public Boolean deleteAdmin(
		Long id,
		Long principalId
	) {
		User user = getUserByUserId(id);
		user.setDeleteYn("Y");
		user.setUpdateUserId(principalId);
		return true;
	}

	private User getUserByUserId(Long id) {
		return userRepository.findUserByUserIdAndDeleteYn(id, "N")
			.orElseThrow(() -> new GeneralException(ResponseCode.BAD_REQUEST));
	}

	/**
	 * 관리자 목록 조회
	 * @return List<AdminResponse>
	 */
	public List<AdminResponse> getAdminAll() {
		return userRepository.findAdminAll();
	}
}
