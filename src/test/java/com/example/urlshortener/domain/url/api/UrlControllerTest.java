package com.example.urlshortener.domain.url.api;

import com.example.urlshortener.domain.url.application.UrlService;
import com.example.urlshortener.domain.url.dto.ShortenUrlForMeRequest;
import com.example.urlshortener.domain.url.dto.ShortenUrlRequest;
import com.example.urlshortener.domain.url.dto.ShortenUrlResponse;
import com.example.urlshortener.domain.url.dto.ShortenUrlsResponse;
import com.example.urlshortener.domain.url.exception.UrlExpiredException;
import com.example.urlshortener.test.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.net.URI;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
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
    @DisplayName("로그인이 된 상태에서의 URL Shorten 요청을 처리한다.")
    void shortenUrlForMe() throws Exception {
        // given
        ShortenUrlForMeRequest request = ShortenUrlForMeRequest.builder()
                .memberId(1L)
                .fullUrl("www.test.com")
                .build();

        ShortenUrlResponse expected = ShortenUrlResponse.builder()
                .fullUrl("www.test.com")
                .hash("hash")
                .build();

        given(urlService.shortenUrlForMe(request)).willReturn(expected);

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/me/url")
                        .characterEncoding("utf-8")
                        .header("authorization", "Bearer TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andDo(document("url-post-logged-in",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("memberId").type(JsonFieldType.NUMBER)
                                        .description("memberId").optional(),
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
        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get("/{hash}", "hash"))
                .andDo(print())
                .andDo(document("url-redirect",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("hash").description("hash value of full url")
                        ),
                        responseHeaders(
                                headerWithName("Location").description("hash 값에 대응되는 url")
                        )
                ));

        // then
        resultActions
                .andExpect(status().isSeeOther())
                .andExpect(redirectedUrl("www.test.com"));
    }

    @Test
    @DisplayName("만료된 URL Shorten Url 접근시, 예외 메시지를 담아 응답한다.")
    void redirectExpiredUrl() throws Exception {
        // given
        given(urlService.redirect("hash")).willThrow(new UrlExpiredException());

        // when
        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get("/{hash}", "hash"))
                .andDo(print())
                .andDo(document("url-redirect-expired",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("hash").description("hash value of full url")
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("오류 메시지"),
                                fieldWithPath("code").type(JsonFieldType.STRING)
                                        .description("Application error code"),
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("http status")
                        )
                ));

        // then
        resultActions
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("로그인이 된 상태에서 short url을 조회하는 요청을 처리한다.")
    void findShortenUrl() throws Exception {
        // given

        ShortenUrlResponse shortUrl_1 = ShortenUrlResponse.builder()
                .fullUrl("www.test.com")
                .hash("hash")
                .build();

        ShortenUrlResponse shortUrl_2 = ShortenUrlResponse.builder()
                .fullUrl("www.test2.com")
                .hash("hash2")
                .build();

        ShortenUrlsResponse expected = ShortenUrlsResponse.builder()
                .urls(List.of(shortUrl_1, shortUrl_2))
                .build();

        given(urlService.findAllByMemberId(1L)).willReturn(expected);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/url")
                        .param("memberId", "1")
                        .header("authorization", "Bearer TOKEN")
                )
                .andDo(print())
                .andDo(document("url-get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("memberId").description("member ID")
                        ),
                        responseFields(
                                fieldWithPath("urls").type(JsonFieldType.ARRAY)
                                        .description("url list")
                                ,
                                fieldWithPath("urls.[].fullUrl").type(JsonFieldType.STRING)
                                        .description("full URL"),
                                fieldWithPath("urls.[].hash").type(JsonFieldType.STRING)
                                        .description("hash result for shorten")
                        )
                ));

        // then
        resultActions.andExpect(status().isOk());
    }

}