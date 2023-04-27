package cn.homyit.entity.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @program: graduate-website
 * @description:
 * @author: Charon
 * @create: 2023-03-28 16:40
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageVo<T> {
    private List<T> rows;
    private Long total;
}