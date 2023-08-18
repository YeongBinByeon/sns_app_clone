package com.example.sns.controller.response;

import com.example.sns.model.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class PostCreateResponse {

    private Integer id;
    private String title;
    private String body;

    public static PostCreateResponse fromPost(Post post){
        return new PostCreateResponse(
                post.getId(),
                post.getTitle(),
                post.getBody()
        );
    }
}
