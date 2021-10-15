package com.github.wz2cool.dynamic;

import com.github.wz2cool.dynamic.model.LogicPagingResult;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class LogicPagingResultTest {

    @Test
    public void testModelConvert() {
        List<UserDO> userDOS = new ArrayList<>();
        userDOS.add(new UserDO(1L, "zhangsan"));
        userDOS.add(new UserDO(2L, "lisi"));

        LogicPagingResult<UserDO> LogicPagingResult = new LogicPagingResult<>();
        LogicPagingResult.setStartPageId(1);
        LogicPagingResult.setEndPageId(2);
        LogicPagingResult.setPageSize(1);
        LogicPagingResult.setHasNextPage(true);
        LogicPagingResult.setHasPreviousPage(false);
        LogicPagingResult.setList(userDOS);

        LogicPagingResult<UserDTO> userDTOLogicPagingResult = LogicPagingResult.convert(u -> {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(u.getId());
            userDTO.setName(u.getName());
            userDTO.setMixContent(u.getId() + u.getName());
            return userDTO;
        });

        Assert.assertEquals(userDTOLogicPagingResult.getList().get(0).getMixContent(), "1zhangsan");
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
