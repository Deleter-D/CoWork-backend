package org.only.cowork.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * This class is a result template class that returns data to the front end in a uniform format.
 *
 * @author WangYanpeng
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class ResponseTemplate<T> {
    private int code;

    private T data;
}
