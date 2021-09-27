package com.github.wz2cool.dynamic;

import com.github.wz2cool.dynamic.model.NormPagingResult;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class NormPagingResultTest {

    @Test
    public void testModelConvert() {
        List<UserDO> userDOS = new ArrayList<>();
        userDOS.add(new UserDO(1L, "zhangsan"));
        userDOS.add(new UserDO(2L, "lisi"));

        NormPagingResult<UserDO> normPagingResult = new NormPagingResult<>();
        normPagingResult.setPageNum(1);
        normPagingResult.setPageSize(1);
        normPagingResult.setHasNextPage(true);
        normPagingResult.setHasPreviousPage(false);
        normPagingResult.setList(userDOS);

        NormPagingResult<UserDTO> userDTONormPagingResult = normPagingResult.convert(u -> {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(u.getId());
            userDTO.setName(u.getName());
            userDTO.setMixContent(u.getId() + u.getName());
            return userDTO;
        });

        Assert.assertEquals(userDTONormPagingResult.getList().get(0).getMixContent(), "1zhangsan");
    }

    class UserDO {
        private Long id;
        private String name;

        public UserDO(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    class UserDTO {
        private Long id;
        private String name;
        private String mixContent;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMixContent() {
            return mixContent;
        }

        public void setMixContent(String mixContent) {
            this.mixContent = mixContent;
        }
    }
}
