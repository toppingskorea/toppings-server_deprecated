package com.toppings.server.domain.recent.service;

import com.toppings.common.constants.ResponseCode;
import com.toppings.common.exception.GeneralException;
import com.toppings.server.domain.recent.constant.RecentType;
import com.toppings.server.domain.recent.dto.RecentRequest;
import com.toppings.server.domain.recent.dto.RecentResponse;
import com.toppings.server.domain.recent.entity.Recent;
import com.toppings.server.domain.recent.repository.RecentRepository;
import com.toppings.server.domain.user.entity.User;
import com.toppings.server.domain.user.repository.UserRepository;
import com.toppings.server.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecentService {

    private final RecentRepository recentRepository;

    private final UserRepository userRepository;

    @Transactional
    public Boolean register(RecentRequest recentRequest, Long id) {
        User user = getUserById(id);
        recentRepository.findRecentByKeywordAndUser(recentRequest.getKeyword(), user)
                .ifPresent(recentRepository::delete);

        Recent saveRecent = RecentRequest.dtoToEntity(recentRequest);
        saveRecent.setUser(user);
        recentRepository.save(saveRecent);
        return true;
    }

    private User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new GeneralException(ResponseCode.BAD_REQUEST));
    }

    public List<RecentResponse> getRecents(RecentType type, Long id) {
        return recentRepository.getRecents(type, id);
    }

    @Transactional
    public Boolean removeOneRecent(Long id) {
        recentRepository.deleteById(id);
        return true;
    }

    @Transactional
    public Boolean removeAllRecent(Long id) {
        User user = getUserById(id);
        recentRepository.deleteAllByUser(user);
        return true;
    }
}
