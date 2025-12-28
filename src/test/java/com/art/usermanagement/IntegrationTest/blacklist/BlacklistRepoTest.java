package com.art.usermanagement.IntegrationTest.blacklist;

import com.art.usermanagement.dto.request.BlacklistFilterParam;
import com.art.usermanagement.model.Blacklist;
import com.art.usermanagement.repository.blacklist.BlacklistRepo;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import({Blacklist.class})
public class BlacklistRepoTest {
    @Autowired
    private BlacklistRepo blacklistRepo;

    private BlacklistFilterParam getParam()
    {
        return new BlacklistFilterParam();
    }

    @Test
    public void testBlacklistFilterWithDefaultSetting()
    {
        BlacklistFilterParam param = getParam();
        Pageable pageable = getPageable(param);
        Page<Blacklist> blacklistList = this.blacklistRepo.filter(pageable);
        assertThat(blacklistList.stream().count()).isEqualTo(5L);
    }

    private static @NonNull Pageable getPageable(BlacklistFilterParam param)
    {
        Sort.Direction sortOrder = param.getOrder().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(sortOrder, param.getSortBy());
        Pageable pageable = PageRequest.of(param.getPage() - 1, param.getSize(), sort);
        return pageable;
    }

    @Test
    public void testBlacklistFilter_one()
    {
        BlacklistFilterParam param = BlacklistFilterParam.builder()
                .size(2).build();
        System.out.println(param);
        Pageable pageable = getPageable(param);
        Page<Blacklist> blacklists = this.blacklistRepo.filter(pageable);
        assertThat(blacklists.stream().count()).isEqualTo(2L);
    }
}
