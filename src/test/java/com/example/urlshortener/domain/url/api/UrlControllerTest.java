package com.example.urlshortener.domain.url.api;

import com.example.urlshortener.domain.url.application.UrlService;
import com.example.urlshortener.domain.url.dto.ShortenUrlRequest;
import com.example.urlshortener.domain.url.dto.ShortenUrlResponse;
import com.example.urlshortener.test.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.net.URI;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UrlController.class)
class UrlControllerTest extends ControllerTest {

    @MockBean
    private UrlService urlService;

    @Test
    @DisplayName("URL Shorten 요청을 처리한다.")
    void shortenUrl() throws Exception {
        // given
        ShortenUrlRequest request = ShortenUrlRequest.builder()
                .fullUrl("www.test.com")
                .build();

        ShortenUrlResponse expected = ShortenUrlResponse.builder()
                .fullUrl("www.test.com")
                .hash("hash")
                .build();

        given(urlService.shortenUrl(request)).willReturn(expected);

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/url")
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andDo(document("url-post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("fullUrl").type(JsonFieldType.STRING)
                                        .description("full URL")
                        ),
                        responseFields(
                                fieldWithPath("fullUrl").type(JsonFieldType.STRING)
                                        .description("full URL"),
                                fieldWithPath("hash").type(JsonFieldType.STRING)
                                        .description("hash result for shorten")

                        )
                ));

        // then
        resultActions.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("URL Shorten Url 접근 시, 저장되어 있는 full URL로 redirect 한다.")
    void redirect() throws Exception {
        // given
        given(urlService.redirect("hash")).willReturn(new URI("www.test.com"));

        // when
        ResultActions resultActions = mockMvc.perform(get("/hash"))
                .andDo(print())
                .andDo(document("url-redirect",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        // then
        resultActions
                .andExpect(status().isSeeOther())
                .andExpect(redirectedUrl("www.test.com"));
    }

}