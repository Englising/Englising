package org.englising.com.englisingbe.singleplay.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WordCheckRequestDto {
    public Long singleplayId;
    public Long singleplayWordId;
    public String word;
}
