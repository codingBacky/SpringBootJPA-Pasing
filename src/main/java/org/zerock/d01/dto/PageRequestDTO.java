package org.zerock.d01.dto;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.d01.domain.BaseEntity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PageRequestDTO extends BaseEntity {
    @Builder.Default
    private int page = 1;
    @Builder.Default
    private int size = 10;
    private String type;
    private String keyword;

    public String[] getTypes(){
        if(type == null || type.isEmpty()){
            return null;
        }
        return type.split("");
    }
    public Pageable getPageable(String ...props){//props 2차 정렬 조건
        return PageRequest.of(this.page-1, this.size, Sort.by(props).descending());
    }
    public String link;
    public String getLink(){
        if(link == null){
            StringBuilder builder = new StringBuilder();
            builder.append("page=" + this.page);
            builder.append("&size=" + this.size);

            if(type != null && type.isEmpty()){
                builder.append("&type=" + type);
            }

            if(keyword != null){
                try {
                    builder.append("&keyword=" + URLEncoder.encode(keyword, "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
            link = builder().toString();//page=1&size=10&type=tcw&keyword=
        }
        return link;
    }
}
