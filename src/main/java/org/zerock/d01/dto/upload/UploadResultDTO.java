package org.zerock.d01.dto.upload;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadResultDTO {
    private String uuid;
    private String fileName;
    private boolean img;
    private String getLink(){
        if(img){
            return "s_"+uuid+"_fileName";
        }else{
            return uuid +"_fileName";
        }
    }
}
